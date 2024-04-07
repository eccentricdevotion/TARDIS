/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.database.resultset;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * The sonic screwdriver is a highly versatile tool used by many, but not all, incarnations of the Doctor. The Doctor
 * modified and ostensibly upgraded it over the years, giving it an increasing number of applications.
 * <p>
 * Control types: 2 = environment-repeater 3 = x-repeater 4 = z-repeater 5 = y-repeater
 *
 * @author eccentric_nz
 */
public class ResultSetRandomInteractions {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final int id;
    private final int[] diodes = new int[5];
    private final String prefix;
    private final HashMap<String, Integer> map = new HashMap<>();

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the controls table.
     *
     * @param plugin an instance of the main class.
     * @param id     the TARDIS id to search for.
     */
    public ResultSetRandomInteractions(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        prefix = this.plugin.getPrefix();
        map.put("WORLD", 0);
        map.put("X", 1);
        map.put("Z", 2);
        map.put("HELMIC_REGULATOR", 4);
        map.put("MULTIPLIER", 3);
    }

    /**
     * Retrieves an SQL ResultSet from the interactions table. This method builds an SQL query string from the
     * parameters supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT control, state FROM " + prefix + "interactions WHERE tardis_id = ? AND control IN ('HELMIC_REGULATOR', 'MULTIPLIER', 'WORLD', 'X', 'Z') ORDER BY control";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                int i = 0;
                while (rs.next()) {
                    // 0 => 'WORLD', 1 => 'X', 2 => 'Z', 3 => 'MULTIPLIER', 4 => HELMIC_REGULATOR
                    // TODO these interactions should have a default state of 1
                    diodes[map.get(rs.getString("control"))] = rs.getInt("state");
                    i++;
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for random interactions table! " + e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing random interactions table! " + e.getMessage());
            }
        }
        return true;
    }

    public int[] getStates() {
        return diodes;
    }
}
