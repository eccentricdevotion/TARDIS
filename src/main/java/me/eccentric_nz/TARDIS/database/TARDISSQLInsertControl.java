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
package me.eccentric_nz.tardis.database;

import me.eccentric_nz.tardis.TARDIS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author eccentric_nz
 */
class TARDISSQLInsertControl implements Runnable {

	private final TARDIS plugin;
	private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
	private final Connection connection = service.getConnection();
	private final int id;
	private final int type;
	private final String l;
	private final int s;
	private final String prefix;

	/**
	 * Updates data in an SQLite database table. This method builds an SQL query string from the parameters supplied and
	 * then executes the update.
	 *
	 * @param plugin an instance of the main plugin class
	 * @param id     the unique tardis identifier
	 * @param type   the type of control to insert
	 * @param l      the location of the control
	 * @param s      whether the control is a secondary control
	 */
	TARDISSQLInsertControl(TARDIS plugin, int id, int type, String l, int s) {
		this.plugin = plugin;
		this.id = id;
		this.type = type;
		this.l = l;
		this.s = s;
		prefix = this.plugin.getPrefix();
	}

	@Override
	public void run() {
		Statement statement = null;
		try {
			service.testConnection(connection);
			statement = connection.createStatement();
			String select = "SELECT c_id FROM " + prefix + "controls WHERE tardis_id = " + id + " AND type = " + type + " AND secondary = " + s;
			ResultSet rs = statement.executeQuery(select);
			if (rs.isBeforeFirst()) {
				rs.next();
				// update
				String update = "UPDATE " + prefix + "controls SET location = '" + l + "' WHERE c_id = " + rs.getInt("c_id");
				statement.executeUpdate(update);
			} else {
				// insert
				String insert = "INSERT INTO " + prefix + "controls (tardis_id, type, location, secondary) VALUES (" + id + ", " + type + ", '" + l + "', " + s + ")";
				statement.executeUpdate(insert);
			}
		} catch (SQLException e) {
			plugin.debug("Insert control error! " + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				plugin.debug("Error closing insert control statement! " + e.getMessage());
			}
		}
	}
}
