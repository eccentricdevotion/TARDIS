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
package me.eccentric_nz.tardis.database;

import me.eccentric_nz.tardis.TARDISPlugin;
import org.bukkit.ChatColor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TARDISes prefer the environment of the Space-Time Vortex to the four dimensional world. They have Curiosity Circuits
 * to encourage them to leave the Vortex.
 *
 * @author eccentric_nz
 */
class TARDISSQLiteDatabaseUpdater {

	private final List<String> areaUpdates = new ArrayList<>();
	private final List<String> blockUpdates = new ArrayList<>();
	private final List<String> countUpdates = new ArrayList<>();
	private final List<String> destUpdates = new ArrayList<>();
	private final List<String> doorUpdates = new ArrayList<>();
	private final List<String> gravityUpdates = new ArrayList<>();
	private final List<String> portalsUpdates = new ArrayList<>();
	private final List<String> prefsUpdates = new ArrayList<>();
	private final List<String> tardisUpdates = new ArrayList<>();
	private final List<String> inventoryUpdates = new ArrayList<>();
	private final List<String> chameleonUpdates = new ArrayList<>();
	private final List<String> farmingUpdates = new ArrayList<>();
	private final List<String> sonicUpdates = new ArrayList<>();
	private final List<String> uuidUpdates = Arrays.asList("achievements", "ars", "player_prefs", "storage", "t_count", "tardis", "travellers");
	private final Statement statement;
	private final TARDISPlugin plugin;
	private final String prefix;

	TARDISSQLiteDatabaseUpdater(TARDISPlugin plugin, Statement statement) {
		this.plugin = plugin;
		prefix = this.plugin.getPrefix();
		this.statement = statement;
		areaUpdates.add("y INTEGER");
		areaUpdates.add("parking_distance INTEGER DEFAULT 2");
		areaUpdates.add("invisibility TEXT DEFAULT 'ALLOW'");
		areaUpdates.add("direction TEXT DEFAULT ''");
		blockUpdates.add("police_box INTEGER DEFAULT 0");
		countUpdates.add("grace INTEGER DEFAULT 0");
		destUpdates.add("preset TEXT DEFAULT ''");
		destUpdates.add("bind TEXT DEFAULT ''");
		destUpdates.add("type INTEGER DEFAULT 0");
		destUpdates.add("direction TEXT DEFAULT ''");
		destUpdates.add("submarine INTEGER DEFAULT 0");
		destUpdates.add("slot INTEGER DEFAULT '-1'");
		destUpdates.add("icon TEXT DEFAULT ''");
		doorUpdates.add("locked INTEGER DEFAULT 0");
		gravityUpdates.add("distance INTEGER DEFAULT 11");
		gravityUpdates.add("velocity REAL DEFAULT 0.5");
		portalsUpdates.add("abandoned INTEGER DEFAULT 0");
		prefsUpdates.add("artron_level INTEGER DEFAULT 0");
		prefsUpdates.add("auto_on INTEGER DEFAULT 0");
		prefsUpdates.add("auto_rescue_on INTEGER DEFAULT 0");
		prefsUpdates.add("auto_siege_on INTEGER DEFAULT 0");
		prefsUpdates.add("beacon_on INTEGER DEFAULT 1");
		prefsUpdates.add("build_on INTEGER DEFAULT 1");
		prefsUpdates.add("ctm_on INTEGER DEFAULT 0");
		prefsUpdates.add("difficulty INTEGER DEFAULT 0");
		prefsUpdates.add("dnd_on INTEGER DEFAULT 0");
		prefsUpdates.add("eps_message TEXT DEFAULT ''");
		prefsUpdates.add("eps_on INTEGER DEFAULT 0");
		prefsUpdates.add("farm_on INTEGER DEFAULT 0");
		prefsUpdates.add("floor TEXT DEFAULT 'LIGHT_GRAY_WOOL'");
		prefsUpdates.add("flying_mode INTEGER DEFAULT 1");
		prefsUpdates.add("throttle INTEGER DEFAULT 4");
		prefsUpdates.add("hads_on INTEGER DEFAULT 1");
		prefsUpdates.add("font_on INTEGER DEFAULT 0");
		prefsUpdates.add("hads_type TEXT DEFAULT 'DISPLACEMENT'");
		prefsUpdates.add("hum TEXT DEFAULT ''");
		prefsUpdates.add("key TEXT DEFAULT ''");
		prefsUpdates.add("language TEXT DEFAULT 'ENGLISH'");
		prefsUpdates.add("lanterns_on INTEGER DEFAULT 0");
		prefsUpdates.add("minecart_on INTEGER DEFAULT 0");
		prefsUpdates.add("policebox_textures_on INTEGER DEFAULT 1");
		prefsUpdates.add("renderer_on INTEGER DEFAULT 1");
		prefsUpdates.add("siege_floor TEXT DEFAULT 'BLACK_TERRACOTTA'");
		prefsUpdates.add("siege_wall TEXT DEFAULT 'GRAY_TERRACOTTA'");
		prefsUpdates.add("sign_on INTEGER DEFAULT 1");
		prefsUpdates.add("submarine_on INTEGER DEFAULT 0");
		prefsUpdates.add("telepathy_on INTEGER DEFAULT 0");
		prefsUpdates.add("texture_in TEXT DEFAULT ''");
		prefsUpdates.add("texture_on INTEGER DEFAULT 0");
		prefsUpdates.add("texture_out TEXT DEFAULT 'default'");
		prefsUpdates.add("travelbar_on INTEGER DEFAULT 0");
		prefsUpdates.add("wall TEXT DEFAULT 'ORANGE_WOOL'");
		prefsUpdates.add("wool_lights_on INTEGER DEFAULT 0");
		prefsUpdates.add("auto_powerup_on INTEGER DEFAULT 0");
		tardisUpdates.add("abandoned INTEGER DEFAULT 0");
		tardisUpdates.add("adapti_on INTEGER DEFAULT 0");
		tardisUpdates.add("artron_level INTEGER DEFAULT 0");
		tardisUpdates.add("beacon TEXT DEFAULT ''");
		tardisUpdates.add("chameleon_demat TEXT DEFAULT 'FACTORY'");
		tardisUpdates.add("chameleon_preset TEXT DEFAULT 'FACTORY'");
		tardisUpdates.add("creeper TEXT DEFAULT ''");
		tardisUpdates.add("eps TEXT DEFAULT ''");
		tardisUpdates.add("handbrake_on INTEGER DEFAULT 1");
		tardisUpdates.add("hidden INTEGER DEFAULT 0");
		tardisUpdates.add("iso_on INTEGER DEFAULT 0");
		tardisUpdates.add("last_known_name TEXT COLLATE NOCASE DEFAULT ''");
		long now = System.currentTimeMillis();
		tardisUpdates.add("last_use INTEGER DEFAULT " + now);
		tardisUpdates.add("lights_on INTEGER DEFAULT 1");
		tardisUpdates.add("monsters INTEGER DEFAULT 0");
		tardisUpdates.add("powered_on INTEGER DEFAULT 0");
		tardisUpdates.add("rail TEXT DEFAULT ''");
		tardisUpdates.add("recharging INTEGER DEFAULT 0");
		tardisUpdates.add("renderer TEXT DEFAULT ''");
		tardisUpdates.add("rotor TEXT DEFAULT ''");
		tardisUpdates.add("siege_on INTEGER DEFAULT 0");
		tardisUpdates.add("tardis_init INTEGER DEFAULT 0");
		tardisUpdates.add("tips INTEGER DEFAULT '-1'");
		tardisUpdates.add("zero TEXT DEFAULT ''");
		inventoryUpdates.add("attributes TEXT DEFAULT ''");
		inventoryUpdates.add("armour_attributes TEXT DEFAULT ''");
		chameleonUpdates.add("line1 TEXT DEFAULT ''");
		chameleonUpdates.add("line2 TEXT DEFAULT ''");
		chameleonUpdates.add("line3 TEXT DEFAULT ''");
		chameleonUpdates.add("line4 TEXT DEFAULT ''");
		chameleonUpdates.add("asymmetric INTEGER DEFAULT 0");
		farmingUpdates.add("apiary TEXT DEFAULT ''");
		farmingUpdates.add("bamboo TEXT DEFAULT ''");
		sonicUpdates.add("arrow INTEGER DEFAULT 0");
		sonicUpdates.add("knockback INTEGER DEFAULT 0");
		sonicUpdates.add("model INTEGER DEFAULT 10000011");
		sonicUpdates.add("sonic_uuid TEXT DEFAULT ''");
	}

	/**
	 * Adds new fields to tables in the database.
	 */
	void updateTables() {
		int i = 0;
		try {
			for (String u : uuidUpdates) {
				String aQuery = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + u + "' AND sql LIKE '%uuid%'";
				ResultSet rsu = statement.executeQuery(aQuery);
				if (!rsu.next()) {
					i++;
					String uAlter = "ALTER TABLE " + prefix + u + " ADD uuid TEXT DEFAULT ''";
					statement.executeUpdate(uAlter);
				}
			}
			for (String a : areaUpdates) {
				String[] aSplit = a.split(" ");
				String aCheck = aSplit[0] + " " + aSplit[1].substring(0, 3);
				String aQuery = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "areas' AND sql LIKE '%" + aCheck + "%'";
				ResultSet rsa = statement.executeQuery(aQuery);
				if (!rsa.next()) {
					i++;
					String aAlter = "ALTER TABLE " + prefix + "areas ADD " + a;
					statement.executeUpdate(aAlter);
				}
			}
			for (String b : blockUpdates) {
				String[] bSplit = b.split(" ");
				String bQuery = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "blocks' AND sql LIKE '%" + bSplit[0] + "%'";
				ResultSet rsb = statement.executeQuery(bQuery);
				if (!rsb.next()) {
					i++;
					String bAlter = "ALTER TABLE " + prefix + "blocks ADD " + b;
					statement.executeUpdate(bAlter);
				}
			}
			for (String c : countUpdates) {
				String[] cSplit = c.split(" ");
				String cQuery = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "t_count' AND sql LIKE '%" + cSplit[0] + "%'";
				ResultSet rsc = statement.executeQuery(cQuery);
				if (!rsc.next()) {
					i++;
					String cAlter = "ALTER TABLE " + prefix + "t_count ADD " + c;
					statement.executeUpdate(cAlter);
				}
			}
			for (String d : destUpdates) {
				String[] dSplit = d.split(" ");
				String dQuery = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "destinations' AND sql LIKE '%" + dSplit[0] + "%'";
				ResultSet rsd = statement.executeQuery(dQuery);
				if (!rsd.next()) {
					i++;
					String dAlter = "ALTER TABLE " + prefix + "destinations ADD " + d;
					statement.executeUpdate(dAlter);
				}
			}
			for (String o : doorUpdates) {
				String[] oSplit = o.split(" ");
				String oQuery = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "doors' AND sql LIKE '%" + oSplit[0] + "%'";
				ResultSet rso = statement.executeQuery(oQuery);
				if (!rso.next()) {
					i++;
					String oAlter = "ALTER TABLE " + prefix + "doors ADD " + o;
					statement.executeUpdate(oAlter);
				}
			}
			for (String g : gravityUpdates) {
				String[] gSplit = g.split(" ");
				String gQuery = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "gravity_well' AND sql LIKE '%" + gSplit[0] + "%'";
				ResultSet rsg = statement.executeQuery(gQuery);
				if (!rsg.next()) {
					i++;
					String gAlter = "ALTER TABLE " + prefix + "gravity_well ADD " + g;
					statement.executeUpdate(gAlter);
				}
			}
			for (String o : portalsUpdates) {
				String[] oSplit = o.split(" ");
				String oCheck = oSplit[0] + " " + oSplit[1].substring(0, 3);
				String oQuery = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "portals' AND sql LIKE '%" + oCheck + "%'";
				ResultSet rso = statement.executeQuery(oQuery);
				if (!rso.next()) {
					i++;
					String oAlter = "ALTER TABLE " + prefix + "portals ADD " + o;
					statement.executeUpdate(oAlter);
				}
			}
			for (String p : prefsUpdates) {
				String[] pSplit = p.split(" ");
				String pCheck = pSplit[0] + " " + pSplit[1].substring(0, 3);
				String pQuery = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "player_prefs' AND sql LIKE '%" + pCheck + "%'";
				ResultSet rsp = statement.executeQuery(pQuery);
				if (!rsp.next()) {
					i++;
					String pAlter = "ALTER TABLE " + prefix + "player_prefs ADD " + p;
					statement.executeUpdate(pAlter);
				}
			}
			for (String t : tardisUpdates) {
				String[] tSplit = t.split(" ");
				String tQuery = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "tardis' AND sql LIKE '%" + tSplit[0] + "%'";
				ResultSet rst = statement.executeQuery(tQuery);
				if (!rst.next()) {
					i++;
					String tAlter = "ALTER TABLE " + prefix + "tardis ADD " + t;
					statement.executeUpdate(tAlter);
				}
			}
			for (String v : inventoryUpdates) {
				String[] vSplit = v.split(" ");
				String vQuery = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "inventories' AND sql LIKE '%" + vSplit[0] + "%'";
				ResultSet rsv = statement.executeQuery(vQuery);
				if (!rsv.next()) {
					i++;
					String vAlter = "ALTER TABLE " + prefix + "inventories ADD " + v;
					statement.executeUpdate(vAlter);
				}
			}
			for (String h : chameleonUpdates) {
				String[] hSplit = h.split(" ");
				String hQuery = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "chameleon' AND sql LIKE '%" + hSplit[0] + "%'";
				ResultSet rsh = statement.executeQuery(hQuery);
				if (!rsh.next()) {
					i++;
					String hAlter = "ALTER TABLE " + prefix + "chameleon ADD " + h;
					statement.executeUpdate(hAlter);
				}
			}
			for (String f : farmingUpdates) {
				String[] fSplit = f.split(" ");
				String fQuery = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "farming' AND sql LIKE '%" + fSplit[0] + "%'";
				ResultSet rsf = statement.executeQuery(fQuery);
				if (!rsf.next()) {
					i++;
					String fAlter = "ALTER TABLE " + prefix + "farming ADD " + f;
					statement.executeUpdate(fAlter);
				}
			}
			for (String s : sonicUpdates) {
				String[] fSplit = s.split(" ");
				String sQuery = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "sonic' AND sql LIKE '%" + fSplit[0] + "%'";
				ResultSet rss = statement.executeQuery(sQuery);
				if (!rss.next()) {
					i++;
					String sAlter = "ALTER TABLE " + prefix + "sonic ADD " + s;
					statement.executeUpdate(sAlter);
				}
			}
			// add biome to current location
			String bioQuery = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "current' AND sql LIKE '%biome%'";
			ResultSet rsBio = statement.executeQuery(bioQuery);
			if (!rsBio.next()) {
				i++;
				String bioAlter = "ALTER TABLE " + prefix + "current ADD biome TEXT DEFAULT ''";
				statement.executeUpdate(bioAlter);
			}
			// add preset to homes
			String presetQuery = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "homes' AND sql LIKE '%preset%'";
			ResultSet rsPreset = statement.executeQuery(presetQuery);
			if (!rsPreset.next()) {
				i++;
				String presetAlter = "ALTER TABLE " + prefix + "homes ADD preset TEXT DEFAULT ''";
				statement.executeUpdate(presetAlter);
			}
			// add repair to t_count
			String repQuery = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "t_count' AND sql LIKE '%repair%'";
			ResultSet rsRep = statement.executeQuery(repQuery);
			if (!rsRep.next()) {
				i++;
				String repAlter = "ALTER TABLE " + prefix + "t_count ADD repair INTEGER DEFAULT 0";
				statement.executeUpdate(repAlter);
			}
			// add tardis_id to dispersed
			String dispersedQuery = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "dispersed' AND sql LIKE '%tardis_id%'";
			ResultSet rsDispersed = statement.executeQuery(dispersedQuery);
			if (!rsDispersed.next()) {
				i++;
				String dispersedAlter = "ALTER TABLE " + prefix + "dispersed ADD tardis_id INTEGER";
				statement.executeUpdate(dispersedAlter);
				// update tardis_id column for existing records
				new TARDISDispersalUpdater(plugin).updateTardisIds();
			}
			// transfer `void` data to `thevoid`, then remove `void` table
			String voidQuery = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + prefix + "void'";
			ResultSet rsVoid = statement.executeQuery(voidQuery);
			if (rsVoid.next()) {
				String getVoid = "SELECT * FROM '" + prefix + "void'";
				ResultSet rsV = statement.executeQuery(getVoid);
				while (rsV.next()) {
					String transfer = "INSERT OR IGNORE INTO " + prefix + "thevoid (tardis_id) VALUES (" + rsV.getInt("tardis_id") + ")";
					statement.executeUpdate(transfer);
				}
				String delVoid = "DROP TABLE '" + prefix + "void'";
				statement.executeUpdate(delVoid);
			}
			// add task to vortex
			String vortexQuery = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "vortex' AND sql LIKE '%task%'";
			ResultSet rsVortex = statement.executeQuery(vortexQuery);
			if (!rsVortex.next()) {
				i++;
				String vortexAlter = "ALTER TABLE " + prefix + "vortex ADD task INTEGER DEFAULT 0";
				statement.executeUpdate(vortexAlter);
			}
			// add post_blocks to room_progress
			String postQuery = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "room_progress' AND sql LIKE '%post_blocks%'";
			ResultSet rsPost = statement.executeQuery(postQuery);
			if (!rsPost.next()) {
				i++;
				String postAlter = "ALTER TABLE " + prefix + "room_progress ADD post_blocks TEXT DEFAULT ''";
				statement.executeUpdate(postAlter);
			}
			// add chest_type to vaults
			String vctQuery = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "vaults' AND sql LIKE '%chest_type%'";
			ResultSet rsVct = statement.executeQuery(vctQuery);
			if (!rsVct.next()) {
				i++;
				String vctAlter = "ALTER TABLE " + prefix + "vaults ADD chest_type TEXT DEFAULT 'DROP'";
				statement.executeUpdate(vctAlter);
			}
			// add y to archive
			String yQuery = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "archive' AND sql LIKE '%y INTEGER%'";
			ResultSet rsY = statement.executeQuery(yQuery);
			if (!rsY.next()) {
				i++;
				String yAlter = "ALTER TABLE " + prefix + "archive ADD y INTEGER DEFAULT '64'";
				statement.executeUpdate(yAlter);
			}
			// transfer farming locations from `tardis` table to `farming` table - only if updating!
			String farmCheckQuery = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "tardis' AND sql LIKE '%farm TEXT%'";
			ResultSet rsFarmCheck = statement.executeQuery(farmCheckQuery);
			if (rsFarmCheck.next()) {
				String farmQuery = "SELECT farm_id FROM " + prefix + "farming";
				ResultSet rsFarm = statement.executeQuery(farmQuery);
				if (!rsFarm.isBeforeFirst()) {
					String tardisFarms = "SELECT tardis_id, birdcage, farm, hutch, igloo, stable, stall, village FROM " + prefix + "tardis";
					ResultSet rsTardisFarms = statement.executeQuery(tardisFarms);
					if (rsTardisFarms.isBeforeFirst()) {
						while (rsTardisFarms.next()) {
							String updateFarms = String.format("INSERT INTO " + prefix + "farming (tardis_id, birdcage, farm, hutch, igloo, stable, stall, village) VALUES (%s, '%s', '%s', '%s', '%s', '%s', '%s', '%s')", rsTardisFarms.getInt("tardis_id"), rsTardisFarms.getString("birdcage"), rsTardisFarms.getString("farm"), rsTardisFarms.getString("hutch"), rsTardisFarms.getString("igloo"), rsTardisFarms.getString("stable"), rsTardisFarms.getString("stall"), rsTardisFarms.getString("village"));
							statement.executeQuery(updateFarms);
						}
						i++;
					}
				}
			}
		} catch (SQLException e) {
			plugin.debug("SQLite database add fields error: " + e.getMessage() + e.getErrorCode());
		}
		if (i > 0) {
			plugin.getConsole().sendMessage(TARDISPlugin.plugin.getPluginName() + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " fields to the SQLite database!");
		}
	}
}
