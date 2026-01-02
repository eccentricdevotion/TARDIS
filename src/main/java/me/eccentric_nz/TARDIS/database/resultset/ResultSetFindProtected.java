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
import me.eccentric_nz.TARDIS.database.data.ProtectedBlock;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
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
public class ResultSetFindProtected {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private final List<ProtectedBlock> data = new ArrayList<>();

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from
     * the current locations table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetFindProtected(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Searches protected TARDIS blocks inn the given range.
     *
     * @return a list of hidden blocks.
     */
    public List<ProtectedBlock> search(Location location, int range) {
        int maxX = location.getBlockX() + range;
        int minX = location.getBlockX() - range;
        int maxZ = location.getBlockZ() + range;
        int minZ = location.getBlockZ() - range;
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT b_id, location FROM " + prefix + "blocks WHERE location LIKE 'Location{world=CraftWorld{name="
                + location.getWorld().getName() + "%'";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();
            while (rs.next()) {
                // break down location
                // Location{world=CraftWorld{name=jin},x=-304.0,y=63.0,z=-378.0,pitch=0.0,yaw=0.0}
                String loc = rs.getString("location");
                String[] bukkit = loc.split(",");
                String[] xPart = bukkit[1].split("=");
                String[] yPart = bukkit[2].split("=");
                String[] zPart = bukkit[3].split("=");
                int x = (int) TARDISNumberParsers.parseFloat(xPart[1]);
                int y = (int) TARDISNumberParsers.parseFloat(yPart[1]);
                int z = (int) TARDISNumberParsers.parseFloat(zPart[1]);
                if (x < maxX && x > minX && z < maxZ && z > minZ) {
                    data.add(new ProtectedBlock(loc, x, y, z, rs.getInt("b_id")));
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for find protected! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing find protected! " + e.getMessage());
            }
        }
        return data;
    }
}
