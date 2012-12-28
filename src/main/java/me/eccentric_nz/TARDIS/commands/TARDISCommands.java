package me.eccentric_nz.TARDIS.commands;

import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.utility.TARDISItemRenamer;
import me.eccentric_nz.TARDIS.utility.TARDISLister;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;

public class TARDISCommands implements CommandExecutor {

    private TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();
    HashSet<Byte> transparent = new HashSet<Byte>();
    private List<String> firstArgs = new ArrayList<String>();

    public TARDISCommands(TARDIS plugin) {
        this.plugin = plugin;
        // add transparent blocks
        transparent.add((byte) Material.AIR.getId());
        transparent.add((byte) Material.SNOW.getId());
        transparent.add((byte) Material.LONG_GRASS.getId());
        transparent.add((byte) Material.VINE.getId());
        // add first arguments
        firstArgs.add("chameleon");
        firstArgs.add("save");
        firstArgs.add("removesave");
        firstArgs.add("list");
        firstArgs.add("help");
        firstArgs.add("find");
        firstArgs.add("reload");
        firstArgs.add("add");
        firstArgs.add("remove");
        firstArgs.add("update");
        firstArgs.add("rebuild");
        firstArgs.add("comehere");
        firstArgs.add("direction");
        firstArgs.add("setdest");
        firstArgs.add("hide");
        firstArgs.add("home");
        firstArgs.add("namekey");
        firstArgs.add("version");
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
                sender.sendMessage(TARDISConstants.COMMANDS.split("\n"));
                return true;
            }
            // the command list - first argument MUST appear here!
            if (!firstArgs.contains(args[0].toLowerCase())) {
                sender.sendMessage(plugin.pluginName + " That command wasn't recognised type " + ChatColor.GREEN + "/tardis help" + ChatColor.RESET + " to see the commands");
                return false;
            }
            if (args[0].equalsIgnoreCase("version")) {
                FileConfiguration pluginYml = YamlConfiguration.loadConfiguration(plugin.pm.getPlugin("TARDIS").getResource("plugin.yml"));
                String version = pluginYml.getString("version");
                String cb = Bukkit.getVersion();
                sender.sendMessage(plugin.pluginName + " You are running TARDIS version: " + ChatColor.AQUA + version + ChatColor.RESET + " with CraftBukkit " + cb);
                return true;
            }
            if (player == null) {
                sender.sendMessage(plugin.pluginName + ChatColor.RED + " This command can only be run by a player");
                return false;
            } else {
                if (args[0].equalsIgnoreCase("chameleon")) {
                    if (player.hasPermission("tardis.timetravel")) {
                        if (args.length < 2 || (!args[1].equalsIgnoreCase("on") && !args[1].equalsIgnoreCase("off"))) {
                            sender.sendMessage(plugin.pluginName + " Too few command arguments!");
                            return false;
                        }
                        // get the players TARDIS id
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("owner", player.getName());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (!rs.resultSet()) {
                            sender.sendMessage(plugin.pluginName + " " + TARDISConstants.NO_TARDIS);
                            return false;
                        }
                        int id = rs.getTardis_id();
                        String chamStr = rs.getChameleon();
                        if (chamStr.equals("")) {
                            sender.sendMessage(plugin.pluginName + " Could not find the Chameleon Circuit!");
                            return false;
                        } else {
                            int x, y, z;
                            String[] chamData = chamStr.split(":");
                            World w = plugin.getServer().getWorld(chamData[0]);
                            TARDISConstants.COMPASS d = rs.getDirection();
                            x = plugin.utils.parseNum(chamData[1]);
                            y = plugin.utils.parseNum(chamData[2]);
                            z = plugin.utils.parseNum(chamData[3]);
                            Block chamBlock = w.getBlockAt(x, y, z);
                            Sign cs = (Sign) chamBlock.getState();
                            QueryFactory qf = new QueryFactory(plugin);
                            HashMap<String, Object> tid = new HashMap<String, Object>();
                            HashMap<String, Object> set = new HashMap<String, Object>();
                            tid.put("tardis_id", id);
                            if (args[1].equalsIgnoreCase("on")) {
                                set.put("chamele_on", 1);
                                qf.doUpdate("tardis", set, tid);
                                sender.sendMessage(plugin.pluginName + " The Chameleon Circuit was turned ON!");
                                cs.setLine(3, ChatColor.GREEN + "ON");
                            }
                            if (args[1].equalsIgnoreCase("off")) {
                                set.put("chamele_on", 0);
                                qf.doUpdate("tardis", set, tid);
                                sender.sendMessage(plugin.pluginName + " The Chameleon Circuit was turned OFF.");
                                cs.setLine(3, ChatColor.RED + "OFF");
                            }
                            cs.update();
                        }
                        return true;
                    } else {
                        sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("comehere")) {
                    if (player.hasPermission("tardis.timetravel")) {
                        final Location eyeLocation = player.getTargetBlock(transparent, 50).getLocation();
                        if (!plugin.getConfig().getBoolean("include_default_world") && plugin.getConfig().getBoolean("default_world") && eyeLocation.getWorld().getName().equals(plugin.getConfig().getString("default_world_name"))) {
                            sender.sendMessage(plugin.pluginName + " The server admin will not allow you to bring the TARDIS to this world!");
                            return true;
                        }
                        if (plugin.worldGuardOnServer && plugin.getConfig().getBoolean("respect_worldguard")) {
                            if (plugin.wgchk.cantBuild(player, eyeLocation)) {
                                sender.sendMessage(plugin.pluginName + "That location is protected by WorldGuard!");
                                return false;
                            }
                        }
                        if (player.hasPermission("tardis.exile")) {
                            String areaPerm = plugin.ta.getExileArea(player);
                            if (plugin.ta.areaCheckInExile(areaPerm, eyeLocation)) {
                                sender.sendMessage(plugin.pluginName + "You exile status does not allow you to bring the TARDIS to this location!");
                                return false;
                            }
                        }
                        if (plugin.ta.areaCheckLocPlayer(player, eyeLocation)) {
                            sender.sendMessage(plugin.pluginName + "You do not have permission [" + plugin.trackPerm.get(player.getName()) + "] to bring the TARDIS to this location!");
                            plugin.trackPerm.remove(player.getName());
                            return false;
                        }
                        Material m = player.getTargetBlock(transparent, 50).getType();
                        if (m != Material.SNOW) {
                            int yplusone = eyeLocation.getBlockY();
                            eyeLocation.setY(yplusone + 1);
                        }
                        // set save location
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("owner", player.getName());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (!rs.resultSet()) {
                            sender.sendMessage(plugin.pluginName + " You must be the Timelord of the TARDIS to use this command!");
                            return false;
                        }
                        final Player p = player;
                        final int id = rs.getTardis_id();
                        String badsave = rs.getSave();
                        final boolean cham = rs.getChameleon_on();
                        final TARDISConstants.COMPASS d = rs.getDirection();
                        String[] saveData = badsave.split(":");
                        World w = plugin.getServer().getWorld(saveData[0]);
                        int x, y, z;
                        x = plugin.utils.parseNum(saveData[1]);
                        y = plugin.utils.parseNum(saveData[2]);
                        z = plugin.utils.parseNum(saveData[3]);
                        final Location oldSave = w.getBlockAt(x, y, z).getLocation();
                        //rs.close();
                        String comehere = eyeLocation.getWorld().getName() + ":" + eyeLocation.getBlockX() + ":" + eyeLocation.getBlockY() + ":" + eyeLocation.getBlockZ();
                        QueryFactory qf = new QueryFactory(plugin);
                        HashMap<String, Object> tid = new HashMap<String, Object>();
                        HashMap<String, Object> set = new HashMap<String, Object>();
                        tid.put("tardis_id", id);
                        set.put("save", comehere);
                        set.put("current", comehere);
                        qf.doUpdate("tardis", set, tid);
                        // how many travellers are in the TARDIS?
                        plugin.utils.updateTravellerCount(id);
                        sender.sendMessage(plugin.pluginName + " The TARDIS is coming...");
                        long delay = 100L;
                        if (plugin.getServer().getPluginManager().getPlugin("Spout") != null && SpoutManager.getPlayer(player).isSpoutCraftEnabled()) {
                            SpoutManager.getSoundManager().playCustomSoundEffect(plugin, SpoutManager.getPlayer(player), "https://dl.dropbox.com/u/53758864/tardis_land.mp3", false, eyeLocation, 9, 75);
                            delay = 400L;
                        }
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                plugin.destroyPB.destroySign(oldSave, d);
                                plugin.destroyPB.destroyTorch(oldSave);
                                plugin.destroyPB.destroyPoliceBox(oldSave, d, id, false);
                                plugin.buildPB.buildPoliceBox(id, eyeLocation, d, cham, p, false);
                            }
                        }, delay);
                        return true;
                    } else {
                        sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("home")) {
                    if (player.hasPermission("tardis.timetravel")) {
                        Location eyeLocation = player.getTargetBlock(transparent, 50).getLocation();
                        if (!plugin.getConfig().getBoolean("include_default_world") && plugin.getConfig().getBoolean("default_world") && eyeLocation.getWorld().getName().equals(plugin.getConfig().getString("default_world_name"))) {
                            sender.sendMessage(plugin.pluginName + " The server admin will not allow you to set the TARDIS home in this world!");
                            return true;
                        }
                        if (plugin.worldGuardOnServer && plugin.getConfig().getBoolean("respect_worldguard")) {
                            if (plugin.wgchk.cantBuild(player, eyeLocation)) {
                                sender.sendMessage(plugin.pluginName + "That location is protected by WorldGuard!");
                                return false;
                            }
                        }
                        if (plugin.ta.areaCheckLocPlayer(player, eyeLocation)) {
                            sender.sendMessage(plugin.pluginName + "You do not have permission [" + plugin.trackPerm.get(player.getName()) + "] to set the TARDIS home to this location!");
                            plugin.trackPerm.remove(player.getName());
                            return false;
                        }
                        Material m = player.getTargetBlock(transparent, 50).getType();
                        if (m != Material.SNOW) {
                            int yplusone = eyeLocation.getBlockY();
                            eyeLocation.setY(yplusone + 1);
                        }
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("owner", player.getName());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (!rs.resultSet()) {
                            sender.sendMessage(plugin.pluginName + " You must be the Timelord of the TARDIS to use this command!");
                            return false;
                        }
                        int id = rs.getTardis_id();
                        String sethome = eyeLocation.getWorld().getName() + ":" + eyeLocation.getBlockX() + ":" + eyeLocation.getBlockY() + ":" + eyeLocation.getBlockZ();
                        QueryFactory qf = new QueryFactory(plugin);
                        HashMap<String, Object> tid = new HashMap<String, Object>();
                        HashMap<String, Object> set = new HashMap<String, Object>();
                        tid.put("tardis_id", id);
                        set.put("home", sethome);
                        qf.doUpdate("tardis", set, tid);
                        sender.sendMessage(plugin.pluginName + " The new TARDIS home was set!");
                        return true;
                    } else {
                        sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("update")) {
                    if (player.hasPermission("tardis.update")) {
                        String[] validBlockNames = {"door", "button", "save-repeater", "x-repeater", "z-repeater", "y-repeater", "chameleon", "save-sign"};
                        if (args.length < 2) {
                            sender.sendMessage(plugin.pluginName + " Too few command arguments!");
                            return false;
                        }
                        if (!Arrays.asList(validBlockNames).contains(args[1].toLowerCase(Locale.ENGLISH))) {
                            player.sendMessage(plugin.pluginName + " That is not a valid TARDIS block name! Try one of : door|button|save-repeater|x-repeater|z-repeater|y-repeater|chameleon|save-sign");
                            return false;
                        }
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("owner", player.getName());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (!rs.resultSet()) {
                            sender.sendMessage(plugin.pluginName + " You are not a Timelord. You need to create a TARDIS first before using this command!");
                            return false;
                        }
                        HashMap<String, Object> wheret = new HashMap<String, Object>();
                        wheret.put("player", player.getName());
                        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
                        if (!rst.resultSet()) {
                            sender.sendMessage(plugin.pluginName + " You are not inside your TARDIS. You need to be to run this command!");
                            return false;
                        }
                        plugin.trackPlayers.put(player.getName(), args[1].toLowerCase());
                        player.sendMessage(plugin.pluginName + " Click the TARDIS " + args[1].toLowerCase() + " to update its position.");
                        return true;
                    } else {
                        sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("rebuild") || args[0].equalsIgnoreCase("hide")) {
                    if (player.hasPermission("tardis.rebuild")) {
                        String save;
                        World w;
                        int x, y, z, id;
                        TARDISConstants.COMPASS d;
                        boolean cham;
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("owner", player.getName());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (!rs.resultSet()) {
                            sender.sendMessage(plugin.pluginName + " " + TARDISConstants.NO_TARDIS);
                            return false;
                        }
                        save = rs.getSave();
                        id = rs.getTardis_id();
                        cham = rs.getChameleon_on();
                        d = rs.getDirection();
                        String[] save_data = save.split(":");
                        w = plugin.getServer().getWorld(save_data[0]);
                        x = plugin.utils.parseNum(save_data[1]);
                        y = plugin.utils.parseNum(save_data[2]);
                        z = plugin.utils.parseNum(save_data[3]);
                        Location l = new Location(w, x, y, z);
                        if (args[0].equalsIgnoreCase("rebuild")) {
                            plugin.buildPB.buildPoliceBox(id, l, d, cham, player, true);
                            sender.sendMessage(plugin.pluginName + " The TARDIS Police Box was rebuilt!");
                            return true;
                        }
                        if (args[0].equalsIgnoreCase("hide")) {
                            // remove torch
                            plugin.destroyPB.destroyTorch(l);
                            // remove sign
                            plugin.destroyPB.destroySign(l, d);
                            // remove blue box
                            plugin.destroyPB.destroyPoliceBox(l, d, id, true);
                            sender.sendMessage(plugin.pluginName + " The TARDIS Police Box was hidden! Use " + ChatColor.GREEN + "/tardis rebuild" + ChatColor.RESET + " to show it again.");
                            return true;
                        }
                    } else {
                        sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("list")) {
                    if (player.hasPermission("tardis.list")) {
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("owner", player.getName());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (!rs.resultSet()) {
                            sender.sendMessage(plugin.pluginName + " " + TARDISConstants.NO_TARDIS);
                            return false;
                        }
                        if (args.length < 2 || (!args[1].equalsIgnoreCase("saves") && !args[1].equalsIgnoreCase("companions") && !args[1].equalsIgnoreCase("areas"))) {
                            sender.sendMessage(plugin.pluginName + " You need to specify which TARDIS list you want to view! [saves|companions|areas]");
                            return false;
                        }
                        TARDISLister.list(player, args[1]);
                        return true;
                    } else {
                        sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("find")) {
                    if (player.hasPermission("tardis.find")) {
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("owner", player.getName());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (!rs.resultSet()) {
                            sender.sendMessage(plugin.pluginName + " " + TARDISConstants.NO_TARDIS);
                            return false;
                        }
                        String loc = rs.getSave();
                        String[] findData = loc.split(":");
                        sender.sendMessage(plugin.pluginName + " TARDIS was left at " + findData[0] + " at x: " + findData[1] + " y: " + findData[2] + " z: " + findData[3]);
                        return true;
                    } else {
                        sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("add")) {
                    if (player.hasPermission("tardis.add")) {
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("owner", player.getName());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        String comps;
                        int id;
                        if (!rs.resultSet()) {
                            sender.sendMessage(plugin.pluginName + " " + TARDISConstants.NO_TARDIS);
                            return false;
                        } else {
                            id = rs.getTardis_id();
                            comps = rs.getCompanions();
                        }
                        if (args.length < 2) {
                            sender.sendMessage(plugin.pluginName + " Too few command arguments!");
                            return false;
                        }
                        if (!args[1].matches("[A-Za-z0-9_]{2,16}")) {
                            sender.sendMessage(plugin.pluginName + "That doesn't appear to be a valid username");
                            return false;
                        } else {
                            QueryFactory qf = new QueryFactory(plugin);
                            HashMap<String, Object> tid = new HashMap<String, Object>();
                            HashMap<String, Object> set = new HashMap<String, Object>();
                            tid.put("tardis_id", id);
                            if (comps != null && !comps.equals("")) {
                                // add to the list
                                String newList = comps + ":" + args[1].toLowerCase();
                                set.put("companions", newList);
                            } else {
                                // make a list
                                set.put("companions", args[1].toLowerCase());
                            }
                            qf.doUpdate("tardis", set, tid);
                            player.sendMessage(plugin.pluginName + " You added " + ChatColor.GREEN + args[1] + ChatColor.RESET + " as a TARDIS companion.");
                            return true;
                        }
                    } else {
                        sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    if (player.hasPermission("tardis.add")) {
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("owner", player.getName());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        String comps;
                        int id;
                        if (!rs.resultSet()) {
                            sender.sendMessage(plugin.pluginName + " " + TARDISConstants.NO_TARDIS);
                            return false;
                        } else {
                            id = rs.getTardis_id();
                            comps = rs.getCompanions();
                            if (comps == null && comps.equals("")) {
                                sender.sendMessage(plugin.pluginName + " You have not added any TARDIS companions yet!");
                                return true;
                            }
                        }
                        if (args.length < 2) {
                            sender.sendMessage(plugin.pluginName + " Too few command arguments!");
                            return false;
                        }
                        if (!args[1].matches("[A-Za-z0-9_]{2,16}")) {
                            sender.sendMessage(plugin.pluginName + "That doesn't appear to be a valid username");
                            return false;
                        } else {
                            String[] split = comps.split(":");
                            StringBuilder buf = new StringBuilder();
                            String newList;
                            if (split.length > 1) {
                                // recompile string without the specified player
                                for (String c : split) {
                                    if (!c.equals(args[1].toLowerCase())) {
                                        // add to new string
                                        buf.append(c).append(":");
                                    }
                                }
                                // remove trailing colon
                                newList = buf.toString();
                                newList = newList.substring(0, newList.length() - 1);
                            } else {
                                newList = "";
                            }
                            QueryFactory qf = new QueryFactory(plugin);
                            HashMap<String, Object> tid = new HashMap<String, Object>();
                            HashMap<String, Object> set = new HashMap<String, Object>();
                            tid.put("tardis_id", id);
                            set.put("companions", newList);
                            player.sendMessage(plugin.pluginName + " You removed " + ChatColor.GREEN + args[1] + ChatColor.RESET + " as a TARDIS companion.");
                            return true;
                        }
                    } else {
                        sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("save")) {
                    if (player.hasPermission("tardis.save")) {
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("owner", player.getName());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (!rs.resultSet()) {
                            sender.sendMessage(plugin.pluginName + " " + TARDISConstants.NO_TARDIS);
                            return false;
                        }
                        if (args.length < 2) {
                            sender.sendMessage(plugin.pluginName + " Too few command arguments!");
                            return false;
                        }
                        if (!args[1].matches("[A-Za-z0-9_]{2,16}")) {
                            sender.sendMessage(plugin.pluginName + "That doesn't appear to be a valid save name (it may be too long or contains spaces).");
                            return false;
                        } else {
                            String cur = rs.getCurrent();
                            String sav = rs.getSave();
                            int id = rs.getTardis_id();
                            String[] curDest;
                            // get current destination
                            HashMap<String, Object> wheret = new HashMap<String, Object>();
                            where.put("player", player.getName());
                            ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
                            if (rst.resultSet()) {
                                // inside TARDIS
                                curDest = cur.split(":");
                            } else {
                                // outside TARDIS
                                curDest = sav.split(":");
                            }
                            QueryFactory qf = new QueryFactory(plugin);
                            HashMap<String, Object> set = new HashMap<String, Object>();
                            set.put("tardis_id", id);
                            set.put("dest_name", args[1]);
                            set.put("world", curDest[0]);
                            set.put("x", plugin.utils.parseNum(curDest[1]));
                            set.put("y", plugin.utils.parseNum(curDest[2]));
                            set.put("z", plugin.utils.parseNum(curDest[3]));
                            if (!qf.doPreparedInsert("destinations", set)) {
                                return false;
                            } else {
                                sender.sendMessage(plugin.pluginName + " The location '" + args[1] + "' was saved successfully.");
                                return true;
                            }
                        }
                    } else {
                        sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("removesave")) {
                    if (player.hasPermission("tardis.save")) {
                        if (args.length < 2) {
                            sender.sendMessage(plugin.pluginName + " Too few command arguments!");
                            return false;
                        }
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("owner", player.getName());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (!rs.resultSet()) {
                            sender.sendMessage(plugin.pluginName + " " + TARDISConstants.NO_TARDIS);
                            return false;
                        }
                        int id = rs.getTardis_id();
                        HashMap<String, Object> whered = new HashMap<String, Object>();
                        whered.put("dest_name", args[1]);
                        whered.put("tardis_id", id);
                        ResultSetDestinations rsd = new ResultSetDestinations(plugin, where, false);
                        if (!rsd.resultSet()) {
                            sender.sendMessage(plugin.pluginName + " Could not find a saved destination with that name!");
                            return false;
                        }
                        int destID = rsd.getDest_id();
                        QueryFactory qf = new QueryFactory(plugin);
                        HashMap<String, Object> did = new HashMap<String, Object>();
                        did.put("dest_id", destID);
                        qf.doDelete("destinations", did);
                        sender.sendMessage(plugin.pluginName + " The destination " + args[1] + " was deleted!");
                        return true;
                    } else {
                        sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("setdest")) {
                    if (player.hasPermission("tardis.save")) {
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("owner", player.getName());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (!rs.resultSet()) {
                            sender.sendMessage(plugin.pluginName + " " + TARDISConstants.NO_TARDIS);
                            return false;
                        }
                        if (args.length < 2) {
                            sender.sendMessage(plugin.pluginName + " Too few command arguments!");
                            return false;
                        }
                        if (!args[1].matches("[A-Za-z0-9_]{2,16}")) {
                            sender.sendMessage(plugin.pluginName + "The destination name must be between 2 and 16 characters and have no spaces!");
                            return false;
                        } else {
                            int id = rs.getTardis_id();
                            // get location player is looking at
                            Block b = player.getTargetBlock(transparent, 50);
                            Location l = b.getLocation();
                            if (!plugin.getConfig().getBoolean("include_default_world") && plugin.getConfig().getBoolean("default_world") && l.getWorld().getName().equals(plugin.getConfig().getString("default_world_name"))) {
                                sender.sendMessage(plugin.pluginName + " The server admin will not allow you to set the TARDIS destination to this world!");
                                return true;
                            }
                            if (plugin.worldGuardOnServer && plugin.getConfig().getBoolean("respect_worldguard")) {
                                if (plugin.wgchk.cantBuild(player, l)) {
                                    sender.sendMessage(plugin.pluginName + "That location is protected by WorldGuard!");
                                    return false;
                                }
                            }
                            if (player.hasPermission("tardis.exile")) {
                                String areaPerm = plugin.ta.getExileArea(player);
                                if (plugin.ta.areaCheckInExile(areaPerm, l)) {
                                    sender.sendMessage(plugin.pluginName + "You exile status does not allow you to save the TARDIS to this location!");
                                    return false;
                                }
                            }
                            if (plugin.ta.areaCheckLocPlayer(player, l)) {
                                sender.sendMessage(plugin.pluginName + "You do not have permission [" + plugin.trackPerm.get(player.getName()) + "] to set the TARDIS destination to this location!");
                                plugin.trackPerm.remove(player.getName());
                                return false;
                            }
                            String dw = l.getWorld().getName();
                            int dx = l.getBlockX();
                            int dy = l.getBlockY() + 1;
                            int dz = l.getBlockZ();
                            QueryFactory qf = new QueryFactory(plugin);
                            HashMap<String, Object> set = new HashMap<String, Object>();
                            set.put("tardis_id", id);
                            set.put("dest_name", args[1]);
                            set.put("world", dw);
                            set.put("x", dx);
                            set.put("y", dy);
                            set.put("z", dz);
                            if (!qf.doPreparedInsert("destinations", set)) {
                                return false;
                            } else {
                                sender.sendMessage(plugin.pluginName + " The destination '" + args[1] + "' was saved successfully.");
                                return true;
                            }
                        }
                    } else {
                        sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("direction")) {
                    if (player.hasPermission("tardis.timetravel")) {
                        if (args.length < 2 || (!args[1].equalsIgnoreCase("north") && !args[1].equalsIgnoreCase("west") && !args[1].equalsIgnoreCase("south") && !args[1].equalsIgnoreCase("east"))) {
                            sender.sendMessage(plugin.pluginName + " You need to specify the compass direction e.g. north, west, south or east!");
                            return false;
                        }
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("owner", player.getName());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (!rs.resultSet()) {
                            sender.sendMessage(plugin.pluginName + " " + TARDISConstants.NO_TARDIS);
                            return false;
                        }
                        String save = rs.getSave();
                        String[] save_data = save.split(":");
                        int id = rs.getTardis_id();
                        boolean cham = rs.getChameleon_on();
                        String dir = args[1].toUpperCase();
                        TARDISConstants.COMPASS old_d = rs.getDirection();
                        QueryFactory qf = new QueryFactory(plugin);
                        HashMap<String, Object> tid = new HashMap<String, Object>();
                        HashMap<String, Object> set = new HashMap<String, Object>();
                        tid.put("tardis_id", id);
                        set.put("direction", dir);
                        qf.doUpdate("tardis", set, tid);
                        HashMap<String, Object> did = new HashMap<String, Object>();
                        HashMap<String, Object> setd = new HashMap<String, Object>();
                        did.put("door_type", 0);
                        did.put("tardis_id", id);
                        setd.put("door_direction", dir);
                        qf.doUpdate("doors", setd, did);
                        World w = plugin.getServer().getWorld(save_data[0]);
                        int x = plugin.utils.parseNum(save_data[1]);
                        int y = plugin.utils.parseNum(save_data[2]);
                        int z = plugin.utils.parseNum(save_data[3]);
                        Location l = new Location(w, x, y, z);
                        TARDISConstants.COMPASS d = TARDISConstants.COMPASS.valueOf(dir);
                        plugin.destroyPB.destroySign(l, old_d);
                        plugin.buildPB.buildPoliceBox(id, l, d, cham, player, true);
                        return true;
                    } else {
                        sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("namekey")) {
                    Material m = Material.getMaterial(plugin.getConfig().getString("key"));
                    ItemStack is = player.getItemInHand();
                    if (!is.getType().equals(m)) {
                        sender.sendMessage(plugin.pluginName + "You can only rename the TARDIS key!");
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
                    TARDISItemRenamer ir = new TARDISItemRenamer(is);
                    ir.setName(tmp, false);
                    sender.sendMessage(plugin.pluginName + "TARDIS key renamed to '" + tmp + "'");
                    return true;
                }
                if (args[0].equalsIgnoreCase("help")) {
                    if (args.length == 1) {
                        sender.sendMessage(TARDISConstants.COMMANDS.split("\n"));
                        return true;
                    }
                    if (args.length == 2) {
                        switch (TARDISConstants.fromString(args[1])) {
                            case CREATE:
                                sender.sendMessage(TARDISConstants.COMMAND_CREATE.split("\n"));
                                break;
                            case DELETE:
                                sender.sendMessage(TARDISConstants.COMMAND_DELETE.split("\n"));
                                break;
                            case TIMETRAVEL:
                                sender.sendMessage(TARDISConstants.COMMAND_TIMETRAVEL.split("\n"));
                                break;
                            case LIST:
                                sender.sendMessage(TARDISConstants.COMMAND_LIST.split("\n"));
                                break;
                            case FIND:
                                sender.sendMessage(TARDISConstants.COMMAND_FIND.split("\n"));
                                break;
                            case SAVE:
                                sender.sendMessage(TARDISConstants.COMMAND_SAVE.split("\n"));
                                break;
                            case REMOVESAVE:
                                sender.sendMessage(TARDISConstants.COMMAND_REMOVESAVE.split("\n"));
                                break;
                            case ADD:
                                sender.sendMessage(TARDISConstants.COMMAND_ADD.split("\n"));
                                break;
                            case TRAVEL:
                                sender.sendMessage(TARDISConstants.COMMAND_TRAVEL.split("\n"));
                                break;
                            case UPDATE:
                                sender.sendMessage(TARDISConstants.COMMAND_UPDATE.split("\n"));
                                break;
                            case REBUILD:
                                sender.sendMessage(TARDISConstants.COMMAND_REBUILD.split("\n"));
                                break;
                            case CHAMELEON:
                                sender.sendMessage(TARDISConstants.COMMAND_CHAMELEON.split("\n"));
                                break;
                            case SFX:
                                sender.sendMessage(TARDISConstants.COMMAND_SFX.split("\n"));
                                break;
                            case PLATFORM:
                                sender.sendMessage(TARDISConstants.COMMAND_PLATFORM.split("\n"));
                                break;
                            case SETDEST:
                                sender.sendMessage(TARDISConstants.COMMAND_SETDEST.split("\n"));
                                break;
                            case HOME:
                                sender.sendMessage(TARDISConstants.COMMAND_HOME.split("\n"));
                                break;
                            case HIDE:
                                sender.sendMessage(TARDISConstants.COMMAND_HIDE.split("\n"));
                                break;
                            case ADMIN:
                                sender.sendMessage(TARDISConstants.COMMAND_ADMIN.split("\n"));
                                break;
                            case AREA:
                                sender.sendMessage(TARDISConstants.COMMAND_AREA.split("\n"));
                                break;
                            default:
                                sender.sendMessage(TARDISConstants.COMMANDS.split("\n"));
                        }
                    }
                    return true;
                }
            }
        }
        // If the above has happened the function will break and return true. if this hasn't happened then value of false will be returned.
        return false;
    }
}