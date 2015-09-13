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
package me.eccentric_nz.TARDIS.siegemode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.desktop.TARDISUpgradeData;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls.Pair;
import me.eccentric_nz.TARDIS.utility.TARDISLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

/**
 * Siege mode is a feature of the TARDIS that can be activated using a lever
 * under the console to prevent entry or exit. Additionally, it makes the TARDIS
 * impervious to all external damage. Siege mode requires power to activate or
 * deactivate.
 *
 * @author eccentric_nz
 */
public class TARDISSiegeMode {

    private final TARDIS plugin;

    public TARDISSiegeMode(TARDIS plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    public void toggleViaSwitch(int id, Player p) {
        // get the current siege status
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (!rs.resultSet()) {
            return;
        }
        boolean cham = (plugin.getConfig().getBoolean("travel.chameleon")) ? rs.isChamele_on() : false;
        // get current location
        HashMap<String, Object> wherec = new HashMap<String, Object>();
        wherec.put("tardis_id", id);
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
        if (!rsc.resultSet()) {
            return;
        }
        Location current = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
        Block siege = current.getBlock();
        HashMap<String, Object> wheres = new HashMap<String, Object>();
        wheres.put("tardis_id", id);
        HashMap<String, Object> set = new HashMap<String, Object>();
        if (rs.isSiege_on()) {
            // must have at least 10% power
            int min = (plugin.getArtronConfig().getInt("full_charge") / 100) * plugin.getArtronConfig().getInt("siege_transfer");
            if (min > rs.getArtron_level()) {
                TARDISMessage.send(p, "SIEGE_POWER");
                return;
            }
            // remove siege block
            siege.setType(Material.AIR);
            // rebuild preset
            final TARDISMaterialisationData pbd = new TARDISMaterialisationData();
            pbd.setChameleon(cham);
            pbd.setDirection(rsc.getDirection());
            pbd.setLocation(current);
            pbd.setMalfunction(false);
            pbd.setOutside(false);
            pbd.setPlayer(p);
            pbd.setRebuild(true);
            pbd.setSubmarine(rsc.isSubmarine());
            pbd.setTardisID(id);
            pbd.setBiome(rsc.getBiome());
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    plugin.getPresetBuilder().buildPreset(pbd);
                }
            }, 10L);
            set.put("siege_on", 0);
            // remove trackers
            if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                plugin.getTrackerKeeper().getInSiegeMode().remove(Integer.valueOf(id));
            }
            if (plugin.getConfig().getInt("siege.breeding") > 0 || plugin.getConfig().getInt("siege.growth") > 0) {
                String[] chu = rs.getChunk().split(":");
                String w = chu[0];
                if (plugin.getConfig().getInt("siege.breeding") > 0) {
                    List<TARDISSiegeArea> breeding = new ArrayList<TARDISSiegeArea>();
                    for (TARDISSiegeArea breeding_area : plugin.getTrackerKeeper().getSiegeBreedingAreas().get(w)) {
                        if (breeding_area.getId() != id) {
                            breeding.add(breeding_area);
                        }
                    }
                    if (breeding.size() > 0) {
                        plugin.getTrackerKeeper().getSiegeBreedingAreas().put(w, breeding);
                    } else {
                        plugin.getTrackerKeeper().getSiegeBreedingAreas().remove(w);
                    }
                }
                if (plugin.getConfig().getInt("siege.growth") > 0) {
                    List<TARDISSiegeArea> growth = new ArrayList<TARDISSiegeArea>();
                    for (TARDISSiegeArea growth_area : plugin.getTrackerKeeper().getSiegeGrowthAreas().get(w)) {
                        if (growth_area.getId() != id) {
                            growth.add(growth_area);
                        }
                    }
                    if (growth.size() > 0) {
                        plugin.getTrackerKeeper().getSiegeGrowthAreas().put(w, growth);
                    } else {
                        plugin.getTrackerKeeper().getSiegeGrowthAreas().remove(w);
                    }
                }

            }
            if (plugin.getConfig().getBoolean("siege.texture")) {
                changeTextures(rs.getUuid().toString(), rs.getSchematic(), p, false);
            }
            TARDISMessage.send(p, "SIEGE_OFF");
        } else {
            // destroy tardis
            final TARDISMaterialisationData pdd = new TARDISMaterialisationData();
            pdd.setChameleon(false);
            pdd.setDirection(rsc.getDirection());
            pdd.setLocation(current);
            pdd.setDematerialise(false);
            pdd.setPlayer(p.getPlayer());
            pdd.setHide(false);
            pdd.setOutside(false);
            pdd.setSubmarine(rsc.isSubmarine());
            pdd.setTardisID(id);
            pdd.setBiome(rsc.getBiome());
            plugin.getPresetDestroyer().destroyPreset(pdd);
            // place siege block
            siege.setType(Material.HUGE_MUSHROOM_1);
            siege.setData((byte) 14, true);
            // track this siege block
            plugin.getTrackerKeeper().getInSiegeMode().add(id);
            set.put("siege_on", 1);
            TARDISMessage.send(p, "SIEGE_ON");
            // butcher hostile mobs?
            if (plugin.getConfig().getBoolean("seige.butcher")) {
                TARDISMessage.send(p, "SIEGE_BUTCHER");
                for (Entity ent : p.getNearbyEntities(72d, 32d, 72d)) {
                    if (ent instanceof Monster) {
                        if (ent instanceof Creeper) {
                            // check it is  not the Artron Capacitor Creeper
                            Location cl = ent.getLocation();
                            Location dbl = TARDISLocationGetters.getLocationFromDB(rs.getCreeper(), 0.0f, 0.0f);
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
                        breeding_areas = new ArrayList<TARDISSiegeArea>();
                    }
                    breeding_areas.add(tsa);
                    plugin.getTrackerKeeper().getSiegeBreedingAreas().put(c.getWorld().getName(), breeding_areas);
                }
                if (plugin.getConfig().getInt("siege.growth") > 0) {
                    List<TARDISSiegeArea> growth_areas = plugin.getTrackerKeeper().getSiegeGrowthAreas().get(c.getWorld().getName());
                    if (growth_areas == null) {
                        growth_areas = new ArrayList<TARDISSiegeArea>();
                    }
                    growth_areas.add(tsa);
                    plugin.getTrackerKeeper().getSiegeGrowthAreas().put(c.getWorld().getName(), growth_areas);
                }
            }
            if (plugin.getConfig().getBoolean("siege.texture")) {
                changeTextures(rs.getUuid().toString(), rs.getSchematic(), p, true);
            }
        }
        // update the database
        new QueryFactory(plugin).doUpdate("tardis", set, wheres);
    }

    private void changeTextures(String uuid, SCHEMATIC schm, Player p, boolean toSiege) {
        HashMap<String, Object> wherepp = new HashMap<String, Object>();
        wherepp.put("uuid", uuid);
        ResultSetPlayerPrefs rspp = new ResultSetPlayerPrefs(plugin, wherepp);
        if (rspp.resultSet()) {
            Pair wall = plugin.getTardisWalls().blocks.get(rspp.getWall());
            Pair floor = plugin.getTardisWalls().blocks.get(rspp.getFloor());
            String sw = rspp.getSiegeWall();
            String sf = rspp.getSiegeFloor();
            if (plugin.getConfig().getBoolean("creation.use_clay")) {
                if (sw.equals("GRAY_CLAY") || sw.equals("GREY_CLAY")) {
                    sw = "GRAY_WOOL";
                }
            }
            if (plugin.getConfig().getBoolean("creation.use_clay")) {
                if (sf.equals("BLACK_CLAY")) {
                    sf = "BLACK_WOOL";
                }
            }
            Pair siege_wall = plugin.getTardisWalls().blocks.get(sw);
            Pair siege_floor = plugin.getTardisWalls().blocks.get(sf);
            // change to a saved theme
            TARDISUpgradeData tud = new TARDISUpgradeData();
            tud.setWall(wall.getType().toString() + ":" + wall.getData());
            tud.setFloor(floor.getType().toString() + ":" + floor.getData());
            tud.setSiegeWall(siege_wall.getType().toString() + ":" + siege_wall.getData());
            tud.setSiegeFloor(siege_floor.getType().toString() + ":" + siege_floor.getData());
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
