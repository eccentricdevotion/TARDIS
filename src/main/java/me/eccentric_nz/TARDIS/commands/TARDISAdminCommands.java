package me.eccentric_nz.TARDIS.commands;

import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
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
        firstArgsBool.add("name_tardis");
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
                if (!firstArgsStr.contains(args[0]) && !firstArgsBool.contains(args[0]) && !firstArgsInt.contains(args[0])) {
                    sender.sendMessage(plugin.pluginName + "TARDIS does not recognise that command argument!");
                    return false;
                }
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        plugin.reloadConfig();
                        sender.sendMessage(plugin.pluginName + "TARDIS config reloaded.");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("config")) {
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
                    if (args[0].equalsIgnoreCase("list")) {
                        // get all tardis positions - max 18
                        int start = 0, end = 18;
                        if (args.length > 1) {
                            int tmp = Integer.parseInt(args[1]);
                            start = (tmp * 18) - 18;
                            end = tmp * 18;
                        }
                        String limit = start + ", " + end;
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        ResultSetTardis rsl = new ResultSetTardis(plugin, where, limit, true);
                        if (rsl.resultSet()) {
                            sender.sendMessage(plugin.pluginName + "TARDIS locations.");
                            ArrayList<HashMap<String, String>> data = rsl.getData();
                            for (HashMap<String, String> map : data) {
                                sender.sendMessage("Timelord: " + map.get("Owner") + ", Location: " + map.get("current"));
                            }
                            sender.sendMessage(plugin.pluginName + "To see more locations, type: /tardisadmin list 2,  /tardisadmin list 3 etc.");
                        }
                        return true;
                    }
                }
                if (args.length < 2) {
                    sender.sendMessage(plugin.pluginName + "Too few command arguments!");
                    return false;
                }
                if (args[0].equalsIgnoreCase("delete")) {
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("owner", args[1]);
                    ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                    if (rs.resultSet()) {
                        int id = rs.getTardis_id();
                        String saveLoc = rs.getSave();
                        String currentLoc = rs.getCurrent();
                        TARDISConstants.SCHEMATIC schm = rs.getSchematic();
                        TARDISConstants.COMPASS d = rs.getDirection();
                        String chunkLoc = rs.getChunk();
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
                        HashMap<String, Object> wheret = new HashMap<String, Object>();
                        where.put("tardis_id", id);
                        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, true);
                        boolean useCurrent = false;
                        QueryFactory qf = new QueryFactory(plugin);
                        HashMap<String, Object> whered = new HashMap<String, Object>();
                        whered.put("tardis_id", id);
                        if (rst.resultSet()) {
                            useCurrent = true;
                            Location spawn = cw.getSpawnLocation();
                            ArrayList<HashMap<String, String>> data = rst.getData();
                            for (HashMap<String, String> map : data) {
                                String op = plugin.getServer().getOfflinePlayer(map.get("player")).getName();
                                // teleport offline player to spawn
                                plugin.iopHandler.setLocation(op, spawn);
                            }
                            qf.doDelete("travellers", whered);
                        }
                        // need to determine if we use the save location or the current location
                        Location bb_loc = (useCurrent) ? plugin.utils.getLocationFromDB(currentLoc, 0, 0) : plugin.utils.getLocationFromDB(saveLoc, 0, 0);
                        // destroy the TARDIS
                        plugin.destroyPB.destroyTorch(bb_loc);
                        plugin.destroyPB.destroySign(bb_loc, d);
                        plugin.destroyI.destroyInner(schm, id, cw, restore, args[1]);
                        if (cw.getWorldType() == WorldType.FLAT) {
                            // replace stone blocks with AIR
                            plugin.destroyI.destroyInner(schm, id, cw, 0, args[1]);
                        }
                        plugin.destroyPB.destroyPoliceBox(bb_loc, d, id, false);
                        // delete the TARDIS from the db
                        HashMap<String, Object> wherec = new HashMap<String, Object>();
                        wherec.put("tardis_id", id);
                        qf.doDelete("chunks", wherec);
                        HashMap<String, Object> wherea = new HashMap<String, Object>();
                        wherea.put("tardis_id", id);
                        qf.doDelete("tardis", wherea);
                        HashMap<String, Object> whereo = new HashMap<String, Object>();
                        whereo.put("tardis_id", id);
                        qf.doDelete("doors", whereo);
                        sender.sendMessage(plugin.pluginName + "The TARDIS was removed from the world and database successfully.");
                    } else {
                        sender.sendMessage(plugin.pluginName + "Could not find player [" + args[1] + "] in the database!");
                        return true;
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("key")) {
                    String setMaterial = args[1].toUpperCase();
                    if (!Arrays.asList(TARDISMaterials.MATERIAL_LIST).contains(setMaterial)) {
                        sender.sendMessage(plugin.pluginName + ChatColor.RED + "That is not a valid Material! Try checking http://jd.bukkit.org/apidocs/org/bukkit/Material.html");
                        return false;
                    } else {
                        plugin.getConfig().set("key", setMaterial);
                        plugin.TARDIS_KEY = setMaterial;
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
                    plugin.getConfig().set("default_world_name", nodots);
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
                        sender.sendMessage(plugin.pluginName + ChatColor.RED + "World does not exist!");
                        return false;
                    }
                    plugin.getConfig().set("worlds." + nodots, false);
                }
                //checks if its a boolean config option
                String firstArg = args[0].toLowerCase();
                if (firstArgsBool.contains(firstArg)) {
                    // check they typed true of false
                    String tf = args[1].toLowerCase();
                    if (!tf.equals("true") && !tf.equals("false")) {
                        sender.sendMessage(plugin.pluginName + ChatColor.RED + "The last argument must be true or false!");
                        return false;
                    }
                    plugin.getConfig().set(firstArg, Boolean.valueOf(tf));
                }
                //checks if its a number config option
                if (firstArgsInt.contains(firstArg)) {
                    String a = args[1];
                    int val;
                    try {
                        val = Integer.parseInt(a);
                    } catch (NumberFormatException nfe) {
                        // not a number
                        sender.sendMessage(plugin.pluginName + ChatColor.RED + " The last argument must be a number!");
                        return false;
                    }
                    plugin.getConfig().set(firstArg, val);
                }
                plugin.saveConfig();
                sender.sendMessage(plugin.pluginName + "The config was updated!");
                return true;
            } else {
                sender.sendMessage(plugin.pluginName + ChatColor.RED + " You must be an Admin to run this command.");
                return false;
            }
        }
        return false;
    }
}