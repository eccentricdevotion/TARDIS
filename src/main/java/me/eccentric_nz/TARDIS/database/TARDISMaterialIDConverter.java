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
package me.eccentric_nz.TARDIS.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;

/**
 *
 * @author eccentric_nz
 */
public class TARDISMaterialIDConverter {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;

    public TARDISMaterialIDConverter(TARDIS plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    public void convert() {
        QueryFactory qf = new QueryFactory(this.plugin);
        PreparedStatement statement = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT c_id, block_data FROM condenser";
        String update = "UPDATE condenser SET block_data = ? WHERE c_id = ?";
        int i = 0;
        try {
            service.testConnection(connection);
            // do condenser data
            statement = connection.prepareStatement(query);
            ps = connection.prepareStatement(update);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    int c_id = rs.getInt("c_id");
                    String blockData = rs.getString("block_data");
                    try {
                        int id = Integer.parseInt(blockData);
                        // look up id, we know it's deprecated, that's why we're changing it...
                        String mat = Material.getMaterial(id).toString();
                        // update the record
                        ps.setString(1, mat);
                        ps.setInt(2, c_id);
                        ps.executeUpdate();
                    } catch (NumberFormatException e) {
                        plugin.debug("Condenser data was not a number");
                    }
                    i++;
                }
            }
        } catch (SQLException e) {
            plugin.debug("Conversion error for condenser IDs! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing condenser table (converting IDs)! " + e.getMessage());
            }
        }
        if (i > 0) {
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Converted " + i + " condenser IDs to material names");
            plugin.getConfig().set("conversions.condenser_done", true);
            plugin.saveConfig();
        }
    }
}
