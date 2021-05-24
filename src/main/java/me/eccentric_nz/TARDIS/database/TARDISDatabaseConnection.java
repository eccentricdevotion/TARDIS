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

import me.eccentric_nz.tardis.TARDISPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class to get the database connection.
 * <p>
 * Many facts, figures, and formulas are contained within the Matrix - a supercomputer and micro-universe used by the
 * High Council of the Time Lords as a storehouse of knowledge to predict future events.
 *
 * @author eccentric_nz
 */
public class TARDISDatabaseConnection {

	private static final TARDISDatabaseConnection INSTANCE = new TARDISDatabaseConnection();
	public Connection connection = null;
	private boolean isMySQL;

	public static synchronized TARDISDatabaseConnection getINSTANCE() {
		return INSTANCE;
	}

	void setIsMySQL(boolean isMySQL) {
		this.isMySQL = isMySQL;
	}

	public void setConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Cannot find the driver in the classpath!", e);
		}
		String jdbc = "jdbc:mysql://" + TARDISPlugin.plugin.getConfig().getString("storage.mysql.host") + ":" + TARDISPlugin.plugin.getConfig().getString("storage.mysql.port") + "/" + TARDISPlugin.plugin.getConfig().getString("storage.mysql.database") + "?autoReconnect=true";
		if (!TARDISPlugin.plugin.getConfig().getBoolean("storage.mysql.useSSL")) {
			jdbc += "&useSSL=false";
		}
		String user = TARDISPlugin.plugin.getConfig().getString("storage.mysql.user");
		String pass = TARDISPlugin.plugin.getConfig().getString("storage.mysql.password");
		try {
			connection = DriverManager.getConnection(jdbc, user, pass);
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			throw new RuntimeException("Cannot connect the database! ", e);
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
	 * @throws CloneNotSupportedException No cloning allowed
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("Clone is not allowed.");
	}

	/**
	 * Test the database connection
	 *
	 * @param connection the database connection to test
	 */
	public void testConnection(Connection connection) {
		try {
			if (isMySQL && !connection.isValid(1)) {
				setConnection();
			}
		} catch (SQLException ex) {
			TARDISPlugin.plugin.debug("Could not re-connect to database! " + ex.getMessage());
		}
	}
}
