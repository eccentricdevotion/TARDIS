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
import me.eccentric_nz.TARDIS.enumeration.Control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * A TARDIS control room, also referred to as a console room, is any place on a TARDIS that contains a functioning
 * control console. This flight deck also functions as a point of exit. A control room's look can be changed over
 * time, to suit one's tastes and personality. The process by which an operator can transform a control room is
 * fairly simple, once compared by the Fifth Doctor to changing a "desktop theme".
 *
 * @author eccentric_nz
 */
public class ResultSetInteraction {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private final UUID uuid;
    private int interaction_id;
    private int tardis_id;
    private Control control;
    private int state;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the interactions table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetInteraction(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the interactions table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "interactions WHERE uuid = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid.toString());
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    interaction_id = rs.getInt("i_id");
                    tardis_id = rs.getInt("tardis_id");
                    control = Control.valueOf(rs.getString("control"));
                    state = rs.getInt("state");
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for interactions table! " + e.getMessage());
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
                plugin.debug("Error closing interactions table! " + e.getMessage());
            }
        }
        return true;
    }

    public int getInteraction_id() {
        return interaction_id;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public Control getControl() {
        return control;
    }

    public int getState() {
        return state;
    }
}
