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

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.travel.TARDISPluginRespect;
import me.eccentric_nz.TARDIS.travel.TARDISTimetravel;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Time travel is the process of travelling through time, even in a non-linear
 * direction.
 *
 * @author eccentric_nz
 */
public class TARDISTravelCommands implements CommandExecutor {

    private TARDIS plugin;
    private TARDISPluginRespect respect;

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
                TARDISTimetravel tt = new TARDISTimetravel(plugin);
                // get tardis data
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("owner", player.getName());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (!rs.resultSet()) {
                    sender.sendMessage(plugin.pluginName + "You are not a Timelord. You need to create a TARDIS before using this command!");
                    return true;
                }
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
                int id = rs.getTardis_id();
                if (tardis_id != id) {
                    sender.sendMessage(plugin.pluginName + "You can only run this command if you are the Timelord of " + ChatColor.LIGHT_PURPLE + "this" + ChatColor.RESET + " TARDIS!");
                    return true;
                }
                int level = rs.getArtron_level();
                int travel = plugin.getConfig().getInt("travel");
                if (level < travel) {
                    player.sendMessage(plugin.pluginName + ChatColor.RED + "The TARDIS does not have enough Artron Energy to make this trip!");
                    return true;
                }
                TARDISConstants.COMPASS d = rs.getDirection();
                String home = rs.getHome();
                HashMap<String, Object> tid = new HashMap<String, Object>();
                HashMap<String, Object> set = new HashMap<String, Object>();
                tid.put("tardis_id", id);
                if (player.hasPermission("tardis.exile")) {
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
                    return true;
                } else {
                    if (args.length == 1) {
                        // we're thinking this is a player's name or home
                        if (args[0].equalsIgnoreCase("home")) {
                            set.put("save", home);
                            qf.doUpdate("tardis", set, tid);
                            sender.sendMessage(plugin.pluginName + "Home location loaded succesfully. Please release the handbrake!");
                            plugin.utils.updateTravellerCount(id);
                            plugin.tardisHasDestination.put(id, travel);
                            return true;
                        } else {
                            if (player.hasPermission("tardis.timetravel.player")) {
                                if (plugin.getServer().getPlayer(args[0]) == null) {
                                    sender.sendMessage(plugin.pluginName + "That player is not online!");
                                    return true;
                                }
                                Player destPlayer = plugin.getServer().getPlayer(args[0]);
                                Location player_loc = destPlayer.getLocation();
                                World w = player_loc.getWorld();
                                int[] start_loc = tt.getStartLocation(player_loc, d);
                                int count = tt.safeLocation(start_loc[0] - 3, player_loc.getBlockY(), start_loc[2], start_loc[1] - 3, start_loc[3], w, d);
                                if (count > 0) {
                                    sender.sendMessage(plugin.pluginName + "The player's location would not be safe! Please tell the player to move!");
                                    return true;
                                }
                                respect = new TARDISPluginRespect(plugin);
                                if (!respect.getRespect(player, player_loc, true)) {
                                    return true;
                                }
                                String save_loc = player_loc.getWorld().getName() + ":" + (player_loc.getBlockX() - 3) + ":" + player_loc.getBlockY() + ":" + player_loc.getBlockZ();
                                set.put("save", save_loc);
                                qf.doUpdate("tardis", set, tid);
                                sender.sendMessage(plugin.pluginName + "The player location was saved succesfully. Please release the handbrake!");
                                plugin.utils.updateTravellerCount(id);
                                plugin.tardisHasDestination.put(id, travel);
                                return true;
                            } else {
                                sender.sendMessage(plugin.pluginName + "You do not have permission to time travel to a player!");
                                return true;
                            }
                        }
                    }
                    if (args.length == 2 && args[0].equalsIgnoreCase("dest")) {
                        // we're thinking this is a saved destination name
                        HashMap<String, Object> whered = new HashMap<String, Object>();
                        whered.put("dest_name", args[1]);
                        ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
                        if (!rsd.resultSet()) {
                            sender.sendMessage(plugin.pluginName + "Could not find a destination with that name! try using " + ChatColor.GREEN + "/TARDIS list saves" + ChatColor.RESET + " first.");
                            return true;
                        }
                        String save_loc = rsd.getWorld() + ":" + rsd.getX() + ":" + rsd.getY() + ":" + rsd.getZ();
                        set.put("save", save_loc);
                        qf.doUpdate("tardis", set, tid);
                        sender.sendMessage(plugin.pluginName + "The specified location was set succesfully. Please release the handbrake!");
                        plugin.utils.updateTravellerCount(id);
                        plugin.tardisHasDestination.put(id, travel);
                        return true;
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
                        plugin.utils.updateTravellerCount(id);
                        plugin.tardisHasDestination.put(id, travel);
                        return true;
                    }
                    if (args.length > 2 && args.length < 4) {
                        sender.sendMessage(plugin.pluginName + "Too few command arguments for co-ordinates travel!");
                        return false;
                    }
                    if (args.length == 4 && player.hasPermission("tardis.timetravel.location")) {
                        // must be a location then
                        int x, y, z;
                        World w = plugin.getServer().getWorld(args[0]);
                        if (w == null) {
                            sender.sendMessage(plugin.pluginName + "Cannot find the specified world! Make sure you type it correctly.");
                            return true;
                        }
                        if (!plugin.getConfig().getBoolean("include_default_world") && plugin.getConfig().getBoolean("default_world") && args[0].equals(plugin.getConfig().getString("default_world_name"))) {
                            sender.sendMessage(plugin.pluginName + "The server admin does not allow time travel to this world!");
                            return true;
                        }
                        x = plugin.utils.parseNum(args[1]);
                        y = plugin.utils.parseNum(args[2]);
                        z = plugin.utils.parseNum(args[3]);
                        Block block = w.getBlockAt(x, y, z);
                        Location location = block.getLocation();
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
                            plugin.utils.updateTravellerCount(id);
                            plugin.tardisHasDestination.put(id, travel);
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
}
