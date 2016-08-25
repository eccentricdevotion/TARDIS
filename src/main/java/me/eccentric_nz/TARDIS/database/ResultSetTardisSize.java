/*
 * Copyright (C) 2016 eccentric_nz
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.schematic.ResultSetArchive;

/**
 * Many facts, figures, and formulas are contained within the Matrix,
 * including... the locations of the TARDIS vaults.
 *
 * Control types: 0 = handbrake 1 = random button 2 = x-repeater 3 = z-repeater
 * 4 = multiplier-repeater 5 = environment-repeater 6 = artron button
 *
 * @author eccentric_nz
 */
public class ResultSetTardisSize {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private ConsoleSize consoleSize = ConsoleSize.SMALL;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet
     * from the vaults table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetTardisSize(TARDIS plugin) {
        this.plugin = plugin;
        this.prefix = this.plugin.getPrefix();
    }

    /**
     * Attempts to see whether the supplied TARDIS id is in the tardis table.
     * This method builds an SQL query string from the parameters supplied and
     * then executes the query.
     *
     * @param uuid the Time Lord uuid to check
     *
     * @return the size of the console
     */
    public boolean fromUUID(String uuid) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT size FROM " + prefix + "tardis WHERE uuid = ? AND abandoned = 0";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                if (rs.getString("size").equals("ARCHIVE")) {
                    // get archive
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("uuid", uuid);
                    where.put("use", 1);
                    ResultSetArchive rsa = new ResultSetArchive(plugin, where);
                    if (rsa.resultSet()) {
                        consoleSize = rsa.getArchive().getConsoleSize();
                    }
                } else {
                    consoleSize = CONSOLES.getBY_NAMES().get(rs.getString("size")).getConsoleSize();
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            plugin.debug("ResultSet error for tardis [tardis_id fromUUID] table! " + e.getMessage());
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
                plugin.debug("Error closing tardis [tardis_id fromUUID] table! " + e.getMessage());
            }
        }
    }

    public ConsoleSize getConsoleSize() {
        return consoleSize;
    }
}
