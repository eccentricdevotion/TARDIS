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

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the locations of the tardis
 * controls.
 * <p>
 * Control types: 0 = handbrake 1 = random button 2 = x-repeater 3 = z-repeater 4 = multiplier-repeater 5 =
 * environment-repeater 6 = artron button
 *
 * @author eccentric_nz
 */
public class ResultSetControls {

	private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
	private final Connection connection = service.getConnection();
	private final TARDISPlugin plugin;
	private final HashMap<String, Object> where;
	private final boolean multiple;
	private final ArrayList<HashMap<String, String>> data = new ArrayList<>();
	private final String prefix;
	private int cId;
	private int tardisId;
	private int type;
	private String location;
	private int secondary;

	/**
	 * Creates a class instance that can be used to retrieve an SQL ResultSet from the controls table.
	 *
	 * @param plugin   an instance of the main class.
	 * @param where    a HashMap&lt;String, Object&gt; of table fields and values to refine the search.
	 * @param multiple a boolean indicating whether multiple rows should be fetched
	 */
	public ResultSetControls(TARDISPlugin plugin, HashMap<String, Object> where, boolean multiple) {
		this.plugin = plugin;
		this.where = where;
		this.multiple = multiple;
		prefix = this.plugin.getPrefix();
	}

	/**
	 * Retrieves an SQL ResultSet from the controls table. This method builds an SQL query string from the parameters
	 * supplied and then executes the query. Use the getters to retrieve the results.
	 *
	 * @return true or false depending on whether any data matches the query
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
		String query = "SELECT * FROM " + prefix + "controls" + wheres;
		try {
			service.testConnection(connection);
			statement = connection.prepareStatement(query);
			if (where != null) {
				int s = 1;
				for (Map.Entry<String, Object> entry : where.entrySet()) {
					if (entry.getValue() instanceof String) {
						statement.setString(s, entry.getValue().toString());
					} else {
						statement.setInt(s, (Integer) entry.getValue());
					}
					s++;
				}
				where.clear();
			}
			rs = statement.executeQuery();
			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					if (multiple) {
						HashMap<String, String> row = new HashMap<>();
						ResultSetMetaData rsmd = rs.getMetaData();
						int columns = rsmd.getColumnCount();
						for (int i = 1; i < columns + 1; i++) {
							row.put(rsmd.getColumnName(i).toLowerCase(Locale.ENGLISH), rs.getString(i));
						}
						data.add(row);
					}
					cId = rs.getInt("c_id");
					tardisId = rs.getInt("tardis_id");
					type = rs.getInt("type");
					location = rs.getString("location");
					secondary = rs.getInt("secondary");
				}
			} else {
				return false;
			}
		} catch (SQLException e) {
			plugin.debug("ResultSet error for controls table! " + e.getMessage());
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
				plugin.debug("Error closing controls table! " + e.getMessage());
			}
		}
		return true;
	}

	public int getcId() {
		return cId;
	}

	public int getTardisId() {
		return tardisId;
	}

	public int getType() {
		return type;
	}

	public String getLocation() {
		return location;
	}

	public int getSecondary() {
		return secondary;
	}

	public ArrayList<HashMap<String, String>> getData() {
		return data;
	}
}
