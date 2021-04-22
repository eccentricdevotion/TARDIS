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
package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import me.eccentric_nz.tardischunkgenerator.helpers.TARDISPlanetData;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class TARDISWorlds {

    private final TARDIS plugin;

    public TARDISWorlds(TARDIS plugin) {
        this.plugin = plugin;
    }

    public static void loadWorld(String world) {
        try {
            String e = TARDIS.plugin.getPlanetsConfig().getString("planets." + world + ".environment");
            World.Environment environment = World.Environment.valueOf(e);
            WorldCreator worldCreator = WorldCreator.name(world).environment(environment);
            String g = TARDIS.plugin.getPlanetsConfig().getString("planets." + world + ".generator");
            if (g != null && !g.equalsIgnoreCase("DEFAULT")) {
                worldCreator.generator(g);
            }
            boolean hardcore = TARDIS.plugin.getPlanetsConfig().getBoolean("planets." + world + ".hardcore");
            if (hardcore) {
                worldCreator.hardcore(true);
            }
            World w = worldCreator.createWorld();
            if (w != null) {
                String gm = TARDIS.plugin.getPlanetsConfig().getString("planets." + world + ".gamemode");
                if (gm != null && gm.toUpperCase(Locale.ENGLISH).equals("CREATIVE")) {
                    TARDIS.plugin.getTardisHelper().setWorldGameMode(world, GameMode.CREATIVE);
                }
                if (TARDIS.plugin.getPlanetsConfig().contains("planets." + world + ".gamerules")) {
                    for (String rule : TARDIS.plugin.getPlanetsConfig().getConfigurationSection("planets." + world + ".gamerules").getKeys(false)) {
                        GameRule gameRule = GameRule.getByName(rule);
                        w.setGameRule(gameRule, TARDIS.plugin.getPlanetsConfig().getBoolean("planets." + world + ".gamerules." + rule));
                    }
                }
                boolean keepSpawnInMemory = TARDIS.plugin.getPlanetsConfig().getBoolean("planets." + world + ".keep_spawn_in_memory");
                w.setKeepSpawnInMemory(keepSpawnInMemory);
            }
        } catch (IllegalArgumentException e) {
            TARDIS.plugin.debug(ChatColor.RED + "Could not load world '" + world + "'! " + e.getMessage());
        }
    }

    public void doWorlds() {
        List<World> worlds = plugin.getServer().getWorlds();
        String defWorld = plugin.getConfig().getString("creation.default_world_name");
        worlds.forEach((w) -> {
            String worldname = TARDISStringUtils.worldName(w.getName());
            if (!plugin.getPlanetsConfig().contains("planets." + worldname) && !worldname.equals(defWorld)) {
                TARDISPlanetData data = plugin.getTardisHelper().getLevelData(w.getName());
                plugin.getPlanetsConfig().set("planets." + worldname + ".enabled", false);
                plugin.getPlanetsConfig().set("planets." + worldname + ".time_travel", !isTARDISDatapackWorld(worldname));
                plugin.getPlanetsConfig().set("planets." + worldname + ".resource_pack", "default");
                plugin.getPlanetsConfig().set("planets." + worldname + ".gamemode", data.getGameMode().toString());
                plugin.getPlanetsConfig().set("planets." + worldname + ".world_type", data.getWorldType().toString());
                plugin.getPlanetsConfig().set("planets." + worldname + ".environment", data.getEnvironment().toString());
                plugin.getPlanetsConfig().set("planets." + worldname + ".generator", (worldname.startsWith("TARDIS_") || worldname.equals(plugin.getConfig().getString("creation.default_world_name"))) ? "TARDISChunkGenerator" : "DEFAULT");
                plugin.getPlanetsConfig().set("planets." + worldname + ".keep_spawn_in_memory", false);
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Added '" + worldname + "' to planets.yml. To exclude this world from time travel run: /tardisadmin exclude " + worldname);
            }
        });
        // revert lowercase TARDIS world names
        if (plugin.getConfig().getBoolean("conversions.level_names")) {
            for (Map.Entry<String, String> level : TARDISConstants.REVERT_LEVELS.entrySet()) {
                // set the LevelName in level.dat
                plugin.getTardisHelper().setLevelName(level.getKey(), level.getValue());
                // rename the planet in planets.yml
                ConfigurationSection section = plugin.getPlanetsConfig().getConfigurationSection("planets." + level.getKey());
                if (section != null) {
                    Map<String, Object> map = section.getValues(true);
                    plugin.getPlanetsConfig().set("planets." + level.getValue(), map);
                    plugin.getPlanetsConfig().set("planets." + level.getKey(), null);
                }
            }
            plugin.getConfig().set("conversions.level_names", null);
            plugin.saveConfig();
        }
        // get default server world
        String s_world = plugin.getServer().getWorlds().get(0).getName();
        // now load TARDIS worlds / remove worlds that may have been deleted
        Set<String> cWorlds = plugin.getPlanetsConfig().getConfigurationSection("planets").getKeys(false);
        cWorlds.forEach((cw) -> {
            if (!TARDISConstants.PLANETS.contains(cw) && plugin.getServer().getWorld(cw) == null) {
                if ((plugin.getWorldManager().equals(WorldManager.NONE) || plugin.getPlanetsConfig().getConfigurationSection("planets").getKeys(false).contains(cw)) && worldFolderExists(cw) && plugin.getPlanetsConfig().getBoolean("planets." + cw + ".enabled")) {
                    plugin.getConsole().sendMessage(plugin.getPluginName() + "Attempting to load world: '" + cw + "'");
                    loadWorld(cw);
                }
            } else if (TARDISConstants.PLANETS.contains(cw)) {
                if (plugin.getPlanetsConfig().getBoolean("planets." + cw + ".enabled")) {
                    // create world
                    if (cw.equalsIgnoreCase(s_world + "_tardis_skaro")) {
                        World skaro = new TARDISSkaro(plugin).loadDalekWorld();
                        if (skaro != null) {
                            for (String rule : plugin.getPlanetsConfig().getConfigurationSection("planets." + s_world + "_tardis_skaro.gamerules").getKeys(false)) {
                                GameRule gameRule = GameRule.getByName(rule);
                                skaro.setGameRule(gameRule, plugin.getPlanetsConfig().getBoolean("planets." + s_world + "_tardis_skaro.gamerules." + rule));
                            }
                        }
                    }
                    if (cw.equalsIgnoreCase(s_world + "_tardis_siluria")) {
                        World siluria = new TARDISSiluria(plugin).loadSilurianUnderworld();
                        if (siluria != null) {
                            for (String rule : plugin.getPlanetsConfig().getConfigurationSection("planets." + s_world + "_tardis_siluria.gamerules").getKeys(false)) {
                                GameRule gameRule = GameRule.getByName(rule);
                                siluria.setGameRule(gameRule, plugin.getPlanetsConfig().getBoolean("planets." + s_world + "_tardis_siluria.gamerules." + rule));
                            }
                        }
                    }
                    if (cw.equalsIgnoreCase(s_world + "_tardis_gallifrey")) {
                        World gallifrey = new TARDISGallifrey(plugin).loadTimeLordWorld();
                        if (gallifrey != null) {
                            for (String rule : plugin.getPlanetsConfig().getConfigurationSection("planets." + s_world + "_tardis_gallifrey.gamerules").getKeys(false)) {
                                GameRule gameRule = GameRule.getByName(rule);
                                gallifrey.setGameRule(gameRule, plugin.getPlanetsConfig().getBoolean("planets." + s_world + "_tardis_gallifrey.gamerules." + rule));
                            }
                        }
                    }
                }
            } else {
                if (!TARDISConstants.PLANETS.contains(cw) && !cw.equals("TARDIS_Zero_Room") && !cw.equals("TARDIS_TimeVortex") && !worldFolderExists(cw)) {
                    plugin.getPlanetsConfig().set("planets." + cw, null);
                    plugin.getConsole().sendMessage(plugin.getPluginName() + "Removed '" + cw + "' from planets.yml");
                    // remove records from database that may contain the removed world
                    plugin.getCleanUpWorlds().add(cw);
                }
            }
        });
        plugin.savePlanetsConfig();
    }

    private boolean worldFolderExists(String world) {
        String worldZero = plugin.getServer().getWorlds().get(0).getName();
        File container = plugin.getServer().getWorldContainer();
        File[] dirs = container.listFiles();
        if (dirs != null) {
            for (File dir : dirs) {
                if (dir.isDirectory()) {
                    String name = dir.getName();
                    if (name.startsWith(worldZero)) {
                        // only check worlds that aren't default dimensions
                        return true;
                    }
                    if (name.equals(world)) {
                        File level = new File(dir, "level.dat");
                        if (level.exists()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isTARDISDatapackWorld(String p) {
        return (p.endsWith("tardis_gallifrey") || p.endsWith("tardis_siluria") || p.endsWith("tardis_skaro"));
    }
}
