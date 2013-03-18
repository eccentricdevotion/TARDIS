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

import java.io.File;
import java.util.Arrays;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.files.TARDISMakeRoomCSV;
import me.eccentric_nz.TARDIS.files.TARDISRoomSchematicReader;
import me.eccentric_nz.TARDIS.files.TARDISSchematic;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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

    public TARDISRoomCommands(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisroom")) {
            if (!sender.hasPermission("tardis.admin")) {
                sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                return false;
            }
            if (args.length < 2) {
                sender.sendMessage(plugin.pluginName + "Too few command arguments!");
                return false;
            }
            if (args[0].toLowerCase(Locale.ENGLISH).equals("add")) {
                String name = args[1].toUpperCase(Locale.ENGLISH);
                if (name.equals("ADD")) {
                    sender.sendMessage(plugin.pluginName + "You cannot call your room 'add'!");
                    return false;
                }
                if (plugin.getConfig().contains("rooms." + name)) {
                    sender.sendMessage(plugin.pluginName + "That room name already exists!");
                    return true;
                }
                String lower = name.toLowerCase(Locale.ENGLISH);
                String filepath = plugin.getDataFolder() + File.separator + "schematics" + File.separator + lower + ".schematic";
                File file = new File(filepath);
                if (!file.exists()) {
                    sender.sendMessage(plugin.pluginName + "You need to put the " + lower + ".schematic into the TARDIS schematics directory!");
                    return true;
                }
                TARDISMakeRoomCSV mrc = new TARDISMakeRoomCSV(plugin);
                TARDISRoomSchematicReader reader = new TARDISRoomSchematicReader(plugin);
                String basepath = plugin.getDataFolder() + File.separator + "schematics" + File.separator;
                File csvfile = mrc.createFile(lower + ".csv");
                boolean square = reader.readAndMakeCSV(basepath + lower, name, false);
                if (!square) {
                    sender.sendMessage(plugin.pluginName + "The schematic needs to have equal length sides!");
                    return true;
                }
                short[] dimensions = plugin.room_dimensions.get(name);
                String[][][] schem = TARDISSchematic.schematic(csvfile, dimensions[0], dimensions[1], dimensions[2]);
                plugin.room_schematics.put(name, schem);
                plugin.getConfig().set("rooms." + name + ".enabled", false);
                plugin.saveConfig();
                sender.sendMessage(plugin.pluginName + "Room added, please set the COST, SEED BLOCK and OFFSET, and then enable it!");
                return true;
            } else {
                // check they have specified a valid room
                String name = args[0].toUpperCase(Locale.ENGLISH);
                if (!plugin.getConfig().contains("rooms." + name)) {
                    sender.sendMessage(plugin.pluginName + "Could not find a room by that name!");
                    return false;
                }
                String option = args[1].toLowerCase(Locale.ENGLISH);
                if (option.equals("true") || option.equals("false")) {
                    // boolean enable/disable
                    // check that the other options have been set first
                    if (!plugin.getConfig().contains("rooms." + name + ".cost") || !plugin.getConfig().contains("rooms." + name + ".seed") || !plugin.getConfig().contains("rooms." + name + ".offset")) {
                        sender.sendMessage(plugin.pluginName + "You must set the COST, SEED BLOCK and OFFSET before you can enable a room!");
                        return true;
                    }
                    boolean bool = Boolean.valueOf(args[1]);
                    plugin.debug(bool);
                    plugin.getConfig().set("rooms." + name + ".enabled", bool);
                    plugin.saveConfig();
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
                            plugin.getConfig().set("rooms." + name + ".cost", num);
                            sender.sendMessage(plugin.pluginName + "The " + name + " cost was set to " + num + "!");
                        } else {
                            plugin.getConfig().set("rooms." + name + ".offset", num);
                            sender.sendMessage(plugin.pluginName + "The " + name + " offset was set to " + num + "!");
                        }
                        plugin.saveConfig();
                        return true;
                    } catch (NumberFormatException nfe) {
                        // string seed
                        String setMaterial = args[1].toUpperCase(Locale.ENGLISH);
                        if (!Arrays.asList(TARDISMaterials.MATERIAL_LIST).contains(setMaterial)) {
                            sender.sendMessage(plugin.pluginName + ChatColor.RED + "That is not a valid Material! Try checking http://jd.bukkit.org/apidocs/org/bukkit/Material.html");
                            return false;
                        }
                        plugin.getConfig().set("rooms." + name + ".seed", setMaterial);
                        sender.sendMessage(plugin.pluginName + "The " + name + " seed block was set to " + setMaterial + "!");
                        plugin.saveConfig();
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
