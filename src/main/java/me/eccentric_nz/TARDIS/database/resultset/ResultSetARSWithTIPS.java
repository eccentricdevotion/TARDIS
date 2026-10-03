/*
 * Copyright (C) 2026 eccentric_nz
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

public class ResultSetARSWithTIPS {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private int tips;
    private int east;
    private int id;
    private int south;
    private int layer;
    private String json;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the ars table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetARSWithTIPS(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the ars table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean fromId(int tardis_id) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT " + prefix + "ars" + ".*, " + prefix + "tardis.tips FROM " + prefix + "ars, " + prefix + "tardis WHERE "
                + prefix + "ars.tardis_id = ? AND "
                + prefix + "ars.tardis_id = " + prefix + "tardis.tardis_id";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, tardis_id);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    tips = rs.getInt("tips");
                    id = rs.getInt("ars_id");
                    east = rs.getInt("ars_x_east");
                    south = rs.getInt("ars_z_south");
                    layer = rs.getInt("ars_y_layer");
                    json = rs.getString("json");
                    if (rs.wasNull()) {
                        json = "";
                    }
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for ars table! " + e.getMessage());
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
                plugin.debug("Error closing ars table! " + e.getMessage());
            }
        }
        return true;
    }

    public int getTips() {
        return tips;
    }

    public int getId() {
        return id;
    }

    public int getEast() {
        return east;
    }

    public int getSouth() {
        return south;
    }

    public int getLayer() {
        return layer;
    }

    public String getJson() {
        return json;
    }
}
