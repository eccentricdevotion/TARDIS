/*
 * Copyright (C) 2026 eccentric_nz
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
import me.eccentric_nz.TARDIS.utility.GameRuleConverter;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

/**
 * @author eccentric_nz
 */
public class PlanetsConfigUpdater {

    private final TARDIS plugin;
    private final FileConfiguration planets_config;

    public PlanetsConfigUpdater(TARDIS plugin, FileConfiguration planets_config) {
        this.plugin = plugin;
        this.planets_config = planets_config;
    }

    public void checkPlanetsConfig() {
        int save = 0;
        // add colour_skies
        if (!planets_config.contains("colour_skies")) {
            planets_config.set("colour_skies", true);
            save++;
        }
        Set<String> worlds = planets_config.getConfigurationSection("planets").getKeys(false);
        // remove `spawn_chunk_radius` and `keep_spawn_in_memory` config options for all worlds
        for (String w : worlds) {
            if (planets_config.contains("planets." + w + ".spawn_chunk_radius")) {
                planets_config.set("planets." + w + ".spawn_chunk_radius", null);
            }
            if (planets_config.contains("planets." + w + ".keep_spawn_in_memory")) {
                planets_config.set("planets." + w + ".keep_spawn_in_memory", null);
            }
        }
        // check there is an `alias` config option for all worlds
        for (String w : worlds) {
            if (!planets_config.contains("planets." + w + ".alias")) {
                if (w.equals("tardis_timevortex")) {
                    planets_config.set("planets." + w + ".alias", "TimeVortex");
                } else if (w.endsWith("tardis_zero_room")) {
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
        // get server.properties value
        String d = getDifficulty();
        // check there is a `difficulty` config option for all worlds
        for (String w : worlds) {
            if (!planets_config.contains("planets." + w + ".difficulty")) {
                planets_config.set("planets." + w + ".difficulty", d);
            }
        }
        // get/set `icon` material config option for all worlds
        for (String w : worlds) {
            if (!planets_config.contains("planets." + w + ".icon")) {
                planets_config.set("planets." + w + ".icon", getIcon(w, planets_config.getString("planets." + w + ".environment")));
            }
        }
        // check there is a `helmic_regulator_order` config option for all worlds
        for (String w : worlds) {
            if (!planets_config.contains("planets." + w + ".helmic_regulator_order")) {
                planets_config.set("planets." + w + ".helmic_regulator_order", -1);
                save++;
            }
        }
        if (planets_config.contains("default_resource_pack") && planets_config.getString("default_resource_pack").equalsIgnoreCase("https://dl.dropboxusercontent.com/u/53758864/rp/Default.zip")) {
            planets_config.set("default_resource_pack", "https://www.dropbox.com/s/utka3zxmer7f19g/Default.zip?dl=1");
            save++;
        }
        // check there is an `allow_portals` config option for all worlds
        for (String w : worlds) {
            if (!planets_config.contains("planets." + w + ".allow_portals")) {
                planets_config.set("planets." + w + ".allow_portals", true);
                save++;
            }
        }
        if (!planets_config.contains("planets.gallifrey")) {
            // Skaro
            planets_config.set("planets.skaro.enabled", false);
            planets_config.set("planets.skaro.resource_pack", "default");
            planets_config.set("planets.skaro.acid", true);
            planets_config.set("planets.skaro.acid_damage", 5);
            planets_config.set("planets.skaro.acid_potions", List.of("WEAKNESS", "POISON"));
            planets_config.set("planets.skaro.rust", true);
            planets_config.set("planets.skaro.flying_daleks", true);
            planets_config.set("planets.skaro.gamemode", "SURVIVAL");
            planets_config.set("planets.skaro.time_travel", true);
            planets_config.set("planets.skaro.world_type", "NORMAL");
            planets_config.set("planets.skaro.environment", "NORMAL");
            planets_config.set("planets.skaro.generator", "TARDIS:skaro");
            planets_config.set("planets.skaro.spawn_chunk_radius", 0);
            planets_config.set("planets.skaro.spawn_other_mobs", true);
            planets_config.set("planets.skaro.gamerules.spawn_wandering_traders", false);
            planets_config.set("planets.skaro.gamerules.spawn_patrols", false);
            planets_config.set("planets.skaro.allow_portals", false);
            planets_config.set("planets.skaro.alias", "Skaro");
            planets_config.set("planets.skaro.icon", "FIRE_CORAL_BLOCK");
            planets_config.set("planets.skaro.helmic_regulator_order", -1);
            // Siluria
            planets_config.set("planets.siluria.enabled", false);
            planets_config.set("planets.siluria.resource_pack", "default");
            planets_config.set("planets.siluria.gamemode", "SURVIVAL");
            planets_config.set("planets.siluria.time_travel", true);
            planets_config.set("planets.siluria.world_type", "NORMAL");
            planets_config.set("planets.siluria.environment", "NORMAL");
            planets_config.set("planets.siluria.generator", "TARDIS:siluria");
            planets_config.set("planets.siluria.spawn_chunk_radius", 0);
            planets_config.set("planets.siluria.spawn_other_mobs", true);
            planets_config.set("planets.siluria.gamerules.spawn_wandering_traders", false);
            planets_config.set("planets.siluria.gamerules.spawn_patrols", false);
            planets_config.set("planets.siluria.allow_portals", false);
            planets_config.set("planets.siluria.alias", "Siluria");
            planets_config.set("planets.siluria.icon", "BAMBOO_MOSAIC");
            planets_config.set("planets.siluria.helmic_regulator_order", -1);
            // Gallifrey:
            planets_config.set("planets.gallifrey.enabled", false);
            planets_config.set("planets.gallifrey.resource_pack", "default");
            planets_config.set("planets.gallifrey.gamemode", "SURVIVAL");
            planets_config.set("planets.gallifrey.time_travel", true);
            planets_config.set("planets.gallifrey.world_type", "NORMAL");
            planets_config.set("planets.gallifrey.environment", "NORMAL");
            planets_config.set("planets.gallifrey.generator", "TARDIS:gallifrey");
            planets_config.set("planets.gallifrey.spawn_chunk_radius", 0);
            planets_config.set("planets.gallifrey.spawn_other_mobs", true);
            planets_config.set("planets.gallifrey.gamerules.spawn_wandering_traders", false);
            planets_config.set("planets.gallifrey.gamerules.spawn_patrols", false);
            planets_config.set("planets.gallifrey.allow_portals", false);
            planets_config.set("planets.gallifrey.alias", "Gallifrey");
            planets_config.set("planets.gallifrey.icon", "RED_SAND");
            planets_config.set("planets.gallifrey.helmic_regulator_order", -1);
            planets_config.set("planets.gallifrey.villager_blueprints.enabled", true);
            planets_config.set("planets.gallifrey.villager_blueprints.uses", 1);
            planets_config.set("planets.gallifrey.villager_blueprints.chance", 20);
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
        if (!planets_config.contains("planets.telos")) {
            // Telos
            planets_config.set("planets.telos.enabled", false);
            planets_config.set("planets.telos.resource_pack", "default");
            planets_config.set("planets.telos.vastial.enabled", true);
            planets_config.set("planets.telos.vastial.gunpowder_chance", 30);
            planets_config.set("planets.telos.twilight", true);
            planets_config.set("planets.telos.gamemode", "SURVIVAL");
            planets_config.set("planets.telos.time_travel", true);
            planets_config.set("planets.telos.world_type", "NORMAL");
            planets_config.set("planets.telos.environment", "NORMAL");
            planets_config.set("planets.telos.generator", "TARDIS:telos");
            planets_config.set("planets.telos.spawn_other_mobs", true);
            planets_config.set("planets.telos.gamerules.spawn_wandering_traders", false);
            planets_config.set("planets.telos.gamerules.spawn_patrols", false);
            planets_config.set("planets.telos.allow_portals", false);
            planets_config.set("planets.telos.alias", "Telos");
            planets_config.set("planets.telos.icon", "PACKED_ICE");
            planets_config.set("planets.telos.helmic_regulator_order", -1);
        }
        if (!planets_config.contains("planets.rooms")) {
            planets_config.set("planets.rooms.enabled", false);
            planets_config.set("planets.rooms.resource_pack", "default");
            planets_config.set("planets.rooms.gamemode", "SURVIVAL");
            planets_config.set("planets.rooms.time_travel", false);
            planets_config.set("planets.rooms.world_type", "FLAT");
            planets_config.set("planets.rooms.environment", "NORMAL");
            planets_config.set("planets.rooms.generator", "TARDIS:rooms");
            planets_config.set("planets.rooms.spawn_other_mobs", false);
            planets_config.set("planets.rooms.gamerules.advance_weather", false);
            planets_config.set("planets.rooms.gamerules.advance_time", false);
            planets_config.set("planets.rooms.gamerules.spawn_mobs", false);
            planets_config.set("planets.rooms.gamerules.spawn_wandering_traders", false);
            planets_config.set("planets.rooms.gamerules.spawn_patrols", false);
            planets_config.set("planets.rooms.gamerules.spawn_wardens", false);
            planets_config.set("planets.rooms.allow_portals", false);
            planets_config.set("planets.rooms.alias", "TARDISRooms");
            planets_config.set("planets.rooms.icon", "BOOKSHELF");
            planets_config.set("planets.rooms.transmat_location.x", 8.5d);
            planets_config.set("planets.rooms.transmat_location.y", 68.0d);
            planets_config.set("planets.rooms.transmat_location.z", 2.5d);
            save++;
        }
        // convert game rules to 1.21.11+
        if (planets_config.contains("planets.tardis_timevortex.gamerules.doWardenSpawning")) {
            for (String w : worlds) {
                if (planets_config.contains("planets." + w + ".gamerules") && planets_config.getConfigurationSection("planets." + w + ".gamerules") != null) {
                    for (String rule : planets_config.getConfigurationSection("planets." + w + ".gamerules").getKeys(false)) {
                        String r = GameRuleConverter.OLD_TO_NEW.get(rule);
                        plugin.debug(r);
                        planets_config.set("planets." + w + ".gamerules." + r, planets_config.getBoolean("planets." + w + ".gamerules." + rule));
                        planets_config.set("planets." + w + ".gamerules." + rule, null);
                    }
                }
            }
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

    private String getDifficulty() {
        try {
            BufferedReader is = new BufferedReader(new FileReader("server.properties"));
            Properties props = new Properties();
            props.load(is);
            is.close();
            return props.getProperty("difficulty").toUpperCase(Locale.ROOT);
        } catch (IOException e) {
            return "EASY"; // minecraft / paper default
        }
    }

    private String getIcon(String world, String env) {
        String icon;
        switch (world) {
            case "tardis_timevortex" -> icon = "CRYING_OBSIDIAN";
            case "tardis_zero_room" -> icon = "PINK_WOOL";
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
