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
package me.eccentric_nz.TARDIS.database.resultset;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ResultSetSonicLocation {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final UUID uuid;
    private final String prefix;

    private Location location;

    public ResultSetSonicLocation(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        prefix = this.plugin.getPrefix();
    }

    public boolean resultset() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT last_scanned, scan_type FROM " + prefix + "sonic WHERE sonic_uuid = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid.toString());
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                if (rs.getInt("scan_type") == 1) {
                    OfflinePlayer player = plugin.getServer().getOfflinePlayer(UUID.fromString(rs.getString("last_scanned")));
                    if (player.isOnline()) {
                        location = player.getPlayer().getLocation();
                    }
                } else {
                    String l = rs.getString("last_scanned");
                    if (!l.isEmpty()) {
                        location = TARDISStaticLocationGetters.getLocationFromBukkitString(l);
                    }
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for sonic last scanned! " + e.getMessage());
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
                plugin.debug("Error closing sonic last scanned! " + e.getMessage());
            }
        }
        return true;
    }

    public Location getLocation() {
        return location;
    }
}
