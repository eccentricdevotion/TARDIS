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
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

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
