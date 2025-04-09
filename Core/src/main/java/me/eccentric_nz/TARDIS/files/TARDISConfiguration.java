/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.files;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.planets.TARDISWorlds;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The Alpha Centauran Table Tennis Club is a club established by the Alpha Centaurans for the play of table tennis. The
 * species is naturally gifted at the game, since they possess six arms. The Doctor is a member of the Club.
 *
 * @author eccentric_nz
 */
public class TARDISConfiguration {

    private final TARDIS plugin;
    private final FileConfiguration config;
    private final HashMap<String, List<String>> comments = new HashMap<>();
    private final HashMap<String, Double> doubleOptions = new HashMap<>();
    private final HashMap<String, String> stringOptions = new HashMap<>();
    private final HashMap<String, Integer> integerOptions = new HashMap<>();
    private final HashMap<String, Boolean> booleanOptions = new HashMap<>();

    public TARDISConfiguration(TARDIS plugin) {
        this.plugin = plugin;
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        // boolean
        booleanOptions.put("abandon.enabled", true);
        booleanOptions.put("abandon.reduce_count", true);
        booleanOptions.put("allow.3d_doors", false);
        booleanOptions.put("allow.achievements", true);
        booleanOptions.put("allow.add_lights", false);
        booleanOptions.put("allow.admin_bypass", false);
        booleanOptions.put("allow.all_blocks", false);
        booleanOptions.put("allow.animal_spawners", true);
        booleanOptions.put("allow.autonomous", true);
        booleanOptions.put("allow.emergency_npc", true);
        booleanOptions.put("allow.dynamic_lamps", false);
        booleanOptions.put("allow.external_gravity", false);
        booleanOptions.put("allow.guardians", false);
        booleanOptions.put("allow.hads", true);
        booleanOptions.put("allow.invisibility", true);
        booleanOptions.put("allow.mob_farming", true);
        booleanOptions.put("allow.perception_filter", true);
        booleanOptions.put("allow.power_down", true);
        booleanOptions.put("allow.power_down_on_quit", false);
        booleanOptions.put("allow.repair", true);
        booleanOptions.put("allow.sfx", true);
        booleanOptions.put("allow.spawn_eggs", true);
        booleanOptions.put("allow.village_travel", false);
        booleanOptions.put("allow.weather_set", false);
        booleanOptions.put("allow.wg_flag_set", true);
        booleanOptions.put("allow.zero_room", false);
        booleanOptions.put("arch.clear_inv_on_death", false);
        booleanOptions.put("arch.enabled", true);
        booleanOptions.put("arch.switch_inventory", true);
        booleanOptions.put("archive.enabled", true);
        booleanOptions.put("circuits.damage", false);
        booleanOptions.put("conversions.all_in_one.helper", false);
        booleanOptions.put("conversions.all_in_one.shop", false);
        booleanOptions.put("conversions.all_in_one.sonic_blaster", false);
        booleanOptions.put("conversions.all_in_one.vortex_manipulator", false);
        booleanOptions.put("conversions.all_in_one.weeping_angels", false);
        booleanOptions.put("conversions.archive_wall_data", false);
        booleanOptions.put("conversions.ars_materials", false);
        booleanOptions.put("conversions.bind", false);
        booleanOptions.put("conversions.block_materials", false);
        booleanOptions.put("conversions.block_wall_signs", false);
        booleanOptions.put("conversions.condenser_materials", false);
        booleanOptions.put("conversions.constructs", false);
        booleanOptions.put("conversions.controls", false);
        booleanOptions.put("conversions.custom_preset", false);
        booleanOptions.put("conversions.eyes", false);
        booleanOptions.put("conversions.icons", false);
        booleanOptions.put("conversions.interior_door_id", false);
        booleanOptions.put("conversions.legacy_budget", false);
        booleanOptions.put("conversions.paintings", false);
        booleanOptions.put("conversions.exploding", false);
        booleanOptions.put("conversions.player_prefs_materials", false);
        booleanOptions.put("conversions.restore_biomes", false);
        booleanOptions.put("conversions.short_grass", false);
        booleanOptions.put("creation.add_perms", true);
        booleanOptions.put("creation.check_for_home", true);
        booleanOptions.put("creation.create_worlds", false);
        booleanOptions.put("creation.create_worlds_with_perms", false);
        booleanOptions.put("creation.default_world", true);
        booleanOptions.put("creation.enable_legacy", true);
        booleanOptions.put("creation.keep_night", true);
        booleanOptions.put("creation.seed_block.crafting", true);
        booleanOptions.put("creation.seed_block.legacy", true);
        booleanOptions.put("debug", false);
        booleanOptions.put("desktop.check_blocks_before_upgrade", false);
        booleanOptions.put("desktop.previews", false);
        booleanOptions.put("difficulty.circuits", false);
        booleanOptions.put("difficulty.disks", false);
        booleanOptions.put("difficulty.disk_in_hand_for_write", true);
        booleanOptions.put("difficulty.biome_reader", true);
        booleanOptions.put("difficulty.stattenheim_remote", false);
        booleanOptions.put("difficulty.tardis_locator", false);
        booleanOptions.put("eye_of_harmony.player_damage", true);
        booleanOptions.put("eye_of_harmony.ossified", true);
        booleanOptions.put("eye_of_harmony.particles", true);
        booleanOptions.put("growth.return_room_seed", true);
        booleanOptions.put("growth.rooms_require_blocks", false);
        booleanOptions.put("junk.enabled", true);
        booleanOptions.put("junk.particles", true);
        booleanOptions.put("modules.blueprints", false);
        booleanOptions.put("modules.mapping", false);
        booleanOptions.put("modules.weeping_angels", false);
        booleanOptions.put("modules.vortex_manipulator", false);
        booleanOptions.put("modules.shop", false);
        booleanOptions.put("modules.regeneration", false);
        booleanOptions.put("modules.sonic_blaster", false);
        booleanOptions.put("police_box.animated_door", true);
        booleanOptions.put("police_box.load_shells", false);
        booleanOptions.put("police_box.keep_chunk_force_loaded", true);
        booleanOptions.put("police_box.use_nick", false);
        booleanOptions.put("police_box.view_interior", false);
        booleanOptions.put("police_box.view_interior_uses_console_size", false);
        booleanOptions.put("preferences.any_key", false);
        booleanOptions.put("preferences.clean", true);
        booleanOptions.put("preferences.nerf_pistons.enabled", false);
        booleanOptions.put("preferences.nerf_pistons.only_tardis_worlds", true);
        booleanOptions.put("preferences.no_coords", false);
        booleanOptions.put("preferences.no_creative_condense", false);
        booleanOptions.put("preferences.no_enchanted_condense", true);
        booleanOptions.put("preferences.update.notify", true);
        booleanOptions.put("preferences.update.auto_download", false);
        booleanOptions.put("preferences.open_door_policy", false);
        booleanOptions.put("preferences.render_entities", false);
        booleanOptions.put("preferences.respect_chunky_border", true);
        booleanOptions.put("preferences.respect_grief_prevention", true);
        booleanOptions.put("preferences.respect_worldborder", true);
        booleanOptions.put("preferences.spawn_random_monsters", true);
        booleanOptions.put("preferences.strike_lightning", true);
        booleanOptions.put("preferences.use_default_condensables", true);
        booleanOptions.put("preferences.use_worldguard", true);
        booleanOptions.put("siege.butcher", false);
        booleanOptions.put("siege.creeper", false);
        booleanOptions.put("siege.enabled", true);
        booleanOptions.put("siege.healing", false);
        booleanOptions.put("siege.texture", false);
        booleanOptions.put("sonic.charge", false);
        booleanOptions.put("storage.mysql.useSSL", false);
        booleanOptions.put("travel.exile", false);
        booleanOptions.put("travel.give_key", false);
        booleanOptions.put("travel.include_default_world", false);
        booleanOptions.put("travel.land_on_water", true);
        booleanOptions.put("travel.nether", false);
        booleanOptions.put("travel.no_destination_malfunctions", true);
        booleanOptions.put("travel.per_world_perms", false);
        booleanOptions.put("travel.terminal.redefine", false);
        booleanOptions.put("travel.the_end", false);
        // integer
        integerOptions.put("allow.force_field", 8);
        integerOptions.put("arch.min_time", 20);
        integerOptions.put("archive.limit", 3);
        integerOptions.put("circuits.uses.ars", 20);
        integerOptions.put("circuits.uses.chameleon", 25);
        integerOptions.put("circuits.uses.input", 50);
        integerOptions.put("circuits.uses.invisibility", 5);
        integerOptions.put("circuits.uses.materialisation", 50);
        integerOptions.put("circuits.uses.memory", 20);
        integerOptions.put("circuits.uses.randomiser", 50);
        integerOptions.put("circuits.uses.scanner", 20);
        integerOptions.put("circuits.uses.telepathic", 20);
        integerOptions.put("circuits.uses.temporal", 20);
        integerOptions.put("circuits.uses.stattenheim", 15);
        integerOptions.put("creation.border_radius", 256);
        integerOptions.put("creation.count", 0);
        integerOptions.put("creation.inventory_group", 0);
        integerOptions.put("creation.tips_limit", 400);
        integerOptions.put("desktop.block_change_percent", 25);
        integerOptions.put("eye_of_harmony.damage_period", 20);
        integerOptions.put("growth.ars_limit", 1);
        integerOptions.put("growth.delay_factor", 1);
        integerOptions.put("growth.gravity_max_distance", 15);
        integerOptions.put("growth.gravity_max_velocity", 5);
        integerOptions.put("growth.room_speed", 4);
        integerOptions.put("growth.rooms_condenser_percent", 100);
        integerOptions.put("junk.return", -1);
        integerOptions.put("mapping.update_period", 30);
        integerOptions.put("mapping.updates_per_tick", 10);
        integerOptions.put("police_box.rebuild_cooldown", 10000);
        integerOptions.put("preferences.chat_width", 55);
        integerOptions.put("preferences.hads_damage", 10);
        integerOptions.put("preferences.hads_distance", 10);
        integerOptions.put("preferences.heal_speed", 200);
        integerOptions.put("preferences.malfunction", 3);
        integerOptions.put("preferences.malfunction_end", 3);
        integerOptions.put("preferences.malfunction_nether", 3);
        integerOptions.put("preferences.sfx_volume", 10);
        integerOptions.put("preferences.spawn_limit", 10);
        integerOptions.put("siege.breeding", 0);
        integerOptions.put("siege.growth", 0);
        integerOptions.put("sonic.charge_interval", 60);
        integerOptions.put("sonic.charge_level", 1000);
        integerOptions.put("sonic.conversion_radius", 1);
        integerOptions.put("sonic.freeze_cooldown", 60);
        integerOptions.put("sonic.usage", 10);
        integerOptions.put("travel.grace_period", 10);
        integerOptions.put("travel.manual_flight_delay", 60);
        integerOptions.put("travel.max_distance", 29999983);
        integerOptions.put("travel.random_attempts", 30);
        integerOptions.put("travel.random_circuit.x", 5000);
        integerOptions.put("travel.random_circuit.z", 5000);
        integerOptions.put("travel.terminal_step", 1);
        integerOptions.put("travel.timeout", 5);
        integerOptions.put("travel.timeout_height", 135);
        integerOptions.put("travel.tp_radius", 500);
        // string
        stringOptions.put("creation.area", "none");
        stringOptions.put("creation.default_world_name", "TARDIS_TimeVortex");
        stringOptions.put("creation.use_clay", "WOOL");
        stringOptions.put("creation.seed_block.easy", "REDSTONE_TORCH");
        stringOptions.put("creation.seed_block.normal", "SOUL_LANTERN");
        stringOptions.put("creation.seed_block.hard", "DRAGON_BREATH");
        stringOptions.put("creation.tips_next", "HIGHEST");
        stringOptions.put("difficulty.crafting", "easy");
        stringOptions.put("display.all", "&6X&7%X% &6Y&7%Y% &6Z&7%Z% &6F&7%FACING% (%FACING_XZ%) %TARGET_BLOCK%");
        stringOptions.put("police_box.default_preset", "FACTORY");
        stringOptions.put("police_box.sign_colour", "WHITE");
        stringOptions.put("preferences.default_key", "eleventh");
        stringOptions.put("preferences.key", "GOLD_NUGGET");
        stringOptions.put("preferences.language", "en");
        stringOptions.put("preferences.respect_towny", "nation");
        stringOptions.put("preferences.respect_worldguard", "build");
        stringOptions.put("preferences.vortex_fall", "kill");
        stringOptions.put("sonic.default_model", "eleventh");
        stringOptions.put("storage.database", "sqlite");
        stringOptions.put("storage.mysql.database", "TARDIS");
        stringOptions.put("storage.mysql.host", "localhost");
        stringOptions.put("storage.mysql.password", "mysecurepassword");
        stringOptions.put("storage.mysql.port", "3306");
        stringOptions.put("storage.mysql.prefix", "");
        stringOptions.put("storage.mysql.user", "bukkit");
        stringOptions.put("travel.terminal.nether", "world");
        stringOptions.put("travel.terminal.the_end", "world");
        // doubles
        doubleOptions.put("eye_of_harmony.damage_amount", 0.5);
        // comments
        comments.put("abandon", List.of("abandoned TARDISes", "https://tardis.pages.dev/abandon#config-options"));
        comments.put("allow", List.of("restrictions", "https://tardis.pages.dev/configuration/allow"));
        comments.put("arch", List.of("chameleon arch", "https://tardis.pages.dev/chameleon-arch#chameleon-arch-configuration"));
        comments.put("archive", List.of("console archives", "https://tardis.pages.dev/archive#config-options"));
        comments.put("circuits", List.of("circuits", "https://tardis.pages.dev/circuit-use#configuration-options"));
        comments.put("creation", List.of("inner TARDIS", "https://tardis.pages.dev/configuration/creation"));
        comments.put("desktop", List.of("desktop theme", "https://tardis.pages.dev/desktop-theme#config-options"));
        comments.put("display", List.of("HUD display", "https://tardis.pages.dev/display"));
        comments.put("eye_of_harmony", List.of("eye of harmony preferences", "https://tardis.pages.dev/eye-of-harmony"));
        comments.put("growth", List.of("room related", "https://tardis.pages.dev/configuration/growth"));
        comments.put("junk", List.of("junk TARDIS", "https://tardis.pages.dev/junk-tardis#configuration-options"));
        comments.put("mapping", List.of("mapping", "https://tardis.pages.dev/modules/mapping#configuration-options"));
        comments.put("modules", List.of("modules are disabled by default", "https://tardis.pages.dev/modules"));
        comments.put("police_box", List.of("outer TARDIS", "https://tardis.pages.dev/configuration/preset"));
        comments.put("preferences", List.of("general preferences", "https://tardis.pages.dev/configuration/prefs"));
        comments.put("siege", List.of("siege mode", "https://tardis.pages.dev/siege-mode#configuration"));
        comments.put("sonic", List.of("sonic screwdriver preferences", "https://tardis.pages.dev/sonic-dock#configuration"));
        comments.put("storage", List.of("how the plugin persists data", "https://tardis.pages.dev/configuration/storage"));
        comments.put("travel", List.of("travel settings", "https://tardis.pages.dev/configuration/travel"));
        comments.put("conversions", List.of("don't touch!"));
    }

    /**
     * Checks that the config file contains all the required entries. If entries are missing, then they are added with
     * default values. Also checks that all current server worlds are added to the config, and any deleted worlds are
     * removed.
     */
    public void checkConfig() {
        int i = 0;
        if (config.getString("creation.default_world_name").equals("tardis_time_vortex")) {
            plugin.getConfig().set("creation.default_world_name", "TARDIS_TimeVortex");
            i++;
        }
        if (!config.contains("rechargers")) {
            plugin.getConfig().createSection("rechargers");
            i++;
        }
        if (config.contains("creation.previews")) {
            plugin.getConfig().set("desktop.previews", config.getBoolean("creation.previews"));
            plugin.getConfig().set("creation.previews", null);
            i++;
        }
        if (config.contains("damage_amount")) {
            plugin.getConfig().set("eye_of_harmony.damage_amount", config.getDouble("damage_amount"));
            plugin.getConfig().set("damage_amount", null);
            i++;
        }
        if (config.contains("travel.chameleon")) {
            plugin.getConfig().set("travel.chameleon", null);
        }
        if (config.contains("creation.custom_schematic")) {
            plugin.getConfig().set("creation.custom_schematic", null);
            plugin.getConfig().set("creation.custom_schematic_seed", null);
        }
        if (config.contains("conversions.lowercase_world_names")) {
            plugin.getConfig().set("conversions.lowercase_world_names", null);
        }
        if (config.contains("preferences.respect_factions")) {
            plugin.getConfig().set("preferences.respect_factions", null);
            plugin.getConfig().set("preferences.respect_red_protect", null);
        }
        if (config.contains("switch_resource_packs")) {
            plugin.getConfig().set("switch_resource_packs", null);
        }
        if (config.contains("preferences.wake_bees")) {
            plugin.getConfig().set("preferences.wake_bees", null);
        }
        if (config.contains("preferences.wand")) {
            plugin.getConfig().set("preferences.wand", null);
        }
        if (config.contains("police_box.set_biome")) {
            plugin.getConfig().set("police_box.set_biome", null);
        }
        if (config.contains("creation.sky_biome")) {
            plugin.getConfig().set("creation.sky_biome", null);
        }
        if (config.contains("creation.use_block_stack")) {
            plugin.getConfig().set("creation.use_block_stack", null);
        }
        // boolean values
        for (Map.Entry<String, Boolean> entry : booleanOptions.entrySet()) {
            if (!config.contains(entry.getKey())) {
                plugin.debug("Missing entry: " + entry.getKey());
                plugin.getConfig().set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // int values
        for (Map.Entry<String, Integer> entry : integerOptions.entrySet()) {
            if (!config.contains(entry.getKey())) {
                plugin.debug("Missing entry: " + entry.getKey());
                plugin.getConfig().set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // double values
        for (Map.Entry<String, Double> entry : doubleOptions.entrySet()) {
            if (!config.contains(entry.getKey())) {
                plugin.debug("Missing entry: " + entry.getKey());
                plugin.getConfig().set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // string values
        for (Map.Entry<String, String> entry : stringOptions.entrySet()) {
            if (!config.contains(entry.getKey())) {
                plugin.debug("Missing entry: " + entry.getKey());
                plugin.getConfig().set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // check mysql settings
        if (config.contains("storage.mysql.url")) {
            // mysql://localhost:3306/TARDIS
            String[] firstSplit = config.getString("storage.mysql.url").split(":");
            String host = firstSplit[1].substring(2);
            String[] secondSplit = firstSplit[2].split("/");
            String port = secondSplit[0];
            String database = secondSplit[1];
            plugin.getConfig().set("storage.mysql.host", host);
            plugin.getConfig().set("storage.mysql.port", port);
            plugin.getConfig().set("storage.mysql.database", database);
            plugin.getConfig().set("storage.mysql.url", null);
            i++;
        }
        // transfer / remove difficulty settings
        if (config.contains("preferences.difficulty")) {
            switch (config.getString("preferences.difficulty", "easy").toLowerCase(Locale.ROOT)) {
                case "medium" -> {
                    plugin.getConfig().set("difficulty.crafting", "easy");
                    plugin.getConfig().set("difficulty.circuits", false);
                    plugin.getConfig().set("difficulty.disks", false);
                    plugin.getConfig().set("difficulty.biome_reader", true);
                    plugin.getConfig().set("difficulty.stattenheim_remote", true);
                    plugin.getConfig().set("difficulty.tardis_locator", true);
                }
                case "hard" -> {
                    plugin.getConfig().set("difficulty.crafting", "hard");
                    plugin.getConfig().set("difficulty.circuits", true);
                    plugin.getConfig().set("difficulty.disks", true);
                    plugin.getConfig().set("difficulty.biome_reader", false);
                    plugin.getConfig().set("difficulty.stattenheim_remote", true);
                    plugin.getConfig().set("difficulty.tardis_locator", true);
                }
                default -> {
                    plugin.getConfig().set("difficulty.crafting", "easy");
                    plugin.getConfig().set("difficulty.circuits", false);
                    plugin.getConfig().set("difficulty.disks", false);
                    plugin.getConfig().set("difficulty.biome_reader", true);
                    plugin.getConfig().set("difficulty.stattenheim_remote", false);
                    plugin.getConfig().set("difficulty.tardis_locator", false);
                }
            }
            plugin.getConfig().set("difficulty.disk_in_hand_for_write", true);
            plugin.getConfig().set("difficulty.system_upgrades", true);
            plugin.getConfig().set("preferences.difficulty", null);
            i++;
        }
        // check / transfer dynmap settings
        if (config.contains("preferences.enable_dynmap")) {
            plugin.getConfig().set("modules.mapping", config.getBoolean("preferences.enable_dynmap"));
            plugin.getConfig().set("mapping.provider", "dynmap");
            plugin.getConfig().set("preferences.enable_dynmap", null);
            i++;
        }
        // check / transfer update settings
        if (config.contains("preferences.notify_update")) {
            plugin.getConfig().set("preferences.update.notify", config.getBoolean("preferences.notify_update"));
            plugin.getConfig().set("preferences.notify_update", null);
            i++;
        }
        // check / transfer sonic settings
        if (config.contains("preferences.freeze_cooldown")) {
            plugin.getConfig().set("sonic.freeze_cooldown", config.getInt("preferences.freeze_cooldown"));
            plugin.getConfig().set("sonic.default_model", config.getString("preferences.default_sonic"));
            plugin.getConfig().set("sonic.conversion_radius", config.get("preferences.sonic_radius") != null ? config.get("preferences.sonic_radius") : 1);
            plugin.getConfig().set("preferences.freeze_cooldown", null);
            plugin.getConfig().set("preferences.default_sonic", null);
            plugin.getConfig().set("preferences.sonic_radius", null);
            i++;
        }
        if (config.contains("dynmap.update_period")) {
            plugin.getConfig().set("mapping.provider", "dynmap");
            plugin.getConfig().set("mapping.update_period", plugin.getConfig().getInt("dynmap.update_period"));
            plugin.getConfig().set("mapping.updates_per_tick", plugin.getConfig().getInt("dynmap.updates_per_tick"));
            plugin.getConfig().set("dynmap.update_period", null);
            plugin.getConfig().set("dynmap.updates_per_tick", null);
            i++;
        }
        if (config.contains("dynmap.enabled")) {
            plugin.getConfig().set("modules.mapping", config.getBoolean("dynmap.enabled"));
            plugin.getConfig().set("mapping.provider", "dynmap");
            plugin.getConfig().set("mapping.update_period", plugin.getConfig().getInt("dynmap.update_period"));
            plugin.getConfig().set("mapping.updates_per_tick", plugin.getConfig().getInt("dynmap.updates_per_tick"));
            plugin.getConfig().set("dynmap.enabled", null);
            plugin.getConfig().set("dynmap.update_period", null);
            plugin.getConfig().set("dynmap.updates_per_tick", null);
            i++;
        }
        if (config.contains("blueprints.enabled")) {
            plugin.getConfig().set("modules.blueprints", config.getBoolean("blueprints.enabled"));
            plugin.getConfig().set("blueprints.enabled", null);
            plugin.getConfig().set("blueprints", null);
        }
        // remove handles
        if (config.contains("handles")) {
            plugin.getConfig().set("allow.handles", null);
            plugin.getConfig().set("handles", null);
            i++;
        }
        // remove seed_block_crafting
        if (config.contains("creation.seed_block_crafting")) {
            plugin.getConfig().set("creation.seed_block_crafting", null);
            i++;
        }
        // remove walk_in_tardis
        if (config.contains("preferences.walk_in_tardis")) {
            plugin.getConfig().set("preferences.walk_in_tardis", null);
            i++;
        }
        // switch chemistry to a module
        if (config.contains("allow.chemistry")) {
            boolean tf = config.getBoolean("allow.chemistry");
            if (!config.contains("modules.chemistry") || config.getBoolean("modules.chemistry") != tf) {
                plugin.getConfig().set("modules.chemistry", tf);
            }
            plugin.getConfig().set("allow.chemistry", null);
            i++;
        }
        // add comments
        for (Map.Entry<String, List<String>> entry : comments.entrySet()) {
            plugin.getConfig().setComments(entry.getKey(), entry.getValue());
        }
        if (i > 0) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Added " + i + " new items to config");
        }
        // worlds
        new TARDISWorlds(plugin).doWorlds();
        plugin.saveConfig();
    }
}
