/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.database;

import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author eccentric_nz
 */
public class TVMMySQL {

    private final TVMDatabase service = TVMDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDISVortexManipulator plugin;
    private Statement statement = null;

    public TVMMySQL(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
    }

    /**
     * Creates the TARDISVortexManipulator default tables in the database.
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
        } catch (SQLException e) {
            plugin.getServer().getConsoleSender().sendMessage(plugin.getPluginName() + "MySQL create table error: " + e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.getServer().getConsoleSender().sendMessage(plugin.getPluginName() + "MySQL close statement error: " + e);
            }
        }
    }
}
