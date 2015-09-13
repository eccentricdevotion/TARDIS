/*
 * Copyright (C) 2014 eccentric_nz
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;

/**
 * TARDISes prefer the environment of the Space-Time Vortex to the four
 * dimensional world. They have Curiosity Circuits to encourage them to leave
 * the Vortex.
 *
 * @author eccentric_nz
 */
public class TARDISMySQLDatabaseUpdater {

    private final List<String> tardisupdates = new ArrayList<String>();
    private final List<String> prefsupdates = new ArrayList<String>();
    private final List<String> destsupdates = new ArrayList<String>();
    private final List<String> countupdates = new ArrayList<String>();
    private final List<String> inventoryupdates = new ArrayList<String>();
    private final HashMap<String, String> uuidUpdates = new HashMap<String, String>();
    private final Statement statement;
    private final TARDIS plugin;
    private final String prefix;

    public TARDISMySQLDatabaseUpdater(TARDIS plugin, Statement statement) {
        this.plugin = plugin;
        this.prefix = this.plugin.getPrefix();
        this.statement = statement;
        uuidUpdates.put("achievements", "a_id");
        uuidUpdates.put("ars", "tardis_id");
        uuidUpdates.put("player_prefs", "pp_id");
        uuidUpdates.put("storage", "tardis_id");
        uuidUpdates.put("t_count", "t_id");
        uuidUpdates.put("tardis", "tardis_id");
        uuidUpdates.put("travellers", "tardis_id");
        tardisupdates.add("lights_on int(1) DEFAULT '1'");
        tardisupdates.add("powered_on int(1) DEFAULT '0'");
        tardisupdates.add("renderer varchar(512) DEFAULT ''");
        tardisupdates.add("hutch varchar(512) DEFAULT ''");
        tardisupdates.add("siege_on int(1) DEFAULT '0'");
        tardisupdates.add("zero varchar(512) DEFAULT ''");
        tardisupdates.add("last_known_name varchar(32) DEFAULT ''");
        prefsupdates.add("language varchar(32) DEFAULT 'AUTO_DETECT'");
        prefsupdates.add("build_on int(1) DEFAULT '1'");
        prefsupdates.add("ctm_on int(1) DEFAULT '0'");
        prefsupdates.add("difficulty int(1) DEFAULT '0'");
        prefsupdates.add("flying_mode int(1) DEFAULT '1'");
        prefsupdates.add("farm_on int(1) DEFAULT '0'");
        prefsupdates.add("lanterns_on int(1) DEFAULT '0'");
        prefsupdates.add("minecart_on int(1) DEFAULT '0'");
        prefsupdates.add("renderer_on int(1) DEFAULT '1'");
        prefsupdates.add("sign_on int(1) DEFAULT '1'");
        prefsupdates.add("wool_lights_on int(1) DEFAULT '0'");
        prefsupdates.add("travelbar_on int(1) DEFAULT '0'");
        prefsupdates.add("auto_siege_on int(1) DEFAULT '0'");
        prefsupdates.add("siege_wall varchar(64) DEFAULT 'GREY_CLAY'");
        prefsupdates.add("siege_floor varchar(64) DEFAULT 'BLACK_CLAY'");
        destsupdates.add("slot int(1) DEFAULT '-1'");
        countupdates.add("grace int(3) DEFAULT '0'");
        inventoryupdates.add("attributes text");
        inventoryupdates.add("armour_attributes text");
    }

    /**
     * Adds new fields to tables in the database.
     */
    public void updateTables() {
        int i = 0;
        try {
            for (Map.Entry<String, String> u : uuidUpdates.entrySet()) {
                String a_query = "SHOW COLUMNS FROM " + prefix + u.getKey() + " LIKE 'uuid'";
                ResultSet rsu = statement.executeQuery(a_query);
                if (!rsu.next()) {
                    i++;
                    String u_alter = "ALTER TABLE " + prefix + u.getKey() + " ADD uuid VARCHAR(48) DEFAULT '' AFTER " + u.getValue();
                    statement.executeUpdate(u_alter);
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
            // add biome to current location
            String bio_query = "SHOW COLUMNS FROM " + prefix + "current LIKE 'biome'";
            ResultSet rsbio = statement.executeQuery(bio_query);
            if (!rsbio.next()) {
                i++;
                String bio_alter = "ALTER TABLE " + prefix + "current ADD biome varchar(64) DEFAULT ''";
                statement.executeUpdate(bio_alter);
            }
            // add parking_distance to areas
            String park_query = "SHOW COLUMNS FROM " + prefix + "areas LIKE 'parking_distance'";
            ResultSet rspark = statement.executeQuery(park_query);
            if (!rspark.next()) {
                i++;
                String park_alter = "ALTER TABLE " + prefix + "areas ADD parking_distance int(2) DEFAULT '2'";
                statement.executeUpdate(park_alter);
            }
        } catch (SQLException e) {
            plugin.debug("MySQL database add fields error: " + e.getMessage() + e.getErrorCode());
        }
        if (i > 0) {
            plugin.getConsole().sendMessage(TARDIS.plugin.getPluginName() + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " fields to the MySQL database!");
        }
    }
}
