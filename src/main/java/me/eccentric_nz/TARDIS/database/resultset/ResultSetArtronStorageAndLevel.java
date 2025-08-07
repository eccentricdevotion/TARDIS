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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetArtronStorageAndLevel {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private int capacitors = 0;
    private int damaged = 0;
    private int currentLevel = 0;

    public ResultSetArtronStorageAndLevel(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public boolean fromID(int id) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT " + prefix + "eyes.capacitors, " + prefix + "eyes.damaged, " + prefix + "tardis.artron_level " +
                "FROM " + prefix + "eyes, " + prefix + "tardis " +
                "WHERE " + prefix + "eyes.tardis_id = ? " +
                "AND " + prefix + "eyes.tardis_id = " + prefix + "tardis.tardis_id";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                capacitors = rs.getInt("capacitors");
                damaged = rs.getInt("damaged");
                currentLevel = rs.getInt("artron_level");
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for artron storage (eyes table)! " + e.getMessage());
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
                plugin.debug("Error closing artron storage (eyes table)! " + e.getMessage());
            }
        }
        return true;
    }

    public int getCapacitorCount() {
        return capacitors;
    }

    public int getDamageCount() {
        return damaged;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
}
