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
package me.eccentric_nz.tardis.desktop;

import com.google.gson.JsonObject;
import me.eccentric_nz.tardis.TARDIS;
import me.eccentric_nz.tardis.ars.TARDISARSMethods;
import me.eccentric_nz.tardis.blueprints.TARDISPermission;
import me.eccentric_nz.tardis.database.data.Archive;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetARS;
import me.eccentric_nz.tardis.database.resultset.ResultSetControls;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.ConsoleSize;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.schematic.ArchiveReset;
import me.eccentric_nz.tardis.schematic.ResultSetArchive;
import me.eccentric_nz.tardis.schematic.TARDISSchematicGZip;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISThemeProcessor {

	private final TARDIS plugin;
	private final UUID uuid;
	private Archive archive_next;

	TARDISThemeProcessor(TARDIS plugin, UUID uuid) {
		this.plugin = plugin;
		this.uuid = uuid;
	}

	void changeDesktop() {
		// get upgrade data
		TARDISUpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(uuid);
		Player player = plugin.getServer().getPlayer(uuid);
		if (plugin.getHandlesConfig().getBoolean("enabled") && TARDISPermission.hasPermission(player, "tardis.handles")) {
			HashMap<String, Object> wheret = new HashMap<>();
			wheret.put("uuid", uuid.toString());
			ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 2);
			rs.resultSet();
			Tardis tardis = rs.getTardis();
			// check if the player has a Handles placed
			HashMap<String, Object> whereh = new HashMap<>();
			whereh.put("type", 26);
			whereh.put("tardis_id", tardis.getTardis_id());
			ResultSetControls rsc = new ResultSetControls(plugin, whereh, false);
			if (rsc.resultSet()) {
				TARDISMessage.send(player, "UPGRADE_REMOVE_HANDLES");
				return;
			}
		}
		// get Archive if necessary
		ConsoleSize size_next;
		int h;
		int w;
		if (tud.getSchematic().getPermission().equals("archive")) {
			HashMap<String, Object> where = new HashMap<>();
			where.put("uuid", uuid.toString());
			where.put("use", 1);
			ResultSetArchive rs = new ResultSetArchive(plugin, where);
			if (rs.resultSet()) {
				archive_next = rs.getArchive();
				JsonObject dimensions = archive_next.getJSON().get("dimensions").getAsJsonObject();
				h = dimensions.get("height").getAsInt();
				w = dimensions.get("width").getAsInt();
				size_next = archive_next.getConsoleSize();
			} else {
				// abort
				Player cp = plugin.getServer().getPlayer(uuid);
				TARDISMessage.send(cp, "ARCHIVE_NOT_FOUND");
				return;
			}
		} else {
			String directory = (tud.getSchematic().isCustom()) ? "user_schematics" : "schematics";
			String path = plugin.getDataFolder() + File.separator + directory + File.separator + tud.getSchematic().getPermission() + ".tschm";
			File file = new File(path);
			if (!file.exists()) {
				plugin.debug("Could not find a schematic with that name!");
				return;
			}
			// get JSON
			JsonObject obj = TARDISSchematicGZip.unzip(path);
			// get dimensions
			JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
			h = dimensions.get("height").getAsInt();
			w = dimensions.get("width").getAsInt();
			size_next = tud.getSchematic().getConsoleSize();
		}
		ConsoleSize size_prev;
		int ph;
		int pw;
		if (tud.getPrevious().getPermission().equals("archive")) {
			HashMap<String, Object> where = new HashMap<>();
			where.put("uuid", uuid.toString());
			where.put("use", 2);
			ResultSetArchive rs = new ResultSetArchive(plugin, where);
			if (rs.resultSet()) {
				JsonObject dimensions = rs.getArchive().getJSON().get("dimensions").getAsJsonObject();
				ph = dimensions.get("height").getAsInt();
				pw = dimensions.get("width").getAsInt();
				size_prev = rs.getArchive().getConsoleSize();
			} else {
				// abort
				Player cp = plugin.getServer().getPlayer(uuid);
				TARDISMessage.send(cp, "ARCHIVE_NOT_FOUND");
				return;
			}
		} else {
			String directory = (tud.getPrevious().isCustom()) ? "user_schematics" : "schematics";
			String path = plugin.getDataFolder() + File.separator + directory + File.separator + tud.getPrevious().getPermission() + ".tschm";
			File file = new File(path);
			if (!file.exists()) {
				plugin.debug("Could not find a schematic with that name!");
				return;
			}
			// get JSON
			JsonObject obj = TARDISSchematicGZip.unzip(path);
			// get dimensions
			JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
			ph = dimensions.get("height").getAsInt();
			pw = dimensions.get("width").getAsInt();
			size_prev = tud.getSchematic().getConsoleSize();
		}
		// if configured check whether there are still any blocks left
		if (plugin.getConfig().getBoolean("desktop.check_blocks_before_upgrade")) {
			TARDISUpgradeBlockScanner scanner = new TARDISUpgradeBlockScanner(plugin, tud, uuid);
			TARDISBlockScannerData check = scanner.check();
			if (check == null) {
				return;
			} else if (!check.allow()) {
				Player cp = plugin.getServer().getPlayer(uuid);
				TARDISMessage.send(cp, "UPGRADE_PERCENT_BLOCKS", plugin.getConfig().getInt("desktop.block_change_percent") + "");
				TARDISMessage.send(cp, "UPGRADE_PERCENT_EXPLAIN", check.getCount() + "", check.getVolume() + "", check.getChanged() + "");
				TARDISMessage.send(cp, "UPGRADE_PERCENT_REASON");
				if (tud.getPrevious().getPermission().equals("archive")) {
					// reset archive use back to 1
					new ArchiveReset(plugin, uuid.toString(), 1).resetUse();
				}
				return;
			}
		}
		// check if there are any rooms that need to be jettisoned
		if (w < pw || h < ph) {
			// we need more space!
			if (checkARSGrid(size_prev, size_next, uuid)) {
				TARDISMessage.send(plugin.getServer().getPlayer(uuid), "UPGRADE_ABORT_SPACE");
				plugin.getTrackerKeeper().getUpgrades().remove(uuid);
				if (tud.getPrevious().getPermission().equals("archive")) {
					// reset archive use back to 1
					new ArchiveReset(plugin, uuid.toString(), 1).resetUse();
				}
				return;
			}
		}
		// update player prefs
		HashMap<String, Object> setp = new HashMap<>();
		setp.put("wall", tud.getWall());
		setp.put("floor", tud.getFloor());
		setp.put("lanterns_on", (tud.getSchematic().hasLanterns()) ? 1 : 0);
		HashMap<String, Object> wherep = new HashMap<>();
		wherep.put("uuid", uuid.toString());
		plugin.getQueryFactory().doUpdate("player_prefs", setp, wherep);
		// update tardis
		HashMap<String, Object> sett = new HashMap<>();
		sett.put("size", tud.getSchematic().getPermission().toUpperCase(Locale.ENGLISH));
		HashMap<String, Object> wheret = new HashMap<>();
		wheret.put("uuid", uuid.toString());
		plugin.getQueryFactory().doUpdate("tardis", sett, wheret);
		// take the Artron Energy
		HashMap<String, Object> wherea = new HashMap<>();
		wherea.put("uuid", uuid.toString());
		String config_path = (archive_next != null) ? "upgrades.archive." + archive_next.getConsoleSize().getConfigPath() : "upgrades." + tud.getSchematic().getPermission().toLowerCase(Locale.ENGLISH);
		int amount = plugin.getArtronConfig().getInt(config_path);
		TARDISThemeRunnable ttr;
		boolean hasLava = tud.getPrevious().getPermission().equals("master") || tud.getPrevious().getPermission().equals("delta");
		if (tud.getPrevious().equals(tud.getSchematic()) && archive_next == null) {
			// reduce the cost of the theme change
			amount = Math.round((plugin.getArtronConfig().getInt("just_wall_floor") / 100F) * amount);
			ttr = new TARDISWallFloorRunnable(plugin, uuid, tud);
		} else {
			// check for master
			if (hasLava) {
				// remove lava and water
				new TARDISDelavafier(plugin, uuid).swap();
			}
			ttr = new TARDISFullThemeRunnable(plugin, uuid, tud);
		}
		plugin.getQueryFactory().alterEnergyLevel("tardis", -amount, wherea, plugin.getServer().getPlayer(uuid));
		// start the rebuild
		long initial_delay = (hasLava) ? 45L : 5L;
		long delay = Math.round(20 / plugin.getConfig().getDouble("growth.room_speed"));
		int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, ttr, initial_delay, delay);
		ttr.setTaskID(task);
	}

	private boolean checkARSGrid(ConsoleSize prev, ConsoleSize next, UUID uuid) {
		// get ars
		HashMap<String, Object> where = new HashMap<>();
		where.put("uuid", uuid.toString());
		ResultSetARS rs = new ResultSetARS(plugin, where);
		if (rs.resultSet()) {
			String json = rs.getJson();
			String[][][] grid = TARDISARSMethods.getGridFromJSON(json);
			switch (prev) {
				case SMALL:
					switch (next) {
						case MEDIUM:
							return (!grid[1][4][5].equals("STONE") || !grid[1][5][4].equals("STONE") || !grid[1][5][5].equals("STONE"));
						case TALL:
							return (!grid[1][4][5].equals("STONE") || !grid[1][5][4].equals("STONE") || !grid[1][5][5].equals("STONE") || !grid[2][4][4].equals("STONE") || !grid[2][4][5].equals("STONE") || !grid[2][5][4].equals("STONE") || !grid[2][5][5].equals("STONE"));
						case MASSIVE:
							return (!grid[1][4][5].equals("STONE") || !grid[1][4][6].equals("STONE") || !grid[1][5][4].equals("STONE") || !grid[1][5][5].equals("STONE") || !grid[1][5][6].equals("STONE") || !grid[1][6][4].equals("STONE") || !grid[1][6][5].equals("STONE") || !grid[1][6][6].equals("STONE") || !grid[2][4][4].equals("STONE") || !grid[2][4][5].equals("STONE") || !grid[2][4][6].equals("STONE") || !grid[2][5][4].equals("STONE") || !grid[2][5][5].equals("STONE") || !grid[2][5][6].equals("STONE") || !grid[2][6][4].equals("STONE") || !grid[2][6][5].equals("STONE") || !grid[2][6][6].equals("STONE"));
						default:
							// same size do nothing
					}
					break;
				case MEDIUM:
					switch (next) {
						case TALL:
							return (!grid[2][4][4].equals("STONE") || !grid[2][4][5].equals("STONE") || !grid[2][5][4].equals("STONE") || !grid[2][5][5].equals("STONE"));
						case MASSIVE:
							return (!grid[1][4][6].equals("STONE") || !grid[1][5][6].equals("STONE") || !grid[1][6][4].equals("STONE") || !grid[1][6][5].equals("STONE") || !grid[1][6][6].equals("STONE") || !grid[2][4][4].equals("STONE") || !grid[2][4][5].equals("STONE") || !grid[2][4][6].equals("STONE") || !grid[2][5][4].equals("STONE") || !grid[2][5][5].equals("STONE") || !grid[2][5][6].equals("STONE") || !grid[2][6][4].equals("STONE") || !grid[2][6][5].equals("STONE") || !grid[2][6][6].equals("STONE"));
						default:
							// same or smaller size do nothing
					}
					break;
				case TALL:
					// same or smaller size do nothing
					if (next == ConsoleSize.MASSIVE) {
						return (!grid[1][4][6].equals("STONE") || !grid[1][5][6].equals("STONE") || !grid[1][6][4].equals("STONE") || !grid[1][6][5].equals("STONE") || !grid[1][6][6].equals("STONE") || !grid[2][4][6].equals("STONE") || !grid[2][5][6].equals("STONE") || !grid[2][6][4].equals("STONE") || !grid[2][6][5].equals("STONE") || !grid[2][6][6].equals("STONE"));
					}
					break;
				default:
					// MASSIVE size do nothing
					break;
			}
		}
		return false;
	}
}
