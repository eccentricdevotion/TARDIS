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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.FileUtil;

/**
 *
 * @author eccentric_nz
 */
public class TARDISConfigConverter {

    private final TARDIS plugin;
    private FileConfiguration config = null;
    HashMap<String, String> sectionsBool = new HashMap<String, String>();
    HashMap<String, String> sectionsInt = new HashMap<String, String>();
    HashMap<String, String> sectionsString = new HashMap<String, String>();

    public TARDISConfigConverter(TARDIS plugin) {
        plugin.debug("Starting config conversion...");
        this.plugin = plugin;
        // old path - new path
        sectionsBool.put("add_perms", "creation.add_perms");
        sectionsBool.put("all_blocks", "allow.all_blocks");
        sectionsBool.put("allow_achievements", "allow.achievements");
        sectionsBool.put("allow_autonomous", "allow.autonomous");
        sectionsBool.put("allow_hads", "allow.hads");
        sectionsBool.put("allow_mob_farming", "allow.mob_farming");
        sectionsBool.put("allow_tp_switch", "allow.tp_switch");
        sectionsBool.put("chameleon", "travel.chameleon");
        sectionsBool.put("conversion_done", "conversions.conversion_done");
        sectionsBool.put("create_worlds", "creation.create_worlds");
        sectionsBool.put("create_worlds_with_perms", "creation.create_worlds_with_perms");
        sectionsBool.put("custom_schematic", "creation.custom_schematic");
        sectionsBool.put("debug", "debug");
        sectionsBool.put("default_world", "creation.default_world");
        sectionsBool.put("emergency_npc", "allow.emergency_npc");
        sectionsBool.put("exile", "travel.exile");
        sectionsBool.put("give_key", "travel.give_key");
        sectionsBool.put("include_default_world", "travel.include_default_world");
        sectionsBool.put("keep_night", "creation.keep_night");
        sectionsBool.put("land_on_water", "travel.land_on_water");
        sectionsBool.put("location_conversion_done", "conversions.location_conversion_done");
        sectionsBool.put("materialise", "police_box.materialise");
        sectionsBool.put("name_tardis", "police_box.name_tardis");
        sectionsBool.put("nether", "travel.nether");
        sectionsBool.put("per_world_perms", "travel.per_world_perms");
        sectionsBool.put("respect_factions", "preferences.respect_factions");
        sectionsBool.put("respect_towny", "preferences.respect_towny");
        sectionsBool.put("respect_worldborder", "preferences.respect_worldborder");
        sectionsBool.put("respect_worldguard", "preferences.respect_worldguard");
        sectionsBool.put("return_room_seed", "growth.return_room_seed");
        sectionsBool.put("rooms_require_blocks", "growth.rooms_require_blocks");
        sectionsBool.put("sfx", "allow.sfx");
        sectionsBool.put("spawn_eggs", "allow.spawn_eggs");
        sectionsBool.put("strike_lightning", "preferences.strike_lightning");
        sectionsBool.put("the_end", "travel.the_end");
        sectionsBool.put("use_block_stack", "creation.use_block_stack");
        sectionsBool.put("use_clay", "creation.use_clay");
        sectionsBool.put("use_worldguard", "preferences.use_worldguard");
        sectionsInt.put("border_radius", "creation.border_radius");
        sectionsInt.put("confirm_timeout", "police_box.confirm_timeout");
        sectionsInt.put("count", "creation.count");
        sectionsInt.put("custom_creeper_id", "creation.custom_creeper_id");
        sectionsInt.put("freeze_cooldown", "preferences.freeze_cooldown");
        sectionsInt.put("gravity_max_distance", "growth.gravity_max_distance");
        sectionsInt.put("gravity_max_velocity", "growth.gravity_max_velocity");
        sectionsInt.put("hads_damage", "preferences.hads_damage");
        sectionsInt.put("hads_distance", "preferences.hads_distance");
        sectionsInt.put("malfunction", "preferences.malfunction");
        sectionsInt.put("malfunction_end", "preferences.malfunction_end");
        sectionsInt.put("malfunction_nether", "preferences.malfunction_nether");
        sectionsInt.put("platform_data", "police_box.platform_data");
        sectionsInt.put("platform_id", "police_box.platform_id");
        sectionsInt.put("random_attempts", "travel.random_attempts");
        sectionsInt.put("room_speed", "growth.room_speed");
        sectionsInt.put("rooms_condenser_percent", "growth.rooms_condenser_percent");
        sectionsInt.put("tardis_lamp", "police_box.tardis_lamp");
        sectionsInt.put("terminal_step", "travel.terminal_step");
        sectionsInt.put("timeout", "travel.timeout");
        sectionsInt.put("timeout_height", "travel.timeout_height");
        sectionsInt.put("tp_radius", "travel.tp_radius");
        sectionsInt.put("wall_data", "police_box.wall_data");
        sectionsInt.put("wall_id", "police_box.wall_id");
        sectionsString.put("custom_schematic_seed", "creation.custom_schematic_seed");
        sectionsString.put("database", "storage.database");
        sectionsString.put("default_world_name", "creation.default_world_name");
        sectionsString.put("difficulty", "preferences.difficulty");
        sectionsString.put("gamemode", "creation.gamemode");
        sectionsString.put("inventory_group", "creation.inventory_group");
        sectionsString.put("key", "preferences.key");
        sectionsString.put("mysql.password", "storage.mysql.password");
        sectionsString.put("mysql.url", "storage.mysql.url");
        sectionsString.put("mysql.user", "storage.mysql.user");
    }

    public boolean convert() {
        File oldFile = new File(plugin.getDataFolder() + File.separator + "config.yml");
        File newFile = new File(plugin.getDataFolder() + File.separator + "config.backup.yml");
        // back up the file
        FileUtil.copy(oldFile, newFile);
        // load config from backup
        this.config = YamlConfiguration.loadConfiguration(newFile);
        // copy the new config to the old config file
        oldFile.delete();
        new TARDISMakeTardisCSV(plugin).copy(plugin.getDataFolder() + File.separator + "config.yml", plugin.getResource("config_1.yml"));
        // update the new file with the old settings
        File newConfigFile = new File(plugin.getDataFolder() + File.separator + "config.yml");
        FileConfiguration newConfig = YamlConfiguration.loadConfiguration(newConfigFile);
        for (Map.Entry<String, String> map : sectionsBool.entrySet()) {
            newConfig.set(map.getValue(), config.getBoolean(map.getKey()));
        }
        for (Map.Entry<String, String> map : sectionsInt.entrySet()) {
            newConfig.set(map.getValue(), config.getInt(map.getKey()));
        }
        for (Map.Entry<String, String> map : sectionsString.entrySet()) {
            if (map.getKey().equals("inventory_group") && config.getString(map.getKey()).equals("0")) {
                newConfig.set(map.getValue(), config.getInt(map.getKey()));
            } else {
                newConfig.set(map.getValue(), config.getString(map.getKey()));
            }
        }
        // get worlds
        Set<String> worldNames = config.getConfigurationSection("worlds").getKeys(false);
        for (String wname : worldNames) {
            newConfig.set("worlds." + wname, config.getBoolean("worlds." + wname));
        }
        // get rechargers
        Set<String> chargerNames = config.getConfigurationSection("rechargers").getKeys(false);
        for (String charname : chargerNames) {
            newConfig.set("rechargers." + charname + ".world", config.getString("rechargers." + charname + ".world"));
            newConfig.set("rechargers." + charname + ".x", config.getInt("rechargers." + charname + ".x"));
            newConfig.set("rechargers." + charname + ".y", config.getInt("rechargers." + charname + ".y"));
            newConfig.set("rechargers." + charname + ".z", config.getInt("rechargers." + charname + ".z"));
        }
        try {
            newConfig.save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException io) {
            plugin.debug("Could not save config.yml, " + io);
            return false;
        }
        plugin.getConsole().sendMessage(plugin.getPluginName() + "Config file successfully updated to new format!");
        plugin.reloadConfig();
        return true;
    }
}
