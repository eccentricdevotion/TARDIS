/*
 * Copyright (C) 2019 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISVortexPersister {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private int count = 0;
    private final String prefix;

    public TARDISVortexPersister(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void save() {
        try {
            // save the vortex TARDISes
            ps = connection.prepareStatement("INSERT INTO " + prefix + "vortex (tardis_id) VALUES (?)");
            for (Integer id : plugin.getTrackerKeeper().getDestinationVortex().keySet()) {
                ps.setInt(1, id);
                count += ps.executeUpdate();
            }
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Saved " + count + " TARDISes floating around in the time vortex.");
        } catch (SQLException ex) {
            plugin.debug("Insert error for vortex table: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing vortex statement: " + ex.getMessage());
            }
        }
    }

    public void load() {
        try {
            // load vortex TARDISes
            ps = connection.prepareStatement("SELECT * FROM " + prefix + "vortex");
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("tardis_id");
                // get handbrake location
                HashMap<String, Object> whereh = new HashMap<>();
                whereh.put("tardis_id", id);
                whereh.put("type", 0);
                ResultSetControls rsh = new ResultSetControls(plugin, whereh, false);
                if (rsh.resultSet()) {
                    Location handbrake = TARDISStaticLocationGetters.getLocationFromBukkitString(rsh.getLocation());
                    new TARDISLoopingFlightSound(plugin, handbrake, id).run();
                    count++;
                }
            }
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Loaded " + count + " TARDISes floating in the time vortex.");
            ps = connection.prepareStatement("DELETE FROM " + prefix + "vortex");
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.debug("ResultSet error for vortex table: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing vortex statement or resultset: " + ex.getMessage());
            }
        }
    }
}
