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
import java.util.UUID;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the location of the tardis Police Box
 * blocks.
 *
 * @author eccentric_nz
 */
public class ResultSetAntiBuild {

	private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
	private final Connection connection = service.getConnection();
	private final TARDISPlugin plugin;
	private final String uuid;
	private final String prefix;
	private Integer tardisId;

	/**
	 * Creates a class instance that can be used to retrieve an SQL ResultSet from the tardis &amp; travellers table.
	 *
	 * @param plugin an instance of the main class.
	 * @param uuid   the uuid who is trying to build.
	 */
	public ResultSetAntiBuild(TARDISPlugin plugin, UUID uuid) {
		this.plugin = plugin;
		this.uuid = uuid.toString();
		prefix = this.plugin.getPrefix();
	}

	/**
	 * Retrieves an SQL ResultSet from the tardis &amp; travellers table. This method builds an SQL query string from
	 * the parameters supplied and then executes the query. Use the getters to retrieve the results.
	 *
	 * @return true or false depending on whether any data matches the query
	 */
	public boolean resultSet() {
		PreparedStatement statement = null;
		ResultSet rs = null;
		String query = "SELECT " + prefix + "tardis.tardisId FROM " + prefix + "tardis, " + prefix + "travellers, " + prefix + "player_prefs WHERE " + prefix + "travellers.uuid = ? AND " + prefix + "tardis.uuid != ? AND " + prefix + "player_prefs.build_on = 0 AND " + prefix + "tardis.tardisId = " + prefix + "travellers.tardisId AND " + prefix + "tardis.uuid = " + prefix + "player_prefs.uuid";
		try {
			service.testConnection(connection);
			statement = connection.prepareStatement(query);
			statement.setString(1, uuid);
			statement.setString(2, uuid);
			rs = statement.executeQuery();
			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					tardisId = rs.getInt("tardisId");
				}
			} else {
				return false;
			}
		} catch (SQLException e) {
			plugin.debug("ResultSet error for antibuild tables! " + e.getMessage());
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
				plugin.debug("Error closing antibuild tables! " + e.getMessage());
			}
		}
		return true;
	}

	public Integer getTardisId() {
		return tardisId;
	}
}
