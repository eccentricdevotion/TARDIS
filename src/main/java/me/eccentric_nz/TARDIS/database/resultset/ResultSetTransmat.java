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
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetTransmat {

	private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
	private final Connection connection = service.getConnection();
	private final TARDIS plugin;
	private final int id;
	private final String name;
	private final String prefix;
	private int transmat_id;
	private int tardis_id;
	private String transmat_name;
	private String world;
	private float x;
	private float y;
	private float z;
	private float yaw;
	private Location location;

	/**
	 * Creates a class instance that can be used to retrieve an SQL ResultSet from the transmats table.
	 *
	 * @param plugin an instance of the main class.
	 * @param id     the TARDIS id to refine the search.
	 * @param name   the name of the transmat location to refine the search.
	 */
	public ResultSetTransmat(TARDIS plugin, int id, String name) {
		this.plugin = plugin;
		this.id = id;
		this.name = name;
		prefix = this.plugin.getPrefix();
	}

	/**
	 * Retrieves an SQL ResultSet from the transmats table. This method builds an SQL query string from the parameters
	 * supplied and then executes the query. Use the getters to retrieve the results.
	 *
	 * @return true or false depending on whether any data matches the query
	 */
	public boolean resultSet() {
		PreparedStatement statement = null;
		ResultSet rs = null;
		String query = "SELECT * FROM " + prefix + "transmats WHERE tardis_id = ? AND name = ?";
		try {
			service.testConnection(connection);
			statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			statement.setString(2, name);
			rs = statement.executeQuery();
			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					transmat_id = rs.getInt("transmat_id");
					tardis_id = rs.getInt("tardis_id");
					transmat_name = rs.getString("name");
					world = rs.getString("world");
					x = rs.getFloat("x");
					y = rs.getFloat("y");
					z = rs.getFloat("z");
					yaw = rs.getFloat("yaw");
					location = new Location(TARDISAliasResolver.getWorldFromAlias(world), x, y, z);
				}
			} else {
				return false;
			}
		} catch (SQLException e) {
			plugin.debug("ResultSet error for transmats table! " + e.getMessage());
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
				plugin.debug("Error closing transmats table! " + e.getMessage());
			}
		}
		return true;
	}

	public int getTransmat_id() {
		return transmat_id;
	}

	public int getTardis_id() {
		return tardis_id;
	}

	public String getTransmat_name() {
		return transmat_name;
	}

	public String getWorld() {
		return world;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public float getYaw() {
		return yaw;
	}

	public Location getLocation() {
		return location;
	}
}
