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
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetHandlesTransmat {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private final int id;
    private Location location;

    public ResultSetHandlesTransmat(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        prefix = this.plugin.getPrefix();
    }

    public boolean findSite(String chat) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "transmats WHERE tardis_id = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    if (chat.equalsIgnoreCase(rs.getString("name"))) {
                        location = new Location(TARDISAliasResolver.getWorldFromAlias(rs.getString("world")), rs.getFloat("x"), rs.getFloat("y"), rs.getFloat("z"));
                        location.setYaw(rs.getFloat("yaw"));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for transmats table! " + e.getMessage());
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
                plugin.debug("Error closing transmats table! " + e.getMessage());
            }
        }
        return false;
    }

    public Location getLocation() {
        return location;
    }
}
