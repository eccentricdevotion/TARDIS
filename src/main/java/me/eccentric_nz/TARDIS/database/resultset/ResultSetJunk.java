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
import me.eccentric_nz.tardis.enumeration.PRESET;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A fission lamp was a type of lamp used around the 50th century. They were very powerful. According to the Tenth
 * Doctor, they would outlast the sun. The Library made extensive use of fission junk. Nonetheless, they were no match
 * for a swarm of Vashta Nerada, whose spread was slowed down by the light from the junk but not stopped.
 *
 * @author eccentric_nz
 */
public class ResultSetJunk {

	private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
	private final Connection connection = service.getConnection();
	private final TARDISPlugin plugin;
	private final HashMap<String, Object> where;
	private final String prefix;
	private int id;
	private UUID uuid;
	private int tardisId;
	private String saveSign;
	private String handbrake;
	private String wall;
	private String floor;
	private PRESET preset;

	/**
	 * Creates a class instance that can be used to retrieve an SQL ResultSet from the junk table.
	 *
	 * @param plugin an instance of the main class.
	 * @param where  a HashMap&lt;String, Object&gt; of table fields and values to refine the search.
	 */
	public ResultSetJunk(TARDISPlugin plugin, HashMap<String, Object> where) {
		this.plugin = plugin;
		this.where = where;
		prefix = this.plugin.getPrefix();
	}

	/**
	 * Retrieves an SQL ResultSet from the junk table. This method builds an SQL query string from the parameters
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
		String query = "SELECT * FROM " + prefix + "junk" + wheres;
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
					id = rs.getInt("id");
					uuid = UUID.fromString(rs.getString("uuid"));
					tardisId = rs.getInt("tardis_id");
					saveSign = rs.getString("save_sign");
					handbrake = rs.getString("handbrake");
					wall = rs.getString("wall");
					floor = rs.getString("floor");
					try {
						preset = PRESET.valueOf(rs.getString("preset"));
					} catch (IllegalArgumentException e) {
						preset = PRESET.FACTORY;
					}
				}
			} else {
				return false;
			}
		} catch (SQLException e) {
			plugin.debug("ResultSet error for junk table! " + e.getMessage());
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
				plugin.debug("Error closing junk table! " + e.getMessage());
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

	public int getTardisId() {
		return tardisId;
	}

	public String getSaveSign() {
		return saveSign;
	}

	public String getHandbrake() {
		return handbrake;
	}

	public String getWall() {
		return wall;
	}

	public String getFloor() {
		return floor;
	}

	public PRESET getPreset() {
		return preset;
	}
}
