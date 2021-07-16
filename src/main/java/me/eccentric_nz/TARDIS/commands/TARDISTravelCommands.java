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
package me.eccentric_nz.TARDIS.commands;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISSerializeInventory;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.listeners.TARDISBiomeReaderListener;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.travel.*;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISWorldBorderChecker;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Command /tardistravel [arguments].
 * <p>
 * Time travel is the process of travelling through time, even in a non-linear direction.
 *
 * @author eccentric_nz
 */
public class TARDISTravelCommands implements CommandExecutor {

    private final TARDIS plugin;
    private final List<String> BIOME_SUBS = new ArrayList<>();
    private final List<String> mustUseAdvanced = Arrays.asList("area", "biome", "dest");
    private final List<String> costs = Arrays.asList("random", "random_circuit", "travel", "comehere", "hide", "rebuild", "autonomous", "backdoor");

    public TARDISTravelCommands(TARDIS plugin) {
        this.plugin = plugin;
        for (Biome bi : Biome.values()) {
            if (!bi.equals(Biome.THE_VOID)) {
                BIOME_SUBS.add(bi.toString());
            }
        }
    }

    private static boolean isNumber(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        // If the player typed /tardistravel then do the following...
        // check there is the right number of arguments
        if (cmd.getName().equalsIgnoreCase("tardistravel")) {
            if (player == null) {
                TARDISMessage.send(sender, "CMD_PLAYER");
                return true;
            }
            if (TARDISPermission.hasPermission(player, "tardis.timetravel")) {
                if (args.length < 1) {
                    new TARDISCommandHelper(plugin).getCommand("tardistravel", sender);
                    return true;
                }
                // get tardis data
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", player.getUniqueId().toString());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
                if (!rs.resultSet()) {
                    TARDISMessage.send(player, "NO_TARDIS");
                    return true;
                }
                Tardis tardis = rs.getTardis();
                int id = tardis.getTardis_id();
                if (args[0].equalsIgnoreCase("cancel")) {
                    if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                        plugin.getTrackerKeeper().getHasDestination().remove(id);
                        TARDISMessage.send(player, "TRAVEL_CANCEL");
                    } else {
                        TARDISMessage.send(player, "TRAVEL_NEED_DEST");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("costs")) {
                    TARDISMessage.send(player, "TRAVEL_COSTS");
                    for (String s : costs) {
                        String c = (s.equals("rebuild")) ? plugin.getArtronConfig().getString("random") : plugin.getArtronConfig().getString(s);
                        TARDISMessage.message(player, "    " + s + ": " + ChatColor.AQUA + c);
                    }
                    return true;
                }
                if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                    TARDISMessage.send(player, "SIEGE_NO_CMD");
                    return true;
                }
                HashMap<String, Object> tid = new HashMap<>();
                tid.put("tardis_id", id);
                HashMap<String, Object> set = new HashMap<>();
                if (args.length == 1 && args[0].equalsIgnoreCase("stop")) {
                    // remove trackers
                    plugin.getTrackerKeeper().getMaterialising().removeAll(Collections.singleton(id));
                    plugin.getTrackerKeeper().getInVortex().removeAll(Collections.singleton(id));
                    plugin.getTrackerKeeper().getDamage().remove(id);
                    plugin.getTrackerKeeper().getMalfunction().remove(id);
                    if (plugin.getTrackerKeeper().getDidDematToVortex().contains(id)) {
                        plugin.getTrackerKeeper().getDidDematToVortex().removeAll(Collections.singleton(id));
                    }
                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                        int taskID = plugin.getTrackerKeeper().getDestinationVortex().get(id);
                        plugin.getServer().getScheduler().cancelTask(taskID);
                        plugin.getTrackerKeeper().getDestinationVortex().remove(id);
                    }
                    // get home location
                    HashMap<String, Object> wherehl = new HashMap<>();
                    wherehl.put("tardis_id", id);
                    ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, wherehl);
                    if (!rsh.resultSet()) {
                        TARDISMessage.send(player, "HOME_NOT_FOUND");
                        return true;
                    }
                    // update current, next and back tables
                    HashMap<String, Object> setlocs = new HashMap<>();
                    setlocs.put("world", rsh.getWorld().getName());
                    setlocs.put("x", rsh.getX());
                    setlocs.put("y", rsh.getY());
                    setlocs.put("z", rsh.getZ());
                    setlocs.put("direction", rsh.getDirection().toString());
                    setlocs.put("submarine", (rsh.isSubmarine()) ? 1 : 0);
                    Location l = new Location(rsh.getWorld(), rsh.getX(), rsh.getY(), rsh.getZ());
                    plugin.getQueryFactory().updateLocations(setlocs, id);
                    // rebuild the exterior
                    BuildData bd = new BuildData(player.getUniqueId().toString());
                    bd.setDirection(rsh.getDirection());
                    bd.setLocation(l);
                    bd.setMalfunction(false);
                    bd.setOutside(true);
                    bd.setPlayer(player);
                    bd.setRebuild(true);
                    bd.setSubmarine(rsh.isSubmarine());
                    bd.setTardisID(id);
                    bd.setThrottle(SpaceTimeThrottle.REBUILD);
                    if (!rsh.getPreset().isEmpty()) {
                        // set the chameleon preset
                        HashMap<String, Object> wherep = new HashMap<>();
                        wherep.put("tardis_id", id);
                        HashMap<String, Object> setp = new HashMap<>();
                        setp.put("chameleon_preset", rsh.getPreset());
                        // set chameleon adaption to OFF
                        setp.put("adapti_on", 0);
                        plugin.getQueryFactory().doSyncUpdate("tardis", setp, wherep);
                    }
                    plugin.getPresetBuilder().buildPreset(bd);
                    plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.STOP, id));
                    return true;
                }
                int level = tardis.getArtron_level();
                boolean powered = tardis.isPowered_on();
                if (!tardis.isHandbrake_on() && !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                    TARDISMessage.send(player, "NOT_WHILE_TRAVELLING");
                    return true;
                }
                HashMap<String, Object> wheret = new HashMap<>();
                wheret.put("uuid", player.getUniqueId().toString());
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
                if (!rst.resultSet()) {
                    TARDISMessage.send(player, "NOT_IN_TARDIS");
                    return true;
                }
                int tardis_id = rst.getTardis_id();
                if (tardis_id != id) {
                    TARDISMessage.send(player, "CMD_ONLY_TL");
                    return true;
                }
                if (plugin.getConfig().getBoolean("allow.power_down") && !powered) {
                    TARDISMessage.send(player, "POWER_DOWN");
                    return true;
                }
                int travel = plugin.getArtronConfig().getInt("travel");
                if (level < travel) {
                    TARDISMessage.send(player, "NOT_ENOUGH_ENERGY");
                    return true;
                }
                if (TARDISPermission.hasPermission(player, "tardis.exile") && plugin.getConfig().getBoolean("travel.exile")) {
                    // get the exile area
                    String permArea = plugin.getTardisArea().getExileArea(player);
                    TARDISMessage.send(player, "EXILE", permArea);
                    Location l = plugin.getTardisArea().getNextSpot(permArea);
                    if (l == null) {
                        TARDISMessage.send(player, "NO_MORE_SPOTS");
                        return true;
                    }
                    set.put("world", l.getWorld().getName());
                    set.put("x", l.getBlockX());
                    set.put("y", l.getBlockY());
                    set.put("z", l.getBlockZ());
                    set.put("submarine", 0);
                    plugin.getQueryFactory().doSyncUpdate("next", set, tid);
                    TARDISMessage.send(player, "TRAVEL_APPROVED", permArea);
                    plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(travel, TravelType.EXILE));
                    plugin.getTrackerKeeper().getRescue().remove(id);
                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                        new TARDISLand(plugin, id, player).exitVortex();
                        plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.EXILE, id));
                    }
                    return true;
                } else {
                    if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("cancel")) {
                            if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                                plugin.getTrackerKeeper().getHasDestination().remove(id);
                                TARDISMessage.send(player, "TRAVEL_CANCEL");
                            } else {
                                TARDISMessage.send(player, "TRAVEL_NEED_DEST");
                            }
                            return true;
                        }
                        if (args[0].equalsIgnoreCase("costs")) {
                            TARDISMessage.send(player, "TRAVEL_COSTS");
                            for (String s : costs) {
                                String c = (s.equals("rebuild")) ? plugin.getArtronConfig().getString("random") : plugin.getArtronConfig().getString(s);
                                TARDISMessage.message(player, "    " + s + ": " + ChatColor.AQUA + c);
                            }
                            return true;
                        }
                        // we're thinking this is a player's name or home / back / cave
                        if (args[0].equalsIgnoreCase("home") || args[0].equalsIgnoreCase("back") || args[0].equalsIgnoreCase("cave") || args[0].equalsIgnoreCase("village")) {
                            String which;
                            TravelType travelType;
                            if (args[0].equalsIgnoreCase("home")) {
                                travelType = TravelType.HOME;
                                // get home location
                                HashMap<String, Object> wherehl = new HashMap<>();
                                wherehl.put("tardis_id", id);
                                ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, wherehl);
                                if (!rsh.resultSet()) {
                                    TARDISMessage.send(player, "HOME_NOT_FOUND");
                                    return true;
                                }
                                set.put("world", rsh.getWorld().getName());
                                set.put("x", rsh.getX());
                                set.put("y", rsh.getY());
                                set.put("z", rsh.getZ());
                                set.put("direction", rsh.getDirection().toString());
                                set.put("submarine", (rsh.isSubmarine()) ? 1 : 0);
                                which = "Home";
                                if (!rsh.getPreset().isEmpty()) {
                                    // set the chameleon preset
                                    HashMap<String, Object> wherep = new HashMap<>();
                                    wherep.put("tardis_id", id);
                                    HashMap<String, Object> setp = new HashMap<>();
                                    setp.put("chameleon_preset", rsh.getPreset());
                                    // set chameleon adaption to OFF
                                    setp.put("adapti_on", 0);
                                    plugin.getQueryFactory().doSyncUpdate("tardis", setp, wherep);
                                }
                            } else if (args[0].equalsIgnoreCase("back")) {
                                travelType = TravelType.BACK;
                                // get fast return location
                                HashMap<String, Object> wherebl = new HashMap<>();
                                wherebl.put("tardis_id", id);
                                ResultSetBackLocation rsb = new ResultSetBackLocation(plugin, wherebl);
                                if (!rsb.resultSet()) {
                                    TARDISMessage.send(player, "PREV_NOT_FOUND");
                                    return true;
                                }
                                set.put("world", rsb.getWorld().getName());
                                set.put("x", rsb.getX());
                                set.put("y", rsb.getY());
                                set.put("z", rsb.getZ());
                                set.put("direction", rsb.getDirection().toString());
                                set.put("submarine", (rsb.isSubmarine()) ? 1 : 0);
                                which = "Fast Return to " + ChatColor.GREEN + "(" + rsb.getWorld().getName() + ":" + rsb.getX() + ":" + rsb.getY() + ":" + rsb.getZ() + ")" + ChatColor.RESET;
                            } else if (args[0].equalsIgnoreCase("cave")) {
                                travelType = TravelType.CAVE;
                                if (!TARDISPermission.hasPermission(player, "tardis.timetravel.cave")) {
                                    TARDISMessage.send(player, "TRAVEL_NO_PERM_CAVE");
                                    return true;
                                }
                                // find a cave
                                Location cave = new TARDISCaveFinder(plugin).searchCave(player, id);
                                if (cave == null) {
                                    TARDISMessage.send(player, "CAVE_NOT_FOUND");
                                    return true;
                                }
                                // check respect
                                if (!plugin.getPluginRespect().getRespect(cave, new Parameters(player, Flag.getDefaultFlags()))) {
                                    if (plugin.getConfig().getBoolean("travel.no_destination_malfunctions")) {
                                        plugin.getTrackerKeeper().getMalfunction().put(id, true);
                                    } else {
                                        return true;
                                    }
                                }
                                set.put("world", cave.getWorld().getName());
                                set.put("x", cave.getBlockX());
                                set.put("y", cave.getBlockY());
                                set.put("z", cave.getBlockZ());
                                set.put("submarine", 0);
                                which = "Cave";
                            } else {
                                if (!plugin.getConfig().getBoolean("allow.village_travel")) {
                                    TARDISMessage.send(player, "TRAVEL_NO_VILLAGE");
                                    return true;
                                }
                                if (!TARDISPermission.hasPermission(player, "tardis.timetravel.village")) {
                                    TARDISMessage.send(player, "TRAVEL_NO_PERM_VILLAGE");
                                    return true;
                                }
                                // find a village / nether fortress / end city
                                Location village = new TARDISVillageTravel(plugin).getRandomVillage(player, id);
                                if (village == null) {
                                    TARDISMessage.send(player, "VILLAGE_NOT_FOUND");
                                    return true;
                                }
                                // check respect
                                if (!plugin.getPluginRespect().getRespect(village, new Parameters(player, Flag.getDefaultFlags()))) {
                                    if (plugin.getConfig().getBoolean("travel.no_destination_malfunctions")) {
                                        plugin.getTrackerKeeper().getMalfunction().put(id, true);
                                    } else {
                                        return true;
                                    }
                                }
                                set.put("world", village.getWorld().getName());
                                set.put("x", village.getBlockX());
                                set.put("y", village.getBlockY());
                                set.put("z", village.getBlockZ());
                                set.put("submarine", 0);
                                switch (village.getWorld().getEnvironment()) {
                                    case THE_END:
                                        which = "End City";
                                        travelType = TravelType.VILLAGE_THE_END;
                                        break;
                                    case NETHER:
                                        which = "Nether Fortress";
                                        travelType = TravelType.VILLAGE_NETHER;
                                        break;
                                    default:
                                        which = "Village";
                                        travelType = TravelType.VILLAGE_OVERWORLD;
                                        break;
                                }
                            }
                            plugin.getQueryFactory().doSyncUpdate("next", set, tid);
                            TARDISMessage.send(player, "TRAVEL_LOADED", which, !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                            plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(travel, travelType));
                            plugin.getTrackerKeeper().getRescue().remove(id);
                            if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                new TARDISLand(plugin, id, player).exitVortex();
                                plugin.getPM().callEvent(new TARDISTravelEvent(player, null, travelType, id));
                            }
                            return true;
                        } else {
                            return travelPlayer(player, args[0], id);
                        }
                    }
                    if (args.length == 2 && args[0].equalsIgnoreCase("player")) {
                        return travelPlayer(player, args[1], id);
                    }
                    if (args.length == 2 && (args[1].equals("?") || args[1].equalsIgnoreCase("tpa"))) {
                        if (!TARDISPermission.hasPermission(player, "tardis.timetravel.player")) {
                            TARDISMessage.send(player, "NO_PERM_PLAYER");
                            return true;
                        }
                        Player requested = plugin.getServer().getPlayer(args[0]);
                        if (requested == null) {
                            TARDISMessage.send(player, "NOT_ONLINE");
                            return true;
                        }
                        // check the to player's DND status
                        ResultSetPlayerPrefs rspp = new ResultSetPlayerPrefs(plugin, requested.getUniqueId().toString());
                        if (rspp.resultSet() && rspp.isDND()) {
                            TARDISMessage.send(player, "DND", args[0]);
                            return true;
                        }
                        // check the location
                        TARDISTravelRequest ttr = new TARDISTravelRequest(plugin);
                        if (!ttr.getRequest(player, requested, requested.getLocation())) {
                            return true;
                        }
                        // ask if we can travel to this player
                        UUID requestedUUID = requested.getUniqueId();
                        TARDISMessage.send(requested, "REQUEST_TRAVEL", player.getName(), ChatColor.AQUA + "tardis request accept" + ChatColor.RESET);
                        plugin.getTrackerKeeper().getChat().put(requestedUUID, player.getUniqueId());
                        Player p = player;
                        String to = args[0];
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            if (plugin.getTrackerKeeper().getChat().containsKey(requestedUUID)) {
                                plugin.getTrackerKeeper().getChat().remove(requestedUUID);
                                TARDISMessage.send(p, "REQUEST_NO_RESPONSE", to);
                            }
                        }, 1200L);
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("biome")) {
                        // we're thinking this is a biome search
                        if (!TARDISPermission.hasPermission(player, "tardis.timetravel.biome")) {
                            TARDISMessage.send(player, "TRAVEL_NO_PERM_BIOME");
                            return true;
                        }
                        String upper = args[1].toUpperCase(Locale.ENGLISH);
                        if (!plugin.getDifficulty().equals(Difficulty.EASY) && mustUseAdvanced.contains(args[0].toLowerCase(Locale.ENGLISH)) && !plugin.getUtils().inGracePeriod(player, false) && !upper.equals("LIST")) {
                            if (plugin.getDifficulty().equals(Difficulty.MEDIUM)) {
                                // check they have a biome disk in storage
                                boolean hasBiomeDisk = false;
                                UUID uuid = player.getUniqueId();
                                HashMap<String, Object> whereb = new HashMap<>();
                                whereb.put("uuid", uuid.toString());
                                ResultSetDiskStorage rsb = new ResultSetDiskStorage(plugin, whereb);
                                if (rsb.resultSet()) {
                                    try {
                                        ItemStack[] disks1 = TARDISSerializeInventory.itemStacksFromString(rsb.getBiomesOne());
                                        if (TARDISBiomeReaderListener.hasBiomeDisk(disks1, upper)) {
                                            hasBiomeDisk = true;
                                        } else {
                                            ItemStack[] disks2 = TARDISSerializeInventory.itemStacksFromString(rsb.getBiomesTwo());
                                            if (TARDISBiomeReaderListener.hasBiomeDisk(disks2, upper)) {
                                                hasBiomeDisk = true;
                                            }
                                        }
                                    } catch (IOException ex) {
                                        plugin.debug("Could not serialize inventory!");
                                    }
                                }
                                if (!hasBiomeDisk) {
                                    TARDISMessage.send(player, "BIOME_DISK_NOT_FOUND");
                                    return true;
                                }
                            } else {
                                TARDISMessage.send(player, "ADV_BIOME");
                                return true;
                            }
                        }
                        if (upper.equals("LIST")) {
                            StringBuilder buf = new StringBuilder();
                            BIOME_SUBS.forEach((bi) -> buf.append(bi).append(", "));
                            String b = buf.substring(0, buf.length() - 2);
                            TARDISMessage.send(player, "BIOMES", b);
                        } else {
                            try {
                                Biome biome = Biome.valueOf(upper);
                                if (biome.equals(Biome.THE_VOID)) {
                                    TARDISMessage.send(player, "BIOME_TRAVEL_NOT_VALID");
                                    return true;
                                }
                                TARDISMessage.send(player, "BIOME_SEARCH");
                                World w;
                                HashMap<String, Object> wherecl = new HashMap<>();
                                wherecl.put("tardis_id", id);
                                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                                if (!rsc.resultSet()) {
                                    TARDISMessage.send(player, "CURRENT_NOT_FOUND");
                                    return true;
                                }
                                // have they specified a world argument?
                                if (args.length > 2) {
                                    // must be in the vortex
                                    if (!plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                        TARDISMessage.send(player, "BIOME_FROM_VORTEX");
                                        return true;
                                    }
                                    String planet = args[2].toLowerCase(Locale.ROOT);
                                    if (planet.endsWith("gallifrey") || planet.endsWith("siluria") || planet.endsWith("skaro")) {
                                        TARDISMessage.send(player, "BIOME_NOT_PLANET", args[2]);
                                        return true;
                                    }
                                    // get the world
                                    w = TARDISAliasResolver.getWorldFromAlias(args[2]);
                                    if (w == null) {
                                        TARDISMessage.send(player, "WORLD_DELETED", args[2]);
                                        return true;
                                    }
                                } else {
                                    String planet = rsc.getWorld().getName().toLowerCase(Locale.ROOT);
                                    if (planet.endsWith("gallifrey") || planet.endsWith("siluria") || planet.endsWith("skaro")) {
                                        TARDISMessage.send(player, "BIOME_NOT_PLANET", rsc.getWorld().getName());
                                        return true;
                                    }
                                    w = rsc.getWorld();
                                }
                                Location current = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                                new TARDISBiomeFinder(plugin).run(w, biome, player, id, rsc.getDirection(), current);
                            } catch (IllegalArgumentException iae) {
                                TARDISMessage.send(player, "BIOME_NOT_VALID");
                                return true;
                            }
                        }
                        return true;
                    }
                    if (args.length == 2 && (args[0].equalsIgnoreCase("dest") || args[0].equalsIgnoreCase("save"))) {
                        // we're thinking this is a saved destination name
                        if (TARDISPermission.hasPermission(player, "tardis.save")) {
                            HashMap<String, Object> whered = new HashMap<>();
                            whered.put("dest_name", args[1]);
                            whered.put("tardis_id", id);
                            ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
                            if (!rsd.resultSet()) {
                                TARDISMessage.send(player, "SAVE_NOT_FOUND");
                                return true;
                            }
                            World w = TARDISAliasResolver.getWorldFromAlias(rsd.getWorld());
                            if (w != null) {
                                if (w.getName().startsWith("TARDIS_")) {
                                    TARDISMessage.send(player, "SAVE_NO_TARDIS");
                                    return true;
                                }
                                Location save_dest = new Location(w, rsd.getX(), rsd.getY(), rsd.getZ());
                                if (!plugin.getPluginRespect().getRespect(save_dest, new Parameters(player, Flag.getDefaultFlags()))) {
                                    if (plugin.getConfig().getBoolean("travel.no_destination_malfunctions")) {
                                        plugin.getTrackerKeeper().getMalfunction().put(id, true);
                                    } else {
                                        return true;
                                    }
                                }
                                TARDISAreaCheck tac = plugin.getTardisArea().areaCheckInExistingArea(save_dest);
                                if (tac.isInArea()) {
                                    // save is in a TARDIS area, so check that the spot is not occupied
                                    HashMap<String, Object> wheres = new HashMap<>();
                                    wheres.put("world", rsd.getWorld());
                                    wheres.put("x", rsd.getX());
                                    wheres.put("y", rsd.getY());
                                    wheres.put("z", rsd.getZ());
                                    ResultSetCurrentLocation rsz = new ResultSetCurrentLocation(plugin, wheres);
                                    if (rsz.resultSet()) {
                                        TARDISMessage.send(player, "TARDIS_IN_SPOT", ChatColor.AQUA + "/tardistravel area [name]" + ChatColor.RESET);
                                        return true;
                                    }
                                    String invisibility = tac.getArea().getInvisibility();
                                    if (invisibility.equals("DENY") && tardis.getPreset().equals(PRESET.INVISIBLE)) {
                                        // check preset
                                        TARDISMessage.send(player, "AREA_NO_INVISIBLE");
                                        return true;
                                    } else if (!invisibility.equals("ALLOW")) {
                                        // force preset
                                        TARDISMessage.send(player, "AREA_FORCE_PRESET", invisibility);
                                        HashMap<String, Object> wherei = new HashMap<>();
                                        wherei.put("tardis_id", id);
                                        HashMap<String, Object> seti = new HashMap<>();
                                        seti.put("chameleon_preset", invisibility);
                                        // set chameleon adaption to OFF
                                        seti.put("adapti_on", 0);
                                        plugin.getQueryFactory().doSyncUpdate("tardis", seti, wherei);
                                    }
                                }
                                set.put("world", rsd.getWorld());
                                set.put("x", rsd.getX());
                                set.put("y", rsd.getY());
                                set.put("z", rsd.getZ());
                                if (!rsd.getDirection().isEmpty() && rsd.getDirection().length() < 6) {
                                    set.put("direction", rsd.getDirection());
                                } else {
                                    // get current direction
                                    HashMap<String, Object> wherecl = new HashMap<>();
                                    wherecl.put("tardis_id", id);
                                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                                    if (!rsc.resultSet()) {
                                        TARDISMessage.send(player, "CURRENT_NOT_FOUND");
                                        return true;
                                    }
                                    set.put("direction", rsc.getDirection().toString());
                                }
                                set.put("submarine", (rsd.isSubmarine()) ? 1 : 0);
                                if (!rsd.getPreset().isEmpty()) {
                                    // set the chameleon preset
                                    HashMap<String, Object> seti = new HashMap<>();
                                    seti.put("chameleon_preset", rsd.getPreset());
                                    // set chameleon adaption to OFF
                                    seti.put("adapti_on", 0);
                                    HashMap<String, Object> wherei = new HashMap<>();
                                    wherei.put("tardis_id", id);
                                    plugin.getQueryFactory().doSyncUpdate("tardis", seti, wherei);
                                }
                                plugin.getQueryFactory().doSyncUpdate("next", set, tid);
                                TARDISMessage.send(player, "LOC_SET", !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                                plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(travel, TravelType.SAVE));
                                plugin.getTrackerKeeper().getRescue().remove(id);
                                if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                    new TARDISLand(plugin, id, player).exitVortex();
                                    plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.SAVE, id));
                                }
                            } else {
                                TARDISMessage.send(player, "SAVE_NO_WORLD");
                            }
                            return true;
                        } else {
                            TARDISMessage.send(player, "TRAVEL_NO_PERM_SAVE");
                            return true;
                        }
                    }
                    if (args.length == 2 && args[0].equalsIgnoreCase("area")) {
                        // we're thinking this is admin defined area name
                        HashMap<String, Object> wherea = new HashMap<>();
                        wherea.put("area_name", args[1]);
                        ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false, false);
                        if (!rsa.resultSet()) {
                            TARDISMessage.send(player, "AREA_NOT_FOUND", ChatColor.GREEN + "/tardis list areas" + ChatColor.RESET);
                            return true;
                        }
                        if ((!TARDISPermission.hasPermission(player, "tardis.area." + args[1]) && !TARDISPermission.hasPermission(player, "tardis.area.*")) || (!player.isPermissionSet("tardis.area." + args[1]) && !player.isPermissionSet("tardis.area.*"))) {
                            TARDISMessage.send(player, "TRAVEL_NO_AREA_PERM", args[1]);
                            return true;
                        }
                        if (!plugin.getDifficulty().equals(Difficulty.EASY) && mustUseAdvanced.contains(args[0].toLowerCase(Locale.ENGLISH)) && !plugin.getUtils().inGracePeriod(player, false)) {
                            TARDISMessage.send(player, "ADV_AREA");
                            return true;
                        }
                        // check whether this is a no invisibility area
                        String invisibility = rsa.getArea().getInvisibility();
                        if (invisibility.equals("DENY") && tardis.getPreset().equals(PRESET.INVISIBLE)) {
                            // check preset
                            TARDISMessage.send(player, "AREA_NO_INVISIBLE");
                            return true;
                        } else if (!invisibility.equals("ALLOW")) {
                            // force preset
                            TARDISMessage.send(player, "AREA_FORCE_PRESET", invisibility);
                            HashMap<String, Object> wherei = new HashMap<>();
                            wherei.put("tardis_id", id);
                            HashMap<String, Object> seti = new HashMap<>();
                            seti.put("chameleon_preset", invisibility);
                            // set chameleon adaption to OFF
                            seti.put("adapti_on", 0);
                            plugin.getQueryFactory().doSyncUpdate("tardis", seti, wherei);
                        }
                        Location l = plugin.getTardisArea().getNextSpot(rsa.getArea().getAreaName());
                        if (l == null) {
                            TARDISMessage.send(player, "NO_MORE_SPOTS");
                            return true;
                        }
                        set.put("world", l.getWorld().getName());
                        set.put("x", l.getBlockX());
                        set.put("y", l.getBlockY());
                        set.put("z", l.getBlockZ());
                        // should be setting direction of TARDIS
                        if (!rsa.getArea().getDirection().isEmpty()) {
                            set.put("direction", rsa.getArea().getDirection());
                        } else {
                            // get current direction
                            HashMap<String, Object> wherecl = new HashMap<>();
                            wherecl.put("tardis_id", id);
                            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                            if (!rsc.resultSet()) {
                                TARDISMessage.send(player, "CURRENT_NOT_FOUND");
                                return true;
                            }
                            set.put("direction", rsc.getDirection().toString());
                        }
                        set.put("submarine", 0);
                        plugin.getQueryFactory().doSyncUpdate("next", set, tid);
                        TARDISMessage.send(player, "TRAVEL_APPROVED", args[1]);
                        plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(travel, TravelType.AREA));
                        plugin.getTrackerKeeper().getRescue().remove(id);
                        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                            new TARDISLand(plugin, id, player).exitVortex();
                            plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.AREA, id));
                        }
                        return true;
                    }
                    if (TARDISPermission.hasPermission(player, "tardis.timetravel.location")) {
                        switch (args.length) {
                            case 2:
                                if (args[0].equalsIgnoreCase("random")) {
                                    // check world is an actual world
                                    World world = TARDISAliasResolver.getWorldFromAlias(args[1]);
                                    if (world == null) {
                                        TARDISMessage.send(player, "COULD_NOT_FIND_WORLD");
                                        return true;
                                    }
                                    // check world is enabled for travel
                                    if (!plugin.getPlanetsConfig().getBoolean("planets." + world.getName() + ".time_travel")) {
                                        TARDISMessage.send(player, "NO_WORLD_TRAVEL");
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
                                            TARDISMessage.send(player, "LOC_SAVED", true);
                                            plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(travel, TravelType.RANDOM));
                                            plugin.getTrackerKeeper().getRescue().remove(id);
                                            if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                                new TARDISLand(plugin, id, player).exitVortex();
                                                plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.RANDOM, id));
                                            }
                                            return true;
                                        }
                                    } else {
                                        TARDISMessage.send(player, "CURRENT_NOT_FOUND");
                                        return true;
                                    }
                                } else {
                                    TARDISMessage.send(player, "ARG_COORDS");
                                    return false;
                                }
                            case 3:
                                if (args[0].startsWith("~")) {
                                    HashMap<String, Object> wherecl = new HashMap<>();
                                    wherecl.put("tardis_id", id);
                                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                                    if (!rsc.resultSet()) {
                                        TARDISMessage.send(player, "CURRENT_NOT_FOUND");
                                        return true;
                                    }
                                    if (!plugin.getPlanetsConfig().getBoolean("planets." + rsc.getWorld().getName() + ".time_travel")) {
                                        TARDISMessage.send(player, "NO_WORLD_TRAVEL");
                                        return true;
                                    }
                                    if (rsc.isSubmarine()) {
                                        TARDISMessage.send(player, "SUB_NO_CMD");
                                        return true;
                                    }
                                    // check args
                                    int rx = getRelativeCoordinate(args[0]);
                                    int ry = getRelativeCoordinate(args[1]);
                                    int rz = getRelativeCoordinate(args[2]);
                                    if (rx == Integer.MAX_VALUE || ry == Integer.MAX_VALUE || rz == Integer.MAX_VALUE) {
                                        TARDISMessage.send(player, "RELATIVE_NOT_FOUND");
                                        return true;
                                    }
                                    // add relative coordinates
                                    int x = rsc.getX() + rx;
                                    int y = rsc.getY() + ry;
                                    int z = rsc.getZ() + rz;
                                    if (y < 0 || y > 256) {
                                        TARDISMessage.send(player, "Y_NOT_VALID");
                                        return true;
                                    }
                                    // make location
                                    Location location = new Location(rsc.getWorld(), x, y, z);
                                    // check location
                                    int count = checkLocation(location, player, id);
                                    if (count > 0) {
                                        TARDISMessage.send(player, "NOT_SAFE");
                                    } else {
                                        set.put("world", location.getWorld().getName());
                                        set.put("x", location.getBlockX());
                                        set.put("y", location.getBlockY());
                                        set.put("z", location.getBlockZ());
                                        set.put("submarine", 0);
                                        plugin.getQueryFactory().doSyncUpdate("next", set, tid);
                                        TARDISMessage.send(player, "LOC_SAVED", true);
                                        plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(travel, TravelType.RELATIVE_COORDINATES));
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
                                            TARDISMessage.send(player, "NOT_SAFE");
                                        } else {
                                            set.put("world", determiney.getWorld().getName());
                                            set.put("x", determiney.getBlockX());
                                            set.put("y", determiney.getBlockY());
                                            set.put("z", determiney.getBlockZ());
                                            set.put("submarine", 0);
                                            plugin.getQueryFactory().doSyncUpdate("next", set, tid);
                                            TARDISMessage.send(player, "LOC_SAVED", true);
                                            plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(travel, TravelType.COORDINATES));
                                            plugin.getTrackerKeeper().getRescue().remove(id);
                                            if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                                new TARDISLand(plugin, id, player).exitVortex();
                                                plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.COORDINATES, id));
                                            }
                                        }
                                        return true;
                                    }
                                }
                                break;
                            default:
                                // coords
                                Location giveny = getCoordinateLocation(args, player, id);
                                if (giveny != null) {
                                    // check location
                                    int count = checkLocation(giveny, player, id);
                                    if (count > 0) {
                                        TARDISMessage.send(player, "NOT_SAFE");
                                    } else {
                                        set.put("world", giveny.getWorld().getName());
                                        set.put("x", giveny.getBlockX());
                                        set.put("y", giveny.getBlockY());
                                        set.put("z", giveny.getBlockZ());
                                        set.put("submarine", 0);
                                        plugin.getQueryFactory().doSyncUpdate("next", set, tid);
                                        TARDISMessage.send(player, "LOC_SAVED", true);
                                        plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(travel, TravelType.COORDINATES));
                                        plugin.getTrackerKeeper().getRescue().remove(id);
                                        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                            new TARDISLand(plugin, id, player).exitVortex();
                                            plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.COORDINATES, id));
                                        }
                                    }
                                    return true;
                                }
                                break;
                        }
                    } else {
                        TARDISMessage.send(player, "TRAVEL_NO_PERM_COORDS");
                        return true;
                    }
                }
            } else {
                TARDISMessage.send(player, "NO_PERMS");
                return false;
            }
        }
        return false;
    }

    private boolean travelPlayer(Player player, String p, int id) {
        if (TARDISPermission.hasPermission(player, "tardis.timetravel.player")) {
            if (!plugin.getDifficulty().equals(Difficulty.EASY) && !plugin.getUtils().inGracePeriod(player, false)) {
                TARDISMessage.send(player, "ADV_PLAYER");
                return true;
            }
            if (player.getName().equalsIgnoreCase(p)) {
                TARDISMessage.send(player, "TRAVEL_NO_SELF");
                return true;
            }
            HashMap<String, Object> wherecl = new HashMap<>();
            wherecl.put("tardis_id", id);
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
            if (!rsc.resultSet()) {
                TARDISMessage.send(player, "CURRENT_NOT_FOUND");
                return true;
            }
            // check the player
            Player saved = plugin.getServer().getPlayer(p);
            if (saved == null) {
                TARDISMessage.send(player, "NOT_ONLINE");
                return true;
            }
            // check the to player's DND status
            ResultSetPlayerPrefs rspp = new ResultSetPlayerPrefs(plugin, saved.getUniqueId().toString());
            if (rspp.resultSet() && rspp.isDND()) {
                TARDISMessage.send(player, "DND", p);
                return true;
            }
            new TARDISRescue(plugin).rescue(player, saved.getUniqueId(), id, rsc.getDirection(), false, false);
        } else {
            TARDISMessage.send(player, "NO_PERM_PLAYER");
        }
        return true;
    }

    private String getQuotedString(String[] args) {
        StringBuilder buf = new StringBuilder();
        String w_str = "";
        for (String s : args) {
            buf.append(s).append(" ");
        }
        String tmp = buf.toString();
        Pattern p = Pattern.compile("'([^']*)'");
        Matcher m = p.matcher(tmp);
        while (m.find()) {
            w_str = m.group(1);
        }
        return w_str;
    }

    private Location getCoordinateLocation(String[] args, Player player, int id) {
        // coords
        String w_str = args[0];
        if (w_str.contains("'")) {
            w_str = getQuotedString(args);
        }
        if (isNumber(args[0])) {
            TARDISMessage.send(player, "WORLD_NOT_FOUND");
            return null;
        }
        if (args[1].startsWith("~")) {
            TARDISMessage.send(player, "NO_WORLD_RELATIVE");
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
                TARDISMessage.send(player, "CURRENT_NOT_FOUND");
                return null;
            }
            w = rsc.getWorld();
        } else {
            if (plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
                w = plugin.getMVHelper().getWorld(w_str);
            } else {
                w = TARDISAliasResolver.getWorldFromAlias(w_str);
            }
        }
        if (w == null) {
            TARDISMessage.send(player, "WORLD_NOT_FOUND");
            return null;
        }
        if (!plugin.getPlanetsConfig().getBoolean("planets." + w.getName() + ".time_travel")) {
            TARDISMessage.send(player, "NO_WORLD_TRAVEL");
            return null;
        }
        if (!plugin.getConfig().getBoolean("travel.include_default_world") && plugin.getConfig().getBoolean("creation.default_world") && args[0].equals(plugin.getConfig().getString("creation.default_world_name"))) {
            TARDISMessage.send(player, "NO_WORLD_TRAVEL");
            return null;
        }
        z = TARDISNumberParsers.parseInt(args[args.length - 1]);
        if (args.length > 3) {
            x = TARDISNumberParsers.parseInt(args[args.length - 3]);
            y = TARDISNumberParsers.parseInt(args[args.length - 2]);
            if (y < 0 || y > 250) {
                TARDISMessage.send(player, "Y_NOT_VALID");
                return null;
            }
        } else {
            x = TARDISNumberParsers.parseInt(args[args.length - 2]);
            Chunk chunk = w.getChunkAt(x, z);
            while (!chunk.isLoaded()) {
                chunk.load();
            }
            y = TARDISStaticLocationGetters.getHighestYin3x3(w, x, z);
        }
        int max = Math.min(plugin.getConfig().getInt("travel.max_distance"), (int) (w.getWorldBorder().getSize() / 2) - 17);
        if (x > max || x < -max || z > max || z < -max) {
            TARDISMessage.send(player, "XZ_NOT_VALID");
            return null;
        }
        return new Location(w, x, y, z);
    }

    public Location searchBiome(Player p, int id, Biome b, World w, int startx, int startz) {
        if (b == null) {
            TARDISMessage.send(p, "BIOME_NOT_VALID");
            return null;
        }
        HashMap<String, Object> wherecl = new HashMap<>();
        wherecl.put("tardis_id", id);
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
        if (!rsc.resultSet()) {
            TARDISMessage.send(p, "CURRENT_NOT_FOUND");
            return null;
        }
        // get a world
        // Assume all non-nether/non-end world environments are NORMAL
        if (w != null && !w.getEnvironment().equals(Environment.NETHER) && !w.getEnvironment().equals(Environment.THE_END)) {
            int limite = startx + 30000;
            int limits = startz + 30000;
            int limitw = startx - 30000;
            int limitn = startz - 30000;
            if (plugin.getPM().isPluginEnabled("WorldBorder")) {
                // get the border limit for this world
                TARDISWorldBorderChecker wb = new TARDISWorldBorderChecker(plugin);
                int[] data = wb.getBorderDistance(w.getName());
                limite = data[0];
                limits = data[1];
                limitw = -data[0];
                limitn = -data[1];
            }
            int step = 10;
            // search in a random direction
            Integer[] directions = new Integer[]{0, 1, 2, 3};
            Collections.shuffle(Arrays.asList(directions));
            for (int i = 0; i < 4; i++) {
                switch (directions[i]) {
                    case 0:
                        // east
                        for (int east = startx; east < limite; east += step) {
                            Biome chkb = w.getBiome(east, w.getHighestBlockYAt(east, startz), startz);
                            if (chkb.equals(b)) {
                                TARDISMessage.send(p, "BIOME_E", b.toString());
                                return new Location(w, east, TARDISStaticLocationGetters.getHighestYin3x3(w, east, startz), startz);
                            }
                        }
                        break;
                    case 1:
                        // south
                        for (int south = startz; south < limits; south += step) {
                            Biome chkb = w.getBiome(startx, w.getHighestBlockYAt(startx, south), south);
                            if (chkb.equals(b)) {
                                TARDISMessage.send(p, "BIOME_S", b.toString());
                                return new Location(w, startx, TARDISStaticLocationGetters.getHighestYin3x3(w, startx, south), south);
                            }
                        }
                        break;
                    case 2:
                        // west
                        for (int west = startx; west > limitw; west -= step) {
                            Biome chkb = w.getBiome(west, w.getHighestBlockYAt(west, startz), startz);
                            if (chkb.equals(b)) {
                                TARDISMessage.send(p, "BIOME_W", b.toString());
                                return new Location(w, west, TARDISStaticLocationGetters.getHighestYin3x3(w, west, startz), startz);
                            }
                        }
                        break;
                    case 3:
                        // north
                        for (int north = startz; north > limitn; north -= step) {
                            Biome chkb = w.getBiome(startx, w.getHighestBlockYAt(startx, north), north);
                            if (chkb.equals(b)) {
                                TARDISMessage.send(p, "BIOME_N", b.toString());
                                return new Location(w, startx, TARDISStaticLocationGetters.getHighestYin3x3(w, startx, north), north);
                            }
                        }
                        break;
                }
            }
        }
        return null;
    }

    private int checkLocation(Location location, Player player, int id) {
        if (location.getWorld().getEnvironment().equals(Environment.NETHER) && location.getY() > 127) {
            TARDISMessage.send(player, "TRAVEL_NETHER");
            return 1;
        }
        if (!plugin.getTardisArea().areaCheckInExisting(location)) {
            TARDISMessage.send(player, "TRAVEL_IN_AREA", ChatColor.AQUA + "/tardistravel area [area name]");
            return 1;
        }
        if (!plugin.getPluginRespect().getRespect(location, new Parameters(player, Flag.getDefaultFlags()))) {
            return 1;
        }
        HashMap<String, Object> wherecl = new HashMap<>();
        wherecl.put("tardis_id", id);
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
        if (!rsc.resultSet()) {
            TARDISMessage.send(player, "CURRENT_NOT_FOUND");
            return 1;
        }
        // check location
        int[] start_loc = TARDISTimeTravel.getStartLocation(location, rsc.getDirection());
        return TARDISTimeTravel.safeLocation(start_loc[0], location.getBlockY(), start_loc[2], start_loc[1], start_loc[3], location.getWorld(), rsc.getDirection());
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
}
