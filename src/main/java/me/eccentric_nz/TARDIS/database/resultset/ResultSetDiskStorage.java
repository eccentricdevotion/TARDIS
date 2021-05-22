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
import me.eccentric_nz.tardis.utility.TARDISStaticUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Tricky van Baalen was the youngest and the smartest of the van Baalen brothers. Tricky worked for his brothers Gregor
 * and Bram, finding and processing salvaged spaceships. Although made captain by their father, he suffered an accident
 * in which he lost his eyes, his voice, and his memory. He was fitted with synthetic eyes and a partially
 * electronic-sounding voice box; unable to replace his memory, his brothers instead convinced him that he was in fact
 * an android.
 *
 * @author eccentric_nz
 */
public class ResultSetDiskStorage {

	private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
	private final Connection connection = service.getConnection();
	private final TARDIS plugin;
	private final HashMap<String, Object> where;
	private final String prefix;
	private int id;
	private int tardis_id;
	private UUID uuid;
	private String savesOne;
	private String savesTwo;
	private String areas;
	private String players;
	private String biomesOne;
	private String biomesTwo;
	private String presetsOne;
	private String presetsTwo;
	private String circuits;
	private String console;

	/**
	 * Creates a class instance that can be used to retrieve an SQL ResultSet from the ars table.
	 *
	 * @param plugin an instance of the main class.
	 * @param where  a HashMap&lt;String, Object&gt; of table fields and values to refine the search.
	 */
	public ResultSetDiskStorage(TARDIS plugin, HashMap<String, Object> where) {
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
		String wheres = "";
		if (where != null) {
			StringBuilder sbw = new StringBuilder();
			where.forEach((key, value) -> sbw.append(key).append(" = ? AND "));
			wheres = " WHERE " + sbw.substring(0, sbw.length() - 5);
		}
		String query = "SELECT * FROM " + prefix + "storage" + wheres;
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
					id = rs.getInt("storage_id");
					tardis_id = rs.getInt("tardis_id");
					if (!rs.wasNull()) {
						uuid = UUID.fromString(rs.getString("uuid"));
					} else {
						uuid = TARDISStaticUtils.getZERO_UUID();
					}
					savesOne = rs.getString("saves_one");
					if (rs.wasNull()) {
						savesOne = "";
					}
					savesTwo = rs.getString("saves_two");
					if (rs.wasNull()) {
						savesTwo = "";
					}
					areas = rs.getString("areas");
					if (rs.wasNull()) {
						areas = "";
					}
					players = rs.getString("players");
					if (rs.wasNull()) {
						players = "";
					}
					biomesOne = rs.getString("biomes_one");
					if (rs.wasNull()) {
						biomesOne = "";
					}
					biomesTwo = rs.getString("biomes_two");
					if (rs.wasNull()) {
						biomesTwo = "";
					}
					presetsOne = rs.getString("presets_one");
					if (rs.wasNull()) {
						presetsOne = "";
					}
					presetsTwo = rs.getString("presets_two");
					if (rs.wasNull()) {
						presetsTwo = "";
					}
					circuits = rs.getString("circuits");
					if (rs.wasNull()) {
						circuits = "";
					}
					console = rs.getString("console");
					if (rs.wasNull()) {
						console = "";
					}
				}
			} else {
				return false;
			}
		} catch (SQLException e) {
			plugin.debug("ResultSet error for storage table! " + e.getMessage());
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
				plugin.debug("Error closing storage table! " + e.getMessage());
			}
		}
		return true;
	}

	public int getId() {
		return id;
	}

	public int getTardis_id() {
		return tardis_id;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getSavesOne() {
		return savesOne;
	}

	public String getSavesTwo() {
		return savesTwo;
	}

	public String getAreas() {
		return areas;
	}

	public String getPlayers() {
		return players;
	}

	public String getBiomesOne() {
		return biomesOne;
	}

	public String getBiomesTwo() {
		return biomesTwo;
	}

	public String getPresetsOne() {
		return presetsOne;
	}

	public String getPresetsTwo() {
		return presetsTwo;
	}

	public String getCircuits() {
		return circuits;
	}

	public String getConsole() {
		return console;
	}
}
