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
package me.eccentric_nz.tardis.destroyers;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.TARDISDatabaseConnection;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.command.CommandSender;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;

/**
 * Reesha was a rosebush-like plant who needed to keep herself pruned to prevent her consciousness from fading and
 * dying. She was invited by the Eighth Doctor to the founding of the Institute of Time.
 *
 * @author eccentric_nz
 */
public class TARDISPruner {

	private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
	private final Connection connection = service.getConnection();
	private final TARDISPlugin plugin;
	private final String prefix;

	public TARDISPruner(TARDISPlugin plugin) {
		this.plugin = plugin;
		prefix = this.plugin.getPrefix();
	}

	public void list(CommandSender sender, int days) {
		long millis = getTime(days);
		Timestamp prune = getTimestamp(millis);
		String query = "SELECT * FROM " + prefix + "tardis WHERE last_use < " + millis;
		Statement statement = null;
		ResultSet rs = null;
		try {
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			String file = plugin.getDataFolder() + File.separator + "TARDIS_Prune_List.txt";
			try {
				try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
					if (rs.isBeforeFirst()) {
						sender.sendMessage(plugin.getPluginName() + "Prune List:");
					} else {
						TARDISMessage.send(sender, "PRUNE_NONE");
					}
					while (rs.next()) {
						HashMap<String, Object> wherecl = new HashMap<>();
						wherecl.put("tardis_id", rs.getInt("tardis_id"));
						ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
						if (rsc.resultSet()) {
							// double check that this is an unused tardis
							Timestamp lastUse = new Timestamp(rs.getLong("last_use"));
							if (lastUse.before(prune)) {
								String line = "Time Lord: " + rs.getString("owner") + ", Location: " +
											  rsc.getWorld().getName() + ":" + rsc.getX() + ":" + rsc.getY() + ":" +
											  rsc.getZ();
								// write line to file
								bw.write(line);
								bw.newLine();
								// display the tardis prune list
								sender.sendMessage(line);
							}
						} else {
							plugin.debug(plugin.getLanguage().getString("CURRENT_NOT_FOUND"));
						}
					}
				}
			} catch (IOException e) {
				plugin.debug("Could not create and write to TARDIS_Prune_List.txt! " + e.getMessage());
			}
		} catch (SQLException e) {
			plugin.debug("ResultSet error trying to display prune list! " + e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				plugin.debug(e.getMessage());
			}
		}
	}

	public void prune(CommandSender sender, int days) {
		long millis = getTime(days);
		Timestamp prune = getTimestamp(millis);
		String query = "SELECT * FROM " + prefix + "tardis WHERE last_use < " + millis;
		Statement statement = null;
		ResultSet rs = null;
		try {
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			TARDISExterminator te = new TARDISExterminator(plugin);
			while (rs.next()) {
				// double check that this is an unused tardis
				Timestamp lastUse = new Timestamp(rs.getLong("last_use"));
				if (lastUse.before(prune)) {
					// remove the tardis
					if (te.exterminate(rs.getInt("tardis_id"))) {
						sender.sendMessage("Pruned " + rs.getString("owner") + "'s tardis");
					}
				}
			}
		} catch (SQLException e) {
			plugin.debug("ResultSet error trying to prune! " + e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				plugin.debug(e.getMessage());
			}
		}
	}

	private long getTime(int day) {
		long period = day * 86400000L;
		long now = System.currentTimeMillis();
		return now - period;
	}

	private Timestamp getTimestamp(long l) {
		return new Timestamp(l);
	}
}
