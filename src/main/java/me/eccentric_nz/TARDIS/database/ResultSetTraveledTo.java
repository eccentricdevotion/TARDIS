/*
 * Copyright (C) 2020 eccentric_nz
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

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.World.Environment;

import org.bukkit.entity.Player;

/**
 * Many facts, figures, and formulas are contained within the Matrix,
 * including... the location of the TARDIS Police Box blocks.
 *
 * @author Technoguyfication
 */
public class ResultSetTraveledTo {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from
     * the traveled_to table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetTraveledTo(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the traveled_to table. This is used to
     * determine if players have traveled to a dimension
     *
     * @return true or false depending on whether the player has traveled to the
     *         world
     */
    public boolean resultSet(Player p, Environment env) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "traveled_to WHERE uuid = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, p.getUniqueId().toString());
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                return rs.getInt(env.toString().toLowerCase()) == 1;
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for traveled_to table! " + e.getMessage());
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
                plugin.debug("Error closing traveled_to table! " + e.getMessage());
            }
        }
    }
}
