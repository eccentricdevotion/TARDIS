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

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the locations of the tardis vaults.
 * <p>
 * Control types: 0 = handbrake 1 = random button 2 = x-repeater 3 = z-repeater 4 = multiplier-repeater 5 =
 * environment-repeater 6 = artron button
 *
 * @author eccentric_nz
 */
public class ResultSetVoid {

	private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
	private final Connection connection = service.getConnection();
	private final TARDIS plugin;
	private final int tardis_id;
	private final String prefix;

	/**
	 * Creates a class instance that can be used to retrieve an SQL ResultSet from the thevoid table.
	 *
	 * @param plugin    an instance of the main class.
	 * @param tardis_id the tardis id to check
	 */
	public ResultSetVoid(TARDIS plugin, int tardis_id) {
		this.plugin = plugin;
		this.tardis_id = tardis_id;
		prefix = this.plugin.getPrefix();
	}

	/**
	 * Attempts to see whether the supplied tardis id is in the thevoid table. This method builds an SQL query string
	 * from the parameters supplied and then executes the query.
	 *
	 * @return true or false depending on whether the tardis id exists in the table
	 */
	public boolean hasUpdatedToVOID() {
		PreparedStatement statement = null;
		ResultSet rs = null;
		String query = "SELECT tardis_id FROM " + prefix + "thevoid WHERE tardis_id = ?";
		try {
			service.testConnection(connection);
			statement = connection.prepareStatement(query);
			statement.setInt(1, tardis_id);
			rs = statement.executeQuery();
			return rs.isBeforeFirst();
		} catch (SQLException e) {
			plugin.debug("ResultSet error for thevoid table! " + e.getMessage());
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
				plugin.debug("Error closing thevoid table! " + e.getMessage());
			}
		}
	}
}
