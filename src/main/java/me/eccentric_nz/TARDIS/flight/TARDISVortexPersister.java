/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.flight;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.builders.BuildData;
import me.eccentric_nz.tardis.builders.TARDISInstantPoliceBox;
import me.eccentric_nz.tardis.builders.TARDISInstantPreset;
import me.eccentric_nz.tardis.database.TARDISDatabaseConnection;
import me.eccentric_nz.tardis.database.resultset.ResultSetBackLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetControls;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.destroyers.DestroyData;
import me.eccentric_nz.tardis.destroyers.TARDISDeinstantPreset;
import me.eccentric_nz.tardis.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.tardis.utility.TARDISStaticLocationGetters;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

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

    private final TARDISPlugin plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private int count = 0;

    public TARDISVortexPersister(TARDISPlugin plugin) {
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
            if (count > 0) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Saved " + count + " TARDISes floating around in the time vortex.");
            }
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
                                DestroyData dd = new DestroyData();
                                Location location = new Location(rsb.getWorld(), rsb.getX(), rsb.getY(), rsb.getZ());
                                dd.setLocation(location);
                                dd.setDirection(rsb.getDirection());
                                dd.setSubmarine(rsb.isSubmarine());
                                dd.setTardisId(id);
                                dd.setSiege(false);
                                dd.setThrottle(SpaceTimeThrottle.REBUILD);
                                dd.setPlayer(Bukkit.getOfflinePlayer(uuid));
                                while (!location.getChunk().isLoaded()) {
                                    location.getChunk().load();
                                }
                                new TARDISDeinstantPreset(plugin).instaDestroyPreset(dd, false, rs.getTardis().getDemat());
                            }
                        }
                        // interrupted materialisation
                        // get next destination and land
                        // next location = 'current' table
                        HashMap<String, Object> wherec = new HashMap<>();
                        wherec.put("tardis_id", id);
                        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
                        if (rsc.resultSet()) {
                            BuildData bd = new BuildData(uuid.toString());
                            bd.setTardisId(id);
                            Location location = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                            bd.setLocation(location);
                            OfflinePlayer olp = plugin.getServer().getOfflinePlayer(uuid);
                            bd.setPlayer(olp);
                            bd.setRebuild(false);
                            bd.setDirection(rsc.getDirection());
                            bd.setSubmarine(rsc.isSubmarine());
                            bd.setMalfunction(false);
                            bd.setThrottle(SpaceTimeThrottle.REBUILD);
                            while (!location.getChunk().isLoaded()) {
                                location.getChunk().load();
                            }
                            plugin.getTrackerKeeper().getMaterialising().add(id);
                            if (rs.getTardis().getPreset().usesItemFrame()) {
                                new TARDISInstantPoliceBox(plugin, bd, rs.getTardis().getPreset()).buildPreset();
                            } else {
                                new TARDISInstantPreset(plugin, bd, rs.getTardis().getPreset(), Material.LIGHT_GRAY_TERRACOTTA.createBlockData(), false).buildPreset();
                            }
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                plugin.getTrackerKeeper().getInVortex().remove(id);
                                plugin.getTrackerKeeper().getDidDematToVortex().remove(id);
                                plugin.getTrackerKeeper().getDestinationVortex().remove(id);
                                plugin.getTrackerKeeper().getDematerialising().remove(id);
                            }, 20L);
                        }
                        land++;
                    }
                } else {
                    // get handbrake location
                    HashMap<String, Object> whereh = new HashMap<>();
                    whereh.put("tardis_id", id);
                    whereh.put("type", 0);
                    whereh.put("secondary", 0);
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
