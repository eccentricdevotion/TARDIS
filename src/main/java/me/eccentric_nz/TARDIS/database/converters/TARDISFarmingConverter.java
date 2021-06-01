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
package me.eccentric_nz.tardis.database.converters;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.TARDISDatabaseConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TARDISFarmingConverter {

	private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
	private final Connection connection = service.getConnection();
	private final TARDISPlugin plugin;
	private final String prefix;

	public TARDISFarmingConverter(TARDISPlugin plugin) {
		this.plugin = plugin;
		prefix = this.plugin.getPrefix();
	}

	public void update() {
		Statement statement = null;
		PreparedStatement ps = null;
		int i = 0;
		try {
			service.testConnection(connection);
			connection.setAutoCommit(false);
			// do condenser data
			statement = connection.createStatement();
			// transfer farming locations from `tardis` table to `farming` table
			String farmQuery = "SELECT farm_id FROM " + prefix + "farming";
			ResultSet rsf = statement.executeQuery(farmQuery);
			if (!rsf.isBeforeFirst()) {
				// check for database fields
				HashMap<String, Boolean> rooms = new HashMap<>();
				rooms.put("birdcage", false);
				rooms.put("farm", false);
				rooms.put("hutch", false);
				rooms.put("igloo", false);
				rooms.put("stable", false);
				rooms.put("stall", false);
				rooms.put("village", false);
				StringBuilder tardisFarms = new StringBuilder("SELECT tardis_id, ");
				String check = (Objects.equals(plugin.getConfig().getString("storage.database"), "sqlite")) ?
						"SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "tardis' AND sql LIKE '%%%s%%'" :
						"SHOW COLUMNS FROM " + prefix + "tardis LIKE '%s'";
				ResultSet rsr;
				for (Map.Entry<String, Boolean> r : rooms.entrySet()) {
					String rquery = String.format(check, r.getKey());
					rsr = statement.executeQuery(rquery);
					if (rsr.isBeforeFirst()) {
						tardisFarms.append(r.getKey()).append(", ");
						rooms.put(r.getKey(), true);
					}
				}
				// delete final comma
				tardisFarms.delete(tardisFarms.length() - 2, tardisFarms.length());
				tardisFarms.append(" FROM ").append(prefix).append("tardis");
				ResultSet rstf = statement.executeQuery(tardisFarms.toString());
				String updateFarms = String.format("INSERT INTO %sfarming (tardis_id, birdcage, farm, hutch, igloo, stable, stall, village) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", prefix);
				ps = connection.prepareStatement(updateFarms);
				if (rstf.isBeforeFirst()) {
					while (rstf.next()) {
						String birdcage = (rooms.get("birdcage")) ? rstf.getString("birdcage") : "";
						String farm = (rooms.get("farm")) ? rstf.getString("farm") : "";
						String hutch = (rooms.get("hutch")) ? rstf.getString("hutch") : "";
						String igloo = (rooms.get("igloo")) ? rstf.getString("igloo") : "";
						String stable = (rooms.get("stable")) ? rstf.getString("stable") : "";
						String stall = (rooms.get("stall")) ? rstf.getString("stall") : "";
						String village = (rooms.get("village")) ? rstf.getString("village") : "";
						ps.setInt(1, rstf.getInt("tardis_id"));
						ps.setString(2, birdcage);
						ps.setString(3, farm);
						ps.setString(4, hutch);
						ps.setString(5, igloo);
						ps.setString(6, stable);
						ps.setString(7, stall);
						ps.setString(8, village);
						ps.addBatch();
						i++;
					}
				}
				if (i > 0) {
					ps.executeBatch();
					connection.commit();
					plugin.getConsole().sendMessage(plugin.getPluginName() + "Converted " + i + " farming records");
				}
			}
		} catch (SQLException e) {
			plugin.debug("Conversion error for condenser materials! " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (statement != null) {
					statement.close();
				}
				// reset auto commit
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				plugin.debug("Error closing condenser table (converting IDs)! " + e.getMessage());
			}
		}
	}
}

