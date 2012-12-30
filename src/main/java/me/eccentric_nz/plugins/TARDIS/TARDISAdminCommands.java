package me.eccentric_nz.plugins.TARDIS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private List<String> firstArgsStr = new ArrayList<String>();
    private List<String> firstArgsBool = new ArrayList<String>();
    private List<String> firstArgsInt = new ArrayList<String>();

    public TARDISAdminCommands(TARDIS plugin) {
        this.plugin = plugin;
        // add first arguments
        firstArgsStr.add("key");
        firstArgsStr.add("reload");
        firstArgsStr.add("config");
        firstArgsStr.add("default_world_name");
        firstArgsStr.add("exclude");
        firstArgsStr.add("delete");
        firstArgsStr.add("find");
        firstArgsStr.add("list");
        // boolean
        firstArgsBool.add("debug");
        firstArgsBool.add("bonus_chest");
        firstArgsBool.add("chameleon");
        firstArgsBool.add("default_world");
        firstArgsBool.add("give_key");
        firstArgsBool.add("include_default_world");
        firstArgsBool.add("land_on_water");
        firstArgsBool.add("name_tardis");
        firstArgsBool.add("nether");
        firstArgsBool.add("platform");
        firstArgsBool.add("protect_blocks");
        firstArgsBool.add("sfx");
        firstArgsBool.add("the_end");
        firstArgsBool.add("use_worldguard");
        firstArgsBool.add("respect_worldguard");
        // integer
        firstArgsInt.add("timeout_height");
        firstArgsInt.add("timeout");
        firstArgsInt.add("tp_radius");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If the player typed /tardisadmin then do the following...
        if (cmd.getName().equalsIgnoreCase("tardisadmin")) {
            if (sender instanceof ConsoleCommandSender || sender.hasPermission("tardis.admin")) {
                if (args.length == 0) {
                    sender.sendMessage(TARDISConstants.COMMAND_ADMIN.split("\n"));
                    return true;
                }
                String first = args[0].toLowerCase();
                if (!firstArgsStr.contains(first) && !firstArgsBool.contains(first) && !firstArgsInt.contains(first)) {
                    sender.sendMessage(plugin.pluginName + " TARDIS does not recognise that command argument!");
                    return false;
                }
                if (args.length == 1) {
                    if (first.equals("reload")) {
                        plugin.reloadConfig();
                        sender.sendMessage(plugin.pluginName + " TARDIS config reloaded.");
                        return true;
                    }
                    if (first.equals("config")) {
                        Set<String> configNames = plugin.getConfig().getKeys(false);
                        sender.sendMessage(plugin.pluginName + ChatColor.RED + " Here are the current plugin config options!");
                        for (String cname : configNames) {
                            String value = plugin.getConfig().getString(cname);
                            if (cname.equals("worlds")) {
                                sender.sendMessage(ChatColor.AQUA + cname + ":" + ChatColor.RESET);
                                Set<String> worldNames = plugin.getConfig().getConfigurationSection("worlds").getKeys(false);
                                for (String wname : worldNames) {
                                    String enabled = plugin.getConfig().getString("worlds." + wname);
                                    sender.sendMessage("      " + ChatColor.GREEN + wname + ": " + ChatColor.RESET + enabled);
                                }
                            } else {
                                sender.sendMessage(ChatColor.AQUA + cname + ": " + ChatColor.RESET + value);
                            }
                        }
                        return true;
                    }
                    if (first.equals("updatesaves")) {
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
                            plugin.console.sendMessage(plugin.pluginName + " Console saves to destinations error: " + e);
                        }
                        sender.sendMessage(plugin.pluginName + "TARDIS saves updated.");
                        return true;
                    }
                }
                if (first.equals("list")) {
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
                            sender.sendMessage(plugin.pluginName + " TARDIS locations.");
                            while (rsList.next()) {
                                sender.sendMessage("Timelord: " + rsList.getString("Owner") + ", Location: " + rsList.getString("current"));
                            }
                            sender.sendMessage(plugin.pluginName + " To see more locations, type: /tardisadmin list 2,  /tardisadmin list 3 etc.");
                        }
                        statement.close();
                    } catch (SQLException e) {
                        plugin.console.sendMessage(plugin.pluginName + " Console saves to destinations error: " + e);
                    }
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(plugin.pluginName + " Too few command arguments!");
                    return false;
                } else {
                    if (first.equals("delete")) {
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
                                TARDISConstants.SCHEMATIC schm = TARDISConstants.SCHEMATIC.valueOf(rsGet.getString("size"));
                                TARDISConstants.COMPASS d = TARDISConstants.COMPASS.valueOf(rsGet.getString("direction"));
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
                                Location bb_loc = (useCurrent) ? TARDISConstants.getLocationFromDB(currentLoc, 0, 0) : TARDISConstants.getLocationFromDB(saveLoc, 0, 0);
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
                                sender.sendMessage(plugin.pluginName + " The TARDIS was removed from the world and database successfully.");
                            } else {
                                sender.sendMessage(plugin.pluginName + " Could not find player [" + args[1] + "] in the database!");
                                return true;
                            }
                            statement.close();
                        } catch (SQLException e) {
                            plugin.console.sendMessage(plugin.pluginName + " Admin delete TARDIS error: " + e);
                        }
                        return true;
                    }
                    if (first.equals("key")) {
                        String setMaterial = args[1].toUpperCase(Locale.ENGLISH);
                        if (!Arrays.asList(TARDISMaterials.MATERIAL_LIST).contains(setMaterial)) {
                            sender.sendMessage(plugin.pluginName + ChatColor.RED + "That is not a valid Material! Try checking http://jd.bukkit.org/apidocs/org/bukkit/Material.html");
                            return false;
                        } else {
                            plugin.getConfig().set("key", setMaterial);
                            TARDISConstants.TARDIS_KEY = setMaterial;
                        }
                    }
                    if (first.equals("default_world_name")) {
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
                        plugin.getConfig().set("default_world_name", nodots);
                    }
                    if (first.equals("exclude")) {
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
                            sender.sendMessage(plugin.pluginName + ChatColor.RED + "World does not exist!");
                            return false;
                        }
                        plugin.getConfig().set("worlds." + nodots, false);
                    }
                    //checks if its a boolean config option
                    if (firstArgsBool.contains(first)) {
                        // check they typed true of false
                        String tf = args[1].toLowerCase();
                        if (!tf.equals("true") && !tf.equals("false")) {
                            sender.sendMessage(plugin.pluginName + ChatColor.RED + "The last argument must be true or false!");
                            return false;
                        }
                        plugin.getConfig().set(first, Boolean.valueOf(tf));
                    }
                    //checks if its a number config option
                    if (firstArgsInt.contains(first)) {
                        String a = args[1];
                        int val;
                        try {
                            val = Integer.parseInt(a);
                        } catch (NumberFormatException nfe) {
                            // not a number
                            sender.sendMessage(plugin.pluginName + ChatColor.RED + " The last argument must be a number!");
                            return false;
                        }
                        plugin.getConfig().set(first, val);
                    }
                    plugin.saveConfig();
                    sender.sendMessage(plugin.pluginName + " The config was updated!");
                }
                return true;
            } else {
                sender.sendMessage(plugin.pluginName + ChatColor.RED + " You must be an Admin to run this command.");
                return false;
            }
        }
        return false;
    }
}