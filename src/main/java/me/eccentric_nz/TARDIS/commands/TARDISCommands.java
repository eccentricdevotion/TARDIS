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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.builders.TARDISChameleonCircuit;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCondenser;
import me.eccentric_nz.TARDIS.database.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import me.eccentric_nz.TARDIS.destroyers.TARDISExterminator;
import me.eccentric_nz.TARDIS.files.TARDISUpdateChecker;
import me.eccentric_nz.TARDIS.rooms.TARDISCondenserData;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import me.eccentric_nz.TARDIS.thirdparty.Version;
import me.eccentric_nz.TARDIS.travel.TARDISPluginRespect;
import me.eccentric_nz.TARDIS.travel.TARDISTimetravel;
import me.eccentric_nz.TARDIS.utility.TARDISItemRenamer;
import me.eccentric_nz.TARDIS.utility.TARDISLampScanner;
import me.eccentric_nz.TARDIS.utility.TARDISLister;
import me.eccentric_nz.tardischunkgenerator.TARDISChunkGenerator;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;

/**
 * Command /tardis [arguments].
 *
 * A TARDIS console room or control room is the area which houses the TARDIS'
 * control console, by which the TARDIS was operated.
 *
 * @author eccentric_nz
 */
public class TARDISCommands implements CommandExecutor {

    private TARDIS plugin;
    private TARDISPluginRespect respect;
    HashSet<Byte> transparent = new HashSet<Byte>();
    private List<String> firstArgs = new ArrayList<String>();
    public List<String> roomArgs = new ArrayList<String>();
    Version bukkitversion;
    Version preIMversion = new Version("1.4.5");
    Version SUBversion;
    Version preSUBversion = new Version("1.0");

    public TARDISCommands(TARDIS plugin) {
        this.plugin = plugin;
        // add transparent blocks
        transparent.add((byte) Material.AIR.getId());
        transparent.add((byte) Material.DEAD_BUSH.getId());
        transparent.add((byte) Material.IRON_FENCE.getId());
        transparent.add((byte) Material.LONG_GRASS.getId());
        transparent.add((byte) Material.SNOW.getId());
        transparent.add((byte) Material.VINE.getId());
        // add first arguments
        firstArgs.add("add");
        firstArgs.add("bind");
        firstArgs.add("chameleon");
        firstArgs.add("check_loc");
        firstArgs.add("comehere");
        firstArgs.add("direction");
        firstArgs.add("exterminate");
        firstArgs.add("find");
        firstArgs.add("gravity");
        firstArgs.add("help");
        firstArgs.add("hide");
        firstArgs.add("home");
        firstArgs.add("jettison");
        firstArgs.add("list");
        firstArgs.add("lamps");
        firstArgs.add("namekey");
        firstArgs.add("occupy");
        firstArgs.add("rebuild");
        firstArgs.add("reload");
        firstArgs.add("remove");
        firstArgs.add("removesave");
        firstArgs.add("gravity");
        firstArgs.add("room");
        firstArgs.add("save");
        firstArgs.add("secondary");
        firstArgs.add("setdest");
        firstArgs.add("unbind");
        firstArgs.add("update");
        firstArgs.add("version");
        firstArgs.add("inside");
        // rooms - only add if enabled in the config
        for (String r : plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false)) {
            if (plugin.getRoomsConfig().getBoolean("rooms." + r + ".enabled")) {
                roomArgs.add(r);
            }
        }
        String[] v = Bukkit.getServer().getBukkitVersion().split("-");
        bukkitversion = (!v[0].equalsIgnoreCase("unknown")) ? new Version(v[0]) : new Version("1.4.7");
        SUBversion = (!v[0].equalsIgnoreCase("unknown")) ? new Version(v[1].substring(1, v[1].length())) : new Version("4.7");
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
            if (!firstArgs.contains(args[0].toLowerCase(Locale.ENGLISH))) {
                sender.sendMessage(plugin.pluginName + "That command wasn't recognised type " + ChatColor.GREEN + "/tardis help" + ChatColor.RESET + " to see the commands");
                return false;
            }
            // delete the TARDIS
            if (args[0].equalsIgnoreCase("exterminate")) {
                if (player == null) {
                    sender.sendMessage(plugin.pluginName + "You must be a player to run this command!");
                    return false;
                }
                if (!plugin.trackExterminate.containsKey(player.getName())) {
                    sender.sendMessage(plugin.pluginName + "You must break the TARDIS Police Box sign first!");
                    return false;
                }
                TARDISExterminator del = new TARDISExterminator(plugin);
                return del.exterminate(player, plugin.trackExterminate.get(player.getName()));
            }
            // temporary command to convert old gravity well to new style
            if (args[0].equalsIgnoreCase("gravity")) {
                if (player == null) {
                    sender.sendMessage(plugin.pluginName + "Must be a player");
                    return false;
                }
                // get the players TARDIS id
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("owner", player.getName());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (!rs.resultSet()) {
                    sender.sendMessage(plugin.pluginName + TARDISConstants.NO_TARDIS);
                    return false;
                }
                int id = rs.getTardis_id();
                try {
                    TARDISDatabase service = TARDISDatabase.getInstance();
                    Connection connection = service.getConnection();
                    Statement statement = connection.createStatement();
                    String query = "SELECT * FROM gravity WHERE tardis_id = " + id;
                    ResultSet rsg = statement.executeQuery(query);
                    if (rsg.isBeforeFirst()) {
                        String up = "Location{world=" + rsg.getString("world") + ",x=" + rsg.getFloat("upx") + ",y=10.0,z=" + rsg.getFloat("upz") + ",pitch=0.0,yaw=0.0}";
                        Double[] values = new Double[3];
                        values[0] = 1D;
                        values[1] = 11D;
                        values[2] = 0.5D;
                        plugin.gravityUpList.put(up, values);
                        String down = "Location{world=" + rsg.getString("world") + ",x=" + rsg.getFloat("downx") + ",y=10.0,z=" + rsg.getFloat("downz") + ",pitch=0.0,yaw=0.0}";
                        plugin.gravityDownList.add(down);
                        HashMap<String, Object> setu = new HashMap<String, Object>();
                        setu.put("tardis_id", id);
                        setu.put("location", up);
                        setu.put("direction", 1);
                        setu.put("distance", 11);
                        setu.put("velocity", 0.5);
                        HashMap<String, Object> setd = new HashMap<String, Object>();
                        setd.put("tardis_id", id);
                        setd.put("location", down);
                        setd.put("direction", 0);
                        setd.put("distance", 0);
                        setd.put("velocity", 0);
                        QueryFactory qf = new QueryFactory(plugin);
                        qf.doInsert("gravity_well", setu);
                        qf.doInsert("gravity_well", setd);
                        player.sendMessage(plugin.pluginName + "Gravity well converted successfully.");
                        return true;
                    }
                } catch (SQLException e) {
                    plugin.debug("Gravity conversion error: " + e.getMessage());
                }
            }
            if (args[0].equalsIgnoreCase("version")) {
                FileConfiguration pluginYml = YamlConfiguration.loadConfiguration(plugin.pm.getPlugin("TARDIS").getResource("plugin.yml"));
                String version = pluginYml.getString("version");
                String cb = Bukkit.getVersion();
                sender.sendMessage(plugin.pluginName + "You are running TARDIS version: " + ChatColor.AQUA + version + ChatColor.RESET + " with CraftBukkit " + cb);
                // also check if there is an update
                if (plugin.getConfig().getBoolean("check_for_updates")) {
                    TARDISUpdateChecker update = new TARDISUpdateChecker(plugin);
                    update.checkVersion(player);
                }
                return true;
            }
            if (player == null) {
                sender.sendMessage(plugin.pluginName + ChatColor.RED + " This command can only be run by a player");
                return false;
            } else {
                if (args[0].equalsIgnoreCase("lamps")) {
                    TARDISLampScanner tls = new TARDISLampScanner(plugin);
                    return tls.addLampBlocks(player);
                }
                if (args[0].equalsIgnoreCase("chameleon")) {
                    if (!plugin.getConfig().getBoolean("chameleon")) {
                        sender.sendMessage(plugin.pluginName + "This server does not allow the use of the chameleon circuit!");
                        return false;
                    }
                    if (player.hasPermission("tardis.timetravel")) {
                        if (args.length < 2 || (!args[1].equalsIgnoreCase("on") && !args[1].equalsIgnoreCase("off") && !args[1].equalsIgnoreCase("short") && !args[1].equalsIgnoreCase("reset"))) {
                            sender.sendMessage(plugin.pluginName + "Too few command arguments!");
                            return false;
                        }
                        // get the players TARDIS id
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("owner", player.getName());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (!rs.resultSet()) {
                            sender.sendMessage(plugin.pluginName + TARDISConstants.NO_TARDIS);
                            return false;
                        }
                        int id = rs.getTardis_id();
                        String chamStr = rs.getChameleon();
                        if (chamStr.isEmpty()) {
                            sender.sendMessage(plugin.pluginName + "Could not find the Chameleon Circuit!");
                            return false;
                        } else {
                            QueryFactory qf = new QueryFactory(plugin);
                            HashMap<String, Object> tid = new HashMap<String, Object>();
                            HashMap<String, Object> set = new HashMap<String, Object>();
                            tid.put("tardis_id", id);
                            if (args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("off")) {
                                int x, y, z;
                                String[] chamData = chamStr.split(":");
                                World w = plugin.getServer().getWorld(chamData[0]);
                                TARDISConstants.COMPASS d = rs.getDirection();
                                x = plugin.utils.parseNum(chamData[1]);
                                y = plugin.utils.parseNum(chamData[2]);
                                z = plugin.utils.parseNum(chamData[3]);
                                Block chamBlock = w.getBlockAt(x, y, z);
                                Material chamType = chamBlock.getType();
                                if (chamType == Material.WALL_SIGN || chamType == Material.SIGN_POST) {
                                    Sign cs = (Sign) chamBlock.getState();
                                    if (args[1].equalsIgnoreCase("on")) {
                                        set.put("chamele_on", 1);
                                        qf.doUpdate("tardis", set, tid);
                                        sender.sendMessage(plugin.pluginName + "The Chameleon Circuit was turned ON!");
                                        cs.setLine(3, ChatColor.GREEN + "ON");
                                    }
                                    if (args[1].equalsIgnoreCase("off")) {
                                        set.put("chamele_on", 0);
                                        qf.doUpdate("tardis", set, tid);
                                        sender.sendMessage(plugin.pluginName + "The Chameleon Circuit was turned OFF.");
                                        cs.setLine(3, ChatColor.RED + "OFF");
                                    }
                                    cs.update();
                                }
                            }
                            if (args[1].equalsIgnoreCase("short")) {
                                // get the block the player is targeting
                                Block target_block = player.getTargetBlock(transparent, 50).getLocation().getBlock();
                                TARDISChameleonCircuit tcc = new TARDISChameleonCircuit(plugin);
                                int[] b_data = tcc.getChameleonBlock(target_block, player, true);
                                int c_id = b_data[0], c_data = b_data[1];
                                set.put("chameleon_id", c_id);
                                set.put("chameleon_data", c_data);
                                qf.doUpdate("tardis", set, tid);
                                boolean bluewool = (c_id == 35 && c_data == (byte) 11);
                                if (!bluewool) {
                                    sender.sendMessage(plugin.pluginName + "The Chameleon Circuit was shorted out to: " + target_block.getType().toString() + ".");
                                }
                            }
                            if (args[1].equalsIgnoreCase("reset")) {
                                set.put("chameleon_id", 35);
                                set.put("chameleon_data", 11);
                                qf.doUpdate("tardis", set, tid);
                                sender.sendMessage(plugin.pluginName + "The Chameleon Circuit was repaired.");
                            }
                        }
                        return true;
                    } else {
                        sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("room")) {
                    if (args.length < 2) {
                        player.sendMessage(plugin.pluginName + "Too few command arguments!");
                        return false;
                    }
                    String room = args[1].toUpperCase(Locale.ENGLISH);
                    StringBuilder buf = new StringBuilder();
                    for (String rl : roomArgs) {
                        buf.append(rl).append(", ");
                    }
                    String roomlist = buf.toString().substring(0, buf.length() - 2);
                    if (room.equals("HELP")) {
                        player.sendMessage(plugin.pluginName + "There are currently " + roomArgs.size() + " room types! They are: " + roomlist + ".");
                        player.sendMessage("View a TARDIS room gallery at http://eccentricdevotion.github.com/TARDIS/room-gallery.html");
                        return true;
                    }
                    if (!roomArgs.contains(room)) {
                        player.sendMessage(plugin.pluginName + "That is not a valid room type! Try one of: " + roomlist + ".");
                        return true;
                    }
                    String perm = "tardis.room." + args[1].toLowerCase(Locale.ENGLISH);
                    if (!player.hasPermission(perm)) {
                        String grammar = (TARDISConstants.vowels.contains(room.substring(0, 1))) ? "an" : "a";
                        sender.sendMessage(plugin.pluginName + "You do not have permission to grow " + grammar + " " + room);
                        return true;
                    }
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("owner", player.getName());
                    ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                    if (!rs.resultSet()) {
                        player.sendMessage(plugin.pluginName + "You are not a Timelord. You need to create a TARDIS first before using this command!");
                        return true;
                    }
                    String chunk = rs.getChunk();
                    String[] data = chunk.split(":");
                    World room_world = plugin.getServer().getWorld(data[0]);
                    ChunkGenerator gen = room_world.getGenerator();
                    WorldType wt = room_world.getWorldType();
                    boolean special = (data[0].contains("TARDIS_TimeVortex") && (wt.equals(WorldType.FLAT) || gen instanceof TARDISChunkGenerator));
                    if (!data[0].contains("TARDIS_WORLD_") && !special) {
                        player.sendMessage(plugin.pluginName + "You cannot grow rooms unless your TARDIS was created in its own world!");
                        return true;
                    }
                    int id = rs.getTardis_id();
                    int level = rs.getArtron_level();
                    // check they are in the tardis
                    HashMap<String, Object> wheret = new HashMap<String, Object>();
                    wheret.put("player", player.getName());
                    wheret.put("tardis_id", id);
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
                    if (!rst.resultSet()) {
                        player.sendMessage(plugin.pluginName + "You are not inside your TARDIS. You need to be to run this command!");
                        return true;
                    }
                    // check they have enough artron energy
                    if (level < plugin.getRoomsConfig().getInt("rooms." + room + ".cost")) {
                        player.sendMessage(plugin.pluginName + "The TARDIS does not have enough Artron Energy to grow this room!");
                        return true;
                    }
                    if (plugin.getConfig().getBoolean("rooms_require_blocks")) {
                        HashMap<String, Integer> blockIDCount = new HashMap<String, Integer>();
                        boolean hasRequired = true;
                        HashMap<String, Integer> roomBlocks = plugin.roomBlockCounts.get(room);
                        String wall = "ORANGE_WOOL";
                        String floor = "LIGHT_GREY_WOOL";
                        HashMap<String, Object> wherepp = new HashMap<String, Object>();
                        boolean hasPrefs = false;
                        wherepp.put("player", player.getName());
                        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
                        if (rsp.resultSet()) {
                            hasPrefs = true;
                            wall = rsp.getWall();
                            floor = rsp.getFloor();
                        }
                        for (Map.Entry<String, Integer> entry : roomBlocks.entrySet()) {
                            String[] block_data = entry.getKey().split(":");
                            int bid = plugin.utils.parseNum(block_data[0]);
                            String mat;
                            String bdata;
                            if (hasPrefs && block_data.length == 2 && (block_data[1].equals("1") || block_data[1].equals("8"))) {
                                mat = (block_data[1].equals("1")) ? wall : floor;
                                TARDISWalls tw = new TARDISWalls();
                                Integer[] iddata = tw.blocks.get(mat);
                                bdata = String.format("%d", iddata[0]);
                            } else {
                                mat = Material.getMaterial(bid).toString();
                                bdata = String.format("%d", bid);
                            }
                            int tmp = Math.round((entry.getValue() / 100.0F) * plugin.getConfig().getInt("rooms_condenser_percent"));
                            int required = (tmp > 0) ? tmp : 1;
                            blockIDCount.put(bdata, required);
                            HashMap<String, Object> wherec = new HashMap<String, Object>();
                            wherec.put("tardis_id", id);
                            wherec.put("block_data", bdata);
                            ResultSetCondenser rsc = new ResultSetCondenser(plugin, wherec, false);
                            if (rsc.resultSet()) {
                                if (rsc.getBlock_count() < required) {
                                    hasRequired = false;
                                    int diff = required - rsc.getBlock_count();
                                    player.sendMessage(plugin.pluginName + "You need to condense " + diff + " more " + mat + "!");
                                }
                            } else {
                                hasRequired = false;
                                player.sendMessage(plugin.pluginName + "You need to condense a minimum of " + required + " " + mat);
                            }
                        }
                        if (hasRequired == false) {
                            player.sendMessage("-----------------------------");
                            return true;
                        }
                        TARDISCondenserData c_data = new TARDISCondenserData();
                        c_data.setBlockIDCount(blockIDCount);
                        c_data.setTardis_id(id);
                        plugin.roomCondenserData.put(player.getName(), c_data);
                    }
                    String message;
                    // if it is a gravity well
                    if (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) {
                        message = "Place the GRAVITY WELL seed block (" + plugin.getRoomsConfig().getString("rooms." + room + ".seed") + ") into the centre of the floor in an empty room, then hit it with the TARDIS key to start growing your room!";
                    } else {
                        message = "Place the " + room + " seed block (" + plugin.getRoomsConfig().getString("rooms." + room + ".seed") + ") where the door should be, then hit it with the TARDIS key to start growing your room!";
                    }
                    plugin.trackRoomSeed.put(player.getName(), room);
                    player.sendMessage(plugin.pluginName + message);
                    return true;

                }
                if (args[0].equalsIgnoreCase("jettison")) {
                    if (player.hasPermission("tardis.room")) {
                        if (args.length < 2) {
                            player.sendMessage(plugin.pluginName + "Too few command arguments!");
                            return false;
                        }
                        String room = args[1].toUpperCase(Locale.ENGLISH);
                        if (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) {
                            player.sendMessage(plugin.pluginName + "You cannot jettison gravity wells! To remove the gravity blocks use the " + ChatColor.AQUA + "/tardisgravity remove" + ChatColor.RESET + " command.");
                            return true;
                        }
                        if (!roomArgs.contains(room)) {
                            StringBuilder buf = new StringBuilder(args[1]);
                            for (String rl : roomArgs) {
                                buf.append(rl).append(", ");
                            }
                            String roomlist = buf.toString().substring(0, buf.length() - 2);
                            player.sendMessage(plugin.pluginName + "That is not a valid room type! Try one of: " + roomlist);
                            return true;
                        }
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("owner", player.getName());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (!rs.resultSet()) {
                            player.sendMessage(plugin.pluginName + "You are not a Timelord. You need to create a TARDIS first before using this command!");
                            return true;
                        }
                        int id = rs.getTardis_id();
                        // check they are in the tardis
                        HashMap<String, Object> wheret = new HashMap<String, Object>();
                        wheret.put("player", player.getName());
                        wheret.put("tardis_id", id);
                        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
                        if (!rst.resultSet()) {
                            player.sendMessage(plugin.pluginName + "You are not inside your TARDIS. You need to be to run this command!");
                            return true;
                        }
                        plugin.trackJettison.put(player.getName(), room);
                        String seed = plugin.getArtronConfig().getString("jettison_seed");
                        player.sendMessage(plugin.pluginName + "Stand in the doorway of the room you want to jettison and place a " + seed + " block directly in front of the door. Hit the " + seed + " with the TARDIS key to jettison the room!");
                        return true;
                    } else {
                        sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("occupy")) {
                    if (player.hasPermission("tardis.timetravel")) {
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("owner", player.getName());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (!rs.resultSet()) {
                            sender.sendMessage(plugin.pluginName + " You must be the Timelord of the TARDIS to use this command!");
                            return false;
                        }
                        int id = rs.getTardis_id();
                        HashMap<String, Object> wheret = new HashMap<String, Object>();
                        wheret.put("tardis_id", id);
                        wheret.put("player", player.getName());
                        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
                        String occupied;
                        QueryFactory qf = new QueryFactory(plugin);
                        if (rst.resultSet()) {
                            HashMap<String, Object> whered = new HashMap<String, Object>();
                            whered.put("tardis_id", id);
                            whered.put("player", player.getName());
                            qf.doDelete("travellers", whered);
                            occupied = ChatColor.RED + "UNOCCUPIED";
                        } else {
                            HashMap<String, Object> wherei = new HashMap<String, Object>();
                            wherei.put("tardis_id", id);
                            wherei.put("player", player.getName());
                            qf.doInsert("travellers", wherei);
                            occupied = ChatColor.GREEN + "OCCUPIED";
                        }
                        sender.sendMessage(plugin.pluginName + " TARDIS occupation was set to: " + occupied);
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
                            sender.sendMessage(plugin.pluginName + "The server admin will not allow you to bring the TARDIS to this world!");
                            return true;
                        }
                        respect = new TARDISPluginRespect(plugin);
                        if (!respect.getRespect(player, eyeLocation, true)) {
                            return true;
                        }
                        if (player.hasPermission("tardis.exile") && plugin.getConfig().getBoolean("exile")) {
                            String areaPerm = plugin.ta.getExileArea(player);
                            if (plugin.ta.areaCheckInExile(areaPerm, eyeLocation)) {
                                sender.sendMessage(plugin.pluginName + "You exile status does not allow you to bring the TARDIS to this location!");
                                return true;
                            }
                        }
                        if (!plugin.ta.areaCheckInExisting(eyeLocation)) {
                            sender.sendMessage(plugin.pluginName + "You cannot use /tardis comehere to bring the Police Box to a TARDIS area! Please use " + ChatColor.AQUA + "/tardistravel area [area name]");
                            return true;
                        }
                        Material m = player.getTargetBlock(transparent, 50).getType();
                        if (m != Material.SNOW) {
                            int yplusone = eyeLocation.getBlockY();
                            eyeLocation.setY(yplusone + 1);
                        }
                        // check the world is not excluded
                        String world = eyeLocation.getWorld().getName();
                        if (!plugin.getConfig().getBoolean("worlds." + world)) {
                            sender.sendMessage(plugin.pluginName + "You cannot bring the TARDIS Police Box to this world");
                            return true;
                        }
                        // check they are a timelord
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("owner", player.getName());
                        final ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (!rs.resultSet()) {
                            sender.sendMessage(plugin.pluginName + "You must be the Timelord of the TARDIS to use this command!");
                            return true;
                        }
                        final int id = rs.getTardis_id();
                        // check they are not in the tardis
                        HashMap<String, Object> wherettrav = new HashMap<String, Object>();
                        wherettrav.put("player", player.getName());
                        wherettrav.put("tardis_id", id);
                        ResultSetTravellers rst = new ResultSetTravellers(plugin, wherettrav, false);
                        if (rst.resultSet()) {
                            player.sendMessage(plugin.pluginName + "You cannot bring the Police Box here because you are inside a TARDIS!");
                            return true;
                        }
                        if (plugin.tardisMaterialising.contains(id)) {
                            sender.sendMessage(plugin.pluginName + "You cannot do that while the TARDIS is materialising!");
                            return true;
                        }
                        final TARDISConstants.COMPASS d = rs.getDirection();
                        TARDISTimetravel tt = new TARDISTimetravel(plugin);
                        int[] start_loc = tt.getStartLocation(eyeLocation, d);
                        // safeLocation(int startx, int starty, int startz, int resetx, int resetz, World w, TARDISConstants.COMPASS d)
                        int count = tt.safeLocation(start_loc[0], eyeLocation.getBlockY(), start_loc[2], start_loc[1], start_loc[3], eyeLocation.getWorld(), d);
                        if (count > 0) {
                            sender.sendMessage(plugin.pluginName + "That location would grief existing blocks! Try somewhere else!");
                            return true;
                        }
                        int level = rs.getArtron_level();
                        int ch = plugin.getArtronConfig().getInt("comehere");
                        if (level < ch) {
                            player.sendMessage(plugin.pluginName + ChatColor.RED + "The TARDIS does not have enough Artron Energy to make this trip!");
                            return true;
                        }
                        final Player p = player;
                        String badsave = rs.getCurrent();
                        boolean chamtmp = false;
                        if (plugin.getConfig().getBoolean("chameleon")) {
                            chamtmp = rs.isChamele_on();
                        }
                        final boolean cham = chamtmp;
                        String[] saveData = badsave.split(":");
                        World w = plugin.getServer().getWorld(saveData[0]);
                        if (w != null) {
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
                            sender.sendMessage(plugin.pluginName + "The TARDIS is coming...");
                            long delay = (plugin.getConfig().getBoolean("materialise")) ? 1L : 180L;
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    if (!rs.isHidden()) {
                                        plugin.tardisDematerialising.add(id);
                                        plugin.destroyPB.destroyPoliceBox(oldSave, d, id, false, plugin.getConfig().getBoolean("materialise"), cham, p);
//                                        plugin.destroyPB.destroyPlatform(rs.getPlatform(), id);
//                                        plugin.destroyPB.destroySign(oldSave, d);
//                                        plugin.destroyPB.destroyTorch(oldSave);
                                    }
                                }
                            }, delay);
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    plugin.buildPB.buildPoliceBox(id, eyeLocation, d, cham, p, false, false);
                                }
                            }, delay * 2);
                            // remove energy from TARDIS
                            HashMap<String, Object> wheret = new HashMap<String, Object>();
                            wheret.put("tardis_id", id);
                            qf.alterEnergyLevel("tardis", -ch, wheret, player);
                            plugin.tardisHasDestination.remove(id);
                            return true;
                        } else {
                            sender.sendMessage(plugin.pluginName + "Could not get the previous location of the TARDIS!");
                            return true;
                        }
                    } else {
                        sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("check_loc")) {
                    final Location eyeLocation = player.getTargetBlock(transparent, 50).getLocation();
                    Material m = player.getTargetBlock(transparent, 50).getType();
                    if (m != Material.SNOW) {
                        int yplusone = eyeLocation.getBlockY();
                        eyeLocation.setY(yplusone + 1);
                    }
                    // check they are a timelord
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("owner", player.getName());
                    ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                    if (!rs.resultSet()) {
                        sender.sendMessage(plugin.pluginName + "You must be the Timelord of a TARDIS to use this command!");
                        return true;
                    }
                    final TARDISConstants.COMPASS d = rs.getDirection();
                    TARDISTimetravel tt = new TARDISTimetravel(plugin);
                    tt.testSafeLocation(eyeLocation, d);
                    return true;
                }
                if (args[0].equalsIgnoreCase("inside")) {
                    // check they are a timelord
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("owner", player.getName());
                    ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                    if (!rs.resultSet()) {
                        sender.sendMessage(plugin.pluginName + "You must be the Timelord of a TARDIS to use this command!");
                        return true;
                    }
                    int id = rs.getTardis_id();
                    HashMap<String, Object> wheret = new HashMap<String, Object>();
                    wheret.put("tardis_id", id);
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, true);
                    if (rst.resultSet()) {
                        List<String> data = rst.getData();
                        sender.sendMessage(plugin.pluginName + "The players inside your TARDIS are:");
                        for (String s : data) {
                            sender.sendMessage(s);
                        }
                    } else {
                        sender.sendMessage(plugin.pluginName + "Nobody is inside your TARDIS.");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("home")) {
                    if (player.hasPermission("tardis.timetravel")) {
                        Location eyeLocation = player.getTargetBlock(transparent, 50).getLocation();
                        if (!plugin.getConfig().getBoolean("include_default_world") && plugin.getConfig().getBoolean("default_world") && eyeLocation.getWorld().getName().equals(plugin.getConfig().getString("default_world_name"))) {
                            sender.sendMessage(plugin.pluginName + "The server admin will not allow you to set the TARDIS home in this world!");
                            return true;
                        }
                        if (!plugin.ta.areaCheckInExisting(eyeLocation)) {
                            sender.sendMessage(plugin.pluginName + "You cannot use /tardis home in a TARDIS area! Please use " + ChatColor.AQUA + "/tardistravel area [area name]");
                            return true;
                        }
                        respect = new TARDISPluginRespect(plugin);
                        if (!respect.getRespect(player, eyeLocation, true)) {
                            return true;
                        }
                        Material m = player.getTargetBlock(transparent, 50).getType();
                        if (m != Material.SNOW) {
                            int yplusone = eyeLocation.getBlockY();
                            eyeLocation.setY(yplusone + 1);
                        }
                        // check the world is not excluded
                        String world = eyeLocation.getWorld().getName();
                        if (!plugin.getConfig().getBoolean("worlds." + world)) {
                            sender.sendMessage(plugin.pluginName + "You cannot set the TARDIS home location to this world");
                            return true;
                        }
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("owner", player.getName());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (!rs.resultSet()) {
                            sender.sendMessage(plugin.pluginName + "You must be the Timelord of the TARDIS to use this command!");
                            return false;
                        }
                        int id = rs.getTardis_id();
                        // check they are not in the tardis
                        HashMap<String, Object> wherettrav = new HashMap<String, Object>();
                        wherettrav.put("player", player.getName());
                        wherettrav.put("tardis_id", id);
                        ResultSetTravellers rst = new ResultSetTravellers(plugin, wherettrav, false);
                        if (rst.resultSet()) {
                            player.sendMessage(plugin.pluginName + "You cannot set the home location here because you are inside a TARDIS!");
                            return true;
                        }
                        String sethome = eyeLocation.getWorld().getName() + ":" + eyeLocation.getBlockX() + ":" + eyeLocation.getBlockY() + ":" + eyeLocation.getBlockZ();
                        QueryFactory qf = new QueryFactory(plugin);
                        HashMap<String, Object> tid = new HashMap<String, Object>();
                        HashMap<String, Object> set = new HashMap<String, Object>();
                        tid.put("tardis_id", id);
                        set.put("home", sethome);
                        qf.doUpdate("tardis", set, tid);
                        sender.sendMessage(plugin.pluginName + "The new TARDIS home was set!");
                        return true;
                    } else {
                        sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("update")) {
                    if (player.hasPermission("tardis.update")) {
                        String[] validBlockNames = {"door", "button", "world-repeater", "x-repeater", "z-repeater", "y-repeater", "chameleon", "save-sign", "artron", "handbrake", "condenser", "scanner", "backdoor", "keyboard"};
                        if (args.length < 2) {
                            sender.sendMessage(plugin.pluginName + "Too few command arguments!");
                            return false;
                        }
                        String tardis_block = args[1].toLowerCase(Locale.ENGLISH);
                        if (!Arrays.asList(validBlockNames).contains(tardis_block)) {
                            player.sendMessage(plugin.pluginName + "That is not a valid TARDIS block name! Try one of : door|button|world-repeater|x-repeater|z-repeater|y-repeater|chameleon|save-sign|artron|handbrake|condenser|scanner|backdoor|keyboard");
                            return false;
                        }
                        if (!player.hasPermission("tardis.backdoor")) {
                            sender.sendMessage(plugin.pluginName + "You do not have permission to create a back door!");
                            return true;
                        }
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("owner", player.getName());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (!rs.resultSet()) {
                            sender.sendMessage(plugin.pluginName + "You are not a Timelord. You need to create a TARDIS first before using this command!");
                            return false;
                        }
                        if (!tardis_block.equals("backdoor")) {
                            HashMap<String, Object> wheret = new HashMap<String, Object>();
                            wheret.put("player", player.getName());
                            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
                            if (!rst.resultSet()) {
                                sender.sendMessage(plugin.pluginName + "You are not inside your TARDIS. You need to be to run this command!");
                                return false;
                            }
                        }
                        plugin.trackPlayers.put(player.getName(), tardis_block);
                        player.sendMessage(plugin.pluginName + "Click the TARDIS " + tardis_block + " to update its position.");
                        return true;
                    } else {
                        sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("secondary")) {
                    if (player.hasPermission("tardis.update")) {
                        String[] validBlockNames = {"button", "world-repeater", "x-repeater", "z-repeater", "y-repeater", "artron", "handbrake", "door"};
                        if (args.length < 2) {
                            sender.sendMessage(plugin.pluginName + "Too few command arguments!");
                            return false;
                        }
                        String tardis_block = args[1].toLowerCase(Locale.ENGLISH);
                        if (!Arrays.asList(validBlockNames).contains(tardis_block)) {
                            player.sendMessage(plugin.pluginName + "That is not a valid TARDIS block name! Try one of : button|world-repeater|x-repeater|z-repeater|y-repeater|artron|handbrake|door");
                            return false;
                        }
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("owner", player.getName());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (!rs.resultSet()) {
                            sender.sendMessage(plugin.pluginName + "You are not a Timelord. You need to create a TARDIS first before using this command!");
                            return false;
                        }
                        HashMap<String, Object> wheret = new HashMap<String, Object>();
                        wheret.put("player", player.getName());
                        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
                        if (!rst.resultSet()) {
                            sender.sendMessage(plugin.pluginName + "You are not inside your TARDIS. You need to be to run this command!");
                            return false;
                        }
                        plugin.trackSecondary.put(player.getName(), tardis_block);
                        player.sendMessage(plugin.pluginName + "Click the TARDIS " + tardis_block + " to update its position.");
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
                        boolean cham = false;
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("owner", player.getName());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (!rs.resultSet()) {
                            sender.sendMessage(plugin.pluginName + TARDISConstants.NO_TARDIS);
                            return false;
                        }
                        id = rs.getTardis_id();
                        HashMap<String, Object> wherein = new HashMap<String, Object>();
                        wherein.put("player", player.getName());
                        ResultSetTravellers rst = new ResultSetTravellers(plugin, wherein, false);
                        if (rst.resultSet() && args[0].equalsIgnoreCase("rebuild") && plugin.tardisHasDestination.containsKey(id)) {
                            sender.sendMessage(plugin.pluginName + "You cannot rebuild the TARDIS right now! Try travelling first.");
                            return true;
                        }
                        int level = rs.getArtron_level();
                        save = rs.getCurrent();
                        if (plugin.tardisMaterialising.contains(id)) {
                            sender.sendMessage(plugin.pluginName + "You cannot do that while the TARDIS is materialising!");
                            return true;
                        }
                        if (plugin.getConfig().getBoolean("chameleon")) {
                            cham = rs.isChamele_on();
                        }
                        d = rs.getDirection();
                        String[] save_data = save.split(":");
                        w = plugin.getServer().getWorld(save_data[0]);
                        x = plugin.utils.parseNum(save_data[1]);
                        y = plugin.utils.parseNum(save_data[2]);
                        z = plugin.utils.parseNum(save_data[3]);
                        Location l = new Location(w, x, y, z);
                        HashMap<String, Object> wheret = new HashMap<String, Object>();
                        wheret.put("tardis_id", id);
                        QueryFactory qf = new QueryFactory(plugin);
                        if (args[0].equalsIgnoreCase("rebuild")) {
                            int rebuild = plugin.getArtronConfig().getInt("random");
                            if (level < rebuild) {
                                player.sendMessage(plugin.pluginName + ChatColor.RED + "The TARDIS does not have enough Artron Energy to rebuild!");
                                return false;
                            }
                            plugin.buildPB.buildPoliceBox(id, l, d, cham, player, true, false);
                            sender.sendMessage(plugin.pluginName + "The TARDIS Police Box was rebuilt!");
                            qf.alterEnergyLevel("tardis", -rebuild, wheret, player);
                            // set hidden to false
                            if (rs.isHidden()) {
                                HashMap<String, Object> whereh = new HashMap<String, Object>();
                                whereh.put("tardis_id", id);
                                HashMap<String, Object> seth = new HashMap<String, Object>();
                                seth.put("hidden", 0);
                                qf.doUpdate("tardis", seth, whereh);
                            }
                            return true;
                        }
                        if (args[0].equalsIgnoreCase("hide")) {
                            int hide = plugin.getArtronConfig().getInt("hide");
                            if (level < hide) {
                                player.sendMessage(plugin.pluginName + ChatColor.RED + "The TARDIS does not have enough Artron Energy to hide!");
                                return false;
                            }
//                            // remove torch
//                            plugin.destroyPB.destroyTorch(l);
//                            // remove sign
//                            plugin.destroyPB.destroySign(l, d);
                            // remove blue box
                            plugin.destroyPB.destroyPoliceBox(l, d, id, true, false, false, null);
                            sender.sendMessage(plugin.pluginName + "The TARDIS Police Box was hidden! Use " + ChatColor.GREEN + "/tardis rebuild" + ChatColor.RESET + " to show it again.");
                            qf.alterEnergyLevel("tardis", -hide, wheret, player);
                            // set hidden to true
                            HashMap<String, Object> whereh = new HashMap<String, Object>();
                            whereh.put("tardis_id", id);
                            HashMap<String, Object> seth = new HashMap<String, Object>();
                            seth.put("hidden", 1);
                            qf.doUpdate("tardis", seth, whereh);
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
                            sender.sendMessage(plugin.pluginName + TARDISConstants.NO_TARDIS);
                            return false;
                        }
                        if (args.length < 2 || (!args[1].equalsIgnoreCase("saves") && !args[1].equalsIgnoreCase("companions") && !args[1].equalsIgnoreCase("areas") && !args[1].equalsIgnoreCase("rechargers"))) {
                            sender.sendMessage(plugin.pluginName + "You need to specify which TARDIS list you want to view! [saves|companions|areas|rechargers]");
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
                            sender.sendMessage(plugin.pluginName + TARDISConstants.NO_TARDIS);
                            return false;
                        }
                        String loc = rs.getCurrent();
                        String[] findData = loc.split(":");
                        sender.sendMessage(plugin.pluginName + "TARDIS was left at " + findData[0] + " at x: " + findData[1] + " y: " + findData[2] + " z: " + findData[3]);
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
                            sender.sendMessage(plugin.pluginName + TARDISConstants.NO_TARDIS);
                            return false;
                        } else {
                            id = rs.getTardis_id();
                            comps = rs.getCompanions();
                        }
                        if (args.length < 2) {
                            sender.sendMessage(plugin.pluginName + "Too few command arguments!");
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
                            if (comps != null && !comps.isEmpty()) {
                                // add to the list
                                String newList = comps + ":" + args[1].toLowerCase(Locale.ENGLISH);
                                set.put("companions", newList);
                            } else {
                                // make a list
                                set.put("companions", args[1].toLowerCase(Locale.ENGLISH));
                            }
                            qf.doUpdate("tardis", set, tid);
                            player.sendMessage(plugin.pluginName + "You added " + ChatColor.GREEN + args[1] + ChatColor.RESET + " as a TARDIS companion.");
                            // are we doing an achievement?
                            if (plugin.getAchivementConfig().getBoolean("friends.enabled")) {
                                TARDISAchievementFactory taf = new TARDISAchievementFactory(plugin, player, "friends", 1);
                                taf.doAchievement(1);
                            }
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
                            sender.sendMessage(plugin.pluginName + TARDISConstants.NO_TARDIS);
                            return false;
                        } else {
                            id = rs.getTardis_id();
                            comps = rs.getCompanions();
                            if (comps == null || comps.isEmpty()) {
                                sender.sendMessage(plugin.pluginName + "You have not added any TARDIS companions yet!");
                                return true;
                            }
                        }
                        if (args.length < 2) {
                            sender.sendMessage(plugin.pluginName + "Too few command arguments!");
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
                                    if (!c.equals(args[1].toLowerCase(Locale.ENGLISH))) {
                                        // add to new string
                                        buf.append(c).append(":");
                                    }
                                }
                                // remove trailing colon
                                newList = buf.toString().substring(0, buf.length() - 1);
                            } else {
                                newList = "";
                            }
                            QueryFactory qf = new QueryFactory(plugin);
                            HashMap<String, Object> tid = new HashMap<String, Object>();
                            HashMap<String, Object> set = new HashMap<String, Object>();
                            tid.put("tardis_id", id);
                            set.put("companions", newList);
                            qf.doUpdate("tardis", set, tid);
                            player.sendMessage(plugin.pluginName + "You removed " + ChatColor.GREEN + args[1] + ChatColor.RESET + " as a TARDIS companion.");
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
                            sender.sendMessage(plugin.pluginName + TARDISConstants.NO_TARDIS);
                            return false;
                        }
                        if (args.length < 2) {
                            sender.sendMessage(plugin.pluginName + "Too few command arguments!");
                            return false;
                        }
                        if (!args[1].matches("[A-Za-z0-9_]{2,16}")) {
                            sender.sendMessage(plugin.pluginName + "That doesn't appear to be a valid save name (it may be too long or contains spaces).");
                            return false;
                        } else {
                            int id = rs.getTardis_id();
                            // get current destination
                            String cur = rs.getCurrent();
                            String[] curDest = cur.split(":");
                            QueryFactory qf = new QueryFactory(plugin);
                            HashMap<String, Object> set = new HashMap<String, Object>();
                            set.put("tardis_id", id);
                            set.put("dest_name", args[1]);
                            set.put("world", curDest[0]);
                            set.put("x", plugin.utils.parseNum(curDest[1]));
                            set.put("y", plugin.utils.parseNum(curDest[2]));
                            set.put("z", plugin.utils.parseNum(curDest[3]));
                            if (qf.doInsert("destinations", set) < 0) {
                                return false;
                            } else {
                                sender.sendMessage(plugin.pluginName + "The location '" + args[1] + "' was saved successfully.");
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
                            sender.sendMessage(plugin.pluginName + "Too few command arguments!");
                            return false;
                        }
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("owner", player.getName());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (!rs.resultSet()) {
                            sender.sendMessage(plugin.pluginName + TARDISConstants.NO_TARDIS);
                            return false;
                        }
                        int id = rs.getTardis_id();
                        HashMap<String, Object> whered = new HashMap<String, Object>();
                        whered.put("dest_name", args[1]);
                        whered.put("tardis_id", id);
                        ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
                        if (!rsd.resultSet()) {
                            sender.sendMessage(plugin.pluginName + "Could not find a saved destination with that name!");
                            return false;
                        }
                        int destID = rsd.getDest_id();
                        QueryFactory qf = new QueryFactory(plugin);
                        HashMap<String, Object> did = new HashMap<String, Object>();
                        did.put("dest_id", destID);
                        qf.doDelete("destinations", did);
                        sender.sendMessage(plugin.pluginName + "The destination " + args[1] + " was deleted!");
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
                            sender.sendMessage(plugin.pluginName + TARDISConstants.NO_TARDIS);
                            return false;
                        }
                        if (args.length < 2) {
                            sender.sendMessage(plugin.pluginName + "Too few command arguments!");
                            return false;
                        }
                        if (!args[1].matches("[A-Za-z0-9_]{2,16}")) {
                            sender.sendMessage(plugin.pluginName + "The destination name must be between 2 and 16 characters and have no spaces!");
                            return false;
                        } else if (args[1].equalsIgnoreCase("hide") || args[1].equalsIgnoreCase("rebuild") || args[1].equalsIgnoreCase("home")) {
                            sender.sendMessage(plugin.pluginName + "That is a reserved destination name!");
                            return false;
                        } else {
                            int id = rs.getTardis_id();
                            // check they are not in the tardis
                            HashMap<String, Object> wherettrav = new HashMap<String, Object>();
                            wherettrav.put("player", player.getName());
                            wherettrav.put("tardis_id", id);
                            ResultSetTravellers rst = new ResultSetTravellers(plugin, wherettrav, false);
                            if (rst.resultSet()) {
                                player.sendMessage(plugin.pluginName + "You cannot bring the Police Box here because you are inside a TARDIS!");
                                return true;
                            }
                            // get location player is looking at
                            Block b = player.getTargetBlock(transparent, 50);
                            Location l = b.getLocation();
                            if (!plugin.ta.areaCheckInExisting(l)) {
                                sender.sendMessage(plugin.pluginName + "You cannot use /tardis setdest in a TARDIS area! Please use " + ChatColor.AQUA + "/tardistravel area [area name]");
                                return true;
                            }
                            String world = l.getWorld().getName();
                            if (!plugin.getConfig().getBoolean("include_default_world") && plugin.getConfig().getBoolean("default_world") && world.equals(plugin.getConfig().getString("default_world_name"))) {
                                sender.sendMessage(plugin.pluginName + "The server admin will not allow you to set the TARDIS destination to this world!");
                                return true;
                            }
                            // check the world is not excluded
                            if (!plugin.getConfig().getBoolean("worlds." + world)) {
                                sender.sendMessage(plugin.pluginName + "You cannot bring the TARDIS Police Box to this world");
                                return true;
                            }
                            respect = new TARDISPluginRespect(plugin);
                            if (!respect.getRespect(player, l, true)) {
                                return true;
                            }
                            if (player.hasPermission("tardis.exile") && plugin.getConfig().getBoolean("exile")) {
                                String areaPerm = plugin.ta.getExileArea(player);
                                if (plugin.ta.areaCheckInExile(areaPerm, l)) {
                                    sender.sendMessage(plugin.pluginName + "You exile status does not allow you to save the TARDIS to this location!");
                                    return false;
                                }
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
                            if (qf.doInsert("destinations", set) < 0) {
                                return false;
                            } else {
                                sender.sendMessage(plugin.pluginName + "The destination '" + args[1] + "' was saved successfully.");
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
                            sender.sendMessage(plugin.pluginName + "You need to specify the compass direction e.g. north, west, south or east!");
                            return false;
                        }
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("owner", player.getName());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (!rs.resultSet()) {
                            sender.sendMessage(plugin.pluginName + TARDISConstants.NO_TARDIS);
                            return false;
                        }
                        int id = rs.getTardis_id();
                        int level = rs.getArtron_level();
                        int amount = plugin.getArtronConfig().getInt("random");
                        if (level < amount) {
                            sender.sendMessage(plugin.pluginName + "The TARDIS does not have enough Artron Energy to change the Police Box direction!");
                            return true;
                        }
                        String save = rs.getCurrent();
                        String[] save_data = save.split(":");
                        if (plugin.tardisMaterialising.contains(id)) {
                            sender.sendMessage(plugin.pluginName + "You cannot do that while the TARDIS is materialising!");
                            return true;
                        }
                        boolean cham = false;
                        if (plugin.getConfig().getBoolean("chameleon")) {
                            cham = rs.isChamele_on();
                        }
                        String dir = args[1].toUpperCase(Locale.ENGLISH);
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
                        // destroy platform
                        if (!rs.isHidden()) {
                            plugin.destroyPB.destroyPlatform(rs.getPlatform(), id);
                            plugin.destroyPB.destroySign(l, old_d);
                        }
                        plugin.buildPB.buildPoliceBox(id, l, d, cham, player, true, false);
                        HashMap<String, Object> wherea = new HashMap<String, Object>();
                        wherea.put("tardis_id", id);
                        qf.alterEnergyLevel("tardis", -amount, wherea, player);
                        sender.sendMessage(plugin.pluginName + "You used " + amount + " Artron Energy changing the Police Box direction.");
                        return true;
                    } else {
                        sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("namekey")) {
                    if (bukkitversion.compareTo(preIMversion) < 0 || (bukkitversion.compareTo(preIMversion) == 0 && SUBversion.compareTo(preSUBversion) < 0)) {
                        sender.sendMessage(plugin.pluginName + "You cannot rename the TARDIS key with this version of Bukkit!");
                        return true;
                    }
                    // determine key item
                    String key;
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("player", player.getName());
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, where);
                    if (rsp.resultSet()) {
                        key = (!rsp.getKey().isEmpty()) ? rsp.getKey() : plugin.getConfig().getString("key");
                    } else {
                        key = plugin.getConfig().getString("key");
                    }
                    Material m = Material.getMaterial(key);
                    if (m.equals(Material.AIR)) {
                        sender.sendMessage(plugin.pluginName + "You cannot rename AIR!");
                        return true;
                    }
                    ItemStack is = player.getItemInHand();
                    if (!is.getType().equals(m)) {
                        sender.sendMessage(plugin.pluginName + "You can only rename the TARDIS key!");
                        return true;
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
                    if (!tmp.isEmpty()) {
                        TARDISItemRenamer ir = new TARDISItemRenamer(is);
                        ir.setName(tmp, false);
                        sender.sendMessage(plugin.pluginName + "TARDIS key renamed to '" + tmp + "'");
                        return true;
                    } else {
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("help")) {
                    if (args.length == 1) {
                        sender.sendMessage(TARDISConstants.COMMANDS.split("\n"));
                        return true;
                    }
                    if (args.length == 2) {
                        List<String> cmds = new ArrayList<String>();
                        for (TARDISConstants.CMDS c : TARDISConstants.CMDS.values()) {
                            cmds.add(c.toString());
                        }
                        // check that the second arument is valid
                        if (!cmds.contains(args[1].toUpperCase(Locale.ENGLISH))) {
                            sender.sendMessage(plugin.pluginName + "That is not a valid help topic!");
                            return true;
                        }
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
                            case VERSION:
                                sender.sendMessage(TARDISConstants.COMMAND_HIDE.split("\n"));
                                break;
                            case ADMIN:
                                sender.sendMessage(TARDISConstants.COMMAND_ADMIN.split("\n"));
                                break;
                            case AREA:
                                sender.sendMessage(TARDISConstants.COMMAND_AREA.split("\n"));
                                break;
                            case ROOM:
                                sender.sendMessage(TARDISConstants.COMMAND_ROOM.split("\n"));
                                break;
                            case ARTRON:
                                sender.sendMessage(TARDISConstants.COMMAND_ARTRON.split("\n"));
                                break;
                            case BIND:
                                sender.sendMessage(TARDISConstants.COMMAND_BIND.split("\n"));
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
