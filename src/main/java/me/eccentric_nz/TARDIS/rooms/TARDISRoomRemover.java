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
package me.eccentric_nz.tardis.rooms;

import com.google.gson.JsonObject;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.enumeration.COMPASS;
import me.eccentric_nz.tardis.schematic.TARDISSchematicGZip;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

/**
 * When the Eleventh Doctor was trying to get out of his universe, he said he was deleting the scullery room and squash
 * court seven to give the tardis an extra boost.
 *
 * @author eccentric_nz
 */
class TARDISRoomRemover {

	private final TARDISPlugin plugin;
	private final String r;
	private final Location l;
	private final COMPASS d;
	private final int id;

	TARDISRoomRemover(TARDISPlugin plugin, String r, Location l, COMPASS d, int id) {
		this.plugin = plugin;
		this.r = r;
		this.l = l;
		this.d = d;
		this.id = id;
	}

	/**
	 * Jettison a tardis room, leaving just the walls behind. We will probably need to get the dimensions of the room
	 * from the schematic, if user supplied room schematics will be allowed.
	 *
	 * @return false if the room has already been jettisoned
	 */
	public boolean remove() {
		int check_distance = (r.equals("ARBORETUM")) ? 5 : 7;
		if (l.getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.valueOf(d.toString()), check_distance).getType().isAir()) {
			return false;
		}
		// get start locations
		int sx, sy, sz, ex, ey, ez, downy, upy;
		// calculate values for downy and upy from schematic dimensions / config
		String directory = (plugin.getRoomsConfig().getBoolean("rooms." + r + ".user")) ? "user_schematics" : "schematics";
		String path = plugin.getDataFolder() + File.separator + directory + File.separator + r.toLowerCase(Locale.ENGLISH) + ".tschm";
		// get JSON
		JsonObject obj = TARDISSchematicGZip.unzip(path);
		// get dimensions
		JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
		int h = dimensions.get("height").getAsInt();
		int wid = dimensions.get("width").getAsInt();
		downy = Math.abs(plugin.getRoomsConfig().getInt("rooms." + r + ".offset"));
		upy = h - (downy + 1);
		int xzoffset = (wid / 2);
		switch (d) {
			case NORTH -> {
				l.setX(l.getX() - xzoffset);
				l.setZ(l.getZ() - wid);
			}
			case WEST -> {
				l.setX(l.getX() - wid);
				l.setZ(l.getZ() - xzoffset);
			}
			case SOUTH -> l.setX(l.getX() - xzoffset);
			default -> l.setZ(l.getZ() - xzoffset);
		}
		sx = l.getBlockX();
		ex = l.getBlockX() + (wid - 1);
		sz = l.getBlockZ();
		ez = l.getBlockZ() + (wid - 1);
		sy = l.getBlockY() + upy;
		ey = l.getBlockY() - downy;
		World w = l.getWorld();
		// loop through blocks and set them to air
		for (int y = sy; y >= ey; y--) {
			for (int x = sx; x <= ex; x++) {
				for (int z = sz; z <= ez; z++) {
					Block block = w.getBlockAt(x, y, z);
					block.setBlockData(TARDISConstants.AIR);
					// if it is a GRAVITY or ANTIGRAVITY well remove it from the database
					if (r.equals("GRAVITY") || r.equals("ANTIGRAVITY")) {
						if (block.getType().equals(Material.LIME_WOOL) || block.getType().equals(Material.PINK_WOOL)) {
							String loc = new Location(w, x, y, z).toString();
							HashMap<String, Object> where = new HashMap<>();
							where.put("location", loc);
							where.put("tardis_id", id);
							plugin.getQueryFactory().doDelete("gravity_well", where);
						}
					}
				}
			}
		}
		if (r.equals("FARM") || r.equals("APIARY") || r.equals("HUTCH") || r.equals("IGLOO") || r.equals("RAIL") || r.equals("STABLE") || r.equals("STALL") || r.equals("VILLAGE") || r.equals("BIRDCAGE") || r.equals("AQUARIUM") || r.equals("BAMBOO")) {
			// remove stored location from the database
			HashMap<String, Object> set = new HashMap<>();
			set.put(r.toLowerCase(Locale.ENGLISH), "");
			HashMap<String, Object> where = new HashMap<>();
			where.put("tardis_id", id);
			plugin.getQueryFactory().doUpdate("farming", set, where);
		}
		return true;
	}
}
