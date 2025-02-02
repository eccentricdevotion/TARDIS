/*
 * Copyright (C) 2025 eccentric_nz
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
public class ResultSetRestoreFollowers {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private final List<Follower> followers = new ArrayList<>();

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the followers table.
     *
     * @param plugin an instance of the TARDIS class.
     */
    public ResultSetRestoreFollowers(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the followers table. This method builds an SQL query string from the parameter
     * supplied and then executes the query. Use the getter to retrieve the result.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        Statement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "followers";
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
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
                    followers.add(new Follower(
                            UUID.fromString(rs.getString("uuid")),
                            owner,
                            Monster.valueOf(rs.getString("species")),
                            rs.getBoolean("following"),
                            rs.getBoolean("option"),
                            colour,
                            rs.getInt("ammo")
                            )
                    );
                }
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

    public List<Follower> getFollowers() {
        return followers;
    }
}
