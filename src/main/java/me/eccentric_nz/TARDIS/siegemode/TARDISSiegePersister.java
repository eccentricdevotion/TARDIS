/*
 * Copyright (C) 2022 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetSiege;
import org.bukkit.Chunk;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

/**
 * @author eccentric_nz
 */
public class TARDISSiegePersister {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private int count = 0;

    public TARDISSiegePersister(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void loadSiege() {
        try {
            ps = connection.prepareStatement("SELECT tardis_id, siege_on FROM " + prefix + "tardis");
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    if (rs.getBoolean("siege_on")) {
                        int id = rs.getInt("tardis_id");
                        plugin.getTrackerKeeper().getInSiegeMode().add(id);
                        if (plugin.getConfig().getInt("siege.breeding") > 0 || plugin.getConfig().getInt("siege.growth") > 0) {
                            Chunk c = plugin.getLocationUtils().getTARDISChunk(id);
                            TARDISSiegeArea tsa = new TARDISSiegeArea(id, c);
                            if (plugin.getConfig().getInt("siege.breeding") > 0) {
                                List<TARDISSiegeArea> breeding_areas = plugin.getTrackerKeeper().getSiegeBreedingAreas().get(c.getWorld().getName());
                                if (breeding_areas == null) {
                                    breeding_areas = new ArrayList<>();
                                }
                                breeding_areas.add(tsa);
                                plugin.getTrackerKeeper().getSiegeBreedingAreas().put(c.getWorld().getName(), breeding_areas);
                            }
                            if (plugin.getConfig().getInt("siege.growth") > 0) {
                                List<TARDISSiegeArea> growth_areas = plugin.getTrackerKeeper().getSiegeGrowthAreas().get(c.getWorld().getName());
                                if (growth_areas == null) {
                                    growth_areas = new ArrayList<>();
                                }
                                growth_areas.add(tsa);
                                plugin.getTrackerKeeper().getSiegeGrowthAreas().put(c.getWorld().getName(), growth_areas);
                            }
                        }
                        count++;
                    }
                }
            }
            if (count > 0) {
                plugin.getLogger().log(Level.INFO, "Loaded " + count + " TARDISes in Siege Mode.");
            }
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

    public void loadCubes() {
        ResultSetSiege rss = new ResultSetSiege(plugin);
        if (rss.resultSet()) {
            HashMap<UUID, Integer> data = rss.getData();
            plugin.getTrackerKeeper().getSiegeCarrying().putAll(data);
            plugin.getTrackerKeeper().getIsSiegeCube().addAll(data.values());
            // clear the table
            try {
                ps = connection.prepareStatement("DELETE FROM " + prefix + "siege");
                ps.executeUpdate();
            } catch (SQLException ex) {
                plugin.debug("Delete error for siege table: " + ex.getMessage());
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (SQLException ex) {
                    plugin.debug("Error closing delete siege statement: " + ex.getMessage());
                }
            }
        }
    }

    public void saveCubes() {
        try {
            ps = connection.prepareStatement("INSERT INTO " + prefix + "siege (uuid, tardis_id) VALUES (?, ?)");
            int i = 0;
            for (Map.Entry<UUID, Integer> map : plugin.getTrackerKeeper().getSiegeCarrying().entrySet()) {
                ps.setString(1, map.getKey().toString());
                ps.setInt(2, map.getValue());
                i += ps.executeUpdate();
            }
            if (i > 0) {
                plugin.getLogger().log(Level.INFO, "Saved " + i + " Siege Cubes");
            }
        } catch (SQLException ex) {
            plugin.debug("Insert error for siege table: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing insert siege statement: " + ex.getMessage());
            }
        }
    }
}
