/*
 * Copyright (C) 2024 eccentric_nz
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
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the location of the TARDIS Police Box
 * blocks.
 *
 * @author eccentric_nz
 */
public class ResultSetScreen {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final int tardis_id;
    private final String prefix;
    private String preset;
    private String world;
    private String location;
    private String x;
    private String y;
    private String z;
    private String artronLevel;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the blocks table.
     *
     * @param plugin    - an instance of the main class
     * @param tardis_id - the tardis_id to get the data for
     */
    public ResultSetScreen(TARDIS plugin, int tardis_id) {
        this.plugin = plugin;
        this.tardis_id = tardis_id;
        prefix = this.plugin.getPrefix();
    }

    public void locationAsync(final ResultSetControlCallback callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            boolean hasResult = locationData();
            // go back to the tick loop
            Bukkit.getScheduler().runTask(plugin, () -> {
                // call the callback with the result
                callback.onDone(hasResult, this);
            });
        });
    }

    /**
     * Retrieves an SQL ResultSet from the tardis, current and controls tables. This method builds an SQL query string
     * from the parameters supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean locationData() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT " + prefix + "current.* FROM " + prefix + "tardis, " + prefix + "current " + "WHERE " + prefix + "tardis.tardis_id = ? AND " + prefix + "tardis.abandoned = 0 " + "AND " + prefix + "tardis.tardis_id = " + prefix + "current.tardis_id";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, tardis_id);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                world = rs.getString("world");
                x = rs.getString("x");
                y = rs.getString("y");
                z = rs.getString("z");
                location = x + ", " + y + ", " + z;
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for console tables! " + e.getMessage());
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
                plugin.debug("Error closing console tables! " + e.getMessage());
            }
        }
        return true;
    }

    public void artronAsync(final ResultSetControlCallback callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            boolean hasResult = artronData();
            // go back to the tick loop
            Bukkit.getScheduler().runTask(plugin, () -> {
                // call the callback with the result
                callback.onDone(hasResult, this);
            });
        });
    }

    /**
     * Retrieves an SQL ResultSet from the tardis, current and controls tables. This method builds an SQL query string
     * from the parameters supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean artronData() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT artron_level, chameleon_preset FROM " + prefix + "tardis WHERE tardis_id = ? AND abandoned = 0";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, tardis_id);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                artronLevel = rs.getString("artron_level");
                preset = rs.getString("chameleon_preset");
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for controls table! " + e.getMessage());
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
                plugin.debug("Error closing controls table! " + e.getMessage());
            }
        }
        return true;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public String getPreset() {
        return preset;
    }

    public String getWorld() {
        return world;
    }

    public String getLocation() {
        return location;
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public String getZ() {
        return z;
    }

    public String getArtronLevel() {
        return artronLevel;
    }

    public interface ResultSetControlCallback {
        void onDone(boolean hasResult, ResultSetScreen resultSetConsole);
    }
}
