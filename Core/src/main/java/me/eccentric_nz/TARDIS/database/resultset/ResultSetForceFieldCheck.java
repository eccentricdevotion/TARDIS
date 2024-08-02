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
import java.util.UUID;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... a list of locations the TARDIS can
 * travel to.
 *
 * @author eccentric_nz
 */
public class ResultSetForceFieldCheck {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final int id;
    private final String prefix;
    private UUID uuid;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the current locations table.
     *
     * @param plugin an instance of the main class.
     * @param id     the TARDIS id to get the hidden status for.
     */
    public ResultSetForceFieldCheck(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves the visibility for the specified TARDIS.
     *
     * @return true if visible, false if hidden.
     */
    public boolean isHidden() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT uuid, hidden, chameleon_preset FROM " + prefix + "tardis WHERE tardis_id =" + id;
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                uuid = UUID.fromString(rs.getString("uuid"));
                return rs.getBoolean("hidden") || rs.getString("chameleon_preset").equals("INVISIBLE");
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for hidden! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing hidden! " + e.getMessage());
            }
        }
        return true;
    }

    public UUID getUuid() {
        return uuid;
    }
}
