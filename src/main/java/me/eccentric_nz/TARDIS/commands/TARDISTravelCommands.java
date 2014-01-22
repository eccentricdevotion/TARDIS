/*
 * Copyright (C) 2013 eccentric_nz
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.ResultSetBackLocation;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.travel.TARDISCaveFinder;
import me.eccentric_nz.TARDIS.travel.TARDISPluginRespect;
import me.eccentric_nz.TARDIS.travel.TARDISRescue;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
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
    private TARDISPluginRespect respect;
    private final List<String> BIOME_SUBS = new ArrayList<String>();

    public TARDISTravelCommands(TARDIS plugin) {
        this.plugin = plugin;
        for (Biome bi : Biome.values()) {
            if (!bi.equals(Biome.HELL) && !bi.equals(Biome.SKY)) {
                BIOME_SUBS.add(bi.toString());
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        // If the player typed /tardistravel then do the following...
        // check there is the right number of arguments
        if (cmd.getName().equalsIgnoreCase("tardistravel")) {
            if (player == null) {
                sender.sendMessage(plugin.pluginName + ChatColor.RED + " This command can only be run by a player");
                return true;
            }
            if (player.hasPermission("tardis.timetravel")) {
                if (args.length < 1) {
                    sender.sendMessage(plugin.pluginName + "Too few command arguments!");
                    return false;
                }
                QueryFactory qf = new QueryFactory(plugin);
                TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
                // get tardis data
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("owner", player.getName());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (!rs.resultSet()) {
                    sender.sendMessage(plugin.pluginName + MESSAGE.NO_TARDIS.getText());
                    return true;
                }
                int id = rs.getTardis_id();
                if (!rs.isHandbrake_on()) {
                    player.sendMessage(plugin.pluginName + ChatColor.RED + MESSAGE.NOT_WHILE_TRAVELLING.getText());
                    return true;
                }
                HashMap<String, Object> wheret = new HashMap<String, Object>();
                wheret.put("player", player.getName());
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
                if (!rst.resultSet()) {
                    sender.sendMessage(plugin.pluginName + MESSAGE.NOT_IN_TARDIS.getText());
                    return true;
                }
                int tardis_id = rst.getTardis_id();
                if (tardis_id != id) {
                    sender.sendMessage(plugin.pluginName + "You can only run this command if you are the Timelord of " + ChatColor.LIGHT_PURPLE + "this" + ChatColor.RESET + " TARDIS!");
                    return true;
                }
                int level = rs.getArtron_level();
                int travel = plugin.getArtronConfig().getInt("travel");
                if (level < travel) {
                    player.sendMessage(plugin.pluginName + ChatColor.RED + MESSAGE.NOT_ENOUGH_ENERGY.getText());
                    return true;
                }
                HashMap<String, Object> tid = new HashMap<String, Object>();
                HashMap<String, Object> set = new HashMap<String, Object>();
                tid.put("tardis_id", id);
                if (player.hasPermission("tardis.exile") && plugin.getConfig().getBoolean("travel.exile")) {
                    // get the exile area
                    String permArea = plugin.ta.getExileArea(player);
                    player.sendMessage(plugin.pluginName + ChatColor.RED + " Notice:" + ChatColor.RESET + " Your travel has been restricted to the [" + permArea + "] area!");
                    Location l = plugin.ta.getNextSpot(permArea);
                    if (l == null) {
                        player.sendMessage(plugin.pluginName + "All available parking spots are taken in this area!");
                        return true;
                    }
                    set.put("world", l.getWorld().getName());
                    set.put("x", l.getBlockX());
                    set.put("y", l.getBlockY());
                    set.put("z", l.getBlockZ());
                    set.put("submarine", 0);
                    qf.doUpdate("next", set, tid);
                    player.sendMessage(plugin.pluginName + "Your TARDIS was approved for parking in [" + permArea + "]!");
                    plugin.tardisHasDestination.put(id, travel);
                    if (plugin.trackRescue.containsKey(Integer.valueOf(id))) {
                        plugin.trackRescue.remove(Integer.valueOf(id));
                    }
                    return true;
                } else {
                    if (args.length == 1) {
                        // we're thinking this is a player's name or home / back / cave
                        if (args[0].equalsIgnoreCase("home") || args[0].equalsIgnoreCase("back") || args[0].equalsIgnoreCase("cave")) {
                            String which;
                            if (args[0].equalsIgnoreCase("home")) {
                                // get home location
                                HashMap<String, Object> wherehl = new HashMap<String, Object>();
                                wherehl.put("tardis_id", id);
                                ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, wherehl);
                                if (!rsh.resultSet()) {
                                    player.sendMessage(plugin.pluginName + "Could not get the TARDIS 'home' location!");
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
                                    player.sendMessage(plugin.pluginName + "Could not get the TARDIS 'previous' location!");
                                    return true;
                                }
                                set.put("world", rsb.getWorld().getName());
                                set.put("x", rsb.getX());
                                set.put("y", rsb.getY());
                                set.put("z", rsb.getZ());
                                set.put("direction", rsb.getDirection().toString());
                                set.put("submarine", (rsb.isSubmarine()) ? 1 : 0);
                                which = "Fast Return";
                            } else {
                                if (!player.hasPermission("tardis.timetravel.cave")) {
                                    player.sendMessage(plugin.pluginName + "You do not have permission to time travel to a cave!");
                                    return true;
                                }
                                // find a cave
                                Location cave = new TARDISCaveFinder(plugin).searchCave(player, id);
                                if (cave == null) {
                                    player.sendMessage(plugin.pluginName + "Could not find a cave within 2000 blocks!");
                                    return true;
                                }
                                // check respect
                                respect = new TARDISPluginRespect(plugin);
                                if (!respect.getRespect(player, cave, true)) {
                                    return true;
                                }
                                set.put("world", cave.getWorld().getName());
                                set.put("x", cave.getBlockX());
                                set.put("y", cave.getBlockY());
                                set.put("z", cave.getBlockZ());
                                set.put("submarine", 0);
                                which = "Cave";
                            }
                            qf.doUpdate("next", set, tid);
                            sender.sendMessage(plugin.pluginName + which + " location loaded succesfully. Please release the handbrake!");
                            plugin.tardisHasDestination.put(id, travel);
                            if (plugin.trackRescue.containsKey(Integer.valueOf(id))) {
                                plugin.trackRescue.remove(Integer.valueOf(id));
                            }
                            return true;
                        } else {
                            if (player.hasPermission("tardis.timetravel.player")) {
                                if (player.getName().equalsIgnoreCase(args[0])) {
                                    player.sendMessage(plugin.pluginName + "You cannot travel to yourself!");
                                    return true;
                                }
                                HashMap<String, Object> wherecl = new HashMap<String, Object>();
                                wherecl.put("tardis_id", id);
                                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                                if (!rsc.resultSet()) {
                                    player.sendMessage(plugin.pluginName + MESSAGE.NO_CURRENT.getText());
                                    return true;
                                }
                                // check the to player's DND status
                                HashMap<String, Object> wherednd = new HashMap<String, Object>();
                                wherednd.put("player", args[0].toLowerCase());
                                ResultSetPlayerPrefs rspp = new ResultSetPlayerPrefs(plugin, wherednd);
                                if (rspp.resultSet() && rspp.isDND()) {
                                    player.sendMessage(plugin.pluginName + args[0] + " does not want to be disturbed right now! Try again later.");
                                    return true;
                                }
                                TARDISRescue to_player = new TARDISRescue(plugin);
                                return to_player.rescue(player, args[0], id, tt, rsc.getDirection(), false);
                            } else {
                                player.sendMessage(plugin.pluginName + "You do not have permission to time travel to a player!");
                                return true;
                            }
                        }
                    }
                    if (args.length == 2 && args[0].equalsIgnoreCase("biome")) {
                        // we're thinking this is a biome search
                        if (!player.hasPermission("tardis.timetravel.biome")) {
                            player.sendMessage(plugin.pluginName + "You do not have permission to time travel to a biome!");
                            return true;
                        }
                        String upper = args[1].toUpperCase(Locale.ENGLISH);
                        if (upper.equals("LIST")) {
                            StringBuilder buf = new StringBuilder();
                            for (String bi : BIOME_SUBS) {
                                buf.append(bi).append(", ");
                            }
                            String b = buf.toString().substring(0, buf.length() - 2);
                            sender.sendMessage(plugin.pluginName + "Biomes: " + b);
                            return true;
                        } else {
                            try {
                                Biome biome = Biome.valueOf(upper);
                                sender.sendMessage(plugin.pluginName + "Searching for biome, this may take some time!");

                                HashMap<String, Object> wherecl = new HashMap<String, Object>();
                                wherecl.put("tardis_id", rs.getTardis_id());
                                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                                if (!rsc.resultSet()) {
                                    player.sendMessage(plugin.pluginName + MESSAGE.NO_CURRENT.getText());
                                    return true;
                                }
                                Location nsob = searchBiome(player, id, biome, rsc.getWorld(), rsc.getX(), rsc.getZ());
                                if (nsob == null) {
                                    sender.sendMessage(plugin.pluginName + "Could not find biome!");
                                    return true;
                                } else {
                                    respect = new TARDISPluginRespect(plugin);
                                    if (!respect.getRespect(player, nsob, true)) {
                                        return true;
                                    }
                                    World bw = nsob.getWorld();
                                    // check location
                                    while (!bw.getChunkAt(nsob).isLoaded()) {
                                        bw.getChunkAt(nsob).load();
                                    }
                                    int[] start_loc = tt.getStartLocation(nsob, rsc.getDirection());
                                    int tmp_y = nsob.getBlockY();
                                    for (int up = 0; up < 10; up++) {
                                        int count = tt.safeLocation(start_loc[0], tmp_y + up, start_loc[2], start_loc[1], start_loc[3], nsob.getWorld(), rsc.getDirection());
                                        if (count == 0) {
                                            nsob.setY(tmp_y + up);
                                            break;
                                        }
                                    }
                                    set.put("world", nsob.getWorld().getName());
                                    set.put("x", nsob.getBlockX());
                                    set.put("y", nsob.getBlockY());
                                    set.put("z", nsob.getBlockZ());
                                    set.put("direction", rsc.getDirection().toString());
                                    set.put("submarine", 0);
                                    qf.doUpdate("next", set, tid);
                                    sender.sendMessage(plugin.pluginName + "The biome was set succesfully. Please release the handbrake!");
                                    plugin.tardisHasDestination.put(id, travel);
                                    if (plugin.trackRescue.containsKey(Integer.valueOf(id))) {
                                        plugin.trackRescue.remove(Integer.valueOf(id));
                                    }
                                }
                            } catch (IllegalArgumentException iae) {
                                sender.sendMessage(plugin.pluginName + "Biome type not valid!");
                                return true;
                            }
                            return true;
                        }
                    }
                    if (args.length == 2 && args[0].equalsIgnoreCase("dest")) {
                        // we're thinking this is a saved destination name
                        HashMap<String, Object> whered = new HashMap<String, Object>();
                        whered.put("dest_name", args[1]);
                        whered.put("tardis_id", id);
                        ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
                        if (!rsd.resultSet()) {
                            sender.sendMessage(plugin.pluginName + "Could not find a destination with that name! try using " + ChatColor.GREEN + "/TARDIS list saves" + ChatColor.RESET + " first.");
                            return true;
                        }
                        World w = plugin.getServer().getWorld(rsd.getWorld());
                        if (w != null) {
                            Location save_dest = new Location(w, rsd.getX(), rsd.getY(), rsd.getZ());
                            respect = new TARDISPluginRespect(plugin);
                            if (!respect.getRespect(player, save_dest, true)) {
                                return true;
                            }
                            if (!plugin.ta.areaCheckInExisting(save_dest)) {
                                // save is in a TARDIS area, so check that the spot is not occupied
                                HashMap<String, Object> wheres = new HashMap<String, Object>();
                                wheres.put("world", rsd.getWorld());
                                wheres.put("x", rsd.getX());
                                wheres.put("y", rsd.getY());
                                wheres.put("z", rsd.getZ());
                                ResultSetCurrentLocation rsz = new ResultSetCurrentLocation(plugin, wheres);
                                if (rsz.resultSet()) {
                                    sender.sendMessage(plugin.pluginName + "A TARDIS already occupies this parking spot! Try using the " + ChatColor.AQUA + "/tardistravel area [name]" + ChatColor.RESET + " command instead.");
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
                                    player.sendMessage(plugin.pluginName + MESSAGE.NO_CURRENT.getText());
                                    return true;
                                }
                                set.put("direction", rsc.getDirection().toString());
                            }
                            set.put("submarine", (rsd.isSubmarine()) ? 1 : 0);
                            qf.doUpdate("next", set, tid);
                            sender.sendMessage(plugin.pluginName + "The specified location was set succesfully. Please release the handbrake!");
                            plugin.tardisHasDestination.put(id, travel);
                            if (plugin.trackRescue.containsKey(Integer.valueOf(id))) {
                                plugin.trackRescue.remove(Integer.valueOf(id));
                            }
                            return true;
                        } else {
                            sender.sendMessage(plugin.pluginName + "Could not get the world for this save!");
                            return true;
                        }
                    }
                    if (args.length == 2 && args[0].equalsIgnoreCase("area")) {
                        // we're thinking this is admin defined area name
                        HashMap<String, Object> wherea = new HashMap<String, Object>();
                        wherea.put("area_name", args[1]);
                        ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false);
                        if (!rsa.resultSet()) {
                            sender.sendMessage(plugin.pluginName + "Could not find an area with that name! try using " + ChatColor.GREEN + "/tardis list areas" + ChatColor.RESET + " first.");
                            return true;
                        }
                        if ((!player.hasPermission("tardis.area." + args[1]) && !player.hasPermission("tardis.area.*")) || (!player.isPermissionSet("tardis.area." + args[1]) && !player.isPermissionSet("tardis.area.*"))) {
                            sender.sendMessage(plugin.pluginName + "You do not have permission [tardis.area." + args[1] + "] to send the TARDIS to this location!");
                            return true;
                        }
                        Location l = plugin.ta.getNextSpot(rsa.getArea_name());
                        if (l == null) {
                            sender.sendMessage(plugin.pluginName + "All available parking spots are taken in this area!");
                            return true;
                        }
                        set.put("world", l.getWorld().getName());
                        set.put("x", l.getBlockX());
                        set.put("y", l.getBlockY());
                        set.put("z", l.getBlockZ());
                        set.put("submarine", 0);
                        qf.doUpdate("next", set, tid);
                        sender.sendMessage(plugin.pluginName + "Your TARDIS was approved for parking in [" + args[1] + "]!");
                        plugin.tardisHasDestination.put(id, travel);
                        if (plugin.trackRescue.containsKey(Integer.valueOf(id))) {
                            plugin.trackRescue.remove(Integer.valueOf(id));
                        }
                        return true;
                    }
                    if (args.length == 3 && args[0].startsWith("~") && player.hasPermission("tardis.timetravel.location")) {
                        HashMap<String, Object> wherecl = new HashMap<String, Object>();
                        wherecl.put("tardis_id", id);
                        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                        if (!rsc.resultSet()) {
                            player.sendMessage(plugin.pluginName + MESSAGE.NO_CURRENT.getText());
                            return true;
                        }
                        if (rsc.isSubmarine()) {
                            player.sendMessage(plugin.pluginName + "You cannot use this command while under water!");
                            return true;
                        }
                        // check args
                        int rx = getRelativeCoordinate(args[0]);
                        int ry = getRelativeCoordinate(args[1]);
                        int rz = getRelativeCoordinate(args[2]);
                        if (rx == Integer.MAX_VALUE || ry == Integer.MAX_VALUE || rz == Integer.MAX_VALUE) {
                            player.sendMessage(plugin.pluginName + "Could not get relative location! Check all arguments start with '~'.");
                            return true;
                        }
                        // add relative coordinates
                        int x = rsc.getX() + rx;
                        int y = rsc.getY() + ry;
                        int z = rsc.getZ() + rz;
                        // make location
                        Location location = new Location(rsc.getWorld(), x, y, z);
                        plugin.debug("relative location: " + location);
                        // check location
                        int count = this.checkLocation(location, player, id, tt);
                        if (count > 0) {
                            sender.sendMessage(plugin.pluginName + "The specified location would not be safe! Please try another.");
                            return true;
                        } else {
                            set.put("world", location.getWorld().getName());
                            set.put("x", location.getBlockX());
                            set.put("y", location.getBlockY());
                            set.put("z", location.getBlockZ());
                            set.put("submarine", 0);
                            qf.doUpdate("next", set, tid);
                            sender.sendMessage(plugin.pluginName + "The specified location was saved succesfully. Please release the handbrake!");
                            plugin.tardisHasDestination.put(id, travel);
                            if (plugin.trackRescue.containsKey(Integer.valueOf(id))) {
                                plugin.trackRescue.remove(Integer.valueOf(id));
                            }
                            return true;
                        }
                    }
                    if (args.length > 2 && args.length < 4) {
                        sender.sendMessage(plugin.pluginName + "Too few command arguments for co-ordinates travel!");
                        return false;
                    }
                    if (args.length >= 4 && player.hasPermission("tardis.timetravel.location")) {
                        String w_str = args[0];
                        if (w_str.contains("'")) {
                            w_str = getQuotedString(args);
                        }
                        // must be a location then
                        int x, y, z;
                        World w = plugin.getServer().getWorld(w_str);
                        if (w == null) {
                            sender.sendMessage(plugin.pluginName + "Cannot find the specified world! Make sure you typed it correctly.");
                            return true;
                        }
                        if (!plugin.getConfig().getBoolean("worlds." + w.getName())) {
                            sender.sendMessage(plugin.pluginName + "The server does not allow time travel to this world!");
                            return true;
                        }
                        if (!plugin.getConfig().getBoolean("travel.include_default_world") && plugin.getConfig().getBoolean("creation.default_world") && args[0].equals(plugin.getConfig().getString("creation.default_world_name"))) {
                            sender.sendMessage(plugin.pluginName + "The server does not allow time travel to this world!");
                            return true;
                        }
                        x = plugin.utils.parseInt(args[args.length - 3]);
                        y = plugin.utils.parseInt(args[args.length - 2]);
                        if (y == 0) {
                            sender.sendMessage(plugin.pluginName + "Y coordinate must be > 0!");
                            return true;
                        }
                        z = plugin.utils.parseInt(args[args.length - 1]);
                        Location location = new Location(w, x, y, z);
                        // check location
                        int count = this.checkLocation(location, player, id, tt);
                        if (count > 0) {
                            sender.sendMessage(plugin.pluginName + "The specified location would not be safe! Please try another.");
                            return true;
                        } else {
                            set.put("world", location.getWorld().getName());
                            set.put("x", location.getBlockX());
                            set.put("y", location.getBlockY());
                            set.put("z", location.getBlockZ());
                            set.put("submarine", 0);
                            qf.doUpdate("next", set, tid);
                            sender.sendMessage(plugin.pluginName + "The specified location was saved succesfully. Please release the handbrake!");
                            plugin.tardisHasDestination.put(id, travel);
                            if (plugin.trackRescue.containsKey(Integer.valueOf(id))) {
                                plugin.trackRescue.remove(Integer.valueOf(id));
                            }
                            return true;
                        }
                    } else {
                        sender.sendMessage(plugin.pluginName + "You do not have permission to use co-ordinates time travel!");
                        return true;
                    }
                }
            } else {
                sender.sendMessage(plugin.pluginName + MESSAGE.NO_PERMS.getText());
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
            p.sendMessage(plugin.pluginName + MESSAGE.NO_CURRENT.getText());
            return null;
        }
        Location l = null;
        // get a world
        // Assume all non-nether/non-end world environments are NORMAL
        if (w != null && !w.getEnvironment().equals(Environment.NETHER) && !w.getEnvironment().equals(Environment.THE_END)) {
            int limitx = 30000;
            int limitz = 30000;
            if (plugin.pm.isPluginEnabled("WorldBorder")) {
                // get the border limit for this world
                TARDISWorldBorderChecker wb = new TARDISWorldBorderChecker(plugin);
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
                                p.sendMessage(plugin.pluginName + b.toString() + " biome found in an easterly direction!");
                                return new Location(w, east, w.getHighestBlockYAt(east, startz), startz);
                            }
                        }
                        break;
                    case 1:
                        // south
                        for (int south = startz; south < limitz; south += step) {
                            Biome chkb = w.getBiome(startx, south);
                            if (chkb.equals(b)) {
                                p.sendMessage(plugin.pluginName + b.toString() + " biome found in a southerly direction!");
                                return new Location(w, startx, w.getHighestBlockYAt(startx, south), south);
                            }
                        }
                        break;
                    case 2:
                        // west
                        for (int west = startx; west > -limitx; west -= step) {
                            Biome chkb = w.getBiome(west, startz);
                            if (chkb.equals(b)) {
                                p.sendMessage(plugin.pluginName + b.toString() + " biome found in a westerly direction!");
                                return new Location(w, west, w.getHighestBlockYAt(west, startz), startz);
                            }
                        }
                        break;
                    case 3:
                        // north
                        for (int north = startz; north > -limitz; north -= step) {
                            Biome chkb = w.getBiome(startx, north);
                            if (chkb.equals(b)) {
                                p.sendMessage(plugin.pluginName + b.toString() + " biome found in a northerly direction!");
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
        if (!plugin.ta.areaCheckInExisting(location)) {
            player.sendMessage(plugin.pluginName + "The location is in a TARDIS area! Please use " + ChatColor.AQUA + "/tardistravel area [area name]");
            return 1;
        }
        respect = new TARDISPluginRespect(plugin);
        if (!respect.getRespect(player, location, true)) {
            return 1;
        }
        HashMap<String, Object> wherecl = new HashMap<String, Object>();
        wherecl.put("tardis_id", id);
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
        if (!rsc.resultSet()) {
            player.sendMessage(plugin.pluginName + MESSAGE.NO_CURRENT.getText());
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
