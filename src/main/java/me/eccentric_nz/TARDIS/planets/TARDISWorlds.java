package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.enumeration.WORLD_MANAGER;
import me.eccentric_nz.tardischunkgenerator.TARDISPlanetData;
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

    public void doWorlds() {
        List<World> worlds = plugin.getServer().getWorlds();
        String defWorld = plugin.getConfig().getString("creation.default_world_name");
        worlds.forEach((w) -> {
            String worldname = w.getName();
            if (!plugin.getPlanetsConfig().contains("planets." + worldname) && !worldname.equals(defWorld)) {
                TARDISPlanetData data = plugin.getTardisHelper().getLevelData(worldname);
                plugin.getPlanetsConfig().set("planets." + worldname + ".enabled", false);
                plugin.getPlanetsConfig().set("planets." + worldname + ".time_travel", true);
                plugin.getPlanetsConfig().set("planets." + worldname + ".resource_pack", "default");
                plugin.getPlanetsConfig().set("planets." + worldname + ".gamemode", data.getGameMode().toString());
                plugin.getPlanetsConfig().set("planets." + worldname + ".world_type", data.getWorldType().toString());
                plugin.getPlanetsConfig().set("planets." + worldname + ".environment", data.getEnvironment().toString());
                plugin.getPlanetsConfig().set("planets." + worldname + ".generator", (worldname.startsWith("TARDIS_") || worldname.equals(plugin.getConfig().getString("creation.default_world_name"))) ? "TARDISChunkGenerator" : "DEFAULT");
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
        // now load TARDIS worlds / remove worlds that may have been deleted
        Set<String> cWorlds = plugin.getPlanetsConfig().getConfigurationSection("planets").getKeys(false);
        cWorlds.forEach((cw) -> {
            if (plugin.getServer().getWorld(cw) == null) {
                if ((plugin.getWorldManager().equals(WORLD_MANAGER.NONE) || plugin.getPlanetsConfig().getConfigurationSection("planets").getKeys(false).contains(cw)) && worldFolderExists(cw) && plugin.getPlanetsConfig().getBoolean("planets." + cw + ".enabled")) {
                    plugin.getConsole().sendMessage(plugin.getPluginName() + "Attempting to load world: '" + cw + "'");
                    loadWorld(cw);
                } else if (TARDISConstants.PLANETS.contains(cw)) {
                    if (!worldFolderExists(cw) && plugin.getPlanetsConfig().getBoolean("planets." + cw + ".enabled")) {
                        // create world
                        if (cw.equalsIgnoreCase("Skaro")) {
                            new TARDISSkaro(plugin).createDalekWorld();
                        }
                        if (cw.equalsIgnoreCase("Siluria")) {
                            new TARDISSiluria(plugin).createSilurianUnderworld();
                        }
                        if (cw.equalsIgnoreCase("Gallifrey")) {
                            new TARDISGallifrey(plugin).createTimeLordWorld();
                        }
                    }
                } else {
                    if (!TARDISConstants.PLANETS.contains(cw) && !cw.equals("TARDIS_Zero_Room")) {
                        plugin.getPlanetsConfig().set("planets." + cw, null);
                        plugin.getConsole().sendMessage(plugin.getPluginName() + "Removed '" + cw + "' from planets.yml");
                        // remove records from database that may contain the removed world
                        plugin.getCleanUpWorlds().add(cw);
                    }
                }
            }
        });
        plugin.savePlanetsConfig();
    }

    private boolean worldFolderExists(String world) {
        File container = plugin.getServer().getWorldContainer();
        File[] dirs = container.listFiles();
        if (dirs != null) {
            for (File dir : dirs) {
                if (dir.isDirectory() && dir.getName().equals(world)) {
                    File level = new File(dir, "level.dat");
                    if (level.exists()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void loadWorld(String world) {
        try {
            String wt = TARDIS.plugin.getPlanetsConfig().getString("planets." + world + ".world_type");
            WorldType worldType = WorldType.valueOf(wt);
            String e = TARDIS.plugin.getPlanetsConfig().getString("planets." + world + ".environment");
            World.Environment environment = World.Environment.valueOf(e);
            WorldCreator worldCreator = WorldCreator.name(world).type(worldType).environment(environment);
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
            TARDIS.plugin.debug(ChatColor.RED + "Could not load world '" + world + "'!");
        }
    }
}
