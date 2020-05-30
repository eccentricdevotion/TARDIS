/*
 * Copyright (C) 2020 eccentric_nz
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
import me.eccentric_nz.TARDIS.builders.TARDISTimeRotor;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTardisID;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISItemFrameListener implements Listener {

    private final TARDIS plugin;

    public TARDISItemFrameListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemFrameClick(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (event.getRightClicked() instanceof ItemFrame) {
            UUID uuid = player.getUniqueId();
            // did they run the `/tardis update direction` command?
            if (plugin.getTrackerKeeper().getPlayers().containsKey(uuid)) {
                String control = plugin.getTrackerKeeper().getPlayers().get(uuid);
                if (control.equalsIgnoreCase("DIRECTION") || control.equalsIgnoreCase("FRAME") || control.equalsIgnoreCase("ROTOR")) {
                    // check they have a TARDIS
                    ResultSetTardisID rst = new ResultSetTardisID(plugin);
                    if (!rst.fromUUID(uuid.toString())) {
                        TARDISMessage.send(player, "NO_TARDIS");
                        return;
                    }
                    int id = rst.getTardis_id();
                    if (control.equalsIgnoreCase("DIRECTION") || control.equalsIgnoreCase("FRAME")) {
                        String l = event.getRightClicked().getLocation().toString();
                        // check whether they have a direction item frame already
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("location", l);
                        where.put("type", control.equalsIgnoreCase("DIRECTION") ? 18 : 27);
                        ResultSetControls rsc = new ResultSetControls(plugin, where, false);
                        HashMap<String, Object> set = new HashMap<>();
                        if (rsc.resultSet()) {
                            // update location
                            set.put("location", l);
                            HashMap<String, Object> whereu = new HashMap<>();
                            whereu.put("tardis_id", id);
                            whereu.put("type", control.equalsIgnoreCase("DIRECTION") ? 18 : 27);
                            plugin.getQueryFactory().doUpdate("controls", set, whereu);
                        } else {
                            // add control
                            plugin.getQueryFactory().insertControl(id, control.equalsIgnoreCase("DIRECTION") ? 18 : 27, l, 0);
                        }
                        plugin.getTrackerKeeper().getPlayers().remove(uuid);
                        TARDISMessage.send(player, control.equalsIgnoreCase("DIRECTION") ? "DIRECTION_UPDATE" : "CHAM_UPDATE");
                    } else {
                        TARDISTimeRotor.updateRotorRecord(id, event.getRightClicked().getUniqueId().toString());
                        TARDISMessage.send(player, "ROTOR_UPDATE");
                    }
                    return;
                }
            }
            ItemFrame frame = (ItemFrame) event.getRightClicked();
            // if the item frame has a tripwire hook in it
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
                        switch (frame.getRotation()) {
                            case FLIPPED:
                                direction = "NORTH";
                                break;
                            case COUNTER_CLOCKWISE:
                                direction = "EAST";
                                break;
                            case NONE:
                                direction = "SOUTH";
                                break;
                            default:
                                direction = "WEST";
                                break;
                        }
                        player.performCommand("tardis direction " + direction);
                        plugin.getConsole().sendMessage(player.getName() + " issued server command: /tardis direction " + direction);
                    } else {
                        Rotation r;
                        // set the rotation
                        switch (frame.getRotation()) {
                            case FLIPPED:
                                r = Rotation.FLIPPED_45;
                                direction = "EAST";
                                break;
                            case COUNTER_CLOCKWISE:
                                r = Rotation.COUNTER_CLOCKWISE_45;
                                direction = "SOUTH";
                                break;
                            case NONE:
                                r = Rotation.CLOCKWISE_45;
                                direction = "WEST";
                                break;
                            default:
                                r = Rotation.CLOCKWISE_135;
                                direction = "NORTH";
                                break;
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
                                Rotation r;
                                switch (rscl.getDirection()) {
                                    case EAST:
                                        r = Rotation.COUNTER_CLOCKWISE;
                                        break;
                                    case SOUTH:
                                        r = Rotation.NONE;
                                        break;
                                    case WEST:
                                        r = Rotation.CLOCKWISE;
                                        break;
                                    default:
                                        r = Rotation.FLIPPED;
                                        break;
                                }
                                frame.setRotation(r);
                                TARDISMessage.send(player, "DIRECTION_CURRENT", rscl.getDirection().toString());
                            }, 4L);
                        }
                    }
                }
            }
        }
    }
}
