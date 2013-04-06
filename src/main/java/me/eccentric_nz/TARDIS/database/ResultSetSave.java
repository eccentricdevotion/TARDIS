/*
 * Copyright (C) 2013 eccentric_nz
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
import me.eccentric_nz.TARDIS.TARDIS;

/**
 * Many facts, figures, and formulas are contained within the Matrix,
 * including... an easy way to retrieve a saved time travel location.
 *
 * @author eccentric_nz
 */
public class ResultSetSave {

    private TARDISDatabase service = TARDISDatabase.getInstance();
    private Connection connection = service.getConnection();
    private TARDIS plugin;
    private String where;

    /**
     * Creates a class instance that can be used to check whether a parking spot
     * in a TARDIS area is free for use.
     *
     * @param plugin an instance of the main class.
     * @param where a String location to check.
     */
    public ResultSetSave(TARDIS plugin, String where) {
        this.plugin = plugin;
        this.where = where;
    }

    /**
     * Retrieves an SQL ResultSet from the tardis table. This method returns
     * true if a matching record was found.
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT save FROM tardis WHERE save LIKE ?";
        //plugin.debug(query);
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, where);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for (save) tardis table! " + e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                plugin.debug("Error closing (save) tardis table! " + e.getMessage());
            }
        }
    }
}
