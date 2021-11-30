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
package me.eccentric_nz.TARDIS.database;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

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
    private final List<String> sonicupdates = new ArrayList<>();
    private final List<String> uuidUpdates = Arrays.asList("achievements", "ars", "player_prefs", "storage", "t_count", "tardis", "travellers");
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
        blockupdates.add("police_box INTEGER DEFAULT 0");
        countupdates.add("grace INTEGER DEFAULT 0");
        destupdates.add("preset TEXT DEFAULT ''");
        destupdates.add("bind TEXT DEFAULT ''");
        destupdates.add("type INTEGER DEFAULT 0");
        destupdates.add("direction TEXT DEFAULT ''");
        destupdates.add("submarine INTEGER DEFAULT 0");
        destupdates.add("slot INTEGER DEFAULT '-1'");
        destupdates.add("icon TEXT DEFAULT ''");
        doorupdates.add("locked INTEGER DEFAULT 0");
        gravityupdates.add("distance INTEGER DEFAULT 11");
        gravityupdates.add("velocity REAL DEFAULT 0.5");
        portalsupdates.add("abandoned INTEGER DEFAULT 0");
        prefsupdates.add("artron_level INTEGER DEFAULT 0");
        prefsupdates.add("auto_on INTEGER DEFAULT 0");
        prefsupdates.add("auto_rescue_on INTEGER DEFAULT 0");
        prefsupdates.add("auto_siege_on INTEGER DEFAULT 0");
        prefsupdates.add("beacon_on INTEGER DEFAULT 1");
        prefsupdates.add("build_on INTEGER DEFAULT 1");
        prefsupdates.add("close_gui_on INTEGER DEFAULT 1");
        prefsupdates.add("difficulty INTEGER DEFAULT 0");
        prefsupdates.add("dnd_on INTEGER DEFAULT 0");
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
        prefsupdates.add("lanterns_on INTEGER DEFAULT 0");
        prefsupdates.add("minecart_on INTEGER DEFAULT 0");
        prefsupdates.add("renderer_on INTEGER DEFAULT 1");
        prefsupdates.add("siege_floor TEXT DEFAULT 'BLACK_TERRACOTTA'");
        prefsupdates.add("siege_wall TEXT DEFAULT 'GRAY_TERRACOTTA'");
        prefsupdates.add("sign_on INTEGER DEFAULT 1");
        prefsupdates.add("submarine_on INTEGER DEFAULT 0");
        prefsupdates.add("telepathy_on INTEGER DEFAULT 0");
        prefsupdates.add("texture_in TEXT DEFAULT ''");
        prefsupdates.add("texture_on INTEGER DEFAULT 0");
        prefsupdates.add("texture_out TEXT DEFAULT 'default'");
        prefsupdates.add("travelbar_on INTEGER DEFAULT 0");
        prefsupdates.add("wall TEXT DEFAULT 'ORANGE_WOOL'");
        prefsupdates.add("wool_lights_on INTEGER DEFAULT 0");
        prefsupdates.add("auto_powerup_on INTEGER DEFAULT 0");
        tardisupdates.add("abandoned INTEGER DEFAULT 0");
        tardisupdates.add("adapti_on INTEGER DEFAULT 0");
        tardisupdates.add("artron_level INTEGER DEFAULT 0");
        tardisupdates.add("beacon TEXT DEFAULT ''");
        tardisupdates.add("chameleon_demat TEXT DEFAULT 'FACTORY'");
        tardisupdates.add("chameleon_preset TEXT DEFAULT 'FACTORY'");
        tardisupdates.add("creeper TEXT DEFAULT ''");
        tardisupdates.add("eps TEXT DEFAULT ''");
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
        chameleonupdates.add("asymmetric INTEGER DEFAULT 0");
        farmingupdates.add("apiary TEXT DEFAULT ''");
        farmingupdates.add("bamboo TEXT DEFAULT ''");
        farmingupdates.add("geode TEXT DEFAULT ''");
        sonicupdates.add("arrow INTEGER DEFAULT 0");
        sonicupdates.add("knockback INTEGER DEFAULT 0");
        sonicupdates.add("model INTEGER DEFAULT 10000011");
        sonicupdates.add("sonic_uuid TEXT DEFAULT ''");
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
                String[] fsplit = s.split(" ");
                String s_query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "sonic' AND sql LIKE '%" + fsplit[0] + "%'";
                ResultSet rss = statement.executeQuery(s_query);
                if (!rss.next()) {
                    i++;
                    String s_alter = "ALTER TABLE " + prefix + "sonic ADD " + s;
                    statement.executeUpdate(s_alter);
                }
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
                new TARDISDispersalUpdater(plugin).updateTardis_ids();
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
            plugin.getLogger().log(Level.INFO, "Added " + ChatColor.AQUA + i + ChatColor.RESET + " fields to the SQLite database!");
        }
    }
}
