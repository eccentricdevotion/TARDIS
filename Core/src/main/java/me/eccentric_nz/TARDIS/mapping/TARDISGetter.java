/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.mapping;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.TARDISData;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.move.TARDISTeleportLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class TARDISGetter {

    private final TARDIS plugin;
    private final World world;
    private final String prefix;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();

    TARDISGetter(TARDIS plugin, World world) {
        this.plugin = plugin;
        this.world = world;
        prefix = plugin.getPrefix();
    }

    public void resultSetAsync(final GetterCallback callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            List<TARDISData> results = getList(world);
            // go back to the tick loop
            Bukkit.getScheduler().runTask(plugin, () -> {
                // call the callback with the result
                callback.onDone(results);
            });
        });
    }

    List<TARDISData> getList(World world) {
        List<TARDISData> dataList = new ArrayList<>();
        Statement statement = null;
        ResultSet rs = null;
        String query = "SELECT "
                + prefix + "tardis.tardis_id, "
                + prefix + "tardis.owner, "
                + prefix + "tardis.chameleon_preset, "
                + prefix + "tardis.size, "
                + prefix + "tardis.abandoned, "
                + prefix + "tardis.powered_on, "
                + prefix + "tardis.siege_on, "
                + prefix + "current.x, "
                + prefix + "current.y, "
                + prefix + "current.z"
                + " FROM " + prefix + "tardis, " + prefix + "current WHERE "
                + prefix + "tardis.tardis_id = " + prefix + "current.tardis_id";
        if (world != null) {
            query += " AND " + prefix + "current.world = '" + world.getName() + "'";
        } else {
            // build world list
            StringBuilder sb = new StringBuilder();
            // only get worlds that are enabled for time travel, and only regular worlds as dynmap doesn't support custom dimensions yet
            for (String planet : plugin.getPlanetsConfig().getConfigurationSection("planets").getKeys(false)) {
                if (plugin.getPlanetsConfig().getBoolean("planets." + planet + ".time_travel")) {
                    sb.append("'").append(planet).append("',");
                }
            }
            query += " AND " + prefix + "current.world IN (" + sb.substring(0, sb.length() - 1) + ")";
        }
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    int id = rs.getInt("tardis_id");
                    // get TARDIS data
                    String owner = rs.getString("owner");
                    Location current = new Location(world, rs.getInt("x"), rs.getInt("y"), rs.getInt("z"));
                    String console = rs.getString("size");
                    String chameleon = rs.getString("chameleon_preset");
                    String door = "Closed";
                    for (Map.Entry<Location, TARDISTeleportLocation> map : plugin.getTrackerKeeper().getPortals().entrySet()) {
                        if (!map.getKey().getWorld().getName().contains("TARDIS") && !map.getValue().isAbandoned()) {
                            if (id == map.getValue().getTardisId()) {
                                door = "Open";
                                break;
                            }
                        }
                    }
                    String powered = (rs.getBoolean("powered_on")) ? "Yes" : "No";
                    String siege = (rs.getBoolean("siege_on")) ? "Yes" : "No";
                    String abandoned = (rs.getBoolean("abandoned")) ? "Yes" : "No";
                    List<String> occupants = plugin.getTardisAPI().getPlayersInTARDIS(id);
                    TARDISData data = new TARDISData(owner, current, console, chameleon, door, powered, siege, abandoned, occupants);
                    dataList.add(data);
                }
            }
        } catch (SQLException e) {
            TARDIS.plugin.debug("ResultSet error for tardis/current table! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                TARDIS.plugin.debug("Error closing tardis/current table! " + e.getMessage());
            }
        }
        return dataList;
    }

    public interface GetterCallback {
        void onDone(List<TARDISData> results);
    }
}
