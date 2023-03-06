/*
 * Copyright (C) 2023 eccentric_nz
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

import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.TARDISTimeRotor;
import me.eccentric_nz.TARDIS.control.TARDISScannerMap;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.Control;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author eccentric_nz
 */
public class TARDISItemFrameUpdateListener implements Listener {

    private final TARDIS plugin;

    public TARDISItemFrameUpdateListener(TARDIS plugin) {
        this.plugin = plugin;

    }

    @EventHandler(ignoreCancelled = true)
    public void onItemFrameUpdate(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof ItemFrame frame) {
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();
            // did they run the `/tardis update direction|frame|rotor|map|monitor|monitor_frame` command?
            if (plugin.getTrackerKeeper().getUpdatePlayers().containsKey(uuid)) {
                Control control;
                try {
                    control = Control.valueOf(plugin.getTrackerKeeper().getUpdatePlayers().get(uuid).toUpperCase());
                } catch (IllegalArgumentException e) {
                    TARDISMessage.send(player, "UPDATE_BAD_CLICK", plugin.getTrackerKeeper().getUpdatePlayers().get(uuid));
                    return;
                }
                if (control.equals(Control.DIRECTION) || control.equals(Control.FRAME) || control.equals(Control.ROTOR) || control.equals(Control.MAP) || control.equals(Control.MONITOR) || control.equals(Control.MONITOR_FRAME)) {
                    // check they have a TARDIS
                    ResultSetTardisID rst = new ResultSetTardisID(plugin);
                    if (!rst.fromUUID(uuid.toString())) {
                        TARDISMessage.send(player, "NO_TARDIS");
                        return;
                    }
                    int id = rst.getTardis_id();
                    switch (control) {
                        case DIRECTION, FRAME, MAP, MONITOR, MONITOR_FRAME -> {
                            if (control.equals(Control.MAP) && !TARDISPermission.hasPermission(player, "tardis.scanner.map")) {
                                plugin.getTrackerKeeper().getUpdatePlayers().remove(uuid);
                                TARDISMessage.send(player, "NO_PERM_MAP");
                                return;
                            }
                            if (control.equals(Control.MAP) || control.equals(Control.MONITOR)) {
                                // frame must have a MAP or FILLED_MAP in it
                                ItemStack map = frame.getItem();
                                if (map.getType() != Material.MAP && map.getType() != Material.FILLED_MAP) {
                                    plugin.getTrackerKeeper().getUpdatePlayers().remove(uuid);
                                    TARDISMessage.send(player, control.equals(Control.MAP) ? "SCANNER_NO_MAP" : "MONITOR_NO_MAP");
                                    return;
                                }
                            }
                            String l = frame.getLocation().toString();
                            // check whether they have a item frame control of this type already
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
                            switch (control) {
                                case DIRECTION -> which = "Direction";
                                case FRAME -> which = "Chameleon";
                                case MAP -> {
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
                                case MONITOR -> {
                                    which = "TARDIS Monitor";
                                    // add initial snapshot
                                    // make frame invisible and fixed
                                    frame.setFixed(true);
                                    frame.setVisible(false);
                                }
                                default -> { // MONITOR_FRAME
                                    which = "Monitor Frame";
                                    frame.setFixed(true);
                                    frame.setVisible(false);
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
                }
            }
        }
    }
}
