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
import java.util.*;

/**
 * Chronomium was a time-active element that displaced time around it. In its unprocessed form, chronomium slowed down
 * time around it. In its refined state, it could be used to accelerate time, achieve time travel, and protect against
 * temporal fields.
 *
 * @author eccentric_nz
 */
public class ResultSetAdvancements {

	private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
	private final Connection connection = service.getConnection();
	private final TARDISPlugin plugin;
	private final HashMap<String, Object> where;
	private final boolean multiple;
	private final ArrayList<HashMap<String, String>> data = new ArrayList<>();
	private final String prefix;
	private int aId;
	private UUID uuid;
	private String name;
	private String amount;
	private boolean completed;

	/**
	 * Creates a class instance that can be used to retrieve an SQL ResultSet from the achievements table.
	 *
	 * @param plugin   an instance of the main class.
	 * @param where    a String location to check.
	 * @param multiple a boolean setting whether to retrieve more than on record, it true returns an ArrayList that can
	 *                 be looped through later.
	 */
	public ResultSetAdvancements(TARDISPlugin plugin, HashMap<String, Object> where, boolean multiple) {
		this.plugin = plugin;
		this.where = where;
		this.multiple = multiple;
		prefix = this.plugin.getPrefix();
	}

	/**
	 * Retrieves an SQL ResultSet from the achievements table. This method returns true if a matching record was found.
	 *
	 * @return true or false
	 */
	public boolean resultSet() {
		PreparedStatement statement = null;
		ResultSet rs = null;
		String wheres = "";
		if (where != null) {
			StringBuilder sbw = new StringBuilder();
			where.forEach((key, value) -> sbw.append(key).append(" = ? AND "));
			wheres = " WHERE " + sbw.substring(0, sbw.length() - 5);
		}
		String query = "SELECT * FROM " + prefix + "achievements" + wheres;
		try {
			service.testConnection(connection);
			statement = connection.prepareStatement(query);
			if (where != null) {
				int s = 1;
				for (Map.Entry<String, Object> entry : where.entrySet()) {
					if (entry.getValue() instanceof String || entry.getValue() instanceof UUID) {
						statement.setString(s, entry.getValue().toString());
					} else {
						statement.setInt(s, (Integer) entry.getValue());
					}
					s++;
				}
				where.clear();
			}
			rs = statement.executeQuery();
			if (rs.next()) {
				if (multiple) {
					HashMap<String, String> row = new HashMap<>();
					ResultSetMetaData rsmd = rs.getMetaData();
					int columns = rsmd.getColumnCount();
					for (int i = 1; i < columns + 1; i++) {
						row.put(rsmd.getColumnName(i).toLowerCase(Locale.ENGLISH), rs.getString(i));
					}
					data.add(row);
				}
				aId = rs.getInt("a_id");
				uuid = UUID.fromString(rs.getString("uuid"));
				name = rs.getString("name");
				amount = rs.getString("amount");
				if (rs.wasNull()) {
					amount = "";
				}
				completed = rs.getBoolean("completed");
			} else {
				return false;
			}
		} catch (SQLException e) {
			plugin.debug("ResultSet error for achievements table! " + e.getMessage());
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
				plugin.debug("Error closing achievements table! " + e.getMessage());
			}
		}
		return true;
	}

	public int getaId() {
		return aId;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public String getAmount() {
		return amount;
	}

	public boolean isCompleted() {
		return completed;
	}

	public ArrayList<HashMap<String, String>> getData() {
		return data;
	}
}
