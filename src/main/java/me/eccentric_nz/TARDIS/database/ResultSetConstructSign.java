/*
 * Copyright (C) 2018 eccentric_nz
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
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Semaphore was a way of communicating using flags. By his twelfth incarnation, the Doctor had apparently erased
 * British Sign Language to learn semaphore.
 *
 * @author eccentric_nz
 */
public class ResultSetConstructSign {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final int id;
    private String line1;
    private String line2;
    private String line3;
    private String line4;
    private boolean asymmetric;
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the gravity_well table.
     *
     * @param plugin an instance of the main class.
     * @param id     the tardis_id of this chameleon construct
     */
    public ResultSetConstructSign(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the chameleon table. This method builds an SQL query string from the supplied
     * tardis_id and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "chameleon WHERE tardis_id = " + id;
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    line1 = rs.getString("line1");
                    line2 = rs.getString("line2");
                    line3 = rs.getString("line3");
                    line4 = rs.getString("line4");
                    asymmetric = rs.getBoolean("asymmetric");
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for chameleon (sign) table! " + e.getMessage());
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
                plugin.debug("Error closing chameleon (sign) table! " + e.getMessage());
            }
        }
        return true;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public String getLine3() {
        return line3;
    }

    public String getLine4() {
        return line4;
    }

    public boolean isAsymmetric() {
        return asymmetric;
    }
}
