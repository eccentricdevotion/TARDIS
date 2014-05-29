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
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

/**
 *
 * @author eccentric_nz
 */
public class TARDISBiomeUpdater implements Runnable {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;

    public TARDISBiomeUpdater(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.debug("Starting biome update...");
        QueryFactory qf = new QueryFactory(this.plugin);
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM current";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    if (rs.getString("biome").isEmpty()) {
                        String b = getBiome(rs.getString("world"), rs.getInt("x"), rs.getInt("y"), rs.getInt("z"));
                        if (!b.isEmpty()) {
                            HashMap<String, Object> where = new HashMap<String, Object>();
                            where.put("current_id", rs.getInt("current_id"));
                            HashMap<String, Object> set = new HashMap<String, Object>();
                            set.put("biome", b);
                            qf.doUpdate("current", set, where);
                        }
                    }
                }
            }
            plugin.getConfig().set("conversions.biome_update", true);
            plugin.saveConfig();
        } catch (SQLException e) {
            plugin.debug("Update error for current biome! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing current table (updating biome)! " + e.getMessage());
            }
        }
    }

    private String getBiome(String w, int x, int y, int z) {
        World world = plugin.getServer().getWorld(w);
        if (world == null) {
            return "";
        }
        Location l = new Location(world, x, y, z);
        while (!world.getChunkAt(l).isLoaded()) {
            world.getChunkAt(l).load();
        }
        return l.getBlock().getRelative(BlockFace.EAST, 2).getBiome().toString();
    }
}
