package me.eccentric_nz.plugins.TARDIS;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class TARDISAdminCommands implements CommandExecutor {

    private TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISAdminCommands(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If the player typed /tardis then do the following...
        // check there is the right number of arguments
        if (cmd.getName().equalsIgnoreCase("tardisadmin")) {
            if (sender instanceof ConsoleCommandSender || sender.hasPermission("tardis.admin")) {
                if (args.length == 0) {
                    sender.sendMessage(Constants.COMMAND_ADMIN.split("\n"));
                    return true;
                }
                if (!args[0].equalsIgnoreCase("timeout") &&
                    !args[0].equalsIgnoreCase("timeout_height") &&
                	!args[0].equalsIgnoreCase("name_tardis") &&
                	!args[0].equalsIgnoreCase("reload") &&
                	!args[0].equalsIgnoreCase("config") &&
                	!args[0].equalsIgnoreCase("key") &&
                	!args[0].equalsIgnoreCase("bonus_chest") &&
                	!args[0].equalsIgnoreCase("protect_blocks") &&
                	!args[0].equalsIgnoreCase("give_key") &&
                	!args[0].equalsIgnoreCase("platform") &&
                	!args[0].equalsIgnoreCase("tp_radius") &&
                	!args[0].equalsIgnoreCase("require_spout") &&
                	!args[0].equalsIgnoreCase("default_world") &&
                	!args[0].equalsIgnoreCase("default_world_name") &&
                	!args[0].equalsIgnoreCase("include_default_world") &&
                	!args[0].equalsIgnoreCase("exclude") &&
                	!args[0].equalsIgnoreCase("sfx") &&
                	!args[0].equalsIgnoreCase("use_worldguard") &&
                	!args[0].equalsIgnoreCase("respect_worldguard") &&
                	!args[0].equalsIgnoreCase("nether") &&
                	!args[0].equalsIgnoreCase("the_end") &&
                	!args[0].equalsIgnoreCase("land_on_water") &&
                	!args[0].equalsIgnoreCase("updatesaves") &&
                	!args[0].equalsIgnoreCase("delete") &&
                	!args[0].equalsIgnoreCase("find") &&
                	!args[0].equalsIgnoreCase("list") &&
                	!args[0].equalsIgnoreCase("debug")) {
                    sender.sendMessage(Constants.MY_PLUGIN_NAME + " TARDIS does not recognise that command argument!");
                    return false;
                }
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        plugin.loadConfig();
                        sender.sendMessage(Constants.MY_PLUGIN_NAME + " TARDIS config reloaded.");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("config")) {
                        Set<String> configNames = plugin.config.getKeys(false);
                        sender.sendMessage(Constants.MY_PLUGIN_NAME + ChatColor.RED + " Here are the current plugin config options!");
                        for (String cname : configNames) {
                            String value = plugin.config.getString(cname);
                            if (cname.equals("worlds")) {
                                sender.sendMessage(ChatColor.AQUA + cname + ":" + ChatColor.RESET);
                                Set<String> worldNames = plugin.config.getConfigurationSection("worlds").getKeys(false);
                                for (String wname : worldNames) {
                                    String enabled = plugin.config.getString("worlds." + wname);
                                    sender.sendMessage("      " + ChatColor.GREEN + wname + ": " + ChatColor.RESET + enabled);
                                }
                            } else {
                                sender.sendMessage(ChatColor.AQUA + cname + ": " + ChatColor.RESET + value);
                            }
                        }
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("updatesaves")) {
                        try {
                            Connection connection = service.getConnection();
                            Statement statement = connection.createStatement();
                            String queryList = "SELECT * FROM tardis";
                            ResultSet rs = statement.executeQuery(queryList);
                            while (rs.next()) {
                                int id = rs.getInt("tardis_id");
                                String[] saves = new String[3];
                                saves[0] = rs.getString("save1");
                                saves[1] = rs.getString("save2");
                                saves[2] = rs.getString("save3");
                                for (String str : saves) {
                                    if (!str.equals("")) {
                                        String[] namesplit = str.split("~");
                                        String dn = namesplit[0];
                                        String[] locsplit = namesplit[1].split(":");
                                        String w = locsplit[0];
                                        String x = locsplit[1];
                                        String y = locsplit[2];
                                        String z = locsplit[3];
                                        String queryDest = "INSERT INTO destinations (tardis_id,dest_name,world,x,y,z) VALUES (" + id + ",'" + dn + "','" + w + "'," + x + "," + y + "," + z + ")";
                                        statement.executeUpdate(queryDest);
                                    }
                                }
                            }
                            rs.close();
                            statement.close();
                        } catch (SQLException e) {
                            System.err.println(Constants.MY_PLUGIN_NAME + " Console saves to destinations error: " + e);
                        }
                        sender.sendMessage(Constants.MY_PLUGIN_NAME + "TARDIS saves updated.");
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("list")) {
                    // get all tardis positions - max 18
                    int start = 0, end = 18;
                    if (args.length > 1) {
                        int tmp = Integer.parseInt(args[1]);
                        start = (tmp * 18) - 18;
                        end = tmp * 18;
                    }
                    try {
                        Connection connection = service.getConnection();
                        Statement statement = connection.createStatement();
                        String queryList = "SELECT owner, current FROM tardis LIMIT " + start + ", " + end;
                        ResultSet rsList = statement.executeQuery(queryList);
                        if (rsList.isBeforeFirst()) {
                            sender.sendMessage(Constants.MY_PLUGIN_NAME + " TARDIS locations.");
                            while (rsList.next()) {
                                sender.sendMessage("Timelord: " + rsList.getString("Owner") + ", Location: " + rsList.getString("current"));
                            }
                            sender.sendMessage(Constants.MY_PLUGIN_NAME + " To see more locations, type: /tardisadmin list 2,  /tardisadmin list 3 etc.");
                        }
                        statement.close();
                    } catch (SQLException e) {
                        System.err.println(Constants.MY_PLUGIN_NAME + " Console saves to destinations error: " + e);
                    }
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(Constants.MY_PLUGIN_NAME + " Too few command arguments!");
                    return false;
                } else {
                    if (args[0].equalsIgnoreCase("delete")) {
                        try {
                            Connection connection = service.getConnection();
                            Statement statement = connection.createStatement();
                            // check the db contains the player name
                            String queryGet = "SELECT tardis_id, chunk, direction, save, current, size FROM tardis WHERE owner = '" + args[1] + "'";
                            System.out.print(queryGet);
                            ResultSet rsGet = statement.executeQuery(queryGet);
                            if (rsGet.next()) {
                                int id = rsGet.getInt("tardis_id");
                                String saveLoc = rsGet.getString("save");
                                String currentLoc = rsGet.getString("current");
                                Constants.SCHEMATIC schm = Constants.SCHEMATIC.valueOf(rsGet.getString("size"));
                                Constants.COMPASS d = Constants.COMPASS.valueOf(rsGet.getString("direction"));
                                String chunkLoc = rsGet.getString("chunk");
                                String[] cdata = chunkLoc.split(":");
                                World cw = plugin.getServer().getWorld(cdata[0]);
                                World.Environment env = cw.getEnvironment();
                                int restore;
                                switch (env) {
                                    case NETHER:
                                        restore = 87;
                                        break;
                                    case THE_END:
                                        restore = 121;
                                        break;
                                    default:
                                        restore = 1;
                                }
                                // check if player is in the TARDIS
                                String queryTravellers = "SELECT player FROM travellers WHERE tardis_id = " + id;
                                ResultSet rsTravellers = statement.executeQuery(queryTravellers);
                                boolean useCurrent = false;
                                if (rsTravellers.isBeforeFirst()) {
                                    useCurrent = true;
                                    Location spawn = cw.getSpawnLocation();
                                    while (rsTravellers.next()) {
                                        String op = plugin.getServer().getOfflinePlayer(rsTravellers.getString("player")).getName();
                                        // teleport offline player to spawn
                                        plugin.iopHandler.setLocation(op, spawn);
                                    }
                                    String queryDelTravellers = "DELETE FROM travellers WHERE tardis_id = " + id;
                                    statement.executeUpdate(queryDelTravellers);
                                }
                                // need to determine if we use the save location or the current location
                                Location bb_loc = (useCurrent) ? Constants.getLocationFromDB(currentLoc, 0, 0) : Constants.getLocationFromDB(saveLoc, 0, 0);
                                // destroy the TARDIS
                                plugin.destroyer.destroyTorch(bb_loc);
                                plugin.destroyer.destroySign(bb_loc, d);
                                plugin.destroyer.destroyTARDIS(schm, id, cw, d, restore, args[1]);
                                if (cw.getWorldType() == WorldType.FLAT) {
                                    // replace stone blocks with AIR
                                    plugin.destroyer.destroyTARDIS(schm, id, cw, d, 0, args[1]);
                                }
                                plugin.destroyer.destroyBlueBox(bb_loc, d, id, false);
                                // delete the TARDIS from the db
                                String queryDeleteChunk = "DELETE FROM chunks WHERE tardis_id = " + id;
                                statement.executeUpdate(queryDeleteChunk);
                                String queryDelTardis = "DELETE FROM tardis WHERE tardis_id = " + id;
                                statement.executeUpdate(queryDelTardis);
                                String queryDeleteDoors = "DELETE FROM doors WHERE tardis_id = " + id;
                                statement.executeUpdate(queryDeleteDoors);
                                sender.sendMessage(Constants.MY_PLUGIN_NAME + " The TARDIS was removed from the world and database successfully.");
                            } else {
                                sender.sendMessage(Constants.MY_PLUGIN_NAME + " Could not find player [" + args[1] + "] in the database!");
                                return true;
                            }
                            statement.close();
                        } catch (SQLException e) {
                            System.err.println(Constants.MY_PLUGIN_NAME + " Admin delete TARDIS error: " + e);
                        }
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("key")) {
                        String setMaterial = args[1].toUpperCase(Locale.ENGLISH);
                        if (!Arrays.asList(Materials.MATERIAL_LIST).contains(setMaterial)) {
                            sender.sendMessage(Constants.MY_PLUGIN_NAME + ChatColor.RED + "That is not a valid Material! Try checking http://jd.bukkit.org/apidocs/org/bukkit/Material.html");
                            return false;
                        } else {
                            plugin.config.set("key", setMaterial);
                            Constants.TARDIS_KEY = setMaterial;
                        }
                    }
                    if (args[0].equalsIgnoreCase("default_world_name")) {
                        // get world name
                        int count = args.length;
                        StringBuilder buf = new StringBuilder();
                        for (int i = 1; i < count; i++) {
                            buf.append(args[i]).append(" ");
                        }
                        String tmp = buf.toString();
                        String t = tmp.substring(0, tmp.length() - 1);
                        // need to make there are no periods(.) in the text
                        String nodots = StringUtils.replace(t, ".", "_");
                        plugin.config.set("default_world_name", nodots);
                    }
                    if (args[0].equalsIgnoreCase("exclude")) {
                        // get world name
                        int count = args.length;
                        StringBuilder buf = new StringBuilder();
                        for (int i = 1; i < count; i++) {
                            buf.append(args[i]).append(" ");
                        }
                        String tmp = buf.toString();
                        String t = tmp.substring(0, tmp.length() - 1);
                        // need to make there are no periods(.) in the text
                        String nodots = StringUtils.replace(t, ".", "_");
                        // check the world actually exists!
                        if (plugin.getServer().getWorld(nodots) == null) {
                            sender.sendMessage(Constants.MY_PLUGIN_NAME + ChatColor.RED + "World does not exist!");
                            return false;
                        }
                        plugin.config.set("worlds." + nodots, false);
                    }
                  //checks if its a boolean config option
                    if (args[0].equalsIgnoreCase("name_tardis") ||
                    	args[0].equalsIgnoreCase("land_on_water") ||
                    	args[0].equalsIgnoreCase("the_end") ||
                    	args[0].equalsIgnoreCase("nether") ||
                    	args[0].equalsIgnoreCase("use_worldguard") ||
                    	args[0].equalsIgnoreCase("respect_worldguard") ||
                    	args[0].equalsIgnoreCase("sfx") ||
                    	args[0].equalsIgnoreCase("include") ||
                    	args[0].equalsIgnoreCase("default_world") ||
                    	args[0].equalsIgnoreCase("require_spout") ||
                    	args[0].equalsIgnoreCase("platform") ||
                    	args[0].equalsIgnoreCase("give_key") ||
                    	args[0].equalsIgnoreCase("protect_blocks") ||
                    	args[0].equalsIgnoreCase("debug") ||
                    	args[0].equalsIgnoreCase("bonus_chest")){
                        // check they typed true of false
                        String tf = args[1].toLowerCase();
                        if (!tf.equals("true") && !tf.equals("false")) {
                            sender.sendMessage(Constants.MY_PLUGIN_NAME + ChatColor.RED + "The last argument must be true or false!");
                            return false;
                        }
                        plugin.config.set(args[0].toLowerCase(), Boolean.valueOf(tf));
                    }
                    //checks if its a number config option
                    if (args[0].equalsIgnoreCase("timeout") ||
                    	args[0].equalsIgnoreCase("timeout_height") ||
                    	args[0].equalsIgnoreCase("tp_radius")) {
                        String a = args[1];
                        int val;
                        try {
                            val = Integer.parseInt(a);
                        } catch (NumberFormatException nfe) {
                            // not a number
                            sender.sendMessage(Constants.MY_PLUGIN_NAME + ChatColor.RED + " The last argument must be a number!");
                            return false;
                        }
                        plugin.config.set(args[0].toLowerCase(), val);
                    }
                    try {
                        plugin.config.save(plugin.myconfigfile);
                        sender.sendMessage(Constants.MY_PLUGIN_NAME + " The config was updated!");
                    } catch (IOException e) {
                        sender.sendMessage(Constants.MY_PLUGIN_NAME + " There was a problem saving the config file!");
                    }
                }
                return true;
            } else {
                sender.sendMessage(Constants.MY_PLUGIN_NAME + ChatColor.RED + " You must be an Admin to run this command.");
                return false;
            }
        }
        return false;
    }
}
