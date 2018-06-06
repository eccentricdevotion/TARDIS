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
package me.eccentric_nz.TARDIS.database;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.sql.*;

/**
 * @author eccentric_nz
 */
public class TARDISCompanionClearer {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;

    public TARDISCompanionClearer(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public boolean clear() {
        // backup database
        plugin.getConsole().sendMessage(plugin.getPluginName() + plugin.getLanguage().getString("BACKUP_DB"));
        File oldFile = new File(plugin.getDataFolder() + File.separator + "TARDIS.db");
        File newFile = new File(plugin.getDataFolder() + File.separator + "TARDIS_" + System.currentTimeMillis() + ".db");
        FileUtil.copy(oldFile, newFile);
        // get all TARDIS owners from database
        Statement statement = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT tardis_id FROM " + prefix + "tardis";
        String update = "UPDATE " + prefix + "tardis SET companions = '' WHERE tardis_id = ?";
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                ps = connection.prepareStatement(update);
                while (rs.next()) {
                    ps.setInt(1, rs.getInt("tardis_id"));
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for Companion clearing! " + e.getMessage());
            return false;
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
            } catch (SQLException e) {
                plugin.debug("Error closing tardis table! " + e.getMessage());
            }
        }
        return true;
    }
}
