/*
 * Copyright (C) 2020 eccentric_nz
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
import java.util.Locale;

public class TARDISBindConverter {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;

    public TARDISBindConverter(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void update() {
        Statement statement = null;
        PreparedStatement ps = null;
        int i = 0;
        try {
            service.testConnection(connection);
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            // transfer bound locations to `bind` table
            String destinationQuery = "SELECT tardis_id, type, bind, dest_name, preset FROM " + prefix + "destinations WHERE bind != ''";
            String bindInsert = "INSERT INTO " + prefix + "bind (tardis_id, type, location, name) VALUES (?, ?, ?, ?)";
            ps = connection.prepareStatement(bindInsert);
            connection.setAutoCommit(false);
            // get destination records
            ResultSet rsd = statement.executeQuery(destinationQuery);
            if (rsd.isBeforeFirst()) {
                while (rsd.next()) {
                    if (rsd.getInt("type") == 6) {
                        // transmat record
                        ps.setInt(1, rsd.getInt("tardis_id"));
                        ps.setInt(2, 6);
                        ps.setString(3, rsd.getString("bind"));
                        ps.setString(4, rsd.getString("preset"));
                        ps.addBatch();
                        i++;
                    }
                    if (rsd.getInt("type") == 5) {
                        // chameleon
                        ps.setInt(1, rsd.getInt("tardis_id"));
                        ps.setInt(2, 5);
                        ps.setString(3, rsd.getString("bind"));
                        ps.setString(4, rsd.getString("preset").toUpperCase(Locale.ENGLISH));
                        ps.addBatch();
                        i++;
                    } else {
                        // everything else
                        ps.setInt(1, rsd.getInt("tardis_id"));
                        ps.setInt(2, rsd.getInt("type"));
                        ps.setString(3, rsd.getString("bind"));
                        ps.setString(4, rsd.getString("dest_name"));
                        ps.addBatch();
                        i++;
                    }
                }
                if (i > 0) {
                    ps.executeBatch();
                    connection.commit();
                    plugin.getConsole().sendMessage(plugin.getPluginName() + "Converted " + i + " old bind records");
                }
            }
        } catch (SQLException e) {
            plugin.debug("Conversion error for destinations/bind tables (converting old bind records)! " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (statement != null) {
                    statement.close();
                }
                // reset auto commit
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                plugin.debug("Error closing destinations/bind tables (converting old bind records)! " + e.getMessage());
            }
        }
    }
}

