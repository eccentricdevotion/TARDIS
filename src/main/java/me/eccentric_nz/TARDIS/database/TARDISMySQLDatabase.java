/*
 * Copyright (C) 2014 eccentric_nz
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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.databasetool.SQL;

/**
 * MySQL database creator and updater.
 *
 * Many facts, figures, and formulas are contained within the Matrix - a
 * supercomputer and micro-universe used by the High Council of the Time Lords
 * as a storehouse of knowledge to predict future events.
 *
 * @author eccentric_nz
 */
public class TARDISMySQLDatabase {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private Statement statement = null;
    private final TARDIS plugin;

    public TARDISMySQLDatabase(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Creates the TARDIS default tables in the database.
     */
    public void createTables() {
        service.setIsMySQL(true);
        try {
            service.testConnection(connection);
            statement = connection.createStatement();

            for (String query : SQL.CREATES) {
                String subbed = String.format(query, plugin.getConfig().getString("storage.mysql.prefix"));
                statement.executeUpdate(subbed);
            }

            // update tables
            TARDISMySQLDatabaseUpdater dbu = new TARDISMySQLDatabaseUpdater(plugin, statement);
            dbu.updateTables();

        } catch (SQLException e) {
            plugin.getConsole().sendMessage(plugin.getPluginName() + "MySQL create table error: " + e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "MySQL close statement error: " + e);
            }
        }
    }
}
