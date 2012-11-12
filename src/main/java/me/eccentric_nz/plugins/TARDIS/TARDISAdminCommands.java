package me.eccentric_nz.plugins.TARDIS;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class TARDISAdminCommands implements CommandExecutor {

    private TARDIS plugin;
    TARDISdatabase service = TARDISdatabase.getInstance();

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
                if (!args[0].equalsIgnoreCase("reload") && !args[0].equalsIgnoreCase("config") && !args[0].equalsIgnoreCase("key") && !args[0].equalsIgnoreCase("bonus_chest") && !args[0].equalsIgnoreCase("protect_blocks") && !args[0].equalsIgnoreCase("give_key") && !args[0].equalsIgnoreCase("platform") && !args[0].equalsIgnoreCase("tp_radius") && !args[0].equalsIgnoreCase("require_spout") && !args[0].equalsIgnoreCase("default_world") && !args[0].equalsIgnoreCase("default_world_name") && !args[0].equalsIgnoreCase("include_default_world") && !args[0].equalsIgnoreCase("exclude") && !args[0].equalsIgnoreCase("sfx") && !args[0].equalsIgnoreCase("use_worldguard") && !args[0].equalsIgnoreCase("respect_worldguard") && !args[0].equalsIgnoreCase("nether") && !args[0].equalsIgnoreCase("the_end") && !args[0].equalsIgnoreCase("land_on_water")) {
                    sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " TARDIS does not recognise that command argument!");
                    return false;
                }
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        plugin.loadConfig();
                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " TARDIS config reloaded.");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("config")) {
                        Set<String> configNames = plugin.config.getKeys(false);
                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + " Here are the current plugin config options!");
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
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Too few command arguments!");
                    return false;
                } else {
                    if (args[0].equalsIgnoreCase("key")) {
                        String setMaterial = args[1].toUpperCase();
                        if (!Arrays.asList(Materials.MATERIAL_LIST).contains(setMaterial)) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + "That is not a valid Material! Try checking http://jd.bukkit.org/apidocs/org/bukkit/Material.html");
                            return false;
                        } else {
                            plugin.config.set("key", setMaterial);
                            Constants.TARDIS_KEY = setMaterial;
                        }
                    }
                    if (args[0].equalsIgnoreCase("bonus_chest")) {
                        String tf = args[1].toLowerCase();
                        if (!tf.equals("true") && !tf.equals("false")) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + "The last argument must be true or false!");
                            return false;
                        }
                        plugin.config.set("bonus_chest", Boolean.valueOf(tf));
                    }
                    if (args[0].equalsIgnoreCase("protect_blocks")) {
                        String tf = args[1].toLowerCase();
                        if (!tf.equals("true") && !tf.equals("false")) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + "The last argument must be true or false!");
                            return false;
                        }
                        plugin.config.set("protect_blocks", Boolean.valueOf(tf));
                    }
                    if (args[0].equalsIgnoreCase("give_key")) {
                        String tf = args[1].toLowerCase();
                        if (!tf.equals("true") && !tf.equals("false")) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + "The last argument must be true or false!");
                            return false;
                        }
                        plugin.config.set("give_key", Boolean.valueOf(tf));
                    }
                    if (args[0].equalsIgnoreCase("platform")) {
                        String tf = args[1].toLowerCase();
                        if (!tf.equals("true") && !tf.equals("false")) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + "The last argument must be true or false!");
                            return false;
                        }
                        plugin.config.set("platform", Boolean.valueOf(tf));
                    }
                    if (args[0].equalsIgnoreCase("tp_radius")) {
                        String a = args[1];
                        int val;
                        try {
                            val = Integer.parseInt(a);
                        } catch (NumberFormatException nfe) {
                            // not a number
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + " The last argument must be a number!");
                            return false;
                        }
                        plugin.config.set("tp_radius", val);
                    }
                    if (args[0].equalsIgnoreCase("require_spout")) {
                        // check they typed true of false
                        String tf = args[1].toLowerCase();
                        if (!tf.equals("true") && !tf.equals("false")) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + "The last argument must be true or false!");
                            return false;
                        }
                        plugin.config.set("require_spout", Boolean.valueOf(tf));
                    }
                    if (args[0].equalsIgnoreCase("default_world")) {
                        // check they typed true of false
                        String tf = args[1].toLowerCase();
                        if (!tf.equals("true") && !tf.equals("false")) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + "The last argument must be true or false!");
                            return false;
                        }
                        plugin.config.set("default_world", Boolean.valueOf(tf));
                    }
                    if (args[0].equalsIgnoreCase("default_world_name")) {
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
                    if (args[0].equalsIgnoreCase("include")) {
                        // check they typed true of false
                        String tf = args[1].toLowerCase();
                        if (!tf.equals("true") && !tf.equals("false")) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + "The last argument must be true or false!");
                            return false;
                        }
                        plugin.config.set("include_default_world", Boolean.valueOf(tf));
                    }
                    if (args[0].equalsIgnoreCase("exclude")) {
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
                        plugin.config.set("worlds." + nodots, false);
                    }
                    if (args[0].equalsIgnoreCase("sfx")) {
                        // check they typed true of false
                        String tf = args[1].toLowerCase();
                        if (!tf.equals("true") && !tf.equals("false")) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + "The last argument must be true or false!");
                            return false;
                        }
                        plugin.config.set("sfx", Boolean.valueOf(tf));
                    }
                    if (args[0].equalsIgnoreCase("use_worldguard")) {
                        // check they typed true of false
                        String tf = args[1].toLowerCase();
                        if (!tf.equals("true") && !tf.equals("false")) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + "The last argument must be true or false!");
                            return false;
                        }
                        plugin.config.set("use_worldguard", Boolean.valueOf(tf));
                    }
                    if (args[0].equalsIgnoreCase("respect_worldguard")) {
                        // check they typed true of false
                        String tf = args[1].toLowerCase();
                        if (!tf.equals("true") && !tf.equals("false")) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + "The last argument must be true or false!");
                            return false;
                        }
                        plugin.config.set("respect_worldguard", Boolean.valueOf(tf));
                    }
                    if (args[0].equalsIgnoreCase("nether")) {
                        // check they typed true of false
                        String tf = args[1].toLowerCase();
                        if (!tf.equals("true") && !tf.equals("false")) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + "The last argument must be true or false!");
                            return false;
                        }
                        plugin.config.set("nether", Boolean.valueOf(tf));
                    }
                    if (args[0].equalsIgnoreCase("the_end")) {
                        // check they typed true of false
                        String tf = args[1].toLowerCase();
                        if (!tf.equals("true") && !tf.equals("false")) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + "The last argument must be true or false!");
                            return false;
                        }
                        plugin.config.set("the_end", Boolean.valueOf(tf));
                    }
                    if (args[0].equalsIgnoreCase("land_on_water")) {
                        // check they typed true of false
                        String tf = args[1].toLowerCase();
                        if (!tf.equals("true") && !tf.equals("false")) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + "The last argument must be true or false!");
                            return false;
                        }
                        plugin.config.set("land_on_water", Boolean.valueOf(tf));
                    }
                    try {
                        plugin.config.save(plugin.myconfigfile);
                        sender.sendMessage(Constants.MY_PLUGIN_NAME + " The config was updated!");
                    } catch (IOException e) {
                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " There was a problem saving the config file!");
                    }
                }
                return true;
            } else {
                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + " You must be an Admin to run this command.");
                return false;
            }
        }
        return false;
    }
}