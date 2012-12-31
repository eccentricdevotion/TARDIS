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
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;

/**
 *
 * @author eccentric_nz
 */
public class ResultSetEntities {

    private TARDISDatabase service = TARDISDatabase.getInstance();
    private Connection connection = service.getConnection();
    private TARDIS plugin;
    private String uuid;
    private boolean multiple;
    private ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

    /**
     * Creates a class instance that can be used to check whether a parking spot
     * in a TARDIS area is free for use.
     *
     * @param plugin an instance of the main class.
     * @param where a String location to check.
     */
    public ResultSetEntities(TARDIS plugin, boolean multiple) {
        this.plugin = plugin;
        this.multiple = multiple;
    }

    /**
     * Retrieves an SQL ResultSet from the tardis table. This method returns
     * true if a matching record was found.
     */
    public boolean resultSet() {
        Statement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM entities";
        //plugin.debug(query);
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
                    this.uuid = rs.getString("uuid");
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

    public String getUuid() {
        return uuid;
    }

    public ArrayList<HashMap<String, String>> getData() {
        return data;
    }
}