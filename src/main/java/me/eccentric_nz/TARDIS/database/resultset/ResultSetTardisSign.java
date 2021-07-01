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
package me.eccentric_nz.TARDIS.database.resultset;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.enumeration.Adaption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... everything about the construction of
 * the TARDIS itself.
 *
 * @author eccentric_nz
 */
public class ResultSetTardisSign {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String where;
    private final String prefix;
    private int tardis_id;
    private UUID uuid;
    private String owner;
    private String save_sign;
    private String chameleon;
    private Adaption adaption;
    private boolean iso_on;
    private boolean powered_on;
    private int which;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the tardis table, for use with the
     * Sign Listener.
     *
     * @param plugin an instance of the main class.
     * @param where  a String value to search for.
     */
    public ResultSetTardisSign(TARDIS plugin, String where) {
        this.plugin = plugin;
        this.where = where;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the tardis table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether a record matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "tardis WHERE chameleon = ? OR save_sign = ? AND abandoned = 0";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, where);
            statement.setString(2, where);
            rs = statement.executeQuery();
            if (rs.next()) {
                tardis_id = rs.getInt("tardis_id");
                uuid = UUID.fromString(rs.getString("uuid"));
                owner = rs.getString("owner");
                save_sign = rs.getString("save_sign");
                chameleon = rs.getString("chameleon");
                adaption = Adaption.values()[rs.getInt("adapti_on")];
                iso_on = rs.getBoolean("iso_on");
                powered_on = rs.getBoolean("powered_on");
                which = (chameleon.equals(where)) ? 0 : 1;
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for tardis table (SIGN)! " + e.getMessage());
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
                plugin.debug("Error closing tardis table (SIGN)! " + e.getMessage());
            }
        }
        return true;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getOwner() {
        return owner;
    }

    public String getSave_sign() {
        return save_sign;
    }

    public String getChameleon() {
        return chameleon;
    }

    public Adaption getAdaption() {
        return adaption;
    }

    public boolean isIso_on() {
        return iso_on;
    }

    public boolean isPowered_on() {
        return powered_on;
    }

    public int getWhich() {
        return which;
    }
}
