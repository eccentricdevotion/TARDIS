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
package me.eccentric_nz.TARDIS.autonomous;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.artron.TARDISAdaptiveBoxLampToggler;
import me.eccentric_nz.TARDIS.artron.TARDISBeaconToggler;
import me.eccentric_nz.TARDIS.artron.TARDISLampToggler;
import me.eccentric_nz.TARDIS.builders.exterior.BuildData;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.desktop.TARDISUpgradeData;
import me.eccentric_nz.TARDIS.desktop.TARDISWallFloorRunnable;
import me.eccentric_nz.TARDIS.destroyers.DestroyData;
import me.eccentric_nz.TARDIS.doors.inner.Inner;
import me.eccentric_nz.TARDIS.doors.inner.InnerDisplayDoorCloser;
import me.eccentric_nz.TARDIS.doors.inner.InnerDoor;
import me.eccentric_nz.TARDIS.doors.inner.InnerMinecraftDoorCloser;
import me.eccentric_nz.TARDIS.doors.outer.OuterDisplayDoorCloser;
import me.eccentric_nz.TARDIS.doors.outer.OuterDoor;
import me.eccentric_nz.TARDIS.doors.outer.OuterMinecraftDoorCloser;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.hads.TARDISCloisterBell;
import me.eccentric_nz.TARDIS.siegemode.TARDISSiegeArea;
import me.eccentric_nz.TARDIS.travel.TARDISEPSRunnable;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TARDISAutonomousDeath {

    private final TARDIS plugin;

    public TARDISAutonomousDeath(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void automate(Player player) {
        UUID uuid = player.getUniqueId();
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        // are they a time lord?
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            if (tardis.isPoweredOn()) {
                int id = tardis.getTardisId();
                String eps = tardis.getEps();
                String creeper = tardis.getCreeper();
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
                if (rsp.resultSet()) {
                    SpaceTimeThrottle spaceTimeThrottle = SpaceTimeThrottle.getByDelay().get(rsp.getThrottle());
                    // do they have the autonomous circuit on?
                    if (rsp.isAutoOn() && !tardis.isSiegeOn() && !plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
                        boolean isHomeDefault = rsp.getAutoDefault() == Autonomous.Default.HOME;
                        // close doors
                        ResultSetTardisPreset rstp = new ResultSetTardisPreset(plugin);
                        if (rstp.fromID(id)) {
                            boolean outerDisplayDoor = rstp.getPreset().usesArmourStand();
                            Inner innerDisplayDoor = new InnerDoor(plugin, id).get();
                            UUID playerUUID = player.getUniqueId();
                            // close inner
                            if (innerDisplayDoor.display()) {
                                new InnerDisplayDoorCloser(plugin).close(innerDisplayDoor.block(), id, playerUUID, true);
                            } else {
                                new InnerMinecraftDoorCloser(plugin).close(innerDisplayDoor.block(), id, playerUUID);
                            }
                            // close outer
                            if (outerDisplayDoor) {
                                new OuterDisplayDoorCloser(plugin).close(new OuterDoor(plugin, id).getDisplay(), id, playerUUID);
                            } else if (rstp.getPreset().hasDoor()) {
                                new OuterMinecraftDoorCloser(plugin).close(new OuterDoor(plugin, id).getMinecraft(), id, playerUUID);
                            }
                        }
                        Location death_loc = player.getLocation();
                        int amount = Math.round(plugin.getArtronConfig().getInt("autonomous") * spaceTimeThrottle.getArtronMultiplier());
                        if (tardis.getArtronLevel() > amount) {
                            if (plugin.getConfig().getBoolean("allow.emergency_npc") && rsp.isEpsOn()) {
                                // check if there are players in the TARDIS
                                HashMap<String, Object> wherev = new HashMap<>();
                                wherev.put("tardis_id", id);
                                ResultSetTravellers rst = new ResultSetTravellers(plugin, wherev, true);
                                if (rst.resultSet()) {
                                    List<UUID> data = rst.getData();
                                    if (!data.isEmpty() && !data.contains(uuid)) {
                                        // schedule the NPC to appear
                                        TARDISEPSRunnable EPS_runnable = new TARDISEPSRunnable(plugin, rsp.getEpsMessage(), player, data, id, eps, creeper);
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, EPS_runnable, 20L);
                                    }
                                }
                            }
                            String death_world = death_loc.getWorld().getName();
                            // where is the TARDIS Police Box?
                            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
                            if (!rsc.resultSet()) {
                                plugin.debug("Current record not found!");
                                return;
                            }
                            Current current = rsc.getCurrent();
                            COMPASS cd = current.direction();
                            // where is home?
                            HashMap<String, Object> wherehl = new HashMap<>();
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
                            // determine where to go
                            Autonomous autonomous = rsp.getAutoType();
                            switch (autonomous) {
                                case HOME -> {
                                    going_home = true;
                                    goto_loc = home_loc;
                                }
                                case AREAS -> {
                                    // look for a recharge location
                                    goto_loc = TARDISAutonomousUtils.getRecharger(death_world, player);
                                    if (goto_loc == null) {
                                        // no parking spots or no rechargers in the death world - use player's default
                                        if (isHomeDefault) {
                                            goto_loc = home_loc;
                                            going_home = true;
                                        }
                                    }
                                }
                                case CONFIGURED_AREAS -> {
                                    // look for a recharge location
                                    goto_loc = TARDISAutonomousUtils.getConfiguredRecharger(player);
                                    if (goto_loc == null) {
                                        // no parking spots - use player's default
                                        if (isHomeDefault) {
                                            goto_loc = home_loc;
                                            going_home = true;
                                        }
                                    }
                                }
                                case CLOSEST -> { // CLOSEST
                                    // if home world is NOT the death world
                                    if (!hw.getName().equals(death_world)) {
                                        // look for a recharge location
                                        goto_loc = TARDISAutonomousUtils.getRecharger(death_world, player);
                                        if (goto_loc == null) {
                                            // no parking spots - default to TARDIS home location
                                            goto_loc = home_loc;
                                            going_home = true;
                                        }
                                    } else {
                                        // died in home world, get the closest location
                                        Location recharger = TARDISAutonomousUtils.getRecharger(death_world, player);
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
                                }
                                default -> { // SAVE
                                    goto_loc = null;
                                    HashMap<String, Object> whered = new HashMap<>();
                                    whered.put("tardis_id", id);
                                    whered.put("autonomous", 1);
                                    ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
                                    if (rsd.resultSet()) {
                                        World world = plugin.getServer().getWorld(rsd.getWorld());
                                        if (world != null) {
                                            goto_loc = new Location(world, rsd.getX(), rsd.getY(), rsd.getZ());
                                        }
                                    }
                                    if (goto_loc == null) {
                                        // no autonomous save - default to TARDIS home location
                                        goto_loc = home_loc;
                                        going_home = true;
                                    }
                                }
                            }
                            // if the TARDIS is already at the home location, do nothing
                            if (goto_loc != null && !TARDISAutonomousUtils.compareCurrentToHome(current.location(), rsh)) {
                                // check for creation area
                                if (!plugin.getConfig().getString("creation.area", "none").equals("none") && plugin.getTardisArea().areaCheckLocPlayer(player, goto_loc)) {
                                    plugin.getTrackerKeeper().getPerm().remove(player.getUniqueId());
                                    return;
                                }
                                COMPASS fd = (going_home) ? hd : cd;
                                if (!plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                    // destroy police box
                                    DestroyData dd = new DestroyData();
                                    dd.setDirection(cd);
                                    dd.setLocation(current.location());
                                    dd.setPlayer(player);
                                    dd.setHide(false);
                                    dd.setOutside(false);
                                    dd.setSubmarine(current.submarine());
                                    dd.setTardisID(id);
                                    dd.setThrottle(spaceTimeThrottle);
                                    // set handbrake off
                                    HashMap<String, Object> set = new HashMap<>();
                                    set.put("handbrake_on", 0);
                                    HashMap<String, Object> tid = new HashMap<>();
                                    tid.put("tardis_id", id);
                                    if (!tardis.isHidden()) {
                                        plugin.getPresetDestroyer().destroyPreset(dd);
                                        plugin.getTrackerKeeper().getDematerialising().add(dd.getTardisID());
                                        plugin.getTrackerKeeper().getInVortex().add(id);
                                        // play tardis_takeoff sfx
                                        TARDISSounds.playTARDISSound(current.location(), "tardis_takeoff");
                                        // sound the cloister bell at current location for dematerialisation
                                        TARDISCloisterBell bell = new TARDISCloisterBell(plugin, 5, id, current.location(), plugin.getServer().getPlayer(uuid), true, "Time Lord death", false);
                                        int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, bell, 2L, 70L);
                                        bell.setTask(taskID);
                                        plugin.getTrackerKeeper().getCloisterBells().put(id, taskID);
                                    } else {
                                        plugin.getPresetDestroyer().removeBlockProtection(id);
                                        set.put("hidden", 0);
                                    }
                                    plugin.getQueryFactory().doUpdate("tardis", set, tid);
                                }
                                BuildData bd = new BuildData(uuid.toString());
                                bd.setDirection(fd);
                                bd.setLocation(goto_loc);
                                bd.setMalfunction(false);
                                bd.setPlayer(player);
                                bd.setRebuild(false);
                                bd.setOutside(false);
                                bd.setSubmarine(sub);
                                bd.setTardisID(id);
                                bd.setThrottle(spaceTimeThrottle);
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                    // rebuild police box - needs to be a delay
                                    plugin.getPresetBuilder().buildPreset(bd);
                                    plugin.getTrackerKeeper().getInVortex().add(id);
                                    // play tardis_land sfx
                                    TARDISSounds.playTARDISSound(bd.getLocation(), "tardis_land");
                                    // sound the cloister bell at current location for dematerialisation
                                    TARDISCloisterBell bell = new TARDISCloisterBell(plugin, 6, id, current.location(), plugin.getServer().getPlayer(uuid), false, "", true);
                                    int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, bell, 2L, 70L);
                                    bell.setTask(taskID);
                                    plugin.getTrackerKeeper().getCloisterBells().put(id, taskID);
                                    // set handbrake on
                                    HashMap<String, Object> seth = new HashMap<>();
                                    seth.put("handbrake_on", 1);
                                    HashMap<String, Object> wheret = new HashMap<>();
                                    wheret.put("tardis_id", id);
                                    plugin.getQueryFactory().doUpdate("tardis", seth, wheret);
                                    plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.AUTONOMOUS, id));
                                }, 500L);
                                // set current
                                HashMap<String, Object> setc = new HashMap<>();
                                setc.put("world", goto_loc.getWorld().getName());
                                setc.put("x", goto_loc.getBlockX());
                                setc.put("y", goto_loc.getBlockY());
                                setc.put("z", goto_loc.getBlockZ());
                                setc.put("direction", fd.toString());
                                setc.put("submarine", (sub) ? 1 : 0);
                                HashMap<String, Object> wherec = new HashMap<>();
                                wherec.put("tardis_id", id);
                                plugin.getQueryFactory().doUpdate("current", setc, wherec);
                                // set back
                                HashMap<String, Object> setb = new HashMap<>();
                                setb.put("world", current.location().getWorld().getName());
                                setb.put("x", current.location().getBlockX());
                                setb.put("y", current.location().getBlockY());
                                setb.put("z", current.location().getBlockZ());
                                setb.put("direction", current.direction().toString());
                                setb.put("submarine", (current.submarine()) ? 1 : 0);
                                HashMap<String, Object> whereb = new HashMap<>();
                                whereb.put("tardis_id", id);
                                plugin.getQueryFactory().doUpdate("back", setb, whereb);
                                // take energy
                                HashMap<String, Object> wherea = new HashMap<>();
                                wherea.put("tardis_id", id);
                                plugin.getQueryFactory().alterEnergyLevel("tardis", -amount, wherea, player);
                                // power down?
                                if (plugin.getConfig().getBoolean("allow.power_down")) {
                                    HashMap<String, Object> wherep = new HashMap<>();
                                    wherep.put("tardis_id", id);
                                    HashMap<String, Object> setp = new HashMap<>();
                                    // power down
                                    setp.put("powered_on", 0);
                                    // police box lamp, delay it in case the TARDIS needs rebuilding
                                    if (tardis.getPreset().equals(ChameleonPreset.ADAPTIVE) || tardis.getPreset().usesArmourStand()) {
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TARDISAdaptiveBoxLampToggler(plugin).toggleLamp(id, false, tardis.getPreset()), 1L);
                                    }
                                    // if lights are on, turn them off
                                    new TARDISLampToggler(plugin).flickSwitch(id, player.getUniqueId(), true, tardis.getSchematic().getLights());
                                    // if beacon is on turn it off
                                    new TARDISBeaconToggler(plugin).flickSwitch(player.getUniqueId(), id, false);
                                    plugin.getQueryFactory().doUpdate("tardis", setp, wherep);
                                }
                            }
                        } else if (plugin.getConfig().getBoolean("siege.enabled") && rsp.isAutoSiegeOn()) {
                            // enter siege mode
                            // where is the TARDIS Police Box?
                            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
                            if (!rsc.resultSet()) {
                                plugin.debug("Current record not found!");
                                return;
                            }
                            Current current = rsc.getCurrent();
                            Block siege = current.location().getBlock();
                            HashMap<String, Object> wheres = new HashMap<>();
                            wheres.put("tardis_id", id);
                            HashMap<String, Object> set = new HashMap<>();
                            // destroy tardis
                            DestroyData dd = new DestroyData();
                            dd.setDirection(current.direction());
                            dd.setLocation(current.location());
                            dd.setPlayer(player);
                            dd.setHide(false);
                            dd.setOutside(false);
                            dd.setSubmarine(current.submarine());
                            dd.setTardisID(id);
                            dd.setThrottle(spaceTimeThrottle);
                            plugin.getPresetDestroyer().destroyPreset(dd);
                            // sound the cloister bell at current location for siege mode
                            TARDISCloisterBell bell = new TARDISCloisterBell(plugin, 7, id, current.location(), plugin.getServer().getPlayer(uuid), true, "Siege mode engaged", false);
                            int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, bell, 2L, 70L);
                            bell.setTask(taskID);
                            plugin.getTrackerKeeper().getCloisterBells().put(id, taskID);
                            // place siege block
                            siege.setBlockData(TARDISConstants.BARRIER);
                            TARDISDisplayItemUtils.remove(siege);
                            TARDISDisplayItemUtils.set(TARDISDisplayItem.SIEGE_CUBE, siege, id);
                            // track this siege block
                            plugin.getTrackerKeeper().getInSiegeMode().add(id);
                            set.put("siege_on", 1);
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
                                // change to a dark theme
                                Schematic schm = tardis.getSchematic();
                                TARDISUpgradeData tud = new TARDISUpgradeData();
                                tud.setFloor("BLACK_WOOL");
                                tud.setWall("GRAY_WOOL");
                                tud.setSchematic(schm);
                                tud.setPrevious(schm);
                                // start the rebuild
                                TARDISWallFloorRunnable ttr = new TARDISWallFloorRunnable(plugin, player.getUniqueId(), tud);
                                long delay = Math.round(20 / plugin.getConfig().getDouble("growth.room_speed"));
                                int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, ttr, 5L, delay);
                                ttr.setTaskID(task);
                            }
                            // update the database
                            plugin.getQueryFactory().doUpdate("tardis", set, wheres);
                        } else if (player.isOnline()) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_NOT_AUTO");
                        }
                    }
                }
            } else if (player.isOnline()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "AUTO_POWER");
            }
        }
    }
}
