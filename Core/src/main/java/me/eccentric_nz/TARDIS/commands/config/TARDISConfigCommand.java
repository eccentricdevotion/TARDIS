/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.UseClay;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

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
        firstsStr.put("autonomous_area", "");
        firstsStr.put("crafting", "difficulty");
        firstsStr.put("database", "storage");
        firstsStr.put("default_key", "preferences");
        firstsStr.put("default_model", "sonic");
        firstsStr.put("default_preset", "police_box");
        firstsStr.put("default_world_name", "creation");
        firstsStr.put("exclude", "");
        firstsStr.put("include", "");
        firstsStr.put("inventory_group", "creation");
        firstsStr.put("key", "preferences");
        firstsStr.put("language", "preferences");
        firstsStr.put("options", "");
        firstsStr.put("provider", "mapping");
        firstsStr.put("reload", "");
        firstsStr.put("respect_towny", "preferences");
        firstsStr.put("respect_worldguard", "preferences");
        firstsStr.put("seed_block.easy", "creation");
        firstsStr.put("seed_block.normal", "creation");
        firstsStr.put("seed_block.hard", "creation");
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
        firstsBool.put("add_lights", "allow");
        firstsBool.put("add_perms", "creation");
        firstsBool.put("admin_bypass", "allow");
        firstsBool.put("all_blocks", "allow");
        firstsBool.put("allow_end_after_visit", "travel");
        firstsBool.put("allow_nether_after_visit", "travel");
        firstsBool.put("animated_door", "police_box");
        firstsBool.put("any_key", "preferences");
        firstsBool.put("archive", "");
        firstsBool.put("autonomous", "allow");
        firstsBool.put("biome_reader", "difficulty");
        firstsBool.put("blueprints", "modules");
        firstsBool.put("chameleon", "travel");
        firstsBool.put("charge", "sonic");
        firstsBool.put("check_blocks_before_upgrade", "desktop");
        firstsBool.put("check_for_home", "creation");
        firstsBool.put("chemistry", "modules");
        firstsBool.put("circuits", "difficulty");
        firstsBool.put("create_worlds", "creation");
        firstsBool.put("create_worlds_with_perms", "creation");
        firstsBool.put("damage", "circuits");
        firstsBool.put("debug", "");
        firstsBool.put("default_world", "creation");
        firstsBool.put("disk_in_hand_for_write", "difficulty");
        firstsBool.put("disks", "difficulty");
        firstsBool.put("dynamic_lamps", "allow");
        firstsBool.put("emergency_npc", "allow");
        firstsBool.put("exile", "travel");
        firstsBool.put("external_gravity", "allow");
        firstsBool.put("furnace_particles", "artron_furnace");
        firstsBool.put("give_key", "travel");
        firstsBool.put("guardians", "allow");
        firstsBool.put("hads", "allow");
        firstsBool.put("handles", "allow");
        firstsBool.put("include_default_world", "travel");
        firstsBool.put("invisibility", "allow");
        firstsBool.put("keep_night", "creation");
        firstsBool.put("land_on_water", "travel");
        firstsBool.put("load_shells", "police_box");
        firstsBool.put("mapping", "modules");
        firstsBool.put("materialise", "police_box");
        firstsBool.put("mob_farming", "allow");
        firstsBool.put("name_tardis", "police_box");
        firstsBool.put("nerf_pistons.enabled", "preferences");
        firstsBool.put("nerf_pistons.only_tardis_worlds", "preferences");
        firstsBool.put("nether", "travel");
        firstsBool.put("no_coords", "preferences");
        firstsBool.put("no_creative_condense", "preferences");
        firstsBool.put("no_enchanted_condense", "preferences");
        firstsBool.put("open_door_policy", "preferences");
        firstsBool.put("particles", "eye_of_harmony");
        firstsBool.put("per_world_perms", "travel");
        firstsBool.put("perception_filter", "allow");
        firstsBool.put("power_down", "allow");
        firstsBool.put("power_down_on_quit", "allow");
        firstsBool.put("previews", "desktop");
        firstsBool.put("reduce_count", "abandon");
        firstsBool.put("regeneration", "modules");
        firstsBool.put("render_entities", "preferences");
        firstsBool.put("respect_factions", "preferences");
        firstsBool.put("respect_grief_prevention", "preferences");
        firstsBool.put("respect_worldborder", "preferences");
        firstsBool.put("return_room_seed", "growth");
        firstsBool.put("rooms_require_blocks", "growth");
        firstsBool.put("seed_block.crafting", "creation");
        firstsBool.put("seed_block.legacy", "creation");
        firstsBool.put("sfx", "allow");
        firstsBool.put("shop", "modules");
        firstsBool.put("sonic_blaster", "modules");
        firstsBool.put("spawn_eggs", "allow");
        firstsBool.put("spawn_random_monsters", "preferences");
        firstsBool.put("stattenheim_remote", "difficulty");
        firstsBool.put("strike_lightning", "preferences");
        firstsBool.put("switch_resource_packs", "");
        firstsBool.put("system_upgrades", "difficulty");
        firstsBool.put("tardis_locator", "difficulty");
        firstsBool.put("terminal.redefine", "travel");
        firstsBool.put("the_end", "travel");
        firstsBool.put("update.auto_update", "preferences");
        firstsBool.put("update.notify", "preferences");
        firstsBool.put("use_default_condensables", "preferences");
        firstsBool.put("use_nick", "police_box");
        firstsBool.put("use_worldguard", "preferences");
        firstsBool.put("view_interior", "police_box");
        firstsBool.put("view_interior_uses_console_size", "police_box");
        firstsBool.put("village_travel", "allow");
        firstsBool.put("vortex_manipulator", "modules");
        firstsBool.put("walk_in_tardis", "preferences");
        firstsBool.put("weather_set", "allow");
        firstsBool.put("weeping_angels", "modules");
        firstsBool.put("wg_flag_set", "allow");
        firstsBool.put("zero_room", "allow");
        // integer
        firstsInt.put("ars_limit", "growth");
        firstsInt.put("block_change_percent", "desktop");
        firstsInt.put("border_radius", "creation");
        firstsInt.put("charge_interval", "sonic");
        firstsInt.put("charge_level", "sonic");
        firstsInt.put("chat_width", "preferences");
        firstsInt.put("conversion_radius", "sonic");
        firstsInt.put("count", "creation");
        firstsInt.put("delay_factor", "growth");
        firstsInt.put("force_field", "allow");
        firstsInt.put("freeze_cooldown", "sonic");
        firstsInt.put("grace_period", "travel");
        firstsInt.put("gravity_max_distance", "growth");
        firstsInt.put("gravity_max_velocity", "growth");
        firstsInt.put("hads_damage", "preferences");
        firstsInt.put("hads_distance", "preferences");
        firstsInt.put("heal_speed", "preferences");
        firstsInt.put("malfunction", "preferences");
        firstsInt.put("malfunction_end", "preferences");
        firstsInt.put("malfunction_nether", "preferences");
        firstsInt.put("min_time", "arch");
        firstsInt.put("random_attempts", "travel");
        firstsInt.put("random_circuit.x", "travel");
        firstsInt.put("random_circuit.z", "travel");
        firstsInt.put("room_speed", "growth");
        firstsInt.put("rooms_condenser_percent", "growth");
        firstsInt.put("sfx_volume", "preferences");
        firstsInt.put("terminal_step", "travel");
        firstsInt.put("timeout", "travel");
        firstsInt.put("timeout_height", "travel");
        firstsInt.put("tips_limit", "creation");
        firstsInt.put("tp_radius", "travel");
        firstsInt.put("update_period", "mapping");
        firstsInt.put("updates_per_tick", "mapping");
        firstsInt.put("usage", "sonic");
        firstsInt.put("uses.ars", "circuits");
        firstsInt.put("uses.chameleon", "circuits");
        firstsInt.put("uses.input", "circuits");
        firstsInt.put("uses.invisibility", "circuits");
        firstsInt.put("uses.materialisation", "circuits");
        firstsInt.put("uses.memory", "circuits");
        firstsInt.put("uses.randomiser", "circuits");
        firstsInt.put("uses.scanner", "circuits");
        firstsInt.put("uses.temporal", "circuits");
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
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If the player typed /tardisadmin then do the following...
        if (cmd.getName().equalsIgnoreCase("tardisconfig")) {
            if (sender instanceof ConsoleCommandSender || sender.hasPermission("tardis.admin")) {
                if (args.length == 0) {
                    new TARDISCommandHelper(plugin).getCommand("tardisconfig", sender);
                    return true;
                }
                String first = args[0].toLowerCase(Locale.ROOT);
                if (!firstsStr.containsKey(first) && !firstsBool.containsKey(first) && !firstsInt.containsKey(first) && !firstsIntArtron.contains(first) && !firstsStrArtron.contains(first)) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_NOT_VALID");
                    return false;
                }
                if (args.length == 1 && first.equals("reload")) {
                    return new TARDISReloadCommand(plugin).reloadConfig(sender);
                }
                if (args.length < 2) {
                    // get the option path
                    boolean isMainConfig = true;
                    String path = "";
                    if (firstsStr.containsKey(first)) {
                        path = firstsStr.get(first) + "." + first;
                    } else if (firstsBool.containsKey(first)) {
                        path = firstsBool.get(first) + "." + first;
                    } else if (firstsInt.containsKey(first)) {
                        path = firstsInt.get(first) + "." + first;
                    } else if (firstsStrArtron.contains(first) || firstsIntArtron.contains(first)) {
                        isMainConfig = false;
                        path = first;
                    }
                    // show the value of the config option
                    if (isMainConfig) {
                        plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_OPTION", path, plugin.getConfig().getString(path));
                    } else {
                        plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_OPTION_ARTRON", first, plugin.getArtronConfig().getString(path));
                    }
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_OPTION_SET", first);
                    return true;
                }
                if (first.equals("provider")) {
                    String provider = args[1];
                    if (!provider.equals("dynmap") && !provider.equals("BlueMap") && !provider.equals("squaremap")) {
                        plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_PROVIDER");
                        return true;
                    }
                    plugin.getConfig().set("mapping.provider", provider);
                }
                if (first.equals("reload")) {
                    return new TARDISReloadCommand(plugin).reloadOtherConfig(sender, args);
                }
                if (first.equals("area")) {
                    plugin.getConfig().set("creation.area", args[1]);
                }
                if (first.equals("autonomous_area")) {
                    return new TARDISAutonomousAreaCommand(plugin).processArea(sender, args);
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
                    String dbtype = args[1].toLowerCase(Locale.ROOT);
                    if (!dbtype.equals("mysql") && !dbtype.equals("sqlite")) {
                        plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_DB");
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
                if (first.equals("key")) {
                    return new TARDISSetMaterialCommand(plugin).setConfigMaterial(sender, args, firstsStr.get(first));
                }
                if (first.equals("full_charge_item") || first.equals("jettison_seed")) {
                    return new TARDISSetMaterialCommand(plugin).setConfigMaterial(sender, args);
                }
                if (first.equals("default_key") || first.equals("default_model")) {
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
                if (first.equals("crafting")) {
                    if (!args[1].equalsIgnoreCase("easy") && !args[1].equalsIgnoreCase("hard")) {
                        plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_DIFF");
                        return true;
                    }
                    plugin.getConfig().set("difficulty.crafting", args[1].toLowerCase(Locale.ROOT));
                    plugin.setDifficulty(CraftingDifficulty.valueOf(args[1].toUpperCase(Locale.ROOT)));
                }
                if (first.equals("default_preset")) {
                    if (plugin.getCustomModelConfig().getConfigurationSection("models").getKeys(false).contains(args[1])) {
                        plugin.getConfig().set("police_box.default_preset", "ITEM:" + args[1]);
                    } else {
                        try {
                            ChameleonPreset.valueOf(args[1].toUpperCase(Locale.ROOT));
                        } catch (IllegalArgumentException e) {
                            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_PRESET");
                            return true;
                        }
                        plugin.getConfig().set("police_box.default_preset", args[1].toUpperCase(Locale.ROOT));
                    }
                }
                if (first.equals("use_clay")) {
                    try {
                        UseClay.valueOf(args[1].toUpperCase(Locale.ROOT));
                    } catch (IllegalArgumentException e) {
                        plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_USE_CLAY");
                        return true;
                    }
                    plugin.getConfig().set("creation.use_clay", args[1].toUpperCase(Locale.ROOT));
                }
                if (first.equals("inventory_group")) {
                    plugin.getConfig().set("creation.inventory_group", args[1]);
                }
                if (first.equals("vortex_fall")) {
                    if (!args[1].equalsIgnoreCase("kill") && !args[1].equalsIgnoreCase("teleport")) {
                        plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_VORTEX");
                        return true;
                    }
                    plugin.getConfig().set("preferences.vortex_fall", args[1].toLowerCase(Locale.ROOT));
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
                    if (first.startsWith("random_circuit.")) {
                        return new TARDISSetIntegerCommand(plugin).setRandomInt(sender, args);
                    } else {
                        return new TARDISSetIntegerCommand(plugin).setConfigInt(sender, args, firstsInt.get(first));
                    }
                }
                if (firstsIntArtron.contains(first)) {
                    return new TARDISSetIntegerCommand(plugin).setConfigInt(sender, args);
                }
                plugin.saveConfig();
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_UPDATED", first);
                return true;
            } else {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_ADMIN");
                return false;
            }
        }
        return false;
    }
}
