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
package me.eccentric_nz.TARDIS.builders;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.planets.TARDISBiome;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.util.ArrayList;
import java.util.List;

public class BiomeSetter {

	public static void setBiome(BuildData bd, boolean umbrella, int loops) {
		World world = bd.getLocation().getWorld();
		int x = bd.getLocation().getBlockX();
		int z = bd.getLocation().getBlockZ();
		List<Chunk> chunks = new ArrayList<>();
		Chunk chunk = bd.getLocation().getChunk();
		chunks.add(chunk);
		// load the chunk
		int cx = bd.getLocation().getBlockX() >> 4;
		int cz = bd.getLocation().getBlockZ() >> 4;
		if (!world.loadChunk(cx, cz, false)) {
			world.loadChunk(cx, cz, true);
		}
		while (!chunk.isLoaded()) {
			world.loadChunk(chunk);
		}
		// set the biome
		for (int c = -3; c < 4; c++) {
			for (int r = -3; r < 4; r++) {
				world.setBiome(x + c, z + r, Biome.DEEP_OCEAN);
				// TODO check re-adding umbrella if rebuilding
				if (umbrella && TARDISConstants.NO_RAIN.contains(bd.getTardisBiome())) {
					// add an invisible roof
					if (loops == 3) {
						TARDISBlockSetters.setBlock(world, x + c, 255, z + r, Material.BARRIER);
					} else {
						TARDISBlockSetters.setBlockAndRemember(world, x + c, 255, z + r, Material.BARRIER, bd.getTardisID());
					}
				}
				Chunk tmp_chunk = world.getChunkAt(new Location(world, x + c, 64, z + r));
				if (!chunks.contains(tmp_chunk)) {
					chunks.add(tmp_chunk);
				}
			}
		}
		// refresh the chunks
		chunks.forEach((c) -> TARDIS.plugin.getTardisHelper().refreshChunk(c));
	}

	public static boolean restoreBiome(Location l, TARDISBiome tardisBiome) {
		Biome biome = null;
		if (tardisBiome != null && tardisBiome.getKey().getNamespace().equalsIgnoreCase("minecraft")) {
			try {
				biome = Biome.valueOf(tardisBiome.name());
			} catch (IllegalArgumentException e) {
				// ignore
			}
		}
		if (l != null && biome != null) {
			int sbx = l.getBlockX();
			int sbz = l.getBlockZ();
			World w = l.getWorld();
			List<Chunk> chunks = new ArrayList<>();
			Chunk chunk = l.getChunk();
			chunks.add(chunk);
			// reset biome and it's not The End
			TARDISBiome blockBiome = TARDISStaticUtils.getBiomeAt(l);
			if (blockBiome.equals(TARDISBiome.DEEP_OCEAN) || blockBiome.equals(TARDISBiome.THE_VOID) || (blockBiome.equals(TARDISBiome.THE_END) && !l.getWorld().getEnvironment().equals(World.Environment.THE_END))) {
				// reset the biome
				for (int c = -3; c < 4; c++) {
					for (int r = -3; r < 4; r++) {
						try {
							w.setBiome(sbx + c, sbz + r, biome);
							Chunk tmp_chunk = w.getChunkAt(new Location(w, sbx + c, 64, sbz + r));
							if (!chunks.contains(tmp_chunk)) {
								chunks.add(tmp_chunk);
							}
						} catch (NullPointerException e) {
							e.printStackTrace();
							return false;
						}
					}
				}
				// refresh the chunks
				chunks.forEach((c) -> TARDIS.plugin.getTardisHelper().refreshChunk(c));
			}
			return true;
		}
		return true;
	}
}
