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
    private Location spawnLoc;
    private LivingEntity thetardis;
    private UUID secID;
    private double secLocX;
    private double secLocY;
    private double secLocZ;
    private String secWorld;
    private static Logger log;
    private boolean fences = false;
    private boolean plates = false;

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
            if (!args[0].equalsIgnoreCase("create") && !args[0].equalsIgnoreCase("timetravel") && !args[0].equalsIgnoreCase("list") && !args[0].equalsIgnoreCase("delete") && !args[0].equalsIgnoreCase("admin")) {
                sender.sendMessage("Do you want to create, time travel or delete?");
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
                    if (args[1].equalsIgnoreCase("s_limit")) {
                        String a = args[2];
                        int val;
                        try {
                            val = Integer.parseInt(a);
                        } catch (NumberFormatException nfe) {
                            // not a number
                            sender.sendMessage("The last argument must be a number!");
                            return false;
                        }
                        plugin.config.set("tardis_limit", val);
                    }
                    if (args[1].equalsIgnoreCase("t_limit")) {
                        String a = args[2];
                        int val;
                        try {
                            val = Integer.parseInt(a);
                        } catch (NumberFormatException nfe) {
                            // not a number
                            sender.sendMessage("The last argument must be a number!");
                            return false;
                        }
                        plugin.config.set("todo_limit", val);
                    }
                    if (args[1].equalsIgnoreCase("r_limit")) {
                        String a = args[2];
                        int val;
                        try {
                            val = Integer.parseInt(a);
                        } catch (NumberFormatException nfe) {
                            // not a number
                            sender.sendMessage("The last argument must be a number!");
                            return false;
                        }
                        plugin.config.set("reminder_limit", val);
                    }
                    if (args[1].equalsIgnoreCase("use_inv")) {
                        // check they typed true of false
                        String tf = args[2].toLowerCase();
                        if (!tf.equals("true") && !tf.equals("false")) {
                            sender.sendMessage(ChatColor.RED + "The last argument must be true or false!");
                            return false;
                        }
                        plugin.config.set("use_inventory", Boolean.valueOf(tf));
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
                            Set<String> tardislist = plugin.timelords.getConfigurationSection(configPath).getKeys(false);
                            int size = tardislist.size();
                            int limit = plugin.config.getInt("tardis_limit");
                            if (size >= limit) {
                                sender.sendMessage("You have reached the maximum allowed number of secretaries! The limit is " + limit + " per player.");
                                return false;
                            }
                        }
                        if (plugin.config.getBoolean("use_inventory") == true && player.getGameMode() == GameMode.SURVIVAL) {
                            if (!player.getInventory().contains(Material.FENCE, 8)) {
                                sender.sendMessage("You do not have enough wood fences, (at least are 8 needed).");
                                return false;
                            }
                            if (!player.getInventory().contains(Material.WOOD_PLATE, 3)) {
                                sender.sendMessage("You do not have enough wood pressure plates, (at least are 3 needed).");
                                return false;
                            }
                            // remove the items from the players inventory
                            player.getInventory().removeItem(new ItemStack(Material.FENCE, 8));
                            player.getInventory().removeItem(new ItemStack(Material.WOOD_PLATE, 3));
                        }

                        EntityType et = EntityType.fromName("VILLAGER");
                        if (et == null) {
                            return true;
                        }
                        // correct for negative yaw
                        float pyaw = player.getLocation().getYaw();
                        if (pyaw >= 0) {
                            pyaw = (pyaw % 360);
                        } else {
                            pyaw = (360 + (pyaw % 360));
                        }
                        spawnLoc = player.getTargetBlock(null, 50).getLocation();
                        /*
                         * need to set the x and z values + 0.5 as block coords
                         * seem to start from the corner of the block causing
                         * the spawned villager to take damage from surrounding
                         * blocks. also need to set the y value + 1 as block
                         * coords seem to start from bottom of the block and we
                         * want to spawn the villager on top of and in the
                         * middle of the block!
                         */
                        double lowX = spawnLoc.getX();
                        double lowY = spawnLoc.getY();
                        double lowZ = spawnLoc.getZ();
                        spawnLoc.setX(lowX + 0.5);
                        spawnLoc.setY(lowY + 1);
                        spawnLoc.setZ(lowZ + 0.5);

                        // setBlock(World w, int x, int y, int z, float yaw, float min, float max, String compare)
                        Constants.buildOuterTARDIS(player, spawnLoc, pyaw);

                        secLocX = thetardis.getLocation().getX();
                        secLocY = thetardis.getLocation().getY();
                        secLocZ = thetardis.getLocation().getZ();
                        secWorld = world.getName();
                        plugin.timelords.set(player.getName() + ".location.world", secWorld);
                        plugin.timelords.set(player.getName() + ".location.x", secLocX);
                        plugin.timelords.set(player.getName() + ".location.y", secLocY);
                        plugin.timelords.set(player.getName() + ".location.z", secLocZ);
                        int px = player.getLocation().getBlockX();
                        int py = player.getLocation().getBlockY();
                        int pz = player.getLocation().getBlockZ();
                        plugin.PlayerTARDISMap.put(player, secWorld + "|" + px + "|" + py + "|" + pz);
                        try {
                            plugin.timelords.save(plugin.timelordsfile);
                        } catch (IOException e) {
                            sender.sendMessage("There was a problem saving the TARDIS settings!");
                        }
                        sender.sendMessage("The TARDIS " + ChatColor.AQUA + " was created and selected successfully!");
                        sender.sendMessage(Constants.INSTRUCTIONS.split("\n"));
                        return true;
                    } else {
                        sender.sendMessage(Constants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("timetravel")) {
                    if (player.hasPermission("TARDIS.timetravel")) {
                        if (!plugin.PlayerTARDISMap.containsKey(player)) {
                            sender.sendMessage("You need to inside your TARDIS before you can time travel!");
                            return false;
                        }
                        String saved_loc = plugin.PlayerTARDISMap.get(player);
                        String configPath = player.getName() + "." + saved_loc;
                        if (!args[1].equalsIgnoreCase("home") && !args[1].equalsIgnoreCase("random") && !args[1].equalsIgnoreCase("dest")) {
                            sender.sendMessage("Do you want to time travel to a random, saved or home destination?");
                            return false;
                        }
                        if (args[1].equalsIgnoreCase("home")) {
                            // check if todo limit has been reached
                            if (plugin.timelords.isSet(configPath)) {
                                Set<String> todolist = plugin.timelords.getConfigurationSection(configPath).getKeys(false);
                                int size = todolist.size();
                                int limit = plugin.config.getInt("todo_limit");
                                if (size >= limit) {
                                    sender.sendMessage("You have reached the maximum allowed number of timelords! The limit is " + limit + " per player.");
                                    return false;
                                }
                            }
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
                                int num = plugin.timelords.getInt(configPath + "." + str + ".status");
                                if (i == val) {
                                    if (num == 0) {
                                        plugin.timelords.set(configPath + "." + str + ".status", 1);
                                        sender.sendMessage("Item " + i + " marked as done.");
                                    } else {
                                        plugin.timelords.set(configPath + "." + str + ".status", 0);
                                        sender.sendMessage("Item " + i + " unmarked.");
                                    }
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
                        if (!plugin.PlayerTARDISMap.containsKey(player)) {
                            sender.sendMessage("You need to inside your TARDIS before you can time travel!");
                            return false;
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("delete")) {
                    if (player.hasPermission("TARDIS.delete")) {
                        if (!plugin.PlayerTARDISMap.containsKey(player)) {
                            sender.sendMessage("You need to select the TARDIS with a " + plugin.config.getString("select_material") + " before you can delete it!");
                            return false;
                        } else {
                            String name = "";
                            String EntID = plugin.PlayerTARDISMap.get(player);
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
                                    plugin.PlayerTARDISMap.remove(player);
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