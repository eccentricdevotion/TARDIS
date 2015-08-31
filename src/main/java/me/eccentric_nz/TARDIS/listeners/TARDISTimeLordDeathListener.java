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
package me.eccentric_nz.TARDIS.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.arch.TARDISArchInventory;
import me.eccentric_nz.TARDIS.arch.TARDISArchPersister;
import me.eccentric_nz.TARDIS.artron.TARDISBeaconToggler;
import me.eccentric_nz.TARDIS.artron.TARDISLampToggler;
import me.eccentric_nz.TARDIS.artron.TARDISPoliceBoxLampToggler;
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.desktop.TARDISUpgradeData;
import me.eccentric_nz.TARDIS.desktop.TARDISWallFloorRunnable;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.siegemode.TARDISSiegeArea;
import me.eccentric_nz.TARDIS.travel.TARDISEPSRunnable;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Several events can trigger an Automatic Emergency Landing. Under these
 * circumstances a TARDIS will use the coordinate override to initiate an
 * Automatic Emergency Landing on the "nearest" available habitable planet.
 *
 * @author eccentric_nz
 */
public class TARDISTimeLordDeathListener implements Listener {

    private final TARDIS plugin;

    public TARDISTimeLordDeathListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player death. If the player is a time lord and the autonomous
     * circuit is engaged, then the TARDIS will automatically return to its
     * 'home' location, or the nearest Recharge area.
     *
     * @param event a player dying
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTimeLordDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID playerUUID = player.getUniqueId();
        if (plugin.getConfig().getBoolean("allow.autonomous")) {
            if (player.hasPermission("tardis.autonomous")) {
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("uuid", playerUUID.toString());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                // are they a time lord?
                if (rs.resultSet()) {
                    if (rs.isPowered_on()) {
                        final int id = rs.getTardis_id();
                        String eps = rs.getEps();
                        String creeper = rs.getCreeper();
                        HashMap<String, Object> whereu = new HashMap<String, Object>();
                        whereu.put("uuid", playerUUID.toString());
                        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, whereu);
                        if (rsp.resultSet()) {
                            // do they have the autonomous circuit on?
                            if (rsp.isAutoOn() && !rs.isSiege_on()) {
                                Location death_loc = player.getLocation();
                                int amount = plugin.getArtronConfig().getInt("autonomous");
                                if (rs.getArtron_level() > amount) {
                                    if (plugin.getPM().isPluginEnabled("Citizens") && plugin.getConfig().getBoolean("allow.emergency_npc") && rsp.isEpsOn()) {
                                        // check if there are players in the TARDIS
                                        HashMap<String, Object> wherev = new HashMap<String, Object>();
                                        wherev.put("tardis_id", id);
                                        ResultSetTravellers rst = new ResultSetTravellers(plugin, wherev, true);
                                        if (rst.resultSet()) {
                                            List<UUID> data = rst.getData();
                                            if (data.size() > 0 && !data.contains(playerUUID)) {
                                                // schedule the NPC to appear
                                                TARDISEPSRunnable EPS_runnable = new TARDISEPSRunnable(plugin, rsp.getEpsMessage(), player, data, id, eps, creeper);
                                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, EPS_runnable, 20L);
                                            }
                                        }
                                    }
                                    String death_world = death_loc.getWorld().getName();
                                    // where is the TARDIS Police Box?
                                    HashMap<String, Object> wherecl = new HashMap<String, Object>();
                                    wherecl.put("tardis_id", id);
                                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                                    if (!rsc.resultSet()) {
                                        plugin.debug("Current record not found!");
                                        return;
                                    }
                                    COMPASS cd = rsc.getDirection();
                                    Location sl = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                                    // where is home?
                                    HashMap<String, Object> wherehl = new HashMap<String, Object>();
                                    wherehl.put("tardis_id", id);
                                    ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, wherehl);
                                    if (!rsh.resultSet()) {
                                        plugin.debug("Home record not found!");
                                        return;
                                    }
                                    World hw = rsh.getWorld();
                                    Location home_loc = new Location(hw, rsh.getX(), rsh.getY(), rsh.getZ());
                                    COMPASS hd = rsh.getDirection();
                                    boolean sub = rsh.isSubmarine();
                                    Location goto_loc;
                                    boolean going_home = false;
                                    // if home world is NOT the death world
                                    if (!hw.getName().equals(death_world)) {
                                        // look for a recharge location
                                        goto_loc = getRecharger(death_world, player);
                                        if (goto_loc == null) {
                                            // no parking spots - default to TARDIS home location
                                            goto_loc = home_loc;
                                            going_home = true;
                                        }
                                    } else {
                                        // died in home world get closest location
                                        Location recharger = getRecharger(death_world, player);
                                        if (recharger != null) {
                                            // which is closer?
                                            boolean closer = death_loc.distanceSquared(home_loc) > death_loc.distanceSquared(recharger);
                                            goto_loc = (closer) ? recharger : home_loc;
                                            if (!closer) {
                                                going_home = true;
                                            }
                                        } else {
                                            // no parking spots - set to TARDIS home location
                                            goto_loc = home_loc;
                                            going_home = true;
                                        }
                                    }
                                    // if the TARDIS is already at the home location, do nothing
                                    if (!compareCurrentToHome(rsc, rsh)) {
                                        // check for creation area
                                        if (!plugin.getConfig().getString("creation.area").equals("none") && plugin.getTardisArea().areaCheckLocPlayer(player, goto_loc)) {
                                            plugin.getTrackerKeeper().getPerm().remove(player.getUniqueId());
                                            return;
                                        }
                                        QueryFactory qf = new QueryFactory(plugin);
                                        boolean cham = rs.isChamele_on();
                                        COMPASS fd = (going_home) ? hd : cd;
                                        // destroy police box
                                        final TARDISMaterialisationData pdd = new TARDISMaterialisationData();
                                        pdd.setChameleon(cham);
                                        pdd.setDirection(cd);
                                        pdd.setLocation(sl);
                                        pdd.setDematerialise(plugin.getConfig().getBoolean("police_box.materialise"));
                                        pdd.setPlayer(player);
                                        pdd.setHide(false);
                                        pdd.setOutside(false);
                                        pdd.setSubmarine(rsc.isSubmarine());
                                        pdd.setTardisID(id);
                                        pdd.setBiome(rsc.getBiome());
                                        if (!rs.isHidden()) {
                                            plugin.getPresetDestroyer().destroyPreset(pdd);
                                        } else {
                                            plugin.getPresetDestroyer().removeBlockProtection(id, qf);
                                            HashMap<String, Object> set = new HashMap<String, Object>();
                                            set.put("hidden", 0);
                                            HashMap<String, Object> tid = new HashMap<String, Object>();
                                            tid.put("tardis_id", id);
                                            qf.doUpdate("tardis", set, tid);
                                        }
                                        final TARDISMaterialisationData pbd = new TARDISMaterialisationData();
                                        pbd.setChameleon(cham);
                                        pbd.setDirection(fd);
                                        pbd.setLocation(goto_loc);
                                        pbd.setMalfunction(false);
                                        pbd.setPlayer(player);
                                        pbd.setRebuild(false);
                                        pbd.setOutside(false);
                                        pbd.setSubmarine(sub);
                                        pbd.setTardisID(id);
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                            @Override
                                            public void run() {
                                                // rebuild police box - needs to be a delay
                                                plugin.getPresetBuilder().buildPreset(pbd);
                                            }
                                        }, 500L);
                                        // set current
                                        HashMap<String, Object> setc = new HashMap<String, Object>();
                                        setc.put("world", goto_loc.getWorld().getName());
                                        setc.put("x", goto_loc.getBlockX());
                                        setc.put("y", goto_loc.getBlockY());
                                        setc.put("z", goto_loc.getBlockZ());
                                        setc.put("direction", fd.toString());
                                        setc.put("submarine", (sub) ? 1 : 0);
                                        HashMap<String, Object> wherec = new HashMap<String, Object>();
                                        wherec.put("tardis_id", id);
                                        qf.doUpdate("current", setc, wherec);
                                        // set back
                                        HashMap<String, Object> setb = new HashMap<String, Object>();
                                        setb.put("world", rsc.getWorld().getName());
                                        setb.put("x", rsc.getX());
                                        setb.put("y", rsc.getY());
                                        setb.put("z", rsc.getZ());
                                        setb.put("direction", rsc.getDirection().toString());
                                        setb.put("submarine", (rsc.isSubmarine()) ? 1 : 0);
                                        HashMap<String, Object> whereb = new HashMap<String, Object>();
                                        whereb.put("tardis_id", id);
                                        qf.doUpdate("back", setb, whereb);
                                        // take energy
                                        HashMap<String, Object> wherea = new HashMap<String, Object>();
                                        wherea.put("tardis_id", id);
                                        qf.alterEnergyLevel("tardis", -amount, wherea, player);
                                        // power down?
                                        if (plugin.getConfig().getBoolean("allow.power_down")) {
                                            HashMap<String, Object> wherep = new HashMap<String, Object>();
                                            wherep.put("tardis_id", id);
                                            HashMap<String, Object> setp = new HashMap<String, Object>();
                                            // power down
                                            setp.put("powered_on", 0);
                                            // police box lamp, delay it incase the TARDIS needs rebuilding
                                            if (rs.getPreset().equals(PRESET.NEW) || rs.getPreset().equals(PRESET.OLD)) {
                                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        new TARDISPoliceBoxLampToggler(plugin).toggleLamp(id, false);
                                                    }
                                                }, 1L);
                                            }
                                            // if lights are on, turn them off
                                            new TARDISLampToggler(plugin).flickSwitch(id, player.getUniqueId(), true, rs.getSchematic().hasLanterns());
                                            // if beacon is on turn it off
                                            new TARDISBeaconToggler(plugin).flickSwitch(player.getUniqueId(), false);
                                            qf.doUpdate("tardis", setp, wherep);
                                        }
                                    }
                                } else if (plugin.getConfig().getBoolean("siege.enabled") && rsp.isAutoSiegeOn()) {
                                // enter siege mode
                                    // where is the TARDIS Police Box?
                                    HashMap<String, Object> wherecl = new HashMap<String, Object>();
                                    wherecl.put("tardis_id", id);
                                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                                    if (!rsc.resultSet()) {
                                        plugin.debug("Current record not found!");
                                        return;
                                    }
                                    Location sl = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                                    Block siege = sl.getBlock();
                                    HashMap<String, Object> wheres = new HashMap<String, Object>();
                                    wheres.put("tardis_id", id);
                                    HashMap<String, Object> set = new HashMap<String, Object>();
                                    // destroy tardis
                                    final TARDISMaterialisationData pdd = new TARDISMaterialisationData();
                                    pdd.setChameleon(false);
                                    pdd.setDirection(rsc.getDirection());
                                    pdd.setLocation(sl);
                                    pdd.setDematerialise(false);
                                    pdd.setPlayer(player);
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
                                        // change to a dark theme
                                        SCHEMATIC schm = rs.getSchematic();
                                        TARDISUpgradeData tud = new TARDISUpgradeData();
                                        tud.setFloor("WOOL:15");
                                        tud.setWall("WOOL:7");
                                        tud.setSchematic(schm);
                                        tud.setPrevious(schm);
                                        // start the rebuild
                                        TARDISWallFloorRunnable ttr = new TARDISWallFloorRunnable(plugin, player.getUniqueId(), tud);
                                        long delay = Math.round(20 / plugin.getConfig().getDouble("growth.room_speed"));
                                        int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, ttr, 5L, delay);
                                        ttr.setTaskID(task);
                                    }
                                    // update the database
                                    new QueryFactory(plugin).doUpdate("tardis", set, wheres);
                                } else {
                                    if (player.isOnline()) {
                                        TARDISMessage.send(player, "ENERGY_NOT_AUTO");
                                    }
                                }
                            }
                        }
                    } else {
                        if (player.isOnline()) {
                            TARDISMessage.send(player, "AUTO_POWER");
                        }
                    }
                }
            }
        }
        // save arched status
        if (plugin.isDisguisesOnServer() && plugin.getConfig().getBoolean("arch.enabled") && plugin.getTrackerKeeper().getJohnSmith().containsKey(playerUUID)) {
            new TARDISArchPersister(plugin).save(playerUUID);
            if (plugin.getConfig().getBoolean("arch.clear_inv_on_death")) {
                // clear inventories
                new TARDISArchInventory().clear(playerUUID);
            }
        }
    }

    private Location getRecharger(String world, Player player) {
        Location l = null;
        HashMap<String, Object> wherea = new HashMap<String, Object>();
        wherea.put("world", world);
        ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false);
        if (rsa.resultSet()) {
            String area = rsa.getAreaName();
            if (!player.hasPermission("tardis.area." + area) || !player.isPermissionSet("tardis.area." + area)) {
                return null;
            }
            l = plugin.getTardisArea().getNextSpot(rsa.getAreaName());
        }
        return l;
    }

    private boolean compareCurrentToHome(ResultSetCurrentLocation c, ResultSetHomeLocation h) {
        return (c.getWorld().equals(h.getWorld())
                && c.getX() == h.getX()
                && c.getY() == h.getY()
                && c.getZ() == h.getZ());
    }
}
