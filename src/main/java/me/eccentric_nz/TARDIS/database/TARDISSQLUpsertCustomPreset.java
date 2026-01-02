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
package me.eccentric_nz.TARDIS.database;

import me.eccentric_nz.TARDIS.TARDIS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author eccentric_nz
 */
class TARDISSQLUpsertCustomPreset implements Runnable {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final int id;
    private final String preset;
    private final String prefix;

    /**
     * Updates data in an SQLite database table. This method builds an SQL query string from the parameters supplied and
     * then executes the update.
     *
     * @param plugin an instance of the main plugin class
     * @param id     the unique TARDIS identifier
     * @param preset the name of the custom preset
     */
    TARDISSQLUpsertCustomPreset(TARDIS plugin, int id, String preset) {
        this.plugin = plugin;
        this.id = id;
        this.preset = preset;
        prefix = this.plugin.getPrefix();
    }

    @Override
    public void run() {
        Statement statement = null;
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            String select = "SELECT custom_id FROM " + prefix + "custom_preset WHERE tardis_id = " + id;
            ResultSet rs = statement.executeQuery(select);
            if (rs.isBeforeFirst()) {
                rs.next();
                // update
                String update = "UPDATE " + prefix + "custom_preset SET preset = '" + preset + "' WHERE custom_id = " + rs.getInt("custom_id");
                statement.executeUpdate(update);
            } else {
                // insert
                String insert = "INSERT INTO " + prefix + "custom_preset (tardis_id, preset) VALUES (" + id + ", '" + preset + "')";
                statement.executeUpdate(insert);
            }
        } catch (SQLException e) {
            plugin.debug("Insert custom_preset error! " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing insert custom_preset statement! " + e.getMessage());
            }
        }
    }
}
