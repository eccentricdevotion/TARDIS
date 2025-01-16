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
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * The TARDIS scanner, also known as the video screen, vid-screen or, in its smoke-like monitor-less configuration, the
 * smoke screen is the main method for the occupants of the vessel to observe the outside environment. The appearance
 * and specifications of the scanner system have varied significantly in the course of the Doctor's travels.
 *
 * @author eccentric_nz
 */
public class ResultSetScreenText {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final int id;
    private final String prefix;
    private UUID uuid;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the travellers table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetScreenText(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        prefix = this.plugin.getPrefix();
    }

    public void resultSetAsync(final ResultSetOccupiedCallback callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            resultSet();
            // go back to the tick loop
            Bukkit.getScheduler().runTask(plugin, () -> {
                // call the callback with the result
                callback.onDone(this);
            });
        });
    }

    /**
     * Retrieves an SQL ResultSet from the travellers table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     */
    public void resultSet() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT uuid FROM " + prefix + "interactions WHERE control = 'SCREEN' AND tardis_id = ?";
        try {
            service.testConnection(connection);
            ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    try {
                        uuid = UUID.fromString(rs.getString("uuid"));
                    } catch (IllegalArgumentException e) {
                        uuid = TARDISStaticUtils.getZERO_UUID();
                    }
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for ResultSetOccupiedScreen! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing ResultSetOccupiedScreen! " + e.getMessage());
            }
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public interface ResultSetOccupiedCallback {

        void onDone(ResultSetScreenText resultSetOccupied);
    }
}
