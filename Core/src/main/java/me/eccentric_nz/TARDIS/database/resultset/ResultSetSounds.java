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

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the companions who travel in the
 * TARDIS.
 * <p>
 * Companions are the Doctor's closest friends. Such people knew the Doctor's "secret": that he was someone non-human
 * who travelled in space and time in a police box-shaped craft called the TARDIS.
 *
 * @author eccentric_nz
 */
public class ResultSetSounds {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final List<UUID> data = new ArrayList<>();
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the travellers table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetSounds(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the travellers table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        Statement statement = null;
        PreparedStatement tardis = null;
        PreparedStatement travellers = null;
        ResultSet rs = null;
        ResultSet rsTARDIS = null;
        ResultSet rsTravellers = null;
        String queryTARDIS = "SELECT tardis_id, powered_on FROM " + prefix + "tardis WHERE tardis_id = ?";
        String queryTravellers = "SELECT uuid FROM " + prefix + "travellers WHERE tardis_id = ?";
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            tardis = connection.prepareStatement(queryTARDIS);
            travellers = connection.prepareStatement(queryTravellers);
            rs = statement.executeQuery("SELECT DISTINCT tardis_id FROM " + prefix + "travellers");
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    tardis.setInt(1, rs.getInt("tardis_id"));
                    rsTARDIS = tardis.executeQuery();
                    if (rsTARDIS.next()) {
                        if (rsTARDIS.getBoolean("powered_on")) {
                            travellers.setInt(1, rsTARDIS.getInt("tardis_id"));
                            rsTravellers = travellers.executeQuery();
                            while (rsTravellers.next()) {
                                data.add(UUID.fromString(rsTravellers.getString("uuid")));
                            }
                        }
                    }
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for travellers table [Sound]! " + e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (rsTARDIS != null) {
                    rsTARDIS.close();
                }
                if (rsTravellers != null) {
                    rsTravellers.close();
                }
                if (tardis != null) {
                    tardis.close();
                }
                if (travellers != null) {
                    travellers.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing travellers table [Sound]! " + e.getMessage());
            }
        }
        return true;
    }

    public List<UUID> getData() {
        return Collections.unmodifiableList(data);
    }
}
