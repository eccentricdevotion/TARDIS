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
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import org.bukkit.Location;
import org.bukkit.World;

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
public class ResultSetForcefield {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String where;
    private final String prefix;
    private UUID uuid;
    private Location location;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the current locations table.
     *
     * @param plugin an instance of the main class.
     * @param where  a HashMap&lt;String, Object&gt; of table fields and values to refine the search.
     */
    public ResultSetForcefield(TARDIS plugin, String where) {
        this.plugin = plugin;
        this.where = where;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the destinations table. This method builds an SQL query string from the
     * parameters supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String currentTable = prefix + "current";
        String tardisTable = prefix + "tardis";
        String query = String.format("SELECT %s.uuid, %s.* FROM %s, %s WHERE %s.uuid = ? AND %s.tardis_id = %s.tardis_id", tardisTable, currentTable, tardisTable, currentTable, tardisTable, tardisTable, currentTable);
        World world;
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, where);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                uuid = UUID.fromString(rs.getString("uuid"));
                world = TARDISAliasResolver.getWorldFromAlias(rs.getString("world"));
                int x = rs.getInt("x");
                int y = rs.getInt("y");
                int z = rs.getInt("z");
                location = new Location(world, x, y, z);
                // check location is not in a TARDIS area
                if (!plugin.getTardisArea().areaCheckInExisting(location)) {
                    TARDISMessage.send(plugin.getServer().getPlayer(uuid), "FORCE_FIELD_IN_AREA");
                    return false;
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for forcefield query! " + e.getMessage());
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
                plugin.debug("Error closing forcefield query! " + e.getMessage());
            }
        }
        return world != null;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Location getLocation() {
        return location;
    }
}
