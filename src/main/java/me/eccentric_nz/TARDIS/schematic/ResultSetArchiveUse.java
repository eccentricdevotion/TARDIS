/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.schematic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author eccentric_nz
 */
public class ResultSetArchiveUse {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String uuid;
    private final String name;
    private final String prefix;

    public ResultSetArchiveUse(TARDIS plugin, String uuid, String name) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.name = name;
        prefix = this.plugin.getPrefix();
    }

    public boolean inActive() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT use FROM " + prefix + "archive WHERE uuid = ? AND name = ?";
        try {
            service.testConnection(connection);
            ps = connection.prepareStatement(query);
            ps.setString(1, uuid);
            ps.setString(2, name);
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                return rs.getInt("use") == 1;
            }
            return true;
        } catch (SQLException e) {
            plugin.debug("ResultSet error for archive in use! " + e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing archive in use! " + e.getMessage());
            }
        }
    }
}
