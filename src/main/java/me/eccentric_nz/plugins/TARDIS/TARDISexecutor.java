package me.eccentric_nz.plugins.TARDIS;

import java.io.IOException;
import java.util.Arrays;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TARDISexecutor implements CommandExecutor {

    private TARDIS plugin;

    public TARDISexecutor(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If the player typed /tardis then do the following...
        // check there is the right number of arguments
        if (cmd.getName().equalsIgnoreCase("tardis")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (args.length == 0) {
                sender.sendMessage(Constants.COMMANDS.split("\n"));
                return true;
            }
            // the command list - first argument MUST appear here!
            if (!args[0].equalsIgnoreCase("save") && !args[0].equalsIgnoreCase("list") && !args[0].equalsIgnoreCase("admin") && !args[0].equalsIgnoreCase("help") && !args[0].equalsIgnoreCase("find") && !args[0].equalsIgnoreCase("reload")) {
                sender.sendMessage("Do you want to list destinations, save a destination, or find the TARDIS?");
                return false;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                plugin.loadConfig();
                sender.sendMessage("TARDIS config reloaded.");
            }
            if (args[0].equalsIgnoreCase("admin")) {
                if (args.length == 1) {
                    sender.sendMessage(Constants.COMMAND_ADMIN.split("\n"));
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage("Too few command arguments!");
                    return false;
                } else {
                    if (!args[1].equalsIgnoreCase("bonus") && !args[1].equalsIgnoreCase("protect") && !args[1].equalsIgnoreCase("max_rad") && !args[1].equalsIgnoreCase("spout") && !args[1].equalsIgnoreCase("default") && !args[1].equalsIgnoreCase("name") && !args[1].equalsIgnoreCase("include") && !args[1].equalsIgnoreCase("key")) {
                        sender.sendMessage("TARDIS does not recognise that command argument!");
                        return false;
                    }
                    if (args[1].equalsIgnoreCase("key")) {
                        String setMaterial = args[2].toUpperCase();
                        if (!Arrays.asList(Materials.MATERIAL_LIST).contains(setMaterial)) {
                            sender.sendMessage(ChatColor.RED + "That is not a valid Material! Try checking http://jd.bukkit.org/apidocs/org/bukkit/Material.html");
                            return false;
                        } else {
                            plugin.config.set("key", setMaterial);
                            Constants.TARDIS_KEY = setMaterial;
                        }
                    }
                    if (args[1].equalsIgnoreCase("bonus")) {
                        String tf = args[2].toLowerCase();
                        if (!tf.equals("true") && !tf.equals("false")) {
                            sender.sendMessage(ChatColor.RED + "The last argument must be true or false!");
                            return false;
                        }
                        plugin.config.set("bonus_chest", Boolean.valueOf(tf));
                    }
                    if (args[1].equalsIgnoreCase("protect")) {
                        String tf = args[2].toLowerCase();
                        if (!tf.equals("true") && !tf.equals("false")) {
                            sender.sendMessage(ChatColor.RED + "The last argument must be true or false!");
                            return false;
                        }
                        plugin.config.set("protect_blocks", Boolean.valueOf(tf));
                    }
                    if (args[1].equalsIgnoreCase("max_rad")) {
                        String a = args[2];
                        int val;
                        try {
                            val = Integer.parseInt(a);
                        } catch (NumberFormatException nfe) {
                            // not a number
                            sender.sendMessage("The last argument must be a number!");
                            return false;
                        }
                        plugin.config.set("tp_radius", val);
                    }
                    if (args[1].equalsIgnoreCase("spout")) {
                        // check they typed true of false
                        String tf = args[2].toLowerCase();
                        if (!tf.equals("true") && !tf.equals("false")) {
                            sender.sendMessage(ChatColor.RED + "The last argument must be true or false!");
                            return false;
                        }
                        plugin.config.set("require_spout", Boolean.valueOf(tf));
                    }
                    if (args[1].equalsIgnoreCase("default")) {
                        // check they typed true of false
                        String tf = args[2].toLowerCase();
                        if (!tf.equals("true") && !tf.equals("false")) {
                            sender.sendMessage(ChatColor.RED + "The last argument must be true or false!");
                            return false;
                        }
                        plugin.config.set("default_world", Boolean.valueOf(tf));
                    }
                    if (args[1].equalsIgnoreCase("name")) {
                        // get world name
                        int count = args.length;
                        StringBuilder buf = new StringBuilder();
                        for (int i = 2; i < count; i++) {
                            buf.append(args[i]).append(" ");
                        }
                        String tmp = buf.toString();
                        String t = tmp.substring(0, tmp.length() - 1);
                        // need to make there are no periods(.) in the text
                        String nodots = StringUtils.replace(t, ".", "_");
                        plugin.config.set("default_world_name", nodots);
                    }
                    if (args[1].equalsIgnoreCase("include")) {
                        // check they typed true of false
                        String tf = args[2].toLowerCase();
                        if (!tf.equals("true") && !tf.equals("false")) {
                            sender.sendMessage(ChatColor.RED + "The last argument must be true or false!");
                            return false;
                        }
                        plugin.config.set("include_default_world", Boolean.valueOf(tf));
                    }
                    try {
                        plugin.config.save(plugin.myconfigfile);
                        sender.sendMessage(Constants.MY_PLUGIN_NAME + " The config was updated!");
                    } catch (IOException e) {
                        sender.sendMessage("There was a problem saving the config file!");
                    }
                }
                return true;
            }
            if (player == null) {
                sender.sendMessage("This command can only be run by a player");
                return false;
            } else {
                if (args[0].equalsIgnoreCase("list")) {
                    if (player.hasPermission("TARDIS.list")) {
                        if (!plugin.timelords.contains(player.getName())) {
                            sender.sendMessage("You have not created a TARDIS yet!");
                            return false;
                        }
                        Constants.list(plugin.timelords, player);
                    } else {
                        sender.sendMessage(Constants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("find")) {
                    if (player.hasPermission("TARDIS.find")) {
                        if (!plugin.timelords.contains(player.getName())) {
                            sender.sendMessage("You have not created a TARDIS yet!");
                            return false;
                        }
                        String loc = plugin.timelords.getString(player.getName()+".save");
                        String[] findData = loc.split(":");
                        sender.sendMessage("You you left your TARDIS in " + findData[0] + " at x:" + findData[1] + " y:" + findData[2] + " z:" + findData[3]);
                        return true;
                    } else {
                        sender.sendMessage(Constants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("save")) {
                    if (player.hasPermission("TARDIS.save")) {
                        if (!plugin.timelords.contains(player.getName())) {
                            sender.sendMessage("You have not created a TARDIS yet!");
                            return false;
                        }
                        if (args.length < 3) {
                            sender.sendMessage("Too few command arguments!");
                            return false;
                        } else {
                            int count = args.length;
                            StringBuilder buf = new StringBuilder();
                            for (int i = 2; i < count; i++) {
                                buf.append(args[i]).append(" ");
                            }
                            String tmp = buf.toString();
                            String t = tmp.substring(0, tmp.length() - 1);
                            // need to make there are no periods(.) in the text
                            String nodots = StringUtils.replace(t, ".", "_");
                            String curDest;
                            // get current destination
                            if (plugin.timelords.getBoolean(player.getName() + ".travelling") == Boolean.valueOf("true")) {
                                // inside TARDIS
                                curDest = plugin.timelords.getString(player.getName() + ".current");
                            } else {
                                // outside TARDIS
                                curDest = plugin.timelords.getString(player.getName() + ".save");
                            }
                            plugin.timelords.set(player.getName() + ".dest" + args[1] + ".name", nodots);
                            plugin.timelords.set(player.getName() + ".dest" + args[1] + ".location", curDest);
                            // save file
                            try {
                                plugin.timelords.save(plugin.timelordsfile);
                            } catch (IOException io) {
                                System.err.println(Constants.MY_PLUGIN_NAME + " Could not save Timelords file!");
                            }
                        }
                    } else {
                        sender.sendMessage(Constants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("help")) {
                    if (args.length == 1) {
                        sender.sendMessage(Constants.COMMANDS.split("\n"));
                        return true;
                    }
                    if (args.length == 2) {
                        switch (Constants.fromString(args[1])) {
                            case CREATE:
                                sender.sendMessage(Constants.COMMAND_CREATE.split("\n"));
                                break;
                            case DELETE:
                                sender.sendMessage(Constants.COMMAND_DELETE.split("\n"));
                                break;
                            case TIMETRAVEL:
                                sender.sendMessage(Constants.COMMAND_TIMETRAVEL.split("\n"));
                                break;
                            case LIST:
                                sender.sendMessage(Constants.COMMAND_LIST.split("\n"));
                                break;
                            case FIND:
                                sender.sendMessage(Constants.COMMAND_FIND.split("\n"));
                                break;
                            case SAVE:
                                sender.sendMessage(Constants.COMMAND_SAVE.split("\n"));
                                break;
                            case ADMIN:
                                sender.sendMessage(Constants.COMMAND_ADMIN.split("\n"));
                                break;
                            default:
                                sender.sendMessage(Constants.COMMANDS.split("\n"));
                        }
                    }
                    return true;
                }
            }
        }
        //If the above has happened the function will break and return true. if this hasn't happened the a value of false will be returned.
        return false;
    }
}