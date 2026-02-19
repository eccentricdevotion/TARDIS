package me.eccentric_nz.TARDIS.commands;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.files.TARDISRoomMap;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class RoomsUtility {

    public static void add(TARDIS plugin, CommandSender sender, String name) {
        Pattern regex = Pattern.compile(".*[A-Z].*");
        if (regex.matcher(name).matches()) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_LOWERCASE");
            return;
        }
        if (name.equals("ADD") || name.equals("BLOCKS")) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ROOM_NO_NAME", name);
            return;
        }
        if (plugin.getRoomsConfig().contains("rooms." + name)) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ROOM_EXISTS");
            return;
        }
        String lower = name.toLowerCase(Locale.ROOT);
        String filepath = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator + lower + ".tschm";
        File file = new File(filepath);
        if (!file.exists()) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ROOM_SCHEMATIC_INFO", lower);
            return;
        }
        boolean success = new TARDISRoomMap(plugin).makeRoomMap(lower, name, true);
        if (!success) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ROOM_FAILED");
            return;
        }
        plugin.getRoomsConfig().set("rooms." + name + ".enabled", false);
        plugin.getRoomsConfig().set("rooms." + name + ".user", true);
        try {
            plugin.getRoomsConfig().save(new File(plugin.getDataFolder(), "rooms.yml"));
        } catch (IOException io) {
            plugin.debug("Could not save rooms.yml, " + io);
        }
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "ROOM_ADDED");
    }

    public static void listBlocks(TARDIS plugin, CommandSender sender, String name) {
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
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "ROOM_BLOCKS", name);
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

    public static void saveBlocks(TARDIS plugin, CommandSender sender) {
        plugin.getRoomsConfig().getConfigurationSection("rooms")
                .getKeys(false)
                .forEach((r) -> {
                    if (plugin.getRoomsConfig().getBoolean("rooms." + r + ".enabled")) {
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
                                int artron_cost = Math.round(cost / 2.0F);
                                float tmp_cost = artron_cost / 10.0f;
                                int config_cost = (((int) tmp_cost) / 25) * 25;
//                                        int offset = plugin.getRoomsConfig().getInt("rooms." + r + ".offset");
//                                        String material = plugin.getRoomsConfig().getString("rooms." + r + ".seed");
//                                        plugin.debug("| " + r + " | " + config_cost + " | " + offset + " | " + material + " |");
//                                        plugin.debug("| " + TARDISStringUtils.capitalise(r) + " | " + artron_cost + " | " + config_cost + " |");
//                                        plugin.debug("integerOptions.put(\"rooms." + r + ".cost\", " + config_cost + ");");
//                                        plugin.debug("integerOptions.put(\"rooms." + r + ".offset\", " + offset + ");");
                                bw.write("Actual room cost: " + artron_cost);
                                bw.newLine();
                                bw.write("Config room cost: " + config_cost);
                                bw.newLine();
                            }
                        } catch (IOException e) {
                            plugin.debug("Could not create and write to " + r + "_block_list.txt! " + e.getMessage());
                        }
                        plugin.getMessenger().send(sender, TardisModule.TARDIS, "ROOM_FILE_SAVED", r);
                    }
                });
    }

    public static void setRoomEnabled(TARDIS plugin, CommandSender sender, String room, boolean bool) {
        plugin.getRoomsConfig().set("rooms." + room + ".enabled", bool);
        try {
            plugin.getRoomsConfig().save(new File(plugin.getDataFolder(), "rooms.yml"));
            // also add or remove the room from the TARDISCommands.roomArgs List
            if (bool) {
                plugin.getGeneralKeeper().getRoomArgs().add(room);
            } else {
                plugin.getGeneralKeeper().getRoomArgs().remove(room);
            }
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ROOM_TRUE_FALSE", room, Boolean.toString(bool));
        } catch (IOException io) {
            plugin.debug("Could not save rooms.yml, " + io);
        }
    }

    public static void setRoomSeed(TARDIS plugin, CommandSender sender, String room, String material) {
        try {
            Material.valueOf(material);
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "MATERIAL_NOT_VALID");
            return;
        }
        // check seed material is not already in use
        for (String m : plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false)) {
            if (material.equalsIgnoreCase(plugin.getRoomsConfig().getString("rooms." + m + ".seed"))) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "ROOM_SEED_EXISTS");
                return;
            }
        }
        plugin.getRoomsConfig().set("rooms." + room + ".seed", material);
        try {
            plugin.getRoomsConfig().save(new File(plugin.getDataFolder(), "rooms.yml"));
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ROOM_SEED_SET", room, material);
        } catch (IOException io) {
            plugin.debug("Could not save rooms.yml, " + io);
        }
        // add the seed block to plugin.getBuildKeeper().getSeeds()
        Material m = Material.valueOf(material);
        plugin.getBuildKeeper().getRoomSeeds().put(m, room);
    }
}
