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
package me.eccentric_nz.tardis.database.resultset;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.TARDISDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the location of the tardis doors in
 * their different dimensions.
 *
 * @author eccentric_nz
 */
public class ResultSetCreeper {

	private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
	private final Connection connection = service.getConnection();
	private final TARDISPlugin plugin;
	private final String location;
	private final String prefix;

	/**
	 * Creates a class instance that can be used to retrieve an SQL ResultSet from the tardis table.
	 *
	 * @param plugin   an instance of the main class.
	 * @param location the location to check for.
	 */
	public ResultSetCreeper(TARDISPlugin plugin, String location) {
		this.plugin = plugin;
		this.location = location;
		prefix = this.plugin.getPrefix();
	}

	/**
	 * Retrieves an SQL ResultSet from the tardis table. This method builds an SQL query string from the parameters
	 * supplied and then executes the query. Use the getters to retrieve the results.
	 *
	 * @return true or false depending on whether any data matches the query
	 */
	public boolean resultSet() {
		PreparedStatement statement = null;
		ResultSet rs = null;
		String query = "SELECT creeper FROM " + prefix + "tardis WHERE creeper = '" + location + "'";
		try {
			service.testConnection(connection);
			statement = connection.prepareStatement(query);
			rs = statement.executeQuery();
			// reverse it as we don't want an Artron Creeper
			return !rs.isBeforeFirst();
		} catch (SQLException e) {
			plugin.debug("ResultSet error for creeper! " + e.getMessage());
			return true;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				plugin.debug("Error closing creeper! " + e.getMessage());
			}
		}
	}
}
