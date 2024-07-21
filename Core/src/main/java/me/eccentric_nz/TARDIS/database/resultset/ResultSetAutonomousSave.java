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

import com.mojang.datafixers.util.Pair;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... a list of locations the TARDIS can
 * travel to.
 *
 * @author eccentric_nz
 */
public class ResultSetAutonomousSave {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private final ArrayList<Pair<String, Integer>> data = new ArrayList<>();
    private String autonomous = "";

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the destinations table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetAutonomousSave(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the destinations table. This method builds an SQL query string from the
     * parameters supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean fromID(int id) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT dest_id, dest_name, autonomous FROM " + prefix + "destinations WHERE tardis_id = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            rs = statement.executeQuery();
            String firstSave = "";
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    String name = rs.getString("dest_name");
                    if (firstSave.isEmpty()) {
                        firstSave = name;
                    }
                    data.add(new Pair<>(name, rs.getInt("dest_id")));
                    if (rs.getBoolean("autonomous")) {
                        autonomous = name;
                    }
                }
                if (autonomous.isEmpty()) {
                    autonomous = firstSave;
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for autonomous destinations table! " + e.getMessage());
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
                plugin.debug("Error closing autonomous destinations table! " + e.getMessage());
            }
        }
        return true;
    }

    public String getAutonomous() {
        return autonomous;
    }

    public ArrayList<Pair<String, Integer>> getData() {
        return data;
    }
}
