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

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.travel.TARDISPluginRespect;
import me.eccentric_nz.TARDIS.travel.TARDISRescue;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

/**
 * Command /tardistravel [arguments].
 *
 * Time travel is the process of travelling through time, even in a non-linear
 * direction.
 *
 * @author eccentric_nz
 */
public class TARDISTravelCommands implements CommandExecutor, TabCompleter {

    private TARDIS plugin;
    private TARDISPluginRespect respect;
    private final List<String> ROOT_SUBS = ImmutableList.of("home", "dest", "area");

    public TARDISTravelCommands(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        // If the player typed /tardis then do the following...
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
                    sender.sendMessage(plugin.pluginName + "You are not a Timelord. You need to create a TARDIS before using this command!");
                    return true;
                }
                int id = rs.getTardis_id();
                if (!rs.isHandbrake_on()) {
                    player.sendMessage(plugin.pluginName + ChatColor.RED + "You cannot set a destination while the TARDIS is travelling!");
                    return true;
                }
                HashMap<String, Object> wheret = new HashMap<String, Object>();
                wheret.put("player", player.getName());
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
                if (!rst.resultSet()) {
                    sender.sendMessage(plugin.pluginName + "You are not inside the TARDIS. You need to be to run this command!");
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
                    player.sendMessage(plugin.pluginName + ChatColor.RED + "The TARDIS does not have enough Artron Energy to make this trip!");
                    return true;
                }
                TARDISConstants.COMPASS d = rs.getDirection();
                String home = rs.getHome();
                HashMap<String, Object> tid = new HashMap<String, Object>();
                HashMap<String, Object> set = new HashMap<String, Object>();
                tid.put("tardis_id", id);
                if (player.hasPermission("tardis.exile") && plugin.getConfig().getBoolean("exile")) {
                    // get the exile area
                    String permArea = plugin.ta.getExileArea(player);
                    player.sendMessage(plugin.pluginName + ChatColor.RED + " Notice:" + ChatColor.RESET + " Your travel has been restricted to the [" + permArea + "] area!");
                    Location l = plugin.ta.getNextSpot(permArea);
                    if (l == null) {
                        player.sendMessage(plugin.pluginName + "All available parking spots are taken in this area!");
                        return true;
                    }
                    String save_loc = l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
                    set.put("save", save_loc);
                    qf.doUpdate("tardis", set, tid);
                    player.sendMessage(plugin.pluginName + "Your TARDIS was approved for parking in [" + permArea + "]!");
                    plugin.tardisHasDestination.put(id, travel);
                    if (plugin.trackRescue.containsKey(Integer.valueOf(id))) {
                        plugin.trackRescue.remove(Integer.valueOf(id));
                    }
                    return true;
                } else {
                    if (args.length == 1) {
                        // we're thinking this is a player's name or home
                        if (args[0].equalsIgnoreCase("home")) {
                            set.put("save", home);
                            qf.doUpdate("tardis", set, tid);
                            sender.sendMessage(plugin.pluginName + "Home location loaded succesfully. Please release the handbrake!");
                            plugin.tardisHasDestination.put(id, travel);
                            if (plugin.trackRescue.containsKey(Integer.valueOf(id))) {
                                plugin.trackRescue.remove(Integer.valueOf(id));
                            }
                            return true;
                        } else {
                            if (player.hasPermission("tardis.timetravel.player")) {
                                TARDISRescue to_player = new TARDISRescue(plugin);
                                return to_player.rescue(player, args[0], id, tt, d, false);
                            } else {
                                player.sendMessage(plugin.pluginName + "You do not have permission to time travel to a player!");
                                return true;
                            }
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
                        String save_loc = rsd.getWorld() + ":" + rsd.getX() + ":" + rsd.getY() + ":" + rsd.getZ();
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
                                wheres.put("save", save_loc);
                                ResultSetTardis rsz = new ResultSetTardis(plugin, wheres, "", true);
                                if (rsz.resultSet()) {
                                    ArrayList<HashMap<String, String>> data = rsz.getData();
                                    if (data.size() > 0) {
                                        sender.sendMessage(plugin.pluginName + "A TARDIS already occupies this parking spot! Try using the " + ChatColor.AQUA + "/tardistravel area [name]" + ChatColor.RESET + " command instead.");
                                        return true;
                                    }
                                }
                            }
                            set.put("save", save_loc);
                            qf.doUpdate("tardis", set, tid);
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
                        if (!player.hasPermission("tardis.area." + args[1]) || !player.isPermissionSet("tardis.area." + args[1])) {
                            sender.sendMessage(plugin.pluginName + "You do not have permission [tardis.area." + args[1] + "] to send the TARDIS to this location!");
                            return true;
                        }
                        Location l = plugin.ta.getNextSpot(rsa.getArea_name());
                        if (l == null) {
                            sender.sendMessage(plugin.pluginName + "All available parking spots are taken in this area!");
                            return true;
                        }
                        String save_loc = l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
                        set.put("save", save_loc);
                        qf.doUpdate("tardis", set, tid);
                        sender.sendMessage(plugin.pluginName + "Your TARDIS was approved for parking in [" + args[1] + "]!");
                        plugin.tardisHasDestination.put(id, travel);
                        if (plugin.trackRescue.containsKey(Integer.valueOf(id))) {
                            plugin.trackRescue.remove(Integer.valueOf(id));
                        }
                        return true;
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
                        if (!plugin.getConfig().getBoolean("include_default_world") && plugin.getConfig().getBoolean("default_world") && args[0].equals(plugin.getConfig().getString("default_world_name"))) {
                            sender.sendMessage(plugin.pluginName + "The server does not allow time travel to this world!");
                            return true;
                        }
                        x = plugin.utils.parseNum(args[args.length - 3]);
                        y = plugin.utils.parseNum(args[args.length - 2]);
                        z = plugin.utils.parseNum(args[args.length - 1]);
                        Block block = w.getBlockAt(x, y, z);
                        Location location = block.getLocation();
                        if (!plugin.ta.areaCheckInExisting(location)) {
                            sender.sendMessage(plugin.pluginName + "The location is in a TARDIS area! Please use " + ChatColor.AQUA + "/tardistravel area [area name]");
                            return true;
                        }
                        respect = new TARDISPluginRespect(plugin);
                        if (!respect.getRespect(player, location, true)) {
                            return true;
                        }
                        // check location
                        int[] start_loc = tt.getStartLocation(location, d);
                        int count = tt.safeLocation(start_loc[0], location.getBlockY(), start_loc[2], start_loc[1], start_loc[3], w, d);
                        if (count > 0) {
                            sender.sendMessage(plugin.pluginName + "The specified location would not be safe! Please try another.");
                            return true;
                        } else {
                            String save_loc = location.getWorld().getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ();
                            set.put("save", save_loc);
                            qf.doUpdate("tardis", set, tid);
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
                sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                return false;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        // Remember that we can return null to default to online player name matching
        String lastArg = args[args.length - 1];
        if (args.length <= 1) {
            List<String> part = partial(args[0], ROOT_SUBS);
            return (part.size() > 0) ? part : null;
        } else if (args.length == 2) {
            String sub = args[0];
            if (sub.equals("area")) {
                return partial(lastArg, getAreas());
            }
        }
        return ImmutableList.of();
    }

    private List<String> partial(String token, Collection<String> from) {
        return StringUtil.copyPartialMatches(token, from, new ArrayList<String>(from.size()));
    }

    private List<String> getAreas() {
        List<String> areas = new ArrayList<String>();
        ResultSetAreas rsa = new ResultSetAreas(plugin, null, true);
        if (rsa.resultSet()) {
            ArrayList<HashMap<String, String>> data = rsa.getData();
            for (HashMap<String, String> map : data) {
                areas.add(map.get("area_name"));
            }
        }
        return areas;
    }

    private String getQuotedString(String[] args) {
        String tmp = "";
        String w_str = "";
        for (String s : args) {
            tmp += s + " ";
        }
        Pattern p = Pattern.compile("'([^']*)'");
        Matcher m = p.matcher(tmp);
        while (m.find()) {
            w_str = m.group(1);
        }
        return w_str;
    }
}
