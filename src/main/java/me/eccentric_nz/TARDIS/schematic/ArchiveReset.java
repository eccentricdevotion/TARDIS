/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.schematic;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.TardisDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author eccentric_nz
 */
public class ArchiveReset {

    private final TardisDatabaseConnection service = TardisDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TardisPlugin plugin;
    private final String uuid;
    private final int use;
    private final String prefix;

    public ArchiveReset(TardisPlugin plugin, String uuid, int use) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.use = use;
        prefix = this.plugin.getPrefix();
    }

    public void resetUse() {
        PreparedStatement statement = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT archive_id FROM " + prefix + "archive WHERE uuid = ? AND use = 2";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                String update = "UPDATE " + prefix + "archive SET use = ? WHERE archive_id = ?";
                ps = connection.prepareStatement(update);
                while (rs.next()) {
                    ps.setInt(1, use);
                    ps.setInt(2, rs.getInt("archive_id"));
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for archive reset! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing archive reset! " + e.getMessage());
            }
        }
    }
}
