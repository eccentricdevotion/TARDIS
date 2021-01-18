/*
 * Copyright (C) 2020 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.files.TARDISRoomMap;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.rooms.RoomRequiredLister;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * The Sub-Wave Network was a piece of sentient software programmed to find anyone who could help to contact the Tenth
 * Doctor. It used sub-wave communication to transmit, which meant it was undetectable as it was below normal waves. It
 * was created by the Mr Copper Foundation and further developed by Great Britain's former Prime Minister Harriet
 * Jones.
 *
 * @author eccentric_nz
 */
public class TARDISRoomCommands implements CommandExecutor {

    private final TARDIS plugin;

    public TARDISRoomCommands(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisroom")) {
            if (args.length < 2) {
                new TARDISCommandHelper(plugin).getCommand("tardisroom", sender);
                return true;
            }
            switch (args[0].toLowerCase(Locale.ENGLISH)) {
                case "blocks": {
                    String name = args[1].toUpperCase(Locale.ENGLISH);
                    Set<String> rooms = plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false);
                    if (name.equals("SAVE")) {
                        if (!sender.hasPermission("tardis.admin")) {
                            TARDISMessage.send(sender, "NO_PERMS");
                            return false;
                        }
                        rooms.forEach((r) -> {
                            HashMap<String, Integer> blockIDs = plugin.getBuildKeeper().getRoomBlockCounts().get(r);
                            String file = plugin.getDataFolder() + File.separator + r + "_block_list.txt";
                            int cost = 0;
                            try {
                                try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
                                    for (Map.Entry<String, Integer> entry : blockIDs.entrySet()) {
                                        String line = entry.getKey() + ", " + entry.getValue();
                                        bw.write(line);
                                        bw.newLine();
                                        if (plugin.getCondensables().containsKey(entry.getKey())) {
                                            int value = entry.getValue() * plugin.getCondensables().get(entry.getKey());
                                            cost += value;
                                        }
                                    }
                                    bw.write("Actual room cost: " + Math.round(cost / 2.0F));
                                    bw.newLine();
                                }
                            } catch (IOException e) {
                                plugin.debug("Could not create and write to " + r + "_block_list.txt! " + e.getMessage());
                            }
                            TARDISMessage.send(sender, "ROOM_FILE_SAVED", r);
                        });
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
                            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, ((Player) sender).getUniqueId().toString());
                            if (rsp.resultSet()) {
                                hasPrefs = true;
                                wall = rsp.getWall();
                                floor = rsp.getFloor();
                            }
                        }
                        TARDISMessage.send(sender, "ROOM_BLOCKS", name);
                        for (Map.Entry<String, Integer> entry : blockIDs.entrySet()) {
                            String mat;
                            if (hasPrefs && (entry.getKey().equals("ORANGE_WOOL") || entry.getKey().equals("LIGHT_GRAY_WOOL"))) {
                                mat = (entry.getKey().equals("ORANGE_WOOL")) ? wall : floor;
                            } else {
                                mat = entry.getKey();
                            }
                            int tmp = Math.round((entry.getValue() / 100.0F) * plugin.getConfig().getInt("growth.rooms_condenser_percent"));
                            int amount = (tmp > 0) ? tmp : 1;
                            String line = mat + ", " + amount;
                            sender.sendMessage(line);
                        }
                    }
                    return true;
                }
                case "required": {
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
                    RoomRequiredLister.listCondensables(plugin, name, player);
                    return true;
                }
                case "add": {
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
                        TARDISMessage.send(sender, "ROOM_SCHEMATIC_INFO", lower);
                        return true;
                    }
                    String basepath = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator;
                    boolean success = new TARDISRoomMap(plugin).makeRoomMap(basepath + lower, name);
                    if (!success) {
                        TARDISMessage.send(sender, "ROOM_FAILED");
                        return true;
                    }
                    plugin.getRoomsConfig().set("rooms." + name + ".enabled", false);
                    plugin.getRoomsConfig().set("rooms." + name + ".user", true);
                    try {
                        plugin.getRoomsConfig().save(new File(plugin.getDataFolder(), "rooms.yml"));
                    } catch (IOException io) {
                        plugin.debug("Could not save rooms.yml, " + io);
                    }
                    TARDISMessage.send(sender, "ROOM_ADDED");
                    return true;
                }
                default: {
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
                        boolean bool = Boolean.parseBoolean(args[1]);
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
                                Material.valueOf(setMaterial);
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
        }
        return false;
    }
}
