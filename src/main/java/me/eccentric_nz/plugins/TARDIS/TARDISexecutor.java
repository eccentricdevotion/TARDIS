package me.eccentric_nz.plugins.TARDIS;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class TARDISexecutor implements CommandExecutor {

    private TARDIS plugin;
    private World world;
    private Block targetBlock;
    private Location bluebox_loc;
    private LivingEntity thetardis;
    private UUID secID;
    private int bb_locX;
    private int bb_locY;
    private int bb_locZ;
    private String bb_world;
    private String d;
    private static Logger log;
    private boolean fences = false;
    private boolean plates = false;
    private int hx = 0, hy = 0, hz = 0;

    public TARDISexecutor(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If the player typed /setprof then do the following...
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
            if (!args[0].equalsIgnoreCase("save") && !args[0].equalsIgnoreCase("list") && !args[0].equalsIgnoreCase("admin") && !args[0].equalsIgnoreCase("help") && !args[0].equalsIgnoreCase("find")) {
                sender.sendMessage("Do you want to list destinations, save a destination, or find the TARDIS?");
                return false;
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
                    if (args[1].equalsIgnoreCase("bonus")) {
                        String tf = args[2].toLowerCase();
                        if (!tf.equals("true") && !tf.equals("false")) {
                            sender.sendMessage(ChatColor.RED + "The last argument must be true or false!");
                            return false;
                        }
                        plugin.config.set("bonus_chest", tf);
                    }
                    if (args[1].equalsIgnoreCase("protect")) {
                        String tf = args[2].toLowerCase();
                        if (!tf.equals("true") && !tf.equals("false")) {
                            sender.sendMessage(ChatColor.RED + "The last argument must be true or false!");
                            return false;
                        }
                        plugin.config.set("bonus_chest", tf);
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
                        plugin.config.set("require_spout", tf);
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
                            String curDest;
                            // get current destination
                            if (plugin.timelords.getBoolean(player.getName()+".travelling") == Boolean.valueOf("true")) {
                                // inside TARDIS
                                curDest = plugin.timelords.getString(player.getName() + ".current");
                            } else {
                                // outside TARDIS
                                curDest = plugin.timelords.getString(player.getName() + ".save");
                            }
                            plugin.timelords.set(player.getName() + ".dest" + args[1] + ".name", args[2]);
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