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
package me.eccentric_nz.tardis.database.converters;

import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.TARDISDatabaseConnection;

import java.sql.*;

/**
 * @author eccentric_nz
 */
public class TARDISSaveIconUpdate {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDISPlugin plugin;
    private final String prefix;

    public TARDISSaveIconUpdate(TARDISPlugin plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void addIcons() {
        Statement statement = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        String select;
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            connection.setAutoCommit(false);
            select = "SELECT dest_id, slot FROM " + prefix + "destinations";
            rs = statement.executeQuery(select);
            // tardis table
            query = "UPDATE " + prefix + "destinations SET icon = ? WHERE dest_id = ?";
            ps = connection.prepareStatement(query);
            int i = 0;
            while (rs.next()) {
                int slot = rs.getInt("slot");
                if (slot != -1) {
                    ps.setString(1, TARDISConstants.GUI_IDS.get(slot).toString());
                    ps.setInt(2, rs.getInt("dest_id"));
                    ps.addBatch();
                    i++;
                }
            }
            if (i > 0) {
                ps.executeBatch();
                connection.commit();
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Added " + i + " saved destination icons");
            }
        } catch (SQLException e) {
            plugin.debug("Update error for lowercase world name update! " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (rs != null) {
                    rs.close();
                }
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                plugin.debug("Error closing lowercase world name update associated tables! " + e.getMessage());
            }
        }
    }
}
