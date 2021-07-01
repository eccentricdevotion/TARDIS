/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.TARDIS.maze;

import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

public final class TARDISMazeBuilder {

    private final boolean[][] maze;
    private final BlockData log = Material.DARK_OAK_LOG.createBlockData();
    private final BlockData leaves = Material.DARK_OAK_LEAVES.createBlockData();
    private final int size;
    private final Location location;

    public TARDISMazeBuilder(boolean[][] maze, Location location) {
        this.maze = maze;
        this.location = location;
        size = this.maze.length;
    }

    public void build(boolean reconfigure) {
        World world = location.getWorld();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        // create walls
        for (int h = 0; h < 4; h++) {
            for (int w = 0; w < size; w++) {
                for (int d = 0; d < size; d++) {
                    Block block = world.getBlockAt(x + w, y + h, z + d);
                    if (maze[w][d]) {
                        block.setBlockData(h < 2 ? log : leaves);
                    } else {
                        block.setBlockData(TARDISConstants.AIR);
                    }
                }
            }
        }
        // make openings
        for (int r = 0; r < 2; r++) {
            world.getBlockAt(x + 10, y + r, z + 5).setBlockData(TARDISConstants.AIR);
            world.getBlockAt(x + 5, y + r, z).setBlockData(TARDISConstants.AIR);
            world.getBlockAt(x, y + r, z + 5).setBlockData(TARDISConstants.AIR);
            world.getBlockAt(x + 5, y + r, z + 10).setBlockData(TARDISConstants.AIR);
        }
        if (reconfigure) {
            BlockData grass = Material.GRASS_BLOCK.createBlockData();
            for (int w = 0; w < size; w++) {
                for (int d = 0; d < size; d++) {
                    Block block = world.getBlockAt(x + w, y - 1, z + d);
                    block.setBlockData(grass);
                }
            }
        }
    }
}
