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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import me.eccentric_nz.TARDIS.database.ResultSetCount;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import static me.eccentric_nz.TARDIS.destroyers.TARDISExterminator.deleteFolder;
import me.eccentric_nz.TARDIS.destroyers.TARDISPruner;
import me.eccentric_nz.TARDIS.files.TARDISConfiguration;
import me.eccentric_nz.TARDIS.travel.TARDISTerminalInventory;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
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
    public List<String> firstsStr = new ArrayList<String>();
    public List<String> firstsStrArtron = new ArrayList<String>();
    public List<String> firstsBool = new ArrayList<String>();
    public List<String> firstsInt = new ArrayList<String>();
    public List<String> firstsIntArtron = new ArrayList<String>();
    HashSet<Byte> transparent = new HashSet<Byte>();
    private Material charger = Material.REDSTONE_LAMP_ON;

    public TARDISAdminCommands(TARDIS plugin) {
        this.plugin = plugin;
        // add first arguments
        firstsStr.add("chunks");
        firstsStr.add("config");
        firstsStr.add("decharge");
        firstsStr.add("default_world_name");
        firstsStr.add("delete");
        firstsStr.add("difficulty");
        firstsStr.add("enter");
        firstsStr.add("exclude");
        firstsStr.add("find");
        firstsStr.add("gamemode");
        firstsStr.add("include");
        firstsStr.add("inventory_group");
        firstsStr.add("key");
        firstsStr.add("list");
        firstsStr.add("playercount");
        firstsStr.add("prune");
        firstsStr.add("recharger");
        firstsStr.add("reload");
        firstsStr.add("stattenheim");
        firstsStrArtron.add("full_charge_item");
        firstsStrArtron.add("jettison_seed");
        // boolean
        firstsBool.add("add_perms");
        firstsBool.add("all_blocks");
        firstsBool.add("allow_achievements");
        firstsBool.add("allow_autonomous");
        firstsBool.add("allow_hads");
        firstsBool.add("allow_mob_farming");
        firstsBool.add("allow_tp_switch");
        firstsBool.add("bonus_chest");
        firstsBool.add("chameleon");
        firstsBool.add("check_for_updates");
        firstsBool.add("create_worlds");
        firstsBool.add("create_worlds_with_perms");
        firstsBool.add("debug");
        firstsBool.add("default_world");
        firstsBool.add("emergency_npc");
        firstsBool.add("exile");
        firstsBool.add("give_key");
        firstsBool.add("include_default_world");
        firstsBool.add("keep_night");
        firstsBool.add("land_on_water");
        firstsBool.add("materialise");
        firstsBool.add("name_tardis");
        firstsBool.add("nether");
        firstsBool.add("per_world_perms");
        firstsBool.add("platform");
        firstsBool.add("respect_factions");
        firstsBool.add("respect_towny");
        firstsBool.add("respect_worldborder");
        firstsBool.add("respect_worldguard");
        firstsBool.add("return_room_seed");
        firstsBool.add("rooms_require_blocks");
        firstsBool.add("sfx");
        firstsBool.add("plain_on");
        firstsBool.add("spawn_eggs");
        firstsBool.add("strike_lightning");
        firstsBool.add("the_end");
        firstsBool.add("use_clay");
        firstsBool.add("use_worldguard");
        // integer
        firstsInt.add("admin_item");
        firstsInt.add("border_radius");
        firstsInt.add("confirm_timeout");
        firstsInt.add("count");
        firstsInt.add("gravity_max_distance");
        firstsInt.add("gravity_max_velocity");
        firstsInt.add("hads_damage");
        firstsInt.add("hads_distance");
        firstsInt.add("malfunction");
        firstsInt.add("malfunction_end");
        firstsInt.add("malfunction_nether");
        firstsInt.add("recharge_distance");
        firstsInt.add("rooms_condenser_percent");
        firstsInt.add("terminal_step");
        firstsInt.add("timeout");
        firstsInt.add("timeout_height");
        firstsInt.add("tp_radius");
        firstsInt.add("wall_id");
        firstsInt.add("wall_data");
        firstsInt.add("tardis_lamp");
        firstsIntArtron.add("autonomous");
        firstsIntArtron.add("backdoor");
        firstsIntArtron.add("comehere");
        firstsIntArtron.add("creeper_recharge");
        firstsIntArtron.add("full_charge");
        firstsIntArtron.add("hide");
        firstsIntArtron.add("jettison");
        firstsIntArtron.add("lightning_recharge");
        firstsIntArtron.add("nether_min");
        firstsIntArtron.add("player");
        firstsIntArtron.add("random");
        firstsIntArtron.add("the_end_min");
        firstsIntArtron.add("travel");

        if (plugin.bukkitversion.compareTo(plugin.prewoodbuttonversion) >= 0) {
            charger = Material.BEACON;
        }
        // add transparent blocks
        transparent.add((byte) Material.AIR.getId());
        transparent.add((byte) Material.DEAD_BUSH.getId());
        transparent.add((byte) Material.GLASS.getId());
        transparent.add((byte) Material.IRON_FENCE.getId());
        transparent.add((byte) Material.LONG_GRASS.getId());
        transparent.add((byte) Material.SNOW.getId());
        transparent.add((byte) Material.VINE.getId());
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
                if (!firstsStr.contains(first) && !firstsBool.contains(first) && !firstsInt.contains(first) && !firstsIntArtron.contains(first) && !firstsStrArtron.contains(first)) {
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
                            if (!cname.equals("worlds") && !cname.equals("rooms") && !cname.equals("rechargers")) {
                                sender.sendMessage(ChatColor.AQUA + cname + ": " + ChatColor.RESET + value);
                            }
                        }
                        return true;
                    }
                }
                if (first.equals("list")) {
                    if (args.length > 1 && args[1].equalsIgnoreCase("save")) {
                        ResultSetTardis rsl = new ResultSetTardis(plugin, null, "", true);
                        if (rsl.resultSet()) {
                            ArrayList<HashMap<String, String>> data = rsl.getData();
                            String file = plugin.getDataFolder() + File.separator + "TARDIS_list.txt";
                            try {
                                BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
                                for (HashMap<String, String> map : data) {
                                    String line = "Timelord: " + map.get("owner") + ", Location: " + map.get("current");
                                    bw.write(line);
                                    bw.newLine();
                                }
                                bw.close();
                            } catch (IOException e) {
                                plugin.debug("Could not create and write to TARDIS_list.txt! " + e.getMessage());
                            }
                        }
                        sender.sendMessage(plugin.pluginName + "File saved to 'plugins/TARDIS/TARDIS_list.txt'");
                        return true;
                    } else {
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
                }
                if (args.length < 2) {
                    sender.sendMessage(plugin.pluginName + "Too few command arguments!");
                    return false;
                }
                if (first.equals("config")) {
                    Set<String> configNames = plugin.getConfig().getKeys(false);
                    sender.sendMessage(plugin.pluginName + ChatColor.RED + " Here are the current plugin " + args[1] + " options!");
                    for (String cname : configNames) {
                        if (cname.equals("worlds") || cname.equals("rooms") || cname.equals("rechargers")) {
                            if (cname.equals("worlds") && args[1].equalsIgnoreCase("worlds")) {
                                sender.sendMessage(ChatColor.AQUA + cname + ":" + ChatColor.RESET);
                                Set<String> worldNames = plugin.getConfig().getConfigurationSection("worlds").getKeys(false);
                                for (String wname : worldNames) {
                                    String enabled = plugin.getConfig().getString("worlds." + wname);
                                    sender.sendMessage("      " + ChatColor.GREEN + wname + ": " + ChatColor.RESET + enabled);
                                }
                            }
                            if (cname.equals("rooms") && args[1].equalsIgnoreCase("rooms")) {
                                sender.sendMessage(ChatColor.AQUA + cname + ":" + ChatColor.RESET);
                                Set<String> roomNames = plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false);
                                for (String r : roomNames) {
                                    sender.sendMessage("      " + ChatColor.GREEN + r + ":");
                                    sender.sendMessage("            enabled: " + plugin.getRoomsConfig().getString("rooms." + r + ".enabled"));
                                    sender.sendMessage("            cost: " + plugin.getRoomsConfig().getString("rooms." + r + ".cost"));
                                    sender.sendMessage("            offset: " + plugin.getRoomsConfig().getString("rooms." + r + ".offset"));
                                    sender.sendMessage("            seed: " + plugin.getRoomsConfig().getString("rooms." + r + ".seed"));
                                }
                            }
                            if (cname.equals("rechargers") && args[1].equalsIgnoreCase("rechargers")) {
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
                        }
                    }
                    return true;
                }
                if (first.equals("playercount")) {
                    int max_count = plugin.getConfig().getInt("count");
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("player", args[1]);
                    ResultSetCount rsc = new ResultSetCount(plugin, where, false);
                    if (rsc.resultSet()) {
                        if (args.length == 3) {
                            // set count
                            int count = plugin.utils.parseNum(args[2]);
                            HashMap<String, Object> setc = new HashMap<String, Object>();
                            setc.put("count", count);
                            HashMap<String, Object> wherec = new HashMap<String, Object>();
                            wherec.put("player", args[1]);
                            QueryFactory qf = new QueryFactory(plugin);
                            qf.doUpdate("t_count", setc, wherec);
                            sender.sendMessage(plugin.pluginName + args[1] + "'s TARDIS count was set to: " + args[2] + " of " + max_count);
                        } else {
                            // display count
                            sender.sendMessage(plugin.pluginName + args[1] + "'s TARDIS count is: " + rsc.getCount() + " of " + max_count);
                        }
                    } else {
                        sender.sendMessage(plugin.pluginName + "That player doesn't have a TARDIS count.");
                    }
                    return true;
                }
                if (first.equals("prune")) {
                    TARDISPruner pruner = new TARDISPruner(plugin);
                    try {
                        int days = Integer.parseInt(args[1]);
                        pruner.prune(sender, days);
                    } catch (NumberFormatException nfe) {
                        if (args[1].equalsIgnoreCase("list") && args.length == 3) {
                            int days = plugin.utils.parseNum(args[2]);
                            pruner.list(sender, days);
                        }
                    }
                    return true;
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
                    // if worldguard is on the server, protect a 3x3x3 area around beacon
                    if (plugin.worldGuardOnServer && plugin.getConfig().getBoolean("use_worldguard")) {
                        int minx = l.getBlockX() - 2;
                        int maxx = l.getBlockX() + 2;
                        int minz = l.getBlockZ() - 2;
                        int maxz = l.getBlockZ() + 2;
                        Location wg1 = new Location(l.getWorld(), minx, l.getBlockY() + 2, minz);
                        Location wg2 = new Location(l.getWorld(), maxx, l.getBlockY() - 2, maxz);
                        plugin.wgchk.addRechargerProtection(player, args[1], wg1, wg2);
                    }
                }
                if (first.equals("decharge")) {
                    if (!plugin.getConfig().contains("rechargers." + args[1])) {
                        sender.sendMessage(plugin.pluginName + "Could not find a recharger with that name! Try using " + ChatColor.AQUA + "/tardis list rechargers" + ChatColor.RESET + " first.");
                        return true;
                    }
                    if (plugin.worldGuardOnServer && plugin.getConfig().getBoolean("use_worldguard")) {
                        plugin.wgchk.removeRechargerRegion(args[1]);
                    }
                    plugin.getConfig().set("rechargers." + args[1], null);
                }
                if (first.equals("enter")) {
                    Player player = null;
                    if (sender instanceof Player) {
                        player = (Player) sender;
                    }
                    if (player == null) {
                        sender.sendMessage(plugin.pluginName + "Only a player can run this command!");
                        return true;
                    }
                    if (!player.hasPermission("tardis.skeletonkey")) {
                        sender.sendMessage(plugin.pluginName + "You do not have permission to run this command!");
                        return true;
                    }
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("owner", args[1]);
                    ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                    if (rs.resultSet()) {
                        int id = rs.getTardis_id();
                        HashMap<String, Object> wherei = new HashMap<String, Object>();
                        wherei.put("door_type", 1);
                        wherei.put("tardis_id", id);
                        ResultSetDoors rsi = new ResultSetDoors(plugin, wherei, false);
                        if (rsi.resultSet()) {
                            TARDISConstants.COMPASS innerD = rsi.getDoor_direction();
                            String doorLocStr = rsi.getDoor_location();
                            String[] split = doorLocStr.split(":");
                            World cw = plugin.getServer().getWorld(split[0]);
                            int cx = 0, cy = 0, cz = 0;
                            try {
                                cx = Integer.parseInt(split[1]);
                                cy = Integer.parseInt(split[2]);
                                cz = Integer.parseInt(split[3]);
                            } catch (NumberFormatException nfe) {
                                plugin.debug(plugin.pluginName + "Could not convert to number!");
                            }
                            Location tmp_loc = cw.getBlockAt(cx, cy, cz).getLocation();
                            int getx = tmp_loc.getBlockX();
                            int getz = tmp_loc.getBlockZ();
                            switch (innerD) {
                                case NORTH:
                                    // z -ve
                                    tmp_loc.setX(getx + 0.5);
                                    tmp_loc.setZ(getz - 0.5);
                                    break;
                                case EAST:
                                    // x +ve
                                    tmp_loc.setX(getx + 1.5);
                                    tmp_loc.setZ(getz + 0.5);
                                    break;
                                case SOUTH:
                                    // z +ve
                                    tmp_loc.setX(getx + 0.5);
                                    tmp_loc.setZ(getz + 1.5);
                                    break;
                                case WEST:
                                    // x -ve
                                    tmp_loc.setX(getx - 0.5);
                                    tmp_loc.setZ(getz + 0.5);
                                    break;
                            }
                            // enter TARDIS!
                            try {
                                Class.forName("org.bukkit.Sound");
                                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
                            } catch (ClassNotFoundException e) {
                                player.getLocation().getWorld().playEffect(player.getLocation(), Effect.GHAST_SHRIEK, 0);
                            }
                            cw.getChunkAt(tmp_loc).load();
                            float yaw = player.getLocation().getYaw();
                            float pitch = player.getLocation().getPitch();
                            tmp_loc.setPitch(pitch);
                            // get players direction so we can adjust yaw if necessary
                            TARDISConstants.COMPASS d = TARDISConstants.COMPASS.valueOf(plugin.utils.getPlayersDirection(player, false));
                            if (!innerD.equals(d)) {
                                switch (d) {
                                    case NORTH:
                                        yaw += plugin.doorListener.adjustYaw[0][innerD.ordinal()];
                                        break;
                                    case WEST:
                                        yaw += plugin.doorListener.adjustYaw[1][innerD.ordinal()];
                                        break;
                                    case SOUTH:
                                        yaw += plugin.doorListener.adjustYaw[2][innerD.ordinal()];
                                        break;
                                    case EAST:
                                        yaw += plugin.doorListener.adjustYaw[3][innerD.ordinal()];
                                        break;
                                }
                            }
                            tmp_loc.setYaw(yaw);
                            final Location tardis_loc = tmp_loc;
                            World playerWorld = player.getLocation().getWorld();
                            plugin.doorListener.movePlayer(player, tardis_loc, false, playerWorld, false);
                            // put player into travellers table
                            QueryFactory qf = new QueryFactory(plugin);
                            HashMap<String, Object> set = new HashMap<String, Object>();
                            set.put("tardis_id", id);
                            set.put("player", player.getName());
                            qf.doInsert("travellers", set);
                            return true;
                        }
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
                        String currentLoc = rs.getCurrent();
                        TARDISConstants.SCHEMATIC schm = rs.getSchematic();
                        TARDISConstants.COMPASS d = rs.getDirection();
                        boolean cham = rs.isChamele_on();
                        String chunkLoc = rs.getChunk();
                        String[] cdata = chunkLoc.split(":");
                        String name = cdata[0];
                        World cw = plugin.getServer().getWorld(name);
                        int restore = 0;
                        // if (!create_worlds) set the restore block
                        if (!name.contains("TARDIS_WORLD_") && cw.getWorldType() != WorldType.FLAT) {
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
                        QueryFactory qf = new QueryFactory(plugin);
                        HashMap<String, Object> whered = new HashMap<String, Object>();
                        whered.put("tardis_id", id);
                        if (rst.resultSet()) {
                            qf.doDelete("travellers", whered);
                        }
                        // get the current location
                        Location bb_loc = plugin.utils.getLocationFromDB(currentLoc, 0, 0);
                        if (bb_loc == null) {
                            sender.sendMessage(plugin.pluginName + "Could not get the location of the TARDIS!");
                            return true;
                        }
                        // destroy the TARDIS
                        if ((plugin.getConfig().getBoolean("create_worlds") && !plugin.getConfig().getBoolean("default_world")) || name.contains("TARDIS_WORLD_")) {
                            // delete TARDIS world
                            List<Player> players = cw.getPlayers();
                            for (Player p : players) {
                                p.kickPlayer("World scheduled for deletion!");
                            }
                            if (plugin.pm.isPluginEnabled("Multiverse-Core")) {
                                plugin.getServer().dispatchCommand(plugin.console, "mv remove " + name);
                            }
                            if (plugin.pm.isPluginEnabled("MultiWorld")) {
                                plugin.getServer().dispatchCommand(plugin.console, "mw unload " + name);
                            }
                            if (plugin.pm.isPluginEnabled("WorldBorder")) {
                                // wb <world> clear
                                plugin.getServer().dispatchCommand(plugin.console, "wb " + name + " clear");
                            }
                            plugin.getServer().unloadWorld(cw, true);
                            File world_folder = new File(plugin.getServer().getWorldContainer() + File.separator + name + File.separator);
                            if (!deleteFolder(world_folder)) {
                                plugin.debug("Could not delete world <" + name + ">");
                            }
                        } else {
                            plugin.destroyI.destroyInner(schm, id, cw, restore, args[1]);
                        }
                        if (!rs.isHidden()) {
                            plugin.destroyPB.destroyPoliceBox(bb_loc, d, id, false, false, false, null);
                        }
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
                        HashMap<String, Object> whereb = new HashMap<String, Object>();
                        whereb.put("tardis_id", id);
                        qf.doDelete("blocks", whereb);
                        HashMap<String, Object> wherev = new HashMap<String, Object>();
                        wherev.put("tardis_id", id);
                        qf.doDelete("travellers", wherev);
                        HashMap<String, Object> whereg = new HashMap<String, Object>();
                        whereg.put("tardis_id", id);
                        qf.doDelete("gravity_well", whereg);
                        HashMap<String, Object> wheres = new HashMap<String, Object>();
                        wheres.put("tardis_id", id);
                        qf.doDelete("destinations", wheres);
                        sender.sendMessage(plugin.pluginName + "The TARDIS was removed from the world and database successfully.");
                    } else {
                        sender.sendMessage(plugin.pluginName + "Could not find player [" + args[1] + "] in the database!");
                        return true;
                    }
                    return true;
                }
                if (first.equals("key") || first.equals("stattenheim") || first.equals("full_charge_item") || first.equals("jettison_seed")) {
                    String setMaterial = args[1].toUpperCase(Locale.ENGLISH);
                    if (!Arrays.asList(TARDISMaterials.MATERIAL_LIST).contains(setMaterial)) {
                        sender.sendMessage(plugin.pluginName + ChatColor.RED + "That is not a valid Material! Try checking http://jd.bukkit.org/apidocs/org/bukkit/Material.html");
                        return false;
                    } else {
                        if (first.equals("full_charge_item") || first.equals("jettison_seed")) {
                            plugin.getArtronConfig().set(first, setMaterial);
                            try {
                                plugin.getArtronConfig().save(new File(plugin.getDataFolder(), "artron.yml"));
                            } catch (IOException io) {
                                plugin.debug("Could not save artron.yml, " + io);
                            }
                        } else {
                            plugin.getConfig().set(first, setMaterial);
                        }
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
                if (first.equals("difficulty")) {
                    if (!args[1].equalsIgnoreCase("easy") && !args[1].equalsIgnoreCase("normal") && !args[1].equalsIgnoreCase("hard")) {
                        sender.sendMessage(plugin.pluginName + ChatColor.RED + "Difficulty must be easy, normal or hard!");
                        return true;
                    }
                    plugin.getConfig().set("difficulty", args[1].toLowerCase(Locale.ENGLISH));
                }
                if (first.equals("gamemode")) {
                    if (!args[1].equalsIgnoreCase("creative") && !args[1].equalsIgnoreCase("survival")) {
                        sender.sendMessage(plugin.pluginName + ChatColor.RED + "Gamemode must be creative or survival!");
                        return true;
                    }
                    plugin.getConfig().set("gamemode", args[1].toLowerCase(Locale.ENGLISH));
                }
                if (first.equals("inventory_group")) {
                    plugin.getConfig().set("inventory_group", args[1]);
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
                // checks if its a boolean config option
                if (firstsBool.contains(first)) {
                    // check they typed true of false
                    String tf = args[1].toLowerCase(Locale.ENGLISH);
                    if (!tf.equals("true") && !tf.equals("false")) {
                        sender.sendMessage(plugin.pluginName + ChatColor.RED + "The last argument must be true or false!");
                        return false;
                    }
                    plugin.getConfig().set(first, Boolean.valueOf(tf));
                }
                // checks if its a number config option
                if (firstsInt.contains(first) || firstsIntArtron.contains(first)) {
                    String a = args[1];
                    int val;
                    try {
                        val = Integer.parseInt(a);
                    } catch (NumberFormatException nfe) {
                        // not a number
                        sender.sendMessage(plugin.pluginName + ChatColor.RED + " The last argument must be a number!");
                        return false;
                    }
                    if (firstsIntArtron.contains(first)) {
                        plugin.getArtronConfig().set(first, val);
                        try {
                            plugin.getArtronConfig().save(new File(plugin.getDataFolder(), "artron.yml"));
                        } catch (IOException io) {
                            plugin.debug("Could not save artron.yml, " + io);
                        }
                    } else {
                        plugin.getConfig().set(first, val);
                        if (first.equals("terminal_step") && plugin.bukkitversion.compareTo(plugin.preIMversion) >= 0) {
                            // reset the terminal inventory
                            plugin.buttonListener.items = new TARDISTerminalInventory().getTerminal();
                        }
                    }
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
