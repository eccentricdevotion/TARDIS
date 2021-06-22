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

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.TardisDatabaseConnection;
import me.eccentric_nz.tardis.utility.TardisStaticLocationGetters;

import java.sql.*;

public class TardisControlsConverter {

    private final TardisDatabaseConnection service = TardisDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TardisPlugin plugin;
    private final String prefix;

    public TardisControlsConverter(TardisPlugin plugin) {
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
            // transfer chameleon (31), save-sign (32) & scanner (33) locations from `tardis` table to `controls` table
            String tardisQuery = "SELECT tardis_id, chameleon, condenser, save_sign, scanner FROM " + prefix + "tardis";
            String controlsInsert = "INSERT INTO " + prefix + "controls (tardis_id, type, location) VALUES (?, ?, ?)";
            ps = connection.prepareStatement(controlsInsert);
            connection.setAutoCommit(false);
            // get tardis records
            ResultSet rst = statement.executeQuery(tardisQuery);
            String location;
            if (rst.isBeforeFirst()) {
                while (rst.next()) {
                    // if there is a chameleon record
                    if (!rst.getString("chameleon").isEmpty()) {
                        ps.setInt(1, rst.getInt("tardis_id"));
                        ps.setInt(2, 31);
                        // convert world:x:y:z to Bukkit Location.toString()
                        location = getLocationFromString(rst.getString("chameleon"));
                        ps.setString(3, location);
                        ps.addBatch();
                        i++;
                    }
                    // if there is a save_sign record
                    if (!rst.getString("save_sign").isEmpty()) {
                        ps.setInt(1, rst.getInt("tardis_id"));
                        ps.setInt(2, 32);
                        location = getLocationFromString(rst.getString("save_sign"));
                        ps.setString(3, location);
                        ps.addBatch();
                        i++;
                    }
                    // if there is a scanner record
                    if (!rst.getString("scanner").isEmpty()) {
                        ps.setInt(1, rst.getInt("tardis_id"));
                        ps.setInt(2, 33);
                        location = getLocationFromString(rst.getString("scanner"));
                        ps.setString(3, location);
                        ps.addBatch();
                        i++;
                    }
                    // if there is a condenser record
                    if (!rst.getString("condenser").isEmpty()) {
                        ps.setInt(1, rst.getInt("tardis_id"));
                        ps.setInt(2, 34);
                        location = getLocationFromString(rst.getString("condenser"));
                        ps.setString(3, location);
                        ps.addBatch();
                        i++;
                    }
                }
                if (i > 0) {
                    ps.executeBatch();
                    connection.commit();
                    plugin.getConsole().sendMessage(plugin.getPluginName() + "Converted " + i + " old tardis control records");
                }
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            plugin.debug("Conversion error for tardis/controls tables (converting old tardis controls)! " + e.getMessage());
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
                plugin.debug("Error closing tardis/controls tables (converting old tardis controls)! " + e.getMessage());
            }
        }
    }

    private String getLocationFromString(String s) {
        String[] split = s.split(":");
        return TardisStaticLocationGetters.makeLocationStr(split[0], split[1], split[2], split[3]);
    }
}

