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

import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
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

    TARDISBuildSilurianStructure(TARDIS plugin) {
        this.plugin = plugin;
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
        String[] paths = {plugin.getDataFolder() + File.separator + "schematics" + File.separator + "siluria_large.tschm", plugin.getDataFolder() + File.separator + "schematics" + File.separator + "siluria_cross.tschm", plugin.getDataFolder() + File.separator + "schematics" + File.separator + "siluria_north_south.tschm", plugin.getDataFolder() + File.separator + "schematics" + File.separator + "siluria_east_west.tschm"};
        File file = new File(paths[0]);
        if (!file.exists()) {
            plugin.debug("Could not find the Silurian schematics!");
            return false;
        }
        plugin.debug("Building Silurian structure @ " + startx + ", " + starty + ", " + startz);
        World world = plugin.getServer().getWorld("Siluria");
        structure(paths[0], world, startx, starty, startz);
        // choose a random direction
        COMPASS compass = COMPASS.values()[TARDISConstants.RANDOM.nextInt(4)];
        // see if the chunk is loaded
        Vector v1 = isChunkLoaded(compass, world.getBlockAt(startx, starty, startz));
        if (v1 != null) {
            startx += v1.getBlockX();
            starty += v1.getBlockY();
            startz += v1.getBlockZ();
            if (TARDISConstants.RANDOM.nextBoolean()) {
                // cross - paths[1]
                structure(paths[1], world, startx, starty, startz);
            } else {
                // straight
                if (compass.equals(COMPASS.NORTH) || compass.equals(COMPASS.SOUTH)) {
                    // east west - paths[2]
                    structure(paths[2], world, startx, starty, startz);
                } else {
                    // north south - paths[3]
                    structure(paths[3], world, startx, starty, startz);
                }
            }
        }
        return false;
    }

    private void structure(String path, World world, int startx, int starty, int startz) {
        // get JSON
        JSONObject obj = TARDISSchematicGZip.unzip(path);
        // get dimensions
        JSONObject dimensions = (JSONObject) obj.get("dimensions");
        int h = dimensions.getInt("height");
        int w = dimensions.getInt("width");
        int l = dimensions.getInt("length");
        Block chest;
        Material type;
        BlockData data;
        // get input array
        JSONArray arr = (JSONArray) obj.get("input");
        // loop like crazy
        for (int level = 0; level < h; level++) {
            JSONArray floor = (JSONArray) arr.get(level);
            for (int row = 0; row < w; row++) {
                JSONArray r = (JSONArray) floor.get(row);
                for (int col = 0; col < l; col++) {
                    JSONObject c = (JSONObject) r.get(col);
                    int x = startx + row;
                    int y = starty + level;
                    int z = startz + col;

                    data = plugin.getServer().createBlockData(c.getString("data"));
                    type = data.getMaterial();

                    switch (type) {
                        case AIR:
                            // only set air blocks if the structure part is above the 'stilts'
                            if (level > h - 6) {
                                TARDISBlockSetters.setBlock(world, x, y, z, data);
                            }
                            break;
                        case CHEST:
                            TARDISBlockSetters.setBlock(world, x, y, z, data);
                            chest = world.getBlockAt(x, y, z);
                            if (chest != null && chest.getType().equals(Material.CHEST)) {
                                try {
                                    // set chest contents
                                    Chest container = (Chest) chest.getState();
                                    container.setLootTable(TARDISConstants.LOOT.get(TARDISConstants.RANDOM.nextInt(11)));
                                    container.update();
                                } catch (ClassCastException e) {
                                    plugin.debug("Could not cast " + chest.getType() + "to Silurian Chest." + e.getMessage());
                                }
                            }
                            break;
                        case SPONGE:
                            Block swap_block = world.getBlockAt(x, y, z);
                            if (!swap_block.getType().isOccluding()) {
                                TARDISBlockSetters.setBlock(world, x, y, z, Material.AIR);
                            }
                            break;
                        case SPAWNER:
                            Block spawner = world.getBlockAt(x, y, z);
                            spawner.setBlockData(data);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                CreatureSpawner cs = (CreatureSpawner) spawner.getState();
                                cs.setSpawnedType(EntityType.SKELETON);
                                cs.update();
                            }, 2L);
                            break;
                        default:
                            TARDISBlockSetters.setBlock(world, x, y, z, data);
                            break;
                    }
                }
            }
        }
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
