/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.tardisweepingangels.utils;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

    final double min_version = 2.0d;
    private final TARDISWeepingAngels plugin;
    HashMap<String, List<String>> listOptions = new HashMap<>();
    HashMap<String, String> strOptions = new HashMap<>();
    HashMap<String, Integer> intOptions = new HashMap<>();
    HashMap<String, Double> doubleOptions = new HashMap<>();
    HashMap<String, Boolean> boolOptions = new HashMap<>();
    private FileConfiguration config;
    private File configFile;

    public Config(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
        configFile = new File(plugin.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        // integer
        intOptions.put("angels.freeze_time", 100);
        intOptions.put("daleks.dalek_sec_chance", 5);
        intOptions.put("daleks.davros_chance", 5);
        intOptions.put("spawn_rate.how_many", 2);
        intOptions.put("spawn_rate.how_often", 400);
        intOptions.put("spawn_rate.default_max", 0);
        intOptions.put("angels.spawn_from_chat.chance", 50);
        intOptions.put("angels.spawn_from_chat.distance_from_player", 10);
        intOptions.put("judoon.ammunition", 25);
        intOptions.put("judoon.damage", 4);
        intOptions.put("ood.spawn_from_villager", 20);
        intOptions.put("ood.spawn_from_cured", 5);
        intOptions.put("toclafane.spawn_from_bee", 5);
        // string
        strOptions.put("angels.weapon", "DIAMOND_PICKAXE");
        strOptions.put("headless_monks.projectile", "SMALL_FIREBALL");
        // list
        listOptions.put("angels.drops", Arrays.asList("STONE", "COBBLESTONE"));
        listOptions.put("angels.teleport_worlds", Arrays.asList("world"));
        listOptions.put("cybermen.drops", Arrays.asList("REDSTONE", "STONE_BUTTON"));
        listOptions.put("daleks.drops", Arrays.asList("SLIME_BALL", "ROTTEN_FLESH"));
        listOptions.put("daleks.dalek_sec_drops", Arrays.asList("VERDANT_FROGLIGHT", "LEATHER_CHESTPLATE"));
        listOptions.put("daleks.davros_drops", Arrays.asList("CHAIN", "CRIMSON_BUTTON"));
        listOptions.put("empty_child.drops", Arrays.asList("COOKED_BEEF", "SUGAR"));
        listOptions.put("hath.drops", Arrays.asList("SALMON", "STONE_PICKAXE"));
        listOptions.put("headless_monks.drops", Arrays.asList("BOOK", "RED_CANDLE"));
        listOptions.put("ice_warriors.drops", Arrays.asList("ICE", "PACKED_ICE", "SNOW_BLOCK"));
        listOptions.put("silent.drops", Arrays.asList("INK_SAC", "FLOWER_POT"));
        listOptions.put("ood.drops", Arrays.asList("NAME_TAG"));
        listOptions.put("racnoss.drops", Arrays.asList("NETHERITE_INGOT", "ECHO_SHARD"));
        listOptions.put("sea_devils.drops", Arrays.asList("COD", "KELP"));
        listOptions.put("silurians.drops", Arrays.asList("GOLD_NUGGET", "FEATHER"));
        listOptions.put("slitheen.drops", Arrays.asList("RABBIT_HIDE", "PHANTOM_MEMBRANE"));
        listOptions.put("sontarans.drops", Arrays.asList("POTATO", "POISONOUS_POTATO"));
        listOptions.put("the_mire.drops", Arrays.asList("HONEY_BOTTLE", "POTION"));
        listOptions.put("toclafane.drops", Arrays.asList("GUNPOWDER", "HONEYCOMB"));
        listOptions.put("vashta_nerada.drops", Arrays.asList("BONE", "LEATHER"));
        listOptions.put("zygons.drops", Arrays.asList("PAINTING", "SAND"));
        // boolean
        boolOptions.put("angels.angels_can_steal", true);
        boolOptions.put("angels.can_build", true);
        boolOptions.put("angels.spawn_from_chat.enabled", true);
        boolOptions.put("cybermen.can_upgrade", true);
        boolOptions.put("headless_monks.particles", true);
        boolOptions.put("sontarans.can_tame", true);
        boolOptions.put("judoon.guards", true);
        boolOptions.put("judoon.can_build", true);
        boolOptions.put("k9.can_build", true);
        boolOptions.put("k9.by_taming", true);
        boolOptions.put("toclafane.destroy_blocks", true);
        // float
        doubleOptions.put("config_version", min_version);
    }

    public void updateConfig() {
        // clear the old spawn_rate settings
        if (config.contains("angels.spawn_rate")) {
            plugin.getConfig().set("angels.spawn_rate", null);
            plugin.getConfig().set("cybermen.spawn_rate", null);
            plugin.getConfig().set("daleks.spawn_rate", null);
            plugin.getConfig().set("empty_child.spawn_rate", null);
            plugin.getConfig().set("ice_warriors.spawn_rate", null);
            plugin.getConfig().set("silurians.spawn_rate", null);
            plugin.getConfig().set("sontarans.spawn_rate", null);
            plugin.getConfig().set("vashta_nerada.spawn_rate", null);
            plugin.getConfig().set("zygons.spawn_rate", null);
        }

        int i = 0;
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
        // list values
        for (Map.Entry<String, List<String>> entry : listOptions.entrySet()) {
            if (!config.contains(entry.getKey())) {
                plugin.getConfig().set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // boolean values
        for (Map.Entry<String, Boolean> entry : boolOptions.entrySet()) {
            if (!config.contains(entry.getKey())) {
                plugin.getConfig().set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // double values
        for (Map.Entry<String, Double> entry : doubleOptions.entrySet()) {
            if (!config.contains(entry.getKey())) {
                plugin.getConfig().set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // fix wrong config node name
        if (config.contains("angels.angel_tp_worlds")) {
            List<String> tpws = config.getStringList("angels.angel_tp_worlds");
            plugin.getConfig().set("angels.teleport_worlds", tpws);
            plugin.getConfig().set("angels.angel_tp_worlds", null);
        }
        // remove milk bucket from Sontaran drops
        List<String> sontaran_old = config.getStringList("sontarans.drops");
        if (sontaran_old.contains("MILK_BUCKET")) {
            sontaran_old.remove("MILK_BUCKET");
            sontaran_old.add("POISONOUS_POTATO");
            plugin.getConfig().set("sontarans.drops", sontaran_old);
        }
        // set POTATO_ITEM to POTATO
        if (sontaran_old.contains("POTATO_ITEM")) {
            sontaran_old.remove("POTATO_ITEM");
            sontaran_old.add("POTATO");
            plugin.getConfig().set("sontarans.drops", sontaran_old);
        }
        // set INK_SACK to INK_SAC, FLOWER_POT_ITEM to FLOWER_POT
        List<String> silent_old = config.getStringList("silent.drops");
        if (silent_old.contains("INK_SACK")) {
            silent_old.remove("INK_SACK");
            silent_old.add("INK_SAC");
            silent_old.remove("FLOWER_POT_ITEM");
            silent_old.add("FLOWER_POT");
            plugin.getConfig().set("silent.drops", silent_old);
        }
        plugin.saveConfig();
        if (i > 0) {
            plugin.getServer().getConsoleSender().sendMessage(plugin.pluginName + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to config");
        }
    }
}
