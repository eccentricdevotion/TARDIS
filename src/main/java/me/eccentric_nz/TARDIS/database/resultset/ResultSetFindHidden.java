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
import me.eccentric_nz.TARDIS.database.data.Hidden;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Many facts, figures, and formulas are contained within the Matrix,
 * including... a list of locations the TARDIS can
 * travel to.
 *
 * @author eccentric_nz
 */
public class ResultSetFindHidden {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private final List<Hidden> data = new ArrayList<>();

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from
     * the current locations table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetFindHidden(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Searches for hidden or invisible TARDISes in the given range.
     *
     * @return a list of TARDISes and their location.
     */
    public List<Hidden> search(Location location, int range) {
        int maxX = location.getBlockX() + range;
        int minX = location.getBlockX() - range;
        int maxZ = location.getBlockZ() + range;
        int minZ = location.getBlockZ() - range;
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT " + prefix + "current.*, " + prefix + "tardis.hidden, " + prefix + "tardis.owner FROM "
                + prefix + "current, " + prefix + "tardis WHERE ("
                + prefix + "tardis.hidden = 1 OR "
                + prefix + "tardis.chameleon_preset = 'INVISIBLE') AND "
                + prefix + "current.world = '" + location.getWorld().getName() + "' AND "
                + prefix + "current.x < " + maxX + " AND "
                + prefix + "current.x > " + minX + " AND "
                + prefix + "current.z < " + maxZ + " AND "
                + prefix + "current.z > " + minZ + " AND "
                + prefix + "current.tardis_id = " + prefix + "tardis.tardis_id";
        // plugin.debug(query);
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();
            while (rs.next()) {
                String status = (rs.getBoolean("hidden")) ? "HIDDEN" : "INVISIBLE";
                data.add(new Hidden(rs.getString("owner"), status, rs.getInt("x"), rs.getInt("y"), rs.getInt("z")));
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for find hidden! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing find hidden! " + e.getMessage());
            }
        }
        return data;
    }
}
