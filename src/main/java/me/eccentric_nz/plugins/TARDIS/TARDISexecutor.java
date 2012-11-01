package me.eccentric_nz.plugins.TARDIS;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;

public class TARDISexecutor implements CommandExecutor {

    private TARDIS plugin;
    TARDISdatabase service = TARDISdatabase.getInstance();

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
            if (!args[0].equalsIgnoreCase("save") && !args[0].equalsIgnoreCase("list") && !args[0].equalsIgnoreCase("admin") && !args[0].equalsIgnoreCase("help") && !args[0].equalsIgnoreCase("find") && !args[0].equalsIgnoreCase("reload") && !args[0].equalsIgnoreCase("add") && !args[0].equalsIgnoreCase("remove") && !args[0].equalsIgnoreCase("update") && !args[0].equalsIgnoreCase("travel") && !args[0].equalsIgnoreCase("rebuild") && !args[0].equalsIgnoreCase("chameleon") && !args[0].equalsIgnoreCase("sfx") && !args[0].equalsIgnoreCase("platform") && !args[0].equalsIgnoreCase("quotes") && !args[0].equalsIgnoreCase("comehere") && !args[0].equalsIgnoreCase("direction") && !args[0].equalsIgnoreCase("setdest")) {
                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Do you want to list destinations, save a destination, travel, update the TARDIS, add/remove companions, turn the Chameleon Circuit or SFX on or off, do some admin stuff or find the TARDIS?");
                return false;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender instanceof ConsoleCommandSender || player.hasPermission("TARDIS.admin")) {
                    plugin.loadConfig();
                    sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " TARDIS config reloaded.");
                } else {
                    sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + " You must be an Admin to run this command.");
                    return false;
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("admin")) {
                if (sender instanceof ConsoleCommandSender || player.hasPermission("TARDIS.admin")) {
                    if (args.length == 1) {
                        sender.sendMessage(Constants.COMMAND_ADMIN.split("\n"));
                        return true;
                    }
                    if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("config")) {
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
                    } else if (args.length < 3) {
                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Too few command arguments!");
                        return false;
                    } else {
                        if (!args[1].equalsIgnoreCase("bonus") && !args[1].equalsIgnoreCase("protect") && !args[1].equalsIgnoreCase("max_rad") && !args[1].equalsIgnoreCase("spout") && !args[1].equalsIgnoreCase("default") && !args[1].equalsIgnoreCase("name") && !args[1].equalsIgnoreCase("include") && !args[1].equalsIgnoreCase("key") && !args[1].equalsIgnoreCase("update") && !args[1].equalsIgnoreCase("exclude") && !args[1].equalsIgnoreCase("platform") && !args[1].equalsIgnoreCase("sfx") && !args[1].equalsIgnoreCase("config") && !args[1].equalsIgnoreCase("area") && !args[1].equalsIgnoreCase("addgroup") && !args[1].equalsIgnoreCase("setgroup")) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " TARDIS does not recognise that command argument!");
                            return false;
                        }
                        if (args[1].equalsIgnoreCase("key")) {
                            String setMaterial = args[2].toUpperCase();
                            if (!Arrays.asList(Materials.MATERIAL_LIST).contains(setMaterial)) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + "That is not a valid Material! Try checking http://jd.bukkit.org/apidocs/org/bukkit/Material.html");
                                return false;
                            } else {
                                plugin.config.set("key", setMaterial);
                                Constants.TARDIS_KEY = setMaterial;
                            }
                        }
                        if (args[1].equalsIgnoreCase("bonus")) {
                            String tf = args[2].toLowerCase();
                            if (!tf.equals("true") && !tf.equals("false")) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + "The last argument must be true or false!");
                                return false;
                            }
                            plugin.config.set("bonus_chest", Boolean.valueOf(tf));
                        }
                        if (args[1].equalsIgnoreCase("protect")) {
                            String tf = args[2].toLowerCase();
                            if (!tf.equals("true") && !tf.equals("false")) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + "The last argument must be true or false!");
                                return false;
                            }
                            plugin.config.set("protect_blocks", Boolean.valueOf(tf));
                        }
                        if (args[1].equalsIgnoreCase("platform")) {
                            String tf = args[2].toLowerCase();
                            if (!tf.equals("true") && !tf.equals("false")) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + "The last argument must be true or false!");
                                return false;
                            }
                            plugin.config.set("platform", Boolean.valueOf(tf));
                        }
                        if (args[1].equalsIgnoreCase("max_rad")) {
                            TARDISUtils utils = new TARDISUtils(plugin);
                            String a = args[2];
                            int val;
                            try {
                                val = utils.parseNum(a);
                            } catch (NumberFormatException nfe) {
                                // not a number
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + " The last argument must be a number!");
                                return false;
                            }
                            plugin.config.set("tp_radius", val);
                        }
                        if (args[1].equalsIgnoreCase("spout")) {
                            // check they typed true of false
                            String tf = args[2].toLowerCase();
                            if (!tf.equals("true") && !tf.equals("false")) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + "The last argument must be true or false!");
                                return false;
                            }
                            plugin.config.set("require_spout", Boolean.valueOf(tf));
                        }
                        if (args[1].equalsIgnoreCase("default")) {
                            // check they typed true of false
                            String tf = args[2].toLowerCase();
                            if (!tf.equals("true") && !tf.equals("false")) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + "The last argument must be true or false!");
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
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + "The last argument must be true or false!");
                                return false;
                            }
                            plugin.config.set("include_default_world", Boolean.valueOf(tf));
                        }
                        if (args[1].equalsIgnoreCase("exclude")) {
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
                        if (args[1].equalsIgnoreCase("sfx")) {
                            // check they typed true of false
                            String tf = args[2].toLowerCase();
                            if (!tf.equals("true") && !tf.equals("false")) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + "The last argument must be true or false!");
                                return false;
                            }
                            plugin.config.set("sfx", Boolean.valueOf(tf));
                        }
                        if (!args[1].equalsIgnoreCase("area")) {
                            try {
                                plugin.config.save(plugin.myconfigfile);
                                sender.sendMessage(Constants.MY_PLUGIN_NAME + " The config was updated!");
                            } catch (IOException e) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " There was a problem saving the config file!");
                            }
                        } else if (args[1].equalsIgnoreCase("area")) {
                            if (args[2].equals("start")) {
                                // check name is unique and acceptable
                                if (!args[3].matches("[A-Za-z0-9_]{2,16}")) {
                                    sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + "That doesn't appear to be a valid area name (it may be too long)");
                                    return false;
                                }
                                String queryName = "SELECT area_name FROM areas";
                                try {
                                    Connection connection = service.getConnection();
                                    Statement statement = connection.createStatement();
                                    ResultSet rsName = statement.executeQuery(queryName);
                                    while (rsName.next()) {
                                        if (rsName.getString("area_name").equals(args[3])) {
                                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Area name already in use!");
                                            return false;
                                        }
                                    }
                                } catch (SQLException e) {
                                    System.err.println(Constants.MY_PLUGIN_NAME + "Couldn't get area names: " + e);
                                }
                                plugin.trackName.put(player.getName(), args[3]);
                                player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Click the area start block to save its position.");
                                return true;
                            }
                            if (args[2].equals("end")) {
                                if (!plugin.trackBlock.containsKey(player.getName())) {
                                    player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + " You haven't selected an area start block!");
                                    return false;
                                }
                                plugin.trackEnd.put(player.getName(), "end");
                                player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Click the area end block to complete the area.");
                                return true;
                            }
                            if (args[2].equals("remove")) {
                                String queryRemove = "DELETE FROM areas WHERE area_name = '" + args[3] + "'";
                                try {
                                    Connection connection = service.getConnection();
                                    Statement statement = connection.createStatement();
                                    statement.executeUpdate(queryRemove);
                                    player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Area [" + args[3] + "] deleted!");
                                    return true;
                                } catch (SQLException e) {
                                    System.err.println(Constants.MY_PLUGIN_NAME + "Couldn't delete area: " + e);
                                }
                            }
                            if (args[2].equals("show")) {
                                String queryGetArea = "SELECT * FROM areas WHERE area_name = '" + args[3] + "'";
                                try {
                                    Connection connection = service.getConnection();
                                    Statement statement = connection.createStatement();
                                    ResultSet rsArea = statement.executeQuery(queryGetArea);
                                    if (!rsArea.next()) {
                                        player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + "Could not find area [" + args[3] + "]! Did you type the name correctly?");
                                        return false;
                                    }
                                    int mix = rsArea.getInt("minx");
                                    int miz = rsArea.getInt("minz");
                                    int max = rsArea.getInt("maxx");
                                    int maz = rsArea.getInt("maxz");
                                    World w = plugin.getServer().getWorld(rsArea.getString("world"));
                                    final Block b1 = w.getHighestBlockAt(mix, miz);
                                    b1.setTypeId(19);
                                    final Block b2 = w.getHighestBlockAt(mix, maz);
                                    b2.setTypeId(19);
                                    final Block b3 = w.getHighestBlockAt(max, miz);
                                    b3.setTypeId(19);
                                    final Block b4 = w.getHighestBlockAt(max, maz);
                                    b4.setTypeId(19);
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                        @Override
                                        public void run() {
                                            b1.setTypeId(0);
                                            b2.setTypeId(0);
                                            b3.setTypeId(0);
                                            b4.setTypeId(0);
                                        }
                                    }, 300L);
                                    return true;
                                } catch (SQLException e) {
                                    System.err.println(Constants.MY_PLUGIN_NAME + "Couldn't delete area: " + e);
                                }
                            }
                            if (args[2].equals("list")) {
                                String queryGetArea = "SELECT * FROM areas";
                                try {
                                    Connection connection = service.getConnection();
                                    Statement statement = connection.createStatement();
                                    ResultSet rsArea = statement.executeQuery(queryGetArea);
                                    int a = 1;
                                    if (!rsArea.isBeforeFirst()) {
                                        player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " No areas were found!");
                                        return false;
                                    }
                                    while (rsArea.next()) {
                                        String name = rsArea.getString("area_name");
                                        String type = (rsArea.getInt("area_type") == 0) ? "deny" : "allow";
                                        String world = rsArea.getString("world");
                                        if (a == 1) {
                                            player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + "Areas");
                                        }
                                        player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + a + ". [" + name + "] in world: " + world + ", flag: " + type);
                                        a++;
                                    }
                                    return true;
                                } catch (SQLException e) {
                                    System.err.println(Constants.MY_PLUGIN_NAME + "Couldn't delete area: " + e);
                                }
                            }
                        }
                    }
                    return true;
                } else {
                    sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + " You must be an Admin to run this command.");
                    return false;
                }
            }
            if (player == null) {
                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + " This command can only be run by a player");
                return false;
            } else {
                if (args[0].equalsIgnoreCase("comehere")) {
                    if (player.hasPermission("TARDIS.timetravel")) {
                        final Location eyeLocation = player.getTargetBlock(null, 50).getLocation();
                        TARDISWorldGuardChecker wg = new TARDISWorldGuardChecker(plugin);
                        if (wg.cantBuild(player, eyeLocation)) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + "That location is protected by WorldGuard!");
                            return false;
                        }
                        TARDISArea ta = new TARDISArea(plugin);
                        if (ta.areaCheckLocPlayer(player, eyeLocation)) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + "You do not have permission [" + plugin.trackPerm.get(player.getName()) + "] to bring the TARDIS to this location!");
                            plugin.trackPerm.remove(player.getName());
                            return false;
                        }
                        Material m = player.getTargetBlock(null, 50).getType();
                        if (m != Material.SNOW) {
                            int yplusone = eyeLocation.getBlockY();
                            eyeLocation.setY(yplusone + 1);
                        }
                        // set save location
                        try {
                            Connection connection = service.getConnection();
                            Statement statement = connection.createStatement();
                            ResultSet rs = service.getTardis(player.getName(), "*");
                            if (!rs.next()) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " You must be the Timelord of the TARDIS to use this command!");
                                return false;
                            }
                            final Player p = player;
                            final int id = rs.getInt("tardis_id");
                            String badsave = rs.getString("save");
                            final boolean cham = rs.getBoolean("chamele_on");
                            final Constants.COMPASS d = Constants.COMPASS.valueOf(rs.getString("direction"));
                            String[] saveData = badsave.split(":");
                            World w = plugin.getServer().getWorld(saveData[0]);
                            int x = 0, y = 0, z = 0;
                            try {
                                x = Integer.valueOf(saveData[1]);
                                y = Integer.valueOf(saveData[2]);
                                z = Integer.valueOf(saveData[3]);
                            } catch (NumberFormatException nfe) {
                                System.err.println(Constants.MY_PLUGIN_NAME + "Couldn't covert to number: " + nfe);
                            }
                            final Location oldSave = w.getBlockAt(x, y, z).getLocation();
                            rs.close();
                            String comehere = eyeLocation.getWorld().getName() + ":" + eyeLocation.getBlockX() + ":" + eyeLocation.getBlockY() + ":" + eyeLocation.getBlockZ();
                            String querySave = "UPDATE tardis SET save = '" + comehere + "' WHERE tardis_id = " + id;
                            statement.executeUpdate(querySave);
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The TARDIS is coming...");
                            long delay = 100L;
                            if (plugin.getServer().getPluginManager().getPlugin("Spout") != null && SpoutManager.getPlayer(player).isSpoutCraftEnabled()) {
                                SpoutManager.getSoundManager().playCustomSoundEffect(plugin, SpoutManager.getPlayer(player), "https://dl.dropbox.com/u/53758864/tardis_land.mp3", false, eyeLocation, 9, 75);
                                delay = 400L;
                            }
                            final TARDISDestroyer td = new TARDISDestroyer(plugin);
                            final TARDISBuilder tb = new TARDISBuilder(plugin);
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                public void run() {
                                    td.destroySign(oldSave, d);
                                    td.destroyTorch(oldSave);
                                    td.destroyBlueBox(oldSave, d, id);
                                    tb.buildOuterTARDIS(id, eyeLocation, d, cham, p);
                                }
                            }, delay);
                            statement.close();
                        } catch (SQLException e) {
                            System.err.println(Constants.MY_PLUGIN_NAME + "Couldn't get TARDIS: " + e);
                        }
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + Constants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("travel")) {
                    if (player.hasPermission("TARDIS.timetravel")) {
                        if (args.length < 2) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Too few command arguments!");
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
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Either you are not a Timelord, or you are not inside your TARDIS. You need to be both to run this command!");
                                return false;
                            }
                            int id = rs.getInt("tardis_id");
                            Constants.COMPASS d = Constants.COMPASS.valueOf(rs.getString("direction"));
                            rs.close();
                            if (args.length == 2) {
                                // we're thinking this is a player's name
                                if (plugin.getServer().getPlayer(args[1]) == null) {
                                    sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " That player is not online!");
                                    return false;
                                }
                                Player destPlayer = plugin.getServer().getPlayer(args[1]);
                                Location player_loc = destPlayer.getLocation();
                                World w = player_loc.getWorld();
                                int[] start_loc = tt.getStartLocation(player_loc, d);
                                int count = tt.safeLocation(start_loc[0] - 3, player_loc.getBlockY(), start_loc[2], start_loc[1], start_loc[3], start_loc[4], start_loc[5], w, d);
                                if (count > 0) {
                                    sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The player's location would not be safe! Please tell the player to move!");
                                    return false;
                                }
                                TARDISArea ta = new TARDISArea(plugin);
                                if (ta.areaCheckLocPlayer(player, player_loc)) {
                                    sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + "You do not have permission [" + plugin.trackPerm.get(player.getName()) + "] to bring the TARDIS to this location!");
                                    plugin.trackPerm.remove(player.getName());
                                    return false;
                                }
                                String save_loc = player_loc.getWorld().getName() + ":" + (player_loc.getBlockX() - 3) + ":" + player_loc.getBlockY() + ":" + player_loc.getBlockZ();
                                String querySave = "UPDATE tardis SET save = '" + save_loc + "' WHERE tardis_id = " + id;
                                statement.executeUpdate(querySave);
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The player location was saved succesfully. Please exit the TARDIS!");
                                return true;
                            }
                            if (args.length == 3 && args[1].equalsIgnoreCase("dest")) {
                                // we're thinking this is a saved destination name
                                String queryGetDest = "SELECT * FROM destinations WHERE tardis_id = " + id + " AND dest_name = '" + args[2] + "'";
                                ResultSet rsDest = statement.executeQuery(queryGetDest);
                                if (!rsDest.next()) {
                                    sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Could not find a destination with that name! try using " + ChatColor.GREEN + "/TARDIS list saves" + ChatColor.RESET + " first.");
                                    return false;
                                }
                                String save_loc = rsDest.getString("world") + ":" + rsDest.getInt("x") + ":" + rsDest.getInt("y") + ":" + rsDest.getInt("z");
                                String querySave = "UPDATE tardis SET save = '" + save_loc + "' WHERE tardis_id = " + id;
                                statement.executeUpdate(querySave);
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The specified location was set succesfully. Please exit the TARDIS!");
                                return true;
                            }
                            if (args.length > 3 && args.length < 5) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Too few command arguments for co-ordinates travel!");
                                return false;
                            }
                            if (args.length == 5) {
                                // must be a location then
                                TARDISUtils utils = new TARDISUtils(plugin);
                                int x = 0, y = 0, z = 0;
                                World w = plugin.getServer().getWorld(args[1]);
                                x = utils.parseNum(args[2]);
                                y = utils.parseNum(args[3]);
                                z = utils.parseNum(args[4]);
                                Block block = w.getBlockAt(x, y, z);
                                Location location = block.getLocation();

                                // check location
                                int[] start_loc = tt.getStartLocation(location, d);
                                int count = tt.safeLocation(start_loc[0], location.getBlockY(), start_loc[2], start_loc[1], start_loc[3], start_loc[4], start_loc[5], w, d);
                                if (count > 0) {
                                    sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The specified location would not be safe! Please try another.");
                                    return false;
                                } else {
                                    String save_loc = location.getWorld().getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ();
                                    String querySave = "UPDATE tardis SET save = '" + save_loc + "' WHERE tardis_id = " + id;
                                    statement.executeUpdate(querySave);
                                    sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The specified location was saved succesfully. Please exit the TARDIS!");
                                    return true;
                                }
                            }
                            statement.close();
                        } catch (SQLException e) {
                            System.err.println(Constants.MY_PLUGIN_NAME + " /TARDIS travel to location Error: " + e);
                        }
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + Constants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("update")) {
                    if (player.hasPermission("TARDIS.update")) {
                        String[] validBlockNames = {"door", "button", "save-repeater", "x-repeater", "z-repeater", "y-repeater", "chameleon"};
                        if (args.length < 2) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Too few command arguments!");
                            return false;
                        }
                        if (!Arrays.asList(validBlockNames).contains(args[1].toLowerCase())) {
                            player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " That is not a valid TARDIS block name! Try one of : door|button|save-repeater|x-repeater|z-repeater|y-repeater");
                            return false;
                        }
                        try {
                            Connection connection = service.getConnection();
                            Statement statement = connection.createStatement();
                            String queryInTARDIS = "SELECT tardis.owner, travellers.player FROM tardis, travellers WHERE travellers.player = '" + player.getName() + "' AND travellers.tardis_id = tardis.tardis_id AND travellers.player = tardis.owner";
                            ResultSet rs = statement.executeQuery(queryInTARDIS);
                            if (rs == null || !rs.next()) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Either you are not a Timelord, or you are not inside your TARDIS. You need to be both to run this command!");
                                return false;
                            }
                            rs.close();
                            statement.close();
                        } catch (SQLException e) {
                            System.err.println(Constants.MY_PLUGIN_NAME + " Update TARDIS Blocks Error: " + e);
                        }
                        plugin.trackPlayers.put(player.getName(), args[1].toLowerCase());
                        player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Click the TARDIS " + args[1].toLowerCase() + " to update its position.");
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + Constants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("rebuild")) {
                    if (player.hasPermission("TARDIS.rebuild")) {
                        String save = "";
                        World w;
                        int x = 0, y = 0, z = 0, id = -1;
                        Constants.COMPASS d = Constants.COMPASS.EAST;
                        boolean cham = false;
                        TARDISBuilder builder = new TARDISBuilder(plugin);
                        try {
                            Connection connection = service.getConnection();
                            Statement statement = connection.createStatement();
                            ResultSet rs = service.getTardis(player.getName(), "*");;
                            if (!rs.next()) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " " + Constants.NO_TARDIS);
                                return false;
                            }
                            save = rs.getString("save");
                            id = rs.getInt("tardis_id");
                            cham = rs.getBoolean("chamele_on");
                            d = Constants.COMPASS.valueOf(rs.getString("direction"));
                            rs.close();
                            statement.close();
                        } catch (SQLException e) {
                            System.err.println(Constants.MY_PLUGIN_NAME + " Select TARDIS By Owner Error: " + e);
                        }
                        TARDISUtils utils = new TARDISUtils(plugin);
                        String[] save_data = save.split(":");
                        w = plugin.getServer().getWorld(save_data[0]);
                        x = utils.parseNum(save_data[1]);
                        y = utils.parseNum(save_data[2]);
                        z = utils.parseNum(save_data[3]);
                        Location l = new Location(w, x, y, z);
                        builder.buildOuterTARDIS(id, l, d, cham, player);
                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The TARDIS Police Box was rebuilt!");

                        return true;
                    } else {
                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + Constants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("list")) {
                    if (player.hasPermission("TARDIS.list")) {
                        try {
                            Connection connection = service.getConnection();
                            Statement statement = connection.createStatement();
                            ResultSet rs = service.getTardis(player.getName(), "owner");
                            if (rs == null || !rs.next()) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " " + Constants.NO_TARDIS);
                                return false;
                            }
                            if (args.length < 2 || (!args[1].equalsIgnoreCase("saves") && !args[1].equalsIgnoreCase("companions"))) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " You need to specify which TARDIS list you want to view! [saves|companions]");
                                return false;
                            }
                            Constants.list(player, args[1]);
                            rs.close();
                            statement.close();
                            return true;
                        } catch (SQLException e) {
                            System.err.println(Constants.MY_PLUGIN_NAME + " List Companions Error: " + e);
                        }
                    } else {
                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + Constants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("find")) {
                    if (player.hasPermission("TARDIS.find")) {
                        try {
                            Connection connection = service.getConnection();
                            Statement statement = connection.createStatement();
                            ResultSet rs = service.getTardis(player.getName(), "save");
                            if (rs == null || !rs.next()) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " " + Constants.NO_TARDIS);
                                return false;
                            }
                            String loc = rs.getString("save");
                            String[] findData = loc.split(":");
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " You you left your TARDIS in " + findData[0] + " at x:" + findData[1] + " y:" + findData[2] + " z:" + findData[3]);
                            rs.close();
                            statement.close();
                            return true;
                        } catch (SQLException e) {
                            System.err.println(Constants.MY_PLUGIN_NAME + " Find TARDIS Error: " + e);
                        }
                    } else {
                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + Constants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("add")) {
                    if (player.hasPermission("TARDIS.add")) {
                        try {
                            Connection connection = service.getConnection();
                            Statement statement = connection.createStatement();
                            String queryList = "SELECT tardis_id, companions FROM tardis WHERE owner = '" + player.getName() + "'";
                            ResultSet rs = statement.executeQuery(queryList);
                            String comps;
                            int id;
                            if (rs == null || !rs.next()) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " " + Constants.NO_TARDIS);
                                return false;
                            } else {
                                comps = rs.getString("companions");
                                id = rs.getInt("tardis_id");
                                rs.close();
                            }
                            if (args.length < 2) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Too few command arguments!");
                                return false;
                            }
                            if (!args[1].matches("[A-Za-z0-9_]{2,16}")) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + "That doesn't appear to be a valid username");
                                return false;
                            } else {
                                String queryCompanions;
                                if (comps != null && !comps.equals("") && !comps.equals("[Null]")) {
                                    // add to the list
                                    String newList = comps + ":" + args[1].toLowerCase();
                                    queryCompanions = "UPDATE tardis SET companions = '" + newList + "' WHERE tardis_id = " + id;
                                } else {
                                    // make a list
                                    queryCompanions = "UPDATE tardis SET companions = '" + args[1].toLowerCase() + "' WHERE tardis_id = " + id;
                                }
                                statement.executeUpdate(queryCompanions);
                                player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " You added " + ChatColor.GREEN + args[1] + ChatColor.RESET + " as a TARDIS companion.");
                                statement.close();
                                return true;
                            }
                        } catch (SQLException e) {
                            System.err.println(Constants.MY_PLUGIN_NAME + " Companion Save Error: " + e);
                        }
                    } else {
                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + Constants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    if (player.hasPermission("TARDIS.add")) {
                        try {
                            Connection connection = service.getConnection();
                            Statement statement = connection.createStatement();
                            String queryList = "SELECT tardis_id, companions FROM tardis WHERE owner = '" + player.getName() + "'";
                            ResultSet rs = statement.executeQuery(queryList);
                            String comps;
                            int id;
                            if (rs == null || !rs.next()) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " " + Constants.NO_TARDIS);
                                return false;
                            } else {
                                id = rs.getInt("tardis_id");
                                comps = rs.getString("companions");
                                rs.close();
                            }
                            if (comps.equals("") || comps.equals("[Null]") || comps == null) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " You have not added any TARDIS companions yet!");
                                return false;
                            }
                            if (args.length < 2) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Too few command arguments!");
                                return false;
                            }
                            if (!args[1].matches("[A-Za-z0-9_]{2,16}")) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + "That doesn't appear to be a valid username");
                                return false;
                            } else {
                                String[] split = comps.split(":");
                                String newList = "";
                                // recompile string without the specified player
                                for (String c : split) {
                                    if (!c.equals(args[1].toLowerCase())) {
                                        // add to new string
                                        newList += c + ":";
                                    }
                                }
                                // remove trailing colon
                                newList = newList.substring(0, newList.length() - 1);
                                String queryCompanions = "UPDATE tardis SET companions = '" + newList + "' WHERE tardis_id = " + id;
                                statement.executeUpdate(queryCompanions);
                                player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " You removed " + ChatColor.GREEN + args[1] + ChatColor.RESET + " as a TARDIS companion.");
                                statement.close();
                                return true;
                            }
                        } catch (SQLException e) {
                            System.err.println(Constants.MY_PLUGIN_NAME + " Companion Save Error: " + e);
                        }
                    } else {
                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + Constants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("save")) {
                    if (player.hasPermission("TARDIS.save")) {
                        try {
                            Connection connection = service.getConnection();
                            Statement statement = connection.createStatement();
                            String queryList = "SELECT * FROM tardis WHERE owner = '" + player.getName() + "'";
                            ResultSet rs = statement.executeQuery(queryList);
                            if (rs == null || !rs.next()) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " " + Constants.NO_TARDIS);
                                return false;
                            }
                            if (args.length < 3) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Too few command arguments!");
                                return false;
                            } else {
                                String cur = rs.getString("current");
                                String sav = rs.getString("save");
                                int id = rs.getInt("tardis_id");
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
                                String queryTraveller = "SELECT * FROM travellers WHERE player = '" + player.getName() + "'";
                                ResultSet rsTraveller = statement.executeQuery(queryTraveller);
                                if (rsTraveller != null && rsTraveller.next()) {
                                    // inside TARDIS
                                    curDest = nodots + "~" + cur;
                                } else {
                                    // outside TARDIS
                                    curDest = nodots + "~" + sav;
                                }
                                String querySave = "UPDATE tardis SET save" + args[1] + " = '" + curDest + "' WHERE tardis_id = " + id;
                                statement.executeUpdate(querySave);
                                rs.close();
                                rsTraveller.close();
                                statement.close();
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The location '" + nodots + "' was saved successfully.");
                                return true;
                            }
                        } catch (SQLException e) {
                            System.err.println(Constants.MY_PLUGIN_NAME + " Location Save Error: " + e);
                        }
                    } else {
                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + Constants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("setdest")) {
                    if (player.hasPermission("TARDIS.save")) {
                        try {
                            Connection connection = service.getConnection();
                            Statement statement = connection.createStatement();
                            String queryList = "SELECT * FROM tardis WHERE owner = '" + player.getName() + "'";
                            ResultSet rs = statement.executeQuery(queryList);
                            if (rs == null || !rs.next()) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " " + Constants.NO_TARDIS);
                                return false;
                            }
                            if (args.length < 2) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Too few command arguments!");
                                return false;
                            }
                            if (!args[1].matches("[A-Za-z0-9_]{2,16}")) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + "The destination name must be between 2 and 16 characters and have no spaces!");
                                return false;
                            } else {
                                int id = rs.getInt("tardis_id");
                                // get location player is looking at
                                Block b = player.getTargetBlock(null, 50);
                                Location l = b.getLocation();
                                TARDISArea ta = new TARDISArea(plugin);
                                if (ta.areaCheckLocPlayer(player, l)) {
                                    sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + "You do not have permission [" + plugin.trackPerm.get(player.getName()) + "] to set the TARDIS destination to this location!");
                                    plugin.trackPerm.remove(player.getName());
                                    return false;
                                }
                                String dw = l.getWorld().getName();
                                int dx = l.getBlockX();
                                int dy = l.getBlockY() + 1;
                                int dz = l.getBlockZ();
                                String querySetDest = "INSERT INTO destinations (tardis_id, dest_name, world, x, y, z) VALUES (" + id + ", '" + args[1] + "', '" + dw + "', " + dx + ", " + dy + ", " + dz + ")";
                                statement.executeUpdate(querySetDest);
                                rs.close();
                                statement.close();
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The destination '" + args[1] + "' was saved successfully.");
                                return true;
                            }
                        } catch (SQLException e) {
                            System.err.println(Constants.MY_PLUGIN_NAME + " Destination Save Error: " + e);
                        }
                    } else {
                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + Constants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("chameleon")) {
                    if (player.hasPermission("TARDIS.timetravel")) {
                        if (args.length < 2 || (!args[1].equalsIgnoreCase("on") && !args[1].equalsIgnoreCase("off") && !args[1].equalsIgnoreCase("add"))) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Too few command arguments!");
                            return false;
                        }
                        TARDISUtils utils = new TARDISUtils(plugin);
                        // get the players TARDIS id
                        try {
                            Connection connection = service.getConnection();
                            Statement statement = connection.createStatement();
                            ResultSet rs = service.getTardis(player.getName(), "*");
                            if (rs == null || !rs.next()) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " " + Constants.NO_TARDIS);
                                return false;
                            }
                            int id = rs.getInt("tardis_id");
                            String chamStr = rs.getString("chameleon");
                            if (chamStr.equals("")) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Could not find the Chameleon Circuit!");
                                return false;
                            } else {
                                int x = 0, y = 0, z = 0;
                                String[] chamData = chamStr.split(":");
                                World w = plugin.getServer().getWorld(chamData[0]);
                                Constants.COMPASS d = Constants.COMPASS.valueOf(rs.getString("direction"));
                                x = utils.parseNum(chamData[1]);
                                y = utils.parseNum(chamData[2]);
                                z = utils.parseNum(chamData[3]);
                                Block chamBlock = w.getBlockAt(x, y, z);
                                Sign cs = (Sign) chamBlock.getState();
                                if (args[1].equalsIgnoreCase("on")) {
                                    String queryChameleon = "UPDATE tardis SET chamele_on = 1 WHERE tardis_id = " + id;
                                    statement.executeUpdate(queryChameleon);
                                    sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The Chameleon Circuit was turned ON!");
                                    cs.setLine(3, "¤aON");
                                }
                                if (args[1].equalsIgnoreCase("off")) {
                                    String queryChameleon = "UPDATE tardis SET chamele_on = 0 WHERE tardis_id = " + id;
                                    statement.executeUpdate(queryChameleon);
                                    sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The Chameleon Circuit was turned OFF.");
                                    cs.setLine(3, "¤cOFF");
                                }
                                cs.update();
                            }
                            rs.close();
                            statement.close();
                            return true;
                        } catch (SQLException e) {
                            System.err.println(Constants.MY_PLUGIN_NAME + " Chameleon Circuit Save Error: " + e);
                        }
                    } else {
                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + Constants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("sfx")) {
                    if (player.hasPermission("TARDIS.timetravel")) {
                        if (args.length < 2 || (!args[1].equalsIgnoreCase("on") && !args[1].equalsIgnoreCase("off"))) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " You need to specify if sound effects should be on or off!");
                            return false;
                        }
                        // get the players sfx setting
                        try {
                            Connection connection = service.getConnection();
                            Statement statement = connection.createStatement();
                            String querySFX = "SELECT * FROM player_prefs WHERE player = '" + player.getName() + "'";
                            ResultSet rs = statement.executeQuery(querySFX);
                            if (rs == null || !rs.next()) {
                                String queryInsert = "INSERT INTO player_prefs (player) VALUES ('" + player.getName() + "')";
                                statement.executeUpdate(queryInsert);
                            }
                            if (args[1].equalsIgnoreCase("on")) {
                                String queryUpdate = "UPDATE player_prefs SET sfx_on = 1 WHERE player = '" + player.getName() + "'";
                                statement.executeUpdate(queryUpdate);
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Sound effects were turned ON!");
                            }
                            if (args[1].equalsIgnoreCase("off")) {
                                String queryUpdate = "UPDATE player_prefs SET sfx_on = 0 WHERE player = '" + player.getName() + "'";
                                statement.executeUpdate(queryUpdate);
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Sound effects were turned OFF.");
                            }
                            rs.close();
                            statement.close();
                            return true;
                        } catch (SQLException e) {
                            System.err.println(Constants.MY_PLUGIN_NAME + " SFX Preferences Save Error: " + e);
                        }
                    } else {
                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + Constants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("platform")) {
                    if (player.hasPermission("TARDIS.timetravel")) {
                        if (args.length < 2 || (!args[1].equalsIgnoreCase("on") && !args[1].equalsIgnoreCase("off"))) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " You need to specify if sound effects should be on or off!");
                            return false;
                        }
                        // get the players platform setting
                        try {
                            Connection connection = service.getConnection();
                            Statement statement = connection.createStatement();
                            String queryPlatform = "SELECT * FROM player_prefs WHERE player = '" + player.getName() + "'";
                            ResultSet rs = statement.executeQuery(queryPlatform);
                            if (rs == null || !rs.next()) {
                                String queryInsert = "INSERT INTO player_prefs (player) VALUES ('" + player.getName() + "')";
                                statement.executeUpdate(queryInsert);
                            }
                            if (args[1].equalsIgnoreCase("on")) {
                                String queryUpdate = "UPDATE player_prefs SET platform_on = 1 WHERE player = '" + player.getName() + "'";
                                statement.executeUpdate(queryUpdate);
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The safety platform was turned ON!");
                            }
                            if (args[1].equalsIgnoreCase("off")) {
                                String queryUpdate = "UPDATE player_prefs SET platform_on = 0 WHERE player = '" + player.getName() + "'";
                                statement.executeUpdate(queryUpdate);
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " safety platform was turned OFF.");
                            }
                            rs.close();
                            statement.close();
                            return true;
                        } catch (SQLException e) {
                            System.err.println(Constants.MY_PLUGIN_NAME + " Platform Preferences Save Error: " + e);
                        }
                    } else {
                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + Constants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("quotes")) {
                    if (player.hasPermission("TARDIS.timetravel")) {
                        if (args.length < 2 || (!args[1].equalsIgnoreCase("on") && !args[1].equalsIgnoreCase("off"))) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " You need to specify if Who quotes should be on or off!");
                            return false;
                        }
                        // get the players quotes setting
                        try {
                            Connection connection = service.getConnection();
                            Statement statement = connection.createStatement();
                            String queryPlatform = "SELECT * FROM player_prefs WHERE player = '" + player.getName() + "'";
                            ResultSet rs = statement.executeQuery(queryPlatform);
                            if (rs == null || !rs.next()) {
                                String queryInsert = "INSERT INTO player_prefs (player) VALUES ('" + player.getName() + "')";
                                statement.executeUpdate(queryInsert);
                            }
                            if (args[1].equalsIgnoreCase("on")) {
                                String queryUpdate = "UPDATE player_prefs SET quotes_on = 1 WHERE player = '" + player.getName() + "'";
                                statement.executeUpdate(queryUpdate);
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Quotes were turned ON!");
                            }
                            if (args[1].equalsIgnoreCase("off")) {
                                String queryUpdate = "UPDATE player_prefs SET quotes_on = 0 WHERE player = '" + player.getName() + "'";
                                statement.executeUpdate(queryUpdate);
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Quotes were turned OFF.");
                            }
                            rs.close();
                            statement.close();
                            return true;
                        } catch (SQLException e) {
                            System.err.println(Constants.MY_PLUGIN_NAME + " Quotes Preferences Save Error: " + e);
                        }
                    } else {
                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + Constants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("direction")) {
                    if (player.hasPermission("TARDIS.timetravel")) {
                        if (args.length < 2 || (!args[1].equalsIgnoreCase("north") && !args[1].equalsIgnoreCase("west") && !args[1].equalsIgnoreCase("south") && !args[1].equalsIgnoreCase("east"))) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " You need to specify the compass direction e.g. north, west, south or east!");
                            return false;
                        }
                        try {
                            Connection connection = service.getConnection();
                            Statement statement = connection.createStatement();
                            ResultSet rs = service.getTardis(player.getName(), "*");
                            if (rs == null || !rs.next()) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " " + Constants.NO_TARDIS);
                                return false;
                            }
                            TARDISUtils utils = new TARDISUtils(plugin);
                            String save = rs.getString("save");
                            String[] save_data = save.split(":");
                            int id = rs.getInt("tardis_id");
                            boolean cham = rs.getBoolean("chamele_on");
                            String dir = args[1].toUpperCase();
                            Constants.COMPASS old_d = Constants.COMPASS.valueOf(rs.getString("direction"));

                            String queryDirectionUpdate = "UPDATE tardis SET direction = '" + dir + "' WHERE tardis_id = " + id;
                            statement.executeUpdate(queryDirectionUpdate);
                            World w = plugin.getServer().getWorld(save_data[0]);
                            int x = utils.parseNum(save_data[1]);
                            int y = utils.parseNum(save_data[2]);
                            int z = utils.parseNum(save_data[3]);
                            Location l = new Location(w, x, y, z);
                            Constants.COMPASS d = Constants.COMPASS.valueOf(dir);
                            TARDISDestroyer destroyer = new TARDISDestroyer(plugin);
                            destroyer.destroySign(l, old_d);
                            TARDISBuilder builder = new TARDISBuilder(plugin);
                            builder.buildOuterTARDIS(id, l, d, cham, player);
                            //sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The TARDIS Police Box was rebuilt!");
                            rs.close();
                            statement.close();
                            return true;
                        } catch (SQLException e) {
                            System.err.println(Constants.MY_PLUGIN_NAME + " Quotes Preferences Save Error: " + e);
                        }
                    } else {
                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + Constants.NO_PERMS_MESSAGE);
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
                            case ADD:
                                sender.sendMessage(Constants.COMMAND_ADD.split("\n"));
                                break;
                            case TRAVEL:
                                sender.sendMessage(Constants.COMMAND_TRAVEL.split("\n"));
                                break;
                            case UPDATE:
                                sender.sendMessage(Constants.COMMAND_UPDATE.split("\n"));
                                break;
                            case REBUILD:
                                sender.sendMessage(Constants.COMMAND_REBUILD.split("\n"));
                                break;
                            case CHAMELEON:
                                sender.sendMessage(Constants.COMMAND_CHAMELEON.split("\n"));
                                break;
                            case SFX:
                                sender.sendMessage(Constants.COMMAND_SFX.split("\n"));
                                break;
                            case PLATFORM:
                                sender.sendMessage(Constants.COMMAND_PLATFORM.split("\n"));
                                break;
                            case SETDEST:
                                sender.sendMessage(Constants.COMMAND_SETDEST.split("\n"));
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
