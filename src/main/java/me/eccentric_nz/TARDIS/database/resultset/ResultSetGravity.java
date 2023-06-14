/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.database.resultset;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

/**
 * Anti-gravity was the process of creating objects free from or releasing objects from the force of gravity. The Time
 * Lords' Matrix held many advanced scientific techniques, including the secret of anti-gravity power.
 *
 * @author eccentric_nz
 */
public class ResultSetGravity {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final HashMap<String, Object> where;
    private final boolean multiple;
    private final ArrayList<HashMap<String, String>> data = new ArrayList<>();
    private final String prefix;
    private int gravity_id;
    private int tardis_id;
    private String location;
    private int direction;
    private double distance;
    private double velocity;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the gravity_well table.
     *
     * @param plugin   an instance of the main class.
     * @param where    a HashMap&lt;String, Object&gt; of table fields and values to refine the search.
     * @param multiple boolean determining whether to return multiple records
     */
    public ResultSetGravity(TARDIS plugin, HashMap<String, Object> where, boolean multiple) {
        this.plugin = plugin;
        this.where = where;
        this.multiple = multiple;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the doors table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String wheres = "";
        if (where != null) {
            StringBuilder sbw = new StringBuilder();
            where.forEach((key, value) -> sbw.append(key).append(" = ? AND "));
            wheres = " WHERE " + sbw.substring(0, sbw.length() - 5);
        }
        String query = "SELECT * FROM " + prefix + "gravity_well" + wheres;
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            if (where != null) {
                int s = 1;
                for (Map.Entry<String, Object> entry : where.entrySet()) {
                    if (entry.getValue() instanceof String) {
                        statement.setString(s, entry.getValue().toString());
                    } else {
                        if (entry.getValue() instanceof Integer) {
                            statement.setInt(s, (Integer) entry.getValue());
                        } else if (entry.getValue() instanceof Double) {
                            statement.setDouble(s, (Double) entry.getValue());
                        } else if (entry.getValue() instanceof Float) {
                            statement.setFloat(s, (Float) entry.getValue());
                        } else if (entry.getValue() instanceof Long) {
                            statement.setLong(s, (Long) entry.getValue());
                        }
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
                    gravity_id = rs.getInt("g_id");
                    tardis_id = rs.getInt("tardis_id");
                    location = rs.getString("location");
                    direction = rs.getInt("direction");
                    distance = rs.getDouble("distance");
                    velocity = rs.getDouble("velocity");
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for gravity_well table! " + e.getMessage());
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
                plugin.debug("Error closing gravity_well table! " + e.getMessage());
            }
        }
        return true;
    }

    public int getGravity_id() {
        return gravity_id;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public String getLocation() {
        return location;
    }

    public int getDirection() {
        return direction;
    }

    public double getDistance() {
        return distance;
    }

    public double getVelocity() {
        return velocity;
    }

    public ArrayList<HashMap<String, String>> getData() {
        return data;
    }
}
