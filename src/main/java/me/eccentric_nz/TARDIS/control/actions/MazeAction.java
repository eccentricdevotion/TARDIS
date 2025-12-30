/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.maze.TARDISMazeBuilder;
import me.eccentric_nz.TARDIS.maze.TARDISMazeGenerator;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MazeAction {

    private final TARDIS plugin;

    public MazeAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void reconfigure(int type, Player player, int id, Location location) {
        switch (type) {
            case 40 -> // WEST
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        // has player moved out of the maze in a northerly direction
                        Location playerLocation = player.getLocation();
                        if (playerLocation.getBlockX() < location.getBlockX()) {
                            // reconfigure maze
                            reconfigureMaze(id);
                        }
                    }, 20L);
            case 41 -> // NORTH
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        // has player moved out of the maze in a westerly direction
                        Location playerLocation = player.getLocation();
                        if (playerLocation.getBlockZ() < location.getBlockZ()) {
                            // reconfigure maze
                            reconfigureMaze(id);
                        }
                    }, 20L);
            case 42 -> // SOUTH
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        // has player moved out of the maze in an easterly direction
                        Location playerLocation = player.getLocation();
                        if (playerLocation.getBlockZ() > location.getBlockZ()) {
                            // reconfigure maze
                            reconfigureMaze(id);
                        }
                    }, 20L);
            case 43 -> // EAST
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        // has player moved out of the maze in a southerly direction
                        Location playerLocation = player.getLocation();
                        if (playerLocation.getBlockX() > location.getBlockX()) {
                            // reconfigure maze
                            reconfigureMaze(id);
                        }
                    }, 20L);
            default -> {
            }
        }
    }

    private void reconfigureMaze(int id) {
        HashMap<String, Object> wherec = new HashMap<>();
        wherec.put("tardis_id", id);
        wherec.put("type", 44);
        ResultSetControls rsc = new ResultSetControls(plugin, wherec, false);
        if (rsc.resultSet()) {
            Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation());
            if (location != null) {
                TARDISMazeGenerator generator = new TARDISMazeGenerator();
                generator.makeMaze();
                TARDISMazeBuilder builder = new TARDISMazeBuilder(generator.getMaze(), location);
                builder.build(true);
            }
        }
    }
}
