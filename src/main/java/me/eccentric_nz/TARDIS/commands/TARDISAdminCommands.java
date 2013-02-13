/*
 * Copyright (C) 2013 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.files.TARDISConfiguration;
import me.eccentric_nz.TARDIS.thirdparty.Version;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * Command /tardisadmin [arguments].
 *
 * The Lord President was the most powerful member of the Time Lord Council and
 * had near absolute authority, and used a link to the Matrix, a vast computer
 * network containing the knowledge and experiences of all past generations of
 * Time Lords, to set Time Lord policy and remain alert to potential threats
 * from lesser civilisations.
 *
 * @author eccentric_nz
 */
public class TARDISAdminCommands implements CommandExecutor {

    private TARDIS plugin;
    private List<String> firstsStr = new ArrayList<String>();
    private List<String> firstsBool = new ArrayList<String>();
    private List<String> firstsInt = new ArrayList<String>();
    private List<String> firstsRoom = new ArrayList<String>();
    HashSet<Byte> transparent = new HashSet<Byte>();
    private Material charger = Material.REDSTONE_LAMP_ON;
    Version bukkitversion;
    Version prebeaconversion = new Version("1.4.2");

    public TARDISAdminCommands(TARDIS plugin) {
        this.plugin = plugin;
        // add first arguments
        firstsStr.add("chunks");
        firstsStr.add("config");
        firstsStr.add("default_world_name");
        firstsStr.add("delete");
        firstsStr.add("exclude");
        firstsStr.add("find");
        firstsStr.add("full_charge_item");
        firstsStr.add("include");
        firstsStr.add("key");
        firstsStr.add("list");
        firstsStr.add("recharger");
        firstsStr.add("reload");
        // boolean
        firstsBool.add("bonus_chest");
        firstsBool.add("chameleon");
        firstsBool.add("check_for_updates");
        firstsBool.add("create_worlds");
        firstsBool.add("debug");
        firstsBool.add("default_world");
        firstsBool.add("give_key");
        firstsBool.add("include_default_world");
        firstsBool.add("land_on_water");
        firstsBool.add("materialise");
        firstsBool.add("name_tardis");
        firstsBool.add("name_tardis");
        firstsBool.add("nether");
        firstsBool.add("platform");
        firstsBool.add("respect_towny");
        firstsBool.add("respect_worldborder");
        firstsBool.add("respect_worldguard");
        firstsBool.add("sfx");
        firstsBool.add("the_end");
        firstsBool.add("use_worldguard");
        // integer
        firstsInt.add("border_radius");
        firstsInt.add("comehere");
        firstsInt.add("confirm_timeout");
        firstsInt.add("creeper_recharge");
        firstsInt.add("full_charge");
        firstsInt.add("hide");
        firstsInt.add("jettison");
        firstsInt.add("lightning_recharge");
        firstsInt.add("nether_min");
        firstsInt.add("player");
        firstsInt.add("random");
        firstsInt.add("recharge_distance");
        firstsInt.add("the_end_min");
        firstsInt.add("scan_radius");
        firstsInt.add("timeout");
        firstsInt.add("timeout_height");
        firstsInt.add("tp_radius");
        firstsInt.add("travel");
        // rooms
        firstsRoom.add("arboretum");
        firstsRoom.add("baker");
        firstsRoom.add("bedroom");
        firstsRoom.add("empty");
        firstsRoom.add("gravity");
        firstsRoom.add("harmony");
        firstsRoom.add("kitchen");
        firstsRoom.add("library");
        firstsRoom.add("passage");
        firstsRoom.add("pool");
        firstsRoom.add("vault");
        firstsRoom.add("wood");
        firstsRoom.add("workshop");

        String[] v = Bukkit.getServer().getBukkitVersion().split("-");
        bukkitversion = new Version(v[0]);
        if (bukkitversion.compareTo(prebeaconversion) >= 0) {
            charger = Material.BEACON;
        }
        // add transparent blocks
        transparent.add((byte) Material.AIR.getId());
        transparent.add((byte) Material.SNOW.getId());
        transparent.add((byte) Material.LONG_GRASS.getId());
        transparent.add((byte) Material.VINE.getId());
        transparent.add((byte) Material.IRON_FENCE.getId());
        transparent.add((byte) Material.DEAD_BUSH.getId());
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
                String first = args[0].toLowerCase(Locale.ENGLISH);
                if (!firstsStr.contains(first) && !firstsBool.contains(first) && !firstsInt.contains(first) && !firstsRoom.contains(first)) {
                    sender.sendMessage(plugin.pluginName + "TARDIS does not recognise that command argument!");
                    return false;
                }
                if (args.length == 1) {
                    if (first.equals("chunks")) {
                        if (plugin.tardisChunkList.size() > 0) {
                            for (Chunk c : plugin.tardisChunkList) {
                                sender.sendMessage(plugin.pluginName + c.getWorld().getName() + ": " + c);
                            }
                        } else {
                            sender.sendMessage("No chunks in list!");
                        }
                        return true;
                    }
                    if (first.equals("reload")) {
                        plugin.reloadConfig();
                        // check worlds
                        TARDISConfiguration tc = new TARDISConfiguration(plugin);
                        tc.doWorlds();
                        plugin.saveConfig();
                        sender.sendMessage(plugin.pluginName + "TARDIS config reloaded.");
                        return true;
                    }
                    if (first.equals("config")) {
                        Set<String> configNames = plugin.getConfig().getKeys(false);
                        sender.sendMessage(plugin.pluginName + ChatColor.RED + " Here are the current plugin config options!");
                        for (String cname : configNames) {
                            String value = plugin.getConfig().getString(cname);
                            if (cname.equals("worlds") || cname.equals("rooms") || cname.equals("rechargers")) {
                                if (cname.equals("worlds")) {
                                    sender.sendMessage(ChatColor.AQUA + cname + ":" + ChatColor.RESET);
                                    Set<String> worldNames = plugin.getConfig().getConfigurationSection("worlds").getKeys(false);
                                    for (String wname : worldNames) {
                                        String enabled = plugin.getConfig().getString("worlds." + wname);
                                        sender.sendMessage("      " + ChatColor.GREEN + wname + ": " + ChatColor.RESET + enabled);
                                    }
                                }
                                if (cname.equals("rooms")) {
                                    sender.sendMessage(ChatColor.AQUA + cname + ":" + ChatColor.RESET);
                                    Set<String> roomNames = plugin.getConfig().getConfigurationSection("rooms").getKeys(false);
                                    for (String r : roomNames) {
                                        sender.sendMessage("      " + ChatColor.GREEN + r + ":");
                                        sender.sendMessage("            cost: " + plugin.getConfig().getString("rooms." + r + ".cost"));
                                        sender.sendMessage("            seed: " + plugin.getConfig().getString("rooms." + r + ".seed"));
                                    }
                                }
                                if (cname.equals("rechargers")) {
                                    sender.sendMessage(ChatColor.AQUA + cname + ":" + ChatColor.RESET);
                                    Set<String> chargerNames = plugin.getConfig().getConfigurationSection("rechargers").getKeys(false);
                                    for (String charname : chargerNames) {
                                        sender.sendMessage("      " + ChatColor.GREEN + charname + ":");
                                        sender.sendMessage("            world: " + plugin.getConfig().getString("rechargers." + charname + ".world"));
                                        sender.sendMessage("            x: " + plugin.getConfig().getString("rechargers." + charname + ".x"));
                                        sender.sendMessage("            y: " + plugin.getConfig().getString("rechargers." + charname + ".y"));
                                        sender.sendMessage("            z: " + plugin.getConfig().getString("rechargers." + charname + ".z"));
                                    }
                                }
                            } else {
                                sender.sendMessage(ChatColor.AQUA + cname + ": " + ChatColor.RESET + value);
                            }
                        }
                        return true;
                    }
                }
                if (first.equals("list")) {
                    // get all tardis positions - max 18
                    int start = 0, end = 18;
                    if (args.length > 1) {
                        int tmp = plugin.utils.parseNum(args[1]);
                        start = (tmp * 18) - 18;
                        end = tmp * 18;
                    }
                    String limit = start + ", " + end;
                    ResultSetTardis rsl = new ResultSetTardis(plugin, null, limit, true);
                    if (rsl.resultSet()) {
                        sender.sendMessage(plugin.pluginName + "TARDIS locations.");
                        ArrayList<HashMap<String, String>> data = rsl.getData();
                        for (HashMap<String, String> map : data) {
                            sender.sendMessage("Timelord: " + map.get("owner") + ", Location: " + map.get("current"));
                        }
                        sender.sendMessage(plugin.pluginName + "To see more locations, type: /tardisadmin list 2,  /tardisadmin list 3 etc.");
                    } else {
                        sender.sendMessage(plugin.pluginName + "There are no more records to display.");
                    }
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(plugin.pluginName + "Too few command arguments!");
                    return false;
                }
                if (first.equals("recharger")) {
                    Player player = null;
                    if (sender instanceof Player) {
                        player = (Player) sender;
                    }
                    if (player == null) {
                        sender.sendMessage(plugin.pluginName + "You can't set a recharger location from the console!");
                        return true;
                    }
                    Block b = player.getTargetBlock(transparent, 50);
                    if (!b.getType().equals(charger)) {
                        player.sendMessage(plugin.pluginName + "You must be targeting a " + charger.toString() + " block!");
                        return true;
                    }
                    // make sure they're not targeting their inner TARDIS beacon
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("player", player.getName());
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
                    if (rst.resultSet()) {
                        player.sendMessage(plugin.pluginName + "You cannot use the TARDIS " + charger.toString() + " to recharge!");
                        return true;
                    }
                    Location l = b.getLocation();
                    plugin.getConfig().set("rechargers." + args[1] + ".world", l.getWorld().getName());
                    plugin.getConfig().set("rechargers." + args[1] + ".x", l.getBlockX());
                    plugin.getConfig().set("rechargers." + args[1] + ".y", l.getBlockY());
                    plugin.getConfig().set("rechargers." + args[1] + ".z", l.getBlockZ());
                    // if worldguard is on the server, protect a 3x3 area around beacon
                    if (plugin.worldGuardOnServer && plugin.getConfig().getBoolean("use_worldguard")) {
                        int minx = l.getBlockX() - 2;
                        int maxx = l.getBlockX() + 2;
                        int minz = l.getBlockZ() - 2;
                        int maxz = l.getBlockZ() + 2;
                        Location wg1 = new Location(l.getWorld(), minx, l.getBlockY(), minz);
                        Location wg2 = new Location(l.getWorld(), maxx, l.getBlockY(), maxz);
                        plugin.wgchk.addRechargerProtection(player, args[1], wg1, wg2);
                    }
                }
                if (first.equals("delete")) {
                    // this should be run from the console if the player running it is the player to be deleted
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (player.getName().equals(args[1])) {
                            sender.sendMessage(plugin.pluginName + "To delete your own records, please disconnect and use the console.");
                            return true;
                        }
                    }
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
                        int restore;
                        // if (create_worlds) just use AIR
                        if (plugin.getConfig().getBoolean("create_worlds")) {
                            restore = 0;
                        } else {
                            World.Environment env = cw.getEnvironment();
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
                        }
                        // check if player is in the TARDIS
                        HashMap<String, Object> wheret = new HashMap<String, Object>();
                        wheret.put("tardis_id", id);
                        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, true);
                        boolean useCurrent = false;
                        QueryFactory qf = new QueryFactory(plugin);
                        HashMap<String, Object> whered = new HashMap<String, Object>();
                        whered.put("tardis_id", id);
                        if (rst.resultSet() || plugin.tardisHasDestination.containsKey(id)) {
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
                        if (bb_loc == null) {
                            sender.sendMessage(plugin.pluginName + "Could not get the location of the TARDIS!");
                            return true;
                        }
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
                if (first.equals("key") || first.equals("full_charge_item")) {
                    String setMaterial = args[1].toUpperCase(Locale.ENGLISH);
                    if (!Arrays.asList(TARDISMaterials.MATERIAL_LIST).contains(setMaterial)) {
                        sender.sendMessage(plugin.pluginName + ChatColor.RED + "That is not a valid Material! Try checking http://jd.bukkit.org/apidocs/org/bukkit/Material.html");
                        return false;
                    } else {
                        plugin.getConfig().set(first, setMaterial);
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
                if (first.equals("exclude") || first.equals("include")) {
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
                    if (first.equals("include")) {
                        plugin.getConfig().set("worlds." + nodots, true);
                    } else {
                        plugin.getConfig().set("worlds." + nodots, false);
                    }
                }
                //checks if its a boolean config option
                if (firstsBool.contains(first)) {
                    // check they typed true of false
                    String tf = args[1].toLowerCase(Locale.ENGLISH);
                    if (!tf.equals("true") && !tf.equals("false")) {
                        sender.sendMessage(plugin.pluginName + ChatColor.RED + "The last argument must be true or false!");
                        return false;
                    }
                    plugin.getConfig().set(first, Boolean.valueOf(tf));
                }
                //checks if its a number config option
                if (firstsInt.contains(first) || firstsRoom.contains(first)) {
                    String a = args[1];
                    int val;
                    try {
                        val = Integer.parseInt(a);
                    } catch (NumberFormatException nfe) {
                        // not a number
                        sender.sendMessage(plugin.pluginName + ChatColor.RED + " The last argument must be a number!");
                        return false;
                    }
                    String option = (firstsRoom.contains(first)) ? "rooms." + first.toUpperCase(Locale.ENGLISH) : first;
                    plugin.getConfig().set(option, val);
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
