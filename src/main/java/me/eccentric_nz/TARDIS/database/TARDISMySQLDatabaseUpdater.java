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
    private final HashMap<String, String> uuidUpdates = new HashMap<String, String>();
    private final Statement statement;
    private final TARDIS plugin;

    public TARDISMySQLDatabaseUpdater(TARDIS plugin, Statement statement) {
        this.plugin = plugin;
        this.statement = statement;
        uuidUpdates.put("achievements", "a_id");
        uuidUpdates.put("ars", "tardis_id");
        uuidUpdates.put("player_prefs", "pp_id");
        uuidUpdates.put("storage", "tardis_id");
        uuidUpdates.put("t_count", "t_id");
        uuidUpdates.put("tardis", "tardis_id");
        uuidUpdates.put("travellers", "tardis_id");
        tardisupdates.add("renderer varchar(512) DEFAULT ''");
        tardisupdates.add("zero varchar(512) DEFAULT ''");
        prefsupdates.add("language varchar(32) DEFAULT 'AUTO_DETECT'");
        prefsupdates.add("build_on int(1) DEFAULT '1'");
        prefsupdates.add("minecart_on int(1) DEFAULT '0'");
        prefsupdates.add("renderer_on int(1) DEFAULT '1'");
        prefsupdates.add("wool_lights_on int(1) DEFAULT '0'");
        destsupdates.add("slot int(1) DEFAULT '-1'");
    }

    /**
     * Adds new fields to tables in the database.
     */
    public void updateTables() {
        int i = 0;
        try {
            for (Map.Entry<String, String> u : uuidUpdates.entrySet()) {
                String a_query = "SHOW COLUMNS FROM " + u.getKey() + " LIKE 'uuid'";
                ResultSet rsu = statement.executeQuery(a_query);
                if (!rsu.next()) {
                    i++;
                    String u_alter = "ALTER TABLE " + u.getKey() + " ADD uuid VARCHAR(48) DEFAULT '' AFTER " + u.getValue();
                    statement.executeUpdate(u_alter);
                }
            }
            for (String t : tardisupdates) {
                String[] tsplit = t.split(" ");
                String t_query = "SHOW COLUMNS FROM tardis LIKE '" + tsplit[0] + "'";
                ResultSet rst = statement.executeQuery(t_query);
                if (!rst.next()) {
                    i++;
                    String t_alter = "ALTER TABLE tardis ADD " + t;
                    statement.executeUpdate(t_alter);
                }
            }
            for (String p : prefsupdates) {
                String[] psplit = p.split(" ");
                String p_query = "SHOW COLUMNS FROM player_prefs LIKE '" + psplit[0] + "'";
                ResultSet rsp = statement.executeQuery(p_query);
                if (!rsp.next()) {
                    i++;
                    String p_alter = "ALTER TABLE player_prefs ADD " + p;
                    statement.executeUpdate(p_alter);
                }
            }
            for (String d : destsupdates) {
                String[] dsplit = d.split(" ");
                String d_query = "SHOW COLUMNS FROM destinations LIKE '" + dsplit[0] + "'";
                ResultSet rsd = statement.executeQuery(d_query);
                if (!rsd.next()) {
                    i++;
                    String d_alter = "ALTER TABLE player_prefs ADD " + d;
                    statement.executeUpdate(d_alter);
                }
            }
        } catch (SQLException e) {
            plugin.debug("MySQL database add fields error: " + e.getMessage() + e.getErrorCode());
        }
        if (i > 0) {
            plugin.getConsole().sendMessage(TARDIS.plugin.getPluginName() + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " fields to the MySQL database!");
        }
    }
}
