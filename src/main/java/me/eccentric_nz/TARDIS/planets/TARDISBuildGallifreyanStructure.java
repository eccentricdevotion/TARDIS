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
class TARDISBuildGallifreyanStructure implements Runnable {

    private final TARDIS plugin;
    private final int startx, y, startz;
    private int task, starty, h, w, d, level = 0, row = 0;
    boolean running = false;
    private JsonObject obj;
    private JsonArray arr;
    private final HashMap<Block, BlockData> postLadderBlocks = new HashMap<>();
    private World world;
    private Block chest;
    private Material type;
    private BlockData data;

    /**
     * Builds a Gallifreyan structure.
     *
     * @param plugin an instance of the main TARDIS plugin class
     * @param startx the start coordinate on the x-axis
     * @param y      the start coordinate on the y-axis
     * @param startz the start coordinate on the z-axis
     */
    public TARDISBuildGallifreyanStructure(TARDIS plugin, int startx, int y, int startz) {
        this.plugin = plugin;
        this.startx = startx;
        this.y = y;
        this.startz = startz;
    }

    @Override
    public void run() {
        if (!running) {
            String path = plugin.getDataFolder() + File.separator + "schematics" + File.separator + "gallifrey.tschm";
            File file = new File(path);
            if (!file.exists()) {
                plugin.debug("Could not find the Gallifrey schematic!");
                plugin.getServer().getScheduler().cancelTask(task);
                task = -1;
                return;
            }
            world = plugin.getServer().getWorld("Gallifrey");
            // get JSON
            obj = TARDISSchematicGZip.unzip(path);
            // get dimensions
            JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
            h = dimensions.get("height").getAsInt() - 1;
            w = dimensions.get("width").getAsInt();
            d = dimensions.get("length").getAsInt() - 1;
            // submerge the structure
            starty = y - 5;
//            plugin.debug("Building Gallifreyan structure @ " + startx + ", " + starty + ", " + startz);
            // get input array
            arr = obj.get("input").getAsJsonArray();
            running = true;
        }
        if (level == h && row == w - 1) {
            // finished
            postLadderBlocks.forEach(Block::setBlockData);
            plugin.getServer().getScheduler().cancelTask(task);
            task = -1;
        } else {
            JsonArray floor = arr.get(level).getAsJsonArray();
            JsonArray r = floor.get(row).getAsJsonArray();
            // loop like crazy
            for (int col = 0; col <= d; col++) {
                JsonObject c = r.get(col).getAsJsonObject();
                int x = startx + row;
                int y = starty + level;
                int z = startz + col;
                data = plugin.getServer().createBlockData(c.get("data").getAsString());
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
                if (col == d && row < w) {
                    row++;
                }
                if (col == d && row == w && level < h) {
                    row = 0;
                    level++;
                }
            }
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
