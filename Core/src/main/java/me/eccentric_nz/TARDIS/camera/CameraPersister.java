/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.camera;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
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
public class CameraPersister {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private int count = 0;

    public CameraPersister(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void save() {
        try {
            // save players who have logged out while using the external camera / or were junk TARDIS travellers
            ps = connection.prepareStatement("INSERT INTO " + prefix + "camera (uuid, location) VALUES (?, ?)");
            for (Map.Entry<UUID, Location> map : plugin.getTrackerKeeper().getJunkRelog().entrySet()) {
                ps.setString(1, map.getKey().toString());
                ps.setString(2, map.getValue().toString());
                count += ps.executeUpdate();
            }
            for (Map.Entry<UUID, CameraLocation> map : TARDISCameraTracker.SPECTATING.entrySet()) {
                ps.setString(1, map.getKey().toString());
                ps.setString(2, map.getValue().getLocation().toString());
                count += ps.executeUpdate();
            }
            if (count > 0) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Saved " + count + " camera/junk players.");
            }
        } catch (SQLException ex) {
            plugin.debug("Insert error for flight table: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing camera statement: " + ex.getMessage());
            }
        }
    }

    public void load() {
        try {
            // load camera/junk players
            ps = connection.prepareStatement("SELECT * FROM " + prefix + "camera");
            rs = ps.executeQuery();
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(rs.getString("location"));
                plugin.getTrackerKeeper().getJunkRelog().put(uuid, location);
                count++;
            }
            if (count > 0) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Loaded " + count + " camera/junk players.");
            }
            ps = connection.prepareStatement("DELETE FROM " + prefix + "camera");
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.debug("ResultSet error for camera table: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing camera statement or resultset: " + ex.getMessage());
            }
        }
    }
}
