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
import me.eccentric_nz.tardischunkgenerator.TARDISPlanetData;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
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
        boolean save = false;
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
                }
            }
            plugin.getConfig().set("worlds", null);
            plugin.saveConfig();
            planets_config.set("planets.Skaro.gamemode", "SURVIVAL");
            planets_config.set("planets.Skaro.time_travel", true);
            planets_config.set("planets.Skaro.world_type", "BUFFET");
            planets_config.set("planets.Skaro.environment", "NORMAL");
            planets_config.set("planets.Siluria.gamemode", "SURVIVAL");
            planets_config.set("planets.Siluria.time_travel", true);
            planets_config.set("planets.Siluria.world_type", "BUFFET");
            planets_config.set("planets.Siluria.environment", "NORMAL");
            planets_config.set("planets.Gallifrey.gamemode", "SURVIVAL");
            planets_config.set("planets.Gallifrey.time_travel", true);
            planets_config.set("planets.Gallifrey.world_type", "BUFFET");
            planets_config.set("planets.Gallifrey.environment", "NORMAL");
            if (planets_config.contains("planets.TARDIS_Zero_Room")) {
                planets_config.set("planets.TARDIS_Zero_Room.enabled", false);
                planets_config.set("planets.TARDIS_Zero_Room.time_travel", false);
                planets_config.set("planets.TARDIS_Zero_Room.resource_pack", "default");
                planets_config.set("planets.TARDIS_Zero_Room.gamemode", plugin.getConfig().getString("creation.gamemode").toUpperCase(Locale.ENGLISH));
                planets_config.set("planets.TARDIS_Zero_Room.world_type", "FLAT");
                planets_config.set("planets.TARDIS_Zero_Room.environment", "NORMAL");
                planets_config.set("planets.TARDIS_Zero_Room.void", true);
            }
            String dn = plugin.getConfig().getString("creation.default_world_name");
            planets_config.set("planets." + dn + ".enabled", true);
            planets_config.set("planets." + dn + ".gamemode", plugin.getConfig().getString("creation.gamemode").toUpperCase(Locale.ENGLISH));
            planets_config.set("planets." + dn + ".time_travel", false);
            planets_config.set("planets." + dn + ".world_type", "FLAT");
            planets_config.set("planets." + dn + ".environment", "NORMAL");
            save = true;
        }
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
