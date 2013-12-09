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
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
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
    public List<String> firstsStr = new ArrayList<String>();
    public List<String> firstsStrArtron = new ArrayList<String>();
    public List<String> firstsBool = new ArrayList<String>();
    public List<String> firstsInt = new ArrayList<String>();
    public List<String> firstsIntArtron = new ArrayList<String>();

    public TARDISAdminCommands(TARDIS plugin) {
        this.plugin = plugin;
        // add first arguments
        firstsStr.add("chunks");
        firstsStr.add("config");
        firstsStr.add("custom_schematic_seed");
        firstsStr.add("database");
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
        firstsStr.add("make_preset");
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
        firstsBool.add("chameleon");
        firstsBool.add("create_worlds");
        firstsBool.add("create_worlds_with_perms");
        firstsBool.add("custom_schematic");
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
        firstsBool.add("use_block_stack");
        firstsBool.add("use_clay");
        firstsBool.add("use_worldguard");
        // integer
        firstsInt.add("admin_item");
        firstsInt.add("border_radius");
        firstsInt.add("confirm_timeout");
        firstsInt.add("count");
        firstsInt.add("custom_creeper_id");
        firstsInt.add("gravity_max_distance");
        firstsInt.add("gravity_max_velocity");
        firstsInt.add("hads_damage");
        firstsInt.add("hads_distance");
        firstsInt.add("malfunction");
        firstsInt.add("malfunction_end");
        firstsInt.add("malfunction_nether");
        firstsInt.add("platform_data");
        firstsInt.add("platform_id");
        firstsInt.add("random_attempts");
        firstsInt.add("recharge_distance");
        firstsInt.add("room_speed");
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
                        return new TARDISChunksCommand(plugin).listChunks(sender);
                    }
                    if (first.equals("reload")) {
                        return new TARDISReloadCommand(plugin).reloadConfig(sender);
                    }
                }
                if (first.equals("config")) {
                    return new TARDISConfigCommand(plugin).showConfigOptions(sender, args);
                }
                if (first.equals("list")) {
                    return new TARDISListTardisesCommand(plugin).listTardises(sender, args);
                }
                if (args.length < 2) {
                    sender.sendMessage(plugin.pluginName + "Too few command arguments!");
                    return false;
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
                if (first.equals("key") || first.equals("stattenheim") || first.equals("full_charge_item") || first.equals("jettison_seed") || first.equals("custom_schematic_seed")) {
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
                    return new TARDISSetWorldInclusionCommand(plugin).setWorldStatus(sender, args);
                }
                // checks if its a boolean config option
                if (firstsBool.contains(first)) {
                    return new TARDISSetBooleanCommand(plugin).setConfigBool(sender, args);
                }
                // checks if its a number config option
                if (firstsInt.contains(first) || firstsIntArtron.contains(first)) {
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
