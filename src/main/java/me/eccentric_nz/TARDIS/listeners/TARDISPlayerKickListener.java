/*
 * Copyright (C) 2015 eccentric_nz
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

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

/**
 *
 * @author eccentric_nz
 */
public class TARDISPlayerKickListener implements Listener {

    private final TARDIS plugin;

    public TARDISPlayerKickListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerKickEvent event) {
        final Player player = event.getPlayer();
        Location location = player.getLocation();
        if (location.getBlockY() < 1 && plugin.getUtils().inTARDISWorld(player)) {
            event.setReason(player.getName() + " fell out of their TARDIS!");
            //event.setLeaveMessage(player.getName() + " fell out of their TARDIS!");
            event.setCancelled(true);
            if (plugin.getConfig().getString("preferences.vortex_fall").equals("kill")) {
                player.setHealth(0);
            } else {
                // get TARDIS player was in
                int id = plugin.getTardisAPI().getIdOfTARDISPlayerIsIn(player);
                // get inner door location
                HashMap<String, Object> wherei = new HashMap<String, Object>();
                wherei.put("door_type", 1);
                wherei.put("tardis_id", id);
                ResultSetDoors rsi = new ResultSetDoors(plugin, wherei, false);
                if (rsi.resultSet()) {
                    COMPASS innerD = rsi.getDoor_direction();
                    String doorLocStr = rsi.getDoor_location();
                    String[] split = doorLocStr.split(":");
                    World cw = plugin.getServer().getWorld(split[0]);
                    int cx = TARDISNumberParsers.parseInt(split[1]);
                    int cy = TARDISNumberParsers.parseInt(split[2]);
                    int cz = TARDISNumberParsers.parseInt(split[3]);
                    Location tmp_loc = cw.getBlockAt(cx, cy, cz).getLocation();
                    int getx = tmp_loc.getBlockX();
                    int getz = tmp_loc.getBlockZ();
                    switch (innerD) {
                        case NORTH:
                            // z -ve
                            tmp_loc.setX(getx + 0.5);
                            tmp_loc.setZ(getz - 0.5);
                            break;
                        case EAST:
                            // x +ve
                            tmp_loc.setX(getx + 1.5);
                            tmp_loc.setZ(getz + 0.5);
                            break;
                        case SOUTH:
                            // z +ve
                            tmp_loc.setX(getx + 0.5);
                            tmp_loc.setZ(getz + 1.5);
                            break;
                        case WEST:
                            // x -ve
                            tmp_loc.setX(getx - 0.5);
                            tmp_loc.setZ(getz + 0.5);
                            break;
                    }
                    // enter TARDIS!
                    cw.getChunkAt(tmp_loc).load();
                    float yaw = player.getLocation().getYaw();
                    float pitch = player.getLocation().getPitch();
                    tmp_loc.setPitch(pitch);
                    tmp_loc.setYaw(yaw);
                    final Location tardis_loc = tmp_loc;
                    World playerWorld = player.getLocation().getWorld();
                    player.setFallDistance(0.0f);
                    plugin.getGeneralKeeper().getDoorListener().movePlayer(player, tardis_loc, false, playerWorld, false, 3, true);
                } else {
                    player.setHealth(0);
                }
            }
        }
    }
}
