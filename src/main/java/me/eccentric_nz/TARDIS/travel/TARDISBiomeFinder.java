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
package me.eccentric_nz.tardis.travel;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.api.Parameters;
import me.eccentric_nz.tardis.enumeration.COMPASS;
import me.eccentric_nz.tardis.enumeration.Flag;
import me.eccentric_nz.tardis.flight.TARDISLand;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TARDISBiomeFinder {

	private final TARDISPlugin plugin;

	public TARDISBiomeFinder(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	public void run(World w, Biome biome, Player player, int id, COMPASS direction) {
		Location tb = plugin.getTardisHelper().searchBiome(w, biome, player);
		// cancel biome finder
		if (tb == null) {
			TARDISMessage.send(player, "BIOME_NOT_FOUND");
			return;
		}
		if (!plugin.getPluginRespect().getRespect(tb, new Parameters(player, Flag.getDefaultFlags()))) {
			if (plugin.getConfig().getBoolean("travel.no_destination_malfunctions")) {
				plugin.getTrackerKeeper().getMalfunction().put(id, true);
			} else {
				// cancel
				TARDISMessage.send(player, "PROTECTED");
				return;
			}
		}
		World bw = tb.getWorld();
		// check location
		while (true) {
			assert bw != null;
			if (bw.getChunkAt(tb).isLoaded()) {
				break;
			}
			bw.getChunkAt(tb).load();
		}
		int highest = tb.getWorld().getHighestBlockYAt(tb);
		if (tb.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
			highest = getNetherHighest(tb);
		}
		tb.setY(highest + 1);
		int[] start_loc = TARDISTimeTravel.getStartLocation(tb, direction);
		int tmp_y = tb.getBlockY();
		for (int up = 0; up < 10; up++) {
			int count = TARDISTimeTravel.safeLocation(start_loc[0], tmp_y + up, start_loc[2], start_loc[1], start_loc[3], tb.getWorld(), direction);
			if (count == 0) {
				tb.setY(tmp_y + up);
				break;
			}
		}
		HashMap<String, Object> set = new HashMap<>();
		set.put("world", tb.getWorld().getName());
		set.put("x", tb.getBlockX());
		set.put("y", tb.getBlockY());
		set.put("z", tb.getBlockZ());
		set.put("direction", direction.toString());
		set.put("submarine", 0);
		HashMap<String, Object> tid = new HashMap<>();
		tid.put("tardis_id", id);
		plugin.getQueryFactory().doSyncUpdate("next", set, tid);
		TARDISMessage.send(player, "BIOME_SET", !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
		plugin.getTrackerKeeper().getHasDestination().put(id, plugin.getArtronConfig().getInt("travel"));
		plugin.getTrackerKeeper().getRescue().remove(id);
		if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
			new TARDISLand(plugin, id, player).exitVortex();
		}
	}

	private int getNetherHighest(Location location) {
		Block startBlock = location.getBlock();
		while (!startBlock.getType().isAir()) {
			startBlock = startBlock.getRelative(BlockFace.DOWN);
		}
		while (startBlock.getType().isAir() && startBlock.getLocation().getBlockY() > 30) {
			startBlock = startBlock.getRelative(BlockFace.DOWN);
		}
		return startBlock.getY() + 1;
	}
}
