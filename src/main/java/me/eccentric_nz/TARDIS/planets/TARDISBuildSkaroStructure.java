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
package me.eccentric_nz.tardis.planets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.schematic.TARDISSchematicGZip;
import me.eccentric_nz.tardis.utility.TARDISBlockSetters;
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
 * The tardis was prone to a number of technical faults, ranging from depleted resources to malfunctioning controls to a
 * simple inability to arrive at the proper time or location. While the Doctor did not build the tardis from scratch, he
 * has substantially modified/rebuilt it.
 *
 * @author eccentric_nz
 */
class TARDISBuildSkaroStructure implements Runnable {

	private final TARDISPlugin plugin;
	private final int startx, y, startz;
	private boolean running = false;
	private int task, starty, h, w, d, level = 0, row = 0;
	private JsonArray arr;
	private World world;

	/**
	 * Builds a Skaro structure.
	 *
	 * @param plugin an instance of the main tardis plugin class
	 * @param startx the start coordinate on the x-axis
	 * @param y      the start coordinate on the y-axis
	 * @param startz the start coordinate on the z-axis
	 */
	TARDISBuildSkaroStructure(TARDISPlugin plugin, int startx, int y, int startz) {
		this.plugin = plugin;
		this.startx = startx;
		this.y = y;
		this.startz = startz;
	}

	@Override
	public void run() {
		if (!running) {
			// get default server world
			String s_world = plugin.getServer().getWorlds().get(0).getName();
			world = plugin.getServer().getWorld(s_world + "_tardis_skaro");
			String path = plugin.getDataFolder() + File.separator + "schematics" + File.separator;
			path += (TARDISConstants.RANDOM.nextInt(100) > 25) ? "dalek_small.tschm" : "dalek_large.tschm";
			File file = new File(path);
			if (!file.exists()) {
				plugin.debug("Could not find the Skaro schematic!");
				plugin.getServer().getScheduler().cancelTask(task);
				task = -1;
				return;
			}
			// get JSON
			JsonObject obj = TARDISSchematicGZip.unzip(path);
			// get dimensions
			assert obj != null;
			JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
			h = dimensions.get("height").getAsInt() - 1;
			w = dimensions.get("width").getAsInt();
			d = dimensions.get("length").getAsInt() - 1;
			// make sure highest block is sand
			Block sand = world.getBlockAt(startx, y, startz);
			if (sand.getType() != Material.SAND) {
				while (sand.getType() != Material.AIR) {
					sand = sand.getRelative(BlockFace.UP);
				}
				starty = sand.getLocation().getBlockY() - 1;
			}
			// get input array
			arr = obj.get("input").getAsJsonArray();
			running = true;
		}
		if (level == h && row == w - 1) {
			// finished
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
				BlockData data = plugin.getServer().createBlockData(c.get("data").getAsString());
				Material type = data.getMaterial();
				switch (type) {
					case CHEST -> {
						TARDISBlockSetters.setBlock(world, x, y, z, data);
						Block chest = world.getBlockAt(x, y, z);
						if (chest.getType().equals(Material.CHEST)) {
							try {
								// set chest contents
								Chest container = (Chest) chest.getState();
								container.setLootTable(TARDISConstants.LOOT.get(TARDISConstants.RANDOM.nextInt(11)));
								container.update();
							} catch (ClassCastException e) {
								plugin.debug("Could not cast " + chest.getType() + "to Skaroan Chest." + e.getMessage());
							}
						}
					}
					case SPONGE -> {
						Block swap_block = world.getBlockAt(x, y, z);
						if (!swap_block.getType().isOccluding()) {
							TARDISBlockSetters.setBlock(world, x, y, z, Material.AIR);
						}
					}
					case SPAWNER -> {
						Block spawner = world.getBlockAt(x, y, z);
						spawner.setBlockData(data);
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							CreatureSpawner cs = (CreatureSpawner) spawner.getState();
							cs.setSpawnedType(EntityType.SKELETON);
							cs.update();
						}, 2L);
					}
					default -> TARDISBlockSetters.setBlock(world, x, y, z, data);
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
