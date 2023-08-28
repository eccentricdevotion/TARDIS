/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author eccentric_nz
 */
public class TARDISTravelCoords {

    private final TARDIS plugin;

    public TARDISTravelCoords(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean action(Player player, String[] args, int id) {
        HashMap<String, Object> set = new HashMap<>();
        HashMap<String, Object> tid = new HashMap<>();
        tid.put("tardis_id", id);
        switch (args.length) {
            case 2 -> {
                if (args[0].equalsIgnoreCase("random")) {
                    // check world is an actual world
                    World world = TARDISAliasResolver.getWorldFromAlias(args[1]);
                    if (world == null) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "COULD_NOT_FIND_WORLD");
                        return true;
                    }
                    // check world is enabled for travel
                    if (!plugin.getPlanetsConfig().getBoolean("planets." + world.getName() + ".time_travel")) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_WORLD_TRAVEL");
                        return true;
                    }
                    // only world specified
                    List<String> worlds = Collections.singletonList(world.getName());
                    // get current location
                    HashMap<String, Object> wherec = new HashMap<>();
                    wherec.put("tardis_id", id);
                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
                    if (rsc.resultSet()) {
                        Parameters parameters = new Parameters(player, Flag.getNoMessageFlags());
                        parameters.setCompass(rsc.getDirection());
                        Location l = plugin.getTardisAPI().getRandomLocation(worlds, world.getEnvironment(), parameters);
                        if (l != null) {
                            set.put("world", l.getWorld().getName());
                            set.put("x", l.getBlockX());
                            set.put("y", l.getBlockY());
                            set.put("z", l.getBlockZ());
                            set.put("submarine", 0);
                            plugin.getQueryFactory().doSyncUpdate("next", set, tid);
                            plugin.getMessenger().send(player, "LOC_SAVED", true);
                            plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), TravelType.RANDOM));
                            plugin.getTrackerKeeper().getRescue().remove(id);
                            if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                new TARDISLand(plugin, id, player).exitVortex();
                                plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.RANDOM, id));
                            }
                            return true;
                        }
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
                        return true;
                    }
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_COORDS");
                    return false;
                }
            }
            case 3 -> {
                if (args[0].startsWith("~")) {
                    HashMap<String, Object> wherecl = new HashMap<>();
                    wherecl.put("tardis_id", id);
                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                    if (!rsc.resultSet()) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
                        return true;
                    }
                    if (!plugin.getPlanetsConfig().getBoolean("planets." + rsc.getWorld().getName() + ".time_travel")) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_WORLD_TRAVEL");
                        return true;
                    }
                    if (rsc.isSubmarine()) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "SUB_NO_CMD");
                        return true;
                    }
                    // check args
                    int rx = getRelativeCoordinate(args[0]);
                    int ry = getRelativeCoordinate(args[1]);
                    int rz = getRelativeCoordinate(args[2]);
                    if (rx == Integer.MAX_VALUE || ry == Integer.MAX_VALUE || rz == Integer.MAX_VALUE) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "RELATIVE_NOT_FOUND");
                        return true;
                    }
                    // add relative coordinates
                    int x = rsc.getX() + rx;
                    int y = rsc.getY() + ry;
                    int z = rsc.getZ() + rz;
                    World.Environment environment = player.getWorld().getEnvironment();
                    if (y < -64 || ((environment.equals(World.Environment.NORMAL) && y > 310) || (!environment.equals(World.Environment.NORMAL) && y > 240))) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "Y_NOT_VALID");
                        return true;
                    }
                    // make location
                    Location location = new Location(rsc.getWorld(), x, y, z);
                    // check location
                    int count = checkLocation(location, player, id);
                    if (count > 0) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_SAFE");
                    } else {
                        set.put("world", location.getWorld().getName());
                        set.put("x", location.getBlockX());
                        set.put("y", location.getBlockY());
                        set.put("z", location.getBlockZ());
                        set.put("submarine", 0);
                        plugin.getQueryFactory().doSyncUpdate("next", set, tid);
                        plugin.getMessenger().send(player, "LOC_SAVED", true);
                        plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), TravelType.RELATIVE_COORDINATES));
                        plugin.getTrackerKeeper().getRescue().remove(id);
                        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                            new TARDISLand(plugin, id, player).exitVortex();
                            plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.RELATIVE_COORDINATES, id));
                        }
                    }
                    return true;
                } else {
                    // automatically get highest block Y coord
                    Location determiney = getCoordinateLocation(args, player, id);
                    if (determiney != null) {
                        int count = checkLocation(determiney, player, id);
                        if (count > 0) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_SAFE");
                        } else {
                            set.put("world", determiney.getWorld().getName());
                            set.put("x", determiney.getBlockX());
                            set.put("y", determiney.getBlockY());
                            set.put("z", determiney.getBlockZ());
                            set.put("submarine", 0);
                            plugin.getQueryFactory().doSyncUpdate("next", set, tid);
                            plugin.getMessenger().send(player, "LOC_SAVED", true);
                            plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), TravelType.COORDINATES));
                            plugin.getTrackerKeeper().getRescue().remove(id);
                            if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                new TARDISLand(plugin, id, player).exitVortex();
                                plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.COORDINATES, id));
                            }
                        }
                        return true;
                    }
                }
            }
            default -> {
                // coords
                Location giveny = getCoordinateLocation(args, player, id);
                if (giveny != null) {
                    // check location
                    int count = checkLocation(giveny, player, id);
                    if (count > 0) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_SAFE");
                    } else {
                        set.put("world", giveny.getWorld().getName());
                        set.put("x", giveny.getBlockX());
                        set.put("y", giveny.getBlockY());
                        set.put("z", giveny.getBlockZ());
                        set.put("submarine", 0);
                        plugin.getQueryFactory().doSyncUpdate("next", set, tid);
                        plugin.getMessenger().send(player, "LOC_SAVED", true);
                        plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), TravelType.COORDINATES));
                        plugin.getTrackerKeeper().getRescue().remove(id);
                        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                            new TARDISLand(plugin, id, player).exitVortex();
                            plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.COORDINATES, id));
                        }
                    }
                    return true;
                }
            }
        }
        return true;
    }

    private int getRelativeCoordinate(String arg) {
        if (arg.startsWith("~")) {
            String value = arg.substring(1);
            if (value.isEmpty()) {
                return 0;
            }
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException nfe) {
                plugin.debug("Could not convert relative coordinate! " + nfe.getMessage());
                return Integer.MAX_VALUE;
            }
        }
        return Integer.MAX_VALUE;
    }

    private int checkLocation(Location location, Player player, int id) {
        if (location.getWorld().getEnvironment().equals(World.Environment.NETHER) && location.getY() > 127) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_NETHER");
            return 1;
        }
        if (plugin.getTardisArea().isInExistingArea(location)) {
            plugin.getMessenger().sendColouredCommand(player, "TRAVEL_IN_AREA", "/tardistravel area [area name]", plugin);
            return 1;
        }
        if (!plugin.getPluginRespect().getRespect(location, new Parameters(player, Flag.getDefaultFlags()))) {
            return 1;
        }
        HashMap<String, Object> wherecl = new HashMap<>();
        wherecl.put("tardis_id", id);
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
        if (!rsc.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
            return 1;
        }
        // check location
        int[] start_loc = TARDISTimeTravel.getStartLocation(location, rsc.getDirection());
        return TARDISTimeTravel.safeLocation(start_loc[0], location.getBlockY(), start_loc[2], start_loc[1], start_loc[3], location.getWorld(), rsc.getDirection());
    }

    private Location getCoordinateLocation(String[] args, Player player, int id) {
        // coords
        String w_str = args[0];
        if (w_str.contains("'")) {
            w_str = TARDISStringUtils.getQuotedString(args);
        }
        if (TARDISNumberParsers.isSimpleNumber(args[0])) {
            plugin.getMessenger().sendColouredCommand(player, "WORLD_NOT_FOUND", "/tardisworld", plugin);
            return null;
        }
        // must be at least a world name/~ and x, z
        if (args.length < 3) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_COORDS");
            return null;
        }
        if (args[1].startsWith("~")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_WORLD_RELATIVE");
            return null;
        }
        // must be a location then
        int x, y, z;
        World w;
        if (args[0].equals("~")) {
            HashMap<String, Object> wherecl = new HashMap<>();
            wherecl.put("tardis_id", id);
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
            if (!rsc.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
                return null;
            }
            w = rsc.getWorld();
        } else {
            if (!plugin.getPlanetsConfig().getBoolean("planets." + w_str + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
                w = plugin.getMVHelper().getWorld(w_str);
            } else {
                w = TARDISAliasResolver.getWorldFromAlias(w_str);
            }
        }
        if (w == null) {
            plugin.getMessenger().sendColouredCommand(player, "WORLD_NOT_FOUND", "/tardisworld", plugin);
            return null;
        }
        if (!plugin.getPlanetsConfig().getBoolean("planets." + w.getName() + ".time_travel")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_WORLD_TRAVEL");
            return null;
        }
        if (!plugin.getConfig().getBoolean("travel.include_default_world") && plugin.getConfig().getBoolean("creation.default_world") && args[0].equals(plugin.getConfig().getString("creation.default_world_name"))) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_WORLD_TRAVEL");
            return null;
        }
        z = TARDISNumberParsers.parseInt(args[args.length - 1]);
        if (args.length > 3) {
            x = TARDISNumberParsers.parseInt(args[args.length - 3]);
            y = TARDISNumberParsers.parseInt(args[args.length - 2]);
            // y values are now lesser and greater in 1.18+, but normal worlds have higher altitude
            if (y < -64 || ((w.getEnvironment().equals(World.Environment.NORMAL) && y > 310) || (!w.getEnvironment().equals(World.Environment.NORMAL) && y > 240))) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "Y_NOT_VALID");
                return null;
            }
        } else {
            x = TARDISNumberParsers.parseInt(args[args.length - 2]);
            Chunk chunk = w.getChunkAt(x, z);
            while (!chunk.isLoaded()) {
                chunk.load();
            }
            if (w.getEnvironment().equals(World.Environment.NETHER)) {
                y = TARDISStaticLocationGetters.getNetherHighest(new Location(w, x, 240, z));
            } else {
                y = TARDISStaticLocationGetters.getHighestYin3x3(w, x, z);
            }
        }
        int max = Math.min(plugin.getConfig().getInt("travel.max_distance"), (int) (w.getWorldBorder().getSize() / 2) - 17);
        if (x > max || x < -max || z > max || z < -max) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "XZ_NOT_VALID");
            return null;
        }
        return new Location(w, x, y, z);
    }
}
