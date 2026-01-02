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
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the companions who travel in the
 * TARDIS.
 * <p>
 * Companions are the Doctor's closest friends. Such people knew the Doctor's "secret": that he was someone non-human
 * who travelled in space and time in a police box-shaped craft called the TARDIS.
 *
 * @author eccentric_nz
 */
public class ResultSetOccupied {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final List<Integer> data = new ArrayList<>();
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the travellers table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetOccupied(TARDIS plugin) {
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
        String query = "SELECT DISTINCT " + prefix + "travellers.tardis_id FROM " + prefix + "travellers, " + prefix + "tardis WHERE " + prefix + "tardis.lastuse > " + time + " AND " + prefix + "tardis.tardis_id = " + prefix + "travellers.tardis_id";
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    data.add(rs.getInt("tardis_id"));
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for travellers table [Occupied]! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing travellers table [Occupied]! " + e.getMessage());
            }
        }
    }

    public List<Integer> getData() {
        return Collections.unmodifiableList(data);
    }

    public interface ResultSetOccupiedCallback {
        void onDone(ResultSetOccupied resultSetOccupied);
    }
}
