/*
 * Copyright (C) 2019 eccentric_nz
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
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.util.HashMap;

/**
 * The TARDIS was prone to a number of technical faults, ranging from depleted resources to malfunctioning controls to a
 * simple inability to arrive at the proper time or location. While the Doctor did not build the TARDIS from scratch, he
 * has substantially modified/rebuilt it.
 *
 * @author eccentric_nz
 */
class TARDISBuildGallifreyanStructure {

    private final TARDIS plugin;

    TARDISBuildGallifreyanStructure(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Builds a Gallifreyan structure.
     *
     * @param startx the start coordinate on the x-axis
     * @param starty the start coordinate on the y-axis
     * @param startz the start coordinate on the z-axis
     * @return false when the build task has finished
     */
    boolean buildCity(int startx, int starty, int startz) {
        World world = plugin.getServer().getWorld("Gallifrey");
        String path = plugin.getDataFolder() + File.separator + "schematics" + File.separator + "gallifrey.tschm";
        File file = new File(path);
        if (!file.exists()) {
            plugin.debug("Could not find the Gallifrey schematic!");
            return false;
        }
        plugin.debug("Building Gallifreyan structure @ " + startx + ", " + starty + ", " + startz);
        // get JSON
        JSONObject obj = TARDISSchematicGZip.unzip(path);
        // get dimensions
        JSONObject dimensions = (JSONObject) obj.get("dimensions");
        int h = dimensions.getInt("height");
        int w = dimensions.getInt("width");
        int l = dimensions.getInt("length");
        HashMap<Block, BlockData> postLadderBlocks = new HashMap<>();
        Block chest;
        Material type;
        BlockData data;
        // submerge the structure
        starty -= 5;
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
                        case CHEST:
                            chest = world.getBlockAt(x, y, z);
                            // set chest contents
                            if (chest != null) {
                                TARDISBlockSetters.setBlock(world, x, y, z, data);
                                chest = world.getBlockAt(x, y, z);
                                if (chest != null && chest.getType().equals(Material.CHEST)) {
                                    try {
                                        // set chest contents
                                        Chest container = (Chest) chest.getState();
                                        container.setLootTable(TARDISConstants.LOOT.get(TARDISConstants.RANDOM.nextInt(11)));
                                        container.update();
                                    } catch (ClassCastException e) {
                                        plugin.debug("Could not cast " + chest.getType() + "to Gallifreyan Chest." + e.getMessage());
                                    }
                                }
                            }
                            break;
                        case LADDER:
                            postLadderBlocks.put(world.getBlockAt(x, y, z), data);
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
                                cs.setSpawnedType(EntityType.VILLAGER);
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
        postLadderBlocks.forEach((pldb, value) -> {
            pldb.setBlockData(value);
        });
        return false;
    }
}
