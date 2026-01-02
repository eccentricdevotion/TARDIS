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
package me.eccentric_nz.TARDIS.database.converters;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

import java.sql.*;

/**
 *
 * @author eccentric_nz
 */
public class TARDISLegacyConverter {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;

    public TARDISLegacyConverter(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void setOriginal() {
        Statement statement = null;
        PreparedStatement ps = null;
        String query = "SELECT tardis_id FROM " + prefix + "tardis WHERE `size` = 'LEGACY_BUDGET'";
        String update = "UPDATE " + prefix + "tardis SET size = 'ORIGINAL' WHERE tardis_id = ?";
        ResultSet rs = null;
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                ps = connection.prepareStatement(update);
                connection.setAutoCommit(false);
                while (rs.next()) {
                    ps.setInt(1, rs.getInt("tardis_id"));
                    ps.addBatch();
                }
                ps.executeBatch();
                connection.commit();
            }
        } catch (SQLException e) {
            plugin.debug("Update error for legacy budget! " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing legacy budget associated tables! " + e.getMessage());
            }
        }
    }
}
