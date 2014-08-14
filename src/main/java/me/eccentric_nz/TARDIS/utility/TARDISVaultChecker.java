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
package me.eccentric_nz.TARDIS.utility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 *
 * @author eccentric_nz
 */
public class TARDISVaultChecker implements Runnable {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    List<Material> chests = Arrays.asList(Material.CHEST, Material.TRAPPED_CHEST);

    public TARDISVaultChecker(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Statement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM vaults";
        QueryFactory qf = new QueryFactory(plugin);
        int i = 0;
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    Location l = plugin.getUtils().getLocationFromBukkitString(rs.getString("location"));
                    if (l != null && !chests.contains(l.getBlock().getType())) {
                        int id = rs.getInt("v_id");
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("v_id", id);
                        qf.doDelete("vaults", where);
                        i++;
                    }
                }
            }
            if (i > 0) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Removed " + i + " unused vault room sorter chests!");
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for vaults table! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing vaults table! " + e.getMessage());
            }
        }
    }
}
