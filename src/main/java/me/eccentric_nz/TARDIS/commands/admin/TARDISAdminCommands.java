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
package me.eccentric_nz.TARDIS.commands.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CMDS;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

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

    private final TARDIS plugin;
    public HashMap<String, String> firstsStr = new HashMap<String, String>();
    public List<String> firstsStrArtron = new ArrayList<String>();
    public HashMap<String, String> firstsBool = new HashMap<String, String>();
    public HashMap<String, String> firstsInt = new HashMap<String, String>();
    public List<String> firstsIntArtron = new ArrayList<String>();

    public TARDISAdminCommands(TARDIS plugin) {
        this.plugin = plugin;
        // add first arguments
        firstsStr.put("chunks", "");
        firstsStr.put("config", "");
        firstsStr.put("custom_schematic_seed", "creation");
        firstsStr.put("database", "storage");
        firstsStr.put("decharge", "");
        firstsStr.put("default_world_name", "creation");
        firstsStr.put("delete", "");
        firstsStr.put("difficulty", "preferences");
        firstsStr.put("enter", "");
        firstsStr.put("exclude", "");
        firstsStr.put("find", "");
        firstsStr.put("gamemode", "creation");
        firstsStr.put("include", "");
        firstsStr.put("inventory_group", "creation");
        firstsStr.put("key", "preferences");
        firstsStr.put("list", "");
        firstsStr.put("make_preset", "");
        firstsStr.put("playercount", "");
        firstsStr.put("prune", "");
        firstsStr.put("prunelist", "");
        firstsStr.put("purge", "");
        firstsStr.put("recharger", "");
        firstsStr.put("reload", "");
        firstsStrArtron.add("full_charge_item");
        firstsStrArtron.add("jettison_seed");
        // boolean
        firstsBool.put("add_perms", "creation");
        firstsBool.put("all_blocks", "allow");
        firstsBool.put("achievements", "allow");
        firstsBool.put("autonomous", "allow");
        firstsBool.put("hads", "allow");
        firstsBool.put("mob_farming", "allow");
        firstsBool.put("tp_switch", "allow");
        firstsBool.put("chameleon", "");
        firstsBool.put("create_worlds", "creation");
        firstsBool.put("create_worlds_with_perms", "creation");
        firstsBool.put("custom_schematic", "creation");
        firstsBool.put("debug", "");
        firstsBool.put("default_world", "creation");
        firstsBool.put("emergency_npc", "allow");
        firstsBool.put("exile", "travel");
        firstsBool.put("give_key", "travel");
        firstsBool.put("include_default_world", "travel");
        firstsBool.put("keep_night", "creation");
        firstsBool.put("land_on_water", "travel");
        firstsBool.put("materialise", "police_box");
        firstsBool.put("name_tardis", "police_box");
        firstsBool.put("nether", "travel");
        firstsBool.put("per_world_perms", "travel");
        firstsBool.put("platform", "travel");
        firstsBool.put("respect_factions", "preferences");
        firstsBool.put("respect_towny", "preferences");
        firstsBool.put("respect_worldborder", "preferences");
        firstsBool.put("respect_worldguard", "preferences");
        firstsBool.put("return_room_seed", "growth");
        firstsBool.put("rooms_require_blocks", "growth");
        firstsBool.put("sfx", "allow");
        firstsBool.put("spawn_eggs", "allow");
        firstsBool.put("strike_lightning", "preferences");
        firstsBool.put("the_end", "travel");
        firstsBool.put("use_block_stack", "creation");
        firstsBool.put("use_clay", "creation");
        firstsBool.put("use_worldguard", "preferences");
        // integer
        firstsInt.put("border_radius", "creation");
        firstsInt.put("confirm_timeout", "police_box");
        firstsInt.put("count", "creation");
        firstsInt.put("custom_creeper_id", "creation");
        firstsInt.put("gravity_max_distance", "growth");
        firstsInt.put("gravity_max_velocity", "growth");
        firstsInt.put("hads_damage", "preferences");
        firstsInt.put("hads_distance", "preferences");
        firstsInt.put("malfunction", "preferences");
        firstsInt.put("malfunction_end", "preferences");
        firstsInt.put("malfunction_nether", "preferences");
        firstsInt.put("platform_data", "police_box");
        firstsInt.put("platform_id", "police_box");
        firstsInt.put("random_attempts", "travel");
        firstsInt.put("room_speed", "growth");
        firstsInt.put("rooms_condenser_percent", "growth");
        firstsInt.put("sfx_volume", "preferences");
        firstsInt.put("terminal_step", "travel");
        firstsInt.put("timeout", "travel");
        firstsInt.put("timeout_height", "travel");
        firstsInt.put("tp_radius", "travel");
        firstsInt.put("wall_id", "police_box");
        firstsInt.put("wall_data", "police_box");
        firstsInt.put("tardis_lamp", "police_box");
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
        firstsIntArtron.add("recharge_distance");
        firstsIntArtron.add("the_end_min");
        firstsIntArtron.add("travel");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If the player typed /tardisadmin then do the following...
        if (cmd.getName().equalsIgnoreCase("tardisadmin")) {
            if (sender instanceof ConsoleCommandSender || sender.hasPermission("tardis.admin")) {
                if (args.length == 0) {
                    sender.sendMessage(CMDS.ADMIN.getHelp().split("\n"));
                    return true;
                }
                String first = args[0].toLowerCase(Locale.ENGLISH);
                if (!firstsStr.containsKey(first) && !firstsBool.containsKey(first) && !firstsInt.containsKey(first) && !firstsIntArtron.contains(first) && !firstsStrArtron.contains(first)) {
                    sender.sendMessage(plugin.pluginName + "TARDIS does not recognise that command argument!");
                    return false;
                }
                if (args.length == 1) {
                    if (first.equals("chunks")) {
                        return new TARDISChunksCommand(plugin).listChunks(sender);
                    }
                    if (first.equals("reload")) {
                        return new TARDISReloadCommand(plugin).reloadConfig(sender);
                    }
                }
                if (first.equals("list")) {
                    return new TARDISListTardisesCommand(plugin).listTardises(sender, args);
                }
                if (args.length < 2) {
                    sender.sendMessage(plugin.pluginName + "Too few command arguments!");
                    return false;
                }
                if (first.equals("config")) {
                    return new TARDISConfigCommand(plugin).showConfigOptions(sender, args);
                }
                if (first.equals("database")) {
                    String dbtype = args[1].toLowerCase(Locale.ENGLISH);
                    if (!dbtype.equals("mysql") && !dbtype.equals("sqlite")) {
                        sender.sendMessage(plugin.pluginName + "TARDIS database type must be one of 'mysql' or 'sqlite'!");
                        return true;
                    }
                    plugin.getConfig().set("database", dbtype);
                }
                if (first.equals("make_preset")) {
                    return new TARDISMakePresetCommand(plugin).scanBlocks(sender, args);
                }
                if (first.equals("playercount")) {
                    return new TARDISPlayerCountCommand(plugin).countPlayers(sender, args);
                }
                if (first.equals("prune")) {
                    return new TARDISPruneCommand(plugin).startPruning(sender, args);
                }
                if (first.equals("prunelist")) {
                    return new TARDISPruneCommand(plugin).listPrunes(sender, args);
                }
                if (first.equals("purge")) {
                    return new TARDISPurgeCommand(plugin).clearAll(sender, args);
                }
                if (first.equals("recharger")) {
                    return new TARDISRechargerCommand(plugin).setRecharger(sender, args);
                }
                if (first.equals("decharge")) {
                    return new TARDISDechargeCommand(plugin).removeChragerStatus(sender, args);
                }
                if (first.equals("enter")) {
                    return new TARDISEnterCommand(plugin).enterTARDIS(sender, args);
                }
                if (first.equals("delete")) {
                    return new TARDISDeleteCommand(plugin).deleteTARDIS(sender, args);
                }
                if (first.equals("key") || first.equals("custom_schematic_seed")) {
                    return new TARDISSetMaterialCommand(plugin).setConfigMaterial(sender, args, firstsStr.get(first));
                }
                if (first.equals("full_charge_item") || first.equals("jettison_seed")) {
                    return new TARDISSetMaterialCommand(plugin).setConfigMaterial(sender, args);
                }
                if (first.equals("default_world_name")) {
                    return new TARDISDefaultWorldNameCommand(plugin).setName(sender, args);
                }
                if (first.equals("difficulty")) {
                    if (!args[1].equalsIgnoreCase("easy") && !args[1].equalsIgnoreCase("hard")) {
                        sender.sendMessage(plugin.pluginName + ChatColor.RED + "Difficulty must be easy or hard!");
                        return true;
                    }
                    plugin.getConfig().set("preferences.difficulty", args[1].toLowerCase(Locale.ENGLISH));
                }
                if (first.equals("gamemode")) {
                    if (!args[1].equalsIgnoreCase("creative") && !args[1].equalsIgnoreCase("survival")) {
                        sender.sendMessage(plugin.pluginName + ChatColor.RED + "Gamemode must be creative or survival!");
                        return true;
                    }
                    plugin.getConfig().set("creation.gamemode", args[1].toLowerCase(Locale.ENGLISH));
                }
                if (first.equals("inventory_group")) {
                    plugin.getConfig().set("creation.inventory_group", args[1]);
                }
                if (first.equals("exclude") || first.equals("include")) {
                    return new TARDISSetWorldInclusionCommand(plugin).setWorldStatus(sender, args);
                }
                // checks if its a boolean config option
                if (firstsBool.containsKey(first)) {
                    return new TARDISSetBooleanCommand(plugin).setConfigBool(sender, args, firstsBool.get(first));
                }
                // checks if its a number config option
                if (firstsInt.containsKey(first)) {
                    return new TARDISSetIntegerCommand(plugin).setConfigInt(sender, args, firstsInt.get(first));
                }
                if (firstsIntArtron.contains(first)) {
                    return new TARDISSetIntegerCommand(plugin).setConfigInt(sender, args);
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
