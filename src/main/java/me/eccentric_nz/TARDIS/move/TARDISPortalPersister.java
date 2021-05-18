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
package me.eccentric_nz.TARDIS.move;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISPortalPersister {

	private final TARDIS plugin;
	private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
	private final Connection connection = service.getConnection();
	private final String prefix;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private int count = 0;
	private int counta = 0;

	public TARDISPortalPersister(TARDIS plugin) {
		this.plugin = plugin;
		prefix = this.plugin.getPrefix();
	}

	public void save() {
		try {
			// save the portals
			ps = connection.prepareStatement("INSERT INTO " + prefix + "portals (portal, teleport, direction, tardis_id, abandoned) VALUES (?,?,?,?,?)");
			for (Map.Entry<Location, TARDISTeleportLocation> map : plugin.getTrackerKeeper().getPortals().entrySet()) {
				TARDISTeleportLocation ttpl = map.getValue();
				ps.setString(1, map.getKey().toString());
				ps.setString(2, ttpl.getLocation().toString());
				ps.setString(3, ttpl.getDirection().toString());
				ps.setInt(4, ttpl.getTardisId());
				ps.setInt(5, (ttpl.isAbandoned() ? 1 : 0));
				if (ttpl.isAbandoned()) {
					counta += ps.executeUpdate();
				} else {
					count += ps.executeUpdate();
				}
			}
			if (count > 0) {
				plugin.getConsole().sendMessage(plugin.getPluginName() + "Saved " + count + " portals.");
			}
			if (counta > 0) {
				plugin.getConsole().sendMessage(plugin.getPluginName() + "Saved " + counta + " abandoned portals.");
			}
			// save the players
			ps = connection.prepareStatement("INSERT INTO " + prefix + "movers (uuid) VALUES (?)");
			for (UUID uuid : plugin.getTrackerKeeper().getMover()) {
				ps.setString(1, uuid.toString());
				ps.executeUpdate();
			}
		} catch (SQLException ex) {
			plugin.debug("Insert error for portals table: " + ex.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException ex) {
				plugin.debug("Error closing portals statement: " + ex.getMessage());
			}
		}
	}

	public void load() {
		try {
			ps = connection.prepareStatement("SELECT * FROM " + prefix + "portals");
			rs = ps.executeQuery();
			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					String p = rs.getString("portal");
					String t = rs.getString("teleport");
					// check for null worlds
					if (!p.contains("null") && !t.contains("null")) {
						Location portal = TARDISStaticLocationGetters.getLocationFromBukkitString(p);
						Location teleport = TARDISStaticLocationGetters.getLocationFromBukkitString(t);
						if (portal != null && teleport != null) {
							COMPASS direction = COMPASS.valueOf(rs.getString("direction"));
							TARDISTeleportLocation ttpl = new TARDISTeleportLocation();
							ttpl.setLocation(teleport);
							ttpl.setDirection(direction);
							ttpl.setTardisId(rs.getInt("tardis_id"));
							boolean abandoned = rs.getBoolean("abandoned");
							ttpl.setAbandoned(abandoned);
							plugin.getTrackerKeeper().getPortals().put(portal, ttpl);
							if (abandoned) {
								counta++;
							} else {
								count++;
							}
						}
					}
				}
			}
			if (count > 0) {
				plugin.getConsole().sendMessage(plugin.getPluginName() + "Loaded " + count + " portals.");
			}
			if (counta > 0) {
				plugin.getConsole().sendMessage(plugin.getPluginName() + "Loaded " + counta + " abandoned portals.");
			}
			// clear the portals table so we don't get any duplicates when saving them
			ps = connection.prepareStatement("DELETE FROM " + prefix + "portals");
			ps.executeUpdate();
			// load the players associated with the portals
			ps = connection.prepareStatement("SELECT uuid FROM " + prefix + "movers");
			rs = ps.executeQuery();
			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					plugin.getTrackerKeeper().getMover().add(UUID.fromString(rs.getString("uuid")));
				}
			}
			// clear the movers table so we don't get any duplicates when saving them
			ps = connection.prepareStatement("DELETE FROM " + prefix + "movers");
			ps.executeUpdate();
		} catch (SQLException ex) {
			plugin.debug("ResultSet error for portals table: " + ex.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
				plugin.debug("Error closing portals statement or resultset: " + ex.getMessage());
			}
		}
	}
}
