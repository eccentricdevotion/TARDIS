/*
 * Copyright (C) 2013 eccentric_nz
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
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import me.eccentric_nz.TARDIS.TARDIS;

/**
 * Singleton class to get the database connection.
 *
 * Many facts, figures, and formulas are contained within the Matrix - a
 * supercomputer and micro-universe used by the High Council of the Time Lords
 * as a storehouse of knowledge to predict future events.
 *
 * @author eccentric_nz
 */
public class TARDISDatabaseConnection {

    private static final TARDISDatabaseConnection instance = new TARDISDatabaseConnection();

    public static synchronized TARDISDatabaseConnection getInstance() {
        return instance;
    }
    public Connection connection = null;
    public Statement statement = null;

    public void setConnection(String path) throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
    }

    public void setConnection() throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find the driver in the classpath!", e);
        }
        String host = "jdbc:" + TARDIS.plugin.getConfig().getString("mysql.url");
        String user = TARDIS.plugin.getConfig().getString("mysql.user");
        String pass = TARDIS.plugin.getConfig().getString("mysql.password");
        try {
            connection = DriverManager.getConnection(host, user, pass);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot connect the database!", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     *
     * @return an exception
     * @throws CloneNotSupportedException
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Clone is not allowed.");
    }
}
