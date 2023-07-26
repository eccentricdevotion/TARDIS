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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonstersConfig {

    private final double min_version = 2.0d;
    private final TARDIS plugin;
    private final HashMap<String, List<String>> listOptions = new HashMap<>();
    private final HashMap<String, String> strOptions = new HashMap<>();
    private final HashMap<String, Integer> intOptions = new HashMap<>();
    private final HashMap<String, Double> doubleOptions = new HashMap<>();
    private final HashMap<String, Boolean> boolOptions = new HashMap<>();
    private final FileConfiguration config;
    private final File monstersFile;

    public MonstersConfig(TARDIS plugin) {
        this.plugin = plugin;
        monstersFile = new File(plugin.getDataFolder(), "monsters.yml");
        config = YamlConfiguration.loadConfiguration(monstersFile);
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
        boolOptions.put("angels.teleport_to_location", false);
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
        int i = 0;
        // int values
        for (Map.Entry<String, Integer> entry : intOptions.entrySet()) {
            if (!config.contains(entry.getKey())) {
                plugin.getMonstersConfig().set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // string values
        for (Map.Entry<String, String> entry : strOptions.entrySet()) {
            if (!config.contains(entry.getKey())) {
                plugin.getMonstersConfig().set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // list values
        for (Map.Entry<String, List<String>> entry : listOptions.entrySet()) {
            if (!config.contains(entry.getKey())) {
                plugin.getMonstersConfig().set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // boolean values
        for (Map.Entry<String, Boolean> entry : boolOptions.entrySet()) {
            if (!config.contains(entry.getKey())) {
                plugin.getMonstersConfig().set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // double values
        for (Map.Entry<String, Double> entry : doubleOptions.entrySet()) {
            if (!config.contains(entry.getKey())) {
                plugin.getMonstersConfig().set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // set angels.teleport_locations to world spawn
        if (!config.contains("angels.teleport_locations")) {
            Location location = plugin.getServer().getWorlds().get(0).getSpawnLocation();
            String spawn = location.getWorld().getName()+","+location.getBlockX()+","+location.getBlockY()+","+location.getBlockZ();
            List<String> list = Arrays.asList(spawn);
            plugin.getMonstersConfig().set("angels.teleport_locations", list);
            i++;
        }
        if (i > 0) {
            try {
                String monstersPath = plugin.getDataFolder() + File.separator + "monsters.yml";
                plugin.getMonstersConfig().save(new File(monstersPath));
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.MONSTERS, "Added " + i + " new items to monsters.yml");
            } catch (IOException io) {
                plugin.debug("Could not save monsters.yml, " + io.getMessage());
            }
        }
    }
}
