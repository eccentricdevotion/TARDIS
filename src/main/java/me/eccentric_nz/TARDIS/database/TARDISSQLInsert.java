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
package me.eccentric_nz.TARDIS.database;

import me.eccentric_nz.TARDIS.TARDIS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISSQLInsert implements Runnable {

	private final TARDIS plugin;
	private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
	private final Connection connection = service.getConnection();
	private final String table;
	private final HashMap<String, Object> data;
	private final String prefix;

	/**
	 * Inserts data into an SQLite database table. This method builds a prepared SQL statement from the parameters
	 * supplied and then executes the insert.
	 *
	 * @param plugin an instance of the main plugin class
	 * @param table  the database table name to insert the data into.
	 * @param data   a HashMap<String, Object> of table fields and values to insert.
	 */
	TARDISSQLInsert(TARDIS plugin, String table, HashMap<String, Object> data) {
		this.plugin = plugin;
		this.table = table;
		this.data = data;
		prefix = this.plugin.getPrefix();
	}

	@Override
	public void run() {
		PreparedStatement ps = null;
		String fields;
		String questions;
		StringBuilder sbf = new StringBuilder();
		StringBuilder sbq = new StringBuilder();
		data.forEach((key, value) -> {
			sbf.append(key).append(",");
			sbq.append("?,");
		});
		fields = sbf.substring(0, sbf.length() - 1);
		questions = sbq.substring(0, sbq.length() - 1);
		try {
			service.testConnection(connection);
			ps = connection.prepareStatement("INSERT INTO " + prefix + table + " (" + fields + ") VALUES (" + questions + ")");
			int i = 1;
			for (Map.Entry<String, Object> entry : data.entrySet()) {
				if (entry.getValue() instanceof String || entry.getValue() instanceof UUID) {
					ps.setString(i, entry.getValue().toString());
				} else {
					if (entry.getValue() instanceof Integer) {
						ps.setInt(i, (Integer) entry.getValue());
					} else if (entry.getValue() instanceof Double) {
						ps.setDouble(i, (Double) entry.getValue());
					} else if (entry.getValue() instanceof Float) {
						ps.setFloat(i, (Float) entry.getValue());
					} else if (entry.getValue() instanceof Long) {
						ps.setLong(i, (Long) entry.getValue());
					}
				}
				i++;
			}
			data.clear();
			ps.executeUpdate();
		} catch (SQLException e) {
			plugin.debug("Insert error for " + table + "! " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				plugin.debug("Error closing " + table + "! " + e.getMessage());
			}
		}
	}
}
