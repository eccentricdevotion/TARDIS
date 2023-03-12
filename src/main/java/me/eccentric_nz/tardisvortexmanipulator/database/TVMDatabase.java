/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.database;

import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author eccentric_nz
 */
public class TVMDatabase {

    private static final TVMDatabase instance = new TVMDatabase();
    public Connection connection = null;
    public Statement statement = null;
    private boolean isMySQL;

    public static synchronized TVMDatabase getInstance() {
        return instance;
    }

    public void setIsMySQL(boolean isMySQL) {
        this.isMySQL = isMySQL;
    }

    public void setConnection() throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find the driver in the classpath!", e);
        }
        String jdbc = "jdbc:mysql://"
                + TARDISVortexManipulator.plugin.getConfig().getString("storage.mysql.host") + ":"
                + TARDISVortexManipulator.plugin.getConfig().getString("storage.mysql.port") + "/"
                + TARDISVortexManipulator.plugin.getConfig().getString("storage.mysql.database")
                + "?autoReconnect=true";
        if (!TARDISVortexManipulator.plugin.getConfig().getBoolean("storage.mysql.useSSL")) {
            jdbc += "&useSSL=false";
        }
        String user = TARDISVortexManipulator.plugin.getConfig().getString("storage.mysql.user");
        String pass = TARDISVortexManipulator.plugin.getConfig().getString("storage.mysql.password");
        try {
            connection = DriverManager.getConnection(jdbc, user, pass);
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot connect the database!", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(String path) throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        connection.setAutoCommit(true);
    }

    /**
     * @return an exception
     * @throws CloneNotSupportedException
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Clone is not allowed.");
    }

    /**
     * Test the database connection
     *
     * @param connection
     * @throws java.sql.SQLException
     */
    public void testConnection(Connection connection) throws SQLException {
        if (isMySQL) {
            try {
                statement = connection.createStatement();
                statement.executeQuery("SELECT 1");
            } catch (SQLException e) {
                try {
                    setConnection();
                } catch (Exception ex) {
                    TARDISVortexManipulator.plugin.debug("Could not re-connect to database!");
                }
            }
        }
    }
}
