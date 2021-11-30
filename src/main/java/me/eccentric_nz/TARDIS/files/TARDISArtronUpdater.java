/*
 * Copyright (C) 2021 eccentric_nz
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
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author eccentric_nz
 */
public class TARDISArtronUpdater {

    private final TARDIS plugin;
    private final FileConfiguration artron_config;
    private final HashMap<String, Boolean> booleanOptions = new HashMap<>();
    private final HashMap<String, String> stringOptions = new HashMap<>();
    private final HashMap<String, Double> doubleOptions = new HashMap<>();
    private final HashMap<String, Integer> integerOptions = new HashMap<>();

    public TARDISArtronUpdater(TARDIS plugin) {
        this.plugin = plugin;
        artron_config = plugin.getArtronConfig();
        // boolean
        booleanOptions.put("artron_furnace.particles", false);
        // double
        doubleOptions.put("artron_furnace.burn_time", 0.5);
        doubleOptions.put("artron_furnace.cook_time", 0.5);
        // integer
        integerOptions.put("artron_furnace.burn_limit", 100000);
        integerOptions.put("autonomous", 100);
        integerOptions.put("backdoor", 100);
        integerOptions.put("comehere", 400);
        integerOptions.put("creeper_recharge", 150);
        integerOptions.put("custard_cream", 25);
        integerOptions.put("full_charge", 5000);
        integerOptions.put("hide", 500);
        integerOptions.put("jettison", 75);
        integerOptions.put("just_wall_floor", 50);
        integerOptions.put("lightning_recharge", 300);
        integerOptions.put("nether_min", 4250);
        integerOptions.put("player", 25);
        integerOptions.put("random", 75);
        integerOptions.put("random_circuit", 150);
        integerOptions.put("recharge_distance", 20);
        integerOptions.put("render", 250);
        integerOptions.put("siege_creeper", 150);
        integerOptions.put("siege_deplete", 100);
        integerOptions.put("siege_ticks", 1500);
        integerOptions.put("siege_transfer", 10);
        integerOptions.put("sonic_generator.standard", 10);
        integerOptions.put("sonic_generator.bio", 10);
        integerOptions.put("sonic_generator.diamond", 10);
        integerOptions.put("sonic_generator.emerald", 10);
        integerOptions.put("sonic_generator.redstone", 10);
        integerOptions.put("sonic_generator.painter", 10);
        integerOptions.put("sonic_generator.ignite", 10);
        integerOptions.put("sonic_generator.arrow", 10);
        integerOptions.put("shell", 500);
        integerOptions.put("standby", 5);
        integerOptions.put("standby_time", 6000);
        integerOptions.put("the_end_min", 5500);
        integerOptions.put("travel", 100);
        integerOptions.put("zero", 250);
        integerOptions.put("upgrades.ars", 5000);
        integerOptions.put("upgrades.bigger", 7500);
        integerOptions.put("upgrades.budget", 5000);
        integerOptions.put("upgrades.cave", 5000);
        integerOptions.put("upgrades.copper", 20000);
        integerOptions.put("upgrades.coral", 8000);
        integerOptions.put("upgrades.deluxe", 10000);
        integerOptions.put("upgrades.eleventh", 10000);
        integerOptions.put("upgrades.ender", 5000);
        integerOptions.put("upgrades.factory", 7500);
        integerOptions.put("upgrades.legacy_bigger", 7500);
        integerOptions.put("upgrades.legacy_budget", 5000);
        integerOptions.put("upgrades.legacy_deluxe", 10000);
        integerOptions.put("upgrades.legacy_eleventh", 10000);
        integerOptions.put("upgrades.legacy_redstone", 8000);
        integerOptions.put("upgrades.master", 10000);
        integerOptions.put("upgrades.delta", 7500);
        integerOptions.put("upgrades.plank", 5000);
        integerOptions.put("upgrades.pyramid", 5000);
        integerOptions.put("upgrades.redstone", 7500);
        integerOptions.put("upgrades.rotor", 5000);
        integerOptions.put("upgrades.steampunk", 5000);
        integerOptions.put("upgrades.tom", 5000);
        integerOptions.put("upgrades.twelfth", 7500);
        integerOptions.put("upgrades.thirteenth", 8000);
        integerOptions.put("upgrades.war", 5000);
        integerOptions.put("upgrades.weathered", 5000);
        integerOptions.put("upgrades.template.small", 1666);
        integerOptions.put("upgrades.template.medium", 2500);
        integerOptions.put("upgrades.template.tall", 3333);
        integerOptions.put("upgrades.custom", 10000);
        integerOptions.put("upgrades.archive.small", 5000);
        integerOptions.put("upgrades.archive.medium", 7500);
        integerOptions.put("upgrades.archive.tall", 10000);
        integerOptions.put("upgrades.archive.massive", 20000);
        // string
        stringOptions.put("jettison_seed", "TNT");
        stringOptions.put("full_charge_item", "NETHER_STAR");
    }

    public void checkArtronConfig() {
        int i = 0;
        // boolean values
        for (Map.Entry<String, Boolean> entry : booleanOptions.entrySet()) {
            if (!artron_config.contains(entry.getKey())) {
                artron_config.set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // double values
        for (Map.Entry<String, Double> entry : doubleOptions.entrySet()) {
            if (!artron_config.contains(entry.getKey())) {
                artron_config.set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // int values
        for (Map.Entry<String, Integer> entry : integerOptions.entrySet()) {
            if (!artron_config.contains(entry.getKey())) {
                artron_config.set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // string values
        for (Map.Entry<String, String> entry : stringOptions.entrySet()) {
            if (!artron_config.contains(entry.getKey())) {
                artron_config.set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        if (artron_config.contains("artron_furnace.set_biome")) {
            plugin.getConfig().set("artron_furnace.set_biome", null);
        }
        try {
            artron_config.save(new File(plugin.getDataFolder(), "artron.yml"));
            if (i > 0) {
                plugin.getLogger().log(Level.INFO, "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to artron.yml");
            }
        } catch (IOException io) {
            plugin.debug("Could not save artron.yml, " + io.getMessage());
        }
    }
}
