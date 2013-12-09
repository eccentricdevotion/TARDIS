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
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.ChatColor;
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
    private File configFile = null;
    HashMap<String, String> strOptions = new HashMap<String, String>();
    HashMap<String, Integer> intOptions = new HashMap<String, Integer>();
    HashMap<String, Boolean> boolOptions = new HashMap<String, Boolean>();
    HashMap<String, String> roomStrOptions = new HashMap<String, String>();
    HashMap<String, Integer> roomIntOptions = new HashMap<String, Integer>();
    HashMap<String, Boolean> roomBoolOptions = new HashMap<String, Boolean>();
    HashMap<String, String> artronStrOptions = new HashMap<String, String>();
    HashMap<String, Integer> artronIntOptions = new HashMap<String, Integer>();

    public TARDISConfiguration(TARDIS plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), TARDISConstants.CONFIG_FILE_NAME);
        this.config = YamlConfiguration.loadConfiguration(configFile);
        this.artron_config = plugin.getArtronConfig();
        this.blocks_config = plugin.getBlocksConfig();
        this.rooms_config = plugin.getRoomsConfig();
        // boolean
        boolOptions.put("add_perms", true);
        boolOptions.put("all_blocks", false);
        boolOptions.put("allow_achievements", true);
        boolOptions.put("allow_autonomous", true);
        boolOptions.put("allow_hads", true);
        boolOptions.put("allow_mob_farming", true);
        boolOptions.put("allow_tp_switch", true);
        boolOptions.put("chameleon", true);
        boolOptions.put("conversion_done", false);
        boolOptions.put("create_worlds", true);
        boolOptions.put("create_worlds_with_perms", false);
        boolOptions.put("custom_schematic", false);
        boolOptions.put("debug", false);
        boolOptions.put("default_world", false);
        boolOptions.put("emergency_npc", true);
        boolOptions.put("exile", false);
        boolOptions.put("give_key", false);
        boolOptions.put("include_default_world", false);
        boolOptions.put("keep_night", true);
        boolOptions.put("land_on_water", true);
        boolOptions.put("location_conversion_done", false);
        boolOptions.put("materialise", true);
        boolOptions.put("name_tardis", false);
        boolOptions.put("nether", false);
        boolOptions.put("per_world_perms", false);
        boolOptions.put("plain_on", false);
        boolOptions.put("platform", false);
        boolOptions.put("respect_factions", true);
        boolOptions.put("respect_towny", true);
        boolOptions.put("respect_worldborder", true);
        boolOptions.put("respect_worldguard", true);
        boolOptions.put("return_room_seed", true);
        boolOptions.put("rooms_require_blocks", false);
        boolOptions.put("sfx", true);
        boolOptions.put("spawn_eggs", true);
        boolOptions.put("strike_lightning", true);
        boolOptions.put("the_end", false);
        boolOptions.put("use_clay", false);
        boolOptions.put("use_block_stack", false);
        boolOptions.put("use_worldguard", true);
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
        roomBoolOptions.put("rooms.KITCHEN.enabled", true);
        roomBoolOptions.put("rooms.KITCHEN.user", false);
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
        // integer
        artronIntOptions.put("autonomous", 100);
        artronIntOptions.put("backdoor", 100);
        artronIntOptions.put("comehere", 400);
        artronIntOptions.put("creeper_recharge", 150);
        artronIntOptions.put("full_charge", 5000);
        artronIntOptions.put("hide", 500);
        artronIntOptions.put("jettison", 75);
        artronIntOptions.put("lightning_recharge", 300);
        artronIntOptions.put("nether_min", 4250);
        artronIntOptions.put("player", 25);
        artronIntOptions.put("random", 75);
        artronIntOptions.put("recharge_distance", 20);
        artronIntOptions.put("the_end_min", 5500);
        artronIntOptions.put("travel", 100);
        intOptions.put("admin_item", 264);
        intOptions.put("border_radius", 64);
        intOptions.put("confirm_timeout", 15);
        intOptions.put("count", 0);
        intOptions.put("custom_creeper_id", 138);
        intOptions.put("gravity_max_distance", 16);
        intOptions.put("gravity_max_velocity", 5);
        intOptions.put("hads_damage", 10);
        intOptions.put("hads_distance", 10);
        intOptions.put("inventory_group", 0);
        intOptions.put("malfunction", 3);
        intOptions.put("malfunction_end", 3);
        intOptions.put("malfunction_nether", 3);
        intOptions.put("platform_data", 8);
        intOptions.put("platform_id", 35);
        intOptions.put("random_attempts", 30);
        intOptions.put("room_speed", 4);
        intOptions.put("rooms_condenser_percent", 100);
        intOptions.put("tardis_lamp", 50);
        intOptions.put("terminal_step", 1);
        intOptions.put("timeout", 5);
        intOptions.put("timeout_height", 135);
        intOptions.put("tp_radius", 256);
        intOptions.put("wall_data", 11);
        intOptions.put("wall_id", 35);
        roomIntOptions.put("rooms.ANTIGRAVITY.cost", 625);
        roomIntOptions.put("rooms.ANTIGRAVITY.offset", -3);
        roomIntOptions.put("rooms.ARBORETUM.cost", 325);
        roomIntOptions.put("rooms.ARBORETUM.offset", -4);
        roomIntOptions.put("rooms.BAKER.cost", 350);
        roomIntOptions.put("rooms.BAKER.offset", -3);
        roomIntOptions.put("rooms.BEDROOM.cost", 475);
        roomIntOptions.put("rooms.BEDROOM.offset", -3);
        roomIntOptions.put("rooms.EMPTY.cost", 250);
        roomIntOptions.put("rooms.EMPTY.offset", -3);
        roomIntOptions.put("rooms.FARM.cost", 350);
        roomIntOptions.put("rooms.FARM.offset", -3);
        roomIntOptions.put("rooms.GRAVITY.cost", 625);
        roomIntOptions.put("rooms.GRAVITY.offset", -19);
        roomIntOptions.put("rooms.GREENHOUSE.cost", 450);
        roomIntOptions.put("rooms.GREENHOUSE.offset", -3);
        roomIntOptions.put("rooms.HARMONY.cost", 450);
        roomIntOptions.put("rooms.HARMONY.offset", -3);
        roomIntOptions.put("rooms.KITCHEN.cost", 450);
        roomIntOptions.put("rooms.KITCHEN.offset", -3);
        roomIntOptions.put("rooms.LIBRARY.cost", 550);
        roomIntOptions.put("rooms.LIBRARY.offset", -3);
        roomIntOptions.put("rooms.MUSHROOM.cost", 350);
        roomIntOptions.put("rooms.MUSHROOM.offset", -3);
        roomIntOptions.put("rooms.PASSAGE.cost", 200);
        roomIntOptions.put("rooms.PASSAGE.offset", -3);
        roomIntOptions.put("rooms.POOL.cost", 450);
        roomIntOptions.put("rooms.POOL.offset", -3);
        roomIntOptions.put("rooms.RAIL.cost", 650);
        roomIntOptions.put("rooms.RAIL.offset", -3);
        roomIntOptions.put("rooms.STABLE.cost", 350);
        roomIntOptions.put("rooms.STABLE.offset", -3);
        roomIntOptions.put("rooms.TRENZALORE.cost", 550);
        roomIntOptions.put("rooms.TRENZALORE.offset", -3);
        roomIntOptions.put("rooms.VAULT.cost", 350);
        roomIntOptions.put("rooms.VAULT.offset", -3);
        roomIntOptions.put("rooms.VILLAGE.cost", 550);
        roomIntOptions.put("rooms.VILLAGE.offset", -3);
        roomIntOptions.put("rooms.WOOD.cost", 350);
        roomIntOptions.put("rooms.WOOD.offset", -3);
        roomIntOptions.put("rooms.WORKSHOP.cost", 400);
        roomIntOptions.put("rooms.WORKSHOP.offset", -3);
        // string
        strOptions.put("custom_schematic_seed", "OBSIDIAN");
        strOptions.put("default_world_name", "myridiculouslylongworldnameiscalledcuthbert");
        strOptions.put("difficulty", "hard");
        strOptions.put("gamemode", "survival");
        strOptions.put("key", "STICK");
        strOptions.put("stattenheim", "FLINT");
        strOptions.put("database", "sqlite");
        strOptions.put("mysql.url", "mysql://localhost:3306/TARDIS");
        strOptions.put("mysql.user", "bukkit");
        strOptions.put("mysql.password", "mysecurepassword");
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
        roomStrOptions.put("rooms.KITCHEN.seed", "PUMPKIN");
        roomStrOptions.put("rooms.LIBRARY.seed", "BOOKSHELF");
        roomStrOptions.put("rooms.MUSHROOM.seed", "GRAVEL");
        roomStrOptions.put("rooms.PASSAGE.seed", "CLAY");
        roomStrOptions.put("rooms.POOL.seed", "SNOW_BLOCK");
        roomStrOptions.put("rooms.RAIL.seed", "HOPPER");
        roomStrOptions.put("rooms.STABLE.seed", "HAY_BLOCK");
        roomStrOptions.put("rooms.TRENZALORE.seed", "BRICK");
        roomStrOptions.put("rooms.VAULT.seed", "DISPENSER");
        roomStrOptions.put("rooms.VILLAGE.seed", "LOG");
        roomStrOptions.put("rooms.WOOD.seed", "WOOD");
        roomStrOptions.put("rooms.WORKSHOP.seed", "NETHER_BRICK");
    }

    /**
     * Checks that the config file contains all the required entries. If entries
     * are missing, then they are added with default values. Also checks that
     * all current server worlds are added to the config, and any deleted worlds
     * are removed.
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
        if (!config.isConfigurationSection("rechargers")) {
            plugin.getConfig().createSection("rechargers");
        }
        if (config.contains("rooms.FIRST")) {
            plugin.getConfig().set("rooms.FIRST", null);
        }
        if (config.contains("difficulty") && config.getString("difficulty").equals("normal")) {
            plugin.getConfig().set("difficulty", "hard");
        }
        if (i > 0) {
            plugin.console.sendMessage(plugin.pluginName + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to config");
        }
        // worlds
        doWorlds();
        checkArtronConfig();
        checkBlocksConfig();
        checkRoomsConfig();
        plugin.saveConfig();
    }

    public void doWorlds() {
        List<World> worlds = plugin.getServer().getWorlds();
        for (World w : worlds) {
            String worldname = "worlds." + w.getName();
            if (!config.contains(worldname)) {
                plugin.getConfig().set(worldname, true);
                plugin.console.sendMessage(plugin.pluginName + "Added '" + w.getName() + "' to config. To exclude this world run: /tardisadmin exclude " + w.getName());
            }
        }
        plugin.saveConfig();
        // now remove worlds that may have been deleted
        Set<String> cWorlds = plugin.getConfig().getConfigurationSection("worlds").getKeys(true);
        for (String cw : cWorlds) {
            if (plugin.getServer().getWorld(cw) == null) {
                plugin.getConfig().set("worlds." + cw, null);
                plugin.console.sendMessage(plugin.pluginName + "Removed '" + cw + " from config.yml");
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
                //if (!rooms_config.contains("rooms." + r)) {
                rooms_config.set("rooms." + r + ".enabled", config.getBoolean("rooms." + r + ".enabled"));
                rooms_config.set("rooms." + r + ".cost", config.getInt("rooms." + r + ".cost"));
                if (!r.equalsIgnoreCase("ANTIGRAVITY")) {
                    rooms_config.set("rooms." + r + ".offset", config.getInt("rooms." + r + ".offset"));
                }
                rooms_config.set("rooms." + r + ".seed", config.getString("rooms." + r + ".seed"));
                rooms_config.set("rooms." + r + ".user", config.getBoolean("rooms." + r + ".user"));
                //}
            }
            // remove old rooms section
            plugin.getConfig().set("rooms", null);
        }
        try {
            rooms_config.save(new File(plugin.getDataFolder(), "rooms.yml"));
            if (i > 0) {
                plugin.console.sendMessage(plugin.pluginName + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to rooms.yml");
            }
        } catch (IOException io) {
            plugin.debug("Could not save rooms.yml, " + io);
        }
    }

    private void checkBlocksConfig() {
        int i = 0;
        if (!blocks_config.contains("tardis_blocks")) {
            List<String> MIDDLE_BLOCKS;
            if (config.contains("tardis_blocks")) {
                MIDDLE_BLOCKS = config.getStringList("tardis_blocks");
                // remove old tardis_blocks section
                plugin.getConfig().set("tardis_blocks", null);
            } else {
                MIDDLE_BLOCKS = Arrays.asList(new String[]{"COBBLESTONE", "MOSSY_COBBLESTONE", "LOG", "LOG_2", "STONE", "DIRT", "WOOD", "SANDSTONE", "WOOL", "BRICK", "NETHERRACK", "SOUL_SAND", "SMOOTH_BRICK", "HUGE_MUSHROOM_1", "HUGE_MUSHROOM_2", "ENDER_STONE", "QUARTZ_BLOCK", "CLAY", "STAINED_CLAY", "HAY_BLOCK", "HARD_CLAY", "PACKED_ICE"});
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
            List<Integer> CHAM_BLOCKS = Arrays.asList(new Integer[]{1, 3, 4, 5, 7, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24, 25, 35, 41, 42, 43, 45, 46, 47, 48, 49, 56, 57, 58, 73, 74, 79, 80, 82, 84, 86, 87, 88, 89, 91, 98, 99, 100, 103, 110, 112, 121, 123, 124, 129, 133, 155, 159, 161, 162, 170, 172, 173, 174});
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
        }
        // add lamp blocks
        if (!blocks_config.contains("lamp_blocks")) {
            List<Integer> LAMP_BLOCKS = Arrays.asList(new Integer[]{50, 76, 89, 91, 123});
            blocks_config.set("lamp_blocks", LAMP_BLOCKS);
            i++;
        }
        if (!blocks_config.contains("under_door_blocks")) {
            List<Integer> UNDER_BLOCKS = Arrays.asList(new Integer[]{0, 6, 8, 9, 10, 11, 18, 20, 26, 27, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 44, 46, 50, 51, 53, 54, 55, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 75, 76, 77, 78, 79, 81, 83, 85, 89, 92, 93, 94, 96, 101, 102, 104, 105, 106, 107, 108, 109, 111, 113, 114, 115, 116, 117, 118, 119, 120, 122, 126, 128, 130, 131, 132, 134, 135, 136, 161, 171});
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
                plugin.console.sendMessage(plugin.pluginName + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to blocks.yml");
            }
        } catch (IOException io) {
            plugin.debug("Could not save blocks.yml, " + io);
        }
    }

    private void checkArtronConfig() {
        int i = 0;
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
                plugin.console.sendMessage(plugin.pluginName + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to artron.yml");
            }
        } catch (IOException io) {
            plugin.debug("Could not save artron.yml, " + io);
        }
    }
}
