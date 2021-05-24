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
import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.rooms.TARDISPainting;
import me.eccentric_nz.tardis.utility.TARDISBannerData;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.eccentric_nz.tardis.schematic.TARDISBannerSetter.setBanners;

/**
 * @author eccentric_nz
 */
class TARDISSchematicPaster implements Runnable {

	private final TARDISPlugin plugin;
	private final Player player;
	private final HashMap<Block, BlockData> postRedstoneTorches = new HashMap<>();
	private final HashMap<Block, TARDISBannerData> postBanners = new HashMap<>();
	private int task, l, r, h, w, d, x, y, z;
	private int counter = 0;
	private double div = 1.0d;
	private World world;
	private JsonObject obj;
	private JsonArray arr;
	private boolean running = false;
	private BossBar bb;

	TARDISSchematicPaster(TARDISPlugin plugin, Player player) {
		this.plugin = plugin;
		this.player = player;
		l = 0;
		r = 0;
	}

	@Override
	public void run() {
		// initialise
		if (!running) {
			UUID uuid = player.getUniqueId();
			if (!plugin.getTrackerKeeper().getPastes().containsKey(uuid)) {
				player.sendMessage(plugin.getPluginName() + "No schematic loaded! " + ChatColor.GREEN + "/ts load [name]");
				plugin.getServer().getScheduler().cancelTask(task);
				task = -1;
				return;
			}
			obj = plugin.getTrackerKeeper().getPastes().get(uuid);
			// get dimensions
			JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
			h = dimensions.get("height").getAsInt() - 1;
			w = dimensions.get("width").getAsInt();
			d = dimensions.get("length").getAsInt() - 1;
			div = (h + 1.0d) * w * (d + 1.0d);
			// get start location
			JsonObject r = obj.get("relative").getAsJsonObject();
			int rx = r.get("x").getAsInt();
			int ry = r.get("y").getAsInt();
			int rz = r.get("z").getAsInt();
			x = player.getLocation().getBlockX() - rx;
			y = player.getLocation().getBlockY() - ry;
			z = player.getLocation().getBlockZ() - rz;
			world = player.getWorld();
			// get input array
			arr = obj.get("input").getAsJsonArray();
			bb = Bukkit.createBossBar("tardis Schematic Paste Progress", BarColor.WHITE, BarStyle.SOLID, TARDISConstants.EMPTY_ARRAY);
			bb.setProgress(0);
			bb.addPlayer(player);
			bb.setVisible(true);
			running = true;
		}
		if (l == h && r == w - 1) {
			for (Map.Entry<Block, BlockData> map : postRedstoneTorches.entrySet()) {
				map.getKey().setBlockData(map.getValue());
				if (TARDISPlugin.plugin.getBlockLogger().isLogging()) {
					TARDISPlugin.plugin.getBlockLogger().logPlacement(map.getKey());
				}
			}
			setBanners(postBanners);
			// paintings
			if (obj.has("paintings")) {
				JsonArray paintings = (JsonArray) obj.get("paintings");
				for (int i = 0; i < paintings.size(); i++) {
					JsonObject painting = paintings.get(i).getAsJsonObject();
					JsonObject rel = painting.get("rel_location").getAsJsonObject();
					int px = rel.get("x").getAsInt();
					int py = rel.get("y").getAsInt();
					int pz = rel.get("z").getAsInt();
					Art art = Art.valueOf(painting.get("art").getAsString());
					BlockFace facing = BlockFace.valueOf(painting.get("facing").getAsString());
					Location pl = TARDISPainting.calculatePosition(art, facing, new Location(world, x + px, y + py, z + pz));
					try {
						Painting ent = (Painting) world.spawnEntity(pl, EntityType.PAINTING);
						ent.teleport(pl);
						ent.setFacingDirection(facing, true);
						ent.setArt(art, true);
					} catch (IllegalArgumentException e) {
						plugin.debug("Invalid painting location!");
					}
				}
			}
			plugin.getServer().getScheduler().cancelTask(task);
			task = -1;
			bb.setProgress(1);
			bb.setVisible(false);
			bb.removeAll();
		}
		// paste a column
		JsonArray level = (JsonArray) arr.get(l);
		JsonArray row = (JsonArray) level.get(r);
		for (int c = 0; c <= d; c++) {
			counter++;
			JsonObject col = row.get(c).getAsJsonObject();
			BlockData data = plugin.getServer().createBlockData(col.get("data").getAsString());
			Block block = world.getBlockAt(x + r, y + l, z + c);
			if (!block.getType().isAir() && plugin.getBlockLogger().isLogging()) {
				plugin.getBlockLogger().logRemoval(block);
			}
			switch (data.getMaterial()) {
				case REDSTONE_TORCH -> postRedstoneTorches.put(block, data);
				case BLACK_BANNER, BLACK_WALL_BANNER, BLUE_BANNER, BLUE_WALL_BANNER, BROWN_BANNER, BROWN_WALL_BANNER, CYAN_BANNER, CYAN_WALL_BANNER, GRAY_BANNER, GRAY_WALL_BANNER, GREEN_BANNER, GREEN_WALL_BANNER, LIGHT_BLUE_BANNER, LIGHT_BLUE_WALL_BANNER, LIGHT_GRAY_BANNER, LIGHT_GRAY_WALL_BANNER, LIME_BANNER, LIME_WALL_BANNER, MAGENTA_BANNER, MAGENTA_WALL_BANNER, ORANGE_BANNER, ORANGE_WALL_BANNER, PINK_BANNER, PINK_WALL_BANNER, PURPLE_BANNER, PURPLE_WALL_BANNER, RED_BANNER, RED_WALL_BANNER, WHITE_BANNER, WHITE_WALL_BANNER, YELLOW_BANNER, YELLOW_WALL_BANNER -> {
					JsonObject state = col.has("banner") ? col.get("banner").getAsJsonObject() : null;
					if (state != null) {
						TARDISBannerData tbd = new TARDISBannerData(data, state);
						postBanners.put(block, tbd);
					}
				}
				default -> {
					block.setBlockData(data, true);
					if (plugin.getBlockLogger().isLogging()) {
						plugin.getBlockLogger().logPlacement(block);
					}
				}
			}
			double progress = counter / div;
			bb.setProgress(progress);
			if (c == d && r < w) {
				r++;
			}
			if (c == d && r == w && l < h) {
				r = 0;
				l++;
			}
		}
	}

	public void setTask(int task) {
		this.task = task;
	}
}
