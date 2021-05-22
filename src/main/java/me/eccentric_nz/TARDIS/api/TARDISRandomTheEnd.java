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
package me.eccentric_nz.tardis.api;

import me.eccentric_nz.tardis.TARDIS;
import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.travel.TARDISTimeTravel;
import me.eccentric_nz.tardis.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISRandomTheEnd extends TARDISRandomLocation {

	private final TARDIS plugin;
	private final Parameters param;
	private final List<World> worlds;
	private Location dest;

	TARDISRandomTheEnd(TARDIS plugin, List<String> list, Parameters param) {
		super(plugin);
		worlds = getWorlds(list);
		this.plugin = plugin;
		this.param = param;
	}

	@Override
	public Location getlocation() {
		WorldAndRange war = getWorldandRange(worlds);
		// loop till random attempts limit reached
		int limit = plugin.getConfig().getInt("travel.random_attempts");
		for (int n = 0; n < limit; n++) {
			// get random values in range
			int randX = TARDISConstants.RANDOM.nextInt(war.getRangeX());
			int randZ = TARDISConstants.RANDOM.nextInt(war.getRangeZ());
			// get the x coord
			int x = war.getMinX() + randX;
			// get the z coord
			int z = war.getMinZ() + randZ;
			// get the spawn point
			Location endSpawn = war.getW().getSpawnLocation();
			int highest = TARDISStaticLocationGetters.getHighestYin3x3(war.getW(), endSpawn.getBlockX() + x, endSpawn.getBlockZ() + z);
			int startx, starty, startz, resetx, resetz, count = 0;
			if (highest > 40) {
				Block currentBlock = war.getW().getBlockAt(x, highest, z);
				Location chunk_loc = currentBlock.getLocation();
				if (plugin.getPluginRespect().getRespect(chunk_loc, param)) {
					while (!war.getW().getChunkAt(chunk_loc).isLoaded()) {
						war.getW().getChunkAt(chunk_loc).load();
					}
					if (param.spaceTardis()) {
						// get start location for checking there is enough space
						int[] gsl = TARDISTimeTravel.getStartLocation(chunk_loc, param.getCompass());
						startx = gsl[0];
						resetx = gsl[1];
						starty = chunk_loc.getBlockY() + 1;
						startz = gsl[2];
						resetz = gsl[3];
						count = TARDISTimeTravel.safeLocation(startx, starty, startz, resetx, resetz, war.getW(), param.getCompass());
					}
				} else {
					count = 1;
				}
			} else {
				count = 1;
			}
			if (count == 0) {
				dest = (highest > 0) ? new Location(war.getW(), x, highest, z) : null;
			}
			return dest;
		}
		return null;
	}
}
