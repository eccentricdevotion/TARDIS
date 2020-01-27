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
package me.eccentric_nz.TARDIS.flight;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.builders.TARDISInstaPreset;
import me.eccentric_nz.TARDIS.database.*;
import me.eccentric_nz.TARDIS.destroyers.DestroyData;
import me.eccentric_nz.TARDIS.destroyers.TARDISDeinstaPreset;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISVortexPersister {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private int count = 0;
    private final String prefix;

    public TARDISVortexPersister(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void save() {
        try {
            // save the vortex TARDISes
            ps = connection.prepareStatement("INSERT INTO " + prefix + "vortex (tardis_id, task) VALUES (?, ?)");
            for (Map.Entry<Integer, Integer> map : plugin.getTrackerKeeper().getDestinationVortex().entrySet()) {
                ps.setInt(1, map.getKey());
                ps.setInt(2, map.getValue());
                count += ps.executeUpdate();
            }
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Saved " + count + " TARDISes floating around in the time vortex.");
        } catch (SQLException ex) {
            plugin.debug("Insert error for vortex table: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing vortex statement: " + ex.getMessage());
            }
        }
    }

    public void load() {
        try {
            // load vortex TARDISes
            ps = connection.prepareStatement("SELECT * FROM " + prefix + "vortex");
            rs = ps.executeQuery();
            int land = 0;
            while (rs.next()) {
                int id = rs.getInt("tardis_id");
                int task = rs.getInt("task");
                if (task < 0) {
                    // get Time Lord UUID
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("tardis_id", id);
                    ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
                    if (rs.resultSet()) {
                        UUID uuid = rs.getTardis().getUuid();
                        if (task == -1) {
                            // interrupted dematerialisation
                            // get previous location and destroy tardis
                            // previous location = 'back' table
                            HashMap<String, Object> whereb = new HashMap<>();
                            whereb.put("tardis_id", id);
                            ResultSetBackLocation rsb = new ResultSetBackLocation(plugin, whereb);
                            if (rsb.resultSet()) {
                                DestroyData dd = new DestroyData(plugin, uuid.toString());
                                Location location = new Location(rsb.getWorld(), rsb.getX(), rsb.getY(), rsb.getZ());
                                dd.setLocation(location);
                                dd.setDirection(rsb.getDirection());
                                dd.setSubmarine(rsb.isSubmarine());
                                dd.setTardisID(id);
                                dd.setBiome(null);
                                dd.setSiege(false);
                                while (!location.getChunk().isLoaded()) {
                                    location.getChunk().load();
                                }
                                new TARDISDeinstaPreset(plugin).instaDestroyPreset(dd, false, rs.getTardis().getDemat());
                            }
                        }
                        // interrupted materialisation
                        // get next destination and land
                        // next location = 'current' table
                        HashMap<String, Object> wherec = new HashMap<>();
                        wherec.put("tardis_id", id);
                        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
                        if (rsc.resultSet()) {
                            BuildData bd = new BuildData(plugin, uuid.toString());
                            bd.setTardisID(id);
                            Location location = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                            bd.setLocation(location);
                            bd.setBiome(rsc.getBiome());
                            bd.setDirection(rsc.getDirection());
                            bd.setSubmarine(rsc.isSubmarine());
                            bd.setMalfunction(false);
                            while (!location.getChunk().isLoaded()) {
                                location.getChunk().load();
                            }
                            plugin.getTrackerKeeper().getMaterialising().add(bd.getTardisID());
                            new TARDISInstaPreset(plugin, bd, rs.getTardis().getPreset(), Material.LIGHT_GRAY_TERRACOTTA.createBlockData(), false).buildPreset();
                        }
                        land++;
                    }
                } else {
                    // get handbrake location
                    HashMap<String, Object> whereh = new HashMap<>();
                    whereh.put("tardis_id", id);
                    whereh.put("type", 0);
                    ResultSetControls rsh = new ResultSetControls(plugin, whereh, false);
                    if (rsh.resultSet()) {
                        Location handbrake = TARDISStaticLocationGetters.getLocationFromBukkitString(rsh.getLocation());
                        new TARDISLoopingFlightSound(plugin, handbrake, id).run();
                        count++;
                    }
                }
            }
            if (count > 0) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Loaded " + count + " TARDISes floating in the time vortex.");
            }
            if (land > 0) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Landed " + land + " TARDISes that never got to materialise.");
            }
            ps = connection.prepareStatement("DELETE FROM " + prefix + "vortex");
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.debug("ResultSet error for vortex table: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing vortex statement or resultset: " + ex.getMessage());
            }
        }
    }
}
