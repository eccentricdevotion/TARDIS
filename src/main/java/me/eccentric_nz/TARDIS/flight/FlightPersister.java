/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.flight;

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
 *
 * @author eccentric_nz
 */
public class FlightPersister {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private int count = 0;

    public FlightPersister(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void save() {
        try {
            // save flying TARDISes
            ps = connection.prepareStatement("INSERT INTO " + prefix + "flight (uuid, tardis_id, location, chicken) VALUES (?, ?, ?, ?)");
            for (Map.Entry<UUID, FlightReturnData> map : plugin.getTrackerKeeper().getFlyingReturnLocation().entrySet()) {
                ps.setString(1, map.getKey().toString());
                ps.setInt(2, map.getValue().getId());
                ps.setString(3, map.getValue().getLocation().toString());
                ps.setString(4, map.getValue().getChicken().toString());
                count += ps.executeUpdate();
            }
            if (count > 0) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Saved " + count + " flying TARDISes.");
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
                plugin.debug("Error closing flight statement: " + ex.getMessage());
            }
        }
    }

    public void load() {
        try {
            // load flying TARDISes
            ps = connection.prepareStatement("SELECT * FROM " + prefix + "flight");
            rs = ps.executeQuery();
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                int id = rs.getInt("tardis_id");
                Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(rs.getString("location"));
                UUID chicken = UUID.fromString(rs.getString("chicken"));
                plugin.getTrackerKeeper().getFlyingReturnLocation().put(uuid, new FlightReturnData(id, location, -1, -1, chicken));
                count++;
            }
            if (count > 0) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Loaded " + count + " flying TARDISes.");
            }
            ps = connection.prepareStatement("DELETE FROM " + prefix + "flight");
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.debug("ResultSet error for flight table: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing flight statement or resultset: " + ex.getMessage());
            }
        }
    }
}
