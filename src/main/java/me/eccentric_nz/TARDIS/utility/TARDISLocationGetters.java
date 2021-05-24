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
package me.eccentric_nz.tardis.utility;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisChunk;
import me.eccentric_nz.tardis.planets.TARDISAliasResolver;
import org.bukkit.Chunk;
import org.bukkit.World;

/**
 * @author eccentric_nz
 */
public class TARDISLocationGetters {

	private final TARDISPlugin plugin;

	public TARDISLocationGetters(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Get the chunk where the interior tardis is.
	 *
	 * @param id the database record id of the tardis to get the chunk for
	 * @return the tardis interior chunk
	 */
	public Chunk getTARDISChunk(int id) {
		ResultSetTardisChunk rs = new ResultSetTardisChunk(plugin);
		if (rs.fromID(id)) {
			String c = rs.getChunk();
			String[] data = c.split(":");
			World w = TARDISAliasResolver.getWorldFromAlias(data[0]);
			int cx = TARDISNumberParsers.parseInt(data[1]);
			int cz = TARDISNumberParsers.parseInt(data[2]);
			return w.getChunkAt(cx, cz);
		}
		return null;
	}

	/**
	 * Gets a start location for building the inner tardis.
	 *
	 * @param id the tardis this location belongs to.
	 * @return an array of ints.
	 */
	public int[] getStartLocation(int id) {
		int[] startLoc = new int[4];
		int cx, cz;
		ResultSetTardisChunk rs = new ResultSetTardisChunk(plugin);
		if (rs.fromID(id)) {
			String chunkstr = rs.getChunk();
			String[] split = chunkstr.split(":");
			World w = TARDISAliasResolver.getWorldFromAlias(split[0]);
			cx = TARDISNumberParsers.parseInt(split[1]);
			cz = TARDISNumberParsers.parseInt(split[2]);
			Chunk chunk = w.getChunkAt(cx, cz);
			startLoc[0] = (chunk.getBlock(0, 64, 0).getX());
			startLoc[1] = startLoc[0];
			startLoc[2] = (chunk.getBlock(0, 64, 0).getZ());
			startLoc[3] = startLoc[2];
		}
		return startLoc;
	}
}
