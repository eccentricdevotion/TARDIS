/*
 * Copyright (C) 2025 eccentric_nz
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
import me.eccentric_nz.TARDIS.enumeration.SmelterChest;

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
    private final String prefix;
    private int vault_id;
    private int tardis_id;
    private String location;
    private SmelterChest chestType;
    private int x;
    private int y;
    private int z;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the vaults table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetVault(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the vaults table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @param where the location of the drop chest.
     * @return true or false depending on whether any data matches the query.
     */
    public boolean fromLocation(String where) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "vaults WHERE location = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, where);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    vault_id = rs.getInt("v_id");
                    tardis_id = rs.getInt("tardis_id");
                    location = rs.getString("location");
                    chestType = SmelterChest.valueOf(rs.getString("chest_type"));
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

    /**
     * Retrieves an SQL ResultSet from the vaults table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @param where the location of the drop chest.
     * @param chestType the type of chest to get the record for.
     * @return true or false depending on whether any data matches the query.
     */
    public boolean fromLocationAndChestType(String where, SmelterChest chestType) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "vaults WHERE location = ? AND chest_type = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, where);
            statement.setString(2, chestType.toString());
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    vault_id = rs.getInt("v_id");
                    tardis_id = rs.getInt("tardis_id");
                    location = rs.getString("location");
                    this.chestType = chestType;
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

    /**
     * Retrieves an SQL ResultSet from the vaults table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @param id the tardis_id of the player updating the drop chest.
     * @return true or false depending on whether any data matches the query.
     */
    public boolean fromId(int id) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "vaults WHERE id = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    vault_id = rs.getInt("v_id");
                    tardis_id = rs.getInt("tardis_id");
                    location = rs.getString("location");
                    chestType = SmelterChest.valueOf(rs.getString("chest_type"));
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

    /**
     * Retrieves an SQL ResultSet from the vaults table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @param id the tardis_id of the player updating the drop chest.
     * @param chestType the type of chest to get the record for.
     * @return true or false depending on whether any data matches the query.
     */
    public boolean fromIdAndChestType(int id, SmelterChest chestType) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "vaults WHERE tardis_id = ? AND chest_type = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.setString(2, chestType.toString());
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    vault_id = rs.getInt("v_id");
                    tardis_id = rs.getInt("tardis_id");
                    location = rs.getString("location");
                    this.chestType = chestType;
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

    public SmelterChest getChestType() {
        return chestType;
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
