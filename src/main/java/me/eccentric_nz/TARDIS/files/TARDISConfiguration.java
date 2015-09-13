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
package me.eccentric_nz.TARDIS.files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * The Alpha Centauran Table Tennis Club is a club established by the Alpha
 * Centaurans for the play of table tennis. The species is naturally gifted at
 * the game, since they possess six arms. The Doctor is a member of the Club.
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
    private File configFile = null;
    HashMap<String, String> strOptions = new HashMap<String, String>();
    HashMap<String, Integer> intOptions = new HashMap<String, Integer>();
    HashMap<String, Boolean> boolOptions = new HashMap<String, Boolean>();
    HashMap<String, String> roomStrOptions = new HashMap<String, String>();
    HashMap<String, Integer> roomIntOptions = new HashMap<String, Integer>();
    HashMap<String, Boolean> roomBoolOptions = new HashMap<String, Boolean>();
    HashMap<String, Boolean> artronBoolOptions = new HashMap<String, Boolean>();
    HashMap<String, String> artronStrOptions = new HashMap<String, String>();
    HashMap<String, Double> artronDoubleOptions = new HashMap<String, Double>();
    HashMap<String, Integer> artronIntOptions = new HashMap<String, Integer>();
    HashMap<String, List<String>> signListOptions = new HashMap<String, List<String>>();

    public TARDISConfiguration(TARDIS plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
        this.config = YamlConfiguration.loadConfiguration(configFile);
        this.artron_config = plugin.getArtronConfig();
        this.blocks_config = plugin.getBlocksConfig();
        this.rooms_config = plugin.getRoomsConfig();
        this.signs_config = plugin.getSigns();
        // boolean
        boolOptions.put("allow.3d_doors", false);
        boolOptions.put("allow.achievements", true);
        boolOptions.put("allow.all_blocks", false);
        boolOptions.put("allow.autonomous", true);
        boolOptions.put("allow.emergency_npc", true);
        boolOptions.put("allow.external_gravity", false);
        boolOptions.put("allow.guardians", false);
        boolOptions.put("allow.hads", true);
        boolOptions.put("allow.invisibility", true);
        boolOptions.put("allow.mob_farming", true);
        boolOptions.put("allow.player_difficulty", true);
        boolOptions.put("allow.power_down", false);
        boolOptions.put("allow.power_down_on_quit", false);
        boolOptions.put("allow.sfx", true);
        boolOptions.put("allow.spawn_eggs", true);
        boolOptions.put("allow.tp_switch", true);
        boolOptions.put("allow.village_travel", false);
        boolOptions.put("allow.wg_flag_set", true);
        boolOptions.put("allow.zero_room", false);
        boolOptions.put("arch.clear_inv_on_death", false);
        boolOptions.put("arch.enabled", true);
        boolOptions.put("arch.switch_inventory", true);
        boolOptions.put("circuits.damage", false);
        boolOptions.put("conversions.biome_update", false);
        boolOptions.put("conversions.companion_clearing_done", false);
        boolOptions.put("conversions.condenser_done", false);
        boolOptions.put("conversions.conversion_done", false);
        boolOptions.put("conversions.location_conversion_done", false);
        boolOptions.put("conversions.uuid_conversion_done", false);
        boolOptions.put("conversions.lastknownname_conversion_done", false);
        boolOptions.put("creation.add_perms", true);
        boolOptions.put("creation.create_worlds", true);
        boolOptions.put("creation.create_worlds_with_perms", false);
        boolOptions.put("creation.custom_schematic", false);
        boolOptions.put("creation.default_world", false);
        boolOptions.put("creation.keep_night", true);
        boolOptions.put("creation.sky_biome", true);
        boolOptions.put("creation.use_block_stack", false);
        boolOptions.put("creation.use_clay", false);
        boolOptions.put("debug", false);
        boolOptions.put("desktop.check_blocks_before_upgrade", false);
        boolOptions.put("growth.return_room_seed", true);
        boolOptions.put("growth.rooms_require_blocks", false);
        boolOptions.put("junk.enabled", true);
        boolOptions.put("junk.particles", true);
        boolOptions.put("police_box.materialise", true);
        boolOptions.put("police_box.name_tardis", false);
        boolOptions.put("police_box.set_biome", true);
        boolOptions.put("preferences.nerf_pistons.enabled", false);
        boolOptions.put("preferences.nerf_pistons.only_tardis_worlds", true);
        boolOptions.put("preferences.render_entities", false);
        boolOptions.put("preferences.respect_factions", true);
        boolOptions.put("preferences.respect_grief_prevention", true);
        boolOptions.put("preferences.respect_worldborder", true);
        boolOptions.put("preferences.spawn_random_monsters", true);
        boolOptions.put("preferences.use_worldguard", true);
        boolOptions.put("preferences.strike_lightning", true);
        boolOptions.put("preferences.use_default_condensables", true);
        boolOptions.put("preferences.walk_in_tardis", true);
        boolOptions.put("siege.enabled", true);
        boolOptions.put("siege.butcher", false);
        boolOptions.put("siege.creeper", false);
        boolOptions.put("siege.healing", false);
        boolOptions.put("siege.texture", false);
        boolOptions.put("travel.chameleon", true);
        boolOptions.put("travel.exile", false);
        boolOptions.put("travel.give_key", false);
        boolOptions.put("travel.include_default_world", false);
        boolOptions.put("travel.land_on_water", true);
        boolOptions.put("travel.nether", false);
        boolOptions.put("travel.per_world_perms", false);
        boolOptions.put("travel.terminal.redefine", false);
        boolOptions.put("travel.the_end", false);
        roomBoolOptions.put("rooms.ANTIGRAVITY.enabled", true);
        roomBoolOptions.put("rooms.ANTIGRAVITY.user", false);
        roomBoolOptions.put("rooms.ARBORETUM.enabled", true);
        roomBoolOptions.put("rooms.ARBORETUM.user", false);
        roomBoolOptions.put("rooms.BAKER.enabled", true);
        roomBoolOptions.put("rooms.BAKER.user", false);
        roomBoolOptions.put("rooms.BEDROOM.enabled", true);
        roomBoolOptions.put("rooms.BEDROOM.user", false);
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
        roomBoolOptions.put("rooms.STABLE.enabled", true);
        roomBoolOptions.put("rooms.STABLE.user", false);
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
        artronBoolOptions.put("artron_furnace.particles", true);
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
        artronIntOptions.put("standby", 5);
        artronIntOptions.put("standby_time", 6000);
        artronIntOptions.put("the_end_min", 5500);
        artronIntOptions.put("travel", 100);
        artronIntOptions.put("zero", 250);
        artronIntOptions.put("upgrades.ars", 5000);
        artronIntOptions.put("upgrades.bigger", 7500);
        artronIntOptions.put("upgrades.budget", 5000);
        artronIntOptions.put("upgrades.deluxe", 10000);
        artronIntOptions.put("upgrades.eleventh", 10000);
        artronIntOptions.put("upgrades.master", 10000);
        artronIntOptions.put("upgrades.plank", 5000);
        artronIntOptions.put("upgrades.pyramid", 5000);
        artronIntOptions.put("upgrades.redstone", 7500);
        artronIntOptions.put("upgrades.steampunk", 5000);
        artronIntOptions.put("upgrades.tom", 5000);
        artronIntOptions.put("upgrades.twelfth", 7500);
        artronIntOptions.put("upgrades.war", 5000);
        artronIntOptions.put("upgrades.custom", 10000);
        intOptions.put("arch.min_time", 20);
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
        intOptions.put("growth.gravity_max_distance", 16);
        intOptions.put("growth.gravity_max_velocity", 5);
        intOptions.put("growth.room_speed", 4);
        intOptions.put("growth.rooms_condenser_percent", 100);
        intOptions.put("junk.return", -1);
        intOptions.put("police_box.confirm_timeout", 15);
        intOptions.put("police_box.platform_data", 8);
        intOptions.put("police_box.platform_id", 35);
        intOptions.put("police_box.rebuild_cooldown", 10000);
        intOptions.put("police_box.tardis_lamp", 50);
        intOptions.put("police_box.wall_data", 11);
        intOptions.put("police_box.wall_id", 35);
        intOptions.put("preferences.freeze_cooldown", 60);
        intOptions.put("preferences.hads_damage", 10);
        intOptions.put("preferences.hads_distance", 10);
        intOptions.put("preferences.heal_speed", 200);
        intOptions.put("preferences.malfunction", 3);
        intOptions.put("preferences.malfunction_end", 3);
        intOptions.put("preferences.malfunction_nether", 3);
        intOptions.put("preferences.sfx_volume", 10);
        intOptions.put("siege.breeding", 0);
        intOptions.put("siege.growth", 0);
        intOptions.put("travel.grace_period", 10);
        intOptions.put("travel.manual_flight_delay", 60);
        intOptions.put("travel.random_attempts", 30);
        intOptions.put("travel.terminal_step", 1);
        intOptions.put("travel.timeout", 5);
        intOptions.put("travel.timeout_height", 135);
        intOptions.put("travel.tp_radius", 256);
        intOptions.put("travel.random_circuit.x", 5000);
        intOptions.put("travel.random_circuit.z", 5000);
        roomIntOptions.put("rooms.ANTIGRAVITY.cost", 625);
        roomIntOptions.put("rooms.ANTIGRAVITY.offset", -4);
        roomIntOptions.put("rooms.ARBORETUM.cost", 325);
        roomIntOptions.put("rooms.ARBORETUM.offset", -4);
        roomIntOptions.put("rooms.BAKER.cost", 350);
        roomIntOptions.put("rooms.BAKER.offset", -4);
        roomIntOptions.put("rooms.BEDROOM.cost", 475);
        roomIntOptions.put("rooms.BEDROOM.offset", -4);
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
        roomIntOptions.put("rooms.STABLE.cost", 350);
        roomIntOptions.put("rooms.STABLE.offset", -4);
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
        strOptions.put("creation.custom_creeper_id", "BEACON");
        strOptions.put("creation.custom_schematic_seed", "OBSIDIAN");
        strOptions.put("creation.default_world_name", "TARDIS_TimeVortex");
        strOptions.put("creation.gamemode", "survival");
        strOptions.put("police_box.default_preset", "NEW");
        strOptions.put("police_box.sign_colour", "WHITE");
        strOptions.put("preferences.default_key", "eleventh");
        strOptions.put("preferences.default_sonic", "eleventh");
        strOptions.put("preferences.difficulty", "hard");
        strOptions.put("preferences.key", "GOLD_NUGGET");
        strOptions.put("preferences.language", "en");
        strOptions.put("preferences.respect_towny", "nation");
        strOptions.put("preferences.respect_worldguard", "build");
        strOptions.put("preferences.wand", "BONE");
        strOptions.put("storage.database", "sqlite");
        strOptions.put("storage.mysql.url", "mysql://localhost:3306/TARDIS");
        strOptions.put("storage.mysql.user", "bukkit");
        strOptions.put("storage.mysql.password", "mysecurepassword");
        strOptions.put("storage.mysql.prefix", "");
        strOptions.put("travel.terminal.nether", "world");
        strOptions.put("travel.terminal.the_end", "world");
        artronStrOptions.put("jettison_seed", "TNT");
        artronStrOptions.put("full_charge_item", "NETHER_STAR");
        roomStrOptions.put("rooms.ARBORETUM.seed", "LEAVES");
        roomStrOptions.put("rooms.BAKER.seed", "ENDER_STONE");
        roomStrOptions.put("rooms.BEDROOM.seed", "GLOWSTONE");
        roomStrOptions.put("rooms.EMPTY.seed", "GLASS");
        roomStrOptions.put("rooms.FARM.seed", "DIRT");
        roomStrOptions.put("rooms.GRAVITY.seed", "MOSSY_COBBLESTONE");
        roomStrOptions.put("rooms.ANTIGRAVITY.seed", "SANDSTONE");
        roomStrOptions.put("rooms.GREENHOUSE.seed", "MELON_BLOCK");
        roomStrOptions.put("rooms.HARMONY.seed", "BRICK_STAIRS");
        roomStrOptions.put("rooms.HUTCH.seed", "LOG_2");
        roomStrOptions.put("rooms.KITCHEN.seed", "PUMPKIN");
        roomStrOptions.put("rooms.LAZARUS.seed", "FURNACE");
        roomStrOptions.put("rooms.LIBRARY.seed", "ENCHANTMENT_TABLE");
        roomStrOptions.put("rooms.MUSHROOM.seed", "GRAVEL");
        roomStrOptions.put("rooms.PASSAGE.seed", "CLAY");
        roomStrOptions.put("rooms.POOL.seed", "SNOW_BLOCK");
        roomStrOptions.put("rooms.RAIL.seed", "HOPPER");
        roomStrOptions.put("rooms.RENDERER.seed", "HARD_CLAY");
        roomStrOptions.put("rooms.STABLE.seed", "HAY_BLOCK");
        roomStrOptions.put("rooms.TRENZALORE.seed", "BRICK");
        roomStrOptions.put("rooms.VAULT.seed", "DISPENSER");
        roomStrOptions.put("rooms.VILLAGE.seed", "LOG");
        roomStrOptions.put("rooms.WOOD.seed", "WOOD");
        roomStrOptions.put("rooms.WORKSHOP.seed", "NETHER_BRICK");
        roomStrOptions.put("rooms.ZERO.seed", "WOOD_BUTTON");
        signListOptions.put("junk", Arrays.asList("Destination"));
    }

    /**
     * Checks that the config file contains all the required entries. If entries
     * are missing, then they are added with default values. Also checks that
     * all current server worlds are added to the config, and any deleted worlds
     * are removed.
     */
    public void checkConfig() {
        if (!config.contains("creation.create_worlds")) {
            new TARDISConfigConverter(plugin).convert();
        } else {
            int i = 0;
            if (config.contains("preferences.respect_towny")) {
                if (plugin.getConfig().getString("preferences.respect_towny").equals("true") || plugin.getConfig().getString("preferences.respect_towny").equals("false")) {
                    String towny = (plugin.getConfig().getBoolean("preferences.respect_towny")) ? "nation" : "none";
                    plugin.getConfig().set("preferences.respect_towny", towny);
                }
            }
            if (config.contains("preferences.respect_worldguard")) {
                if (plugin.getConfig().getString("preferences.respect_worldguard").equals("true") || plugin.getConfig().getString("preferences.respect_worldguard").equals("false")) {
                    String guard = (plugin.getConfig().getBoolean("preferences.respect_worldguard")) ? "build" : "none";
                    plugin.getConfig().set("preferences.respect_worldguard", guard);
                }
            }
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
                if (entry.getKey().equals("preferences.sfx_volume") && plugin.getConfig().getInt("preferences.sfx_volume") == 0) {
                    plugin.getConfig().set("preferences.sfx_volume", 10);
                }
            }
            // string values
            for (Map.Entry<String, String> entry : strOptions.entrySet()) {
                if (!config.contains(entry.getKey())) {
                    plugin.getConfig().set(entry.getKey(), entry.getValue());
                    i++;
                } else if (entry.getKey().equals("creation.custom_creeper_id")) {
                    try {
                        int id = Integer.parseInt(plugin.getConfig().getString("creation.custom_creeper_id"));
                        String set = Material.getMaterial(id).toString();
                        plugin.getConfig().set("creation.custom_creeper_id", set);
                    } catch (NumberFormatException e) {
                        // no conversion necessary
                    }
                }
            }
            if (!config.isConfigurationSection("rechargers")) {
                plugin.getConfig().createSection("rechargers");
            }
            if (config.contains("rooms.FIRST")) {
                plugin.getConfig().set("rooms.FIRST", null);
            }
            if (config.contains("creation.materialise")) {
                plugin.getConfig().set("creation.materialise", null);
            }
            if (config.contains("difficulty") && config.getString("difficulty").equals("normal")) {
                plugin.getConfig().set("difficulty", "hard");
            }
            if (i > 0) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to config");
            }
        }
        // worlds
        doWorlds();
        checkArtronConfig();
        checkBlocksConfig();
        checkRoomsConfig();
        checkSignsConfig();
        plugin.saveConfig();
    }

    public void doWorlds() {
        List<World> worlds = plugin.getServer().getWorlds();
        for (World w : worlds) {
            String worldname = "worlds." + w.getName();
            if (!config.contains(worldname)) {
                plugin.getConfig().set(worldname, true);
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Added '" + w.getName() + "' to config. To exclude this world run: /tardisadmin exclude " + w.getName());
            }
        }
        plugin.saveConfig();
        // now remove worlds that may have been deleted
        Set<String> cWorlds = plugin.getConfig().getConfigurationSection("worlds").getKeys(true);
        for (String cw : cWorlds) {
            if (plugin.getServer().getWorld(cw) == null) {
                plugin.getConfig().set("worlds." + cw, null);
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Removed '" + cw + " from config.yml");
                // remove records from database that may contain
                // the removed world
                plugin.getCleanUpWorlds().add(cw);
            }
        }
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
        // copy old settings and add any custom rooms
        if (config.contains("rooms")) {
            for (String r : config.getConfigurationSection("rooms").getKeys(false)) {
                rooms_config.set("rooms." + r + ".enabled", config.getBoolean("rooms." + r + ".enabled"));
                rooms_config.set("rooms." + r + ".cost", config.getInt("rooms." + r + ".cost"));
                if (!r.equalsIgnoreCase("ANTIGRAVITY")) {
                    rooms_config.set("rooms." + r + ".offset", config.getInt("rooms." + r + ".offset"));
                }
                rooms_config.set("rooms." + r + ".seed", config.getString("rooms." + r + ".seed"));
                rooms_config.set("rooms." + r + ".user", config.getBoolean("rooms." + r + ".user"));
            }
            // remove old rooms section
            plugin.getConfig().set("rooms", null);
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
        if (!blocks_config.contains("keys")) {
            List<String> KEYS = Arrays.asList("GOLD_NUGGET", "STICK");
            blocks_config.set("keys", KEYS);
            i++;
        }
        if (!blocks_config.contains("tardis_blocks")) {
            List<String> MIDDLE_BLOCKS;
            if (config.contains("tardis_blocks")) {
                MIDDLE_BLOCKS = config.getStringList("tardis_blocks");
                // remove old tardis_blocks section
                plugin.getConfig().set("tardis_blocks", null);
            } else {
                MIDDLE_BLOCKS = Arrays.asList("COBBLESTONE", "MOSSY_COBBLESTONE", "LOG", "LOG_2", "STONE", "DIRT", "WOOD", "SANDSTONE", "WOOL", "BRICK", "NETHERRACK", "SOUL_SAND", "SMOOTH_BRICK", "HUGE_MUSHROOM_1", "HUGE_MUSHROOM_2", "ENDER_STONE", "QUARTZ_BLOCK", "CLAY", "STAINED_CLAY", "HAY_BLOCK", "HARD_CLAY", "PACKED_ICE");
            }
            blocks_config.set("tardis_blocks", MIDDLE_BLOCKS);
            i++;
        } else {
            List<String> tblocs = blocks_config.getStringList("tardis_blocks");
            if (!tblocs.contains("STAINED_CLAY")) {
                tblocs.add("STAINED_CLAY");
                tblocs.add("HAY_BLOCK");
                tblocs.add("HARD_CLAY");
                blocks_config.set("tardis_blocks", tblocs);
                i++;
            }
            if (!tblocs.contains("LOG")) {
                tblocs.add("COBBLESTONE");
                tblocs.add("MOSSY_COBBLESTONE");
                tblocs.add("LOG");
                blocks_config.set("tardis_blocks", tblocs);
                i++;
            }
            if (!tblocs.contains("LOG_2")) {
                tblocs.add("LOG_2");
                tblocs.add("PACKED_ICE");
                blocks_config.set("tardis_blocks", tblocs);
                i++;
            }
        }
        if (!blocks_config.contains("chameleon_blocks")) {
            List<Integer> CHAM_BLOCKS = Arrays.asList(1, 3, 4, 5, 7, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24, 25, 35, 41, 42, 45, 46, 47, 48, 49, 56, 57, 58, 73, 79, 80, 82, 84, 86, 87, 88, 89, 91, 98, 99, 100, 103, 110, 112, 121, 123, 129, 133, 155, 159, 161, 162, 170, 172, 173, 174);
            blocks_config.set("chameleon_blocks", CHAM_BLOCKS);
            i++;
        } else {
            List<Integer> cblocs = blocks_config.getIntegerList("chameleon_blocks");
            if (!cblocs.contains(159)) {
                cblocs.add(159);
                cblocs.add(170);
                cblocs.add(172);
                cblocs.add(173);
                blocks_config.set("chameleon_blocks", cblocs);
                i++;
            }
            if (!cblocs.contains(161)) {
                cblocs.add(161);
                cblocs.add(162);
                cblocs.add(174);
                blocks_config.set("chameleon_blocks", cblocs);
                i++;
            }
            if (!cblocs.contains(179)) {
                cblocs.add(179);
                blocks_config.set("chameleon_blocks", cblocs);
                i++;
            }
            if (cblocs.contains(43)) {
                cblocs.remove(Integer.valueOf(43));
                cblocs.remove(Integer.valueOf(74));
                cblocs.remove(Integer.valueOf(124));
                blocks_config.set("chameleon_blocks", cblocs);
            }
        }
        // add lamp blocks
        if (!blocks_config.contains("lamp_blocks")) {
            List<String> LAMP_BLOCKS = Arrays.asList("TORCH", "REDSTONE_TORCH_ON", "GLOWSTONE", "JACK_O_LANTERN", "REDSTONE_LAMP_OFF", "SEA_LANTERN");
            blocks_config.set("lamp_blocks", LAMP_BLOCKS);
            i++;
        } else if (blocks_config.getStringList("lamp_blocks").get(0).equals("50")) {
            List<String> lstrs = new ArrayList<String>();
            for (int l : blocks_config.getIntegerList("lamp_blocks")) {
                try {
                    lstrs.add(Material.getMaterial(l).toString());
                } catch (Exception e) {
                    plugin.debug("Invalid Material ID in lamp_blocks section.");
                }
            }
            blocks_config.set("lamp_blocks", lstrs);
            i++;
        }
        if (!blocks_config.contains("under_door_blocks")) {
            List<Integer> UNDER_BLOCKS = Arrays.asList(0, 6, 8, 9, 10, 11, 18, 20, 26, 27, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 44, 46, 50, 51, 53, 54, 55, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 75, 76, 77, 78, 79, 81, 83, 85, 89, 92, 93, 94, 96, 101, 102, 104, 105, 106, 107, 108, 109, 111, 113, 114, 115, 116, 117, 118, 119, 120, 122, 126, 128, 130, 131, 132, 134, 135, 136, 161, 171);
            blocks_config.set("under_door_blocks", UNDER_BLOCKS);
            i++;
        } else {
            List<Integer> udblocs = blocks_config.getIntegerList("under_door_blocks");
            if (!udblocs.contains(161)) {
                udblocs.add(161);
                blocks_config.set("under_door_blocks", udblocs);
                i++;
            }
            if (!udblocs.contains(171)) {
                udblocs.add(171);
                blocks_config.set("under_door_blocks", udblocs);
                i++;
            }
        }
        if (!blocks_config.contains("no_artron_value")) {
            blocks_config.set("no_artron_value", new ArrayList<String>());
            i++;
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
}
