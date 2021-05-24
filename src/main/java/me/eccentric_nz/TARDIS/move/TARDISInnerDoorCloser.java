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
package me.eccentric_nz.tardis.move;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetDoorBlocks;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author eccentric_nz
 */
public class TARDISInnerDoorCloser {

	private final TARDISPlugin plugin;
	private final UUID uuid;
	private final int id;

	public TARDISInnerDoorCloser(TARDISPlugin plugin, UUID uuid, int id) {
		this.plugin = plugin;
		this.uuid = uuid;
		this.id = id;
	}

	public void closeDoor() {
		// get inner door location
		ResultSetDoorBlocks rs = new ResultSetDoorBlocks(plugin, id);
		if (rs.resultSet()) {
			if (!rs.getInnerBlock().getChunk().isLoaded()) {
				rs.getInnerBlock().getChunk().load();
			}
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> close(rs.getInnerBlock()), 5L);
		}
	}

	/**
	 * Close the door.
	 *
	 * @param block the bottom door block
	 */
	private void close(Block block) {
		if (block != null && Tag.DOORS.isTagged(block.getType())) {
			Openable closeable = (Openable) block.getBlockData();
			closeable.setOpen(false);
			block.setBlockData(closeable, true);
		}
		if (plugin.getConfig().getBoolean("preferences.walk_in_tardis")) {
			// get all companion UUIDs
			List<UUID> uuids = new ArrayList<>();
			HashMap<String, Object> where = new HashMap<>();
			where.put("tardis_id", id);
			ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
			if (rs.resultSet()) {
				if (!plugin.getConfig().getBoolean("preferences.open_door_policy")) {
					if (rs.getTardis().getCompanions().equalsIgnoreCase("everyone")) {
						for (Player p : Bukkit.getServer().getOnlinePlayers()) {
							uuids.add(p.getUniqueId());
						}
					} else {
						String[] companions = rs.getTardis().getCompanions().split(":");
						for (String c : companions) {
							if (!c.isEmpty()) {
								uuids.add(UUID.fromString(c));
							}
						}
						uuids.add(uuid);
					}
				}
			}
			// get locations
			// interior portal
			assert block != null;
			Location inportal = block.getLocation();
			// exterior portal (from current location)
			HashMap<String, Object> where_exportal = new HashMap<>();
			where_exportal.put("tardis_id", id);
			ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, where_exportal);
			rsc.resultSet();
			Location exportal = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
			// unset trackers
			if (!plugin.getConfig().getBoolean("preferences.open_door_policy")) {
				// players
				uuids.forEach((u) -> plugin.getTrackerKeeper().getMover().remove(u));
			}
			// locations
			plugin.getTrackerKeeper().getPortals().remove(exportal);
			plugin.getTrackerKeeper().getPortals().remove(inportal);
		}
	}
}
