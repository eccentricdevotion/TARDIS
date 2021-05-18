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
package me.eccentric_nz.TARDIS.database.resultset;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the companions who travel in the
 * TARDIS.
 * <p>
 * Companions are the Doctor's closest friends. Such people knew the Doctor's "secret": that he was someone non-human
 * who travelled in space and time in a police box-shaped craft called the TARDIS.
 *
 * @author eccentric_nz
 */
public class ResultSetTravellers {

	private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
	private final Connection connection = service.getConnection();
	private final TARDIS plugin;
	private final HashMap<String, Object> where;
	private final boolean multiple;
	private final List<UUID> data = new ArrayList<>();
	private final String prefix;
	private int traveller_id;
	private int tardis_id;
	private UUID uuid;

	/**
	 * Creates a class instance that can be used to retrieve an SQL ResultSet from the travellers table.
	 *
	 * @param plugin   an instance of the main class.
	 * @param where    a HashMap&lt;String, Object&gt; of table fields and values to refine the search.
	 * @param multiple a boolean setting whether to retrieve more than one record, it true returns an ArrayList that can
	 *                 be looped through later.
	 */
	public ResultSetTravellers(TARDIS plugin, HashMap<String, Object> where, boolean multiple) {
		this.plugin = plugin;
		this.where = where;
		this.multiple = multiple;
		prefix = this.plugin.getPrefix();
	}

	/**
	 * Retrieves an SQL ResultSet from the travellers table. This method builds an SQL query string from the parameters
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
		String query = "SELECT * FROM " + prefix + "travellers" + wheres;
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
			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					if (multiple) {
						data.add(UUID.fromString(rs.getString("uuid")));
					}
					traveller_id = rs.getInt("traveller_id");
					tardis_id = rs.getInt("tardis_id");
					uuid = UUID.fromString(rs.getString("uuid"));
				}
			} else {
				return false;
			}
		} catch (SQLException e) {
			plugin.debug("ResultSet error for travellers table! " + e.getMessage());
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
				plugin.debug("Error closing travellers table! " + e.getMessage());
			}
		}
		return true;
	}

	public int getTraveller_id() {
		return traveller_id;
	}

	public int getTardis_id() {
		return tardis_id;
	}

	public UUID getUuid() {
		return uuid;
	}

	public List<UUID> getData() {
		return Collections.unmodifiableList(data);
	}
}
