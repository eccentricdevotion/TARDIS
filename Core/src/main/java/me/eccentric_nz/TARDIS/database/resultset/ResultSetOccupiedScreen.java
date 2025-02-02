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

import com.mojang.datafixers.util.Pair;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * The TARDIS scanner, also known as the video screen, vid-screen or, in its smoke-like monitor-less configuration,
 * the smoke screen is the main method for the occupants of the vessel to observe the outside environment.
 * The appearance and specifications of the scanner system have varied significantly in the course of the Doctor's travels.
 *
 * @author eccentric_nz
 */
public class ResultSetOccupiedScreen {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final List<Pair<Integer, UUID>> data = new ArrayList<>();
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the travellers table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetOccupiedScreen(TARDIS plugin) {
        this.plugin = plugin;
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
        Statement statement = null;
        ResultSet rs = null;
        long time = System.currentTimeMillis() - 86400000;
        String query = "SELECT DISTINCT " + prefix + "travellers.tardis_id, " + prefix + "interactions.uuid FROM "
                + prefix + "travellers, " + prefix + "tardis, " + prefix + "interactions WHERE "
                + prefix + "interactions.control = 'SCREEN' AND "
                + prefix + "tardis.lastuse > " + time + " AND "
                + prefix + "tardis.tardis_id = " + prefix + "travellers.tardis_id  AND "
                + prefix + "tardis.tardis_id = " + prefix + "interactions.tardis_id";
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    UUID uuid;
                    try {
                        uuid = UUID.fromString(rs.getString("uuid"));
                    } catch (IllegalArgumentException e) {
                        uuid = TARDISStaticUtils.getZERO_UUID();
                    }
                    Pair<Integer, UUID> pair = new Pair<>(rs.getInt("tardis_id"), uuid);
                    data.add(pair);
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for ResultSetOccupiedScreen! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing ResultSetOccupiedScreen! " + e.getMessage());
            }
        }
    }

    public List<Pair<Integer, UUID>> getData() {
        return Collections.unmodifiableList(data);
    }

    public interface ResultSetOccupiedCallback {

        void onDone(ResultSetOccupiedScreen resultSetOccupied);
    }
}
