/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardischunkgenerator.helpers.TARDISPlanetData;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;
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
                    planets_config.set("planets." + w + ".difficulty", data.getDifficulty().toString());
                    if (w.startsWith("TARDIS_") || w.equals(plugin.getConfig().getString("creation.default_world_name"))) {
                        planets_config.set("planets." + w + ".generator", "TARDIS:void");
                    } else {
                        planets_config.set("planets." + w + ".generator", "DEFAULT");
                    }
                }
            }
            plugin.getConfig().set("worlds", null);
            plugin.saveConfig();
            if (!planets_config.contains("planets.TARDIS_Zero_Room")) {
                planets_config.set("planets.TARDIS_Zero_Room.enabled", false);
                planets_config.set("planets.TARDIS_Zero_Room.time_travel", false);
                planets_config.set("planets.TARDIS_Zero_Room.resource_pack", "default");
                planets_config.set("planets.TARDIS_Zero_Room.gamemode", plugin.getConfig().getString("creation.gamemode").toUpperCase(Locale.ENGLISH));
                planets_config.set("planets.TARDIS_Zero_Room.world_type", "FLAT");
                planets_config.set("planets.TARDIS_Zero_Room.environment", "NORMAL");
                planets_config.set("planets.TARDIS_Zero_Room.difficulty", "NORMAL");
                planets_config.set("planets.TARDIS_Zero_Room.generator", "TARDIS:void");
                planets_config.set("planets.TARDIS_Zero_Room.void", true);
                planets_config.set("planets.TARDIS_Zero_Room.keep_spawn_in_memory", false);
                save++;
            }
            planets_config.set("planets." + dn + ".enabled", true);
            planets_config.set("planets." + dn + ".time_travel", false);
            planets_config.set("planets." + dn + ".resource_pack", "default");
            planets_config.set("planets." + dn + ".gamemode", plugin.getConfig().getString("creation.gamemode").toUpperCase(Locale.ENGLISH));
            planets_config.set("planets." + dn + ".world_type", "FLAT");
            planets_config.set("planets." + dn + ".environment", "NORMAL");
            planets_config.set("planets." + dn + ".difficulty", "NORMAL");
            planets_config.set("planets." + dn + ".generator", "TARDIS:void");
            planets_config.set("planets." + dn + ".void", true);
            planets_config.set("planets." + dn + ".gamerules.doWeatherCycle", false);
            planets_config.set("planets." + dn + ".gamerules.doDaylightCycle", false);
            planets_config.set("planets." + dn + ".keep_spawn_in_memory", false);
            save++;
        }
        if (!planets_config.contains("colour_skies")) {
            planets_config.set("colour_skies", true);
            save++;
        }
        if (!planets_config.contains("planets.TARDIS_Zero_Room.alias")) {
            Set<String> worlds = planets_config.getConfigurationSection("planets").getKeys(false);
            for (String w : worlds) {
                if (w.equals("TARDIS_TimeVortex")) {
                    planets_config.set("planets." + w + ".alias", "TimeVortex");
                } else if (w.endsWith("TARDIS_Zero_Room")) {
                    planets_config.set("planets." + w + ".alias", "ZeroRoom");
                } else if (w.toLowerCase(Locale.ROOT).endsWith("gallifrey")) {
                    planets_config.set("planets." + w + ".alias", "Gallifrey");
                } else if (w.toLowerCase(Locale.ROOT).endsWith("siluria")) {
                    planets_config.set("planets." + w + ".alias", "Siluria");
                } else if (w.toLowerCase(Locale.ROOT).endsWith("skaro")) {
                    planets_config.set("planets." + w + ".alias", "Skaro");
                } else if (w.endsWith("_the_end")) {
                    planets_config.set("planets." + w + ".alias", "TheEnd");
                } else if (w.endsWith("_nether")) {
                    planets_config.set("planets." + w + ".alias", "TheNether");
                } else {
                    planets_config.set("planets." + w + ".alias", w);
                }
            }
        }
        if (!planets_config.contains("planets.TARDIS_Zero_Room.difficulty")) {
            Set<String> worlds = planets_config.getConfigurationSection("planets").getKeys(false);
            // get server.properties value
            String d = getDifficulty();
            for (String w : worlds) {
                planets_config.set("planets." + w + ".difficulty", d);
            }
        }
        if (!planets_config.contains("planets.TARDIS_Zero_Room.icon")) {
            Set<String> worlds = planets_config.getConfigurationSection("planets").getKeys(false);
            // get/set icon material
            for (String w : worlds) {
                planets_config.set("planets." + w + ".icon", getIcon(w, planets_config.getString("planets." + w + ".environment")));
            }
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
        if (!planets_config.contains("planets.gallifrey")) {
            // Skaro
            planets_config.set("planets.skaro.enabled", false);
            planets_config.set("planets.skaro.resource_pack", "default");
            planets_config.set("planets.skaro.acid", true);
            planets_config.set("planets.skaro.acid_damage", 5);
            planets_config.set("planets.skaro.acid_potions", Arrays.asList("WEAKNESS", "POISON"));
            planets_config.set("planets.skaro.rust", true);
            planets_config.set("planets.skaro.flying_daleks", true);
            planets_config.set("planets.skaro.gamemode", "SURVIVAL");
            planets_config.set("planets.skaro.time_travel", true);
            planets_config.set("planets.skaro.world_type", "NORMAL");
            planets_config.set("planets.skaro.environment", "NORMAL");
            planets_config.set("planets.skaro.generator", "TARDIS:skaro");
            planets_config.set("planets.skaro.keep_spawn_in_memory", false);
            planets_config.set("planets.skaro.spawn_other_mobs", true);
            planets_config.set("planets.skaro.gamerules.doTraderSpawning", false);
            planets_config.set("planets.skaro.gamerules.doPatrolSpawning", false);
            planets_config.set("planets.skaro.allow_portals", false);
            planets_config.set("planets.skaro.alias", "Skaro");
            // Siluria
            planets_config.set("planets.siluria.enabled", false);
            planets_config.set("planets.siluria.resource_pack", "default");
            planets_config.set("planets.siluria.gamemode", "SURVIVAL");
            planets_config.set("planets.siluria.time_travel", true);
            planets_config.set("planets.siluria.world_type", "NORMAL");
            planets_config.set("planets.siluria.environment", "NORMAL");
            planets_config.set("planets.siluria.generator", "TARDIS:siluria");
            planets_config.set("planets.siluria.keep_spawn_in_memory", false);
            planets_config.set("planets.siluria.spawn_other_mobs", true);
            planets_config.set("planets.siluria.gamerules.doTraderSpawning", false);
            planets_config.set("planets.siluria.gamerules.doPatrolSpawning", false);
            planets_config.set("planets.siluria.allow_portals", false);
            planets_config.set("planets.siluria.alias", "Siluria");
            // Gallifrey:
            planets_config.set("planets.gallifrey.enabled", false);
            planets_config.set("planets.gallifrey.resource_pack", "default");
            planets_config.set("planets.gallifrey.gamemode", "SURVIVAL");
            planets_config.set("planets.gallifrey.time_travel", true);
            planets_config.set("planets.gallifrey.world_type", "NORMAL");
            planets_config.set("planets.gallifrey.environment", "NORMAL");
            planets_config.set("planets.gallifrey.generator", "TARDIS:gallifrey");
            planets_config.set("planets.gallifrey.keep_spawn_in_memory", false);
            planets_config.set("planets.gallifrey.spawn_other_mobs", true);
            planets_config.set("planets.gallifrey.gamerules.doTraderSpawning", false);
            planets_config.set("planets.gallifrey.gamerules.doPatrolSpawning", false);
            planets_config.set("planets.gallifrey.allow_portals", false);
            planets_config.set("planets.gallifrey.alias", "Gallifrey");
            save++;
        }
        if (!planets_config.contains("planets.gallifrey.villager_blueprints")) {
            planets_config.set("planets.gallifrey.villager_blueprints.enabled", true);
            planets_config.set("planets.gallifrey.villager_blueprints.uses", 1);
            planets_config.set("planets.gallifrey.villager_blueprints.chance", 20);
            save++;
        }
        if (planets_config.getString("planets.skaro.generator").equalsIgnoreCase("DEFAULT")) {
            planets_config.set("planets.gallifrey.generator", "TARDIS:gallifrey");
            planets_config.set("planets.siluria.generator", "TARDIS:siluria");
            planets_config.set("planets.skaro.generator", "TARDIS:skaro");
            save++;
        }
        if (planets_config.getString("planets.TARDIS_TimeVortex.generator").equals("TARDISChunkGenerator")) {
            planets_config.set("planets.TARDIS_TimeVortex.generator", "TARDIS:void");
            planets_config.set("planets.TARDIS_Zero_Room.generator", "TARDIS:void");
            save++;
        }
        if (planets_config.contains("planets.TARDIS_TimeVortex.gamerules.doWardenSpawning")) {
            planets_config.set("planets.TARDIS_TimeVortex.gamerules.doWardenSpawning", false);
            planets_config.set("planets.TARDIS_Zero_Room.gamerules.doWardenSpawning", false);
            save++;
        }
        if (planets_config.getString("planets.TARDIS_TimeVortex.generator").equals("TARDISChunkGenerator:void")) {
            for (String key : planets_config.getConfigurationSection("planets").getKeys(false)) {
                String gen = planets_config.getString("planets." + key + ".generator");
                if (gen != null && gen.contains("TARDISChunkGenerator")) {
                    String[] split = gen.split(":");
                    if (split.length > 1) {
                        planets_config.set("planets." + key + ".generator", "TARDIS:" + split[1]);
                    }
                }
            }
            save++;
        }
        // remove datapack dimension worlds
        String levelName = getLevelName();
        if (planets_config.contains("planets." + levelName + "_tardis_gallifrey")) {
            planets_config.set("planets." + levelName + "_tardis_gallifrey", null);
            save++;
        }
        if (planets_config.contains("planets." + levelName + "_tardis_siluria")) {
            planets_config.set("planets." + levelName + "_tardis_siluria", null);
            save++;
        }
        if (planets_config.contains("planets." + levelName + "_tardis_skaro")) {
            planets_config.set("planets." + levelName + "_tardis_skaro", null);
            save++;
        }
        if (save > 0) {
            try {
                String planetsPath = plugin.getDataFolder() + File.separator + "planets.yml";
                planets_config.save(new File(planetsPath));
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Added " + save + " new items to planets.yml");
            } catch (IOException io) {
                plugin.debug("Could not save planets.yml, " + io.getMessage());
            }
        }
    }

    private String getLevelName() {
        try {
            BufferedReader is = new BufferedReader(new FileReader("server.properties"));
            Properties props = new Properties();
            props.load(is);
            is.close();
            return props.getProperty("level-name");
        } catch (IOException e) {
            return "world"; // minecraft / spigot default
        }
    }

    private String getDifficulty() {
        try {
            BufferedReader is = new BufferedReader(new FileReader("server.properties"));
            Properties props = new Properties();
            props.load(is);
            is.close();
            return props.getProperty("difficulty").toUpperCase();
        } catch (IOException e) {
            return "EASY"; // minecraft / spigot default
        }
    }

    private String getIcon(String world, String env) {
        String icon;
        switch (world) {
            case "TARDIS_TimeVortex" -> icon = "CRYING_OBSIDIAN";
            case "TARDIS_Zero_Room" -> icon = "PINK_WOOL";
            case "skaro" -> icon = "FIRE_CORAL_BLOCK";
            case "siluria" -> icon = "BAMBOO_MOSAIC";
            case "gallifrey" -> icon = "RED_SAND";
            default -> {
                if (env != null) {
                    switch (env) {
                        case "NETHER" -> icon = "NETHERRACK";
                        case "THE_END" -> icon = "END_STONE";
                        default -> icon = "STONE";
                    }
                } else {
                    icon = "DIRT";
                }
            }
        }
        return icon;
    }
}
