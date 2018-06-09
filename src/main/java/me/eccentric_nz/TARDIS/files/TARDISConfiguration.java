/*
 * Copyright (C) 2018 eccentric_nz
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
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * The Alpha Centauran Table Tennis Club is a club established by the Alpha Centaurans for the play of table tennis. The
 * species is naturally gifted at the game, since they possess six arms. The Doctor is a member of the Club.
 *
 * @author eccentric_nz
 */
public class TARDISConfiguration {

    private final TARDIS plugin;
    private FileConfiguration config = null;
    private FileConfiguration artron_config = null;
    private FileConfiguration blocks_config = null;
    private FileConfiguration rooms_config = null;
    private FileConfiguration signs_config = null;
    private FileConfiguration chameleon_config = null;
    private FileConfiguration planets_config = null;
    private File configFile = null;
    HashMap<String, String> strOptions = new HashMap<>();
    HashMap<String, Integer> intOptions = new HashMap<>();
    HashMap<String, Boolean> boolOptions = new HashMap<>();
    HashMap<String, String> roomStrOptions = new HashMap<>();
    HashMap<String, Integer> roomIntOptions = new HashMap<>();
    HashMap<String, Boolean> roomBoolOptions = new HashMap<>();
    HashMap<String, Boolean> artronBoolOptions = new HashMap<>();
    HashMap<String, String> artronStrOptions = new HashMap<>();
    HashMap<String, Double> artronDoubleOptions = new HashMap<>();
    HashMap<String, Integer> artronIntOptions = new HashMap<>();
    HashMap<String, List<String>> signListOptions = new HashMap<>();
    HashMap<String, String> chameleonOptions = new HashMap<>();
    HashMap<String, List<String>> chameleonListOptions = new HashMap<>();

    public TARDISConfiguration(TARDIS plugin) {
        this.plugin = plugin;
        configFile = new File(plugin.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        artron_config = plugin.getArtronConfig();
        blocks_config = plugin.getBlocksConfig();
        rooms_config = plugin.getRoomsConfig();
        signs_config = plugin.getSigns();
        chameleon_config = plugin.getChameleonGuis();
        planets_config = plugin.getPlanetsConfig();
        // boolean
        boolOptions.put("abandon.enabled", true);
        boolOptions.put("abandon.reduce_count", true);
        boolOptions.put("allow.3d_doors", false);
        boolOptions.put("allow.achievements", true);
        boolOptions.put("allow.all_blocks", false);
        boolOptions.put("allow.autonomous", true);
        boolOptions.put("allow.emergency_npc", true);
        boolOptions.put("allow.external_gravity", false);
        boolOptions.put("allow.guardians", false);
        boolOptions.put("allow.hads", true);
        boolOptions.put("allow.handles", true);
        boolOptions.put("allow.invisibility", true);
        boolOptions.put("allow.mob_farming", true);
        boolOptions.put("allow.perception_filter", true);
        boolOptions.put("allow.player_difficulty", true);
        boolOptions.put("allow.power_down_on_quit", false);
        boolOptions.put("allow.power_down", true);
        boolOptions.put("allow.repair", true);
        boolOptions.put("allow.sfx", true);
        boolOptions.put("allow.spawn_eggs", true);
        boolOptions.put("allow.tp_switch", true);
        boolOptions.put("allow.village_travel", false);
        boolOptions.put("allow.wg_flag_set", true);
        boolOptions.put("allow.zero_room", false);
        boolOptions.put("arch.clear_inv_on_death", false);
        boolOptions.put("arch.enabled", true);
        boolOptions.put("arch.switch_inventory", true);
        boolOptions.put("archive.enabled", true);
        boolOptions.put("circuits.damage", false);
        boolOptions.put("conversions.ars_materials", false);
        boolOptions.put("conversions.condenser_materials", false);
        boolOptions.put("conversions.player_prefs_materials", false);
        boolOptions.put("creation.add_perms", true);
        boolOptions.put("creation.create_worlds_with_perms", false);
        boolOptions.put("creation.create_worlds", false);
        boolOptions.put("creation.custom_schematic", false);
        boolOptions.put("creation.default_world", true);
        boolOptions.put("creation.enable_legacy", true);
        boolOptions.put("creation.keep_night", true);
        boolOptions.put("creation.sky_biome", true);
        boolOptions.put("creation.use_block_stack", false);
        boolOptions.put("debug", false);
        boolOptions.put("growth.return_room_seed", true);
        boolOptions.put("desktop.check_blocks_before_upgrade", false);
        boolOptions.put("growth.rooms_require_blocks", false);
        boolOptions.put("handles.reminders.enabled", true);
        boolOptions.put("junk.enabled", true);
        boolOptions.put("junk.particles", true);
        boolOptions.put("police_box.name_tardis", false);
        boolOptions.put("police_box.set_biome", true);
        boolOptions.put("preferences.nerf_pistons.enabled", false);
        boolOptions.put("preferences.nerf_pistons.only_tardis_worlds", true);
        boolOptions.put("preferences.no_coords", false);
        boolOptions.put("preferences.no_creative_condense", false);
        boolOptions.put("preferences.open_door_policy", false);
        boolOptions.put("preferences.render_entities", false);
        boolOptions.put("preferences.respect_factions", true);
        boolOptions.put("preferences.respect_grief_prevention", true);
        boolOptions.put("preferences.respect_worldborder", true);
        boolOptions.put("preferences.spawn_random_monsters", true);
        boolOptions.put("preferences.strike_lightning", true);
        boolOptions.put("preferences.use_default_condensables", true);
        boolOptions.put("preferences.use_worldguard", true);
        boolOptions.put("preferences.walk_in_tardis", true);
        boolOptions.put("siege.butcher", false);
        boolOptions.put("siege.creeper", false);
        boolOptions.put("siege.enabled", true);
        boolOptions.put("siege.healing", false);
        boolOptions.put("siege.texture", false);
        boolOptions.put("travel.exile", false);
        boolOptions.put("travel.give_key", false);
        boolOptions.put("travel.include_default_world", false);
        boolOptions.put("travel.land_on_water", true);
        boolOptions.put("travel.nether", false);
        boolOptions.put("travel.no_destination_malfunctions", true);
        boolOptions.put("travel.per_world_perms", false);
        boolOptions.put("travel.terminal.redefine", false);
        boolOptions.put("travel.the_end", false);
        // boolean
        roomBoolOptions.put("rooms.ANTIGRAVITY.enabled", true);
        roomBoolOptions.put("rooms.ANTIGRAVITY.user", false);
        roomBoolOptions.put("rooms.AQUARUIM.enabled", true);
        roomBoolOptions.put("rooms.AQUARUIM.user", false);
        roomBoolOptions.put("rooms.ARBORETUM.enabled", true);
        roomBoolOptions.put("rooms.ARBORETUM.user", false);
        roomBoolOptions.put("rooms.BAKER.enabled", true);
        roomBoolOptions.put("rooms.BAKER.user", false);
        roomBoolOptions.put("rooms.BEDROOM.enabled", true);
        roomBoolOptions.put("rooms.BEDROOM.user", false);
        roomBoolOptions.put("rooms.BIRDCAGE.enabled", true);
        roomBoolOptions.put("rooms.BIRDCAGE.user", false);
        roomBoolOptions.put("rooms.EMPTY.enabled", true);
        roomBoolOptions.put("rooms.EMPTY.user", false);
        roomBoolOptions.put("rooms.FARM.enabled", true);
        roomBoolOptions.put("rooms.FARM.user", false);
        roomBoolOptions.put("rooms.GRAVITY.enabled", true);
        roomBoolOptions.put("rooms.GRAVITY.user", false);
        roomBoolOptions.put("rooms.GREENHOUSE.enabled", true);
        roomBoolOptions.put("rooms.GREENHOUSE.user", false);
        roomBoolOptions.put("rooms.HARMONY.enabled", true);
        roomBoolOptions.put("rooms.HARMONY.user", false);
        roomBoolOptions.put("rooms.HUTCH.enabled", true);
        roomBoolOptions.put("rooms.HUTCH.user", false);
        roomBoolOptions.put("rooms.IGLOO.enabled", true);
        roomBoolOptions.put("rooms.IGLOO.user", false);
        roomBoolOptions.put("rooms.KITCHEN.enabled", true);
        roomBoolOptions.put("rooms.KITCHEN.user", false);
        roomBoolOptions.put("rooms.LAZARUS.enabled", true);
        roomBoolOptions.put("rooms.LAZARUS.user", false);
        roomBoolOptions.put("rooms.LIBRARY.enabled", true);
        roomBoolOptions.put("rooms.LIBRARY.user", false);
        roomBoolOptions.put("rooms.MUSHROOM.enabled", true);
        roomBoolOptions.put("rooms.MUSHROOM.user", false);
        roomBoolOptions.put("rooms.PASSAGE.enabled", true);
        roomBoolOptions.put("rooms.PASSAGE.user", false);
        roomBoolOptions.put("rooms.POOL.enabled", true);
        roomBoolOptions.put("rooms.POOL.user", false);
        roomBoolOptions.put("rooms.RAIL.enabled", true);
        roomBoolOptions.put("rooms.RAIL.user", false);
        roomBoolOptions.put("rooms.RENDERER.enabled", true);
        roomBoolOptions.put("rooms.RENDERER.user", false);
        roomBoolOptions.put("rooms.SHELL.enabled", true);
        roomBoolOptions.put("rooms.SHELL.user", false);
        roomBoolOptions.put("rooms.SMELTER.enabled", true);
        roomBoolOptions.put("rooms.SMELTER.user", false);
        roomBoolOptions.put("rooms.STABLE.enabled", true);
        roomBoolOptions.put("rooms.STABLE.user", false);
        roomBoolOptions.put("rooms.STALL.enabled", true);
        roomBoolOptions.put("rooms.STALL.user", false);
        roomBoolOptions.put("rooms.TRENZALORE.enabled", true);
        roomBoolOptions.put("rooms.TRENZALORE.user", false);
        roomBoolOptions.put("rooms.VAULT.enabled", true);
        roomBoolOptions.put("rooms.VAULT.user", false);
        roomBoolOptions.put("rooms.VILLAGE.enabled", true);
        roomBoolOptions.put("rooms.VILLAGE.user", false);
        roomBoolOptions.put("rooms.WOOD.enabled", true);
        roomBoolOptions.put("rooms.WOOD.user", false);
        roomBoolOptions.put("rooms.WORKSHOP.enabled", true);
        roomBoolOptions.put("rooms.WORKSHOP.user", false);
        roomBoolOptions.put("rooms.ZERO.enabled", true);
        roomBoolOptions.put("rooms.ZERO.user", false);
        // boolean
        artronBoolOptions.put("artron_furnace.particles", false);
        artronBoolOptions.put("artron_furnace.set_biome", true);
        // double
        artronDoubleOptions.put("artron_furnace.burn_time", 0.5);
        artronDoubleOptions.put("artron_furnace.cook_time", 0.5);
        // integer
        artronIntOptions.put("artron_furnace.burn_limit", 100000);
        artronIntOptions.put("autonomous", 100);
        artronIntOptions.put("backdoor", 100);
        artronIntOptions.put("comehere", 400);
        artronIntOptions.put("creeper_recharge", 150);
        artronIntOptions.put("full_charge", 5000);
        artronIntOptions.put("hide", 500);
        artronIntOptions.put("jettison", 75);
        artronIntOptions.put("just_wall_floor", 50);
        artronIntOptions.put("lightning_recharge", 300);
        artronIntOptions.put("nether_min", 4250);
        artronIntOptions.put("player", 25);
        artronIntOptions.put("random", 75);
        artronIntOptions.put("random_circuit", 150);
        artronIntOptions.put("recharge_distance", 20);
        artronIntOptions.put("render", 250);
        artronIntOptions.put("siege_creeper", 150);
        artronIntOptions.put("siege_deplete", 100);
        artronIntOptions.put("siege_ticks", 1500);
        artronIntOptions.put("siege_transfer", 10);
        artronIntOptions.put("sonic_generator.standard", 10);
        artronIntOptions.put("sonic_generator.bio", 10);
        artronIntOptions.put("sonic_generator.diamond", 10);
        artronIntOptions.put("sonic_generator.emerald", 10);
        artronIntOptions.put("sonic_generator.redstone", 10);
        artronIntOptions.put("sonic_generator.painter", 10);
        artronIntOptions.put("sonic_generator.ignite", 10);
        artronIntOptions.put("shell", 500);
        artronIntOptions.put("standby", 5);
        artronIntOptions.put("standby_time", 6000);
        artronIntOptions.put("the_end_min", 5500);
        artronIntOptions.put("travel", 100);
        artronIntOptions.put("zero", 250);
        artronIntOptions.put("upgrades.ars", 5000);
        artronIntOptions.put("upgrades.bigger", 7500);
        artronIntOptions.put("upgrades.budget", 5000);
        artronIntOptions.put("upgrades.coral", 8000);
        artronIntOptions.put("upgrades.deluxe", 10000);
        artronIntOptions.put("upgrades.eleventh", 10000);
        artronIntOptions.put("upgrades.ender", 5000);
        artronIntOptions.put("upgrades.legacy_bigger", 7500);
        artronIntOptions.put("upgrades.legacy_budget", 5000);
        artronIntOptions.put("upgrades.legacy_deluxe", 10000);
        artronIntOptions.put("upgrades.legacy_eleventh", 10000);
        artronIntOptions.put("upgrades.legacy_redstone", 8000);
        artronIntOptions.put("upgrades.master", 10000);
        artronIntOptions.put("upgrades.plank", 5000);
        artronIntOptions.put("upgrades.pyramid", 5000);
        artronIntOptions.put("upgrades.redstone", 7500);
        artronIntOptions.put("upgrades.steampunk", 5000);
        artronIntOptions.put("upgrades.tom", 5000);
        artronIntOptions.put("upgrades.twelfth", 7500);
        artronIntOptions.put("upgrades.war", 5000);
        artronIntOptions.put("upgrades.template.small", 1666);
        artronIntOptions.put("upgrades.template.medium", 2500);
        artronIntOptions.put("upgrades.template.tall", 3333);
        artronIntOptions.put("upgrades.custom", 10000);
        artronIntOptions.put("upgrades.archive.small", 5000);
        artronIntOptions.put("upgrades.archive.medium", 7500);
        artronIntOptions.put("upgrades.archive.tall", 10000);
        // integer
        intOptions.put("arch.min_time", 20);
        intOptions.put("archive.limit", 3);
        intOptions.put("circuits.uses.ars", 20);
        intOptions.put("circuits.uses.chameleon", 25);
        intOptions.put("circuits.uses.input", 50);
        intOptions.put("circuits.uses.invisibility", 5);
        intOptions.put("circuits.uses.materialisation", 50);
        intOptions.put("circuits.uses.memory", 20);
        intOptions.put("circuits.uses.randomiser", 50);
        intOptions.put("circuits.uses.scanner", 20);
        intOptions.put("circuits.uses.temporal", 20);
        intOptions.put("creation.border_radius", 256);
        intOptions.put("creation.count", 0);
        intOptions.put("creation.inventory_group", 0);
        intOptions.put("creation.tips_limit", 400);
        intOptions.put("desktop.block_change_percent", 25);
        intOptions.put("growth.ars_limit", 1);
        intOptions.put("growth.gravity_max_distance", 15);
        intOptions.put("growth.gravity_max_velocity", 5);
        intOptions.put("growth.room_speed", 4);
        intOptions.put("growth.rooms_condenser_percent", 100);
        intOptions.put("handles.reminders.schedule", 1200);
        intOptions.put("junk.return", -1);
        intOptions.put("police_box.confirm_timeout", 15);
        intOptions.put("police_box.rebuild_cooldown", 10000);
        intOptions.put("preferences.freeze_cooldown", 60);
        intOptions.put("preferences.hads_damage", 10);
        intOptions.put("preferences.hads_distance", 10);
        intOptions.put("preferences.heal_speed", 200);
        intOptions.put("preferences.malfunction_end", 3);
        intOptions.put("preferences.malfunction_nether", 3);
        intOptions.put("preferences.malfunction", 3);
        intOptions.put("preferences.sfx_volume", 10);
        intOptions.put("preferences.spawn_limit", 10);
        intOptions.put("siege.breeding", 0);
        intOptions.put("siege.growth", 0);
        intOptions.put("travel.grace_period", 10);
        intOptions.put("travel.manual_flight_delay", 60);
        intOptions.put("travel.max_distance", 29999983);
        intOptions.put("travel.random_attempts", 30);
        intOptions.put("travel.random_circuit.x", 5000);
        intOptions.put("travel.random_circuit.z", 5000);
        intOptions.put("travel.terminal_step", 1);
        intOptions.put("travel.timeout_height", 135);
        intOptions.put("travel.timeout", 5);
        intOptions.put("travel.tp_radius", 500);
        // integer
        roomIntOptions.put("rooms.ANTIGRAVITY.cost", 625);
        roomIntOptions.put("rooms.ANTIGRAVITY.offset", -4);
        roomIntOptions.put("rooms.AQUARUIM.cost", 450);
        roomIntOptions.put("rooms.AQUARUIM.offset", -4);
        roomIntOptions.put("rooms.ARBORETUM.cost", 325);
        roomIntOptions.put("rooms.ARBORETUM.offset", -4);
        roomIntOptions.put("rooms.BAKER.cost", 350);
        roomIntOptions.put("rooms.BAKER.offset", -4);
        roomIntOptions.put("rooms.BEDROOM.cost", 475);
        roomIntOptions.put("rooms.BEDROOM.offset", -4);
        roomIntOptions.put("rooms.BIRDCAGE.cost", 350);
        roomIntOptions.put("rooms.BIRDCAGE.offset", -4);
        roomIntOptions.put("rooms.EMPTY.cost", 250);
        roomIntOptions.put("rooms.EMPTY.offset", -4);
        roomIntOptions.put("rooms.FARM.cost", 350);
        roomIntOptions.put("rooms.FARM.offset", -4);
        roomIntOptions.put("rooms.GRAVITY.cost", 625);
        roomIntOptions.put("rooms.GRAVITY.offset", -20);
        roomIntOptions.put("rooms.GREENHOUSE.cost", 450);
        roomIntOptions.put("rooms.GREENHOUSE.offset", -4);
        roomIntOptions.put("rooms.HARMONY.cost", 450);
        roomIntOptions.put("rooms.HARMONY.offset", -4);
        roomIntOptions.put("rooms.HUTCH.cost", 450);
        roomIntOptions.put("rooms.HUTCH.offset", -4);
        roomIntOptions.put("rooms.IGLOO.cost", 650);
        roomIntOptions.put("rooms.IGLOO.offset", -4);
        roomIntOptions.put("rooms.KITCHEN.cost", 450);
        roomIntOptions.put("rooms.KITCHEN.offset", -4);
        roomIntOptions.put("rooms.LAZARUS.cost", 750);
        roomIntOptions.put("rooms.LAZARUS.offset", -4);
        roomIntOptions.put("rooms.LIBRARY.cost", 550);
        roomIntOptions.put("rooms.LIBRARY.offset", -4);
        roomIntOptions.put("rooms.MUSHROOM.cost", 350);
        roomIntOptions.put("rooms.MUSHROOM.offset", -4);
        roomIntOptions.put("rooms.PASSAGE.cost", 200);
        roomIntOptions.put("rooms.PASSAGE.offset", -4);
        roomIntOptions.put("rooms.POOL.cost", 450);
        roomIntOptions.put("rooms.POOL.offset", -4);
        roomIntOptions.put("rooms.RAIL.cost", 650);
        roomIntOptions.put("rooms.RAIL.offset", -4);
        roomIntOptions.put("rooms.RENDERER.cost", 650);
        roomIntOptions.put("rooms.RENDERER.offset", -4);
        roomIntOptions.put("rooms.SHELL.cost", 550);
        roomIntOptions.put("rooms.SHELL.offset", -4);
        roomIntOptions.put("rooms.SMELTER.cost", 750);
        roomIntOptions.put("rooms.SMELTER.offset", -4);
        roomIntOptions.put("rooms.STABLE.cost", 350);
        roomIntOptions.put("rooms.STABLE.offset", -4);
        roomIntOptions.put("rooms.STALL.cost", 350);
        roomIntOptions.put("rooms.STALL.offset", -4);
        roomIntOptions.put("rooms.TRENZALORE.cost", 550);
        roomIntOptions.put("rooms.TRENZALORE.offset", -4);
        roomIntOptions.put("rooms.VAULT.cost", 350);
        roomIntOptions.put("rooms.VAULT.offset", -4);
        roomIntOptions.put("rooms.VILLAGE.cost", 550);
        roomIntOptions.put("rooms.VILLAGE.offset", -4);
        roomIntOptions.put("rooms.WOOD.cost", 350);
        roomIntOptions.put("rooms.WOOD.offset", -4);
        roomIntOptions.put("rooms.WORKSHOP.cost", 400);
        roomIntOptions.put("rooms.WORKSHOP.offset", -4);
        roomIntOptions.put("rooms.ZERO.cost", 650);
        roomIntOptions.put("rooms.ZERO.offset", -4);
        // string
        strOptions.put("creation.area", "none");
        strOptions.put("creation.custom_schematic_seed", "OBSIDIAN");
        strOptions.put("creation.default_world_name", "TARDIS_TimeVortex");
        strOptions.put("creation.gamemode", "survival");
        strOptions.put("creation.use_clay", "WOOL");
        strOptions.put("handles.prefix", "Hey Handles");
        strOptions.put("police_box.default_preset", "FACTORY");
        strOptions.put("police_box.sign_colour", "WHITE");
        strOptions.put("preferences.default_key", "eleventh");
        strOptions.put("preferences.default_sonic", "eleventh");
        strOptions.put("preferences.difficulty", "hard");
        strOptions.put("preferences.key", "GOLD_NUGGET");
        strOptions.put("preferences.language", "en");
        strOptions.put("preferences.respect_towny", "nation");
        strOptions.put("preferences.respect_worldguard", "build");
        strOptions.put("preferences.vortex_fall", "kill");
        strOptions.put("preferences.wand", "BONE");
        strOptions.put("storage.database", "sqlite");
        strOptions.put("storage.mysql.url", "mysql://localhost:3306/TARDIS");
        strOptions.put("storage.mysql.user", "bukkit");
        strOptions.put("storage.mysql.password", "mysecurepassword");
        strOptions.put("storage.mysql.prefix", "");
        strOptions.put("travel.terminal.nether", "world");
        strOptions.put("travel.terminal.the_end", "world");
        // string
        artronStrOptions.put("jettison_seed", "TNT");
        artronStrOptions.put("full_charge_item", "NETHER_STAR");
        // string
        roomStrOptions.put("rooms.AQUARUIM.seed", "TUBE_CORAL_BLOCK");
        roomStrOptions.put("rooms.ARBORETUM.seed", "OAK_LEAVES");
        roomStrOptions.put("rooms.BAKER.seed", "END_STONE");
        roomStrOptions.put("rooms.BEDROOM.seed", "GLOWSTONE");
        roomStrOptions.put("rooms.EMPTY.seed", "GLASS");
        roomStrOptions.put("rooms.FARM.seed", "DIRT");
        roomStrOptions.put("rooms.GRAVITY.seed", "MOSSY_COBBLESTONE");
        roomStrOptions.put("rooms.ANTIGRAVITY.seed", "SANDSTONE");
        roomStrOptions.put("rooms.GREENHOUSE.seed", "MELON_BLOCK");
        roomStrOptions.put("rooms.HARMONY.seed", "BRICK_STAIRS");
        roomStrOptions.put("rooms.HUTCH.seed", "ACACIA_LOG");
        roomStrOptions.put("rooms.IGLOO.seed", "PACKED_ICE");
        roomStrOptions.put("rooms.KITCHEN.seed", "PUMPKIN");
        roomStrOptions.put("rooms.LAZARUS.seed", "FURNACE");
        roomStrOptions.put("rooms.LIBRARY.seed", "ENCHANTMENT_TABLE");
        roomStrOptions.put("rooms.MUSHROOM.seed", "GRAVEL");
        roomStrOptions.put("rooms.PASSAGE.seed", "CLAY");
        roomStrOptions.put("rooms.POOL.seed", "SNOW_BLOCK");
        roomStrOptions.put("rooms.RAIL.seed", "HOPPER");
        roomStrOptions.put("rooms.RENDERER.seed", "TERRACOTTA");
        roomStrOptions.put("rooms.SHELL.seed", "TUBE_CORAL_BLOCK");
        roomStrOptions.put("rooms.SMELTER.seed", "CHEST");
        roomStrOptions.put("rooms.STABLE.seed", "HAY_BLOCK");
        roomStrOptions.put("rooms.STALL.seed", "BROWN_GLAZED_TERRACOTTA");
        roomStrOptions.put("rooms.BIRDCAGE.seed", "YELLOW_GLAZED_TERRACOTTA");
        roomStrOptions.put("rooms.TRENZALORE.seed", "BRICK");
        roomStrOptions.put("rooms.VAULT.seed", "DISPENSER");
        roomStrOptions.put("rooms.VILLAGE.seed", "LOG");
        roomStrOptions.put("rooms.WOOD.seed", "WOOD");
        roomStrOptions.put("rooms.WORKSHOP.seed", "WORKBENCH");
        roomStrOptions.put("rooms.ZERO.seed", "WOOD_BUTTON");
        signListOptions.put("junk", Arrays.asList("Destination"));
        chameleonListOptions.put("ADAPT_LORE", Arrays.asList("The Chameleon Circuit", "will choose a preset", "that blends in with", "the environment.", "Use BIOME or BLOCK mode."));
        chameleonListOptions.put("APPLY_LORE", Arrays.asList("Rebuild the TARDIS", "exterior with the", "current settings."));
        chameleonListOptions.put("CONSTRUCT_LORE", Arrays.asList("Build your own", "Chameleon preset."));
        chameleonListOptions.put("DISABLED_LORE", Arrays.asList("Disable the Chameleon", "Circuit and revert", "to the FACTORY preset."));
        chameleonListOptions.put("INVISIBLE_LORE", Arrays.asList("Engages the TARDIS", "Invisiblity Circuit."));
        chameleonListOptions.put("SHORT_LORE", Arrays.asList("Make the Chameleon", "Circuit malfunction and", "always choose the", "same appearance."));
        chameleonOptions.put("ADAPT", "Adaptive");
        chameleonOptions.put("APPLY", "Apply");
        chameleonOptions.put("BACK_CHAM_OPTS", "Back to Chameleon Circuit");
        chameleonOptions.put("CONSTRUCT", "Construct");
        chameleonOptions.put("DISABLED", "DISABLED");
        chameleonOptions.put("INVISIBLE", "Invisible");
        chameleonOptions.put("SHORT", "Shorted out");
        chameleonOptions.put("USE_PREV", "Use last saved construct");
    }

    /**
     * Checks that the config file contains all the required entries. If entries are missing, then they are added with
     * default values. Also checks that all current server worlds are added to the config, and any deleted worlds are
     * removed.
     */
    public void checkConfig() {
        int i = 0;
        // boolean values
        for (Map.Entry<String, Boolean> entry : boolOptions.entrySet()) {
            if (!config.contains(entry.getKey())) {
                plugin.getConfig().set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // int values
        for (Map.Entry<String, Integer> entry : intOptions.entrySet()) {
            if (!config.contains(entry.getKey())) {
                plugin.getConfig().set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // string values
        for (Map.Entry<String, String> entry : strOptions.entrySet()) {
            if (!config.contains(entry.getKey())) {
                plugin.getConfig().set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        if (i > 0) {
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to config");
        }
        // worlds
        doWorlds();
        checkArtronConfig();
        checkBlocksConfig();
        checkRoomsConfig();
        checkSignsConfig();
        checkChameleonConfig();
        checkPlanetsConfig();
        plugin.saveConfig();
    }

    public void doWorlds() {
        List<World> worlds = plugin.getServer().getWorlds();
        worlds.forEach((w) -> {
            String worldname = "worlds." + w.getName();
            if (!config.contains(worldname)) {
                plugin.getConfig().set(worldname, true);
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Added '" + w.getName() + "' to config. To exclude this world run: /tardisadmin exclude " + w.getName());
            }
        });
        plugin.saveConfig();
        // now remove worlds that may have been deleted
        Set<String> cWorlds = plugin.getConfig().getConfigurationSection("worlds").getKeys(true);
        cWorlds.forEach((cw) -> {
            if (plugin.getServer().getWorld(cw) == null) {
                plugin.getConfig().set("worlds." + cw, null);
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Removed '" + cw + " from config.yml");
                // remove records from database that may contain
                // the removed world
                plugin.getCleanUpWorlds().add(cw);
            }
        });
    }

    private void checkRoomsConfig() {
        int i = 0;
        // boolean values
        for (Map.Entry<String, Boolean> entry : roomBoolOptions.entrySet()) {
            if (!rooms_config.contains(entry.getKey())) {
                rooms_config.set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // int values
        for (Map.Entry<String, Integer> entry : roomIntOptions.entrySet()) {
            if (!rooms_config.contains(entry.getKey()) || (entry.getKey().equals("rooms.RAIL.offset")) && rooms_config.getInt("rooms.RAIL.offset") == -2) {
                rooms_config.set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // string values
        for (Map.Entry<String, String> entry : roomStrOptions.entrySet()) {
            if (!rooms_config.contains(entry.getKey())) {
                rooms_config.set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        try {
            rooms_config.save(new File(plugin.getDataFolder(), "rooms.yml"));
            if (i > 0) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to rooms.yml");
            }
        } catch (IOException io) {
            plugin.debug("Could not save rooms.yml, " + io.getMessage());
        }
    }

    private void checkBlocksConfig() {
        int i = 0;
        if (!blocks_config.contains("version")) {
            List<String> CHAM_BLOCKS = Arrays.asList("STONE", "DIRT", "COBBLESTONE", "OAK_PLANKS", "BIRCH_PLANKS", "SPRUCE_PLANKS", "JUNGLE_PLANKS", "ACACIA_PLANKS", "DARK_OAK_PLANKS", "GOLD_ORE", "IRON_ORE", "COAL_ORE", "OAK_LOG", "BIRCH_LOG", "SPRUCE_LOG", "JUNGLE_LOG", "ACACIA_LOG", "DARK_OAK_LOG", "OAK_LEAVES", "BIRCH_LEAVES", "SPRUCE_LEAVES", "JUNGLE_LEAVES", "ACACIA_LEAVES", "DARK_OAK_LEAVES", "SPONGE", "GLASS", "LAPIS_ORE", "LAPIS_BLOCK", "SANDSTONE", "NOTE_BLOCK", "WHITE_WOOL", "ORANGE_WOOL", "MAGENTA_WOOL", "LIGHT_BLUE_WOOL", "YELLOW_WOOL", "LIME_WOOL", "PINK_WOOL", "GRAY_WOOL", "LIGHT_GRAY_WOOL", "CYAN_WOOL", "PURPLE_WOOL", "BLUE_WOOL", "BROWN_WOOL", "GREEN_WOOL", "RED_WOOL", "BLACK_WOOL", "GOLD_BLOCK", "IRON_BLOCK", "BRICK", "TNT", "BOOKSHELF", "MOSSY_COBBLESTONE", "OBSIDIAN", "DIAMOND_ORE", "DIAMOND_BLOCK", "CRAFTING_TABLE", "REDSTONE_ORE", "ICE", "SNOW_BLOCK", "CLAY", "JUKEBOX", "PUMPKIN", "NETHERRACK", "SOUL_SAND", "GLOWSTONE", "JACK_O_LANTERN", "WHITE_STAINED_GLASS", "ORANGE_STAINED_GLASS", "MAGENTA_STAINED_GLASS", "LIGHT_BLUE_STAINED_GLASS", "YELLOW_STAINED_GLASS", "LIME_STAINED_GLASS", "PINK_STAINED_GLASS", "GRAY_STAINED_GLASS", "LIGHT_GRAY_STAINED_GLASS", "CYAN_STAINED_GLASS", "PURPLE_STAINED_GLASS", "BLUE_STAINED_GLASS", "BROWN_STAINED_GLASS", "GREEN_STAINED_GLASS", "RED_STAINED_GLASS", "BLACK_STAINED_GLASS", "STONE_BRICKS", "BROWN_MUSHROOM_BLOCK", "RED_MUSHROOM_BLOCK", "MELON_BLOCK", "MYCELIUM", "NETHER_BRICKS", "END_STONE", "REDSTONE_LAMP", "EMERALD_ORE", "EMERALD_BLOCK", "QUARTZ_BLOCK", "WHITE_TERRACOTTA", "ORANGE_TERRACOTTA", "MAGENTA_TERRACOTTA", "LIGHT_BLUE_TERRACOTTA", "YELLOW_TERRACOTTA", "LIME_TERRACOTTA", "PINK_TERRACOTTA", "GRAY_TERRACOTTA", "LIGHT_GRAY_TERRACOTTA", "CYAN_TERRACOTTA", "PURPLE_TERRACOTTA", "BLUE_TERRACOTTA", "BROWN_TERRACOTTA", "GREEN_TERRACOTTA", "RED_TERRACOTTA", "BLACK_TERRACOTTA", "SLIME_BLOCK", "BARRIER", "PRISMARINE", "HAY_BLOCK", "WHITE_CARPET", "ORANGE_CARPET", "MAGENTA_CARPET", "LIGHT_BLUE_CARPET", "YELLOW_CARPET", "LIME_CARPET", "PINK_CARPET", "GRAY_CARPET", "LIGHT_GRAY_CARPET", "CYAN_CARPET", "PURPLE_CARPET", "BLUE_CARPET", "BROWN_CARPET", "GREEN_CARPET", "RED_CARPET", "BLACK_CARPET", "TERRACOTTA", "COAL_BLOCK", "PACKED_ICE", "RED_SANDSTONE", "PURPUR_BLOCK", "PURPUR_PILLAR", "END_STONE_BRICKS", "WHITE_GLAZED_TERRACOTTA", "ORANGE_GLAZED_TERRACOTTA", "MAGENTA_GLAZED_TERRACOTTA", "LIGHT_BLUE_GLAZED_TERRACOTTA", "YELLOW_GLAZED_TERRACOTTA", "LIME_GLAZED_TERRACOTTA", "PINK_GLAZED_TERRACOTTA", "GRAY_GLAZED_TERRACOTTA", "LIGHT_GRAY_GLAZED_TERRACOTTA", "CYAN_GLAZED_TERRACOTTA", "PURPLE_GLAZED_TERRACOTTA", "BLUE_GLAZED_TERRACOTTA", "BROWN_GLAZED_TERRACOTTA", "GREEN_GLAZED_TERRACOTTA", "RED_GLAZED_TERRACOTTA", "BLACK_GLAZED_TERRACOTTA", "WHITE_CONCRETE", "ORANGE_CONCRETE", "MAGENTA_CONCRETE", "LIGHT_BLUE_CONCRETE", "YELLOW_CONCRETE", "LIME_CONCRETE", "PINK_CONCRETE", "GRAY_CONCRETE", "LIGHT_GRAY_CONCRETE", "CYAN_CONCRETE", "PURPLE_CONCRETE", "BLUE_CONCRETE", "BROWN_CONCRETE", "GREEN_CONCRETE", "RED_CONCRETE", "BLACK_CONCRETE");
            blocks_config.set("chameleon_blocks", CHAM_BLOCKS);
            List<String> UNDER_BLOCKS = Arrays.asList("AIR", "OAK_LEAVES", "BIRCH_LEAVES", "SPRUCE_LEAVES", "JUNGLE_LEAVES", "ACACIA_LEAVES", "DARK_OAK_LEAVES", "WATER", "LAVA", "OAK_SAPLING", "BIRCH_SAPLING", "SPRUCE_SAPLING", "JUNGLE_SAPLING", "ACACIA_SAPLING", "DARK_OAK_SAPLING", "GLASS", "WHITE_BED", "ORANGE_BED", "MAGENTA_BED", "LIGHT_BLUE_BED", "YELLOW_BED", "LIME_BED", "PINK_BED", "GRAY_BED", "LIGHT_GRAY_BED", "CYAN_BED", "PURPLE_BED", "BLUE_BED", "BROWN_BED", "GREEN_BED", "RED_BED", "BLACK_BED", "POWERED_RAIL", "DETECTOR_RAIL", "COBWEB", "GRASS", "DEAD_BUSH", "PISTON", "STICKY_PISTON", "PISTON_HEAD", "ALLIUM", "AZURE_BLUET", "BLUE_ORCHID", "FERN", "LARGE_FERN", "LILAC", "ORANGE_TULIP", "OXEYE_DAISY", "PEONY", "PINK_TULIP", "POPPY", "RED_TULIP", "ROSE_BUSH", "SUNFLOWER", "TALL_GRASS", "WHITE_TULIP", "RED_MUSHROOM", "BROWN_MUSHROOM", "TNT", "TORCH", "FIRE", "OAK_STAIRS", "BIRCH_STAIRS", "SPRUCE_STAIRS", "JUNGLE_STAIRS", "ACACIA_STAIRS", "DARK_OAK_STAIRS", "CHEST", "REDSTONE_WIRE", "FARMLAND", "FURNACE", "SIGN", "OAK_DOOR", "BIRCH_DOOR", "SPRUCE_DOOR", "JUNGLE_DOOR", "ACACIA_DOOR", "DARK_OAK_DOOR", "LADDER", "RAIL", "COBBLESTONE_STAIRS", "WALL_SIGN", "LEVER", "STONE_PRESSURE_PLATE", "HEAVY_WEIGHTED_PRESSURE_PLATE", "LIGHT_WEIGHTED_PRESSURE_PLATE", "IRON_DOOR", "OAK_PRESSURE_PLATE", "BIRCH_PRESSURE_PLATE", "SPRUCE_PRESSURE_PLATE", "JUNGLE_PRESSURE_PLATE", "ACACIA_PRESSURE_PLATE", "DARK_OAK_PRESSURE_PLATE", "REDSTONE_TORCH", "STONE_BUTTON", "SNOW", "ICE", "CACTUS", "SUGAR_CANE", "OAK_FENCE", "BIRCH_FENCE", "SPRUCE_FENCE", "JUNGLE_FENCE", "ACACIA_FENCE", "DARK_OAK_FENCE", "GLOWSTONE", "CAKE", "REPEATER", "OAK_TRAPDOOR", "BIRCH_TRAPDOOR", "SPRUCE_TRAPDOOR", "JUNGLE_TRAPDOOR", "ACACIA_TRAPDOOR", "DARK_OAK_TRAPDOOR", "IRON_BARS", "GLASS_PANE", "PUMPKIN_STEM", "MELON_STEM", "VINE", "OAK_FENCE_GATE", "BIRCH_FENCE_GATE", "SPRUCE_FENCE_GATE", "JUNGLE_FENCE_GATE", "ACACIA_FENCE_GATE", "DARK_OAK_FENCE_GATE", "BRICK_STAIRS", "STONE_BRICK_STAIRS", "LILY_PAD", "NETHER_BRICK_FENCE", "NETHER_BRICK_STAIRS", "NETHER_WART", "ENCHANTING_TABLE", "BREWING_STAND", "CAULDRON", "END_PORTAL", "END_PORTAL_FRAME", "DRAGON_EGG", "OAK_SLAB", "BIRCH_SLAB", "SPRUCE_SLAB", "JUNGLE_SLAB", "ACACIA_SLAB", "DARK_OAK_SLAB", "SANDSTONE_STAIRS", "ENDER_CHEST", "TRIPWIRE_HOOK", "TRIPWIRE", "IRON_TRAPDOOR", "WHITE_CARPET", "ORANGE_CARPET", "MAGENTA_CARPET", "LIGHT_BLUE_CARPET", "YELLOW_CARPET", "LIME_CARPET", "PINK_CARPET", "GRAY_CARPET", "LIGHT_GRAY_CARPET", "CYAN_CARPET", "PURPLE_CARPET", "BLUE_CARPET", "BROWN_CARPET", "GREEN_CARPET", "RED_CARPET", "BLACK_CARPET", "WHITE_BANNER", "ORANGE_BANNER", "MAGENTA_BANNER", "LIGHT_BLUE_BANNER", "YELLOW_BANNER", "LIME_BANNER", "PINK_BANNER", "GRAY_BANNER", "LIGHT_GRAY_BANNER", "CYAN_BANNER", "PURPLE_BANNER", "BLUE_BANNER", "BROWN_BANNER", "GREEN_BANNER", "RED_BANNER", "BLACK_BANNER", "WHITE_WALL_BANNER", "ORANGE_WALL_BANNER", "MAGENTA_WALL_BANNER", "LIGHT_BLUE_WALL_BANNER", "YELLOW_WALL_BANNER", "LIME_WALL_BANNER", "PINK_WALL_BANNER", "GRAY_WALL_BANNER", "LIGHT_GRAY_WALL_BANNER", "CYAN_WALL_BANNER", "PURPLE_WALL_BANNER", "BLUE_WALL_BANNER", "BROWN_WALL_BANNER", "GREEN_WALL_BANNER", "RED_WALL_BANNER", "BLACK_WALL_BANNER", "DAYLIGHT_DETECTOR", "RED_SANDSTONE_STAIRS", "END_ROD", "CHORUS_PLANT", "CHORUS_FLOWER", "PURPUR_STAIRS", "PURPUR_SLAB", "BEETROOTS", "GRASS_PATH", "FROSTED_ICE", "STRUCTURE_VOID", "WHITE_SHULKER_BOX", "ORANGE_SHULKER_BOX", "MAGENTA_SHULKER_BOX", "LIGHT_BLUE_SHULKER_BOX", "YELLOW_SHULKER_BOX", "LIME_SHULKER_BOX", "PINK_SHULKER_BOX", "GRAY_SHULKER_BOX", "LIGHT_GRAY_SHULKER_BOX", "CYAN_SHULKER_BOX", "PURPLE_SHULKER_BOX", "BLUE_SHULKER_BOX", "BROWN_SHULKER_BOX", "GREEN_SHULKER_BOX", "RED_SHULKER_BOX", "BLACK_SHULKER_BOX", "STRUCTURE_BLOCK");
            blocks_config.set("under_door_blocks", UNDER_BLOCKS);
            List<String> MIDDLE_BLOCKS = Arrays.asList("LAPIS_BLOCK", "STONE", "COBBLESTONE", "MOSSY_COBBLESTONE", "DIRT", "OAK_PLANKS", "BIRCH_PLANKS", "SPRUCE_PLANKS", "JUNGLE_PLANKS", "ACACIA_PLANKS", "DARK_OAK_PLANKS", "OAK_LOG", "BIRCH_LOG", "SPRUCE_LOG", "JUNGLE_LOG", "ACACIA_LOG", "DARK_OAK_LOG", "SANDSTONE", "WHITE_WOOL", "ORANGE_WOOL", "MAGENTA_WOOL", "LIGHT_BLUE_WOOL", "YELLOW_WOOL", "LIME_WOOL", "PINK_WOOL", "GRAY_WOOL", "LIGHT_GRAY_WOOL", "CYAN_WOOL", "PURPLE_WOOL", "BLUE_WOOL", "BROWN_WOOL", "GREEN_WOOL", "RED_WOOL", "BLACK_WOOL", "BRICKS", "NETHERRACK", "SOUL_SAND", "STONE_BRICKS", "BROWN_MUSHROOM_BLOCK", "RED_MUSHROOM_BLOCK", "END_STONE", "QUARTZ_BLOCK", "CLAY", "WHITE_TERRACOTTA", "ORANGE_TERRACOTTA", "MAGENTA_TERRACOTTA", "LIGHT_BLUE_TERRACOTTA", "YELLOW_TERRACOTTA", "LIME_TERRACOTTA", "PINK_TERRACOTTA", "GRAY_TERRACOTTA", "LIGHT_GRAY_TERRACOTTA", "CYAN_TERRACOTTA", "PURPLE_TERRACOTTA", "BLUE_TERRACOTTA", "BROWN_TERRACOTTA", "GREEN_TERRACOTTA", "RED_TERRACOTTA", "BLACK_TERRACOTTA", "HAY_BLOCK", "TERRACOTTA", "PACKED_ICE");
            blocks_config.set("tardis_blocks", MIDDLE_BLOCKS);
            blocks_config.set("version", 4);
            i += 4;
        }
        try {
            blocks_config.save(new File(plugin.getDataFolder(), "blocks.yml"));
            if (i > 0) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to blocks.yml");
            }
        } catch (IOException io) {
            plugin.debug("Could not save blocks.yml, " + io.getMessage());
        }
    }

    private void checkArtronConfig() {
        int i = 0;
        // boolean values
        for (Map.Entry<String, Boolean> entry : artronBoolOptions.entrySet()) {
            if (!artron_config.contains(entry.getKey())) {
                artron_config.set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // double values
        for (Map.Entry<String, Double> entry : artronDoubleOptions.entrySet()) {
            if (!artron_config.contains(entry.getKey())) {
                artron_config.set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // int values
        for (Map.Entry<String, Integer> entry : artronIntOptions.entrySet()) {
            if (!artron_config.contains(entry.getKey())) {
                if (config.contains(entry.getKey())) {
                    artron_config.set(entry.getKey(), config.getInt(entry.getKey()));
                    plugin.getConfig().set(entry.getKey(), null);
                } else {
                    artron_config.set(entry.getKey(), entry.getValue());
                    i++;
                }
            }
        }
        // string values
        for (Map.Entry<String, String> entry : artronStrOptions.entrySet()) {
            if (!artron_config.contains(entry.getKey())) {
                if (config.contains(entry.getKey())) {
                    artron_config.set(entry.getKey(), config.getString(entry.getKey()));
                    plugin.getConfig().set(entry.getKey(), null);
                } else {
                    artron_config.set(entry.getKey(), entry.getValue());
                    i++;
                }
            }
        }
        try {
            artron_config.save(new File(plugin.getDataFolder(), "artron.yml"));
            if (i > 0) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to artron.yml");
            }
        } catch (IOException io) {
            plugin.debug("Could not save artron.yml, " + io.getMessage());
        }
    }

    public void checkSignsConfig() {
        int i = 0;
        for (Map.Entry<String, List<String>> entry : signListOptions.entrySet()) {
            if (!signs_config.contains(entry.getKey())) {
                signs_config.set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        try {
            String signPath = plugin.getDataFolder() + File.separator + "language" + File.separator + "signs.yml";
            signs_config.save(new File(signPath));
            if (i > 0) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to signs.yml");
            }
        } catch (IOException io) {
            plugin.debug("Could not save signs.yml, " + io.getMessage());
        }
    }

    public void checkChameleonConfig() {
        if (chameleon_config.getString("SAVE").equals("Save construction")) {
            chameleon_config.set("SAVE", "Save construct");
        }
        int i = 0;
        for (Map.Entry<String, String> entry : chameleonOptions.entrySet()) {
            if (!chameleon_config.contains(entry.getKey())) {
                chameleon_config.set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        for (Map.Entry<String, List<String>> entry : chameleonListOptions.entrySet()) {
            if (!chameleon_config.contains(entry.getKey())) {
                chameleon_config.set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        try {
            String chameleonPath = plugin.getDataFolder() + File.separator + "language" + File.separator + "chameleon_guis.yml";
            chameleon_config.save(new File(chameleonPath));
            if (i > 0) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to chameleon_guis.yml");
            }
        } catch (IOException io) {
            plugin.debug("Could not save chameleon_guis.yml, " + io.getMessage());
        }
    }

    public void checkPlanetsConfig() {
        boolean save = false;
        if (!planets_config.contains("planets.Skaro.flying_daleks")) {
            planets_config.set("planets.Skaro.flying_daleks", true);
            save = true;
        }
        if (planets_config.contains("default_resource_pack") && planets_config.getString("default_resource_pack").equalsIgnoreCase("https://dl.dropboxusercontent.com/u/53758864/rp/Default.zip")) {
            planets_config.set("default_resource_pack", "https://www.dropbox.com/s/utka3zxmer7f19g/Default.zip?dl=1");
            save = true;
        }
        if (planets_config.contains("planets.Skaro.resource_pack") && planets_config.getString("planets.Skaro.resource_pack").equalsIgnoreCase("https://dl.dropboxusercontent.com/u/53758864/rp/Skaro.zip")) {
            planets_config.set("planets.Skaro.resource_pack", "https://www.dropbox.com/s/nr93rhbiyw2s5d0/Skaro.zip?dl=1");
            save = true;
        }
        if (!planets_config.contains("planets.Siluria.enabled")) {
            planets_config.set("planets.Siluria.enabled", false);
            planets_config.set("planets.Siluria.resource_pack", "default");
            save = true;
        }
        if (!planets_config.contains("planets.Siluria.false_nether")) {
            planets_config.set("planets.Siluria.false_nether", true);
            save = true;
        }
        if (!planets_config.contains("planets.Gallifrey.enabled")) {
            planets_config.set("planets.Gallifrey.enabled", false);
            planets_config.set("planets.Gallifrey.resource_pack", "https://www.dropbox.com/s/i7bpjju9jrgclq7/Gallifrey.zip?dl=1");
            save = true;
        }
        if (save) {
            try {
                String planetsPath = plugin.getDataFolder() + File.separator + "planets.yml";
                planets_config.save(new File(planetsPath));
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Added " + ChatColor.AQUA + "1" + ChatColor.RESET + " new item to planets.yml");
            } catch (IOException io) {
                plugin.debug("Could not save planets.yml, " + io.getMessage());
            }
        }
    }
}
