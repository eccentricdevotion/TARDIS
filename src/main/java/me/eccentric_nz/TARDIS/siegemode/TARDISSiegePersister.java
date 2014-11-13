/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.siegemode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSiegePersister {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private int count = 0;

    public TARDISSiegePersister(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void load() {
        try {
            ps = connection.prepareStatement("SELECT tardis_id, siege_on FROM tardis");
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    if (rs.getBoolean("siege_on")) {
                        plugin.getTrackerKeeper().getInSiegeMode().add(rs.getInt("tardis_id"));
                        count++;
                    }
                }
            }
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Loaded " + count + " TARDISes in Siege Mode.");
        } catch (SQLException ex) {
            plugin.debug("ResultSet error for tardis table: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing tardis statement or resultset: " + ex.getMessage());
            }
        }
    }
}
