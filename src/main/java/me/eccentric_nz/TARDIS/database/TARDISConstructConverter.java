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

package me.eccentric_nz.TARDIS.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;

/**
 *
 * @author eccentric_nz
 */
public class TARDISConstructConverter {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;

    public TARDISConstructConverter(TARDIS plugin) {
        this.plugin = plugin;
        this.prefix = this.plugin.getPrefix();
    }

    public boolean convert() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT tardis_id, chameleon_preset, chameleon_id, chameleon_data FROM " + prefix + "tardis WHERE (chameleon_preset = 'NEW' OR chameleon_preset = 'OLD') AND (chameleon_id != '35' OR chameleon_data != '11')";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();
            int i = 0;
            while (rs.next()) {
                PRESET preset = PRESET.valueOf(rs.getString("chameleon_preset"));
                new TARDISConstructBuilder(plugin, preset, rs.getInt("tardis_id"), rs.getInt("chameleon_id"), rs.getInt("chameleon_data")).buildAndSave();
                i++;
            }
            plugin.debug("Converted " + i + " shorted out Police Boxes to CONSTRUCTS.");
        } catch (SQLException e) {
            plugin.debug("ResultSet error for construct converter! " + e.getMessage());
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
                plugin.debug("Error closing construct converter! " + e.getMessage());
            }
        }
        return true;
    }
}