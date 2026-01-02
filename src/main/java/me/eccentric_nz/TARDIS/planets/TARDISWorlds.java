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
package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import me.eccentric_nz.tardischunkgenerator.helpers.TARDISPlanetData;
import org.bukkit.GameRules;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class TARDISWorlds {

    private final TARDIS plugin;

    public TARDISWorlds(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void doWorlds() {
        List<World> worlds = plugin.getServer().getWorlds();
        String defWorld = plugin.getConfig().getString("creation.default_world_name");
        worlds.forEach((w) -> {
            String worldName = w.getName();
            if (!plugin.getPlanetsConfig().contains("planets." + worldName) && !worldName.equals(defWorld)) {
                TARDISPlanetData data = plugin.getTardisHelper().getLevelData(w.getName());
                plugin.getPlanetsConfig().set("planets." + worldName + ".enabled", false);
                plugin.getPlanetsConfig().set("planets." + worldName + ".time_travel", !TARDISConstants.isTARDISPlanet(worldName));
                plugin.getPlanetsConfig().set("planets." + worldName + ".resource_pack", "default");
                plugin.getPlanetsConfig().set("planets." + worldName + ".gamemode", data.gameMode().toString());
                plugin.getPlanetsConfig().set("planets." + worldName + ".world_type", data.worldType().toString());
                plugin.getPlanetsConfig().set("planets." + worldName + ".environment", data.environment().toString());
                plugin.getPlanetsConfig().set("planets." + worldName + ".difficulty", data.difficulty().toString());
                plugin.getPlanetsConfig().set("planets." + worldName + ".generator", (worldName.startsWith("TARDIS_") || worldName.equals(plugin.getConfig().getString("creation.default_world_name"))) ? "TARDISChunkGenerator" : "DEFAULT");
                plugin.getPlanetsConfig().set("planets." + worldName + ".spawn_chunk_radius", 0);
                plugin.getPlanetsConfig().set("planets." + worldName + ".alias", TARDISStringUtils.uppercaseFirst(worldName));
                plugin.getPlanetsConfig().set("planets." + worldName + ".spawn_other_mobs", true);
                plugin.getPlanetsConfig().set("planets." + worldName + ".gamerules", List.of());
                plugin.getPlanetsConfig().set("planets." + worldName + ".allow_portals", true);
                plugin.getPlanetsConfig().set("planets." + worldName + ".helmic_regulator_order", -1);
                String icon;
                switch (data.environment()) {
                    case NETHER -> icon = "NETHERRACK";
                    case THE_END -> icon = "END_STONE";
                    default -> icon = "STONE";
                }
                plugin.getPlanetsConfig().set("planets." + worldName + ".icon", icon);
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Added '" + worldName + "' to planets.yml. To exclude this world from time travel run: /tardisadmin exclude " + worldName);
            }
        });
        // now load TARDIS worlds / remove worlds that may have been deleted
        Set<String> cWorlds = plugin.getPlanetsConfig().getConfigurationSection("planets").getKeys(false);
        cWorlds.forEach((cw) -> {
            if (TARDISAliasResolver.getWorldFromAlias(cw) == null) {
                if (worldFolderExists(cw) && plugin.getPlanetsConfig().getBoolean("planets." + cw + ".enabled")) {
                    plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Attempting to load world: '" + cw + "'");
                    new WorldLoader(plugin).loadWorld(cw);
                    if (cw.equals("telos")) {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            if (plugin.getPlanetsConfig().getBoolean("planets.telos.twilight")) {
                                World telos = plugin.getServer().getWorld("telos");
                                if (telos != null) {
                                    telos.setTime(13000);
                                    telos.setGameRule(GameRules.ADVANCE_TIME, false);
                                    plugin.getPlanetsConfig().set("planets.telos.gamerules.advance_time", false);
                                }
                            } else {
                                plugin.getPlanetsConfig().set("planets.telos.gamerules.advance_time", true);
                            }
                            String planetsPath = plugin.getDataFolder() + File.separator + "planets.yml";
                            try {
                                plugin.getPlanetsConfig().save(new File(planetsPath));
                            } catch (IOException ignored) {
                            }
                        }, 300L);
                    }
                }
            } else {
                if (!TARDISConstants.isTARDISPlanet(cw) && !cw.equals("TARDIS_Zero_Room") && !cw.equals("TARDIS_TimeVortex") && !worldFolderExists(cw)) {
                    plugin.getPlanetsConfig().set("planets." + cw, null);
                    plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Removed '" + cw + "' from planets.yml");
                    // remove records from database that may contain the removed world
                    plugin.getCleanUpWorlds().add(cw);
                }
            }
        });
        plugin.savePlanetsConfig();
    }

    private boolean worldFolderExists(String world) {
        String worldZero = plugin.getServer().getWorlds().getFirst().getName();
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
}
