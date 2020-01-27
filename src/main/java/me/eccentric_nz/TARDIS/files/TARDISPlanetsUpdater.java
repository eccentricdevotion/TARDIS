/*
 * Copyright (C) 2020 eccentric_nz
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
import me.eccentric_nz.tardischunkgenerator.TARDISPlanetData;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

/**
 * @author eccentric_nz
 */
public class TARDISPlanetsUpdater {

    private final TARDIS plugin;
    private final FileConfiguration planets_config;

    public TARDISPlanetsUpdater(TARDIS plugin, FileConfiguration planets_config) {
        this.plugin = plugin;
        this.planets_config = planets_config;
    }

    public void checkPlanetsConfig() {
        int save = 0;
        String dn = plugin.getConfig().getString("creation.default_world_name");
        if (plugin.getConfig().contains("worlds")) {
            Set<String> worlds = plugin.getConfig().getConfigurationSection("worlds").getKeys(false);
            for (String w : worlds) {
                if (!planets_config.contains("planets." + w)) {
                    // get level data
                    TARDISPlanetData data = plugin.getTardisHelper().getLevelData(w);
                    planets_config.set("planets." + w + ".enabled", true);
                    planets_config.set("planets." + w + ".time_travel", plugin.getConfig().getBoolean("worlds." + w));
                    planets_config.set("planets." + w + ".resource_pack", "default");
                    planets_config.set("planets." + w + ".gamemode", data.getGameMode().toString());
                    planets_config.set("planets." + w + ".world_type", data.getWorldType().toString());
                    planets_config.set("planets." + w + ".environment", data.getEnvironment().toString());
                    if (w.startsWith("TARDIS_") || w.equals(plugin.getConfig().getString("creation.default_world_name"))) {
                        planets_config.set("planets." + w + ".generator", "TARDISChunkGenerator");
                    } else {
                        planets_config.set("planets." + w + ".generator", "DEFAULT");
                    }
                }
            }
            plugin.getConfig().set("worlds", null);
            plugin.saveConfig();
            planets_config.set("planets.Skaro.gamemode", "SURVIVAL");
            planets_config.set("planets.Skaro.time_travel", true);
            planets_config.set("planets.Skaro.world_type", "BUFFET");
            planets_config.set("planets.Skaro.environment", "NORMAL");
            planets_config.set("planets.Skaro.keep_spawn_in_memory", false);
            planets_config.set("planets.Siluria.gamemode", "SURVIVAL");
            planets_config.set("planets.Siluria.time_travel", true);
            planets_config.set("planets.Siluria.world_type", "BUFFET");
            planets_config.set("planets.Siluria.environment", "NORMAL");
            planets_config.set("planets.Siluria.keep_spawn_in_memory", false);
            planets_config.set("planets.Gallifrey.gamemode", "SURVIVAL");
            planets_config.set("planets.Gallifrey.time_travel", true);
            planets_config.set("planets.Gallifrey.world_type", "BUFFET");
            planets_config.set("planets.Gallifrey.environment", "NORMAL");
            planets_config.set("planets.Gallifrey.keep_spawn_in_memory", false);
            if (planets_config.contains("planets.TARDIS_Zero_Room")) {
                planets_config.set("planets.TARDIS_Zero_Room.enabled", false);
                planets_config.set("planets.TARDIS_Zero_Room.time_travel", false);
                planets_config.set("planets.TARDIS_Zero_Room.resource_pack", "default");
                planets_config.set("planets.TARDIS_Zero_Room.gamemode", plugin.getConfig().getString("creation.gamemode").toUpperCase(Locale.ENGLISH));
                planets_config.set("planets.TARDIS_Zero_Room.world_type", "FLAT");
                planets_config.set("planets.TARDIS_Zero_Room.environment", "NORMAL");
                planets_config.set("planets.TARDIS_Zero_Room.generator", "TARDISChunkGenerator");
                planets_config.set("planets.TARDIS_Zero_Room.void", true);
                planets_config.set("planets.TARDIS_Zero_Room.keep_spawn_in_memory", false);
            }
            planets_config.set("planets." + dn + ".enabled", true);
            planets_config.set("planets." + dn + ".time_travel", false);
            planets_config.set("planets." + dn + ".resource_pack", "default");
            planets_config.set("planets." + dn + ".gamemode", plugin.getConfig().getString("creation.gamemode").toUpperCase(Locale.ENGLISH));
            planets_config.set("planets." + dn + ".world_type", "FLAT");
            planets_config.set("planets." + dn + ".environment", "NORMAL");
            planets_config.set("planets." + dn + ".generator", "TARDISChunkGenerator");
            planets_config.set("planets." + dn + ".void", true);
            planets_config.set("planets." + dn + ".gamerules.doWeatherCycle", false);
            planets_config.set("planets." + dn + ".gamerules.doDaylightCycle", false);
            planets_config.set("planets." + dn + ".keep_spawn_in_memory", false);
            save++;
        }
        if (!planets_config.contains("planets.TARDIS_Zero_Room.gamerules.doWeatherCycle")) {
            planets_config.set("planets." + dn + ".gamerules.doWeatherCycle", false);
            planets_config.set("planets." + dn + ".gamerules.doDaylightCycle", false);
            planets_config.set("planets.TARDIS_Zero_Room.gamerules.doWeatherCycle", false);
            planets_config.set("planets.TARDIS_Zero_Room.gamerules.doDaylightCycle", false);
            planets_config.set("planets.TARDIS_Zero_Room.gamerules.announceAdvancements", false);
            save++;
        }
        if (!planets_config.contains("planets.Skaro.generator")) {
            for (String w : planets_config.getConfigurationSection("planets").getKeys(false)) {
                if (w.startsWith("TARDIS_") || w.equals(plugin.getConfig().getString("creation.default_world_name"))) {
                    planets_config.set("planets." + w + ".generator", "TARDISChunkGenerator");
                } else {
                    planets_config.set("planets." + w + ".generator", "DEFAULT");
                }
            }
            save++;
        }
        if (planets_config.contains("planets.Siluria.false_nether")) {
            planets_config.set("planets.Siluria.false_nether", null);
            save++;
        }
        if (!planets_config.contains("planets.Skaro.flying_daleks") || !planets_config.contains("planets.Skaro.acid")) {
            planets_config.set("planets.Skaro.flying_daleks", true);
            planets_config.set("planets.Skaro.acid", true);
            planets_config.set("planets.Skaro.acid_damage", 5);
            planets_config.set("planets.Skaro.acid_potions", Arrays.asList("WEAKNESS", "POISON"));
            planets_config.set("planets.Skaro.rust", true);
            save++;
        }
        if (planets_config.contains("default_resource_pack") && planets_config.getString("default_resource_pack").equalsIgnoreCase("https://dl.dropboxusercontent.com/u/53758864/rp/Default.zip")) {
            planets_config.set("default_resource_pack", "https://www.dropbox.com/s/utka3zxmer7f19g/Default.zip?dl=1");
            save++;
        }
        if (planets_config.contains("planets.Skaro.resource_pack") && (planets_config.getString("planets.Skaro.resource_pack").equalsIgnoreCase("https://dl.dropboxusercontent.com/u/53758864/rp/Skaro.zip") || planets_config.getString("planets.Skaro.resource_pack").equalsIgnoreCase("default"))) {
            planets_config.set("planets.Skaro.resource_pack", "https://www.dropbox.com/s/nr93rhbiyw2s5d0/Skaro.zip?dl=1");
            save++;
        }
        if (!planets_config.contains("planets.Siluria.enabled")) {
            planets_config.set("planets.Siluria.enabled", false);
            planets_config.set("planets.Siluria.resource_pack", "default");
            save++;
        }
        if (!planets_config.contains("planets.Gallifrey.enabled")) {
            planets_config.set("planets.Gallifrey.enabled", false);
            planets_config.set("planets.Gallifrey.resource_pack", "https://www.dropbox.com/s/i7bpjju9jrgclq7/Gallifrey.zip?dl=1");
            save++;
        }
        if (planets_config.contains("planets.Gallifrey.resource_pack") && planets_config.getString("planets.Gallifrey.resource_pack").equalsIgnoreCase("default")) {
            planets_config.set("planets.Gallifrey.resource_pack", "https://www.dropbox.com/s/i7bpjju9jrgclq7/Gallifrey.zip?dl=1");
            save++;
        }
        for (String p : planets_config.getConfigurationSection("planets").getKeys(false)) {
            if (!planets_config.contains("planets." + p + ".keep_spawn_in_memory")) {
                planets_config.set("planets." + p + ".keep_spawn_in_memory", false);
                save++;
            }
        }
        if (save > 0) {
            try {
                String planetsPath = plugin.getDataFolder() + File.separator + "planets.yml";
                planets_config.save(new File(planetsPath));
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Added " + ChatColor.AQUA + save + ChatColor.RESET + " new item to planets.yml");
            } catch (IOException io) {
                plugin.debug("Could not save planets.yml, " + io.getMessage());
            }
        }
    }
}
