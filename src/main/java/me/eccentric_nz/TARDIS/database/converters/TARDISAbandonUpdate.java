/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.database.converters;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author eccentric_nz
 */
public class TARDISAbandonUpdate {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final int id;
    private final String uuid;
    private final String prefix;

    public TARDISAbandonUpdate(TARDIS plugin, int id, String uuid) {
        this.plugin = plugin;
        this.id = id;
        this.uuid = uuid;
        prefix = this.plugin.getPrefix();
    }

    public void run() {
        PreparedStatement ps = null;
        String query;
        try {
            service.testConnection(connection);
            // tardis table
            query = "UPDATE " + prefix + "tardis SET abandoned = 1, powered_on = 0, companions = '' WHERE tardis_id = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
            // get current location
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
            if (rsc.resultSet()) {
                Current current = rsc.getCurrent();
                // back
                query = "UPDATE " + prefix + "back SET world = ?, x = ?, y = ?, z = ?, direction = ?, submarine = ? WHERE tardis_id = ?";
                ps = connection.prepareStatement(query);
                ps.setString(1, current.location().getWorld().getName());
                ps.setInt(2, current.location().getBlockX());
                ps.setInt(3, current.location().getBlockY());
                ps.setInt(4, current.location().getBlockZ());
                ps.setString(5, current.direction().toString());
                ps.setInt(6, (current.submarine() ? 1 : 0));
                ps.setInt(7, id);
                ps.executeUpdate();
                // home
                query = "UPDATE " + prefix + "homes SET world = ?, x = ?, y = ?, z = ?, direction = ?, submarine = ? WHERE tardis_id = ?";
                ps = connection.prepareStatement(query);
                ps.setString(1, current.location().getWorld().getName());
                ps.setInt(2, current.location().getBlockX());
                ps.setInt(3, current.location().getBlockY());
                ps.setInt(4, current.location().getBlockZ());
                ps.setString(5, current.direction().toString());
                ps.setInt(6, (current.submarine() ? 1 : 0));
                ps.setInt(7, id);
                ps.executeUpdate();
            }
            // saves
            query = "DELETE FROM " + prefix + "destinations WHERE tardis_id = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
            // junk
            query = "DELETE FROM " + prefix + "junk WHERE tardis_id = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
            // storage
            query = "UPDATE " + prefix + "storage SET tardis_id = '-1' WHERE tardis_id = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
            // t_count
            if (plugin.getConfig().getInt("creation.count") > 0 && plugin.getConfig().getBoolean("abandon.reduce_count")) {
                query = "UPDATE " + prefix + "t_count SET count = (count - 1) WHERE uuid = ?";
                ps = connection.prepareStatement(query);
                ps.setString(1, uuid);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            plugin.debug("Update error for abandon! " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing abandon associated tables! " + e.getMessage());
            }
        }
    }
}
