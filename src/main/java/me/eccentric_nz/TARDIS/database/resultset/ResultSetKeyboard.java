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
package me.eccentric_nz.TARDIS.database.resultset;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetKeyboard {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String where;
    private final String prefix;
    private int c_id;
    private int tardis_id;
    private int type;
    private String location;
    private int secondary;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the controls table.
     *
     * @param plugin an instance of the main class.
     * @param where  a HashMap&lt;String, Object&gt; of table fields and values to refine the search.
     */
    public ResultSetKeyboard(TARDIS plugin, String where) {
        this.plugin = plugin;
        this.where = where;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the controls table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        if (where == null) {
            return false;
        }
        String query = "SELECT * FROM " + prefix + "controls WHERE `type` IN (7,22) AND `location` = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, where);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    c_id = rs.getInt("c_id");
                    tardis_id = rs.getInt("tardis_id");
                    type = rs.getInt("type");
                    location = rs.getString("location");
                    secondary = rs.getInt("secondary");
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for [keyboard] controls table! " + e.getMessage());
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
                plugin.debug("Error closing [keyboard] controls table! " + e.getMessage());
            }
        }
        return true;
    }

    public int getC_id() {
        return c_id;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public int getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public int getSecondary() {
        return secondary;
    }
}
