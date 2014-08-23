/*
 * Copyright (C) 2014 eccentric_nz
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
import me.eccentric_nz.TARDIS.files.TARDISRoomMap;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls.Pair;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
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
                TARDISMessage.send(sender, "TOO_FEW_ARGS");
                return false;
            }
            if (args[0].toLowerCase(Locale.ENGLISH).equals("blocks")) {
                String name = args[1].toUpperCase(Locale.ENGLISH);
                Set<String> rooms = plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false);
                if (name.equals("SAVE")) {
                    if (!sender.hasPermission("tardis.admin")) {
                        TARDISMessage.send(sender, "NO_PERMS");
                        return false;
                    }
                    for (String r : rooms) {
                        HashMap<String, Integer> blockIDs = plugin.getBuildKeeper().getRoomBlockCounts().get(r);
                        String file = plugin.getDataFolder() + File.separator + r + "_block_list.txt";
                        int cost = 0;
                        try {
                            BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
                            for (Map.Entry<String, Integer> entry : blockIDs.entrySet()) {
                                String[] data = entry.getKey().split(":");
                                int bid = plugin.getUtils().parseInt(data[0]);
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
                        TARDISMessage.send(sender, "ROOM_FILE_SAVED", r);
                    }
                    return true;
                } else {
                    if (!rooms.contains(name)) {
                        TARDISMessage.send(sender, "COULD_NOT_FIND_ROOM");
                        return true;
                    }
                    HashMap<String, Integer> blockIDs = plugin.getBuildKeeper().getRoomBlockCounts().get(name);
                    boolean hasPrefs = false;
                    String wall = "ORANGE WOOL";
                    String floor = "LIGHT GREY WOOL";
                    if (sender instanceof Player) {
                        HashMap<String, Object> wherepp = new HashMap<String, Object>();
                        wherepp.put("uuid", ((Player) sender).getUniqueId().toString());
                        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
                        if (rsp.resultSet()) {
                            hasPrefs = true;
                            wall = rsp.getWall();
                            floor = rsp.getFloor();
                        }
                    }
                    TARDISMessage.send(sender, "ROOM_BLOCKS", name);
                    for (Map.Entry<String, Integer> entry : blockIDs.entrySet()) {
                        String[] block_data = entry.getKey().split(":");
                        int bid = plugin.getUtils().parseInt(block_data[0]);
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
                    TARDISMessage.send(sender, "CMD_NO_CONSOLE");
                    return true;
                }
                String name = args[1].toUpperCase(Locale.ENGLISH);
                Set<String> rooms = plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false);
                if (!rooms.contains(name)) {
                    TARDISMessage.send(player, "COULD_NOT_FIND_ROOM");
                    return true;
                }
                HashMap<String, Integer> blockTypes = plugin.getBuildKeeper().getRoomBlockCounts().get(name);
                boolean hasPrefs = false;
                String wall = "ORANGE_WOOL";
                String floor = "LIGHT_GREY_WOOL";
                HashMap<String, Object> wherepp = new HashMap<String, Object>();
                wherepp.put("uuid", ((Player) sender).getUniqueId().toString());
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
                if (rsp.resultSet()) {
                    hasPrefs = true;
                    wall = rsp.getWall();
                    floor = rsp.getFloor();
                }
                // get the TARDIS id
                HashMap<String, Object> wheret = new HashMap<String, Object>();
                wheret.put("uuid", player.getUniqueId().toString());
                ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false);
                if (rs.resultSet()) {
                    TARDISMessage.send(player, "CONDENSE_REQUIRE", name);
                    HashMap<String, Integer> item_counts = new HashMap<String, Integer>();
                    for (Map.Entry<String, Integer> entry : blockTypes.entrySet()) {
                        String[] block_data = entry.getKey().split(":");
                        String bid = block_data[0];
                        String mat;
                        String bkey;
                        if (hasPrefs && block_data.length == 2 && (block_data[1].equals("1") || block_data[1].equals("8"))) {
                            mat = (block_data[1].equals("1")) ? wall : floor;
                            Pair iddata = plugin.getTardisWalls().blocks.get(mat);
                            bkey = iddata.getType().toString();
                        } else {
                            bkey = bid;
                        }
                        int tmp = Math.round((entry.getValue() / 100.0F) * plugin.getConfig().getInt("growth.rooms_condenser_percent"));
                        int amount = (tmp > 0) ? tmp : 1;
                        if (item_counts.containsKey(bkey)) {
                            item_counts.put(bkey, item_counts.get(bkey) + amount);
                        } else {
                            item_counts.put(bkey, amount);
                        }
                    }
                    int total = 0;
                    for (Map.Entry<String, Integer> map : item_counts.entrySet()) {
                        // get the amount of this block that the player has condensed
                        HashMap<String, Object> wherec = new HashMap<String, Object>();
                        wherec.put("tardis_id", rs.getTardis_id());
                        wherec.put("block_data", map.getKey());
                        ResultSetCondenser rsc = new ResultSetCondenser(plugin, wherec, false);
                        int has = (rsc.resultSet()) ? rsc.getBlock_count() : 0;
                        int required = map.getValue() - has;
                        if (required > 0) {
                            String line = map.getKey() + ", " + required;
                            player.sendMessage(line);
                            total += required;
                        }
                    }
                    if (total == 0) {
                        TARDISMessage.send(player, "CONDENSE_NONE");
                    }
                    return true;
                } else {
                    TARDISMessage.send(player, "ID_NOT_FOUND");
                    return true;
                }
            } else if (args[0].toLowerCase(Locale.ENGLISH).equals("add")) {
                if (!sender.hasPermission("tardis.admin")) {
                    TARDISMessage.send(sender, "NO_PERMS");
                    return false;
                }
                Pattern regex = Pattern.compile(".*[A-Z].*");
                if (regex.matcher(args[1]).matches()) {
                    TARDISMessage.send(sender, "ARG_LOWERCASE");
                    return true;
                }
                String name = args[1].toUpperCase(Locale.ENGLISH);
                if (name.equals("ADD") || name.equals("BLOCKS")) {
                    TARDISMessage.send(sender, "ROOM_NO_NAME", args[1]);
                    return false;
                }
                if (plugin.getRoomsConfig().contains("rooms." + name)) {
                    TARDISMessage.send(sender, "ROOM_EXISTS");
                    return true;
                }
                String lower = name.toLowerCase(Locale.ENGLISH);
                String filepath = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator + lower + ".tschm";
                File file = new File(filepath);
                if (!file.exists()) {
                    TARDISMessage.send(sender, "ROOM_SCHEMATIC_INFO" + lower);
                    return true;
                }
                String basepath = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator;
                new TARDISRoomMap(plugin).makeRoomMap(basepath + lower, name);
                plugin.getRoomsConfig().set("rooms." + name + ".enabled", false);
                plugin.getRoomsConfig().set("rooms." + name + ".user", true);
                try {
                    plugin.getRoomsConfig().save(new File(plugin.getDataFolder(), "rooms.yml"));
                } catch (IOException io) {
                    plugin.debug("Could not save rooms.yml, " + io);
                }
                TARDISMessage.send(sender, "ROOM_ADDED");
                return true;
            } else {
                if (!sender.hasPermission("tardis.admin")) {
                    TARDISMessage.send(sender, "NO_PERMS");
                    return false;
                }
                // check they have specified a valid room
                String name = args[0].toUpperCase(Locale.ENGLISH);
                if (!plugin.getRoomsConfig().contains("rooms." + name)) {
                    TARDISMessage.send(sender, "COULD_NOT_FIND_ROOM");
                    return false;
                }
                String option = args[1].toLowerCase(Locale.ENGLISH);
                if (option.equals("true") || option.equals("false")) {
                    // boolean enable/disable
                    // check that the other options have been set first
                    if (!plugin.getRoomsConfig().contains("rooms." + name + ".cost") || !plugin.getRoomsConfig().contains("rooms." + name + ".seed") || !plugin.getRoomsConfig().contains("rooms." + name + ".offset")) {
                        TARDISMessage.send(sender, "ROOM_NO_ENABLE");
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
                        plugin.getGeneralKeeper().getRoomArgs().add(name);
                    } else {
                        plugin.getGeneralKeeper().getRoomArgs().remove(name);
                    }
                    TARDISMessage.send(sender, "ROOM_TRUE_FALSE", name, option);
                    return true;
                } else {
                    // cost, offset or seed?
                    try {
                        // cost
                        int num = Integer.parseInt(args[1]);
                        if (num > 0) {
                            plugin.getRoomsConfig().set("rooms." + name + ".cost", num);
                            TARDISMessage.send(sender, "ROOM_COST", name, String.format("%d", num));
                        } else {
                            plugin.getRoomsConfig().set("rooms." + name + ".offset", num);
                            TARDISMessage.send(sender, "ROOM_OFFSET", name, String.format("%d", num));
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
                            TARDISMessage.send(sender, "MATERIAL_NOT_VALID");
                            return false;
                        }
                        // check seed material is not already in use
                        for (String m : plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false)) {
                            if (setMaterial.equalsIgnoreCase(plugin.getRoomsConfig().getString("rooms." + m + ".seed"))) {
                                TARDISMessage.send(sender, "ROOM_SEED_EXISTS");
                                return true;
                            }
                        }
                        plugin.getRoomsConfig().set("rooms." + name + ".seed", setMaterial);
                        TARDISMessage.send(sender, "ROOM_SEED_SET", name, setMaterial);
                        try {
                            plugin.getRoomsConfig().save(new File(plugin.getDataFolder(), "rooms.yml"));
                        } catch (IOException io) {
                            plugin.debug("Could not save rooms.yml, " + io);
                        }
                        // add the seed block to plugin.getBuildKeeper().getSeeds()
                        Material m = Material.valueOf(setMaterial);
                        plugin.getBuildKeeper().getSeeds().put(m, name);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
