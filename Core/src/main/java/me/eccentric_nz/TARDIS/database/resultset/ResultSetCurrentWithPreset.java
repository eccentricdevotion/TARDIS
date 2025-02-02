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
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... a list of locations the TARDIS can
 * travel to.
 *
 * @author eccentric_nz
 */
public class ResultSetCurrentWithPreset {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private final String world;
    private final int x;
    private final int y;
    private final int z;
    private int id;
    private ChameleonPreset preset;
    private boolean sign = false;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the current locations table.
     *
     * @param plugin an instance of the main class.
     * @param world  the world the armour stand is in.
     * @param x      the X coordinate of the armour stand.
     * @param y      the Y coordinate of the armour stand.
     * @param z      the Z coordinate of the armour stand.
     */
    public ResultSetCurrentWithPreset(TARDIS plugin, String world, int x, int y, int z) {
        this.plugin = plugin;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
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

        // chameleon_preset & tardis_id + signOn preference
        String query = "SELECT " + prefix + "tardis.tardis_id, " + prefix + "tardis.uuid, " + prefix + "tardis.chameleon_preset FROM " + prefix + "tardis, " + prefix + "current WHERE " + prefix + "current.world = ? AND " + prefix + "current.x = ? AND " + prefix + "current.y = ? AND " + prefix + "current.z = ? AND " + prefix + "tardis.tardis_id = " + prefix + "current.tardis_id";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, world);
            statement.setInt(2, x);
            statement.setInt(3, y);
            statement.setInt(4, z);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                id = rs.getInt("tardis_id");
                String p = rs.getString("chameleon_preset");
                if (p.startsWith("ITEM:")) {
                    preset = ChameleonPreset.ITEM;
                } else {
                    try {
                        preset = ChameleonPreset.valueOf(p);
                    } catch (IllegalArgumentException e) {
                        preset = ChameleonPreset.FACTORY;
                    }
                }
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, rs.getString("uuid"));
                if (rsp.resultSet()) {
                    sign = rsp.isSignOn();
                }
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for current vehicle table! " + e.getMessage());
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
                plugin.debug("Error closing current vehicle table! " + e.getMessage());
            }
        }
    }

    public int getId() {
        return id;
    }

    public ChameleonPreset getPreset() {
        return preset;
    }

    public boolean hasSign() {
        return sign;
    }
}
