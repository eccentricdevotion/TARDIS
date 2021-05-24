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

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.travel.TARDISTimeTravel;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISRandomNether extends TARDISRandomLocation {

	private final TARDISPlugin plugin;
	private final Parameters param;
	private final List<World> worlds;

	TARDISRandomNether(TARDISPlugin plugin, List<String> list, Parameters param) {
		super(plugin);
		worlds = getWorlds(list);
		this.plugin = plugin;
		this.param = param;
	}

	@Override
	public Location getLocation() {
		WorldAndRange war = getWorldAndRange(worlds);
		// loop till random attempts limit reached
		for (int n = 0; n < plugin.getConfig().getInt("travel.random_attempts"); n++) {
			// get random values in range
			int randX = TARDISConstants.RANDOM.nextInt(war.getRangeX());
			int randZ = TARDISConstants.RANDOM.nextInt(war.getRangeZ());
			// get the x coord
			int x = war.getMinX() + randX;
			// get the z coord
			int z = war.getMinZ() + randZ;
			int startX, startY, startZ, resetX, resetZ, count;
			int whereY = 100;
			Block startBlock = war.getW().getBlockAt(x, whereY, z);
			while (!startBlock.getChunk().isLoaded()) {
				startBlock.getChunk().load();
			}
			while (!startBlock.getType().isAir()) {
				startBlock = startBlock.getRelative(BlockFace.DOWN);
			}
			int air = 0;
			while (startBlock.getType().isAir() && startBlock.getLocation().getBlockY() > 30) {
				startBlock = startBlock.getRelative(BlockFace.DOWN);
				air++;
			}
			Material mat = startBlock.getType();
			if (plugin.getGeneralKeeper().getGoodNether().contains(mat) && air >= 4) {
				Location dest = startBlock.getLocation();
				int netherLocY = dest.getBlockY();
				dest.setY(netherLocY + 1);
				if (param.spaceTardis()) {
					if (plugin.getPluginRespect().getRespect(dest, param)) {
						// get start location for checking there is enough space
						int[] gsl = TARDISTimeTravel.getStartLocation(dest, param.getCompass());
						startX = gsl[0];
						resetX = gsl[1];
						startY = dest.getBlockY();
						startZ = gsl[2];
						resetZ = gsl[3];
						count = TARDISTimeTravel.safeLocation(startX, startY, startZ, resetX, resetZ, war.getW(), param.getCompass());
					} else {
						count = 1;
					}
					if (count == 0) {
						return dest;
					}
				} else {
					// space for a player / check plugin respect
					if (plugin.getPluginRespect().getRespect(dest, param)) {
						return dest;
					}
				}
			}
		}
		return null;
	}
}
