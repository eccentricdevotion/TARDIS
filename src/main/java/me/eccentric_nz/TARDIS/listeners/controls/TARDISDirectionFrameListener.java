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
import java.util.logging.Level;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent; 

/**
 * @author eccentric_nz
 */
public class TARDISDirectionFrameListener implements Listener {

    private final TARDIS plugin;

    public TARDISDirectionFrameListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onDirectionFrameClick(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof ItemFrame frame) {
            // check if it is a TARDIS direction item frame
            String l = frame.getLocation().toString();
            HashMap<String, Object> where = new HashMap<>();
            where.put("location", l);
            where.put("type", 18);
            ResultSetControls rs = new ResultSetControls(plugin, where, false);
            if (rs.resultSet()) {
                // it's a TARDIS direction frame
                int id = rs.getTardis_id();
                Player player = event.getPlayer();
                // prevent other players from stealing the tripwire hook
                HashMap<String, Object> wherep = new HashMap<>();
                wherep.put("tardis_id", id);
                ResultSetTardis rso = new ResultSetTardis(plugin, wherep, "", false, 2);
                if (rso.resultSet()) {
                    Tardis tardis = rso.getTardis();
                    if (!tardis.getUuid().equals(player.getUniqueId())) {
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
                                case FLIPPED_45 -> "NORTH_EAST";
                                case COUNTER_CLOCKWISE -> "EAST";
                                case COUNTER_CLOCKWISE_45 -> "SOUTH_EAST";
                                case NONE -> "SOUTH";
                                case CLOCKWISE_45 -> "SOUTH_WEST";
                                case CLOCKWISE -> "WEST";
                                default -> "NORTH_WEST";
                            };
                            player.performCommand("tardis direction " + direction);
                            plugin.getLogger().log(Level.INFO, player.getName() + " issued server command: /tardis direction " + direction);
                        } else {
                            boolean isPreset = !tardis.getPreset().usesItemFrame();
                            Rotation r;
                            // set the rotation
                            switch (frame.getRotation()) {
                                case FLIPPED -> {
                                    r = Rotation.FLIPPED_45;
                                    direction = (isPreset) ? "EAST" : "NORTH_EAST";
                                }
                                case FLIPPED_45 -> {
                                    r = Rotation.COUNTER_CLOCKWISE;
                                    direction = "EAST";
                                }
                                case COUNTER_CLOCKWISE_45 -> {
                                    r = Rotation.NONE;
                                    direction = "SOUTH";
                                }
                                case CLOCKWISE_45 -> {
                                    r = Rotation.CLOCKWISE;
                                    direction = "WEST";
                                }
                                case CLOCKWISE_135 -> {
                                    r = Rotation.FLIPPED;
                                    direction = "NORTH";
                                }
                                case COUNTER_CLOCKWISE -> {
                                    r = Rotation.COUNTER_CLOCKWISE_45;
                                    direction = (isPreset) ? "SOUTH" : "SOUTH_EAST";
                                }
                                case NONE -> {
                                    r = Rotation.CLOCKWISE_45;
                                    direction = (isPreset) ? "WEST" : "SOUTH_WEST";
                                }
                                default -> {
                                    r = Rotation.CLOCKWISE_135;
                                    direction = (isPreset) ? "NORTH" : "NORTH_WEST";
                                }
                            }
                            if (isPreset) {
                                frame.setRotation(r);
                            }
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
                                        case SOUTH_EAST -> Rotation.COUNTER_CLOCKWISE_45;
                                        case SOUTH -> Rotation.NONE;
                                        case SOUTH_WEST -> Rotation.CLOCKWISE_45;
                                        case WEST -> Rotation.CLOCKWISE;
                                        case NORTH_WEST -> Rotation.CLOCKWISE_135;
                                        case NORTH -> Rotation.FLIPPED;
                                        default -> Rotation.FLIPPED_45;
                                    };
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
}
