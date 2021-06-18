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
package me.eccentric_nz.tardis.forcefield;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.TardisDatabaseConnection;
import me.eccentric_nz.tardis.utility.TardisStaticLocationGetters;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class TardisForceFieldPersister {

    private final TardisPlugin plugin;
    private final TardisDatabaseConnection service = TardisDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private int count = 0;

    public TardisForceFieldPersister(TardisPlugin plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void save() {
        if (plugin.getTrackerKeeper().getActiveForceFields().size() > 0) {
            try {
                connection.setAutoCommit(false);
                ps = connection.prepareStatement("INSERT INTO " + prefix + "forcefield (uuid, location) VALUES (?, ?)");
                for (Map.Entry<UUID, Location> map : plugin.getTrackerKeeper().getActiveForceFields().entrySet()) {
                    ps.setString(1, map.getKey().toString());
                    ps.setString(2, map.getValue().toString());
                    ps.addBatch();
                    count++;
                }
                ps.executeBatch();
                connection.setAutoCommit(true);
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Saved " + count + " tardis force fields.");
            } catch (SQLException e) {
                plugin.debug("Insert error for force field query: " + e.getMessage());
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (SQLException e) {
                    plugin.debug("Error closing force field statement or resultset: " + e.getMessage());
                }
            }
        }
    }

    public void load() {
        try {
            ps = connection.prepareStatement("SELECT uuid, location FROM " + prefix + "forcefield");
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    UUID uuid = UUID.fromString(rs.getString("uuid"));
                    Location location = TardisStaticLocationGetters.getLocationFromBukkitString(rs.getString("location"));
                    // add to tracker
                    plugin.getTrackerKeeper().getActiveForceFields().put(uuid, location);
                    count++;
                }
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Loaded " + count + " tardis force fields.");
            }
            // clear the table
            ps = connection.prepareStatement("DELETE FROM " + prefix + "forcefield");
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.debug("ResultSet error for force field query: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing force field statement or resultset: " + e.getMessage());
            }
        }
    }
}
