/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.database;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.converters.DispersalUpdater;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * TARDISes prefer the environment of the Space-Time Vortex to the four dimensional world. They have Curiosity Circuits
 * to encourage them to leave the Vortex.
 *
 * @author eccentric_nz
 */
class TARDISSQLiteDatabaseUpdater {

    private final List<String> areaupdates = new ArrayList<>();
    private final List<String> blockupdates = new ArrayList<>();
    private final List<String> countupdates = new ArrayList<>();
    private final List<String> destupdates = new ArrayList<>();
    private final List<String> doorupdates = new ArrayList<>();
    private final List<String> gravityupdates = new ArrayList<>();
    private final List<String> portalsupdates = new ArrayList<>();
    private final List<String> prefsupdates = new ArrayList<>();
    private final List<String> tardisupdates = new ArrayList<>();
    private final List<String> inventoryupdates = new ArrayList<>();
    private final List<String> chameleonupdates = new ArrayList<>();
    private final List<String> farmingupdates = new ArrayList<>();
    private final List<String> farmingprefsupdates = new ArrayList<>();
    private final List<String> sonicupdates = new ArrayList<>();
    private final List<String> flightupdates = new ArrayList<>();
    private final List<String> systemupdates = new ArrayList<>();
    private final List<String> particleupdates = new ArrayList<>();
    private final List<String> lampsupdates = new ArrayList<>();
    private final List<String> uuidUpdates = List.of("achievements", "ars", "player_prefs", "storage", "t_count", "tardis", "travellers");
    private final Statement statement;
    private final TARDIS plugin;
    private final String prefix;

    TARDISSQLiteDatabaseUpdater(TARDIS plugin, Statement statement) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
        this.statement = statement;
        areaupdates.add("y INTEGER");
        areaupdates.add("parking_distance INTEGER DEFAULT 2");
        areaupdates.add("invisibility TEXT DEFAULT 'ALLOW'");
        areaupdates.add("direction TEXT DEFAULT ''");
        areaupdates.add("grid INTEGER DEFAULT 1");
        blockupdates.add("police_box INTEGER DEFAULT 0");
        countupdates.add("grace INTEGER DEFAULT 0");
        destupdates.add("preset TEXT DEFAULT ''");
        destupdates.add("bind TEXT DEFAULT ''");
        destupdates.add("type INTEGER DEFAULT 0");
        destupdates.add("direction TEXT DEFAULT ''");
        destupdates.add("submarine INTEGER DEFAULT 0");
        destupdates.add("slot INTEGER DEFAULT '-1'");
        destupdates.add("icon TEXT DEFAULT ''");
        destupdates.add("autonomous INTEGER DEFAULT '0'");
        doorupdates.add("locked INTEGER DEFAULT 0");
        gravityupdates.add("distance INTEGER DEFAULT 11");
        gravityupdates.add("velocity REAL DEFAULT 0.5");
        particleupdates.add("colour TEXT DEFAULT 'WHITE'");
        particleupdates.add("block TEXT DEFAULT 'STONE'");
        particleupdates.add("density INTEGER DEFAULT 16");
        portalsupdates.add("abandoned INTEGER DEFAULT 0");
        prefsupdates.add("announce_repeaters_on INTEGER DEFAULT 0");
        prefsupdates.add("artron_level INTEGER DEFAULT 0");
        prefsupdates.add("auto_on INTEGER DEFAULT 0");
        prefsupdates.add("auto_type TEXT DEFAULT 'CLOSEST'");
        prefsupdates.add("auto_default TEXT DEFAULT 'HOME'");
        prefsupdates.add("auto_rescue_on INTEGER DEFAULT 0");
        prefsupdates.add("auto_siege_on INTEGER DEFAULT 0");
        prefsupdates.add("beacon_on INTEGER DEFAULT 1");
        prefsupdates.add("build_on INTEGER DEFAULT 1");
        prefsupdates.add("close_gui_on INTEGER DEFAULT 1");
        prefsupdates.add("dnd_on INTEGER DEFAULT 0");
        prefsupdates.add("dialogs_on INTEGER DEFAULT 0");
        prefsupdates.add("dynamic_lamps_on INTEGER DEFAULT 0");
        prefsupdates.add("eps_message TEXT DEFAULT ''");
        prefsupdates.add("eps_on INTEGER DEFAULT 0");
        prefsupdates.add("farm_on INTEGER DEFAULT 0");
        prefsupdates.add("floor TEXT DEFAULT 'LIGHT_GRAY_WOOL'");
        prefsupdates.add("flying_mode INTEGER DEFAULT 1");
        prefsupdates.add("throttle INTEGER DEFAULT 4");
        prefsupdates.add("hads_on INTEGER DEFAULT 1");
        prefsupdates.add("hads_type TEXT DEFAULT 'DISPLACEMENT'");
        prefsupdates.add("hum TEXT DEFAULT ''");
        prefsupdates.add("key TEXT DEFAULT ''");
        prefsupdates.add("language TEXT DEFAULT 'ENGLISH'");
        prefsupdates.add("lights TEXT DEFAULT 'TENTH'");
        prefsupdates.add("minecart_on INTEGER DEFAULT 0");
        prefsupdates.add("open_display_door_on INTEGER DEFAULT 0");
        prefsupdates.add("renderer_on INTEGER DEFAULT 1");
        prefsupdates.add("siege_floor TEXT DEFAULT 'BLACK_TERRACOTTA'");
        prefsupdates.add("siege_wall TEXT DEFAULT 'GRAY_TERRACOTTA'");
        prefsupdates.add("sign_on INTEGER DEFAULT 1");
        prefsupdates.add("submarine_on INTEGER DEFAULT 0");
        prefsupdates.add("telepathy_on INTEGER DEFAULT 0");
        prefsupdates.add("travelbar_on INTEGER DEFAULT 0");
        prefsupdates.add("info_on INTEGER DEFAULT 0");
        prefsupdates.add("wall TEXT DEFAULT 'ORANGE_WOOL'");
        prefsupdates.add("auto_powerup_on INTEGER DEFAULT 0");
        prefsupdates.add("regenerations INTEGER DEFAULT 15");
        prefsupdates.add("regen_block_on INTEGER DEFAULT 0");
        tardisupdates.add("abandoned INTEGER DEFAULT 0");
        tardisupdates.add("adapti_on INTEGER DEFAULT 0");
        tardisupdates.add("artron_level INTEGER DEFAULT 0");
        tardisupdates.add("beacon TEXT DEFAULT ''");
        tardisupdates.add("bedrock INTEGER DEFAULT 0");
        tardisupdates.add("chameleon_demat TEXT DEFAULT 'FACTORY'");
        tardisupdates.add("chameleon_preset TEXT DEFAULT 'FACTORY'");
        tardisupdates.add("creeper TEXT DEFAULT ''");
        tardisupdates.add("eps TEXT DEFAULT ''");
        tardisupdates.add("furnaces INTEGER DEFAULT 0");
        tardisupdates.add("handbrake_on INTEGER DEFAULT 1");
        tardisupdates.add("hidden INTEGER DEFAULT 0");
        tardisupdates.add("iso_on INTEGER DEFAULT 0");
        tardisupdates.add("last_known_name TEXT COLLATE NOCASE DEFAULT ''");
        long now = System.currentTimeMillis();
        tardisupdates.add("lastuse INTEGER DEFAULT " + now);
        tardisupdates.add("lights_on INTEGER DEFAULT 1");
        tardisupdates.add("monsters INTEGER DEFAULT 0");
        tardisupdates.add("powered_on INTEGER DEFAULT 0");
        tardisupdates.add("rail TEXT DEFAULT ''");
        tardisupdates.add("recharging INTEGER DEFAULT 0");
        tardisupdates.add("renderer TEXT DEFAULT ''");
        tardisupdates.add("rotor TEXT DEFAULT ''");
        tardisupdates.add("siege_on INTEGER DEFAULT 0");
        tardisupdates.add("tardis_init INTEGER DEFAULT 0");
        tardisupdates.add("tips INTEGER DEFAULT '-1'");
        tardisupdates.add("zero TEXT DEFAULT ''");
        inventoryupdates.add("attributes TEXT DEFAULT ''");
        inventoryupdates.add("armour_attributes TEXT DEFAULT ''");
        chameleonupdates.add("line1 TEXT DEFAULT ''");
        chameleonupdates.add("line2 TEXT DEFAULT ''");
        chameleonupdates.add("line3 TEXT DEFAULT ''");
        chameleonupdates.add("line4 TEXT DEFAULT ''");
        chameleonupdates.add("active INTEGER DEFAULT 0");
        farmingupdates.add("allay TEXT DEFAULT ''");
        farmingupdates.add("apiary TEXT DEFAULT ''");
        farmingupdates.add("bamboo TEXT DEFAULT ''");
        farmingupdates.add("geode TEXT DEFAULT ''");
        farmingupdates.add("happy TEXT DEFAULT ''");
        farmingupdates.add("lava TEXT DEFAULT ''");
        farmingupdates.add("mangrove TEXT DEFAULT ''");
        farmingupdates.add("iistubil TEXT DEFAULT ''");
        farmingupdates.add("pen TEXT DEFAULT ''");
        farmingupdates.add("nautilus TEXT DEFAULT ''");
        sonicupdates.add("arrow INTEGER DEFAULT 0");
        sonicupdates.add("knockback INTEGER DEFAULT 0");
        sonicupdates.add("brush INTEGER DEFAULT 0");
        sonicupdates.add("conversion INTEGER DEFAULT 0");
        sonicupdates.add("model INTEGER DEFAULT 10000011");
        sonicupdates.add("sonic_uuid TEXT DEFAULT ''");
        sonicupdates.add("last_scanned TEXT DEFAULT ''");
        sonicupdates.add("scan_type INTEGER DEFAULT 0");
        flightupdates.add("stand TEXT DEFAULT ''");
        flightupdates.add("display TEXT DEFAULT ''");
        systemupdates.add("monitor INTEGER DEFAULT 0");
        systemupdates.add("telepathic INTEGER DEFAULT 0");
        systemupdates.add("feature INTEGER DEFAULT 0");
        systemupdates.add("throttle INTEGER DEFAULT 0");
        systemupdates.add("faster INTEGER DEFAULT 0");
        systemupdates.add("rapid INTEGER DEFAULT 0");
        systemupdates.add("warp INTEGER DEFAULT 0");
        systemupdates.add("flight INTEGER DEFAULT 0");
        lampsupdates.add("material_on TEXT DEFAULT ''");
        lampsupdates.add("material_off TEXT DEFAULT ''");
        lampsupdates.add("percentage REAL DEFAULT 1.0");
        farmingprefsupdates.add("happy INTEGER DEFAULT 1");
        farmingprefsupdates.add("nautilus INTEGER DEFAULT 1");

    }

    /**
     * Adds new fields to tables in the database.
     */
    void updateTables() {
        int i = 0;
        try {
            for (String u : uuidUpdates) {
                String a_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + u + "' AND sql LIKE '%uuid%'";
                ResultSet rsu = statement.executeQuery(a_query);
                if (!rsu.next()) {
                    i++;
                    String u_alter = "ALTER TABLE " + prefix + u + " ADD uuid TEXT DEFAULT ''";
                    statement.executeUpdate(u_alter);
                }
            }
            for (String a : areaupdates) {
                String[] asplit = a.split(" ");
                String acheck = asplit[0] + " " + asplit[1].substring(0, 3);
                String a_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "areas' AND sql LIKE '%" + acheck + "%'";
                ResultSet rsa = statement.executeQuery(a_query);
                if (!rsa.next()) {
                    i++;
                    String a_alter = "ALTER TABLE " + prefix + "areas ADD " + a;
                    statement.executeUpdate(a_alter);
                }
            }
            for (String b : blockupdates) {
                String[] bsplit = b.split(" ");
                String b_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "blocks' AND sql LIKE '%" + bsplit[0] + "%'";
                ResultSet rsb = statement.executeQuery(b_query);
                if (!rsb.next()) {
                    i++;
                    String b_alter = "ALTER TABLE " + prefix + "blocks ADD " + b;
                    statement.executeUpdate(b_alter);
                }
            }
            for (String c : countupdates) {
                String[] csplit = c.split(" ");
                String c_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "t_count' AND sql LIKE '%" + csplit[0] + "%'";
                ResultSet rsc = statement.executeQuery(c_query);
                if (!rsc.next()) {
                    i++;
                    String c_alter = "ALTER TABLE " + prefix + "t_count ADD " + c;
                    statement.executeUpdate(c_alter);
                }
            }
            for (String d : destupdates) {
                String[] dsplit = d.split(" ");
                String d_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "destinations' AND sql LIKE '%" + dsplit[0] + "%'";
                ResultSet rsd = statement.executeQuery(d_query);
                if (!rsd.next()) {
                    i++;
                    String d_alter = "ALTER TABLE " + prefix + "destinations ADD " + d;
                    statement.executeUpdate(d_alter);
                }
            }
            for (String o : doorupdates) {
                String[] osplit = o.split(" ");
                String o_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "doors' AND sql LIKE '%" + osplit[0] + "%'";
                ResultSet rso = statement.executeQuery(o_query);
                if (!rso.next()) {
                    i++;
                    String o_alter = "ALTER TABLE " + prefix + "doors ADD " + o;
                    statement.executeUpdate(o_alter);
                }
            }
            for (String g : gravityupdates) {
                String[] gsplit = g.split(" ");
                String g_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "gravity_well' AND sql LIKE '%" + gsplit[0] + "%'";
                ResultSet rsg = statement.executeQuery(g_query);
                if (!rsg.next()) {
                    i++;
                    String g_alter = "ALTER TABLE " + prefix + "gravity_well ADD " + g;
                    statement.executeUpdate(g_alter);
                }
            }
            for (String o : portalsupdates) {
                String[] osplit = o.split(" ");
                String ocheck = osplit[0] + " " + osplit[1].substring(0, 3);
                String o_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "portals' AND sql LIKE '%" + ocheck + "%'";
                ResultSet rso = statement.executeQuery(o_query);
                if (!rso.next()) {
                    i++;
                    String o_alter = "ALTER TABLE " + prefix + "portals ADD " + o;
                    statement.executeUpdate(o_alter);
                }
            }
            for (String p : prefsupdates) {
                String[] psplit = p.split(" ");
                String pcheck = psplit[0] + " " + psplit[1].substring(0, 3);
                String p_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "player_prefs' AND sql LIKE '%" + pcheck + "%'";
                ResultSet rsp = statement.executeQuery(p_query);
                if (!rsp.next()) {
                    i++;
                    String p_alter = "ALTER TABLE " + prefix + "player_prefs ADD " + p;
                    statement.executeUpdate(p_alter);
                }
            }
            for (String p : particleupdates) {
                String[] psplit = p.split(" ");
                String pcheck = psplit[0] + " " + psplit[1].substring(0, 3);
                String p_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "particle_prefs' AND sql LIKE '%" + pcheck + "%'";
                ResultSet rsp = statement.executeQuery(p_query);
                if (!rsp.next()) {
                    i++;
                    String p_alter = "ALTER TABLE " + prefix + "particle_prefs ADD " + p;
                    statement.executeUpdate(p_alter);
                }
            }
            for (String t : tardisupdates) {
                String[] tsplit = t.split(" ");
                String t_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "tardis' AND sql LIKE '%" + tsplit[0] + "%'";
                ResultSet rst = statement.executeQuery(t_query);
                if (!rst.next()) {
                    i++;
                    String t_alter = "ALTER TABLE " + prefix + "tardis ADD " + t;
                    statement.executeUpdate(t_alter);
                }
            }
            for (String v : inventoryupdates) {
                String[] vsplit = v.split(" ");
                String v_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "inventories' AND sql LIKE '%" + vsplit[0] + "%'";
                ResultSet rsv = statement.executeQuery(v_query);
                if (!rsv.next()) {
                    i++;
                    String v_alter = "ALTER TABLE " + prefix + "inventories ADD " + v;
                    statement.executeUpdate(v_alter);
                }
            }
            for (String h : chameleonupdates) {
                String[] hsplit = h.split(" ");
                String h_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "chameleon' AND sql LIKE '%" + hsplit[0] + "%'";
                ResultSet rsh = statement.executeQuery(h_query);
                if (!rsh.next()) {
                    i++;
                    String h_alter = "ALTER TABLE " + prefix + "chameleon ADD " + h;
                    statement.executeUpdate(h_alter);
                }
            }
            for (String f : farmingupdates) {
                String[] fsplit = f.split(" ");
                String f_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "farming' AND sql LIKE '%" + fsplit[0] + "%'";
                ResultSet rsf = statement.executeQuery(f_query);
                if (!rsf.next()) {
                    i++;
                    String f_alter = "ALTER TABLE " + prefix + "farming ADD " + f;
                    statement.executeUpdate(f_alter);
                }
            }
            for (String s : sonicupdates) {
                String[] ssplit = s.split(" ");
                String s_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "sonic' AND sql LIKE '%" + ssplit[0] + "%'";
                ResultSet rss = statement.executeQuery(s_query);
                if (!rss.next()) {
                    i++;
                    String s_alter = "ALTER TABLE " + prefix + "sonic ADD " + s;
                    statement.executeUpdate(s_alter);
                }
            }
            for (String f : flightupdates) {
                String[] fsplit = f.split(" ");
                String f_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "flight' AND sql LIKE '%" + fsplit[0] + "%'";
                ResultSet rsf = statement.executeQuery(f_query);
                if (!rsf.next()) {
                    i++;
                    String f_alter = "ALTER TABLE " + prefix + "flight ADD " + f;
                    statement.executeUpdate(f_alter);
                }
            }
            for (String sys : systemupdates) {
                String[] ssplit = sys.split(" ");
                String sys_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "system_upgrades' AND sql LIKE '%" + ssplit[0] + "%'";
                ResultSet rssys = statement.executeQuery(sys_query);
                if (!rssys.next()) {
                    i++;
                    String sys_alter = "ALTER TABLE " + prefix + "system_upgrades ADD " + sys;
                    statement.executeUpdate(sys_alter);
                }
            }
            for (String l : lampsupdates) {
                String[] lsplit = l.split(" ");
                String s_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "lamps' AND sql LIKE '%" + lsplit[0] + "%'";
                ResultSet rsl = statement.executeQuery(s_query);
                if (!rsl.next()) {
                    i++;
                    String l_alter = "ALTER TABLE " + prefix + "lamps ADD " + l;
                    statement.executeUpdate(l_alter);
                }
            }
            for (String fp : farmingprefsupdates) {
                String[] fpsplit = fp.split(" ");
                String fp_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "farming_prefs' AND sql LIKE '%" + fpsplit[0] + "%'";
                ResultSet rsfp = statement.executeQuery(fp_query);
                if (!rsfp.next()) {
                    i++;
                    String l_alter = "ALTER TABLE " + prefix + "farming_prefs ADD " + fp;
                    statement.executeUpdate(l_alter);
                }
            }
            // add tardis_id to previewers
            String pre_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "previewers' AND sql LIKE '%tardis_id%'";
            ResultSet rspre = statement.executeQuery(pre_query);
            if (!rspre.next()) {
                i++;
                String pre_alter = "ALTER TABLE " + prefix + "previewers ADD tardis_id INTEGER DEFAULT 0";
                statement.executeUpdate(pre_alter);
            }
            // add biome to current location
            String bio_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "current' AND sql LIKE '%biome%'";
            ResultSet rsbio = statement.executeQuery(bio_query);
            if (!rsbio.next()) {
                i++;
                String bio_alter = "ALTER TABLE " + prefix + "current ADD biome TEXT DEFAULT ''";
                statement.executeUpdate(bio_alter);
            }
            // add preset to homes
            String preset_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "homes' AND sql LIKE '%preset%'";
            ResultSet rspreset = statement.executeQuery(preset_query);
            if (!rspreset.next()) {
                i++;
                String preset_alter = "ALTER TABLE " + prefix + "homes ADD preset TEXT DEFAULT ''";
                statement.executeUpdate(preset_alter);
            }
            // add repair to t_count
            String rep_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "t_count' AND sql LIKE '%repair%'";
            ResultSet rsrep = statement.executeQuery(rep_query);
            if (!rsrep.next()) {
                i++;
                String rep_alter = "ALTER TABLE " + prefix + "t_count ADD repair INTEGER DEFAULT 0";
                statement.executeUpdate(rep_alter);
            }
            // add tardis_id to dispersed
            String dispersed_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "dispersed' AND sql LIKE '%tardis_id%'";
            ResultSet rsdispersed = statement.executeQuery(dispersed_query);
            if (!rsdispersed.next()) {
                i++;
                String dispersed_alter = "ALTER TABLE " + prefix + "dispersed ADD tardis_id INTEGER";
                statement.executeUpdate(dispersed_alter);
                // update tardis_id column for existing records
                new DispersalUpdater(plugin).updateTardis_ids();
            }
            // transfer `void` data to `thevoid`, then remove `void` table
            String voidQuery = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + prefix + "void'";
            ResultSet rsvoid = statement.executeQuery(voidQuery);
            if (rsvoid.next()) {
                String getVoid = "SELECT * FROM '" + prefix + "void'";
                ResultSet rsv = statement.executeQuery(getVoid);
                while (rsv.next()) {
                    String transfer = "INSERT OR IGNORE INTO " + prefix + "thevoid (tardis_id) VALUES (" + rsv.getInt("tardis_id") + ")";
                    statement.executeUpdate(transfer);
                }
                String delVoid = "DROP TABLE '" + prefix + "void'";
                statement.executeUpdate(delVoid);
            }
            // add task to vortex
            String vortex_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "vortex' AND sql LIKE '%task%'";
            ResultSet rsvortex = statement.executeQuery(vortex_query);
            if (!rsvortex.next()) {
                i++;
                String vortex_alter = "ALTER TABLE " + prefix + "vortex ADD task INTEGER DEFAULT 0";
                statement.executeUpdate(vortex_alter);
            }
            // add post_blocks to room_progress
            String post_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "room_progress' AND sql LIKE '%post_blocks%'";
            ResultSet rspost = statement.executeQuery(post_query);
            if (!rspost.next()) {
                i++;
                String post_alter = "ALTER TABLE " + prefix + "room_progress ADD post_blocks TEXT DEFAULT ''";
                statement.executeUpdate(post_alter);
            }
            // add chest_type to vaults
            String vct_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "vaults' AND sql LIKE '%chest_type%'";
            ResultSet rsvct = statement.executeQuery(vct_query);
            if (!rsvct.next()) {
                i++;
                String vct_alter = "ALTER TABLE " + prefix + "vaults ADD chest_type TEXT DEFAULT 'DROP'";
                statement.executeUpdate(vct_alter);
            }
            // add happy to farming_prefs
            String happy_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "farming_prefs' AND sql LIKE '%happy%'";
            ResultSet rshappy = statement.executeQuery(happy_query);
            if (!rshappy.next()) {
                i++;
                String happy_alter = "ALTER TABLE " + prefix + "farming_prefs ADD happy INTEGER DEFAULT 1";
                statement.executeUpdate(happy_alter);
            }
            // add version to storage
            String version_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "storage' AND sql LIKE '%versions%'";
            ResultSet rsversion = statement.executeQuery(version_query);
            if (!rsversion.next()) {
                i++;
                String happy_alter = "ALTER TABLE " + prefix + "storage ADD versions TEXT DEFAULT '0,0,0,0,0,0,0,0,0'";
                statement.executeUpdate(happy_alter);
            }
            // add y to archive
            String y_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "archive' AND sql LIKE '%y INTEGER%'";
            ResultSet rsy = statement.executeQuery(y_query);
            if (!rsy.next()) {
                i++;
                String y_alter = "ALTER TABLE " + prefix + "archive ADD y INTEGER DEFAULT '64'";
                statement.executeUpdate(y_alter);
            }
            // transfer farming locations from `tardis` table to `farming` table - only if updating!
            String farmCheckQuery = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "tardis' AND sql LIKE '%farm TEXT%'";
            ResultSet rsfc = statement.executeQuery(farmCheckQuery);
            if (rsfc.next()) {
                String farmQuery = "SELECT farm_id FROM " + prefix + "farming";
                ResultSet rsf = statement.executeQuery(farmQuery);
                if (!rsf.isBeforeFirst()) {
                    String tardisFarms = "SELECT tardis_id, birdcage, farm, hutch, igloo, stable, stall, village FROM " + prefix + "tardis";
                    ResultSet rstf = statement.executeQuery(tardisFarms);
                    if (rstf.isBeforeFirst()) {
                        while (rstf.next()) {
                            String updateFarms = String.format("INSERT INTO " + prefix + "farming (tardis_id, birdcage, farm, hutch, igloo, stable, stall, village) VALUES (%s, '%s', '%s', '%s', '%s', '%s', '%s', '%s')", rstf.getInt("tardis_id"), rstf.getString("birdcage"), rstf.getString("farm"), rstf.getString("hutch"), rstf.getString("igloo"), rstf.getString("stable"), rstf.getString("stall"), rstf.getString("village"));
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
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Added " + i + " fields to the SQLite database!");
        }
    }
}
