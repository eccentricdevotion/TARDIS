/*
 * Copyright (C) 2016 eccentric_nz
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
package me.eccentric_nz.TARDIS.hads;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @author eccentric_nz
 */
public class TARDISHadsPersister {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private int count = 0;
    private final String prefix;

    public TARDISHadsPersister(TARDIS plugin) {
        this.plugin = plugin;
        this.prefix = this.plugin.getPrefix();
    }

    public void save() {
        try {
            // save the dispersed police boxes
            ps = connection.prepareStatement("INSERT INTO " + prefix + "dispersed (uuid, world, x, y, z, tardis_id) VALUES (?, ?, ?, ?, ?, ?)");
            for (Map.Entry<UUID, Location> map : plugin.getTrackerKeeper().getDispersed().entrySet()) {
                Location l = map.getValue();
                String uuid = map.getKey().toString();
                ps.setString(1, uuid);
                ps.setString(2, l.getWorld().getName());
                ps.setInt(3, l.getBlockX());
                ps.setInt(4, l.getBlockY());
                ps.setInt(5, l.getBlockZ());
                // get tardis_id
                ResultSetTardisID rst = new ResultSetTardisID(plugin);
                rst.fromUUID(uuid);
                ps.setInt(6, rst.getTardis_id());
                count += ps.executeUpdate();
            }
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Saved " + count + " 'dispersed' TARDISes.");
        } catch (SQLException ex) {
            plugin.debug("Insert error for dispersed table: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing dispersed statement: " + ex.getMessage());
            }
        }
    }

    public void load() {
        try {
            ps = connection.prepareStatement("SELECT * FROM " + prefix + "dispersed");
            rs = ps.executeQuery();
            while (rs.next()) {
                World world = plugin.getServer().getWorld(rs.getString("world"));
                if (world != null) {
                    UUID uuid = UUID.fromString(rs.getString("uuid"));
                    Location l = new Location(world, rs.getInt("x"), rs.getInt("y"), rs.getInt("z"));
                    plugin.getTrackerKeeper().getDispersed().put(uuid, l);
                    plugin.getTrackerKeeper().getDispersedTARDII().add(rs.getInt("tardis_id"));
                    count++;
                }
            }
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Loaded " + count + " dispersed Police Boxes.");
            ps = connection.prepareStatement("DELETE FROM " + prefix + "dispersed");
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.debug("ResultSet error for dispersed table: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing dispersed statement or resultset: " + ex.getMessage());
            }
        }
    }
}
