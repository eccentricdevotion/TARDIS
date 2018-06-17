/*
 * Copyright (C) 2018 eccentric_nz
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

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... a list of locations the TARDIS can
 * travel to.
 *
 * @author eccentric_nz
 */
public class ResultSetDestinations {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final HashMap<String, Object> where;
    private final boolean multiple;
    private int dest_id;
    private int tardis_id;
    private String dest_name;
    private String world;
    private int x;
    private int y;
    private int z;
    private String direction;
    private String preset;
    private String bind;
    private int type;
    private boolean submarine;
    private int slot;
    private final ArrayList<HashMap<String, String>> data = new ArrayList<>();
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the destinations table.
     *
     * @param plugin   an instance of the main class.
     * @param where    a HashMap<String, Object> of table fields and values to refine the search.
     * @param multiple a boolean indicating whether multiple rows should be fetched
     */
    public ResultSetDestinations(TARDIS plugin, HashMap<String, Object> where, boolean multiple) {
        this.plugin = plugin;
        this.where = where;
        this.multiple = multiple;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the destinations table. This method builds an SQL query string from the
     * parameters supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String wheres = "";
        if (where != null) {
            StringBuilder sbw = new StringBuilder();
            where.entrySet().forEach((entry) -> {
                sbw.append(entry.getKey()).append(" = ? AND ");
            });
            wheres = " WHERE " + sbw.toString().substring(0, sbw.length() - 5);
        }
        String query = "SELECT * FROM " + prefix + "destinations" + wheres;
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            if (where != null) {
                int s = 1;
                for (Map.Entry<String, Object> entry : where.entrySet()) {
                    if (entry.getValue() instanceof String) {
                        statement.setString(s, entry.getValue().toString());
                    } else {
                        statement.setInt(s, (Integer) entry.getValue());
                    }
                    s++;
                }
                where.clear();
            }
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    if (multiple) {
                        HashMap<String, String> row = new HashMap<>();
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columns = rsmd.getColumnCount();
                        for (int i = 1; i < columns + 1; i++) {
                            row.put(rsmd.getColumnName(i).toLowerCase(Locale.ENGLISH), rs.getString(i));
                        }
                        data.add(row);
                    }
                    dest_id = rs.getInt("dest_id");
                    tardis_id = rs.getInt("tardis_id");
                    dest_name = rs.getString("dest_name");
                    world = rs.getString("world");
                    x = rs.getInt("x");
                    y = rs.getInt("y");
                    z = rs.getInt("z");
                    direction = rs.getString("direction");
                    preset = rs.getString("preset");
                    submarine = rs.getBoolean("submarine");
                    bind = rs.getString("bind");
                    type = rs.getInt("type");
                    slot = rs.getInt("slot");
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for destinations table! " + e.getMessage());
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
                plugin.debug("Error closing destinations table! " + e.getMessage());
            }
        }
        return true;
    }

    public int getDest_id() {
        return dest_id;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public String getDest_name() {
        return dest_name;
    }

    public String getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public String getDirection() {
        return direction;
    }

    public String getPreset() {
        return preset;
    }

    public boolean isSubmarine() {
        return submarine;
    }

    public String getBind() {
        return bind;
    }

    public int getType() {
        return type;
    }

    public int getSlot() {
        return slot;
    }

    public ArrayList<HashMap<String, String>> getData() {
        return data;
    }
}
