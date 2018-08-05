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
import me.eccentric_nz.TARDIS.enumeration.WORLD_MANAGER;
import me.eccentric_nz.tardischunkgenerator.TARDISChunkGenerator;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The Alpha Centauran Table Tennis Club is a club established by the Alpha Centaurans for the play of table tennis. The
 * species is naturally gifted at the game, since they possess six arms. The Doctor is a member of the Club.
 *
 * @author eccentric_nz
 */
public class TARDISConfiguration {

    private final TARDIS plugin;
    private final FileConfiguration config;
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
        booleanOptions.put("allow.all_blocks", false);
        booleanOptions.put("allow.autonomous", true);
        booleanOptions.put("allow.emergency_npc", true);
        booleanOptions.put("allow.external_gravity", false);
        booleanOptions.put("allow.guardians", false);
        booleanOptions.put("allow.hads", true);
        booleanOptions.put("allow.handles", true);
        booleanOptions.put("allow.invisibility", true);
        booleanOptions.put("allow.mob_farming", true);
        booleanOptions.put("allow.perception_filter", true);
        booleanOptions.put("allow.player_difficulty", true);
        booleanOptions.put("allow.power_down_on_quit", false);
        booleanOptions.put("allow.power_down", true);
        booleanOptions.put("allow.repair", true);
        booleanOptions.put("allow.sfx", true);
        booleanOptions.put("allow.spawn_eggs", true);
        booleanOptions.put("allow.tp_switch", true);
        booleanOptions.put("allow.village_travel", false);
        booleanOptions.put("allow.wg_flag_set", true);
        booleanOptions.put("allow.zero_room", false);
        booleanOptions.put("arch.clear_inv_on_death", false);
        booleanOptions.put("arch.enabled", true);
        booleanOptions.put("arch.switch_inventory", true);
        booleanOptions.put("archive.enabled", true);
        booleanOptions.put("circuits.damage", false);
        booleanOptions.put("conversions.ars_materials", false);
        booleanOptions.put("conversions.condenser_materials", false);
        booleanOptions.put("conversions.player_prefs_materials", false);
        booleanOptions.put("conversions.block_materials", false);
        booleanOptions.put("conversions.custom_preset", false);
        booleanOptions.put("creation.add_perms", true);
        booleanOptions.put("creation.create_worlds_with_perms", false);
        booleanOptions.put("creation.create_worlds", false);
        booleanOptions.put("creation.custom_schematic", false);
        booleanOptions.put("creation.default_world", true);
        booleanOptions.put("creation.enable_legacy", true);
        booleanOptions.put("creation.keep_night", true);
        booleanOptions.put("creation.sky_biome", true);
        booleanOptions.put("creation.use_block_stack", false);
        booleanOptions.put("debug", false);
        booleanOptions.put("growth.return_room_seed", true);
        booleanOptions.put("desktop.check_blocks_before_upgrade", false);
        booleanOptions.put("growth.rooms_require_blocks", false);
        booleanOptions.put("handles.reminders.enabled", true);
        booleanOptions.put("junk.enabled", true);
        booleanOptions.put("junk.particles", true);
        booleanOptions.put("police_box.name_tardis", false);
        booleanOptions.put("police_box.set_biome", true);
        booleanOptions.put("preferences.nerf_pistons.enabled", false);
        booleanOptions.put("preferences.nerf_pistons.only_tardis_worlds", true);
        booleanOptions.put("preferences.no_coords", false);
        booleanOptions.put("preferences.no_creative_condense", false);
        booleanOptions.put("preferences.open_door_policy", false);
        booleanOptions.put("preferences.render_entities", false);
        booleanOptions.put("preferences.respect_factions", true);
        booleanOptions.put("preferences.respect_grief_prevention", true);
        booleanOptions.put("preferences.respect_worldborder", true);
        booleanOptions.put("preferences.spawn_random_monsters", true);
        booleanOptions.put("preferences.strike_lightning", true);
        booleanOptions.put("preferences.use_default_condensables", true);
        booleanOptions.put("preferences.use_worldguard", true);
        booleanOptions.put("preferences.walk_in_tardis", true);
        booleanOptions.put("siege.butcher", false);
        booleanOptions.put("siege.creeper", false);
        booleanOptions.put("siege.enabled", true);
        booleanOptions.put("siege.healing", false);
        booleanOptions.put("siege.texture", false);
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
        integerOptions.put("circuits.uses.temporal", 20);
        integerOptions.put("creation.border_radius", 256);
        integerOptions.put("creation.count", 0);
        integerOptions.put("creation.inventory_group", 0);
        integerOptions.put("creation.tips_limit", 400);
        integerOptions.put("desktop.block_change_percent", 25);
        integerOptions.put("growth.ars_limit", 1);
        integerOptions.put("growth.gravity_max_distance", 15);
        integerOptions.put("growth.gravity_max_velocity", 5);
        integerOptions.put("growth.room_speed", 4);
        integerOptions.put("growth.rooms_condenser_percent", 100);
        integerOptions.put("handles.reminders.schedule", 1200);
        integerOptions.put("junk.return", -1);
        integerOptions.put("police_box.confirm_timeout", 15);
        integerOptions.put("police_box.rebuild_cooldown", 10000);
        integerOptions.put("preferences.freeze_cooldown", 60);
        integerOptions.put("preferences.hads_damage", 10);
        integerOptions.put("preferences.hads_distance", 10);
        integerOptions.put("preferences.heal_speed", 200);
        integerOptions.put("preferences.malfunction_end", 3);
        integerOptions.put("preferences.malfunction_nether", 3);
        integerOptions.put("preferences.malfunction", 3);
        integerOptions.put("preferences.sfx_volume", 10);
        integerOptions.put("preferences.spawn_limit", 10);
        integerOptions.put("siege.breeding", 0);
        integerOptions.put("siege.growth", 0);
        integerOptions.put("travel.grace_period", 10);
        integerOptions.put("travel.manual_flight_delay", 60);
        integerOptions.put("travel.max_distance", 29999983);
        integerOptions.put("travel.random_attempts", 30);
        integerOptions.put("travel.random_circuit.x", 5000);
        integerOptions.put("travel.random_circuit.z", 5000);
        integerOptions.put("travel.terminal_step", 1);
        integerOptions.put("travel.timeout_height", 135);
        integerOptions.put("travel.timeout", 5);
        integerOptions.put("travel.tp_radius", 500);
        // string
        stringOptions.put("creation.area", "none");
        stringOptions.put("creation.custom_schematic_seed", "OBSIDIAN");
        stringOptions.put("creation.default_world_name", "TARDIS_TimeVortex");
        stringOptions.put("creation.gamemode", "survival");
        stringOptions.put("creation.use_clay", "WOOL");
        stringOptions.put("handles.prefix", "Hey Handles");
        stringOptions.put("police_box.default_preset", "FACTORY");
        stringOptions.put("police_box.sign_colour", "WHITE");
        stringOptions.put("police_box.tardis_lamp", "REDSTONE_LAMP");
        stringOptions.put("preferences.default_key", "eleventh");
        stringOptions.put("preferences.default_sonic", "eleventh");
        stringOptions.put("preferences.difficulty", "hard");
        stringOptions.put("preferences.key", "GOLD_NUGGET");
        stringOptions.put("preferences.language", "en");
        stringOptions.put("preferences.respect_towny", "nation");
        stringOptions.put("preferences.respect_worldguard", "build");
        stringOptions.put("preferences.vortex_fall", "kill");
        stringOptions.put("preferences.wand", "BONE");
        stringOptions.put("storage.database", "sqlite");
        stringOptions.put("storage.mysql.url", "mysql://localhost:3306/TARDIS");
        stringOptions.put("storage.mysql.user", "bukkit");
        stringOptions.put("storage.mysql.password", "mysecurepassword");
        stringOptions.put("storage.mysql.prefix", "");
        stringOptions.put("travel.terminal.nether", "world");
        stringOptions.put("travel.terminal.the_end", "world");
    }

    /**
     * Checks that the config file contains all the required entries. If entries are missing, then they are added with
     * default values. Also checks that all current server worlds are added to the config, and any deleted worlds are
     * removed.
     */
    public void checkConfig() {
        int i = 0;
        if (!config.contains("rechargers")) {
            plugin.getConfig().createSection("rechargers");
            i++;
        }
        // boolean values
        for (Map.Entry<String, Boolean> entry : booleanOptions.entrySet()) {
            if (!config.contains(entry.getKey())) {
                plugin.debug("missing entry: " + entry.getKey());
                plugin.getConfig().set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // int values
        for (Map.Entry<String, Integer> entry : integerOptions.entrySet()) {
            if (!config.contains(entry.getKey())) {
                plugin.debug("missing entry: " + entry.getKey());
                plugin.getConfig().set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // string values
        for (Map.Entry<String, String> entry : stringOptions.entrySet()) {
            if (!config.contains(entry.getKey())) {
                plugin.debug("missing entry: " + entry.getKey());
                plugin.getConfig().set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        if (i > 0) {
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to config");
        }
        // worlds
        doWorlds();
        plugin.saveConfig();
    }

    public void doWorlds() {
        List<World> worlds = plugin.getServer().getWorlds();
        String defWorld = config.getString("creation.default_world_name");
        worlds.forEach((w) -> {
            String worldname = "worlds." + w.getName();
            if (!config.contains(worldname) && !worldname.equals(defWorld)) {
                plugin.getConfig().set(worldname, true);
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Added '" + w.getName() + "' to config. To exclude this world run: /tardisadmin exclude " + w.getName());
            }
        });
        plugin.saveConfig();
        // now remove worlds that may have been deleted
        Set<String> cWorlds = plugin.getConfig().getConfigurationSection("worlds").getKeys(true);
        cWorlds.forEach((cw) -> {
            if (plugin.getServer().getWorld(cw) == null) {
                if (plugin.getWorldManager().equals(WORLD_MANAGER.NONE) && worldFolderExists(cw)) {
                    plugin.getConsole().sendMessage(plugin.getPluginName() + "Attempting to load world: '" + cw + "'");
                    loadWorld(cw);
                } else {
                    plugin.getConfig().set("worlds." + cw, null);
                    plugin.getConsole().sendMessage(plugin.getPluginName() + "Removed '" + cw + "' from config.yml");
                    // remove records from database that may contain
                    // the removed world
                    plugin.getCleanUpWorlds().add(cw);
                }
            }
        });
    }

    private boolean worldFolderExists(String world) {
        File container = plugin.getServer().getWorldContainer();
        File[] dirs = container.listFiles();
        if (dirs != null) {
            for (File dir : dirs) {
                if (dir.isDirectory() && dir.getName().equals(world)) {
                    File level = new File(dir, "level.dat");
                    if (level.exists()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void loadWorld(String world) {
        String which = (plugin.getPM().isPluginEnabled("TerrainControl")) ? "TerrainControl" : "OpenTerrainGenerator";
        if ((world.equals(plugin.getConfig().getString("creation.default_world_name")) || world.equals("TARDIS_Zero_Room"))) {
            WorldCreator.name(world).type(WorldType.FLAT).environment(Environment.NORMAL).generator(new TARDISChunkGenerator()).createWorld();
        } else if (world.equals("Gallifrey") || world.equals("Skaro")) {
            WorldCreator.name(world).type(WorldType.NORMAL).environment(Environment.NORMAL).generator(which).createWorld();
        } else if (world.equals("Siluria")) {
            WorldCreator.name(world).type(WorldType.NORMAL).environment(Environment.NETHER).generator(which).createWorld();
        } else {
            WorldCreator.name(world).createWorld();
        }
    }
}
