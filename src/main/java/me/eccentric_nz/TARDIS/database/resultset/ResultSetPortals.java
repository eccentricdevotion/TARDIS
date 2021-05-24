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
package me.eccentric_nz.tardis.database.resultset;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.TARDISDatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the location of the tardis doors in
 * their different dimensions.
 *
 * @author eccentric_nz
 */
public class ResultSetPortals {

	private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
	private final Connection connection = service.getConnection();
	private final TARDISPlugin plugin;
	private final int id;
	private final ArrayList<HashMap<String, String>> data = new ArrayList<>();
	private final String prefix;

	/**
	 * Creates a class instance that can be used to retrieve an SQL ResultSet from the doors table.
	 *
	 * @param plugin an instance of the main class.
	 * @param id     the tardis id to get the doors for.
	 */
	public ResultSetPortals(TARDISPlugin plugin, int id) {
		this.plugin = plugin;
		this.id = id;
		prefix = this.plugin.getPrefix();
	}

	/**
	 * Retrieves an SQL ResultSet from the doors table. This method builds an SQL query string from the parameters
	 * supplied and then executes the query. Use the getters to retrieve the results.
	 */
	public void resultSet() {
		PreparedStatement statement = null;
		ResultSet rs = null;
		String query = "SELECT * FROM " + prefix + "doors WHERE tardis_id = " + id + " AND door_type IN (0,1)";
		try {
			service.testConnection(connection);
			statement = connection.prepareStatement(query);
			rs = statement.executeQuery();
			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					HashMap<String, String> row = new HashMap<>();
					ResultSetMetaData rsmd = rs.getMetaData();
					int columns = rsmd.getColumnCount();
					for (int i = 1; i < columns + 1; i++) {
						row.put(rsmd.getColumnName(i).toLowerCase(Locale.ENGLISH), rs.getString(i));
					}
					data.add(row);
				}
			}
		} catch (SQLException e) {
			plugin.debug("ResultSet error for doors table! " + e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				plugin.debug("Error closing doors table! " + e.getMessage());
			}
		}
	}

	public ArrayList<HashMap<String, String>> getData() {
		return data;
	}
}
