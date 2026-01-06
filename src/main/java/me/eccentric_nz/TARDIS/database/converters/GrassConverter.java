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
import me.eccentric_nz.TARDIS.enumeration.TardisModule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GrassConverter {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;

    public GrassConverter(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void checkBlockData() {
        PreparedStatement statement = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT b_id, data FROM " + prefix + "blocks where data = 'minecraft:grass'";
        String update = "UPDATE " + prefix + "blocks SET data = 'minecraft:short_grass' WHERE b_id = ?";
        int i = 0;
        try {
            service.testConnection(connection);
            connection.setAutoCommit(false);
            // do condenser data
            statement = connection.prepareStatement(query);
            ps = connection.prepareStatement(update);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    int b_id = rs.getInt("b_id");
                    // update the record
                    ps.setInt(1, b_id);
                    ps.addBatch();
                    i++;
                }
            }
            if (i > 0) {
                ps.executeBatch();
                connection.commit();
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Converted " + i + " grass to short_grass");
            }
            plugin.getConfig().set("conversions.short_grass", true);
            plugin.saveConfig();
        } catch (SQLException e) {
            plugin.debug("Conversion error for blocks short_grass! " + e.getMessage());
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
                // reset auto commit
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                plugin.debug("Error closing blocks table (short_grass)! " + e.getMessage());
            }
        }
    }
}
