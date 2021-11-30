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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * TARDISes prefer the environment of the Space-Time Vortex to the four dimensional world. They have Curiosity Circuits
 * to encourage them to leave the Vortex.
 *
 * @author eccentric_nz
 */
class TARDISMySQLDatabaseUpdater {

    private final List<String> areaupdates = new ArrayList<>();
    private final List<String> tardisupdates = new ArrayList<>();
    private final List<String> prefsupdates = new ArrayList<>();
    private final List<String> destsupdates = new ArrayList<>();
    private final List<String> countupdates = new ArrayList<>();
    private final List<String> portalsupdates = new ArrayList<>();
    private final List<String> inventoryupdates = new ArrayList<>();
    private final List<String> chameleonupdates = new ArrayList<>();
    private final List<String> farmingupdates = new ArrayList<>();
    private final List<String> sonicupdates = new ArrayList<>();
    private final HashMap<String, String> uuidUpdates = new HashMap<>();
    private final Statement statement;
    private final TARDIS plugin;
    private final String prefix;

    TARDISMySQLDatabaseUpdater(TARDIS plugin, Statement statement) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
        this.statement = statement;
        uuidUpdates.put("achievements", "a_id");
        uuidUpdates.put("ars", "tardis_id");
        uuidUpdates.put("player_prefs", "pp_id");
        uuidUpdates.put("storage", "tardis_id");
        uuidUpdates.put("t_count", "t_id");
        uuidUpdates.put("tardis", "tardis_id");
        uuidUpdates.put("travellers", "tardis_id");
        areaupdates.add("parking_distance int(2) DEFAULT '2'");
        areaupdates.add("invisibility varchar(32) DEFAULT 'ALLOW'");
        areaupdates.add("direction varchar(5) DEFAULT ''");
        tardisupdates.add("last_known_name varchar(32) DEFAULT ''");
        tardisupdates.add("lights_on int(1) DEFAULT '1'");
        tardisupdates.add("monsters int(2) DEFAULT '0'");
        tardisupdates.add("abandoned int(1) DEFAULT '0'");
        tardisupdates.add("powered_on int(1) DEFAULT '0'");
        tardisupdates.add("renderer varchar(512) DEFAULT ''");
        tardisupdates.add("rotor varchar(48) DEFAULT ''");
        tardisupdates.add("siege_on int(1) DEFAULT '0'");
        tardisupdates.add("zero varchar(512) DEFAULT ''");
        prefsupdates.add("auto_rescue_on int(1) DEFAULT '0'");
        prefsupdates.add("auto_siege_on int(1) DEFAULT '0'");
        prefsupdates.add("build_on int(1) DEFAULT '1'");
        prefsupdates.add("close_gui_on int(1) DEFAULT '1'");
        prefsupdates.add("difficulty int(1) DEFAULT '0'");
        prefsupdates.add("dnd_on int(1) DEFAULT '0'");
        prefsupdates.add("farm_on int(1) DEFAULT '0'");
        prefsupdates.add("flying_mode int(1) DEFAULT '1'");
        prefsupdates.add("throttle int(1) DEFAULT '4'");
        prefsupdates.add("hads_type varchar(12) DEFAULT 'DISPLACEMENT'");
        prefsupdates.add("hum varchar(24) DEFAULT ''");
        prefsupdates.add("language varchar(32) DEFAULT 'ENGLISH'");
        prefsupdates.add("lanterns_on int(1) DEFAULT '0'");
        prefsupdates.add("minecart_on int(1) DEFAULT '0'");
        prefsupdates.add("renderer_on int(1) DEFAULT '1'");
        prefsupdates.add("siege_floor varchar(64) DEFAULT 'BLACK_TERRACOTTA'");
        prefsupdates.add("siege_wall varchar(64) DEFAULT 'GRAY_TERRACOTTA'");
        prefsupdates.add("sign_on int(1) DEFAULT '1'");
        prefsupdates.add("telepathy_on int(1) DEFAULT '0'");
        prefsupdates.add("travelbar_on int(1) DEFAULT '0'");
        prefsupdates.add("wool_lights_on int(1) DEFAULT '0'");
        prefsupdates.add("auto_powerup_on int(1) DEFAULT '0'");
        destsupdates.add("preset varchar(32) DEFAULT ''");
        destsupdates.add("slot int(1) DEFAULT '-1'");
        destsupdates.add("icon varchar(64) DEFAULT ''");
        countupdates.add("grace int(3) DEFAULT '0'");
        portalsupdates.add("abandoned int(1) DEFAULT '0'");
        inventoryupdates.add("attributes text");
        inventoryupdates.add("armour_attributes text");
        chameleonupdates.add("line1 varchar(48) DEFAULT ''");
        chameleonupdates.add("line2 varchar(48) DEFAULT ''");
        chameleonupdates.add("line3 varchar(48) DEFAULT ''");
        chameleonupdates.add("line4 varchar(48) DEFAULT ''");
        chameleonupdates.add("asymmetric int(1) DEFAULT '0'");
        farmingupdates.add("apiary varchar(512) DEFAULT ''");
        farmingupdates.add("bamboo varchar(512) DEFAULT ''");
        farmingupdates.add("geode varchar(512) DEFAULT ''");
        sonicupdates.add("arrow int(1) DEFAULT '0'");
        sonicupdates.add("knockback int(1) DEFAULT '0'");
        sonicupdates.add("model int(11) DEFAULT '10000011'");
        sonicupdates.add("sonic_uuid varchar(48) DEFAULT ''");
    }

    /**
     * Adds new fields to tables in the database.
     */
    void updateTables() {
        int i = 0;
        try {
            for (Map.Entry<String, String> u : uuidUpdates.entrySet()) {
                String u_query = "SHOW COLUMNS FROM " + prefix + u.getKey() + " LIKE 'uuid'";
                ResultSet rsu = statement.executeQuery(u_query);
                if (!rsu.next()) {
                    i++;
                    String u_alter = "ALTER TABLE " + prefix + u.getKey() + " ADD uuid VARCHAR(48) DEFAULT '' AFTER " + u.getValue();
                    statement.executeUpdate(u_alter);
                }
            }
            for (String a : areaupdates) {
                String[] asplit = a.split(" ");
                String a_query = "SHOW COLUMNS FROM " + prefix + "areas LIKE '" + asplit[0] + "'";
                ResultSet rsa = statement.executeQuery(a_query);
                if (!rsa.next()) {
                    i++;
                    String a_alter = "ALTER TABLE " + prefix + "areas ADD " + a;
                    statement.executeUpdate(a_alter);
                }
            }
            for (String t : tardisupdates) {
                String[] tsplit = t.split(" ");
                String t_query = "SHOW COLUMNS FROM " + prefix + "tardis LIKE '" + tsplit[0] + "'";
                ResultSet rst = statement.executeQuery(t_query);
                if (!rst.next()) {
                    i++;
                    String t_alter = "ALTER TABLE " + prefix + "tardis ADD " + t;
                    statement.executeUpdate(t_alter);
                }
            }
            for (String p : prefsupdates) {
                String[] psplit = p.split(" ");
                String p_query = "SHOW COLUMNS FROM " + prefix + "player_prefs LIKE '" + psplit[0] + "'";
                ResultSet rsp = statement.executeQuery(p_query);
                if (!rsp.next()) {
                    i++;
                    String p_alter = "ALTER TABLE " + prefix + "player_prefs ADD " + p;
                    statement.executeUpdate(p_alter);
                }
            }
            for (String d : destsupdates) {
                String[] dsplit = d.split(" ");
                String d_query = "SHOW COLUMNS FROM " + prefix + "destinations LIKE '" + dsplit[0] + "'";
                ResultSet rsd = statement.executeQuery(d_query);
                if (!rsd.next()) {
                    i++;
                    String d_alter = "ALTER TABLE " + prefix + "destinations ADD " + d;
                    statement.executeUpdate(d_alter);
                }
            }
            for (String c : countupdates) {
                String[] csplit = c.split(" ");
                String c_query = "SHOW COLUMNS FROM " + prefix + "t_count LIKE '" + csplit[0] + "'";
                ResultSet rsc = statement.executeQuery(c_query);
                if (!rsc.next()) {
                    i++;
                    String c_alter = "ALTER TABLE " + prefix + "t_count ADD " + c;
                    statement.executeUpdate(c_alter);
                }
            }
            for (String o : portalsupdates) {
                String[] osplit = o.split(" ");
                String o_query = "SHOW COLUMNS FROM " + prefix + "portals LIKE '" + osplit[0] + "'";
                ResultSet rso = statement.executeQuery(o_query);
                if (!rso.next()) {
                    i++;
                    String o_alter = "ALTER TABLE " + prefix + "portals ADD " + o;
                    statement.executeUpdate(o_alter);
                }
            }
            for (String v : inventoryupdates) {
                String[] vsplit = v.split(" ");
                String v_query = "SHOW COLUMNS FROM " + prefix + "inventories LIKE '" + vsplit[0] + "'";
                ResultSet rsv = statement.executeQuery(v_query);
                if (!rsv.next()) {
                    i++;
                    String v_alter = "ALTER TABLE " + prefix + "inventories ADD " + v;
                    statement.executeUpdate(v_alter);
                }
            }
            for (String h : chameleonupdates) {
                String[] hsplit = h.split(" ");
                String h_query = "SHOW COLUMNS FROM " + prefix + "chameleon LIKE '" + hsplit[0] + "'";
                ResultSet rsh = statement.executeQuery(h_query);
                if (!rsh.next()) {
                    i++;
                    String h_alter = "ALTER TABLE " + prefix + "chameleon ADD " + h;
                    statement.executeUpdate(h_alter);
                }
            }
            for (String f : farmingupdates) {
                String[] fsplit = f.split(" ");
                String f_query = "SHOW COLUMNS FROM " + prefix + "farming LIKE '" + fsplit[0] + "'";
                ResultSet fsa = statement.executeQuery(f_query);
                if (!fsa.next()) {
                    i++;
                    String f_alter = "ALTER TABLE " + prefix + "farming ADD " + f;
                    statement.executeUpdate(f_alter);
                }
            }
            for (String s : sonicupdates) {
                String[] ssplit = s.split(" ");
                String s_query = "SHOW COLUMNS FROM " + prefix + "sonic LIKE '" + ssplit[0] + "'";
                ResultSet ssa = statement.executeQuery(s_query);
                if (!ssa.next()) {
                    i++;
                    String s_alter = "ALTER TABLE " + prefix + "sonic ADD " + s;
                    statement.executeUpdate(s_alter);
                }
            }
            // update data type for `data` in blocks
            String blockdata_check = "SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = '" + prefix + "blocks' AND COLUMN_NAME = 'data'";
            ResultSet rsbdc = statement.executeQuery(blockdata_check);
            if (rsbdc.next() && !rsbdc.getString("DATA_TYPE").equalsIgnoreCase("text")) {
                String blockdata_query = "ALTER TABLE " + prefix + "blocks CHANGE `data` `data` TEXT";
                statement.executeUpdate(blockdata_query);
            }
            // update data type for `time` in tag
            String tagtime_check = "SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = '" + prefix + "blocks' AND COLUMN_NAME = 'data'";
            ResultSet rsttc = statement.executeQuery(tagtime_check);
            if (rsttc.next() && !rsttc.getString("DATA_TYPE").equalsIgnoreCase("int")) {
                String tagtime_query = "ALTER TABLE " + prefix + "tag CHANGE `time` `time` BIGINT NULL DEFAULT '0'";
                statement.executeUpdate(tagtime_query);
            }
            // add biome to current location
            String bio_query = "SHOW COLUMNS FROM " + prefix + "current LIKE 'biome'";
            ResultSet rsbio = statement.executeQuery(bio_query);
            if (!rsbio.next()) {
                i++;
                String bio_alter = "ALTER TABLE " + prefix + "current ADD biome varchar(64) DEFAULT ''";
                statement.executeUpdate(bio_alter);
            }
            // add preset to homes
            String preset_query = "SHOW COLUMNS FROM " + prefix + "homes LIKE 'biome'";
            ResultSet rspreset = statement.executeQuery(preset_query);
            if (!rspreset.next()) {
                i++;
                String preset_alter = "ALTER TABLE " + prefix + "homes ADD preset varchar(64) DEFAULT ''";
                statement.executeUpdate(preset_alter);
            }
            // add tardis_id to dispersed
            String dispersed_query = "SHOW COLUMNS FROM " + prefix + "dispersed LIKE 'tardis_id'";
            ResultSet rsdispersed = statement.executeQuery(dispersed_query);
            if (!rsdispersed.next()) {
                i++;
                String dispersed_alter = "ALTER TABLE " + prefix + "dispersed ADD tardis_id int(11)";
                statement.executeUpdate(dispersed_alter);
                // update tardis_id column for existing records
                new TARDISDispersalUpdater(plugin).updateTardis_ids();
            }
            // add repair to t_count
            String rep_query = "SHOW COLUMNS FROM " + prefix + "t_count LIKE 'repair'";
            ResultSet rsrep = statement.executeQuery(rep_query);
            if (!rsrep.next()) {
                i++;
                String rep_alter = "ALTER TABLE " + prefix + "t_count ADD repair int(3) DEFAULT '0'";
                statement.executeUpdate(rep_alter);
            }
            // add task to vortex
            String vortex_query = "SHOW COLUMNS FROM " + prefix + "vortex LIKE 'task'";
            ResultSet rsvortex = statement.executeQuery(vortex_query);
            if (!rsvortex.next()) {
                i++;
                String vortex_alter = "ALTER TABLE " + prefix + "vortex ADD task int(11) DEFAULT '0'";
                statement.executeUpdate(vortex_alter);
            }
            // add post_blocks to room_progress
            String post_query = "SHOW COLUMNS FROM " + prefix + "room_progress LIKE 'post_blocks'";
            ResultSet rspost = statement.executeQuery(post_query);
            if (!rspost.next()) {
                i++;
                String post_alter = "ALTER TABLE " + prefix + "room_progress ADD post_blocks text NULL";
                statement.executeUpdate(post_alter);
            }
            // add chest_type to vaults
            String vct_query = "SHOW COLUMNS FROM " + prefix + "vaults LIKE 'chest_type'";
            ResultSet rsvct = statement.executeQuery(vct_query);
            if (!rsvct.next()) {
                i++;
                String vct_alter = "ALTER TABLE " + prefix + "vaults ADD chest_type varchar(8) DEFAULT 'DROP'";
                statement.executeUpdate(vct_alter);
            }
            // add y to archive
            String y_query = "SHOW COLUMNS FROM " + prefix + "archive LIKE 'y'";
            ResultSet rsy = statement.executeQuery(y_query);
            if (!rsy.next()) {
                i++;
                String y_alter = "ALTER TABLE " + prefix + "archive ADD y int(3) DEFAULT '64'";
                statement.executeUpdate(y_alter);
            }
            // transfer `void` data to `thevoid`, then remove `void` table
            String voidQuery = "SHOW TABLES LIKE '" + prefix + "void'";
            ResultSet rsvoid = statement.executeQuery(voidQuery);
            if (rsvoid.next()) {
                String getVoid = "SELECT * FROM '" + prefix + "void'";
                ResultSet rsv = statement.executeQuery(getVoid);
                while (rsv.next()) {
                    String transfer = "INSERT IGNORE INTO " + prefix + "thevoid (tardis_id) VALUES (" + rsv.getInt("tardis_id") + ")";
                    statement.executeUpdate(transfer);
                }
                String delVoid = "DROP TABLE '" + prefix + "void'";
                statement.executeUpdate(delVoid);
            }
        } catch (SQLException e) {
            plugin.debug("MySQL database add fields error: " + e.getMessage() + " " + e.getErrorCode());
        }
        if (i > 0) {
            plugin.getLogger().log(Level.INFO, "Added " + ChatColor.AQUA + i + ChatColor.RESET + " fields to the MySQL database!");
        }
    }
}
