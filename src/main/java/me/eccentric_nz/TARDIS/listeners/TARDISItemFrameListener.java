/*
 * Copyright (C) 2022 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.TARDISTimeRotor;
import me.eccentric_nz.TARDIS.control.TARDISScannerMap;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.Control;
import me.eccentric_nz.TARDIS.handles.TARDISHandlesProcessor;
import me.eccentric_nz.TARDIS.handles.TARDISHandlesProgramInventory;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/**
 * @author eccentric_nz
 */
public class TARDISItemFrameListener implements Listener {

    private final TARDIS plugin;
    private final List<Integer> talkingHandles = new ArrayList<>();

    public TARDISItemFrameListener(TARDIS plugin) {
        this.plugin = plugin;

    }

    @EventHandler(ignoreCancelled = true)
    public void onItemFrameClick(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (event.getRightClicked() instanceof ItemFrame frame) {
            UUID uuid = player.getUniqueId();
            // did they run the `/tardis update direction|frame|rotor|map` command?
            if (plugin.getTrackerKeeper().getUpdatePlayers().containsKey(uuid)) {
                Control control;
                try {
                    control = Control.valueOf(plugin.getTrackerKeeper().getUpdatePlayers().get(uuid).toUpperCase());
                } catch (IllegalArgumentException e) {
                    TARDISMessage.send(player, "UPDATE_BAD_CLICK", plugin.getTrackerKeeper().getUpdatePlayers().get(uuid));
                    return;
                }
                if (control.equals(Control.DIRECTION) || control.equals(Control.FRAME) || control.equals(Control.ROTOR) || control.equals(Control.MAP)) {
                    // check they have a TARDIS
                    ResultSetTardisID rst = new ResultSetTardisID(plugin);
                    if (!rst.fromUUID(uuid.toString())) {
                        TARDISMessage.send(player, "NO_TARDIS");
                        return;
                    }
                    int id = rst.getTardis_id();
                    switch (control) {
                        case DIRECTION, FRAME, MAP -> {
                            if (control.equals(Control.MAP) && !TARDISPermission.hasPermission(player, "tardis.scanner.map")) {
                                plugin.getTrackerKeeper().getUpdatePlayers().remove(uuid);
                                TARDISMessage.send(player, "NO_PERM_MAP");
                                return;
                            }
                            if (control.equals(Control.MAP)) {
                                // frame must have a MAP or FILLED_MAP in it
                                ItemStack map = frame.getItem();
                                if (map.getType() != Material.MAP && map.getType() != Material.FILLED_MAP) {
                                    plugin.getTrackerKeeper().getUpdatePlayers().remove(uuid);
                                    TARDISMessage.send(player, "SCANNER_NO_MAP");
                                    return;
                                }
                            }
                            String l = frame.getLocation().toString();
                            // check whether they have a direction item frame already
                            HashMap<String, Object> where = new HashMap<>();
                            where.put("location", l);
                            where.put("type", control.getId());
                            ResultSetControls rsc = new ResultSetControls(plugin, where, false);
                            HashMap<String, Object> set = new HashMap<>();
                            if (rsc.resultSet()) {
                                // update location
                                set.put("location", l);
                                HashMap<String, Object> whereu = new HashMap<>();
                                whereu.put("tardis_id", id);
                                whereu.put("type", control.getId());
                                plugin.getQueryFactory().doUpdate("controls", set, whereu);
                            } else {
                                // add control
                                plugin.getQueryFactory().insertControl(id, control.getId(), l, 0);
                            }
                            plugin.getTrackerKeeper().getUpdatePlayers().remove(uuid);
                            String which;
                            if (control.equals(Control.DIRECTION)) {
                                which = "Direction";
                            } else if (control.equals(Control.FRAME)) {
                                which = "Chameleon";
                            } else {
                                which = "Scanner Map";
                                // place a map
                                HashMap<String, Object> wherec = new HashMap<>();
                                wherec.put("tardis_id", id);
                                ResultSetCurrentLocation rscl = new ResultSetCurrentLocation(plugin, wherec);
                                if (rscl.resultSet()) {
                                    Location scan_loc = new Location(rscl.getWorld(), rscl.getX(), rscl.getY(), rscl.getZ());
                                    new TARDISScannerMap(TARDIS.plugin, scan_loc, frame).setMap();
                                }
                            }
                            TARDISMessage.send(player, "FRAME_UPDATE", which);
                        }
                        default -> {
                            // ROTOR
                            UUID rotorId = frame.getUniqueId();
                            TARDISTimeRotor.updateRotorRecord(id, rotorId.toString());
                            plugin.getGeneralKeeper().getTimeRotors().add(rotorId);
                            // set fixed and invisible
                            frame.setFixed(true);
                            frame.setVisible(false);
                            plugin.getTrackerKeeper().getUpdatePlayers().remove(uuid);
                            TARDISMessage.send(player, "ROTOR_UPDATE");
                        }
                    }
                    return;
                }
            }
            // check if it is a TARDIS direction item frame
            String l = frame.getLocation().toString();
            HashMap<String, Object> where = new HashMap<>();
            where.put("location", l);
            where.put("type", 18);
            ResultSetControls rs = new ResultSetControls(plugin, where, false);
            if (rs.resultSet()) {
                // it's a TARDIS direction frame
                int id = rs.getTardis_id();
                // prevent other players from stealing the tripwire hook
                HashMap<String, Object> wherep = new HashMap<>();
                wherep.put("tardis_id", id);
                ResultSetTardis rso = new ResultSetTardis(plugin, wherep, "", false, 2);
                if (rso.resultSet() && !rso.getTardis().getUuid().equals(uuid)) {
                    event.setCancelled(true);
                    return;
                }
                // if the item frame has a tripwire hook in it
                if (frame.getItem().getType().equals(Material.TRIPWIRE_HOOK)) {
                    if (plugin.getConfig().getBoolean("allow.power_down") && !rso.getTardis().isPowered_on()) {
                        TARDISMessage.send(player, "POWER_DOWN");
                        return;
                    }
                    String direction;
                    if (player.isSneaking()) {
                        // cancel the rotation!
                        event.setCancelled(true);
                        // perform the rotation
                        direction = switch (frame.getRotation()) {
                            case FLIPPED -> "NORTH";
                            case COUNTER_CLOCKWISE -> "EAST";
                            case NONE -> "SOUTH";
                            default -> "WEST";
                        };
                        player.performCommand("tardis direction " + direction);
                        plugin.getLogger().log(Level.INFO, player.getName() + " issued server command: /tardis direction " + direction);
                    } else {
                        Rotation r;
                        // set the rotation
                        switch (frame.getRotation()) {
                            case FLIPPED -> {
                                r = Rotation.FLIPPED_45;
                                direction = "EAST";
                            }
                            case COUNTER_CLOCKWISE -> {
                                r = Rotation.COUNTER_CLOCKWISE_45;
                                direction = "SOUTH";
                            }
                            case NONE -> {
                                r = Rotation.CLOCKWISE_45;
                                direction = "WEST";
                            }
                            default -> {
                                r = Rotation.CLOCKWISE_135;
                                direction = "NORTH";
                            }
                        }
                        frame.setRotation(r);
                        TARDISMessage.send(player, "DIRECTON_SET", direction);
                    }
                } else {
                    // are they placing a tripwire hook?
                    if (frame.getItem().getType().isAir() && player.getInventory().getItemInMainHand().getType().equals(Material.TRIPWIRE_HOOK)) {
                        // get current tardis direction
                        HashMap<String, Object> wherec = new HashMap<>();
                        wherec.put("tardis_id", id);
                        ResultSetCurrentLocation rscl = new ResultSetCurrentLocation(plugin, wherec);
                        if (rscl.resultSet()) {
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                // update the TRIPWIRE_HOOK rotation
                                Rotation r = switch (rscl.getDirection()) {
                                    case EAST -> Rotation.COUNTER_CLOCKWISE;
                                    case SOUTH -> Rotation.NONE;
                                    case WEST -> Rotation.CLOCKWISE;
                                    default -> Rotation.FLIPPED;
                                };
                                frame.setRotation(r);
                                TARDISMessage.send(player, "DIRECTION_CURRENT", rscl.getDirection().toString());
                            }, 4L);
                        }
                    }
                }
            }
            // is it an already registered handles frame?
            HashMap<String, Object> whereh = new HashMap<>();
            whereh.put("location", l);
            whereh.put("type", 26);
            ResultSetControls rsh = new ResultSetControls(plugin, whereh, false);
            if (rsh.resultSet()) {
                if (!TARDISPermission.hasPermission(player, "tardis.handles.use")) {
                    TARDISMessage.send(player, "NO_PERMS");
                    return;
                }
                ItemStack is = frame.getItem();
                if (isHandles(is)) {
                    Integer handlesId = rsh.getTardis_id();
                    // play sound
                    talkingHandles.add(handlesId);    // add this handles to the list of currently talking handleses (by tardis id)
                    TARDISSounds.playTARDISSound(player, "handles", 5L);
                    ItemMeta im = is.getItemMeta();
                    im.setCustomModelData(10000002);
                    is.setItemMeta(im);
                    frame.setItem(is, false);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        talkingHandles.remove(handlesId);   // remove this handles from the list of talking handles
                        im.setCustomModelData(10000001);
                        is.setItemMeta(im);
                        frame.setItem(is, false);
                    }, 20L);
                    if (!TARDISPermission.hasPermission(player, "tardis.handles.program")) {
                        TARDISMessage.send(player, "NO_PERMS");
                        return;
                    }
                    if (player.isSneaking()) {
                        // open programming GUI
                        ItemStack[] handles = new TARDISHandlesProgramInventory(plugin, 0).getHandles();
                        Inventory hgui = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Handles Program");
                        hgui.setContents(handles);
                        player.openInventory(hgui);
                    } else {
                        // check if item in hand is a Handles program disk
                        ItemStack disk = player.getInventory().getItemInMainHand();
                        if (disk != null && disk.getType().equals(Material.MUSIC_DISC_WARD) && disk.hasItemMeta()) {
                            ItemMeta dim = disk.getItemMeta();
                            if (dim.hasDisplayName() && ChatColor.stripColor(dim.getDisplayName()).equals("Handles Program Disk")) {
                                // get the program_id from the disk
                                int pid = TARDISNumberParsers.parseInt(dim.getLore().get(1));
                                // query the database
                                ResultSetProgram rsp = new ResultSetProgram(plugin, pid);
                                if (rsp.resultSet()) {
                                    // send program to processor
                                    new TARDISHandlesProcessor(plugin, rsp.getProgram(), player, pid).processDisk();
                                    // check in the disk
                                    HashMap<String, Object> set = new HashMap<>();
                                    set.put("checked", 0);
                                    HashMap<String, Object> wherep = new HashMap<>();
                                    wherep.put("program_id", pid);
                                    plugin.getQueryFactory().doUpdate("programs", set, wherep);
                                    player.getInventory().setItemInMainHand(null);
                                }
                            }
                        }
                    }
                }
            } else {
                ItemStack is = player.getInventory().getItemInMainHand();
                if (isHandles(is)) {
                    if (!TARDISPermission.hasPermission(player, "tardis.handles.use")) {
                        TARDISMessage.send(player, "NO_PERMS");
                        return;
                    }
                    // cannot place unless inside the TARDIS
                    if (!plugin.getUtils().inTARDISWorld(event.getPlayer())) {
                        TARDISMessage.handlesSend(player, "HANDLES_TARDIS");
                        event.setCancelled(true);
                        return;
                    }
                    // must have a TARDIS
                    ResultSetTardisID rst = new ResultSetTardisID(plugin);
                    if (rst.fromUUID(uuid.toString())) {
                        // check if they have a handles block
                        HashMap<String, Object> wherec = new HashMap<>();
                        wherec.put("tardis_id", rst.getTardis_id());
                        wherec.put("type", 26);
                        ResultSetControls rsc = new ResultSetControls(plugin, wherec, false);
                        if (!rsc.resultSet()) {
                            String newLocation = frame.getLocation().toString();
                            plugin.getQueryFactory().insertControl(rst.getTardis_id(), 26, newLocation, 0);
                        } else {
                            event.setCancelled(true);
                            TARDISMessage.send(event.getPlayer(), "HANDLES_PLACED");
                        }
                    } else {
                        TARDISMessage.handlesSend(player, "HANDLES_NO_COMMAND");
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemFrameDamage(EntityDamageByEntityEvent event) {
        if (event.getEntityType().equals(EntityType.ITEM_FRAME)) {
            ItemFrame frame = (ItemFrame) event.getEntity();
            ItemStack is = frame.getItem();
            Player player = null;
            if (event.getDamager() instanceof Player) {
                player = (Player) event.getDamager();
            }
            if (isHandles(is)) {
                // check location
                HashMap<String, Object> where = new HashMap<>();
                where.put("type", 26);
                where.put("location", frame.getLocation().toString());
                ResultSetControls rsc = new ResultSetControls(plugin, where, false);
                if (!rsc.resultSet()) {
                    return;
                }
                // set this handles id to its tardis id
                Integer handlesId = rsc.getTardis_id();
                if (player != null) {
                    if (player.isSneaking()) {
                        talkingHandles.add(handlesId);    // add this handles to the list of currently talking handleses (by tardis id)
                        event.setCancelled(true);
                        TARDISSounds.playTARDISSound(player, "handles", 5L);
                        ItemMeta im = is.getItemMeta();
                        im.setCustomModelData(10000002);
                        is.setItemMeta(im);
                        frame.setItem(is, false);
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            talkingHandles.remove(handlesId);   // remove this handles from the list of talking handles
                            im.setCustomModelData(10000001);
                            is.setItemMeta(im);
                            frame.setItem(is, false);
                        }, 40L);
                    } else {
                        // is the handles currently talking?
                        // match by predicate in case multiple entries are in list. can happen when handles is clicked many times repeatedly
                        if (talkingHandles.stream().anyMatch(id -> id.equals(handlesId))) {
                            event.setCancelled(true);
                            plugin.debug(String.format("Cancelling breaking handles ID %d because he is still talking", handlesId));
                            return;
                        }
                        // is it the players handles?
                        ResultSetTardisID rst = new ResultSetTardisID(plugin);
                        if (rst.fromUUID(player.getUniqueId().toString()) && rsc.getTardis_id() == rst.getTardis_id()) {
                            // remove control record
                            HashMap<String, Object> wherec = new HashMap<>();
                            wherec.put("c_id", rsc.getC_id());
                            plugin.getQueryFactory().doDelete("controls", wherec);
                        } else {
                            event.setCancelled(true);
                        }
                    }
                } else {
                    event.setCancelled(true);
                }
            } else if (plugin.getGeneralKeeper().getProtectBlockMap().containsKey(event.getEntity().getLocation().getBlock().getLocation().toString())) {
                event.setCancelled(true);
            } else if (player != null && is.getType() == Material.FILLED_MAP) {
                // check location
                HashMap<String, Object> where = new HashMap<>();
                where.put("type", 37);
                where.put("location", frame.getLocation().toString());
                ResultSetControls rsc = new ResultSetControls(plugin, where, false);
                if (!rsc.resultSet()) {
                    return;
                }
                TARDISMessage.send(player, "SCANNER_MAP");
            }
        }
    }

    private boolean isHandles(ItemStack is) {
        if (is != null && is.getType().equals(Material.BIRCH_BUTTON) && is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            return im.hasDisplayName() && im.getDisplayName().equals("Handles");
        }
        return false;
    }
}
