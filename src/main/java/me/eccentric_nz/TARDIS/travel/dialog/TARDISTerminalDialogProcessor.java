/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.travel.dialog;

import com.mojang.datafixers.util.Pair;
import io.papermc.paper.dialog.DialogResponseView;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.advanced.DamageUtility;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class TARDISTerminalDialogProcessor {

    private final TARDIS plugin;
    private final Player player;

    public TARDISTerminalDialogProcessor(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void process(DialogResponseView data) {
        if (data != null) {
            // {environment:"the_end",multiplier:8.0f,submarine:0b,x:200.0f,z:300.0f}
            String environment = data.getText("environment");
            Boolean submarine = data.getBoolean("submarine");
            Float x = data.getFloat("x");
            Float z = data.getFloat("z");
            Float multiplier = data.getFloat("multiplier");
            if (environment == null || submarine == null || x == null || z == null || multiplier == null) {
                plugin.getMessenger().message(player, "Could not read Terminal values!");
                return;
            }
            UUID uuid = player.getUniqueId();
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
            if (rst.resultSet()) {
                int id = rst.getTardis_id();
                ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
                rsc.resultSet();
                Location location = null;
                switch (environment) {
                    case "CURRENT" -> {
                        // add coords to current location
                        location = rsc.getCurrent().location().clone().add(x * multiplier, 0, z * multiplier);
                    }
                    case "NETHER" -> {
                        // get a nether world
                        if (plugin.getConfig().getBoolean("travel.nether") || !plugin.getConfig().getBoolean("travel.terminal.redefine")) {
                            World nether = getWorld("NETHER", player);
                            if (nether != null) {
                                location = new Location(nether, x * multiplier, 64, z * multiplier);
                            }
                        } else {
                            World alternate = plugin.getServer().getWorld(plugin.getConfig().getString("travel.terminal.nether", "world"));
                            if (alternate != null) {
                                location = new Location(alternate, x * multiplier, 64, z * multiplier);
                            }
                        }
                    }
                    case "THE_END" -> {
                        // get an end world
                        if (plugin.getConfig().getBoolean("travel.the_end") || !plugin.getConfig().getBoolean("travel.terminal.redefine")) {
                            World the_end = getWorld("THE_END", player);
                            if (the_end != null) {
                                location = new Location(the_end, x * multiplier, 64, z * multiplier);
                            }
                        } else {
                            World alternate = plugin.getServer().getWorld(plugin.getConfig().getString("travel.terminal.the_end", "world"));
                            if (alternate != null) {
                                location = new Location(alternate, x * multiplier, 64, z * multiplier);
                            }
                        }
                    }
                    // "NORMAL"
                    default -> {
                        // get an overworld
                        World normal = getWorld("NORMAL", player);
                        if (normal != null) {
                            location = new Location(normal, x * multiplier, 64, z * multiplier);
                        }
                    }
                }
                if (location == null) {
                    // message
                    plugin.getMessenger().send(player, "DEST_SET", !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                    return;
                }
                // slightly randomise x & z
                location.add(getRandomAddition(), 0, getRandomAddition());
                // get highest Y
                int y = location.getWorld().getHighestBlockYAt(location.getBlockX(), location.getBlockZ());
                location.setY(y);
                // check location
                Location finalLocation = location;
                // need to run on main thread
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    Pair<Boolean, Integer> safe = safe(finalLocation, rsc.getCurrent().direction(), submarine, player);
                    if (safe.getFirst()) {
                        HashMap<String, Object> set = new HashMap<>();
                        String ww = (!plugin.getPlanetsConfig().getBoolean("planets." + finalLocation.getWorld().getName() + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) ? plugin.getMVHelper().getWorld(finalLocation.getWorld().getName()).getName() : finalLocation.getWorld().getName();
                        set.put("world", ww);
                        set.put("x", finalLocation.getBlockX());
                        set.put("y", safe.getSecond());
                        set.put("z", finalLocation.getBlockZ());
                        set.put("direction", rsc.getCurrent().direction());
                        set.put("submarine", submarine ? 1 : 0);
                        HashMap<String, Object> wheret = new HashMap<>();
                        wheret.put("tardis_id", id);
                        plugin.getQueryFactory().doSyncUpdate("next", set, wheret);
                        plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), TravelType.TERMINAL));
                        plugin.getTrackerKeeper().getRescue().remove(id);
                        plugin.getMessenger().send(player, "DEST_SET", !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                            new TARDISLand(plugin, id, player).exitVortex();
                            plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.TERMINAL, id));
                        }
                        // damage the circuit if configured
                        DamageUtility.run(plugin, DiskCircuit.INPUT, id, player);
                        // message
                        plugin.getMessenger().send(player, "DEST_SET", !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                    }
                });
            }
        }
    }

    private double getRandomAddition() {
        double r = TARDISConstants.RANDOM.nextInt(50);
        if (TARDISConstants.RANDOM.nextBoolean()) {
            r = 0 - r;
        }
        return r;
    }

    private World getWorld(String environment, Player player) {
        List<World> allowedWorlds = new ArrayList<>();
        Set<String> worldlist = plugin.getPlanetsConfig().getConfigurationSection("planets").getKeys(false);
        for (String o : worldlist) {
            if (!plugin.getPlanetsConfig().getBoolean("planets." + o + ".time_travel")) {
                continue;
            }
            if (plugin.getConfig().getBoolean("travel.per_world_perms") && !TARDISPermission.hasPermission(player, "tardis.travel." + o)) {
                continue;
            }
            World ww = TARDISAliasResolver.getWorldFromAlias(o);
            if (ww != null) {
                String env = ww.getEnvironment().toString();
                if (env.equals(environment)) {
                    allowedWorlds.add(ww);
                }
            }
        }
        return !allowedWorlds.isEmpty() ?
                allowedWorlds.get(TARDISConstants.RANDOM.nextInt(allowedWorlds.size()))
                : null;
    }

    private Pair<Boolean, Integer> safe(Location location, COMPASS d, boolean submarine, Player p) {
        World world = location.getWorld();
        int blockX = location.getBlockX();
        int blockZ = location.getBlockZ();
        String loc_str = world.getName() + ":" + blockX + ":" + blockZ;
        TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
        switch (world.getEnvironment()) {
            case THE_END -> {
                int endy = TARDISStaticLocationGetters.getHighestYin3x3(world, blockX, blockZ);
                if (endy > 40 && Math.abs(blockX) > 9 && Math.abs(blockZ) > 9) {
                    Location loc = new Location(world, blockX, 0, blockZ);
                    int[] estart = TARDISTimeTravel.getStartLocation(loc, d);
                    int esafe = TARDISTimeTravel.safeLocation(estart[0], endy, estart[2], estart[1], estart[3], world, d);
                    if (esafe == 0) {
                        String save = world.getName() + ":" + blockX + ":" + endy + ":" + blockZ;
                        if (plugin.getPluginRespect().getRespect(new Location(world, blockX, endy, blockZ), new Parameters(p, Flag.getNoMessageFlags()))) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "DEST_SET_TERMINAL", save);
                            return new Pair<>(true, endy);
                        } else {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "DEST_PROTECTED", save);
                            return new Pair<>(false, -1);
                        }
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "DEST_NOT_SAFE", loc_str);
                        return new Pair<>(false, -1);
                    }
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "DEST_NOT_SAFE", loc_str);
                    return new Pair<>(false, -1);
                }
            }
            case NETHER -> {
                if (tt.safeNether(world, blockX, blockZ, d, p)) {
                    int nethery = plugin.getUtils().getHighestNetherBlock(world, blockX, blockZ);
                    String save = world.getName() + ":" + blockX + ":" + nethery + ":" + blockZ;
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "DEST_SET_TERMINAL", save);
                    return new Pair<>(true, nethery);
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "DEST_NOT_SAFE", loc_str);
                    return new Pair<>(false, -1);
                }
            }
            default -> {
                Location loc = new Location(world, blockX, 0, blockZ);
                int[] start = TARDISTimeTravel.getStartLocation(loc, d);
                int starty = TARDISStaticLocationGetters.getHighestYin3x3(world, blockX, blockZ);
                // allow room for under door block
                if (starty <= 0) {
                    starty = 1;
                }
                int safe;
                // check submarine
                loc.setY(starty);
                if (submarine && TARDISStaticUtils.isOceanBiome(loc.getBlock().getBiome())) {
                    Location subloc = tt.submarine(loc.getBlock(), d);
                    if (subloc != null) {
                        safe = 0;
                        starty = subloc.getBlockY();
                    } else {
                        safe = 1;
                    }
                } else {
                    safe = TARDISTimeTravel.safeLocation(start[0], starty, start[2], start[1], start[3], world, d);
                }
                if (safe == 0) {
                    String save = world.getName() + ":" + blockX + ":" + starty + ":" + blockZ;
                    if (plugin.getPluginRespect().getRespect(new Location(world, blockX, starty, blockZ), new Parameters(p, Flag.getNoMessageFlags()))) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "DEST_SET_TERMINAL", save);
                        return new Pair<>(true, starty);
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "DEST_PROTECTED", save);
                        return new Pair<>(false, -1);
                    }
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "DEST_NOT_SAFE", loc_str);
                    return new Pair<>(false, -1);
                }
            }
        }
    }
}
