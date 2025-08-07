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

import java.sql.*;

public class EyeStopper {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;
    private Statement statement = null;
    private PreparedStatement ps = null;

    public EyeStopper(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void kill() {
        ResultSet rs = null;
        try {
            String update = "UPDATE " + prefix + "eyes SET task = -1 WHERE eye_id = ?";
            ps = connection.prepareStatement(update);
            String query = "SELECT eye_id, task FROM " + prefix + "eyes";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                connection.setAutoCommit(false);
                int count = 0;
                while (rs.next()) {
                        plugin.getServer().getScheduler().cancelTask(rs.getInt("task"));
                        ps.setInt(1, rs.getInt("eye_id"));
                        ps.addBatch();
                        count++;
                }
                if (count > 0) {
                    ps.executeBatch();
                }
                connection.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            plugin.debug("Error stopping eye of harmony stars: " + ex.getMessage());
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
            } catch (SQLException ex) {
                plugin.debug("Error closing eye of harmony stars: " + ex.getMessage());
            }
        }
    }
}
