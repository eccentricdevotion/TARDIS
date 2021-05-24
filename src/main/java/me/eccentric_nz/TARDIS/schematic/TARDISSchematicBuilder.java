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
package me.eccentric_nz.tardis.schematic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetControls;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.utility.TARDISNumberParsers;
import me.eccentric_nz.tardis.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.tardis.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISSchematicBuilder {

	private final TARDISPlugin plugin;
	private final World w;
	private final int id, sx, ex, sy, ey, sz, ez;
	private final int[] controls = {0, 2, 3, 4, 5};
	private final HashMap<String, Material> mushroom_stem = new HashMap<>();
	private Location h;

	public TARDISSchematicBuilder(TARDISPlugin plugin, int id, World w, int sx, int ex, int sy, int ey, int sz, int ez) {
		this.plugin = plugin;
		this.id = id;
		this.w = w;
		this.sx = sx;
		this.ex = ex;
		this.sy = sy;
		this.ey = ey;
		this.sz = sz;
		this.ez = ez;
		// orange hexagon
		mushroom_stem.put("minecraft:mushroom_stem[down=true,east=false,north=true,south=true,up=true,west=true]", Material.ORANGE_WOOL);
		// blue box
		mushroom_stem.put("minecraft:mushroom_stem[down=true,east=true,north=false,south=true,up=true,west=true]", Material.BLUE_WOOL);
		// white roundel
		mushroom_stem.put("minecraft:mushroom_stem[down=true,east=true,north=false,south=false,up=false,west=false]", Material.WHITE_STAINED_GLASS);
		// white roundel offset
		mushroom_stem.put("minecraft:mushroom_stem[down=true,east=true,north=false,south=false,up=false,west=true]", Material.WHITE_TERRACOTTA);
	}

	public ArchiveData build() {
		boolean ars = true;
		// get locations of controls first and compare their coords...
		HashMap<Integer, Location> map = new HashMap<>();
		for (int c : controls) {
			HashMap<String, Object> whereh = new HashMap<>();
			whereh.put("tardis_id", id);
			whereh.put("type", c);
			ResultSetControls rsc = new ResultSetControls(plugin, whereh, false);
			if (rsc.resultSet()) {
				Location location = TARDISStaticLocationGetters.getLocationFromDB(rsc.getLocation());
				switch (c) {
					// world repeater
					// x repeater
					// z repeater
					case 2, 3, 4, 5 ->
							// distance multiplier
							map.put(c, location);
					default ->
							// handbrake
							h = TARDISStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation());
				}
			}
		}
		// also find the beacon location...
		HashMap<String, Object> whereb = new HashMap<>();
		whereb.put("tardis_id", id);
		ResultSetTardis rs = new ResultSetTardis(plugin, whereb, "", false, 2);
		int bx = 0, by = 0, bz = 0, cx = 0, cy = 0, cz = 0;
		if (rs.resultSet()) {
			String[] split = rs.getTardis().getBeacon().split(":");
			bx = TARDISNumberParsers.parseInt(split[1]);
			by = TARDISNumberParsers.parseInt(split[2]);
			bz = TARDISNumberParsers.parseInt(split[3]);
			// and the creeper location...
			String[] csplit = rs.getTardis().getCreeper().split(":");
			cx = TARDISNumberParsers.parseInt(csplit[1].substring(0, csplit[1].length() - 2));
			cy = TARDISNumberParsers.parseInt(csplit[2]);
			cz = TARDISNumberParsers.parseInt(csplit[3].substring(0, csplit[3].length() - 2));
		}

		// get the min & max coords
		int minx = Math.min(sx, ex);
		int maxx = Math.max(sx, ex);
		int miny = Math.min(sy, ey);
		int maxy = Math.max(sy, ey);
		int minz = Math.min(sz, ez);
		int maxz = Math.max(sz, ez);
		// create a JSON object for relative position
		JsonObject relative = new JsonObject();
		relative.addProperty("x", maxx);
		relative.addProperty("y", miny);
		relative.addProperty("z", minz - 1);
		// create a JSON object for dimensions
		JsonObject dimensions = new JsonObject();
		int width = (maxx - minx) + 1;
		int height = (maxy - miny) + 1;
		int length = (maxz - minz) + 1;
		dimensions.addProperty("width", width);
		dimensions.addProperty("height", height);
		dimensions.addProperty("length", length);
		// create JSON arrays for block data
		JsonArray levels = new JsonArray();
		int f = 2;
		int beacon = 0;
		// loop through the blocks inside this cube
		for (int l = miny; l <= maxy; l++) {
			JsonArray rows = new JsonArray();
			for (int r = minx; r <= maxx; r++) {
				JsonArray columns = new JsonArray();
				for (int c = minz; c <= maxz; c++) {
					JsonObject obj = new JsonObject();
					Block b = w.getBlockAt(r, l, c);
					BlockData data = b.getBlockData();
					Material m = data.getMaterial();
					// set ars block
					if (ars && m.isAir()) {
						data = Material.INFESTED_COBBLESTONE.createBlockData();
						ars = false;
					}
					switch (m) {
						case REPEATER:
							// random location blocks
							if (isControlBlock(map.get(f), w, r, l, c)) {
								MultipleFacing mushroom = (MultipleFacing) Material.MUSHROOM_STEM.createBlockData();
								mushroom.setFace(BlockFace.DOWN, true);
								mushroom.setFace(BlockFace.EAST, true);
								mushroom.setFace(BlockFace.NORTH, true);
								mushroom.setFace(BlockFace.SOUTH, true);
								mushroom.setFace(BlockFace.UP, true);
								mushroom.setFace(BlockFace.WEST, true);
								data = mushroom;
								f++;
							}
							break;
						case LEVER:
							// handbrake
							if (isControlBlock(h, w, r, l, c)) {
								data = Material.CAKE.createBlockData();
							}
							break;
						case MUSHROOM_STEM:
							if (mushroom_stem.containsKey(data.getAsString())) {
								data = mushroom_stem.get(data.getAsString()).createBlockData();
							}
						default:
							break;
					}
					if (l == by && r == bx && c == bz) {
						data = Material.BEDROCK.createBlockData();
					}
					if (l == cy && r == cx && c == cz) {
						data = (m.equals(Material.BEACON)) ? Material.BEACON.createBlockData() : Material.COMMAND_BLOCK.createBlockData();
						beacon = (m.equals(Material.BEACON)) ? 1 : 0;
					}
					obj.addProperty("data", data.getAsString());
					// banners
					if (TARDISStaticUtils.isBanner(m)) {
						JsonObject state = new JsonObject();
						Banner banner = (Banner) b.getState();
						state.addProperty("colour", banner.getBaseColor().toString());
						JsonArray patterns = new JsonArray();
						if (banner.numberOfPatterns() > 0) {
							banner.getPatterns().forEach((p) -> {
								JsonObject pattern = new JsonObject();
								pattern.addProperty("pattern", p.getPattern().toString());
								pattern.addProperty("pattern_colour", p.getColor().toString());
								patterns.add(pattern);
							});
						}
						state.add("patterns", patterns);
						obj.add("banner", state);
					}
					columns.add(obj);
				}
				rows.add(columns);
			}
			levels.add(rows);
		}
		JsonObject schematic = new JsonObject();
		schematic.add("relative", relative);
		schematic.add("dimensions", dimensions);
		schematic.add("input", levels);
		return new ArchiveData(schematic, beacon);
	}

	private boolean isControlBlock(Location l, World w, int x, int y, int z) {
		Location n = new Location(w, x, y, z);
		return (n.equals(l));
	}

	public static class ArchiveData {

		private final JsonObject JSON;
		private final int beacon;

		ArchiveData(JsonObject JSON, int beacon) {
			this.JSON = JSON;
			this.beacon = beacon;
		}

		public JsonObject getJSON() {
			return JSON;
		}

		public int getBeacon() {
			return beacon;
		}
	}
}
