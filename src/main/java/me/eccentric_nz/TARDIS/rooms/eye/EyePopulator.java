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
package me.eccentric_nz.TARDIS.rooms.eye;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;

import java.sql.*;

public class EyePopulator {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;

    public EyePopulator(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void insert() {
        PreparedStatement ps = null;
        Statement statement = null;
        ResultSet rs = null;
        String insert = "INSERT INTO " + prefix + "eyes (tardis_id) VALUES (?)";
        String query = "SELECT tardis_id FROM " + prefix + "tardis";
        try {
            service.testConnection(connection);
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(insert);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            int i = 0;
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    ps.setInt(1, rs.getInt("tardis_id"));
                    ps.addBatch();
                    i++;
                }
            }
            if (i > 0) {
                ps.executeBatch();
                connection.commit();
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Added " + i + " Eye of Harmony records");
            }
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            plugin.debug("Insert error for saving tardis_id to eyes! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing eyes statement! " + e.getMessage());
            }
        }
    }
}
