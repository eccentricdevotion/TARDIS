/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.config;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.enumeration.UseClay;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Command /tardisadmin [arguments].
 * <p>
 * The Lord President was the most powerful member of the Time Lord Council and had near absolute authority, and used a
 * link to the Matrix, a vast computer network containing the knowledge and experiences of all past generations of Time
 * Lords, to set Time Lord policy and remain alert to potential threats from lesser civilisations.
 *
 * @author eccentric_nz
 */
public class TARDISConfigCommand implements CommandExecutor {

    final HashMap<String, String> firstsStr = new HashMap<>();
    final List<String> firstsStrArtron = new ArrayList<>();
    final HashMap<String, String> firstsBool = new HashMap<>();
    final HashMap<String, String> firstsInt = new HashMap<>();
    final List<String> firstsIntArtron = new ArrayList<>();
    private final TARDIS plugin;

    public TARDISConfigCommand(TARDIS plugin) {
        this.plugin = plugin;
        // add first arguments
        firstsStr.put("area", "creation");
        firstsStr.put("custom_schematic_seed", "creation");
        firstsStr.put("database", "storage");
        firstsStr.put("default_key", "preferences");
        firstsStr.put("default_preset", "police_box");
        firstsStr.put("default_sonic", "preferences");
        firstsStr.put("default_world_name", "creation");
        firstsStr.put("difficulty", "preferences");
        firstsStr.put("exclude", "");
        firstsStr.put("include", "");
        firstsStr.put("inventory_group", "creation");
        firstsStr.put("key", "preferences");
        firstsStr.put("language", "preferences");
        firstsStr.put("options", "");
        firstsStr.put("reload", "");
        firstsStr.put("respect_towny", "preferences");
        firstsStr.put("respect_worldguard", "preferences");
        firstsStr.put("siege", "siege");
        firstsStr.put("sign_colour", "police_box");
        firstsStr.put("use_clay", "creation");
        firstsStr.put("vortex_fall", "preferences");
        firstsStrArtron.add("full_charge_item");
        firstsStrArtron.add("jettison_seed");
        // boolean
        firstsBool.put("3d_doors", "allow");
        firstsBool.put("abandon", "");
        firstsBool.put("achievements", "allow");
        firstsBool.put("add_perms", "creation");
        firstsBool.put("admin_bypass", "allow");
        firstsBool.put("all_blocks", "allow");
        firstsBool.put("allow_end_after_visit", "travel");
        firstsBool.put("allow_nether_after_visit", "travel");
        firstsBool.put("archive", "");
        firstsBool.put("autonomous", "allow");
        firstsBool.put("blueprints", "");
        firstsBool.put("chameleon", "travel");
        firstsBool.put("check_blocks_before_upgrade", "desktop");
        firstsBool.put("create_worlds", "creation");
        firstsBool.put("create_worlds_with_perms", "creation");
        firstsBool.put("custom_schematic", "creation");
        firstsBool.put("damage", "circuits");
        firstsBool.put("debug", "");
        firstsBool.put("default_world", "creation");
        firstsBool.put("emergency_npc", "allow");
        firstsBool.put("dynmap", "");
        firstsBool.put("exile", "travel");
        firstsBool.put("external_gravity", "allow");
        firstsBool.put("give_key", "travel");
        firstsBool.put("guardians", "allow");
        firstsBool.put("hads", "allow");
        firstsBool.put("handles", "allow");
        firstsBool.put("include_default_world", "travel");
        firstsBool.put("invisibility", "allow");
        firstsBool.put("keep_night", "creation");
        firstsBool.put("land_on_water", "travel");
        firstsBool.put("materialise", "police_box");
        firstsBool.put("mob_farming", "allow");
        firstsBool.put("name_tardis", "police_box");
        firstsBool.put("nether", "travel");
        firstsBool.put("no_coords", "preferences");
        firstsBool.put("no_creative_condense", "preferences");
        firstsBool.put("no_enchanted_condense", "preferences");
        firstsBool.put("open_door_policy", "preferences");
        firstsBool.put("particles", "artron_furnace");
        firstsBool.put("per_world_perms", "travel");
        firstsBool.put("perception_filter", "allow");
        firstsBool.put("power_down", "allow");
        firstsBool.put("power_down_on_quit", "allow");
        firstsBool.put("redefine", "travel.terminal");
        firstsBool.put("reduce_count", "abandon");
        firstsBool.put("render_entities", "preferences");
        firstsBool.put("respect_factions", "preferences");
        firstsBool.put("respect_grief_prevention", "preferences");
        firstsBool.put("respect_worldborder", "preferences");
        firstsBool.put("return_room_seed", "growth");
        firstsBool.put("rooms_require_blocks", "growth");
        firstsBool.put("sfx", "allow");
        firstsBool.put("spawn_eggs", "allow");
        firstsBool.put("spawn_random_monsters", "preferences");
        firstsBool.put("strike_lightning", "preferences");
        firstsBool.put("switch_resource_packs", "");
        firstsBool.put("the_end", "travel");
        firstsBool.put("tp_switch", "allow");
        firstsBool.put("use_worldguard", "preferences");
        firstsBool.put("village_travel", "allow");
        firstsBool.put("walk_in_tardis", "preferences");
        firstsBool.put("weather_set", "allow");
        firstsBool.put("wg_flag_set", "allow");
        firstsBool.put("zero_room", "allow");
        // integer
        firstsInt.put("ARS", "circuits.uses");
        firstsInt.put("ars_limit", "growth");
        firstsInt.put("block_change_percent", "desktop");
        firstsInt.put("border_radius", "creation");
        firstsInt.put("chameleon_uses", "circuits.uses");
        firstsInt.put("chat_width", "preferences");
        firstsInt.put("confirm_timeout", "police_box");
        firstsInt.put("count", "creation");
        firstsInt.put("force_field", "allow.force_field");
        firstsInt.put("grace_period", "travel");
        firstsInt.put("gravity_max_distance", "growth");
        firstsInt.put("gravity_max_velocity", "growth");
        firstsInt.put("hads_damage", "preferences");
        firstsInt.put("hads_distance", "preferences");
        firstsInt.put("heal_speed", "preferences");
        firstsInt.put("input", "circuits.uses");
        firstsInt.put("invisibility_uses", "circuits.uses");
        firstsInt.put("malfunction", "preferences");
        firstsInt.put("malfunction_end", "preferences");
        firstsInt.put("malfunction_nether", "preferences");
        firstsInt.put("materialisation", "circuits.uses");
        firstsInt.put("memory", "circuits.uses");
        firstsInt.put("min_time", "arch");
        firstsInt.put("random_attempts", "travel");
        firstsInt.put("random_circuit", "travel");
        firstsInt.put("randomiser", "circuits.uses");
        firstsInt.put("room_speed", "growth");
        firstsInt.put("rooms_condenser_percent", "growth");
        firstsInt.put("scanner", "circuits.uses");
        firstsInt.put("sfx_volume", "preferences");
        firstsInt.put("temporal", "circuits.uses");
        firstsInt.put("terminal_step", "travel");
        firstsInt.put("timeout", "travel");
        firstsInt.put("timeout_height", "travel");
        firstsInt.put("tips_limit", "creation");
        firstsInt.put("tp_radius", "travel");
        firstsInt.put("update_period", "dynmap");
        firstsInt.put("updates_per_tick", "dynmap");
        firstsInt.put("wall_data", "police_box");
        firstsInt.put("wall_id", "police_box");
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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        // If the player typed /tardisadmin then do the following...
        if (cmd.getName().equalsIgnoreCase("tardisconfig")) {
            if (sender instanceof ConsoleCommandSender || sender.hasPermission("tardis.admin")) {
                if (args.length == 0) {
                    new TARDISCommandHelper(plugin).getCommand("tardisadmin", sender);
                    return true;
                }
                String first = args[0].toLowerCase(Locale.ENGLISH);
                if (!firstsStr.containsKey(first) && !firstsBool.containsKey(first) && !firstsInt.containsKey(first) && !firstsIntArtron.contains(first) && !firstsStrArtron.contains(first)) {
                    TARDISMessage.send(sender, "ARG_NOT_VALID");
                    return false;
                }
                if (args.length == 1 && first.equals("reload")) {
                    return new TARDISReloadCommand(plugin).reloadConfig(sender);
                }
                if (args.length < 2) {
                    TARDISMessage.send(sender, "TOO_FEW_ARGS");
                    return false;
                }
                if (first.equals("reload")) {
                    return new TARDISReloadCommand(plugin).reloadOtherConfig(sender, args);
                }
                if (first.equals("area")) {
                    plugin.getConfig().set("creation.area", args[1]);
                }
                if (first.equals("options")) {
                    return new TARDISConfigOptionsCommand(plugin).showConfigOptions(sender, args);
                }
                if (first.equals("language")) {
                    return new TARDISLanguageCommand(plugin).setLanguage(sender, args);
                }
                if (first.equals("power_down")) {
                    return new TARDISPowerDownCommand(plugin).togglePowerDown(sender, args);
                }
                if (first.equals("database")) {
                    String dbtype = args[1].toLowerCase(Locale.ENGLISH);
                    if (!dbtype.equals("mysql") && !dbtype.equals("sqlite")) {
                        TARDISMessage.send(sender, "ARG_DB");
                        return true;
                    }
                    plugin.getConfig().set("database", dbtype);
                }
                if (first.equals("include") || first.equals("exclude")) {
                    return new TARDISSetWorldInclusionCommand(plugin).setWorldStatus(sender, args);
                }
                if (first.equals("siege")) {
                    return new TARDISSiegeCommand(plugin).setOption(sender, args);
                }
                if (first.equals("sign_colour")) {
                    return new TARDISSignColourCommand(plugin).setColour(sender, args);
                }
                if (first.equals("key") || first.equals("custom_schematic_seed")) {
                    return new TARDISSetMaterialCommand(plugin).setConfigMaterial(sender, args, firstsStr.get(first));
                }
                if (first.equals("full_charge_item") || first.equals("jettison_seed")) {
                    return new TARDISSetMaterialCommand(plugin).setConfigMaterial(sender, args);
                }
                if (first.equals("default_key") || first.equals("default_sonic")) {
                    return new TARDISDefaultCommand(plugin).setDefaultItem(sender, args);
                }
                if (first.equals("default_world_name")) {
                    return new TARDISDefaultWorldNameCommand(plugin).setName(sender, args);
                }
                if (first.equals("respect_towny")) {
                    return new TARDISSetRespectCommand(plugin).setRegion(sender, args);
                }
                if (first.equals("respect_worldguard")) {
                    return new TARDISSetRespectCommand(plugin).setFlag(sender, args);
                }
                if (first.equals("difficulty")) {
                    if (!args[1].equalsIgnoreCase("easy") && !args[1].equalsIgnoreCase("medium") && !args[1].equalsIgnoreCase("hard")) {
                        TARDISMessage.send(sender, "ARG_DIFF");
                        return true;
                    }
                    plugin.getConfig().set("preferences.difficulty", args[1].toLowerCase(Locale.ENGLISH));
                    plugin.setDifficulty(Difficulty.valueOf(args[1].toUpperCase(Locale.ENGLISH)));
                }
                if (first.equals("default_preset")) {
                    try {
                        PRESET.valueOf(args[1].toUpperCase(Locale.ENGLISH));
                    } catch (IllegalArgumentException e) {
                        TARDISMessage.send(sender, "ARG_PRESET");
                        return true;
                    }
                    plugin.getConfig().set("police_box.default_preset", args[1].toUpperCase(Locale.ENGLISH));
                }
                if (first.equals("use_clay")) {
                    try {
                        UseClay.valueOf(args[1].toUpperCase(Locale.ENGLISH));
                    } catch (IllegalArgumentException e) {
                        TARDISMessage.send(sender, "ARG_USE_CLAY");
                        return true;
                    }
                    plugin.getConfig().set("creation.use_clay", args[1].toUpperCase(Locale.ENGLISH));
                }
                if (first.equals("inventory_group")) {
                    plugin.getConfig().set("creation.inventory_group", args[1]);
                }
                if (first.equals("vortex_fall")) {
                    if (!args[1].equalsIgnoreCase("kill") && !args[1].equalsIgnoreCase("teleport")) {
                        TARDISMessage.send(sender, "ARG_VORTEX");
                        return true;
                    }
                    plugin.getConfig().set("preferences.vortex_fall", args[1].toLowerCase(Locale.ENGLISH));
                }
                // checks if it's a boolean config option
                if (firstsBool.containsKey(first)) {
                    if (first.equals("zero_room")) {
                        return new TARDISSetZeroRoomCommand(plugin).setConfigZero(sender, args);
                    } else {
                        return new TARDISSetBooleanCommand(plugin).setConfigBool(sender, args, firstsBool.get(first));
                    }
                }
                // checks if it's a number config option
                if (firstsInt.containsKey(first)) {
                    if (first.equalsIgnoreCase("random_circuit")) {
                        return new TARDISSetIntegerCommand(plugin).setRandomInt(sender, args);
                    } else {
                        return new TARDISSetIntegerCommand(plugin).setConfigInt(sender, args, firstsInt.get(first));
                    }
                }
                if (firstsIntArtron.contains(first)) {
                    return new TARDISSetIntegerCommand(plugin).setConfigInt(sender, args);
                }
                plugin.saveConfig();
                TARDISMessage.send(sender, "CONFIG_UPDATED");
                return true;
            } else {
                TARDISMessage.send(sender, "CMD_ADMIN");
                return false;
            }
        }
        return false;
    }
}
