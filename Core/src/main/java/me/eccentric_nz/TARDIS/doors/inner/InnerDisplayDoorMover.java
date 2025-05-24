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
package me.eccentric_nz.TARDIS.doors.inner;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISCache;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.exterior.TARDISEmergencyRelocation;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.mobfarming.TARDISFarmer;
import me.eccentric_nz.TARDIS.mobfarming.TARDISFollowerSpawner;
import me.eccentric_nz.TARDIS.mobfarming.TARDISPetsAndFollowers;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class InnerDisplayDoorMover {

    private final TARDIS plugin;

    public InnerDisplayDoorMover(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void exit(Player player, Block block) {
        Location block_loc = block.getLocation();
        String bw = block_loc.getWorld().getName();
        int bx = block_loc.getBlockX();
        int by = block_loc.getBlockY();
        int bz = block_loc.getBlockZ();
        String doorloc = bw + ":" + bx + ":" + by + ":" + bz;
        HashMap<String, Object> where = new HashMap<>();
        where.put("door_location", doorloc);
        ResultSetDoors rsd = new ResultSetDoors(plugin, where, false);
        if (rsd.resultSet()) {
            int id = rsd.getTardis_id();
            if (rsd.isLocked()) {
                plugin.getMessenger().sendStatus(player, "DOOR_DEADLOCKED");
                return;
            }
            if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                plugin.getMessenger().sendStatus(player, "SIEGE_NO_EXIT");
                return;
            }
            Tardis tardis = TARDISCache.BY_ID.get(id);
            if (tardis != null) {
                if (!tardis.isHandbrakeOn()) {
                    plugin.getMessenger().sendStatus(player, "HANDBRAKE_ENGAGE");
                    return;
                }
                ChameleonPreset preset = tardis.getPreset();
                float yaw = player.getLocation().getYaw();
                float pitch = player.getLocation().getPitch();
                boolean hb = tardis.isHandbrakeOn();
                Current current = TARDISCache.CURRENT.get(tardis.getTardisId());
                if (current == null) {
                    // emergency TARDIS relocation
                    new TARDISEmergencyRelocation(plugin).relocate(id, player);
                    return;
                }
                // get quotes player prefs
                boolean userQuotes = true;
                boolean minecart = false;
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
                if (rsp.resultSet()) {
                    userQuotes = rsp.isQuotesOn();
                    minecart = rsp.isMinecartOn();
                }
                // get the other door direction
                COMPASS exitDirection;
                HashMap<String, Object> other = new HashMap<>();
                other.put("tardis_id", id);
                other.put("door_type", 0);
                ResultSetDoors rsexit = new ResultSetDoors(plugin, other, false);
                if (rsexit.resultSet()) {
                    exitDirection = rsexit.getDoor_direction();
                } else {
                    exitDirection = current.direction(); // current direction
                }
                // is the TARDIS materialising?
                if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "LOST_IN_VORTEX");
                    return;
                }
                // Can't SHIFT-click if INVISIBLE preset
                if (preset.equals(ChameleonPreset.INVISIBLE)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "INVISIBILITY_SNEAK");
                    return;
                }
                Location exitLoc;
                // player is in the TARDIS - always exit to current location
                boolean opened = TARDISStaticUtils.isDoorOpen(block);
                if (opened && preset.hasDoor()) {
                    exitLoc = TARDISStaticLocationGetters.getLocationFromDB(rsexit.getDoor_location());
                } else {
                    exitLoc = current.location();
                }
                if (hb && exitLoc != null) {
                    COMPASS interiorDirection = rsd.getDoor_direction();
                    // change the yaw if the door directions are different
                    if (!interiorDirection.equals(exitDirection)) {
                        yaw += plugin.getGeneralKeeper().getDoorListener().adjustYaw(interiorDirection, exitDirection);
                    }
                    exitLoc.setYaw(yaw);
                    // get location from database
                    // make location safe i.e. outside the blue box
                    double ex = exitLoc.getX();
                    double ez = exitLoc.getZ();
                    if (opened) {
                        exitLoc.setX(ex + 0.5);
                        exitLoc.setZ(ez + 0.5);
                    } else {
                        switch (exitDirection.forPreset()) {
                            case NORTH -> {
                                exitLoc.setX(ex + 0.5);
                                exitLoc.setZ(ez + 2.5);
                            }
                            case EAST -> {
                                exitLoc.setX(ex - 1.5);
                                exitLoc.setZ(ez + 0.5);
                            }
                            case SOUTH -> {
                                exitLoc.setX(ex + 0.5);
                                exitLoc.setZ(ez - 1.5);
                            }
                            // WEST
                            default -> {
                                exitLoc.setX(ex + 2.5);
                                exitLoc.setZ(ez + 0.5);
                            }
                        }
                    }
                    // exit TARDIS!
                    plugin.getGeneralKeeper().getDoorListener().movePlayer(player, exitLoc, true, block.getWorld(), userQuotes, 2, minecart, false);
                    if (plugin.getConfig().getBoolean("allow.mob_farming") && TARDISPermission.hasPermission(player, "tardis.farm")) {
                        TARDISFarmer tf = new TARDISFarmer(plugin);
                        TARDISPetsAndFollowers petsAndFollowers = tf.exitPets(player);
                        if (petsAndFollowers != null) {
                            if (!petsAndFollowers.getPets().isEmpty()) {
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getGeneralKeeper().getDoorListener().movePets(petsAndFollowers.getPets(), exitLoc, player, exitDirection, false), 10L);
                            }
                            if (!petsAndFollowers.getFollowers().isEmpty()) {
                                new TARDISFollowerSpawner(plugin).spawn(petsAndFollowers.getFollowers(), exitLoc, player, exitDirection, false);
                            }
                        }
                    }
                    // remove player from traveller table
                    HashMap<String, Object> wheret = new HashMap<>();
                    wheret.put("uuid", player.getUniqueId().toString());
                    plugin.getQueryFactory().doSyncDelete("travellers", wheret);
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "LOST_IN_VORTEX");
                }
            }
        }
    }
}
