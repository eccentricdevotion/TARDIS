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
import me.eccentric_nz.TARDIS.planets.TARDISWorldResolver;
import me.eccentric_nz.TARDIS.rooms.loader.Ticket;
import org.bukkit.World;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the chunk location of the TARDIS
 * interior.
 *
 * @author eccentric_nz
 */
public class ResultSetChunkTickets {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final List<Ticket> data = new ArrayList<>();
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the chunks table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetChunkTickets(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the chunks table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean fromId(int tardis_id) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "chunks WHERE tardis_id = ? AND ticket = 1";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, tardis_id);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    World world = TARDISWorldResolver.getFromString(rs.getString("world"));
                    Ticket row = new Ticket(tardis_id, UUID.fromString(rs.getString("uuid")), world, rs.getInt("x"), rs.getInt("z"));
                    data.add(row);
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for chunk tickets! " + e.getMessage());
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
                plugin.debug("Error closing chunk tickets! " + e.getMessage());
            }
        }
        return true;
    }

    public List<Ticket> getData() {
        return data;
    }

    public List<Integer> getIds() {
        List<Integer> ids = new ArrayList<>();
        Statement statement = null;
        ResultSet rs = null;
        String query = "SELECT DISTINCT tardis_id FROM " + prefix + "chunks WHERE ticket = 1";
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    ids.add(rs.getInt("tardis_id"));
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for chunk ticket ids! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing chunk ticket ids! " + e.getMessage());
            }
        }
        return ids;
    }

    public int count(int id) {
        int count = 0;
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT COUNT(*) AS count FROM " + prefix + "chunks WHERE tardis_id = ? AND ticket = 1";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    count = rs.getInt("count");
                }
            } else {
                return 0;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for runnable chunk tickets! " + e.getMessage());
            return 0;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing runnable chunk tickets! " + e.getMessage());
            }
        }
        return count;
    }
}
