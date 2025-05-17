/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.listeners.controls;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodels.keys.Whoniverse;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetProgram;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.handles.TARDISHandlesProcessor;
import me.eccentric_nz.TARDIS.handles.TARDISHandlesProgramInventory;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

/**
 * @author eccentric_nz
 */
public class TARDISHandlesFrameListener implements Listener {

    private final TARDIS plugin;
    private final List<Integer> talkingHandles = new ArrayList<>();

    public TARDISHandlesFrameListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onHandlesFrameClick(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof ItemFrame frame) {
            String l = frame.getLocation().toString();
            Player player = event.getPlayer();
            // is it an already registered handles frame?
            HashMap<String, Object> whereh = new HashMap<>();
            whereh.put("location", l);
            whereh.put("type", 26);
            ResultSetControls rsh = new ResultSetControls(plugin, whereh, false);
            if (rsh.resultSet()) {
                if (!TARDISPermission.hasPermission(player, "tardis.handles.use")) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
                    return;
                }
                ItemStack is = frame.getItem();
                if (isHandles(is)) {
                    // cancel item rotation unless player has run `/tardis update handles rotate`
                    if (!plugin.getTrackerKeeper().getHandlesRotation().contains(player.getUniqueId())) {
                        event.setCancelled(true);
                    }
                    Integer handlesId = rsh.getTardis_id();
                    // play sound
                    talkingHandles.add(handlesId); // add this handles to the list of currently talking handleses (by tardis id)
                    TARDISSounds.playTARDISSound(player, "handles", 5L);
                    ItemMeta im = is.getItemMeta();
                    im.setItemModel(Whoniverse.HANDLES_ON.getKey());
                    is.setItemMeta(im);
                    frame.setItem(is, false);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        talkingHandles.remove(handlesId); // remove this handles from the list of talking handles
                        im.setItemModel(Whoniverse.HANDLES_OFF.getKey());
                        is.setItemMeta(im);
                        frame.setItem(is, false);
                    }, 20L);
                    if (!TARDISPermission.hasPermission(player, "tardis.handles.program")) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
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
                        if (disk.getType().equals(Material.MUSIC_DISC_WARD) && disk.hasItemMeta()) {
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
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
                        return;
                    }
                    // cannot place unless inside the TARDIS
                    if (!plugin.getUtils().inTARDISWorld(event.getPlayer())) {
                        plugin.getMessenger().handlesSend(player, "HANDLES_TARDIS");
                        event.setCancelled(true);
                        return;
                    }
                    // must have a TARDIS
                    ResultSetTardisID rst = new ResultSetTardisID(plugin);
                    if (rst.fromUUID(player.getUniqueId().toString())) {
                        // check if they have a handles block
                        HashMap<String, Object> wherec = new HashMap<>();
                        wherec.put("tardis_id", rst.getTardisId());
                        wherec.put("type", 26);
                        ResultSetControls rsc = new ResultSetControls(plugin, wherec, false);
                        if (!rsc.resultSet()) {
                            String newLocation = frame.getLocation().toString();
                            plugin.getQueryFactory().insertControl(rst.getTardisId(), 26, newLocation, 0);
                        } else {
                            event.setCancelled(true);
                            plugin.getMessenger().send(event.getPlayer(), TardisModule.TARDIS, "HANDLES_PLACED");
                        }
                    } else {
                        plugin.getMessenger().handlesSend(player, "HANDLES_NO_COMMAND");
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
            if (event.getDamager() instanceof Player player) {
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
                    if (player.isSneaking()) {
                        talkingHandles.add(handlesId); // add this handles to the list of currently talking handleses (by tardis id)
                        event.setCancelled(true);
                        TARDISSounds.playTARDISSound(player, "handles", 5L);
                        ItemMeta im = is.getItemMeta();
                        im.setItemModel(Whoniverse.HANDLES_ON.getKey());
                        is.setItemMeta(im);
                        frame.setItem(is, false);
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            talkingHandles.remove(handlesId); // remove this handles from the list of talking handles
                            im.setItemModel(Whoniverse.HANDLES_OFF.getKey());
                            is.setItemMeta(im);
                            frame.setItem(is, false);
                        }, 40L);
                    } else {
                        // is the handles currently talking?
                        // match by predicate in case multiple entries are in list. can happen when
                        // handles is clicked many times repeatedly
                        if (talkingHandles.stream().anyMatch(id -> id.equals(handlesId))) {
                            event.setCancelled(true);
                            plugin.debug(String.format("Cancelling breaking handles ID %d because he is still talking",
                                    handlesId));
                            return;
                        }
                        // is it the players handles?
                        ResultSetTardisID rst = new ResultSetTardisID(plugin);
                        if (rst.fromUUID(player.getUniqueId().toString()) && rsc.getTardis_id() == rst.getTardisId()) {
                            // remove control record
                            HashMap<String, Object> wherec = new HashMap<>();
                            wherec.put("c_id", rsc.getC_id());
                            plugin.getQueryFactory().doDelete("controls", wherec);
                        } else {
                            event.setCancelled(true);
                        }
                    }
                } else if (plugin.getGeneralKeeper().getProtectBlockMap().containsKey(event.getEntity().getLocation().getBlock().getLocation().toString())) {
                    event.setCancelled(true);
                } else if (is.getType() == Material.FILLED_MAP) {
                    // check location
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("type", 37);
                    where.put("location", frame.getLocation().toString());
                    ResultSetControls rsc = new ResultSetControls(plugin, where, false);
                    if (!rsc.resultSet()) {
                        return;
                    }
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SCANNER_MAP");
                }
            } else {
                event.setCancelled(true);
            }
        }
    }

    private boolean isHandles(ItemStack is) {
        if (is != null && is.getType().equals(Material.BIRCH_BUTTON) && is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            return im.hasDisplayName() && im.getDisplayName().endsWith("Handles");
        }
        return false;
    }
}
