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
package me.eccentric_nz.TARDIS.database.converters;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TARDISWallSignConverter {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;

    public TARDISWallSignConverter(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void convertSignBlocks() {
        PreparedStatement statement = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT b_id, data FROM " + prefix + "blocks WHERE data LIKE 'minecraft:wall_sign%'";
        String update = "UPDATE " + prefix + "blocks SET data = 'minecraft:oak_wall_sign[facing=north,waterlogged=false]' WHERE b_id = ?";
        int i = 0;
        try {
            service.testConnection(connection);
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(query);
            ps = connection.prepareStatement(update);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    ps.setInt(1, rs.getInt("b_id"));
                    ps.addBatch();
                    i++;
                    if (i > 0) {
                        ps.executeBatch();
                        connection.commit();
                        plugin.getConsole().sendMessage(plugin.getPluginName() + "Converted " + i + " legacy wall signs to oak wall signs");
                    }
                }
            }
        } catch (SQLException e) {
            plugin.debug("Conversion error for wall sign blocks! " + e.getMessage());
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
                plugin.debug("Error closing blocks table (wall signs)! " + e.getMessage());
            }
        }
    }
}
