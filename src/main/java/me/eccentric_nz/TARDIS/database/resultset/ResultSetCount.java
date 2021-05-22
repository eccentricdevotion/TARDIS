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

import me.eccentric_nz.tardis.TARDIS;
import me.eccentric_nz.tardis.database.TARDISDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * A numerical Type designates each model of tardis. Every tardis that is constructed follows the specifications of its
 * specific "Type." For example the Doctor usually operates a Type 40 tardis. Higher Type numbers indicated later model
 * TARDISes.
 *
 * @author eccentric_nz
 */
public class ResultSetCount {

	private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
	private final Connection connection = service.getConnection();
	private final TARDIS plugin;
	private final String where;
	private final String prefix;
	private int id;
	private UUID uuid;
	private int count;
	private int grace;
	private int repair;

	/**
	 * Creates a class instance that can be used to retrieve an SQL ResultSet from the count table.
	 *
	 * @param plugin an instance of the main class.
	 * @param where  a player's UUID.toString() to refine the search.
	 */
	public ResultSetCount(TARDIS plugin, String where) {
		this.plugin = plugin;
		this.where = where;
		prefix = this.plugin.getPrefix();
	}

	/**
	 * Retrieves an SQL ResultSet from the lamps table. This method builds an SQL query string from the parameters
	 * supplied and then executes the query. Use the getters to retrieve the results.
	 *
	 * @return true or false depending on whether any data matches the query
	 */
	public boolean resultSet() {
		PreparedStatement statement = null;
		ResultSet rs = null;
		String query = "SELECT * FROM " + prefix + "t_count WHERE uuid = ?";
		try {
			service.testConnection(connection);
			statement = connection.prepareStatement(query);
			statement.setString(1, where);
			rs = statement.executeQuery();
			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					id = rs.getInt("t_id");
					uuid = UUID.fromString(rs.getString("uuid"));
					count = rs.getInt("count");
					grace = rs.getInt("grace");
					repair = rs.getInt("repair");
				}
			} else {
				return false;
			}
		} catch (SQLException e) {
			plugin.debug("ResultSet error for count table! " + e.getMessage());
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
				plugin.debug("Error closing count table! " + e.getMessage());
			}
		}
		return true;
	}

	public int getId() {
		return id;
	}

	public UUID getUuid() {
		return uuid;
	}

	public int getCount() {
		return count;
	}

	public int getGrace() {
		return grace;
	}

	public int getRepair() {
		return repair;
	}
}
