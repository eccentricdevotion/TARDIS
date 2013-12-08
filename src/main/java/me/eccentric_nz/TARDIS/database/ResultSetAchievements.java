/*
 * Copyright (C) 2013 eccentric_nz
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;

/**
 * Chronomium was a time-active element that displaced time around it. In its
 * unprocessed form, chronomium slowed down time around it. In its refined
 * state, it could be used to accelerate time, achieve time travel, and protect
 * against temporal fields.
 *
 * @author eccentric_nz
 */
public class ResultSetAchievements {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final HashMap<String, Object> where;
    private final boolean multiple;
    private int a_id;
    private String player;
    private String name;
    private String amount;
    private boolean completed;
    private final ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet
     * from the achievements table.
     *
     * @param plugin an instance of the main class.
     * @param where a String location to check.
     * @param multiple a boolean setting whether to retrieve more than on
     * record, it true returns an ArrayList that can be looped through later.
     */
    public ResultSetAchievements(TARDIS plugin, HashMap<String, Object> where, boolean multiple) {
        this.plugin = plugin;
        this.where = where;
        this.multiple = multiple;
    }

    /**
     * Retrieves an SQL ResultSet from the achievements table. This method
     * returns true if a matching record was found.
     *
     * @return
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String wheres = "";
        if (where != null) {
            StringBuilder sbw = new StringBuilder();
            for (Map.Entry<String, Object> entry : where.entrySet()) {
                sbw.append(entry.getKey()).append(" = ? AND ");
            }
            wheres = " WHERE " + sbw.toString().substring(0, sbw.length() - 5);
        }
        String query = "SELECT * FROM achievements" + wheres;
        //plugin.debug(query);
        try {
            statement = connection.prepareStatement(query);
            if (where != null) {
                int s = 1;
                for (Map.Entry<String, Object> entry : where.entrySet()) {
                    if (entry.getValue().getClass().equals(String.class)) {
                        statement.setString(s, entry.getValue().toString());
                    } else {
                        statement.setInt(s, plugin.utils.parseNum(entry.getValue().toString()));
                    }
                    s++;
                }
                where.clear();
            }
            rs = statement.executeQuery();
            if (rs.next()) {
                if (multiple) {
                    HashMap<String, String> row = new HashMap<String, String>();
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columns = rsmd.getColumnCount();
                    for (int i = 1; i < columns + 1; i++) {
                        row.put(rsmd.getColumnName(i).toLowerCase(Locale.ENGLISH), rs.getString(i));
                    }
                    data.add(row);
                }
                this.a_id = rs.getInt("a_id");
                this.player = rs.getString("player");
                this.name = rs.getString("name");
                this.amount = rs.getString("amount");
                if (rs.wasNull()) {
                    this.amount = "";
                }
                this.completed = rs.getBoolean("completed");
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for achievements table! " + e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing achievements table! " + e.getMessage());
            }
        }
        return true;
    }

    public int getA_id() {
        return a_id;
    }

    public String getPlayer() {
        return player;
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }

    public boolean isCompleted() {
        return completed;
    }

    public ArrayList<HashMap<String, String>> getData() {
        return data;
    }
}
