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
package me.eccentric_nz.TARDIS.rooms.eye;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.particles.Sphere;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;

import java.sql.*;
import java.util.UUID;

public class EyeStarter {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;
    private Statement statement = null;
    private PreparedStatement ps = null;

    public EyeStarter(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void goSuperNova() {
        ResultSet rs = null;
        try {
            String update = "UPDATE " + prefix + "eyes SET task = ? WHERE tardis_id = ?";
            ps = connection.prepareStatement(update);
            String query = "SELECT " + prefix + "controls.location, " + prefix + "eyes.capacitors, " + prefix + "tardis.uuid, " + prefix + "tardis.tardis_id " +
                    "FROM " + prefix + "controls, " + prefix + "eyes, " + prefix + "tardis " +
                    "WHERE " + prefix + "controls.type = 53 " +
                    "AND " + prefix + "controls.tardis_id = " + prefix + "eyes.tardis_id " +
                    "AND " + prefix + "eyes.tardis_id = " + prefix + "tardis.tardis_id";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                connection.setAutoCommit(false);
                int count = 0;
                while (rs.next()) {
                    int capacitors = rs.getInt("capacitors");
                    if (capacitors > 0) {
                        Location s = TARDISStaticLocationGetters.getLocationFromBukkitString(rs.getString("location"));
                        Capacitor capacitor = Capacitor.values()[rs.getInt("capacitors") - 1];
                        Sphere sphere = new Sphere(plugin, UUID.fromString(rs.getString("uuid")), s, capacitor);
                        int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, sphere, 0, 10);
                        sphere.setTaskID(task);
                        ps.setInt(1, task);
                        ps.setInt(2, rs.getInt("tardis_id"));
                        ps.addBatch();
                        count++;
                    }
                }
                if (count > 0) {
                    ps.executeBatch();
                }
                connection.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            plugin.debug("Error loading eye of harmony stars: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing eye of harmony stars: " + ex.getMessage());
            }
        }
    }
}
