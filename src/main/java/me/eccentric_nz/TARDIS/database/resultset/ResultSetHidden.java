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
 * Many facts, figures, and formulas are contained within the Matrix, including... a list of locations the tardis can
 * travel to.
 *
 * @author eccentric_nz
 */
public class ResultSetHidden {

	private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
	private final Connection connection = service.getConnection();
	private final TARDISPlugin plugin;
	private final int id;
	private final String prefix;

	/**
	 * Creates a class instance that can be used to retrieve an SQL ResultSet from the current locations table.
	 *
	 * @param plugin an instance of the main class.
	 * @param id     the tardis id to get the hidden status for.
	 */
	public ResultSetHidden(TARDISPlugin plugin, int id) {
		this.plugin = plugin;
		this.id = id;
		prefix = this.plugin.getPrefix();
	}

	/**
	 * Retrieves the visibility for the specified tardis.
	 *
	 * @return true if visible, false if hidden.
	 */
	public boolean isVisible() {
		PreparedStatement statement = null;
		ResultSet rs = null;
		String query = "SELECT hidden, chameleon_preset FROM " + prefix + "tardis WHERE tardis_id =" + id;
		try {
			service.testConnection(connection);
			statement = connection.prepareStatement(query);
			rs = statement.executeQuery();
			if (rs.isBeforeFirst()) {
				rs.next();
				return rs.getBoolean("hidden") || rs.getString("chameleon_preset").equals("INVISIBLE");
			}
		} catch (SQLException e) {
			plugin.debug("ResultSet error for hidden! " + e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				plugin.debug("Error closing hidden! " + e.getMessage());
			}
		}
		return true;
	}
}
