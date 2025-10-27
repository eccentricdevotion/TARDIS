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
package me.eccentric_nz.tardisweepingangels.utils;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonstersConfig {

    private final TARDIS plugin;
    private final HashMap<String, List<String>> listOptions = new HashMap<>();
    private final HashMap<String, String> strOptions = new HashMap<>();
    private final HashMap<String, Integer> intOptions = new HashMap<>();
    private final HashMap<String, Double> doubleOptions = new HashMap<>();
    private final HashMap<String, Boolean> boolOptions = new HashMap<>();
    private final FileConfiguration config;

    public MonstersConfig(TARDIS plugin) {
        this.plugin = plugin;
        File monstersFile = new File(plugin.getDataFolder(), "monsters.yml");
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
        listOptions.put("angel_of_liberty.drops", List.of("TORCH", "OXIDIZED_COPPER"));
        listOptions.put("angels.drops", List.of("STONE", "COBBLESTONE"));
        listOptions.put("angels.teleport_worlds", List.of("world"));
        listOptions.put("cybermen.drops", List.of("REDSTONE", "STONE_BUTTON"));
        listOptions.put("daleks.dalek_sec_drops", List.of("VERDANT_FROGLIGHT", "LEATHER_CHESTPLATE"));
        listOptions.put("daleks.davros_drops", List.of("IRON_CHAIN", "CRIMSON_BUTTON"));
        listOptions.put("daleks.drops", List.of("SLIME_BALL", "ROTTEN_FLESH"));
        listOptions.put("empty_child.drops", List.of("COOKED_BEEF", "SUGAR"));
        listOptions.put("hath.drops", List.of("SALMON", "STONE_PICKAXE"));
        listOptions.put("headless_monks.drops", List.of("BOOK", "RED_CANDLE"));
        listOptions.put("heavenly_hosts.drops", List.of("GOLD_NUGGET", "PAPER"));
        listOptions.put("ice_warriors.drops", List.of("ICE", "PACKED_ICE", "SNOW_BLOCK"));
        listOptions.put("nimon.drops", List.of("BEEF", "POLISHED_BLACKSTONE"));
        listOptions.put("omega.drops", List.of("GOLD_INGOT", "CLOCK"));
        listOptions.put("ood.drops", List.of("NAME_TAG"));
        listOptions.put("ossified.drops", List.of("CHARCOAL", "FIREWORK_STAR", "DEAD_BRAIN_CORAL"));
        listOptions.put("racnoss.drops", List.of("NETHERITE_INGOT", "ECHO_SHARD"));
        listOptions.put("sea_devils.drops", List.of("COD", "KELP"));
        listOptions.put("silent.drops", List.of("INK_SAC", "FLOWER_POT"));
        listOptions.put("silurians.drops", List.of("GOLD_NUGGET", "FEATHER"));
        listOptions.put("slitheen.drops", List.of("RABBIT_HIDE", "PHANTOM_MEMBRANE"));
        listOptions.put("smilers.drops", List.of("COMPARATOR", "CHERRY_LOG"));
        listOptions.put("sontarans.drops", List.of("POTATO", "POISONOUS_POTATO"));
        listOptions.put("sutekh.drops", List.of("GLOWSTONE_DUST", "LIGHTNING_ROD"));
        listOptions.put("the_beast.drops", List.of("MAGMA_BLOCK", "IRON_CHAIN"));
        listOptions.put("the_mire.drops", List.of("HONEY_BOTTLE", "POTION"));
        listOptions.put("toclafane.drops", List.of("GUNPOWDER", "HONEYCOMB"));
        listOptions.put("vampires.drops", List.of("TROPICAL_FISH", "FISHING_ROD"));
        listOptions.put("vashta_nerada.drops", List.of("BONE", "LEATHER"));
        listOptions.put("zygons.drops", List.of("PAINTING", "SAND"));
        // boolean
        boolOptions.put("angels.angels_can_steal", true);
        boolOptions.put("angels.teleport_to_location", false);
        boolOptions.put("angels.can_build", true);
        boolOptions.put("angels.spawn_from_chat.enabled", true);
        boolOptions.put("cybermen.can_upgrade", true);
        boolOptions.put("headless_monks.particles", true);
        boolOptions.put("silurians.check_slime_chunk", false);
        boolOptions.put("sontarans.can_tame", true);
        boolOptions.put("judoon.guards", true);
        boolOptions.put("judoon.can_build", true);
        boolOptions.put("k9.can_build", true);
        boolOptions.put("k9.by_taming", true);
        boolOptions.put("toclafane.destroy_blocks", true);
        boolOptions.put("custom_spawners", true);
        // float
        double min_version = 2.0d;
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
            Location location = plugin.getServer().getWorlds().getFirst().getSpawnLocation();
            String spawn = location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
            List<String> list = List.of(spawn);
            plugin.getMonstersConfig().set("angels.teleport_locations", list);
            i++;
        }
        // convert CHAIN to IRON_CHAIN - 1.21.10
        List<String> drops = config.getStringList("daleks.davros_drops");
        if (!drops.isEmpty() && drops.contains("CHAIN")) {
            drops.remove("CHAIN");
            drops.add("IRON_CHAIN");
            plugin.getMonstersConfig().set("daleks.davros_drops", drops);
            i++;
        }
        List<String> bdrops = config.getStringList("the_beast.drops");
        if (!bdrops.isEmpty() && bdrops.contains("CHAIN")) {
            bdrops.remove("CHAIN");
            bdrops.add("COPPER_CHAIN");
            plugin.getMonstersConfig().set("the_beast.drops", bdrops);
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
