/*
 * Copyright (C) 2025 eccentric_nz
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
import me.eccentric_nz.TARDIS.builders.exterior.BuildData;
import me.eccentric_nz.TARDIS.builders.exterior.TARDISInstantPoliceBox;
import me.eccentric_nz.TARDIS.builders.exterior.TARDISInstantPreset;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetBackLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.destroyers.DestroyData;
import me.eccentric_nz.TARDIS.destroyers.TARDISDeinstantPreset;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
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

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private int count = 0;

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
            if (count > 0) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Saved " + count + " TARDISes floating around in the time vortex.");
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
                    ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                    if (rs.resultSet()) {
                        Tardis tardis = rs.getTardis();
                        UUID uuid = tardis.getUuid();
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
                                dd.setTardisID(id);
                                dd.setSiege(false);
                                dd.setThrottle(SpaceTimeThrottle.REBUILD);
                                dd.setPlayer(Bukkit.getOfflinePlayer(uuid));
                                while (!location.getChunk().isLoaded()) {
                                    location.getChunk().load();
                                }
                                new TARDISDeinstantPreset(plugin).instaDestroyPreset(dd, false, tardis.getDemat());
                            }
                        }
                        // interrupted materialisation
                        // get next destination and land
                        // next location = 'current' table
                        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
                        if (rsc.resultSet()) {
                            Current current = rsc.getCurrent();
                            BuildData bd = new BuildData(uuid.toString());
                            bd.setTardisID(id);
                            bd.setLocation(current.location());
                            OfflinePlayer olp = plugin.getServer().getOfflinePlayer(uuid);
                            bd.setPlayer(olp);
                            bd.setRebuild(false);
                            bd.setDirection(current.direction());
                            bd.setSubmarine(current.submarine());
                            bd.setMalfunction(false);
                            bd.setThrottle(SpaceTimeThrottle.REBUILD);
                            while (!current.location().getChunk().isLoaded()) {
                                current.location().getChunk().load();
                            }
                            plugin.getTrackerKeeper().getMaterialising().add(id);
                            if (tardis.getPreset().usesArmourStand()) {
                                new TARDISInstantPoliceBox(plugin, bd, tardis.getPreset()).buildPreset();
                            } else {
                                new TARDISInstantPreset(plugin, bd, tardis.getPreset(), Material.LIGHT_GRAY_TERRACOTTA.createBlockData(), false).buildPreset();
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
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Loaded " + count + " TARDISes floating in the time vortex.");
            }
            if (land > 0) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Landed " + land + " TARDISes that never got to materialise.");
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
