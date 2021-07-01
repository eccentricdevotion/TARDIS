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
package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.TARDISEmergencyRelocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetRepeaters;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravelledTo;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
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
        if (TARDISPermission.hasPermission(player, "tardis.exile") && plugin.getConfig().getBoolean("travel.exile")) {
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
                if (repeaters[0] == 1) { // first position
                    environment = "THIS";
                    // check TARDIS travel is allowed in this world
                    if (!plugin.getPlanetsConfig().getBoolean("planets." + rscl.getWorld().getName() + ".time_travel")) {
                        TARDISMessage.send(player, "NO_WORLD_TRAVEL");
                        return;
                    }
                }
                if (repeaters[0] == 2) { // second position - normal
                    environment = "NORMAL";
                }
                if (repeaters[0] == 3) { // third position - nether
                    environment = "NORMAL";
                    if (!plugin.getConfig().getBoolean("travel.nether")) {  // nether travel enabled
                        TARDISMessage.send(player, "ANCIENT", "Nether");
                    } else if (!TARDISPermission.hasPermission(player, "tardis.nether")) {    // nether permission
                        TARDISMessage.send(player, "NO_PERM_TRAVEL", "Nether");
                    } else if (plugin.getConfig().getBoolean("travel.allow_nether_after_visit") && !new ResultSetTravelledTo(plugin).resultSet(player.getUniqueId().toString(), "NETHER")) { // check if they need to visit nether first
                        TARDISMessage.send(player, "TRAVEL_NOT_VISITED", "Nether");
                    } else if (level < nether_min) {    // check if they have enough artron to travel to the nether
                        TARDISMessage.send(player, "NOT_ENOUGH_TRAVEL_ENERGY", String.format("%d", nether_min), "Nether");
                    } else {    // player can go to the nether! yay
                        environment = "NETHER";
                    }
                }
                if (repeaters[0] == 4) { // last position - the end
                    environment = "NORMAL";
                    if (!plugin.getConfig().getBoolean("travel.the_end")) {  // end travel enabled
                        TARDISMessage.send(player, "ANCIENT", "End");
                    } else if (!TARDISPermission.hasPermission(player, "tardis.end")) {    // end permission
                        TARDISMessage.send(player, "NO_PERM_TRAVEL", "End");
                    } else if (plugin.getConfig().getBoolean("travel.allow_end_after_visit") && !new ResultSetTravelledTo(plugin).resultSet(player.getUniqueId().toString(), "THE_END")) { // check if they need to visit the end first
                        TARDISMessage.send(player, "TRAVEL_NOT_VISITED", "End");
                    } else if (level < the_end_min) {    // check if they have enough artron to travel to the end
                        TARDISMessage.send(player, "NOT_ENOUGH_TRAVEL_ENERGY", String.format("%d", the_end_min), "End");
                    } else {    // player can go to the end! yay
                        environment = "THE_END";
                    }
                }
                // create a random destination
                TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
                Location rand = tt.randomDestination(player, repeaters[1], repeaters[2], repeaters[3], dir, environment, rscl.getWorld(), false, cl);
                if (rand != null) {
                    // double check TARDIS travel is allowed in this world
                    if (!plugin.getPlanetsConfig().getBoolean("planets." + rand.getWorld().getName() + ".time_travel")) {
                        TARDISMessage.send(player, "NO_WORLD_TRAVEL");
                        return;
                    }
                    set.put("world", rand.getWorld().getName());
                    set.put("x", rand.getBlockX());
                    set.put("y", rand.getBlockY());
                    set.put("z", rand.getBlockZ());
                    set.put("direction", dir.toString());
                    set.put("submarine", (plugin.getTrackerKeeper().getSubmarine().contains(id)) ? 1 : 0);
                    plugin.getTrackerKeeper().getSubmarine().remove(id);
                    String worldname;
                    if (plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
                        worldname = plugin.getMVHelper().getAlias(rand.getWorld());
                    } else {
                        worldname = TARDISAliasResolver.getWorldAlias(rand.getWorld());
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
                        if (isTL) {
                            TARDISMessage.send(player, "DEST", dchat);
                        } else if (plugin.getServer().getPlayer(ownerUUID) != null) {
                            TARDISMessage.send(plugin.getServer().getPlayer(ownerUUID), "DEST", dchat);
                        }
                    }
                    HashMap<String, Object> wherel = new HashMap<>();
                    wherel.put("tardis_id", id);
                    plugin.getQueryFactory().doSyncUpdate("next", set, wherel);
                    plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(cost, TravelType.RANDOM));
                    plugin.getTrackerKeeper().getRescue().remove(id);
                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                        new TARDISLand(plugin, id, player).exitVortex();
                        plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.RANDOM, id));
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
