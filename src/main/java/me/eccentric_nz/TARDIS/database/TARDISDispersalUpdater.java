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

import java.sql.*;

/**
 * Cyber-conversion into Cybermen involves the replacement of body parts (including limbs, organs, and vital systems)
 * with artificial components. Partial conversion, with the victim retaining autonomy and a human identity and body
 * parts, is possible.
 *
 * @author eccentric_nz
 */
class TARDISDispersalUpdater {

	private final TARDISPlugin plugin;
	private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
	private final Connection connection = service.getConnection();
	private final String prefix;

	TARDISDispersalUpdater(TARDISPlugin plugin) {
		this.plugin = plugin;
		prefix = this.plugin.getPrefix();
	}

	/**
	 * Convert pre-tardis v2.3 controls to the new system.
	 */
	void updateTardisIds() {
		int i = 0;
		Statement statement = null;
		ResultSet rs = null;
		ResultSet rst = null;
		PreparedStatement ps_select = null;
		PreparedStatement ps_update = null;
		try {
			service.testConnection(connection);
			statement = connection.createStatement();
			rs = statement.executeQuery("SELECT d_id, uuid FROM " + prefix + "dispersed");
			// insert values from tardis table
			if (rs.isBeforeFirst()) {
				ps_select = connection.prepareStatement("SELECT tardis_id FROM " + prefix + "tardis WHERE uuid = ?");
				ps_update = connection.prepareStatement("UPDATE " + prefix + "dispersed SET tardis_id = ? WHERE d_id = ?");
				while (rs.next()) {
					ps_select.setString(1, rs.getString("uuid"));
					rst = ps_select.executeQuery();
					if (rst.next()) {
						ps_update.setInt(1, rst.getInt("tardis_id"));
						ps_update.setInt(2, rs.getInt("d_id"));
						ps_update.executeUpdate();
						i++;
					}
				}
			}
		} catch (SQLException e) {
			plugin.debug("Dispersal update error: " + e.getMessage());
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					plugin.debug("Dispersal statement close error: " + e.getMessage());
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					plugin.debug("Dispersal statement close error: " + e.getMessage());
				}
			}
			if (rst != null) {
				try {
					rst.close();
				} catch (SQLException e) {
					plugin.debug("Dispersal statement close error: " + e.getMessage());
				}
			}
			if (ps_select != null) {
				try {
					ps_select.close();
				} catch (SQLException e) {
					plugin.debug("Dispersal select prepared statement close error: " + e.getMessage());
				}
			}
			if (ps_update != null) {
				try {
					ps_update.close();
				} catch (SQLException e) {
					plugin.debug("Dispersal update prepared statement close error: " + e.getMessage());
				}
			}
		}
		if (i > 0) {
			plugin.getConsole().sendMessage(plugin.getPluginName() + "Updated " + i + " dispersed records");
		}
	}
}
