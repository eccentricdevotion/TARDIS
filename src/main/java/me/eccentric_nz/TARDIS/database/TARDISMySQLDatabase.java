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
package me.eccentric_nz.tardis.database;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.tool.Sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * MySQL database creator and updater.
 * <p>
 * Many facts, figures, and formulas are contained within the Matrix - a supercomputer and micro-universe used by the
 * High Council of the Time Lords as a storehouse of knowledge to predict future events.
 *
 * @author eccentric_nz
 */
public class TardisMySqlDatabase {

    private final TardisDatabaseConnection service = TardisDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TardisPlugin plugin;
    private Statement statement = null;

    public TardisMySqlDatabase(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Creates the tardis default tables in the database.
     */
    public void createTables() {
        service.setIsMySQL(true);
        try {
            service.testConnection(connection);
            statement = connection.createStatement();

            for (String query : Sql.CREATES) {
                String subbed = String.format(query, plugin.getConfig().getString("storage.mysql.prefix"));
                statement.executeUpdate(subbed);
            }

            // update tables
            TardisMySqlDatabaseUpdater dbu = new TardisMySqlDatabaseUpdater(plugin, statement);
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
