package me.eccentric_nz.plugins.TARDIS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TARDISTravelCommands implements CommandExecutor {

    private TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

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
                    sender.sendMessage(plugin.pluginName + " Too few command arguments!");
                    return false;
                }
                TARDISTimetravel tt = new TARDISTimetravel(plugin);
                // get tardis_id & direction
                try {
                    Connection connection = service.getConnection();
                    Statement statement = connection.createStatement();
                    String queryDirection = "SELECT tardis.tardis_id, tardis.direction, travellers.player FROM tardis, travellers WHERE tardis.owner = '" + player.getName() + "' AND tardis.owner = travellers.player";
                    ResultSet rs = statement.executeQuery(queryDirection);
                    if (rs == null || !rs.next()) {
                        sender.sendMessage(plugin.pluginName + " Either you are not a Timelord, or you are not inside your TARDIS. You need to be both to run this command!");
                        return true;
                    }
                    int id = rs.getInt("tardis_id");
                    TARDISConstants.COMPASS d = TARDISConstants.COMPASS.valueOf(rs.getString("direction"));
                    rs.close();
                    if (player.hasPermission("tardis.exile")) {
                        // get the exile area
                        String permArea = plugin.ta.getExileArea(player);
                        player.sendMessage(plugin.pluginName + ChatColor.RED + " Notice:" + ChatColor.RESET + " Your travel has been restricted to the [" + permArea + "] area!");
                        Location l = plugin.ta.getNextSpot(permArea);
                        if (l == null) {
                            player.sendMessage(plugin.pluginName + " All available parking spots are taken in this area!");
                        }
                        String save_loc = l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
                        String querySave = "UPDATE tardis SET save = '" + save_loc + "' WHERE tardis_id = " + id;
                        statement.executeUpdate(querySave);
                        player.sendMessage(plugin.pluginName + " Your TARDIS was approved for parking in [" + permArea + "]!");
                    } else {
                        if (args.length == 1) {
                            // we're thinking this is a player's name or home
                            if (args[0].equalsIgnoreCase("home")) {
                                String querySave = "UPDATE tardis SET save = home WHERE tardis_id = " + id;
                                statement.executeUpdate(querySave);
                                sender.sendMessage(plugin.pluginName + " Home location loaded succesfully. Please exit the TARDIS!");
                                plugin.utils.updateTravellerCount(id);
                                return true;
                            } else {
                                if (player.hasPermission("tardis.timetravel.player")) {
                                    if (plugin.getServer().getPlayer(args[0]) == null) {
                                        sender.sendMessage(plugin.pluginName + " That player is not online!");
                                        return true;
                                    }
                                    Player destPlayer = plugin.getServer().getPlayer(args[0]);
                                    Location player_loc = destPlayer.getLocation();
                                    World w = player_loc.getWorld();
                                    int[] start_loc = tt.getStartLocation(player_loc, d);
                                    int count = tt.safeLocation(start_loc[0] - 3, player_loc.getBlockY(), start_loc[2], start_loc[1], start_loc[3], w, d);
                                    if (count > 0) {
                                        sender.sendMessage(plugin.pluginName + " The player's location would not be safe! Please tell the player to move!");
                                        return true;
                                    }
                                    if (plugin.worldGuardOnServer && plugin.getConfig().getBoolean("respect_worldguard")) {
                                        if (plugin.wgchk.cantBuild(player, player_loc)) {
                                            sender.sendMessage(plugin.pluginName + "That location is protected by WorldGuard!");
                                            return true;
                                        }
                                    }
                                    if (player.hasPermission("tardis.exile")) {
                                        String areaPerm = plugin.ta.getExileArea(player);
                                        if (plugin.ta.areaCheckInExile(areaPerm, player_loc)) {
                                            sender.sendMessage(plugin.pluginName + "Your exile status does not allow you to go to this player's location!");
                                            return true;
                                        }
                                    }
                                    if (plugin.ta.areaCheckLocPlayer(player, player_loc)) {
                                        sender.sendMessage(plugin.pluginName + "You do not have permission [" + plugin.trackPerm.get(player.getName()) + "] to bring the TARDIS to this location!");
                                        plugin.trackPerm.remove(player.getName());
                                        return true;
                                    }
                                    String save_loc = player_loc.getWorld().getName() + ":" + (player_loc.getBlockX() - 3) + ":" + player_loc.getBlockY() + ":" + player_loc.getBlockZ();
                                    String querySave = "UPDATE tardis SET save = '" + save_loc + "' WHERE tardis_id = " + id;
                                    statement.executeUpdate(querySave);
                                    sender.sendMessage(plugin.pluginName + " The player location was saved succesfully. Please exit the TARDIS!");
                                    plugin.utils.updateTravellerCount(id);
                                    return true;
                                } else {
                                    sender.sendMessage(plugin.pluginName + " You do not have permission to time travel to a player!");
                                    return true;
                                }
                            }
                        }
                        if (args.length == 2 && args[0].equalsIgnoreCase("dest")) {
                            // we're thinking this is a saved destination name
                            String queryGetDest = "SELECT * FROM destinations WHERE tardis_id = " + id + " AND dest_name = '" + args[1] + "'";
                            ResultSet rsDest = statement.executeQuery(queryGetDest);
                            if (!rsDest.next()) {
                                sender.sendMessage(plugin.pluginName + " Could not find a destination with that name! try using " + ChatColor.GREEN + "/TARDIS list saves" + ChatColor.RESET + " first.");
                                return true;
                            }
                            String save_loc = rsDest.getString("world") + ":" + rsDest.getInt("x") + ":" + rsDest.getInt("y") + ":" + rsDest.getInt("z");
                            String querySave = "UPDATE tardis SET save = '" + save_loc + "' WHERE tardis_id = " + id;
                            statement.executeUpdate(querySave);
                            sender.sendMessage(plugin.pluginName + " The specified location was set succesfully. Please exit the TARDIS!");
                            plugin.utils.updateTravellerCount(id);
                            return true;
                        }
                        if (args.length == 2 && args[0].equalsIgnoreCase("area")) {
                            // we're thinking this is admin defined area name
                            String queryGetArea = "SELECT area_name FROM areas WHERE area_name = '" + args[1] + "'";
                            ResultSet rsArea = statement.executeQuery(queryGetArea);
                            if (!rsArea.next()) {
                                sender.sendMessage(plugin.pluginName + " Could not find an area with that name! try using " + ChatColor.GREEN + "/TARDIS list areas" + ChatColor.RESET + " first.");
                                return true;
                            }
                            if (!player.hasPermission("tardis.area." + args[1]) || !player.isPermissionSet("tardis.area." + args[1])) {
                                sender.sendMessage(plugin.pluginName + "You do not have permission [tardis.area." + args[1] + "] to send the TARDIS to this location!");
                                return true;
                            }
                            Location l = plugin.ta.getNextSpot(rsArea.getString("area_name"));
                            if (l == null) {
                                sender.sendMessage(plugin.pluginName + " All available parking spots are taken in this area!");
                                return true;
                            }
                            String save_loc = l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
                            String querySave = "UPDATE tardis SET save = '" + save_loc + "' WHERE tardis_id = " + id;
                            statement.executeUpdate(querySave);
                            sender.sendMessage(plugin.pluginName + " Your TARDIS was approved for parking in [" + args[1] + "]!");
                            plugin.utils.updateTravellerCount(id);
                            return true;
                        }
                        if (args.length > 2 && args.length < 4) {
                            sender.sendMessage(plugin.pluginName + " Too few command arguments for co-ordinates travel!");
                            return false;
                        }
                        if (args.length == 4 && player.hasPermission("tardis.timetravel.location")) {
                            // must be a location then
                            int x, y, z;
                            World w = plugin.getServer().getWorld(args[0]);
                            if (!plugin.getConfig().getBoolean("include_default_world") && plugin.getConfig().getBoolean("default_world") && args[0].equals(plugin.getConfig().getString("default_world_name"))) {
                                sender.sendMessage(plugin.pluginName + " The server admin does not allow time travel to this world!");
                                return true;
                            }
                            x = plugin.utils.parseNum(args[1]);
                            y = plugin.utils.parseNum(args[2]);
                            z = plugin.utils.parseNum(args[3]);
                            Block block = w.getBlockAt(x, y, z);
                            Location location = block.getLocation();
                            if (plugin.worldGuardOnServer && plugin.getConfig().getBoolean("respect_worldguard")) {
                                if (plugin.wgchk.cantBuild(player, location)) {
                                    sender.sendMessage(plugin.pluginName + "That location is protected by WorldGuard!");
                                    return true;
                                }
                            }
                            if (player.hasPermission("tardis.exile")) {
                                String areaPerm = plugin.ta.getExileArea(player);
                                if (plugin.ta.areaCheckInExile(areaPerm, location)) {
                                    sender.sendMessage(plugin.pluginName + "Your exile status does not allow you to bring the TARDIS to this location!");
                                    return true;
                                }
                            }
                            if (plugin.ta.areaCheckLocPlayer(player, location)) {
                                sender.sendMessage(plugin.pluginName + "You do not have permission [" + plugin.trackPerm.get(player.getName()) + "] to send the TARDIS to this location!");
                                plugin.trackPerm.remove(player.getName());
                                return true;
                            }
                            // check location
                            int[] start_loc = tt.getStartLocation(location, d);
                            int count = tt.safeLocation(start_loc[0], location.getBlockY(), start_loc[2], start_loc[1], start_loc[3], w, d);
                            if (count > 0) {
                                sender.sendMessage(plugin.pluginName + " The specified location would not be safe! Please try another.");
                                return true;
                            } else {
                                String save_loc = location.getWorld().getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ();
                                String querySave = "UPDATE tardis SET save = '" + save_loc + "' WHERE tardis_id = " + id;
                                statement.executeUpdate(querySave);
                                sender.sendMessage(plugin.pluginName + " The specified location was saved succesfully. Please exit the TARDIS!");
                                plugin.utils.updateTravellerCount(id);
                                return true;
                            }
                        } else {
                            sender.sendMessage(plugin.pluginName + " You do not have permission to use co-ordinates time travel!");
                            return true;
                        }
                    }
                    statement.close();
                } catch (SQLException e) {
                    plugin.console.sendMessage(plugin.pluginName + " /TARDIS travel to location Error: " + e);
                }
                return true;
            } else {
                sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                return false;
            }
        }
        return false;
    }
}