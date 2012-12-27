/*
 * Copyright (C) 2012 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;

/**
 *
 * @author eccentric_nz
 */
public class ResultSetPlayerPrefs {

    private TARDISDatabase service = TARDISDatabase.getInstance();
    private Connection connection = service.getConnection();
    private TARDIS plugin;
    private HashMap<String, Object> where;
    private int pp_id;
    private String player;
    private boolean sfx_on;
    private boolean platform_on;
    private boolean quotes_on;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet
     * from the player_prefs table.
     *
     * @param plugin an instance of the main class.
     * @param where a HashMap<String, Object> of table fields and values to
     * refine the search.
     */
    public ResultSetPlayerPrefs(TARDIS plugin, HashMap<String, Object> where) {
        this.plugin = plugin;
        this.where = where;
    }

    /**
     * Retrieves an SQL ResultSet from the player_prefs table. This method
     * builds an SQL query string from the parameters supplied and then executes
     * the query. Use the getters to retrieve the results.
     */
    public boolean resultSet() {
        Statement statement = null;
        ResultSet rs = null;
        String wheres = "";
        if (where != null) {
            StringBuilder sbw = new StringBuilder();
            for (Map.Entry<String, Object> entry : where.entrySet()) {
                sbw.append(entry.getKey()).append(" = ");
                if (entry.getValue().getClass().equals(String.class)) {
                    sbw.append("'").append(entry.getValue()).append("',");
                } else {
                    sbw.append(entry.getValue()).append(",");
                }
            }
            wheres = " WHERE " + sbw.toString().substring(0, sbw.length() - 1);
            where.clear();
        }
        String query = "SELECT * FROM doors" + wheres;
        plugin.debug(query);
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.next()) {
                this.pp_id = rs.getInt("pp_id");
                this.player = rs.getString("player");
                this.sfx_on = rs.getBoolean("sfx_on");
                this.platform_on = rs.getBoolean("platform_on");
                this.quotes_on = rs.getBoolean("quotes_on");
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for tardis table! " + e.getMessage());
            return false;
        } finally {
            try {
                rs.close();
                statement.close();
            } catch (Exception e) {
                plugin.debug("Error closing tardis table! " + e.getMessage());
            }
        }
        return true;
    }

    public int getPp_id() {
        return pp_id;
    }

    public String getPlayer() {
        return player;
    }

    public boolean getSfx_on() {
        return sfx_on;
    }

    public boolean getPlatform_on() {
        return platform_on;
    }

    public boolean getQuotes_on() {
        return quotes_on;
    }
}