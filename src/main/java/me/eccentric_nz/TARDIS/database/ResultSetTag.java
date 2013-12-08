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
import me.eccentric_nz.TARDIS.TARDIS;

/**
 * Many facts, figures, and formulas are contained within the Matrix,
 * including... the location of the TARDIS Police Box blocks.
 *
 * @author eccentric_nz
 */
public class ResultSetTag {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet
     * from the tag table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetTag(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Retrieves an SQL ResultSet from the blocks table. This method builds an
     * SQL query string from the parameters supplied and then executes the
     * query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM tag ORDER BY time DESC LIMIT 5";
        try {
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                //plugin.debug(query);
                while (rs.next()) {
                    HashMap<String, String> row = new HashMap<String, String>();
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columns = rsmd.getColumnCount();
                    for (int i = 1; i < columns + 1; i++) {
                        row.put(rsmd.getColumnName(i).toLowerCase(Locale.ENGLISH), rs.getString(i));

                    }
                    data.add(row);
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for blocks table! " + e.getMessage());
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
                plugin.debug("Error closing blocks table! " + e.getMessage());
            }
        }
        return true;
    }

    public ArrayList<HashMap<String, String>> getData() {
        return data;
    }
}
