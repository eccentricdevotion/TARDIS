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
package me.eccentric_nz.TARDIS.database;

import me.eccentric_nz.TARDIS.TARDIS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISTimeRotorLoader {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private int count = 0;

    public TARDISTimeRotorLoader(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void load() {
        try {
            ps = connection.prepareStatement("SELECT tardis_id, rotor FROM " + prefix + "tardis");
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    String u = rs.getString("rotor");
                    // check for empty uuid
                    if (!u.isEmpty()) {
                        try {
                            UUID uuid = UUID.fromString(u);
                            plugin.getGeneralKeeper().getTimeRotors().add(uuid);
                            count++;
                        } catch (IllegalArgumentException e) {
                            plugin.debug("Invalid Time Rotor UUID: " + e.getMessage());
                        }
                    }
                }
            }
            if (count > 0) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Loaded " + count + " portals.");
            }
        } catch (SQLException ex) {
            plugin.debug("ResultSet error for time rotor loading: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing time rotor statement or resultset: " + ex.getMessage());
            }
        }
    }
}
