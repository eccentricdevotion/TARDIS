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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
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

public class TARDISSilurianStructureRunnable implements Runnable {

    private final TARDIS plugin;
    private final int startx, starty, startz;
    private final String path;
    private boolean running = false;
    private int task, h, w, d, level = 0, row = 0;
    private JsonObject obj;
    private JsonArray arr;
    private World world;
    private Block chest;
    private Material type;
    private BlockData data;

    TARDISSilurianStructureRunnable(TARDIS plugin, int startx, int starty, int startz, String path) {
        this.plugin = plugin;
        this.startx = startx;
        this.starty = starty;
        this.startz = startz;
        this.path = path;
    }

    @Override
    public void run() {
        if (!running) {
            File file = new File(path);
            if (!file.exists()) {
                plugin.debug("Could not find the Silurian schematic!");
                plugin.getServer().getScheduler().cancelTask(task);
                task = -1;
                return;
            }
            // get default server world
            String s_world = plugin.getServer().getWorlds().get(0).getName();
            world = plugin.getServer().getWorld(s_world + "_tardis_siluria");
            obj = TARDISSchematicGZip.unzip(path);
            // get dimensions
            assert obj != null;
            JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
            h = dimensions.get("height").getAsInt() - 1;
            w = dimensions.get("width").getAsInt();
            d = dimensions.get("length").getAsInt() - 1;
            // get input array
            arr = obj.get("input").getAsJsonArray();
            running = true;
        }
        if (level == h && row == w - 1) {
            // finished
            plugin.getServer().getScheduler().cancelTask(task);
            task = -1;
        } else {
            // loop like crazy
            JsonArray floor = arr.get(level).getAsJsonArray();
            JsonArray r = floor.get(row).getAsJsonArray();
            for (int col = 0; col <= d; col++) {
                JsonObject c = r.get(col).getAsJsonObject();
                int x = startx + row;
                int y = starty + level;
                int z = startz + col;
                data = plugin.getServer().createBlockData(c.get("data").getAsString());
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
