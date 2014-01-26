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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetCondenser;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.files.TARDISMakeRoomCSV;
import me.eccentric_nz.TARDIS.files.TARDISRoomSchematicReader;
import me.eccentric_nz.TARDIS.files.TARDISSchematic;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The Sub-Wave Network was a piece of sentient software programmed to find
 * anyone who could help to contact the Tenth Doctor. It used sub-wave
 * communication to transmit, which meant it was undetectable as it was below
 * normal waves. It was created by the Mr Copper Foundation and further
 * developed by Great Britain's former Prime Minister Harriet Jones.
 *
 * @author eccentric_nz
 */
public class TARDISRoomCommands implements CommandExecutor {

    private final TARDIS plugin;
    public List<String> roomArgs = new ArrayList<String>();

    public TARDISRoomCommands(TARDIS plugin) {
        this.plugin = plugin;
        // rooms - only add if enabled in the config
        for (String r : plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false)) {
            if (plugin.getRoomsConfig().getBoolean("rooms." + r + ".enabled")) {
                roomArgs.add(r);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisroom")) {
            if (args.length < 2) {
                sender.sendMessage(plugin.pluginName + "Too few command arguments!");
                return false;
            }
            if (args[0].toLowerCase(Locale.ENGLISH).equals("blocks")) {
                String name = args[1].toUpperCase(Locale.ENGLISH);
                Set<String> rooms = plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false);
                if (name.equals("SAVE")) {
                    if (!sender.hasPermission("tardis.admin")) {
                        sender.sendMessage(plugin.pluginName + MESSAGE.NO_PERMS.getText());
                        return false;
                    }
                    for (String r : rooms) {
                        HashMap<String, Integer> blockIDs = plugin.roomBlockCounts.get(r);
                        String file = plugin.getDataFolder() + File.separator + r + "_block_list.txt";
                        int cost = 0;
                        try {
                            BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
                            for (Map.Entry<String, Integer> entry : blockIDs.entrySet()) {
                                String[] data = entry.getKey().split(":");
                                int bid = plugin.utils.parseInt(data[0]);
                                String mat = Material.getMaterial(bid).toString();
                                String line = mat + " (" + entry.getKey() + "), " + entry.getValue();
                                bw.write(line);
                                bw.newLine();
                                if (plugin.getCondensables().containsKey(mat)) {
                                    int value = entry.getValue() * plugin.getCondensables().get(mat);
                                    cost += value;
                                }
                            }
                            bw.write("Actual room cost: " + Math.round(cost / 2.0F));
                            bw.newLine();
                            bw.close();
                        } catch (IOException e) {
                            plugin.debug("Could not create and write to " + r + "_block_list.txt! " + e.getMessage());
                        }
                        sender.sendMessage(plugin.pluginName + "File saved to 'plugins/TARDIS/" + r + "_block_list.txt'");
                    }
                    return true;
                } else {
                    if (!rooms.contains(name)) {
                        sender.sendMessage(plugin.pluginName + "Could not find a room with that name");
                        return true;
                    }
                    HashMap<String, Integer> blockIDs = plugin.roomBlockCounts.get(name);
                    boolean hasPrefs = false;
                    String wall = "ORANGE WOOL";
                    String floor = "LIGHT GREY WOOL";
                    if (sender instanceof Player) {
                        HashMap<String, Object> wherepp = new HashMap<String, Object>();
                        wherepp.put("player", ((Player) sender).getName());
                        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
                        if (rsp.resultSet()) {
                            hasPrefs = true;
                            wall = rsp.getWall();
                            floor = rsp.getFloor();
                        }
                    }
                    sender.sendMessage(plugin.pluginName + name + " blocks:");
                    for (Map.Entry<String, Integer> entry : blockIDs.entrySet()) {
                        String[] block_data = entry.getKey().split(":");
                        int bid = plugin.utils.parseInt(block_data[0]);
                        String mat;
                        if (hasPrefs && block_data.length == 2 && (block_data[1].equals("1") || block_data[1].equals("8"))) {
                            mat = (block_data[1].equals("1")) ? wall : floor;
                        } else {
                            mat = Material.getMaterial(bid).toString();
                        }
                        int tmp = Math.round((entry.getValue() / 100.0F) * plugin.getConfig().getInt("growth.rooms_condenser_percent"));
                        int amount = (tmp > 0) ? tmp : 1;
                        String line = mat + ", " + amount;
                        sender.sendMessage(line);
                    }
                    return true;
                }
            } else if (args[0].toLowerCase(Locale.ENGLISH).equals("required")) {
                Player player = null;
                if (sender instanceof Player) {
                    player = (Player) sender;
                }
                if (player == null) {
                    sender.sendMessage(plugin.pluginName + "You cannot run this command from the console!");
                    return true;
                }
                String name = args[1].toUpperCase(Locale.ENGLISH);
                Set<String> rooms = plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false);
                if (!rooms.contains(name)) {
                    sender.sendMessage(plugin.pluginName + "Could not find a room with that name!");
                    return true;
                }
                HashMap<String, Integer> blockIDs = plugin.roomBlockCounts.get(name);
                boolean hasPrefs = false;
                String wall = "ORANGE WOOL";
                String floor = "LIGHT GREY WOOL";

                HashMap<String, Object> wherepp = new HashMap<String, Object>();
                wherepp.put("player", ((Player) sender).getName());
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
                if (rsp.resultSet()) {
                    hasPrefs = true;
                    wall = rsp.getWall();
                    floor = rsp.getFloor();
                }
                // get the TARDIS id
                HashMap<String, Object> wheret = new HashMap<String, Object>();
                wheret.put("owner", player.getName());
                ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false);
                if (rs.resultSet()) {
                    sender.sendMessage(plugin.pluginName + "You need to condense the following blocks to grow a " + name + ":");
                    for (Map.Entry<String, Integer> entry : blockIDs.entrySet()) {
                        String[] block_data = entry.getKey().split(":");
                        int bid = plugin.utils.parseInt(block_data[0]);
                        String mat;
                        if (hasPrefs && block_data.length == 2 && (block_data[1].equals("1") || block_data[1].equals("8"))) {
                            mat = (block_data[1].equals("1")) ? wall : floor;
                        } else {
                            mat = Material.getMaterial(bid).toString();
                        }
                        int tmp = Math.round((entry.getValue() / 100.0F) * plugin.getConfig().getInt("growth.rooms_condenser_percent"));
                        int amount = (tmp > 0) ? tmp : 1;
                        // get the amount of this block that the player has condensed
                        HashMap<String, Object> wherec = new HashMap<String, Object>();
                        wherec.put("tardis_id", rs.getTardis_id());
                        wherec.put("block_data", bid);
                        ResultSetCondenser rsc = new ResultSetCondenser(plugin, wherec, false);
                        int has = (rsc.resultSet()) ? rsc.getBlock_count() : 0;
                        int required = amount - has;
                        if (required > 0) {
                            String line = mat + ", " + required;
                            sender.sendMessage(line);
                        }
                    }
                    return true;
                } else {
                    sender.sendMessage(plugin.pluginName + "Could not get TARDIS id!");
                    return true;
                }
            } else if (args[0].toLowerCase(Locale.ENGLISH).equals("add")) {
                if (!sender.hasPermission("tardis.admin")) {
                    sender.sendMessage(plugin.pluginName + MESSAGE.NO_PERMS.getText());
                    return false;
                }
                Pattern regex = Pattern.compile(".*[A-Z].*");
                if (regex.matcher(args[1]).matches()) {
                    sender.sendMessage(plugin.pluginName + "Please make sure the file name is lowercase only!");
                    return true;
                }
                String name = args[1].toUpperCase(Locale.ENGLISH);
                if (name.equals("ADD") || name.equals("BLOCKS")) {
                    sender.sendMessage(plugin.pluginName + "You cannot call your room '" + args[1] + "'!");
                    return false;
                }
                if (plugin.getRoomsConfig().contains("rooms." + name)) {
                    sender.sendMessage(plugin.pluginName + "That room name already exists!");
                    return true;
                }
                String lower = name.toLowerCase(Locale.ENGLISH);
                String filepath = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator + lower + ".schematic";
                File file = new File(filepath);
                if (!file.exists()) {
                    sender.sendMessage(plugin.pluginName + "You need to put the " + lower + ".schematic into the TARDIS user_schematics directory!");
                    return true;
                }
                TARDISMakeRoomCSV mrc = new TARDISMakeRoomCSV(plugin);
                TARDISRoomSchematicReader reader = new TARDISRoomSchematicReader(plugin);
                String basepath = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator;
                File csvfile = mrc.createFile(lower + ".csv", basepath);
                boolean square = reader.readAndMakeRoomCSV(basepath + lower, name, false);
                if (!square) {
                    sender.sendMessage(plugin.pluginName + "The schematic needs to have equal length sides!");
                    return true;
                }
                short[] dimensions = plugin.room_dimensions.get(name);
                String[][][] schem = TARDISSchematic.schematic(csvfile, dimensions[0], dimensions[1], dimensions[2]);
                plugin.room_schematics.put(name, schem);
                plugin.getRoomsConfig().set("rooms." + name + ".enabled", false);
                plugin.getRoomsConfig().set("rooms." + name + ".user", true);
                try {
                    plugin.getRoomsConfig().save(new File(plugin.getDataFolder(), "rooms.yml"));
                } catch (IOException io) {
                    plugin.debug("Could not save rooms.yml, " + io);
                }
                sender.sendMessage(plugin.pluginName + "Room added, please set the COST, SEED BLOCK and OFFSET, and then enable it!");
                return true;
            } else {
                // check they have specified a valid room
                String name = args[0].toUpperCase(Locale.ENGLISH);
                if (!plugin.getRoomsConfig().contains("rooms." + name)) {
                    sender.sendMessage(plugin.pluginName + "Could not find a room by that name!");
                    return false;
                }
                String option = args[1].toLowerCase(Locale.ENGLISH);
                if (option.equals("true") || option.equals("false")) {
                    // boolean enable/disable
                    // check that the other options have been set first
                    if (!plugin.getRoomsConfig().contains("rooms." + name + ".cost") || !plugin.getRoomsConfig().contains("rooms." + name + ".seed") || !plugin.getRoomsConfig().contains("rooms." + name + ".offset")) {
                        sender.sendMessage(plugin.pluginName + "You must set the COST, SEED BLOCK and OFFSET before you can enable a room!");
                        return true;
                    }
                    boolean bool = Boolean.valueOf(args[1]);
                    plugin.getRoomsConfig().set("rooms." + name + ".enabled", bool);
                    try {
                        plugin.getRoomsConfig().save(new File(plugin.getDataFolder(), "rooms.yml"));
                    } catch (IOException io) {
                        plugin.debug("Could not save rooms.yml, " + io);
                    }
                    // also add or remove the room from the TARDISCommands.roomArgs List
                    if (bool) {
                        plugin.tardisCommand.roomArgs.add(name);
                    } else {
                        plugin.tardisCommand.roomArgs.remove(name);
                    }
                    sender.sendMessage(plugin.pluginName + name + " was set to " + option + "!");
                    return true;
                } else {
                    // cost, offset or seed?
                    try {
                        // cost
                        int num = Integer.parseInt(args[1]);
                        if (num > 0) {
                            plugin.getRoomsConfig().set("rooms." + name + ".cost", num);
                            sender.sendMessage(plugin.pluginName + "The " + name + " cost was set to " + num + "!");
                        } else {
                            plugin.getRoomsConfig().set("rooms." + name + ".offset", num);
                            sender.sendMessage(plugin.pluginName + "The " + name + " offset was set to " + num + "!");
                        }
                        try {
                            plugin.getRoomsConfig().save(new File(plugin.getDataFolder(), "rooms.yml"));
                        } catch (IOException io) {
                            plugin.debug("Could not save rooms.yml, " + io);
                        }
                        return true;
                    } catch (NumberFormatException nfe) {
                        // string seed
                        String setMaterial = args[1].toUpperCase(Locale.ENGLISH);
                        try {
                            Material go = Material.valueOf(setMaterial);
                        } catch (IllegalArgumentException e) {
                            sender.sendMessage(plugin.pluginName + ChatColor.RED + "That is not a valid Material! Try checking http://jd.bukkit.org/apidocs/org/bukkit/Material.html");
                            return false;
                        }
                        // check seed material is not already in use
                        for (String m : plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false)) {
                            if (setMaterial.equalsIgnoreCase(plugin.getRoomsConfig().getString("rooms." + m + ".seed"))) {
                                sender.sendMessage(plugin.pluginName + "Seed block material is already in use!");
                                return true;
                            }
                        }
                        plugin.getRoomsConfig().set("rooms." + name + ".seed", setMaterial);
                        sender.sendMessage(plugin.pluginName + "The " + name + " seed block was set to " + setMaterial + "!");
                        try {
                            plugin.getRoomsConfig().save(new File(plugin.getDataFolder(), "rooms.yml"));
                        } catch (IOException io) {
                            plugin.debug("Could not save rooms.yml, " + io);
                        }
                        // add the sedd block to plugin.seeds
                        Material m = Material.valueOf(setMaterial);
                        plugin.seeds.put(m, name);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
