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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.builders;

import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.planets.TARDISBiome;
import me.eccentric_nz.tardis.utility.TARDISBlockSetters;
import me.eccentric_nz.tardis.utility.TARDISStaticUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BiomeSetter {

	public static void setBiome(BuildData bd, boolean umbrella, int loops) {
		World world = bd.getLocation().getWorld();
		int x = bd.getLocation().getBlockX();
		int y = bd.getLocation().getBlockY();
		int z = bd.getLocation().getBlockZ();
		List<Chunk> chunks = new ArrayList<>();
		Chunk chunk = bd.getLocation().getChunk();
		chunks.add(chunk);
		// load the chunk
		int cx = bd.getLocation().getBlockX() >> 4;
		int cz = bd.getLocation().getBlockZ() >> 4;
		assert world != null;
		if (!(world).loadChunk(cx, cz, false)) {
			world.loadChunk(cx, cz, true);
		}
		while (!chunk.isLoaded()) {
			world.loadChunk(chunk);
		}
		// set the biome
		for (int l = -3; l < 4; l++) {
			for (int w = -3; w < 4; w++) {
				for (int h = -1; h < 5; h++) {
					world.setBiome(x + l, y + h, z + w, Biome.DEEP_OCEAN);
					// TODO check re-adding umbrella if rebuilding
					if (umbrella && TARDISConstants.NO_RAIN.contains(bd.getTardisBiome())) {
						// add an invisible roof
						if (loops == 3) {
							TARDISBlockSetters.setBlock(world, x + l, 255, z + w, Material.BARRIER);
						} else {
							TARDISBlockSetters.setBlockAndRemember(world, x + l, 255, z + w, Material.BARRIER, bd.getTardisId());
						}
					}
					Chunk tmpChunk = world.getChunkAt(new Location(world, x + l, 64, z + w));
					if (!chunks.contains(tmpChunk)) {
						chunks.add(tmpChunk);
					}
				}
			}
		}
		// refresh the chunks
		chunks.forEach((c) -> TARDISPlugin.plugin.getTardisHelper().refreshChunk(c));
	}

	public static boolean restoreBiome(Location location, TARDISBiome tardisBiome) {
		Biome biome = null;
		if (tardisBiome != null && tardisBiome.getKey().getNamespace().equalsIgnoreCase("minecraft")) {
			try {
				biome = Biome.valueOf(tardisBiome.name());
			} catch (IllegalArgumentException ignored) {
			}
		}
		if (location != null && biome != null) {
			int sbx = location.getBlockX();
			int sby = location.getBlockY();
			int sbz = location.getBlockZ();
			World world = location.getWorld();
			List<Chunk> chunks = new ArrayList<>();
			Chunk chunk = location.getChunk();
			chunks.add(chunk);
			// reset biome and it's not The End
			TARDISBiome blockBiome = TARDISStaticUtils.getBiomeAt(location);
			if (blockBiome.equals(TARDISBiome.DEEP_OCEAN) || blockBiome.equals(TARDISBiome.THE_VOID) || (blockBiome.equals(TARDISBiome.THE_END) && !Objects.requireNonNull(location.getWorld()).getEnvironment().equals(World.Environment.THE_END))) {
				// reset the biome
				for (int l = -1; l < 2; l++) {
					for (int w = -1; w < 2; w++) {
						for (int h = 0; h < 4; h++) {
							try {
								assert world != null;
								world.setBiome(sbx + l, sby + h, sbz + w, biome);
								Chunk tmp_chunk = world.getChunkAt(new Location(world, sbx + l, 64, sbz + w));
								if (!chunks.contains(tmp_chunk)) {
									chunks.add(tmp_chunk);
								}
							} catch (NullPointerException e) {
								e.printStackTrace();
								return false;
							}
						}
					}
				}
				// refresh the chunks
				chunks.forEach((c) -> TARDISPlugin.plugin.getTardisHelper().refreshChunk(c));
			}
			return true;
		}
		return true;
	}
}
