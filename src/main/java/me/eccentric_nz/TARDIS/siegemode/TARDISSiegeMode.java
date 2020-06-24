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
package me.eccentric_nz.TARDIS.siegemode;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.event.TARDISSiegeEvent;
import me.eccentric_nz.TARDIS.api.event.TARDISSiegeOffEvent;
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.desktop.TARDISUpgradeData;
import me.eccentric_nz.TARDIS.destroyers.DestroyData;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.enumeration.UseClay;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Siege mode is a feature of the TARDIS that can be activated using a lever under the console to prevent entry or exit.
 * Additionally, it makes the TARDIS impervious to all external damage. Siege mode requires power to activate or
 * deactivate.
 *
 * @author eccentric_nz
 */
public class TARDISSiegeMode {

    private final TARDIS plugin;

    public TARDISSiegeMode(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void toggleViaSwitch(int id, Player p) {
        // get the current siege status
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
        if (!rs.resultSet()) {
            return;
        }
        Tardis tardis = rs.getTardis();
        // get current location
        HashMap<String, Object> wherec = new HashMap<>();
        wherec.put("tardis_id", id);
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
        if (!rsc.resultSet()) {
            return;
        }
        Location current = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
        Block siege = current.getBlock();
        HashMap<String, Object> wheres = new HashMap<>();
        wheres.put("tardis_id", id);
        HashMap<String, Object> set = new HashMap<>();
        if (tardis.isSiege_on()) {
            // must have at least 10% power
            int min = (plugin.getArtronConfig().getInt("full_charge") / 100) * plugin.getArtronConfig().getInt("siege_transfer");
            if (min > tardis.getArtron_level()) {
                TARDISMessage.send(p, "SIEGE_POWER");
                return;
            }
            plugin.getPM().callEvent(new TARDISSiegeOffEvent(p, tardis));
            // remove siege block
            siege.setBlockData(TARDISConstants.AIR);
            // rebuild preset
            BuildData bd = new BuildData(p.getUniqueId().toString());
            bd.setDirection(rsc.getDirection());
            bd.setLocation(current);
            bd.setMalfunction(false);
            bd.setOutside(false);
            bd.setPlayer(p);
            bd.setRebuild(true);
            bd.setSubmarine(rsc.isSubmarine());
            bd.setTardisID(id);
            bd.setBiome(rsc.getBiome());
            bd.setThrottle(SpaceTimeThrottle.REBUILD);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getPresetBuilder().buildPreset(bd), 10L);
            set.put("siege_on", 0);
            // remove trackers
            if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                plugin.getTrackerKeeper().getInSiegeMode().remove(Integer.valueOf(id));
            }
            if (plugin.getConfig().getInt("siege.breeding") > 0 || plugin.getConfig().getInt("siege.growth") > 0) {
                String[] chu = tardis.getChunk().split(":");
                if (plugin.getConfig().getInt("siege.breeding") > 0) {
                    List<TARDISSiegeArea> breeding = new ArrayList<>();
                    plugin.getTrackerKeeper().getSiegeBreedingAreas().get(chu[0]).forEach((breeding_area) -> {
                        if (breeding_area.getId() != id) {
                            breeding.add(breeding_area);
                        }
                    });
                    if (breeding.size() > 0) {
                        plugin.getTrackerKeeper().getSiegeBreedingAreas().put(chu[0], breeding);
                    } else {
                        plugin.getTrackerKeeper().getSiegeBreedingAreas().remove(chu[0]);
                    }
                }
                if (plugin.getConfig().getInt("siege.growth") > 0) {
                    List<TARDISSiegeArea> growth = new ArrayList<>();
                    plugin.getTrackerKeeper().getSiegeGrowthAreas().get(chu[0]).forEach((growth_area) -> {
                        if (growth_area.getId() != id) {
                            growth.add(growth_area);
                        }
                    });
                    if (growth.size() > 0) {
                        plugin.getTrackerKeeper().getSiegeGrowthAreas().put(chu[0], growth);
                    } else {
                        plugin.getTrackerKeeper().getSiegeGrowthAreas().remove(chu[0]);
                    }
                }
            }
            if (plugin.getConfig().getBoolean("siege.texture")) {
                changeTextures(tardis.getUuid().toString(), tardis.getSchematic(), p, false);
            }
            TARDISMessage.send(p, "SIEGE_OFF");
        } else {
            // make sure TARDIS is not dispersed
            if (plugin.getTrackerKeeper().getDispersedTARDII().contains(id) || plugin.getTrackerKeeper().getInVortex().contains(id)) {
                TARDISMessage.send(p, "NOT_WHILE_DISPERSED");
                return;
            }
            // destroy tardis
            DestroyData dd = new DestroyData();
            dd.setDirection(rsc.getDirection());
            dd.setLocation(current);
            dd.setPlayer(p.getPlayer());
            dd.setHide(false);
            dd.setOutside(false);
            dd.setSiege(true);
            dd.setSubmarine(rsc.isSubmarine());
            dd.setTardisID(id);
            dd.setBiome(rsc.getBiome());
            dd.setThrottle(SpaceTimeThrottle.REBUILD);
            plugin.getPresetDestroyer().destroyPreset(dd);
            // track this siege block
            plugin.getTrackerKeeper().getInSiegeMode().add(id);
            set.put("siege_on", 1);
            TARDISMessage.send(p, "SIEGE_ON");
            plugin.getPM().callEvent(new TARDISSiegeEvent(p, tardis));
            // butcher hostile mobs?
            if (plugin.getConfig().getBoolean("siege.butcher")) {
                TARDISMessage.send(p, "SIEGE_BUTCHER");
                for (Entity ent : p.getNearbyEntities(72d, 32d, 72d)) {
                    if (ent instanceof Monster) {
                        if (ent instanceof Creeper) {
                            // check it is not the Artron Capacitor Creeper
                            Location cl = ent.getLocation();
                            Location dbl = TARDISStaticLocationGetters.getLocationFromDB(tardis.getCreeper());
                            if (cl.getBlockX() == dbl.getBlockX() && cl.getBlockY() == dbl.getBlockY() && cl.getBlockZ() == dbl.getBlockZ()) {
                                continue;
                            }
                        }
                        ent.remove();
                    }
                }
            }
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
            if (plugin.getConfig().getBoolean("siege.texture")) {
                changeTextures(tardis.getUuid().toString(), tardis.getSchematic(), p, true);
            }
            // turn off force field
            if (plugin.getTrackerKeeper().getActiveForceFields().containsKey(p.getUniqueId())) {
                plugin.getTrackerKeeper().getActiveForceFields().remove(p.getUniqueId());
            }
        }
        // update the database
        plugin.getQueryFactory().doUpdate("tardis", set, wheres);
    }

    void changeTextures(String uuid, Schematic schm, Player p, boolean toSiege) {
        ResultSetPlayerPrefs rspp = new ResultSetPlayerPrefs(plugin, uuid);
        if (rspp.resultSet()) {
            String wall = rspp.getWall();
            String floor = rspp.getFloor();
            String sw = rspp.getSiegeWall();
            String sf = rspp.getSiegeFloor();
            // determine 'use_clay' material
            UseClay use_clay;
            try {
                use_clay = UseClay.valueOf(plugin.getConfig().getString("creation.use_clay"));
            } catch (IllegalArgumentException e) {
                use_clay = UseClay.WOOL;
            }
            if (!use_clay.equals(UseClay.WOOL)) {
                if (sw.equals("GRAY_CLAY") || sw.equals("GREY_CONCRETE")) {
                    sw = "GRAY_WOOL";
                }
            }
            if (!use_clay.equals(UseClay.WOOL)) {
                if (sf.equals("BLACK_CLAY") || sw.equals("BLACK_CONCRETE")) {
                    sf = "BLACK_WOOL";
                }
            }
            // change to a saved theme
            TARDISUpgradeData tud = new TARDISUpgradeData();
            tud.setWall(wall);
            tud.setFloor(floor);
            tud.setSiegeWall(sw);
            tud.setSiegeFloor(sf);
            tud.setSchematic(schm);
            tud.setPrevious(schm);
            // start the rebuild
            TARDISSiegeWallFloorRunnable ttr = new TARDISSiegeWallFloorRunnable(plugin, p.getUniqueId(), tud, toSiege);
            long delay = Math.round(20 / plugin.getConfig().getDouble("growth.room_speed"));
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, ttr, 5L, delay);
            ttr.setTaskID(task);
        }
    }
}
