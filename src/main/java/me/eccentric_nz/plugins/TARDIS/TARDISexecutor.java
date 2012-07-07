package me.eccentric_nz.plugins.TARDIS;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
            if (!args[0].equalsIgnoreCase("create") && !args[0].equalsIgnoreCase("timetravel") && !args[0].equalsIgnoreCase("tt") && !args[0].equalsIgnoreCase("list") && !args[0].equalsIgnoreCase("delete") && !args[0].equalsIgnoreCase("admin") && !args[0].equalsIgnoreCase("help") && !args[0].equalsIgnoreCase("find")) {
                sender.sendMessage("Do you want to create, time travel, list, find or delete?");
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
                    if (args[1].equalsIgnoreCase("use_inv")) {
                        // check they typed true of false
                        String tf = args[2].toLowerCase();
                        if (!tf.equals("true") && !tf.equals("false")) {
                            sender.sendMessage(ChatColor.RED + "The last argument must be true or false!");
                            return false;
                        }
                        plugin.config.set("require_blocks", Boolean.valueOf(tf));
                    }
                    try {
                        plugin.config.save(plugin.myconfigfile);
                        sender.sendMessage("The config was updated!");
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
                if (args[0].equalsIgnoreCase("create")) {
                    if (player.hasPermission("TARDIS.create")) {
                        // check if TARDIS limit has been reached
                        String configPath = player.getName();
                        if (plugin.timelords.isSet(configPath)) {
                            String leftLoc = plugin.timelords.getString(configPath + ".save");
                            String[] leftData = leftLoc.split(":");
                            player.sendMessage("You already have a TARDIS, you left it in " + leftData[0] + " at x:" + leftData[1] + " y:" + leftData[2] + " z:" + leftData[3]);
                            return false;
                        }
                        if (plugin.config.getBoolean("require_blocks") == true && player.getGameMode() == GameMode.SURVIVAL) {
                            if (!player.getInventory().contains(Material.IRON_BLOCK, 1)) {
                                sender.sendMessage("You do not have an IRON BLOCK, you better get that pick-axe out!");
                                return false;
                            }
                            if (!player.getInventory().contains(Material.LAPIS_BLOCK, 1)) {
                                sender.sendMessage("You do not have a LAPIS BLOCK, time to go mining!");
                                return false;
                            }
                            if (!player.getInventory().contains(Material.REDSTONE_TORCH_ON, 1)) {
                                sender.sendMessage("You do not have a REDSTONE TORCH, you should probably make one!");
                                return false;
                            }
                            // remove the items from the players inventory
                            player.getInventory().removeItem(new ItemStack(Material.IRON_BLOCK, 1));
                            player.getInventory().removeItem(new ItemStack(Material.LAPIS_BLOCK, 1));
                            player.getInventory().removeItem(new ItemStack(Material.REDSTONE_TORCH_ON, 1));
                        }

                        // correct for negative yaw
                        float pyaw = player.getLocation().getYaw();
                        if (pyaw >= 0) {
                            pyaw = (pyaw % 360);
                        } else {
                            pyaw = (360 + (pyaw % 360));
                        }
                        if (pyaw >= 315 || pyaw < 45) {
                            d = "NORTH";
                        }
                        if (pyaw >= 225 && pyaw < 315) {
                            d = "WEST";
                        }
                        if (pyaw >= 135 && pyaw < 225) {
                            d = "SOUTH";
                        }
                        if (pyaw >= 45 && pyaw < 135) {
                            d = "EAST";
                        }
                        bluebox_loc = player.getTargetBlock(null, 50).getLocation();
                        int lowY = bluebox_loc.getBlockY();
                        bluebox_loc.setY(lowY + 1);

                        // setBlock(World w, int x, int y, int z, float yaw, float min, float max, String compare)
                        Constants.buildOuterTARDIS(player, bluebox_loc, pyaw);
                        Schematic s = new Schematic(plugin);
                        s.buildInnerTARDIS(plugin.schematic, player, bluebox_loc, Constants.COMPASS.valueOf(d));

                        bb_locX = bluebox_loc.getBlockX();
                        bb_locY = bluebox_loc.getBlockY();
                        bb_locZ = bluebox_loc.getBlockZ();
                        bb_world = player.getWorld().getName();
                        plugin.timelords.set(configPath + ".home", bb_world+":"+bb_locX+":"+bb_locY+":"+bb_locZ);
                        plugin.timelords.set(configPath + ".save", bb_world+":"+bb_locX+":"+bb_locY+":"+bb_locZ);
                        plugin.timelords.set(configPath + ".direction", d);
                        try {
                            plugin.timelords.save(plugin.timelordsfile);
                        } catch (IOException e) {
                            sender.sendMessage("There was a problem saving the TARDIS settings!");
                            System.err.println(Constants.MY_PLUGIN_NAME + " Could not save the time lords file!");
                        }
                        sender.sendMessage(Constants.INSTRUCTIONS.split("\n"));
                        return true;
                    } else {
                        sender.sendMessage(Constants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("timetravel") || args[0].equalsIgnoreCase("tt")) {
                    if (player.hasPermission("TARDIS.timetravel")) {
                        String configPath = player.getName();
                        if (!plugin.PlayerTARDISMap.containsKey(configPath)) {
                            sender.sendMessage("You need to be inside your TARDIS before you can time travel!");
                            return false;
                        }
                        float yaw = player.getLocation().getYaw();
                        float pitch = player.getLocation().getPitch();
                        if (!args[1].equalsIgnoreCase("home") && !args[1].equalsIgnoreCase("random") && !args[1].equalsIgnoreCase("dest")) {
                            sender.sendMessage("Do you want to time travel to a random, saved or home destination?");
                            return false;
                        }
                        if (args[1].equalsIgnoreCase("home")) {
                            // destination = place where TARDIS was first created
                            String home = plugin.timelords.getString(configPath + ".home");
                            String h_data[] = home.split(":");
                            World h_world = Bukkit.getServer().getWorld(h_data[0]);
                            try {
                                hx = Integer.parseInt(h_data[1]);
                                hy = Integer.parseInt(h_data[2]);
                                hz = Integer.parseInt(h_data[3]);
                            } catch (NumberFormatException n) {
                                System.err.println(Constants.MY_PLUGIN_NAME + "Could not convert to number");
                            }
                            Location exitTardis = new Location(h_world, hx, hy, hz, yaw, pitch);
                            // System.out.println("exitTardis: " + exitTardis);
                            h_world.getChunkAt(exitTardis).load();
                            // exit TARDIS!
                            player.teleport(exitTardis);
                            plugin.PlayerTARDISMap.remove(player.getName());
                            if (args.length < 3) {
                                sender.sendMessage("Too few command arguments!");
                                return false;
                            }
                            int count = args.length;
                            String t = "";
                            for (int i = 2; i < count; i++) {
                                t += args[i] + " ";
                            }
                            t = t.substring(0, t.length() - 1);
                            // need to make there are no periods(.) in the text
                            String nodots = StringUtils.replace(t, ".", "_");
                            plugin.timelords.set(configPath + "." + nodots + ".status", 0);
                            try {
                                plugin.timelords.save(plugin.timelordsfile);
                            } catch (IOException e) {
                                sender.sendMessage("There was a problem saving the todo item!");
                            }
                            sender.sendMessage("The todo was added successfully!");
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("random")) {
                            // randomness is determined by the position of the repeaters on the console
                            // r1 (by door) determines
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("dest")) {
                            if (args.length < 3) {
                                sender.sendMessage("Too few command arguments!");
                                return false;
                            }
                            Set<String> todolist = plugin.timelords.getConfigurationSection(configPath).getKeys(false);
                            String a = args[2];
                            int val;
                            try {
                                val = Integer.parseInt(a);
                            } catch (NumberFormatException nfe) {
                                // not a number
                                sender.sendMessage("The last argument must be a number!");
                                return false;
                            }
                            int i = 1;
                            for (String str : todolist) {
                                if (i == val) {
                                    plugin.timelords.set(configPath + "." + str, null);
                                    sender.sendMessage("Item " + i + " deleted.");
                                    try {
                                        plugin.timelords.save(plugin.timelordsfile);
                                    } catch (IOException e) {
                                        sender.sendMessage("There was a problem changing the todo status!");
                                    }
                                    break;
                                }
                                i++;
                            }
                            return true;
                        }
                    } else {
                        sender.sendMessage(Constants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("list")) {
                    if (player.hasPermission("TARDIS.timetravel")) {
                        if (!plugin.PlayerTARDISMap.containsKey(player.getName())) {
                            sender.sendMessage("You need to inside your TARDIS before you can time travel!");
                            return false;
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("delete")) {
                    if (player.hasPermission("TARDIS.delete")) {
                        if (!plugin.PlayerTARDISMap.containsKey(player.getName())) {
                            sender.sendMessage("You need to select the TARDIS with a " + plugin.config.getString("select_material") + " before you can delete it!");
                            return false;
                        } else {
                            String name = "";
                            Boolean EntID = plugin.PlayerTARDISMap.get(player.getName());
                            String configPath = player.getName() + "." + EntID;
                            world = player.getWorld();
                            Set<String> seclist = plugin.timelords.getConfigurationSection(player.getName()).getKeys(false);
                            Set<String> todolist = plugin.timelords.getConfigurationSection(player.getName()).getKeys(false);
                            Set<String> reminderlist = plugin.timelords.getConfigurationSection(player.getName()).getKeys(false);
                            boolean found = false;
                            for (String u : seclist) {
                                if (u.equals(EntID.toString())) {
                                    found = true;
                                    name = "'" + plugin.timelords.getString(player.getName() + "." + u + ".name") + "'";
                                    plugin.timelords.set(configPath, null);
                                    // remove the villager
                                    List<LivingEntity> elist = world.getLivingEntities();
                                    for (LivingEntity l : elist) {
                                        if (l.getUniqueId().equals(EntID)) {
                                            l.remove();
                                        }
                                    }
                                }
                            }
                            if (found == false) {
                                sender.sendMessage("Could not find the TARDIS's config!");
                                return false;
                            } else {
                                for (String t : todolist) {
                                    if (t.equals(EntID.toString())) {
                                        plugin.timelords.set(configPath, null);
                                    }
                                }
                                for (String r : todolist) {
                                    if (r.equals(EntID.toString())) {
                                        plugin.timelords.set(configPath, null);
                                    }
                                }
                                try {
                                    plugin.PlayerTARDISMap.remove(player.getName());
                                    plugin.timelords.save(plugin.timelordsfile);
                                } catch (IOException io) {
                                    System.out.println("Could not save the config files!");
                                }
                                sender.sendMessage("The TARDIS " + name + " was deleted successfully!");
                                return true;
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