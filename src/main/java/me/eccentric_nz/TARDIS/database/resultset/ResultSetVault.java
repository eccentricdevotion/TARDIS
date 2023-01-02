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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the locations of the TARDIS vaults.
 * <p>
 * Control types: 0 = handbrake 1 = random button 2 = x-repeater 3 = z-repeater 4 = multiplier-repeater 5 =
 * environment-repeater 6 = artron button
 *
 * @author eccentric_nz
 */
public class ResultSetVault {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String where;
    private final int id;
    private final String prefix;
    private int vault_id;
    private int tardis_id;
    private String location;
    private int x;
    private int y;
    private int z;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the vaults table.
     *
     * @param plugin an instance of the main class.
     * @param where  the location of the drop chest.
     */
    public ResultSetVault(TARDIS plugin, String where) {
        this.plugin = plugin;
        this.where = where;
        id = -1;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the vaults table.
     *
     * @param plugin an instance of the main class.
     * @param id     the tardis_id of the player updating the drop chest.
     */
    public ResultSetVault(TARDIS plugin, int id) {
        this.plugin = plugin;
        where = "";
        this.id = id;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the vaults table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query;
        if (where.isEmpty()) {
            query = "SELECT * FROM " + prefix + "vaults WHERE tardis_id = ?";
        } else {
            query = "SELECT * FROM " + prefix + "vaults WHERE location = ?";
        }
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            if (where.isEmpty()) {
                statement.setInt(1, id);
            } else {
                statement.setString(1, where);
            }
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    vault_id = rs.getInt("v_id");
                    tardis_id = rs.getInt("tardis_id");
                    location = rs.getString("location");
                    x = rs.getInt("x");
                    y = rs.getInt("y");
                    z = rs.getInt("z");
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for vaults table! " + e.getMessage());
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
                plugin.debug("Error closing vaults table! " + e.getMessage());
            }
        }
        return true;
    }

    public int getVault_id() {
        return vault_id;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public String getLocation() {
        return location;
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
}
