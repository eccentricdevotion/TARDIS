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
package me.eccentric_nz.TARDIS.database.resultset;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.database.data.Reminder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the locations of the TARDIS vaults.
 * <p>
 * Control types: 0 = handbrake 1 = random button 2 = x-repeater 3 = z-repeater 4 = multiplier-repeater 5 =
 * environment-repeater 6 = artron button
 *
 * @author eccentric_nz
 */
public class ResultSetReminders {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private final List<Reminder> reminders;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the reminders table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetReminders(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
        reminders = new ArrayList<>();
    }

    /**
     * Retrieves an SQL ResultSet from the reminders table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;

        String query = "SELECT * FROM " + prefix + "reminders";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    Reminder reminder = new Reminder(
                            rs.getInt("reminder_id"),
                            UUID.fromString(rs.getString("uuid")),
                            rs.getString("reminder"),
                            rs.getLong("time")
                    );
                    reminders.add(reminder);
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for reminders table! " + e.getMessage());
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
                plugin.debug("Error closing reminders table! " + e.getMessage());
            }
        }
        return true;
    }

    public List<Reminder> getReminders() {
        return reminders;
    }
}
