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
package me.eccentric_nz.tardis.database.resultset;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.TARDISDatabaseConnection;
import me.eccentric_nz.tardis.enumeration.COMPASS;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the location of the tardis doors in
 * their different dimensions.
 *
 * @author eccentric_nz
 */
public class ResultSetDoors {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDISPlugin plugin;
    private final HashMap<String, Object> where;
    private final boolean multiple;
    private final ArrayList<HashMap<String, String>> data = new ArrayList<>();
    private final String prefix;
    private int doorId;
    private int tardisId;
    private int doorType;
    private String doorLocation;
    private COMPASS doorDirection;
    private boolean locked;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the doors table.
     *
     * @param plugin   an instance of the main class.
     * @param where    a HashMap&lt;String, Object&gt; of table fields and values to refine the search.
     * @param multiple a boolean setting whether to retrieve more than on record, it true returns an ArrayList that can
     *                 be looped through later.
     */
    public ResultSetDoors(TARDISPlugin plugin, HashMap<String, Object> where, boolean multiple) {
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
        String query = "SELECT * FROM " + prefix + "doors" + wheres;
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
                    doorId = rs.getInt("door_id");
                    tardisId = rs.getInt("tardis_id");
                    doorType = rs.getInt("door_type");
                    doorLocation = rs.getString("door_location");
                    doorDirection = COMPASS.valueOf(rs.getString("door_direction"));
                    locked = rs.getBoolean("locked");
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for doors table! " + e.getMessage());
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
                plugin.debug("Error closing doors table! " + e.getMessage());
            }
        }
        return true;
    }

    public int getDoorId() {
        return doorId;
    }

    public int getTardisId() {
        return tardisId;
    }

    public int getDoorType() {
        return doorType;
    }

    public String getDoorLocation() {
        return doorLocation;
    }

    public COMPASS getDoorDirection() {
        return doorDirection;
    }

    public boolean isLocked() {
        return locked;
    }

    public ArrayList<HashMap<String, String>> getData() {
        return data;
    }
}
