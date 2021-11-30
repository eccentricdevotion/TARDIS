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
import me.eccentric_nz.tardischunkgenerator.helpers.TARDISPlanetData;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;

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
        if (!planets_config.contains("planets.TARDIS_Zero_Room.alias")) {
            Set<String> worlds = planets_config.getConfigurationSection("planets").getKeys(false);
            for (String w : worlds) {
                if (w.equals("TARDIS_TimeVortex")) {
                    planets_config.set("planets." + w + ".alias", "TimeVortex");
                } else if (w.endsWith("TARDIS_Zero_Room")) {
                    planets_config.set("planets." + w + ".alias", "ZeroRoom");
                } else if (w.endsWith("gallifrey")) {
                    planets_config.set("planets." + w + ".alias", "Gallifrey");
                } else if (w.endsWith("siluria")) {
                    planets_config.set("planets." + w + ".alias", "Siluria");
                } else if (w.endsWith("skaro")) {
                    planets_config.set("planets." + w + ".alias", "Skaro");
                } else if (w.endsWith("_the_end")) {
                    planets_config.set("planets." + w + ".alias", "TheEnd");
                } else if (w.endsWith("_nether")) {
                    planets_config.set("planets." + w + ".alias", "TheNether");
                } else {
                    planets_config.set("planets." + w + ".alias", w);
                }
            }
            plugin.getPlanetsConfig().set("planets.Gallifrey", null);
            plugin.getPlanetsConfig().set("planets.Siluria", null);
            plugin.getPlanetsConfig().set("planets.Skaro", null);
        }
        if (!planets_config.contains("planets.TARDIS_Zero_Room.gamerules.doWeatherCycle")) {
            planets_config.set("planets." + dn + ".gamerules.doWeatherCycle", false);
            planets_config.set("planets." + dn + ".gamerules.doDaylightCycle", false);
            planets_config.set("planets.TARDIS_Zero_Room.gamerules.doWeatherCycle", false);
            planets_config.set("planets.TARDIS_Zero_Room.gamerules.doDaylightCycle", false);
            planets_config.set("planets.TARDIS_Zero_Room.gamerules.announceAdvancements", false);
            save++;
        }
        if (planets_config.contains("default_resource_pack") && planets_config.getString("default_resource_pack").equalsIgnoreCase("https://dl.dropboxusercontent.com/u/53758864/rp/Default.zip")) {
            planets_config.set("default_resource_pack", "https://www.dropbox.com/s/utka3zxmer7f19g/Default.zip?dl=1");
            save++;
        }
        for (String p : planets_config.getConfigurationSection("planets").getKeys(false)) {
            if (!planets_config.contains("planets." + p + ".keep_spawn_in_memory")) {
                planets_config.set("planets." + p + ".keep_spawn_in_memory", false);
                save++;
            }
            if (!planets_config.contains("planets." + p + ".allow_portals")) {
                planets_config.set("planets." + p + ".allow_portals", true);
                save++;
            }
        }
        if (save > 0) {
            try {
                String planetsPath = plugin.getDataFolder() + File.separator + "planets.yml";
                planets_config.save(new File(planetsPath));
                plugin.getLogger().log(Level.INFO, "Added " + ChatColor.AQUA + save + ChatColor.RESET + " new items to planets.yml");
            } catch (IOException io) {
                plugin.debug("Could not save planets.yml, " + io.getMessage());
            }
        }
    }
}
