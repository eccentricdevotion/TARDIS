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
package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.io.File;

/**
 * The TARDIS was prone to a number of technical faults, ranging from depleted resources to malfunctioning controls to a
 * simple inability to arrive at the proper time or location. While the Doctor did not build the TARDIS from scratch, he
 * has substantially modified/rebuilt it.
 *
 * @author eccentric_nz
 */
class TARDISBuildSilurianStructure {

    private final TARDIS plugin;
    private final String[] paths;

    TARDISBuildSilurianStructure(TARDIS plugin) {
        this.plugin = plugin;
        paths = new String[]{plugin.getDataFolder() + File.separator + "schematics" + File.separator + "siluria_large.tschm", plugin.getDataFolder() + File.separator + "schematics" + File.separator + "siluria_cross.tschm", plugin.getDataFolder() + File.separator + "schematics" + File.separator + "siluria_north_south.tschm", plugin.getDataFolder() + File.separator + "schematics" + File.separator + "siluria_east_west.tschm"};
    }

    /**
     * Builds a Siluria structure.
     *
     * @param startx the start coordinate on the x-axis
     * @param starty the start coordinate on the y-axis
     * @param startz the start coordinate on the z-axis
     * @return false when the build task has finished
     */
    boolean buildCity(int startx, int starty, int startz) {
        File file = new File(paths[0]);
        if (!file.exists()) {
            plugin.debug("Could not find the Silurian schematics!");
            return false;
        }
        TARDISSilurianStructureRunnable tssr = new TARDISSilurianStructureRunnable(plugin, startx, starty, startz, paths[0]);
        int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, tssr, 1L, 1L);
        tssr.setTask(task);
        // choose a random direction
        COMPASS compass = COMPASS.values()[TARDISConstants.RANDOM.nextInt(4)];
        // see if the chunk is loaded
        Vector v1 = isChunkLoaded(compass, plugin.getServer().getWorld("Siluria").getBlockAt(startx, starty, startz));
        if (v1 != null) {
            startx += v1.getBlockX();
            starty += v1.getBlockY();
            startz += v1.getBlockZ();
            String path;
            if (TARDISConstants.RANDOM.nextBoolean()) {
                // cross - paths[1]
                path = paths[1];
            } else {
                // straight
                if (compass.equals(COMPASS.NORTH) || compass.equals(COMPASS.SOUTH)) {
                    // east west - paths[2]
                    path = paths[2];
                } else {
                    // north south - paths[3]
                    path = paths[3];
                }
            }
            TARDISSilurianStructureRunnable secondary = new TARDISSilurianStructureRunnable(plugin, startx, starty, startz, path);
            int secondaryTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, secondary, 20L, 1L);
            secondary.setTask(secondaryTask);
        }
        return false;
    }

    private Vector isChunkLoaded(COMPASS compass, Block block) {
        Chunk chunk = block.getChunk();
        int x = chunk.getX();
        int z = chunk.getZ();
        Vector vector;
        switch (compass) {
            case WEST:
                vector = new Vector(-16, 17, 0);
                x -= 1;
                break;
            case NORTH:
                vector = new Vector(0, 17, -16);
                z -= 1;
                break;
            case EAST:
                vector = new Vector(16, 17, 0);
                x += 1;
                break;
            default: //SOUTH
                vector = new Vector(0, 17, 16);
                z += 1;
                break;
        }
        // see if the chunk is loaded
        Chunk newChunk = chunk.getWorld().getChunkAt(x, z);
        return newChunk.isLoaded() ? vector : null;
    }
}
