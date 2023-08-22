/*
 * Copyright (C) 2023 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.data.Follower;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.monsters.ood.OodColour;
import me.eccentric_nz.tardisweepingangels.utils.Monster;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the companions who travel in the
 * TARDIS.
 * <p>
 * Companions are the Doctor's closest friends. Such people knew the Doctor's "secret": that he was someone non-human
 * who travelled in space and time in a police box-shaped craft called the TARDIS.
 *
 * @author eccentric_nz
 */
public class ResultSetFollowers {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private final String uuid;
    private Follower follower;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the followers table.
     *
     * @param plugin an instance of the TARDIS class.
     */
    public ResultSetFollowers(TARDIS plugin, String uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the followers table. This method builds an SQL query string from the parameter
     * supplied and then executes the query. Use the getter to retrieve the result.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "followers WHERE uuid = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                UUID owner = TARDISWeepingAngels.UNCLAIMED;
                String o = rs.getString("owner");
                if (!o.isEmpty()) {
                    owner = UUID.fromString(o);
                }
                OodColour colour = OodColour.BLACK;
                String c = rs.getString("colour");
                if (!c.isEmpty()) {
                    colour = OodColour.valueOf(c);
                }
                follower = new Follower(
                    UUID.fromString(rs.getString("uuid")),
                    owner,
                    Monster.valueOf(rs.getString("species")),
                    rs.getBoolean("following"),
                    rs.getBoolean("option"),
                    colour,
                    rs.getInt("ammo")
                );
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for followers table! " + e.getMessage());
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
                plugin.debug("Error closing followers table! " + e.getMessage());
            }
        }
    }

    public Follower getEntity() {
        return follower;
    }
}
