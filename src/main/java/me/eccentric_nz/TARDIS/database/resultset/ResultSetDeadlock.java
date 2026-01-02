/*
 * Copyright (C) 2026 eccentric_nz
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
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetDeadlock {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final Location location;
    private final String prefix;
    private boolean locked;

    public ResultSetDeadlock(TARDIS plugin, Location location) {
        this.plugin = plugin;
        this.location = location;
        prefix = this.plugin.getPrefix();
    }

    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String l = TARDISStaticLocationGetters.makeLocationStr(location);
        String query = "SELECT * FROM " + prefix + "doors WHERE door_location = ? and door_type = 1";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, l);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                locked = rs.getBoolean("locked");
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for deadlocked doors table! " + e.getMessage());
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
                plugin.debug("Error closing deadlocked doors table! " + e.getMessage());
            }
        }
        return true;
    }

    public boolean isLocked() {
        return locked;
    }
}
