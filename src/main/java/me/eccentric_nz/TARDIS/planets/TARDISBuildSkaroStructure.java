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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;

import java.io.File;

/**
 * The TARDIS was prone to a number of technical faults, ranging from depleted resources to malfunctioning controls to a
 * simple inability to arrive at the proper time or location. While the Doctor did not build the TARDIS from scratch, he
 * has substantially modified/rebuilt it.
 *
 * @author eccentric_nz
 */
class TARDISBuildSkaroStructure {

    private final TARDIS plugin;

    TARDISBuildSkaroStructure(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Builds a Skaro structure.
     *
     * @param startx the start coordinate on the x-axis
     * @param starty the start coordinate on the y-axis
     * @param startz the start coordinate on the z-axis
     * @return false when the build task has finished
     */
    boolean buildCity(int startx, int starty, int startz) {
        World world = plugin.getServer().getWorld("Skaro");
        String path = plugin.getDataFolder() + File.separator + "schematics" + File.separator;
        path += (TARDISConstants.RANDOM.nextInt(100) > 25) ? "dalek_small.tschm" : "dalek_large.tschm";
        File file = new File(path);
        if (!file.exists()) {
            plugin.debug("Could not find the Skaro schematic!");
            return false;
        }
        plugin.debug("Building Skaro structure @ " + startx + ", " + starty + ", " + startz);
        // get JSON
        JsonObject obj = TARDISSchematicGZip.unzip(path);
        // get dimensions
        JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
        int h = dimensions.get("height").getAsInt();
        int w = dimensions.get("width").getAsInt();
        int l = dimensions.get("length").getAsInt();
        Block chest;
        Material type;
        BlockData data;
        // make sure highest block is sand
        Block sand = world.getBlockAt(startx, starty, startz);
        if (sand.getType() != Material.SAND) {
            while (sand.getType() != Material.AIR) {
                sand = sand.getRelative(BlockFace.UP);
            }
            starty = sand.getLocation().getBlockY() - 1;
        }
        // get input array
        JsonArray arr = obj.get("input").getAsJsonArray();
        // loop like crazy
        for (int level = 0; level < h; level++) {
            JsonArray floor = arr.get(level).getAsJsonArray();
            for (int row = 0; row < w; row++) {
                JsonArray r = floor.get(row).getAsJsonArray();
                for (int col = 0; col < l; col++) {
                    JsonObject c = r.get(col).getAsJsonObject();
                    int x = startx + row;
                    int y = starty + level;
                    int z = startz + col;

                    data = plugin.getServer().createBlockData(c.get("data").getAsString());
                    type = data.getMaterial();

                    switch (type) {
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
                                    plugin.debug("Could not cast " + chest.getType() + "to Skaroan Chest." + e.getMessage());
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
                            }, 2l);
                            break;
                        default:
                            TARDISBlockSetters.setBlock(world, x, y, z, data);
                            break;
                    }
                }
            }
        }
        return false;
    }
}
