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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;

/**
 *
 * @author eccentric_nz
 */
public class ResultSetDoors {

    private TARDISDatabase service = TARDISDatabase.getInstance();
    private Connection connection = service.getConnection();
    private TARDIS plugin;
    private HashMap<String, Object> where;
    private boolean multiple;
    private int door_id;
    private int tardis_id;
    private int door_type;
    private String door_location;
    private TARDISConstants.COMPASS door_direction;
    private ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet
     * from the doors table.
     *
     * @param plugin an instance of the main class.
     * @param where a HashMap<String, Object> of table fields and values to
     * refine the search.
     */
    public ResultSetDoors(TARDIS plugin, HashMap<String, Object> where, boolean multiple) {
        this.plugin = plugin;
        this.where = where;
        this.multiple = multiple;
    }

    /**
     * Retrieves an SQL ResultSet from the doors table. This method builds an
     * SQL query string from the parameters supplied and then executes the
     * query. Use the getters to retrieve the results.
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
                    sbw.append("'").append(entry.getValue()).append("' AND ");
                } else {
                    sbw.append(entry.getValue()).append(" AND ");
                }
            }
            wheres = " WHERE " + sbw.toString().substring(0, sbw.length() - 5);
            where.clear();
        }
        String query = "SELECT * FROM doors" + wheres;
        plugin.debug(query);
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    if (multiple) {
                        HashMap<String, String> row = new HashMap<String, String>();
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columns = rsmd.getColumnCount();
                        for (int i = 1; i < columns + 1; i++) {
                            row.put(rsmd.getColumnName(i).toLowerCase(), rs.getString(i));
                        }
                        data.add(row);
                    }
                    this.door_id = rs.getInt("door_id");
                    this.tardis_id = rs.getInt("tardis_id");
                    this.door_type = rs.getInt("door_type");
                    this.door_location = rs.getString("door_location");
                    this.door_direction = TARDISConstants.COMPASS.valueOf(rs.getString("door_direction"));
                }
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

    public int getDoor_id() {
        return door_id;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public int getDoor_type() {
        return door_type;
    }

    public String getDoor_location() {
        return door_location;
    }

    public TARDISConstants.COMPASS getDoor_direction() {
        return door_direction;
    }

    public ArrayList<HashMap<String, String>> getData() {
        return data;
    }
}