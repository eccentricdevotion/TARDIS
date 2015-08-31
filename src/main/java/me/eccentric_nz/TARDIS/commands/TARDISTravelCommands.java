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
package me.eccentric_nz.TARDIS.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.ResultSetBackLocation;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.FLAG;
import me.eccentric_nz.TARDIS.travel.TARDISCaveFinder;
import me.eccentric_nz.TARDIS.travel.TARDISRescue;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.travel.TARDISTravelRequest;
import me.eccentric_nz.TARDIS.travel.TARDISVillageTravel;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISWorldBorderChecker;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command /tardistravel [arguments].
 *
 * Time travel is the process of travelling through time, even in a non-linear
 * direction.
 *
 * @author eccentric_nz
 */
public class TARDISTravelCommands implements CommandExecutor {

    private final TARDIS plugin;
    private final List<String> BIOME_SUBS = new ArrayList<String>();
    private final List<String> mustUseAdvanced = Arrays.asList("area", "biome", "dest");

    public TARDISTravelCommands(TARDIS plugin) {
        this.plugin = plugin;
        for (Biome bi : Biome.values()) {
            if (!bi.equals(Biome.HELL) && !bi.equals(Biome.SKY)) {
                BIOME_SUBS.add(bi.toString());
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
            if (player.hasPermission("tardis.timetravel")) {
                if (args.length < 1) {
                    new TARDISCommandHelper(plugin).getCommand("tardistravel", sender);
                    return true;
                }
                QueryFactory qf = new QueryFactory(plugin);
                TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
                // get tardis data
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("uuid", player.getUniqueId().toString());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (!rs.resultSet()) {
                    TARDISMessage.send(player, "NO_TARDIS");
                    return true;
                }
                int id = rs.getTardis_id();
                if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                    TARDISMessage.send(player, "SIEGE_NO_CMD");
                    return true;
                }
                int level = rs.getArtron_level();
                boolean powered = rs.isPowered_on();
                if (!rs.isHandbrake_on()) {
                    TARDISMessage.send(player, "NOT_WHILE_TRAVELLING");
                    return true;
                }
                HashMap<String, Object> wheret = new HashMap<String, Object>();
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
                HashMap<String, Object> tid = new HashMap<String, Object>();
                HashMap<String, Object> set = new HashMap<String, Object>();
                tid.put("tardis_id", id);
                if (player.hasPermission("tardis.exile") && plugin.getConfig().getBoolean("travel.exile")) {
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
                    qf.doUpdate("next", set, tid);
                    TARDISMessage.send(player, "TRAVEL_APPROVED", permArea);
                    plugin.getTrackerKeeper().getHasDestination().put(id, travel);
                    if (plugin.getTrackerKeeper().getRescue().containsKey(id)) {
                        plugin.getTrackerKeeper().getRescue().remove(id);
                    }
                    return true;
                } else {
                    if (args.length == 1) {
                        // we're thinking this is a player's name or home / back / cave
                        if (args[0].equalsIgnoreCase("home") || args[0].equalsIgnoreCase("back") || args[0].equalsIgnoreCase("cave") || args[0].equalsIgnoreCase("village")) {
                            String which;
                            if (args[0].equalsIgnoreCase("home")) {
                                // get home location
                                HashMap<String, Object> wherehl = new HashMap<String, Object>();
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
                            } else if (args[0].equalsIgnoreCase("back")) {
                                // get fast return location
                                HashMap<String, Object> wherebl = new HashMap<String, Object>();
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
                                which = "Fast Return";
                            } else if (args[0].equalsIgnoreCase("cave")) {
                                if (!player.hasPermission("tardis.timetravel.cave")) {
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
                                if (!plugin.getPluginRespect().getRespect(cave, new Parameters(player, FLAG.getDefaultFlags()))) {
                                    return true;
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
                                if (!player.hasPermission("tardis.timetravel.village")) {
                                    TARDISMessage.send(player, "TRAVEL_NO_PERM_VILLAGE");
                                    return true;
                                }
                                // find a village
                                Location village = new TARDISVillageTravel(plugin).getRandomVillage(player, id);
                                if (village == null) {
                                    TARDISMessage.send(player, "CAVE_NOT_FOUND");
                                    return true;
                                }
                                // check respect
                                if (!plugin.getPluginRespect().getRespect(village, new Parameters(player, FLAG.getDefaultFlags()))) {
                                    return true;
                                }
                                set.put("world", village.getWorld().getName());
                                set.put("x", village.getBlockX());
                                set.put("y", village.getBlockY());
                                set.put("z", village.getBlockZ());
                                set.put("submarine", 0);
                                which = "Village";
                            }
                            qf.doUpdate("next", set, tid);
                            TARDISMessage.send(player, "TRAVEL_LOADED", which, true);
                            plugin.getTrackerKeeper().getHasDestination().put(id, travel);
                            if (plugin.getTrackerKeeper().getRescue().containsKey(id)) {
                                plugin.getTrackerKeeper().getRescue().remove(id);
                            }
                            return true;
                        } else {
                            if (player.hasPermission("tardis.timetravel.player")) {
                                if (plugin.getConfig().getString("preferences.difficulty").equals("hard") && !plugin.getUtils().inGracePeriod(player, false)) {
                                    TARDISMessage.send(player, "ADV_PLAYER");
                                    return true;
                                }
                                if (player.getName().equalsIgnoreCase(args[0])) {
                                    TARDISMessage.send(player, "TRAVEL_NO_SELF");
                                    return true;
                                }
                                HashMap<String, Object> wherecl = new HashMap<String, Object>();
                                wherecl.put("tardis_id", id);
                                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                                if (!rsc.resultSet()) {
                                    TARDISMessage.send(player, "CURRENT_NOT_FOUND");
                                    return true;
                                }
                                // check the player
                                Player saved = plugin.getServer().getPlayer(args[0]);
                                if (saved == null) {
                                    TARDISMessage.send(player, "NOT_ONLINE");
                                    return true;
                                }
                                // check the to player's DND status
                                HashMap<String, Object> wherednd = new HashMap<String, Object>();
                                wherednd.put("uuid", saved.getUniqueId().toString());
                                ResultSetPlayerPrefs rspp = new ResultSetPlayerPrefs(plugin, wherednd);
                                if (rspp.resultSet() && rspp.isDND()) {
                                    TARDISMessage.send(player, "DND", args[0]);
                                    return true;
                                }
                                new TARDISRescue(plugin).rescue(player, saved.getUniqueId(), id, tt, rsc.getDirection(), false, false);
                                return true;
                            } else {
                                TARDISMessage.send(player, "NO_PERM_PLAYER");
                                return true;
                            }
                        }
                    }
                    if (args.length == 2 && (args[1].equals("?") || args[1].equalsIgnoreCase("tpa"))) {
                        if (!player.hasPermission("tardis.timetravel.player")) {
                            TARDISMessage.send(player, "NO_PERM_PLAYER");
                            return true;
                        }
                        Player requested = plugin.getServer().getPlayer(args[0]);
                        if (requested == null) {
                            TARDISMessage.send(player, "NOT_ONLINE");
                            return true;
                        }
                        // check the to player's DND status
                        HashMap<String, Object> wherednd = new HashMap<String, Object>();
                        wherednd.put("uuid", requested.getUniqueId().toString());
                        ResultSetPlayerPrefs rspp = new ResultSetPlayerPrefs(plugin, wherednd);
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
                        final UUID requestedUUID = requested.getUniqueId();
                        TARDISMessage.send(requested, "REQUEST_TRAVEL", player.getName(), ChatColor.AQUA + "tardis request accept" + ChatColor.RESET);
                        plugin.getTrackerKeeper().getChat().put(requestedUUID, player.getUniqueId());
                        final Player p = player;
                        final String to = args[0];
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                if (plugin.getTrackerKeeper().getChat().containsKey(requestedUUID)) {
                                    plugin.getTrackerKeeper().getChat().remove(requestedUUID);
                                    TARDISMessage.send(p, "REQUEST_NO_RESPONSE", to);
                                }
                            }
                        }, 1200L);
                        return true;
                    }
                    if (args.length == 2 && args[0].equalsIgnoreCase("biome")) {
                        // we're thinking this is a biome search
                        if (!player.hasPermission("tardis.timetravel.biome")) {
                            TARDISMessage.send(player, "TRAVEL_NO_PERM_BIOME");
                            return true;
                        }
                        if (plugin.getConfig().getString("preferences.difficulty").equals("hard") && mustUseAdvanced.contains(args[0].toLowerCase()) && !plugin.getUtils().inGracePeriod(player, false)) {
                            TARDISMessage.send(player, "ADV_BIOME");
                            return true;
                        }
                        String upper = args[1].toUpperCase(Locale.ENGLISH);
                        if (upper.equals("LIST")) {
                            StringBuilder buf = new StringBuilder();
                            for (String bi : BIOME_SUBS) {
                                buf.append(bi).append(", ");
                            }
                            String b = buf.toString().substring(0, buf.length() - 2);
                            TARDISMessage.send(player, "BIOMES", b);
                            return true;
                        } else {
                            try {
                                Biome biome = Biome.valueOf(upper);
                                TARDISMessage.send(player, "BIOME_SEARCH");

                                HashMap<String, Object> wherecl = new HashMap<String, Object>();
                                wherecl.put("tardis_id", rs.getTardis_id());
                                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                                if (!rsc.resultSet()) {
                                    TARDISMessage.send(player, "CURRENT_NOT_FOUND");
                                    return true;
                                }
                                Location tb = searchBiome(player, id, biome, rsc.getWorld(), rsc.getX() + 5, rsc.getZ() + 5);
                                if (tb == null) {
                                    TARDISMessage.send(player, "BIOME_NOT_FOUND");
                                    return true;
                                } else {
                                    if (!plugin.getPluginRespect().getRespect(tb, new Parameters(player, FLAG.getDefaultFlags()))) {
                                        return true;
                                    }
                                    World bw = tb.getWorld();
                                    // check location
                                    while (!bw.getChunkAt(tb).isLoaded()) {
                                        bw.getChunkAt(tb).load();
                                    }
                                    int[] start_loc = tt.getStartLocation(tb, rsc.getDirection());
                                    int tmp_y = tb.getBlockY();
                                    for (int up = 0; up < 10; up++) {
                                        int count = tt.safeLocation(start_loc[0], tmp_y + up, start_loc[2], start_loc[1], start_loc[3], tb.getWorld(), rsc.getDirection());
                                        if (count == 0) {
                                            tb.setY(tmp_y + up);
                                            break;
                                        }
                                    }
                                    set.put("world", tb.getWorld().getName());
                                    set.put("x", tb.getBlockX());
                                    set.put("y", tb.getBlockY());
                                    set.put("z", tb.getBlockZ());
                                    set.put("direction", rsc.getDirection().toString());
                                    set.put("submarine", 0);
                                    qf.doUpdate("next", set, tid);
                                    TARDISMessage.send(player, "BIOME_SET", true);
                                    plugin.getTrackerKeeper().getHasDestination().put(id, travel);
                                    if (plugin.getTrackerKeeper().getRescue().containsKey(id)) {
                                        plugin.getTrackerKeeper().getRescue().remove(id);
                                    }
                                }
                            } catch (IllegalArgumentException iae) {
                                TARDISMessage.send(player, "BIOME_NOT_VALID");
                                return true;
                            }
                            return true;
                        }
                    }
                    if (args.length == 2 && args[0].equalsIgnoreCase("dest")) {
                        // we're thinking this is a saved destination name
                        if (player.hasPermission("tardis.save")) {
                            HashMap<String, Object> whered = new HashMap<String, Object>();
                            whered.put("dest_name", args[1]);
                            whered.put("tardis_id", id);
                            ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
                            if (!rsd.resultSet()) {
                                TARDISMessage.send(player, "SAVE_NOT_FOUND");
                                return true;
                            }
                            World w = plugin.getServer().getWorld(rsd.getWorld());
                            if (w != null) {
                                if (w.getName().startsWith("TARDIS_")) {
                                    TARDISMessage.send(player, "SAVE_NO_TARDIS");
                                    return true;
                                }
                                Location save_dest = new Location(w, rsd.getX(), rsd.getY(), rsd.getZ());
                                if (!plugin.getPluginRespect().getRespect(save_dest, new Parameters(player, FLAG.getDefaultFlags()))) {
                                    return true;
                                }
                                if (!plugin.getTardisArea().areaCheckInExisting(save_dest)) {
                                    // save is in a TARDIS area, so check that the spot is not occupied
                                    HashMap<String, Object> wheres = new HashMap<String, Object>();
                                    wheres.put("world", rsd.getWorld());
                                    wheres.put("x", rsd.getX());
                                    wheres.put("y", rsd.getY());
                                    wheres.put("z", rsd.getZ());
                                    ResultSetCurrentLocation rsz = new ResultSetCurrentLocation(plugin, wheres);
                                    if (rsz.resultSet()) {
                                        TARDISMessage.send(player, "TARDIS_IN_SPOT", ChatColor.AQUA + "/tardistravel area [name]" + ChatColor.RESET);
                                        return true;
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
                                    HashMap<String, Object> wherecl = new HashMap<String, Object>();
                                    wherecl.put("tardis_id", rs.getTardis_id());
                                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                                    if (!rsc.resultSet()) {
                                        TARDISMessage.send(player, "CURRENT_NOT_FOUND");
                                        return true;
                                    }
                                    set.put("direction", rsc.getDirection().toString());
                                }
                                set.put("submarine", (rsd.isSubmarine()) ? 1 : 0);
                                qf.doUpdate("next", set, tid);
                                TARDISMessage.send(player, "LOC_SET", true);
                                plugin.getTrackerKeeper().getHasDestination().put(id, travel);
                                if (plugin.getTrackerKeeper().getRescue().containsKey(id)) {
                                    plugin.getTrackerKeeper().getRescue().remove(id);
                                }
                                return true;
                            } else {
                                TARDISMessage.send(player, "SAVE_NO_WORLD");
                                return true;
                            }
                        } else {
                            TARDISMessage.send(player, "TRAVEL_NO_PERM_SAVE");
                            return true;
                        }
                    }
                    if (args.length == 2 && args[0].equalsIgnoreCase("area")) {
                        // we're thinking this is admin defined area name
                        HashMap<String, Object> wherea = new HashMap<String, Object>();
                        wherea.put("area_name", args[1]);
                        ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false);
                        if (!rsa.resultSet()) {
                            TARDISMessage.send(player, "AREA_NOT_FOUND", ChatColor.GREEN + "/tardis list areas" + ChatColor.RESET);
                            return true;
                        }
                        if ((!player.hasPermission("tardis.area." + args[1]) && !player.hasPermission("tardis.area.*")) || (!player.isPermissionSet("tardis.area." + args[1]) && !player.isPermissionSet("tardis.area.*"))) {
                            TARDISMessage.send(player, "TRAVEL_NO_AREA_PERM", args[1]);
                            return true;
                        }
                        if (plugin.getConfig().getString("preferences.difficulty").equals("hard") && mustUseAdvanced.contains(args[0].toLowerCase()) && !plugin.getUtils().inGracePeriod(player, false)) {
                            TARDISMessage.send(player, "ADV_AREA");
                            return true;
                        }
                        Location l = plugin.getTardisArea().getNextSpot(rsa.getAreaName());
                        if (l == null) {
                            TARDISMessage.send(player, "NO_MORE_SPOTS");
                            return true;
                        }
                        set.put("world", l.getWorld().getName());
                        set.put("x", l.getBlockX());
                        set.put("y", l.getBlockY());
                        set.put("z", l.getBlockZ());
                        set.put("submarine", 0);
                        qf.doUpdate("next", set, tid);
                        TARDISMessage.send(player, "TRAVEL_APPROVED", args[1]);
                        plugin.getTrackerKeeper().getHasDestination().put(id, travel);
                        if (plugin.getTrackerKeeper().getRescue().containsKey(id)) {
                            plugin.getTrackerKeeper().getRescue().remove(id);
                        }
                        return true;
                    }
                    if (args.length == 3 && args[0].startsWith("~") && player.hasPermission("tardis.timetravel.location")) {
                        HashMap<String, Object> wherecl = new HashMap<String, Object>();
                        wherecl.put("tardis_id", id);
                        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                        if (!rsc.resultSet()) {
                            TARDISMessage.send(player, "CURRENT_NOT_FOUND");
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
                        // make location
                        Location location = new Location(rsc.getWorld(), x, y, z);
                        // check location
                        int count = this.checkLocation(location, player, id, tt);
                        if (count > 0) {
                            TARDISMessage.send(player, "NOT_SAFE");
                            return true;
                        } else {
                            set.put("world", location.getWorld().getName());
                            set.put("x", location.getBlockX());
                            set.put("y", location.getBlockY());
                            set.put("z", location.getBlockZ());
                            set.put("submarine", 0);
                            qf.doUpdate("next", set, tid);
                            TARDISMessage.send(player, "LOC_SAVED", true);
                            plugin.getTrackerKeeper().getHasDestination().put(id, travel);
                            if (plugin.getTrackerKeeper().getRescue().containsKey(id)) {
                                plugin.getTrackerKeeper().getRescue().remove(id);
                            }
                            return true;
                        }
                    }
                    if (args.length > 2 && args.length < 4) {
                        TARDISMessage.send(player, "ARG_COORDS");
                        return false;
                    }
                    if (args.length >= 4 && player.hasPermission("tardis.timetravel.location")) {
                        // coords
                        String w_str = args[0];
                        if (w_str.contains("'")) {
                            w_str = getQuotedString(args);
                        }
                        if (args[1].startsWith("~")) {
                            TARDISMessage.send(player, "NO_WORLD_RELATIVE");
                            return true;
                        }
                        // must be a location then
                        int x, y, z;
                        World w;
                        if (args[0].equals("~")) {
                            HashMap<String, Object> wherecl = new HashMap<String, Object>();
                            wherecl.put("tardis_id", id);
                            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                            if (!rsc.resultSet()) {
                                TARDISMessage.send(player, "CURRENT_NOT_FOUND");
                                return true;
                            }
                            w = rsc.getWorld();
                        } else {
                            w = plugin.getServer().getWorld(w_str);
                        }
                        if (w == null) {
                            TARDISMessage.send(player, "WORLD_NOT_FOUND");
                            return true;
                        }
                        if (!plugin.getConfig().getBoolean("worlds." + w.getName())) {
                            TARDISMessage.send(player, "NO_WORLD_TRAVEL");
                            return true;
                        }
                        if (!plugin.getConfig().getBoolean("travel.include_default_world") && plugin.getConfig().getBoolean("creation.default_world") && args[0].equals(plugin.getConfig().getString("creation.default_world_name"))) {
                            TARDISMessage.send(player, "NO_WORLD_TRAVEL");
                            return true;
                        }
                        x = TARDISNumberParsers.parseInt(args[args.length - 3]);
                        y = TARDISNumberParsers.parseInt(args[args.length - 2]);
                        if (y == 0 || y > 250) {
                            TARDISMessage.send(player, "Y_NOT_VALID");
                            return true;
                        }
                        z = TARDISNumberParsers.parseInt(args[args.length - 1]);
                        if (x > 15000000 || x < -15000000 || z > 15000000 || z < -15000000) {
                            TARDISMessage.send(player, "XZ_NOT_VALID");
                            return true;
                        }
                        Location location = new Location(w, x, y, z);
                        // check location
                        int count = this.checkLocation(location, player, id, tt);
                        if (count > 0) {
                            TARDISMessage.send(player, "NOT_SAFE");
                            return true;
                        } else {
                            set.put("world", location.getWorld().getName());
                            set.put("x", location.getBlockX());
                            set.put("y", location.getBlockY());
                            set.put("z", location.getBlockZ());
                            set.put("submarine", 0);
                            qf.doUpdate("next", set, tid);
                            TARDISMessage.send(player, "LOC_SAVED", true);
                            plugin.getTrackerKeeper().getHasDestination().put(id, travel);
                            if (plugin.getTrackerKeeper().getRescue().containsKey(id)) {
                                plugin.getTrackerKeeper().getRescue().remove(id);
                            }
                            return true;
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

    public Location searchBiome(Player p, int id, Biome b, World w, int startx, int startz) {
        HashMap<String, Object> wherecl = new HashMap<String, Object>();
        wherecl.put("tardis_id", id);
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
        if (!rsc.resultSet()) {
            TARDISMessage.send(p, "CURRENT_NOT_FOUND");
            return null;
        }
        Location l = null;
        // get a world
        // Assume all non-nether/non-end world environments are NORMAL
        if (w != null && !w.getEnvironment().equals(Environment.NETHER) && !w.getEnvironment().equals(Environment.THE_END)) {
            int limitx = 30000;
            int limitz = 30000;
            if (plugin.getPM().isPluginEnabled("WorldBorder")) {
                // get the border limit for this world
                TARDISWorldBorderChecker wb = new TARDISWorldBorderChecker(plugin, plugin.getPluginRespect().isBorderOnServer());
                int[] data = wb.getBorderDistance(w.getName());
                limitx = data[0];
                limitz = data[1];
            }
            int step = 10;
            // search in a random direction
            Integer[] directions = new Integer[]{0, 1, 2, 3};
            Collections.shuffle(Arrays.asList(directions));
            for (int i = 0; i < 4; i++) {
                switch (directions[i]) {
                    case 0:
                        // east
                        for (int east = startx; east < limitx; east += step) {
                            Biome chkb = w.getBiome(east, startz);
                            if (chkb.equals(b)) {
                                TARDISMessage.send(p, "BIOME_E", b.toString());
                                return new Location(w, east, w.getHighestBlockYAt(east, startz), startz);
                            }
                        }
                        break;
                    case 1:
                        // south
                        for (int south = startz; south < limitz; south += step) {
                            Biome chkb = w.getBiome(startx, south);
                            if (chkb.equals(b)) {
                                TARDISMessage.send(p, "BIOME_S", b.toString());
                                return new Location(w, startx, w.getHighestBlockYAt(startx, south), south);
                            }
                        }
                        break;
                    case 2:
                        // west
                        for (int west = startx; west > -limitx; west -= step) {
                            Biome chkb = w.getBiome(west, startz);
                            if (chkb.equals(b)) {
                                TARDISMessage.send(p, "BIOME_W", b.toString());
                                return new Location(w, west, w.getHighestBlockYAt(west, startz), startz);
                            }
                        }
                        break;
                    case 3:
                        // north
                        for (int north = startz; north > -limitz; north -= step) {
                            Biome chkb = w.getBiome(startx, north);
                            if (chkb.equals(b)) {
                                TARDISMessage.send(p, "BIOME_N", b.toString());
                                return new Location(w, startx, w.getHighestBlockYAt(startx, north), north);
                            }
                        }
                        break;
                }
            }
        }
        return l;
    }

    private int checkLocation(Location location, Player player, int id, TARDISTimeTravel tt) {
        if (!plugin.getTardisArea().areaCheckInExisting(location)) {
            TARDISMessage.send(player, "TRAVEL_IN_AREA", ChatColor.AQUA + "/tardistravel area [area name]");
            return 1;
        }
        if (!plugin.getPluginRespect().getRespect(location, new Parameters(player, FLAG.getDefaultFlags()))) {
            return 1;
        }
        HashMap<String, Object> wherecl = new HashMap<String, Object>();
        wherecl.put("tardis_id", id);
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
        if (!rsc.resultSet()) {
            TARDISMessage.send(player, "CURRENT_NOT_FOUND");
            return 1;
        }
        // check location
        int[] start_loc = tt.getStartLocation(location, rsc.getDirection());
        return tt.safeLocation(start_loc[0], location.getBlockY(), start_loc[2], start_loc[1], start_loc[3], location.getWorld(), rsc.getDirection());
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
