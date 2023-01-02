/*
 * Copyright (C) 2023 eccentric_nz
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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author eccentric_nz
 */
class TARDISSQLUpdateLocations implements Runnable {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final HashMap<String, Object> data;
    private final int id;
    private final String prefix;

    /**
     * Updates data in an SQLite database table. This method builds a prepared SQL statement from the parameters
     * supplied and then executes the insert.
     *
     * @param plugin an instance of the main plugin class
     * @param data   a HashMap<String, Object> of table fields and values to insert.
     * @param id     the tardis_id
     */
    TARDISSQLUpdateLocations(TARDIS plugin, HashMap<String, Object> data, int id) {
        this.plugin = plugin;
        this.data = data;
        this.id = id;
        prefix = this.plugin.getPrefix();
    }

    @Override
    public void run() {
        String[] tables = {"current", "next", "back"};
        PreparedStatement ps = null;
        StringBuilder sbu = new StringBuilder();
        data.forEach((key, value) -> sbu.append(key).append(" = ?,"));
        String updates = sbu.substring(0, sbu.length() - 1);
        try {
            service.testConnection(connection);
            for (String s : tables) {
                ps = connection.prepareStatement("UPDATE " + prefix + s + " SET " + updates + " WHERE tardis_id = ?");
                int i = 1;
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    if (entry.getValue() instanceof String) {
                        ps.setString(i, entry.getValue().toString());
                    } else {
                        ps.setInt(i, (Integer) entry.getValue());
                    }
                    i++;
                }
                ps.setInt(7, id);
                ps.executeUpdate();
            }
            data.clear();
        } catch (SQLException e) {
            plugin.debug("Update error for travel stop locations! " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing travel stop location tables! " + e.getMessage());
            }
        }
    }
}
