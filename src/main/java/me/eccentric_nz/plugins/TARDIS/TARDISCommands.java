package me.eccentric_nz.plugins.TARDIS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;

public class TARDISCommands implements CommandExecutor {

    private TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();
    HashSet<Byte> transparent = new HashSet<Byte>();

    public TARDISCommands(TARDIS plugin) {
        this.plugin = plugin;
        transparent.add((byte) Material.AIR.getId());
        transparent.add((byte) Material.SNOW.getId());
        transparent.add((byte) Material.LONG_GRASS.getId());
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
            if (!args[0].equalsIgnoreCase("chameleon") && !args[0].equalsIgnoreCase("save") && !args[0].equalsIgnoreCase("removesave") && !args[0].equalsIgnoreCase("list") && !args[0].equalsIgnoreCase("help") && !args[0].equalsIgnoreCase("find") && !args[0].equalsIgnoreCase("reload") && !args[0].equalsIgnoreCase("add") && !args[0].equalsIgnoreCase("remove") && !args[0].equalsIgnoreCase("update") && !args[0].equalsIgnoreCase("rebuild") && !args[0].equalsIgnoreCase("comehere") && !args[0].equalsIgnoreCase("direction") && !args[0].equalsIgnoreCase("setdest") && !args[0].equalsIgnoreCase("hide") && !args[0].equalsIgnoreCase("home") && !args[0].equalsIgnoreCase("occupy") && !args[0].equalsIgnoreCase("namekey")) {
                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " That command wasn't recognised type " + ChatColor.GREEN + "/tardis help" + ChatColor.RESET + " to see the commands");
                return false;
            }
            if (player == null) {
                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + " This command can only be run by a player");
                return false;
            } else {
                if (args[0].equalsIgnoreCase("chameleon")) {
                    if (player.hasPermission("tardis.timetravel")) {
                        if (args.length < 2 || (!args[1].equalsIgnoreCase("on") && !args[1].equalsIgnoreCase("off") && !args[1].equalsIgnoreCase("add"))) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Too few command arguments!");
                            return false;
                        }
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
                                x = plugin.utils.parseNum(chamData[1]);
                                y = plugin.utils.parseNum(chamData[2]);
                                z = plugin.utils.parseNum(chamData[3]);
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
                if (args[0].equalsIgnoreCase("occupy")) {
                    if (player.hasPermission("tardis.timetravel")) {
                        try {
                            Connection connection = service.getConnection();
                            Statement statement = connection.createStatement();
                            ResultSet rs = service.getTardis(player.getName(), "tardis_id");
                            if (!rs.next()) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " You must be the Timelord of the TARDIS to use this command!");
                                return false;
                            }
                            int id = rs.getInt("tardis_id");
                            rs.close();
                            String queryOccupied = "SELECT * FROM travellers WHERE tardis_id = " + id + " AND player = '" + player.getName() + "'";
                            ResultSet rsOccupied = statement.executeQuery(queryOccupied);
                            String queryOcc;
                            String occupied;
                            if (rsOccupied.next()) {
                                queryOcc = "DELETE FROM travellers WHERE tardis_id = " + id + " AND player = '" + player.getName() + "'";
                                occupied = ChatColor.RED + "UNOCCUPIED";
                            } else {
                                queryOcc = "INSERT INTO travellers (tardis_id,player) VALUES (" + id + ",'" + player.getName() + "')";
                                occupied = ChatColor.GREEN + "OCCUPIED";
                            }
                            statement.executeUpdate(queryOcc);
                            sender.sendMessage(Constants.MY_PLUGIN_NAME + " TARDIS occupation was set to: " + occupied);
                            return true;
                        } catch (SQLException e) {
                            System.err.println(Constants.MY_PLUGIN_NAME + " Couldn't get TARDIS: " + e);
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("comehere")) {
                    if (player.hasPermission("tardis.timetravel")) {
                        final Location eyeLocation = player.getTargetBlock(transparent, 50).getLocation();
                        if (!plugin.config.getBoolean("include_default_world") && plugin.config.getBoolean("default_world") && eyeLocation.getWorld().getName().equals(plugin.config.getString("default_world_name"))) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The server admin will not allow you to bring the TARDIS to this world!");
                            return true;
                        }
                        if (plugin.WorldGuardOnServer && plugin.config.getBoolean("respect_worldguard")) {
                            if (plugin.wgchk.cantBuild(player, eyeLocation)) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + "That location is protected by WorldGuard!");
                                return false;
                            }
                        }
                        if (player.hasPermission("tardis.exile")) {
                            String areaPerm = plugin.ta.getExileArea(player);
                            if (plugin.ta.areaCheckInExile(areaPerm, eyeLocation)) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + "You exile status does not allow you to bring the TARDIS to this location!");
                                return false;
                            }
                        }
                        if (plugin.ta.areaCheckLocPlayer(player, eyeLocation)) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + "You do not have permission [" + plugin.trackPerm.get(player.getName()) + "] to bring the TARDIS to this location!");
                            plugin.trackPerm.remove(player.getName());
                            return false;
                        }
                        Material m = player.getTargetBlock(transparent, 50).getType();
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
                            int x, y, z;
                            x = plugin.utils.parseNum(saveData[1]);
                            y = plugin.utils.parseNum(saveData[2]);
                            z = plugin.utils.parseNum(saveData[3]);
                            final Location oldSave = w.getBlockAt(x, y, z).getLocation();
                            rs.close();
                            String comehere = eyeLocation.getWorld().getName() + ":" + eyeLocation.getBlockX() + ":" + eyeLocation.getBlockY() + ":" + eyeLocation.getBlockZ();
                            String querySave = "UPDATE tardis SET save = '" + comehere + "', current = '" + comehere + "' WHERE tardis_id = " + id;
                            statement.executeUpdate(querySave);
                            // how many travellers are in the TARDIS?
                            plugin.utils.updateTravellerCount(id);
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The TARDIS is coming...");
                            long delay = 100L;
                            if (plugin.getServer().getPluginManager().getPlugin("Spout") != null && SpoutManager.getPlayer(player).isSpoutCraftEnabled()) {
                                SpoutManager.getSoundManager().playCustomSoundEffect(plugin, SpoutManager.getPlayer(player), "https://dl.dropbox.com/u/53758864/tardis_land.mp3", false, eyeLocation, 9, 75);
                                delay = 400L;
                            }
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    plugin.destroyer.destroySign(oldSave, d);
                                    plugin.destroyer.destroyTorch(oldSave);
                                    plugin.destroyer.destroyBlueBox(oldSave, d, id);
                                    plugin.builder.buildOuterTARDIS(id, eyeLocation, d, cham, p, false);
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
                if (args[0].equalsIgnoreCase("home")) {
                    if (player.hasPermission("tardis.timetravel")) {
                        Location eyeLocation = player.getTargetBlock(transparent, 50).getLocation();
                        if (!plugin.config.getBoolean("include_default_world") && plugin.config.getBoolean("default_world") && eyeLocation.getWorld().getName().equals(plugin.config.getString("default_world_name"))) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The server admin will not allow you to set the TARDIS home in this world!");
                            return true;
                        }
                        if (plugin.WorldGuardOnServer && plugin.config.getBoolean("respect_worldguard")) {
                            if (plugin.wgchk.cantBuild(player, eyeLocation)) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + "That location is protected by WorldGuard!");
                                return false;
                            }
                        }
                        if (plugin.ta.areaCheckLocPlayer(player, eyeLocation)) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + "You do not have permission [" + plugin.trackPerm.get(player.getName()) + "] to set the TARDIS home to this location!");
                            plugin.trackPerm.remove(player.getName());
                            return false;
                        }
                        Material m = player.getTargetBlock(transparent, 50).getType();
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
                            int id = rs.getInt("tardis_id");
                            rs.close();
                            String sethome = eyeLocation.getWorld().getName() + ":" + eyeLocation.getBlockX() + ":" + eyeLocation.getBlockY() + ":" + eyeLocation.getBlockZ();
                            String querySave = "UPDATE tardis SET home = '" + sethome + "' WHERE tardis_id = " + id;
                            statement.executeUpdate(querySave);
                            statement.close();
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The new TARDIS home was set!");
                        } catch (SQLException e) {
                            System.err.println(Constants.MY_PLUGIN_NAME + "Couldn't get TARDIS: " + e);
                        }
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + Constants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("update")) {
                    if (player.hasPermission("tardis.update")) {
                        String[] validBlockNames = {"door", "button", "save-repeater", "x-repeater", "z-repeater", "y-repeater", "chameleon", "save-sign"};
                        if (args.length < 2) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Too few command arguments!");
                            return false;
                        }
                        if (!Arrays.asList(validBlockNames).contains(args[1].toLowerCase())) {
                            player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " That is not a valid TARDIS block name! Try one of : door|button|save-repeater|x-repeater|z-repeater|y-repeater|chameleon|save-sign");
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
                if (args[0].equalsIgnoreCase("rebuild") || args[0].equalsIgnoreCase("hide")) {
                    if (player.hasPermission("tardis.rebuild")) {
                        String save = "";
                        World w;
                        int x = 0, y = 0, z = 0, id = -1;
                        Constants.COMPASS d = Constants.COMPASS.EAST;
                        boolean cham = false;
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
                        String[] save_data = save.split(":");
                        w = plugin.getServer().getWorld(save_data[0]);
                        x = plugin.utils.parseNum(save_data[1]);
                        y = plugin.utils.parseNum(save_data[2]);
                        z = plugin.utils.parseNum(save_data[3]);
                        Location l = new Location(w, x, y, z);
                        if (args[0].equalsIgnoreCase("rebuild")) {
                            plugin.builder.buildOuterTARDIS(id, l, d, cham, player, true);
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The TARDIS Police Box was rebuilt!");
                            return true;
                        }
                        if (args[0].equalsIgnoreCase("hide")) {
                            // remove torch
                            plugin.destroyer.destroyTorch(l);
                            // remove sign
                            plugin.destroyer.destroySign(l, d);
                            // remove blue box
                            plugin.destroyer.destroyBlueBox(l, d, id);
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The TARDIS Police Box was hidden! Use " + ChatColor.GREEN + "/tardis rebuild" + ChatColor.RESET + " to show it again.");
                            return true;
                        }
                    } else {
                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + Constants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("list")) {
                    if (player.hasPermission("tardis.list")) {
                        try {
                            Connection connection = service.getConnection();
                            Statement statement = connection.createStatement();
                            ResultSet rs = service.getTardis(player.getName(), "owner");
                            if (rs == null || !rs.next()) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " " + Constants.NO_TARDIS);
                                return false;
                            }
                            if (args.length < 2 || (!args[1].equalsIgnoreCase("saves") && !args[1].equalsIgnoreCase("companions") && !args[1].equalsIgnoreCase("areas"))) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " You need to specify which TARDIS list you want to view! [saves|companions|areas]");
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
                    if (player.hasPermission("tardis.find")) {
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
                    if (player.hasPermission("tardis.add")) {
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
                    if (player.hasPermission("tardis.add")) {
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
                                if (split.length > 1) {
                                    // recompile string without the specified player
                                    for (String c : split) {
                                        if (!c.equals(args[1].toLowerCase())) {
                                            // add to new string
                                            newList += c + ":";
                                        }
                                    }
                                    // remove trailing colon
                                    newList = newList.substring(0, newList.length() - 1);
                                } else {
                                    newList = "";
                                }
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
                    if (player.hasPermission("tardis.save")) {
                        try {
                            Connection connection = service.getConnection();
                            Statement statement = connection.createStatement();
                            ResultSet rs = service.getTardis(player.getName(), "*");
                            if (rs == null || !rs.next()) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " " + Constants.NO_TARDIS);
                                return false;
                            }
                            if (args.length < 2) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Too few command arguments!");
                                return false;
                            }
                            if (!args[1].matches("[A-Za-z0-9_]{2,16}")) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + "That doesn't appear to be a valid save name (it may be too long or contains spaces).");
                                return false;
                            } else {
                                String cur = rs.getString("current");
                                String sav = rs.getString("save");
                                int id = rs.getInt("tardis_id");
                                String[] curDest;
                                // get current destination
                                String queryTraveller = "SELECT * FROM travellers WHERE player = '" + player.getName() + "'";
                                ResultSet rsTraveller = statement.executeQuery(queryTraveller);
                                if (rsTraveller != null && rsTraveller.next()) {
                                    // inside TARDIS
                                    curDest = cur.split(":");
                                } else {
                                    // outside TARDIS
                                    curDest = sav.split(":");
                                }
                                PreparedStatement psSave = connection.prepareStatement("INSERT INTO destinations (tardis_id,dest_name, world, x, y, z) VALUES (?,?,?,?,?,?)");
                                psSave.setInt(1, id);
                                psSave.setString(2, args[1]);
                                psSave.setString(3, curDest[0]);
                                psSave.setInt(4, plugin.utils.parseNum(curDest[1]));
                                psSave.setInt(5, plugin.utils.parseNum(curDest[2]));
                                psSave.setInt(6, plugin.utils.parseNum(curDest[3]));
                                psSave.executeUpdate();
                                rs.close();
                                rsTraveller.close();
                                statement.close();
                                psSave.close();
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The location '" + args[1] + "' was saved successfully.");
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
                if (args[0].equalsIgnoreCase("removesave")) {
                    if (player.hasPermission("tardis.save")) {
                        if (args.length < 2) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Too few command arguments!");
                            return false;
                        }
                        try {
                            Connection connection = service.getConnection();
                            Statement statement = connection.createStatement();
                            ResultSet rs = service.getTardis(player.getName(), "tardis_id");
                            if (rs == null || !rs.next()) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " " + Constants.NO_TARDIS);
                                return false;
                            }
                            int id = rs.getInt("tardis_id");
                            String queryDest = "SELECT dest_id FROM destinations WHERE dest_name = '" + args[1] + "' AND tardis_id = " + id;
                            ResultSet rsDest = statement.executeQuery(queryDest);
                            if (rsDest == null || !rsDest.next()) {
                                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Could not find a saved destination with that name!");
                                return false;
                            }
                            int destID = rsDest.getInt("dest_id");
                            String queryDelete = "DELETE FROM destinations WHERE dest_id = " + destID;
                            statement.executeUpdate(queryDelete);
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The destination " + args[1] + " was deleted!");
                            return true;
                        } catch (SQLException e) {
                            System.err.println(Constants.MY_PLUGIN_NAME + " Destination Save Error: " + e);
                        }
                    } else {
                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + Constants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("setdest")) {
                    if (player.hasPermission("tardis.save")) {
                        try {
                            Connection connection = service.getConnection();
                            Statement statement = connection.createStatement();
                            ResultSet rs = service.getTardis(player.getName(), "*");
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
                                Block b = player.getTargetBlock(transparent, 50);
                                Location l = b.getLocation();
                                if (!plugin.config.getBoolean("include_default_world") && plugin.config.getBoolean("default_world") && l.getWorld().getName().equals(plugin.config.getString("default_world_name"))) {
                                    sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The server admin will not allow you to set the TARDIS destination to this world!");
                                    return true;
                                }
                                if (plugin.WorldGuardOnServer && plugin.config.getBoolean("respect_worldguard")) {
                                    if (plugin.wgchk.cantBuild(player, l)) {
                                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + "That location is protected by WorldGuard!");
                                        return false;
                                    }
                                }
                                if (player.hasPermission("tardis.exile")) {
                                    String areaPerm = plugin.ta.getExileArea(player);
                                    if (plugin.ta.areaCheckInExile(areaPerm, l)) {
                                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + "You exile status does not allow you to save the TARDIS to this location!");
                                        return false;
                                    }
                                }
                                if (plugin.ta.areaCheckLocPlayer(player, l)) {
                                    sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + "You do not have permission [" + plugin.trackPerm.get(player.getName()) + "] to set the TARDIS destination to this location!");
                                    plugin.trackPerm.remove(player.getName());
                                    return false;
                                }
                                String dw = l.getWorld().getName();
                                int dx = l.getBlockX();
                                int dy = l.getBlockY() + 1;
                                int dz = l.getBlockZ();
                                PreparedStatement psSetDest = connection.prepareStatement("INSERT INTO destinations (tardis_id, dest_name, world, x, y, z) VALUES (?,?,?,?,?,?)");
                                psSetDest.setInt(1, id);
                                psSetDest.setString(2, args[1]);
                                psSetDest.setString(3, dw);
                                psSetDest.setInt(4, dx);
                                psSetDest.setInt(5, dy);
                                psSetDest.setInt(6, dz);
                                psSetDest.executeUpdate();
                                rs.close();
                                statement.close();
                                psSetDest.close();
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
                if (args[0].equalsIgnoreCase("direction")) {
                    if (player.hasPermission("tardis.timetravel")) {
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
                            String save = rs.getString("save");
                            String[] save_data = save.split(":");
                            int id = rs.getInt("tardis_id");
                            boolean cham = rs.getBoolean("chamele_on");
                            String dir = args[1].toUpperCase();
                            Constants.COMPASS old_d = Constants.COMPASS.valueOf(rs.getString("direction"));

                            String queryDirectionUpdate = "UPDATE tardis SET direction = '" + dir + "' WHERE tardis_id = " + id;
                            statement.executeUpdate(queryDirectionUpdate);
                            String queryDoorDirectionUpdate = "UPDATE doors SET door_direction = '" + dir + "' WHERE door_type = 0 AND tardis_id = " + id;
                            statement.executeUpdate(queryDoorDirectionUpdate);
                            World w = plugin.getServer().getWorld(save_data[0]);
                            int x = plugin.utils.parseNum(save_data[1]);
                            int y = plugin.utils.parseNum(save_data[2]);
                            int z = plugin.utils.parseNum(save_data[3]);
                            Location l = new Location(w, x, y, z);
                            Constants.COMPASS d = Constants.COMPASS.valueOf(dir);
                            plugin.destroyer.destroySign(l, old_d);
                            plugin.builder.buildOuterTARDIS(id, l, d, cham, player, true);
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
                if (args[0].equalsIgnoreCase("namekey")) {
                    Material m = Material.getMaterial(plugin.config.getString("key"));
                    ItemStack is = player.getItemInHand();
                    if (!is.getType().equals(m)) {
                        sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + "You can only rename the TARDIS key!");
                        return false;
                    }
                    int count = args.length;
                    if (count < 2) {
                        return false;
                    }
                    StringBuilder buf = new StringBuilder(args[1]);
                    for (int i = 2; i < count; i++) {
                        buf.append(" ").append(args[i]);
                    }
                    String tmp = buf.toString();
                    if (is != null) {
                        TARDISItemRenamer ir = new TARDISItemRenamer(is);
                        ir.setName(tmp);
                    }
                    sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + "TARDIS key renamed to '" + tmp + "'");
                    return true;
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
                            case REMOVESAVE:
                                sender.sendMessage(Constants.COMMAND_REMOVESAVE.split("\n"));
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
                            case HOME:
                                sender.sendMessage(Constants.COMMAND_HOME.split("\n"));
                                break;
                            case HIDE:
                                sender.sendMessage(Constants.COMMAND_HIDE.split("\n"));
                                break;
                            case ADMIN:
                                sender.sendMessage(Constants.COMMAND_ADMIN.split("\n"));
                                break;
                            case AREA:
                                sender.sendMessage(Constants.COMMAND_AREA.split("\n"));
                                break;
                            default:
                                sender.sendMessage(Constants.COMMANDS.split("\n"));
                        }
                    }
                    return true;
                }
            }
        }
        //If the above has happened the function will break and return true. if this hasn't happened then value of false will be returned.
        return false;
    }
}