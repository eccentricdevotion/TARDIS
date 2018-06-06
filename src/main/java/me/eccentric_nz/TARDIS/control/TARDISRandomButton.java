/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISEmergencyRelocation;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetRepeaters;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISRandomButton {

    private final TARDIS plugin;
    private final Player player;
    private final int id;
    private final int level;
    private final HashMap<String, Object> set = new HashMap<>();
    private final int secondary;
    private final String comps;
    private final UUID ownerUUID;

    public TARDISRandomButton(TARDIS plugin, Player player, int id, int level, int secondary, String comps, UUID ownerUUID) {
        this.plugin = plugin;
        this.player = player;
        this.id = id;
        this.level = level;
        this.secondary = secondary;
        this.comps = comps;
        this.ownerUUID = ownerUUID;
    }

    public void clickButton() {
        int cost = plugin.getArtronConfig().getInt("random");
        if (level < cost) {
            TARDISMessage.send(player, "NOT_ENOUGH_ENERGY");
            return;
        }
        HashMap<String, Object> wherecl = new HashMap<>();
        wherecl.put("tardis_id", id);
        ResultSetCurrentLocation rscl = new ResultSetCurrentLocation(plugin, wherecl);
        if (!rscl.resultSet()) {
            // emergency TARDIS relocation
            new TARDISEmergencyRelocation(plugin).relocate(id, player);
            return;
        }
        COMPASS dir = rscl.getDirection();
        Location cl = new Location(rscl.getWorld(), rscl.getX(), rscl.getY(), rscl.getZ());
        if (player.hasPermission("tardis.exile") && plugin.getConfig().getBoolean("travel.exile")) {
            // get the exile area
            String permArea = plugin.getTardisArea().getExileArea(player);
            TARDISMessage.send(player, "EXILE", permArea);
            Location l = plugin.getTardisArea().getNextSpot(permArea);
            if (l == null) {
                TARDISMessage.send(player, "NO_MORE_SPOTS");
            } else {
                set.put("world", l.getWorld().getName());
                set.put("x", l.getBlockX());
                set.put("y", l.getBlockY());
                set.put("z", l.getBlockZ());
                set.put("direction", dir.toString());
                set.put("submarine", 0);
                TARDISMessage.send(player, "TRAVEL_APPROVED", permArea);
            }
        } else {
            ResultSetRepeaters rsr = new ResultSetRepeaters(plugin, id, secondary);
            if (rsr.resultSet()) {
                String environment = "THIS";
                int nether_min = plugin.getArtronConfig().getInt("nether_min");
                int the_end_min = plugin.getArtronConfig().getInt("the_end_min");
                int[] repeaters = rsr.getRepeaters();
                if (repeaters[0] == -1) {
                    TARDISMessage.send(player, "FLIGHT_BAD");
                    return;
                }
                if (repeaters[0] <= 3) { // first position
                    environment = "THIS";
                }
                if (repeaters[0] >= 4 && repeaters[0] <= 7) { // second position
                    environment = "NORMAL";
                }
                if (repeaters[0] >= 8 && repeaters[0] <= 11) { // third position
                    if (plugin.getConfig().getBoolean("travel.nether") && player.hasPermission("tardis.nether")) {
                        // check they have enough artron energy to travel to the NETHER
                        if (level < nether_min) {
                            environment = "NORMAL";
                            TARDISMessage.send(player, "NOT_ENOUGH_TRAVEL_ENERGY", String.format("%d", nether_min), "Nether");
                        } else {
                            environment = "NETHER";
                        }
                    } else {
                        String message = (player.hasPermission("tardis.nether")) ? "ANCIENT" : "NO_PERM_TRAVEL";
                        TARDISMessage.send(player, message, "Nether");
                    }
                }
                if (repeaters[0] >= 12 && repeaters[0] <= 15) { // last position
                    if (plugin.getConfig().getBoolean("travel.the_end") && player.hasPermission("tardis.end")) {
                        // check they have enough artron energy to travel to THE_END
                        if (level < the_end_min) {
                            environment = "NORMAL";
                            TARDISMessage.send(player, "NOT_ENOUGH_TRAVEL_ENERGY", String.format("%d", the_end_min), "End");
                        } else {
                            environment = "THE_END";
                        }
                    } else {
                        String message = (player.hasPermission("tardis.end")) ? "ANCIENT" : "NO_PERM_TRAVEL";
                        TARDISMessage.send(player, message, "End");
                    }
                }
                // create a random destination
                TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
                Location rand = tt.randomDestination(player, repeaters[1], repeaters[2], repeaters[3], dir, environment, rscl.getWorld(), false, cl);
                if (rand != null) {
                    set.put("world", rand.getWorld().getName());
                    set.put("x", rand.getBlockX());
                    set.put("y", rand.getBlockY());
                    set.put("z", rand.getBlockZ());
                    set.put("direction", dir.toString());
                    set.put("submarine", (plugin.getTrackerKeeper().getSubmarine().contains(id)) ? 1 : 0);
                    plugin.getTrackerKeeper().getSubmarine().remove(Integer.valueOf(id));
                    String worldname;
                    if (plugin.isMVOnServer()) {
                        worldname = plugin.getMVHelper().getAlias(rand.getWorld());
                    } else {
                        worldname = rand.getWorld().getName();
                    }
                    String dchat = worldname + " at x: " + rand.getBlockX() + " y: " + rand.getBlockY() + " z: " + rand.getBlockZ();
                    boolean isTL = true;
                    if (comps != null && !comps.isEmpty()) {
                        String[] companions = comps.split(":");
                        for (String c : companions) {
                            // are they online - AND are they travelling
                            UUID cuuid = UUID.fromString(c);
                            if (plugin.getServer().getPlayer(cuuid) != null && !cuuid.equals(ownerUUID)) {
                                // are they travelling
                                HashMap<String, Object> wherec = new HashMap<>();
                                wherec.put("tardis_id", id);
                                wherec.put("uuid", c);
                                ResultSetTravellers rsv = new ResultSetTravellers(plugin, wherec, false);
                                if (rsv.resultSet() && !plugin.getConfig().getBoolean("preferences.no_coords")) {
                                    TARDISMessage.send(plugin.getServer().getPlayer(cuuid), "DEST", dchat);
                                }
                            }
                            if (c.equalsIgnoreCase(player.getName())) {
                                isTL = false;
                            }
                        }
                    }
                    if (!plugin.getConfig().getBoolean("preferences.no_coords")) {
                        if (isTL == true) {
                            TARDISMessage.send(player, "DEST", dchat);
                        } else if (plugin.getServer().getPlayer(ownerUUID) != null) {
                            TARDISMessage.send(plugin.getServer().getPlayer(ownerUUID), "DEST", dchat);
                        }
                    }
                    HashMap<String, Object> wherel = new HashMap<>();
                    wherel.put("tardis_id", id);
                    new QueryFactory(plugin).doSyncUpdate("next", set, wherel);
                    plugin.getTrackerKeeper().getHasDestination().put(id, cost);
                    plugin.getTrackerKeeper().getRescue().remove(id);
                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                        new TARDISLand(plugin, id, player).exitVortex();
                    }
                } else if (plugin.getConfig().getBoolean("travel.no_destination_malfunctions")) {
                    plugin.getTrackerKeeper().getMalfunction().put(id, true);
                } else {
                    TARDISMessage.send(player, "PROTECTED");
                }
            }
        }
    }
}
