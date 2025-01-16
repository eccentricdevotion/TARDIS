/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.control.TARDISScannerMap;
import me.eccentric_nz.TARDIS.custommodels.keys.SonicItem;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.Control;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.monitor.MonitorSnapshot;
import me.eccentric_nz.TARDIS.monitor.MonitorUtils;
import me.eccentric_nz.TARDIS.monitor.Snapshot;
import me.eccentric_nz.TARDIS.rotors.TARDISTimeRotor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * @author eccentric_nz
 */
public class TARDISItemFrameUpdateListener implements Listener {

    private final TARDIS plugin;
    private final Set<Control> onlyThese = new HashSet<>();

    public TARDISItemFrameUpdateListener(TARDIS plugin) {
        this.plugin = plugin;
        onlyThese.add(Control.DIRECTION);
        onlyThese.add(Control.FRAME);
        onlyThese.add(Control.MAP);
        onlyThese.add(Control.MONITOR);
        onlyThese.add(Control.MONITOR_FRAME);
        onlyThese.add(Control.ROTOR);
        onlyThese.add(Control.SONIC_DOCK);
        onlyThese.add(Control.EXTERIOR_LAMP);
        onlyThese.add(Control.LIGHT_LEVEL);
        onlyThese.add(Control.CONSOLE_LAMP_SWITCH);
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemFrameUpdate(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof ItemFrame frame) {
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();
            // did they run the `/tardis update direction|frame|rotor|map|monitor|monitor_frame` command?
            if (plugin.getTrackerKeeper().getUpdatePlayers().containsKey(uuid)) {
                event.setCancelled(true);
                Control control;
                try {
                    control = Control.valueOf(plugin.getTrackerKeeper().getUpdatePlayers().get(uuid).toUpperCase(Locale.ROOT));
                } catch (IllegalArgumentException e) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_BAD_CLICK", plugin.getTrackerKeeper().getUpdatePlayers().get(uuid));
                    return;
                }
                if (onlyThese.contains(control)) {
                    // check they have a TARDIS
                    ResultSetTardisID rst = new ResultSetTardisID(plugin);
                    if (!rst.fromUUID(uuid.toString())) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
                        return;
                    }
                    int id = rst.getTardisId();
                    if (control == Control.ROTOR) {
                        UUID rotorId = frame.getUniqueId();
                        TARDISTimeRotor.updateRotorRecord(id, rotorId.toString());
                        plugin.getGeneralKeeper().getTimeRotors().add(rotorId);
                        // set fixed and invisible
                        frame.setFixed(true);
                        frame.setVisible(false);
                        plugin.getTrackerKeeper().getUpdatePlayers().remove(uuid);
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "ROTOR_UPDATE");
                    } else {
                        if (control.equals(Control.MAP) && !TARDISPermission.hasPermission(player, "tardis.scanner.map")) {
                            plugin.getTrackerKeeper().getUpdatePlayers().remove(uuid);
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_MAP");
                            return;
                        }
                        Location l = frame.getLocation();
                        plugin.getTrackerKeeper().getUpdatePlayers().remove(uuid);
                        String which;
                        switch (control) {
                            case DIRECTION -> which = "Direction";
                            case FRAME -> which = "Chameleon";
                            case MAP -> {
                                // frame must have a MAP or FILLED_MAP in it
                                ItemStack map = frame.getItem();
                                if (map.getType() != Material.MAP && map.getType() != Material.FILLED_MAP) {
                                    plugin.getTrackerKeeper().getUpdatePlayers().remove(uuid);
                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SCANNER_NO_MAP");
                                    return;
                                }
                                which = "Scanner Map";
                                // place a map
                                ResultSetCurrentFromId rscl = new ResultSetCurrentFromId(plugin, id);
                                if (rscl.resultSet()) {
                                    Location scan_loc = new Location(rscl.getWorld(), rscl.getX(), rscl.getY(), rscl.getZ());
                                    new TARDISScannerMap(TARDIS.plugin, scan_loc, frame).setMap();
                                }
                            }
                            case MONITOR -> {
                                ItemStack map = frame.getItem();
                                // does it have a TARDIS Monitor map?
                                if (map.getType() == Material.MAP && map.hasItemMeta() && map.getItemMeta().hasItemModel()) {
                                    // do nothing
                                } else {
                                    plugin.getTrackerKeeper().getUpdatePlayers().remove(uuid);
                                    plugin.getMessenger().sendColouredCommand(player, "MONITOR_PLACE_MAP", "/trecipe monitor", plugin);
                                    return;
                                }
                                // get door location
                                Snapshot snapshot = MonitorUtils.getLocationAndDirection(id, false);
                                Location door = snapshot.getLocation();
                                if (door != null) {
                                    // load chunks
                                    MonitorSnapshot.loadChunks(plugin, door, false, snapshot.getDirection(), id, 128);
                                    // update the map
                                    ItemStack filled = MonitorUtils.createMap(door, 128);
                                    frame.setItem(filled);
                                    frame.setRotation(Rotation.NONE);
                                }
                                // make frame invisible and fixed
                                frame.setFixed(true);
                                frame.setVisible(false);
                                which = "TARDIS Monitor";
                            }
                            case MONITOR_FRAME -> {
                                ItemStack glass = frame.getItem();
                                Rotation rotation = frame.getRotation();
                                // does it have a Monitor frame?
                                if (glass.getType() == Material.GLASS && glass.hasItemMeta() && glass.getItemMeta().hasItemModel()) {
                                    // remove display name
                                    ItemMeta gm = glass.getItemMeta();
                                    gm.setDisplayName(null);
                                    glass.setItemMeta(gm);
                                    // get the monitor item frame, from the same block location
                                    ItemFrame mapFrame = MonitorUtils.getItemFrameFromLocation(l, frame.getUniqueId());
                                    if (mapFrame != null) {
                                        // does it have a filled map?
                                        ItemStack map = mapFrame.getItem();
                                        if (map.getType() == Material.FILLED_MAP) {
                                            // get door location
                                            Snapshot snapshot = MonitorUtils.getLocationAndDirection(id, false);
                                            Location door = snapshot.getLocation();
                                            if (door != null) {
                                                // load chunks
                                                MonitorSnapshot.loadChunks(plugin, door, false, snapshot.getDirection(), id, 128);
                                                // update the map
                                                MonitorUtils.updateSnapshot(door, 128, map);
                                                frame.setItem(glass);
                                                frame.setRotation(rotation);
                                                frame.setFixed(true);
                                                frame.setVisible(false);
                                            } else {
                                                plugin.debug("door was null");
                                            }
                                        } else {
                                            // they haven't placed/updated a monitor first
                                            plugin.getTrackerKeeper().getUpdatePlayers().remove(uuid);
                                            plugin.getMessenger().send(player, TardisModule.TARDIS, "MONITOR_PLACE_FIRST");
                                            return;
                                        }
                                    } else {
                                        // they haven't placed/updated a monitor first
                                        plugin.getTrackerKeeper().getUpdatePlayers().remove(uuid);
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "MONITOR_PLACE_FIRST");
                                        return;
                                    }
                                } else {
                                    plugin.getTrackerKeeper().getUpdatePlayers().remove(uuid);
                                    plugin.getMessenger().sendColouredCommand(player, "MONITOR_PLACE_FRAME", "/trecipe monitor-frame", plugin);
                                    return;
                                }
                                which = "Monitor Frame";
                            }
                            case SONIC_DOCK -> {
                                if (!isDock(frame)) {
                                    // they haven't placed a dock in the frame first
                                    plugin.getTrackerKeeper().getUpdatePlayers().remove(uuid);
                                    plugin.getMessenger().sendColouredCommand(player, "DOCK_PLACE_FRAME", "/trecipe sonic-dock", plugin);
                                    return;
                                }
                                frame.setFixed(true);
                                frame.setVisible(false);
                                plugin.getTrackerKeeper().getUpdatePlayers().remove(uuid);
                                which = "Sonic Dock";
                            }
                            default -> { // EXTERIOR_LAMP, LIGHT_LEVEL && CONSOLE_LAMP_SWITCH
                                SwitchPair sp = isLevelSwitch(frame);
                                if (!sp.isSwitch()) {
                                    String what = switch (control) {
                                        case Control.EXTERIOR_LAMP -> "exterior-lamp-level-switch";
                                        case Control.LIGHT_LEVEL -> "interior-light-level-switch";
                                        default -> "console-lamp-switch";
                                    };
                                    // they haven't placed a level switch in the frame first
                                    plugin.getTrackerKeeper().getUpdatePlayers().remove(uuid);
                                    plugin.getMessenger().sendColouredCommand(player, "LIGHT_LEVEL_PLACE_FRAME", "/trecipe " + what, plugin);
                                    return;
                                }
                                frame.setFixed(true);
                                frame.setVisible(false);
                                // set display to shorter version
                                ItemMeta im = sp.getLamp().getItemMeta();
                                String dn = switch (control) {
                                    case Control.EXTERIOR_LAMP -> "Lamp";
                                    case Control.LIGHT_LEVEL -> "Light";
                                    default -> "Console";
                                };
                                im.setDisplayName(dn);
                                sp.getLamp().setItemMeta(im);
                                frame.setItem(sp.getLamp());
                                plugin.getTrackerKeeper().getUpdatePlayers().remove(uuid);
                                which = switch (control) {
                                    case Control.EXTERIOR_LAMP -> "Exterior Lamp Level Switch";
                                    case Control.LIGHT_LEVEL -> "Interior Light Level Switch";
                                    default -> "Console Lamp Switch";
                                };
                            }
                        }
                        // check whether they have an item frame control of this type already
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("location", l.toString());
                        where.put("type", control.getId());
                        ResultSetControls rsc = new ResultSetControls(plugin, where, false);
                        HashMap<String, Object> set = new HashMap<>();
                        if (rsc.resultSet()) {
                            // update location
                            set.put("location", l.toString());
                            HashMap<String, Object> whereu = new HashMap<>();
                            whereu.put("tardis_id", id);
                            whereu.put("type", control.getId());
                            plugin.getQueryFactory().doUpdate("controls", set, whereu);
                        } else {
                            // add control
                            plugin.getQueryFactory().insertControl(id, control.getId(), l.toString(), 0);
                        }
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "FRAME_UPDATE", which);
                    }
                }
            }
        }
    }

    private boolean isDock(ItemFrame frame) {
        ItemStack dock = frame.getItem();
        if (dock.getType() != Material.FLOWER_POT || !dock.hasItemMeta()) {
            return false;
        }
        ItemMeta im = dock.getItemMeta();
        return im.hasItemModel() && (im.getItemModel().getKey().contains("sonic_dock"));
    }

    private SwitchPair isLevelSwitch(ItemFrame frame) {
        ItemStack lampSwitch = frame.getItem();
        if (lampSwitch.getType() != Material.LEVER || !lampSwitch.hasItemMeta()) {
            return new SwitchPair(false, lampSwitch);
        }
        ItemMeta im = lampSwitch.getItemMeta();
        if (!im.hasDisplayName()) {
            return new SwitchPair(false, lampSwitch);
        }
        return new SwitchPair(im.hasItemModel() && im.getDisplayName().endsWith("Switch"), lampSwitch);
    }

    private class SwitchPair {

        final boolean b;
        final ItemStack lamp;

        public SwitchPair(boolean b, ItemStack lamp) {
            this.b = b;
            this.lamp = lamp;
        }

        public boolean isSwitch() {
            return b;
        }

        public ItemStack getLamp() {
            return lamp;
        }
    }
}
