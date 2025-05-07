package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.areas.PlotArea;
import me.eccentric_nz.TARDIS.commands.TARDISPlotCommand;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardischunkgenerator.worldgen.PlotListener;
import org.bukkit.*;

import java.util.Locale;
import java.util.logging.Level;

public class WorldLoader {

    private final TARDIS plugin;

    public WorldLoader(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void loadWorld(String world) {
        try {
            String e = plugin.getPlanetsConfig().getString("planets." + world + ".environment");
            World.Environment environment = World.Environment.valueOf(e);
            WorldCreator worldCreator = WorldCreator.name(world).environment(environment);
            try {
                WorldType worldType = WorldType.valueOf(plugin.getPlanetsConfig().getString("planets." + world + ".world_type"));
                worldCreator.type(worldType);
                worldCreator.seed(TARDISConstants.RANDOM.nextLong());
            } catch (IllegalArgumentException iae) {
                plugin.getMessenger().sendWithColour(plugin.getConsole(), TardisModule.DEBUG, "Invalid World Type specified for '" + world + "'! " + iae.getMessage(), "#FF5555");
            }
            String g = plugin.getPlanetsConfig().getString("planets." + world + ".generator");
            if (g != null && !g.equalsIgnoreCase("DEFAULT")) {
                worldCreator.generator(g);
            }
            boolean structures = true; // true if not specified
            if (plugin.getPlanetsConfig().contains("planets." + world + ".generate_structures")) {
                structures = plugin.getPlanetsConfig().getBoolean("planets." + world + ".generate_structures");
            }
            worldCreator.generateStructures(structures);
            boolean hardcore = plugin.getPlanetsConfig().getBoolean("planets." + world + ".hardcore");
            if (hardcore) {
                worldCreator.hardcore(true);
            }
            World w = worldCreator.createWorld();
            if (w != null) {
                if (g != null && g.equals("TARDIS:plot")) {
                    // load the plot listener
                    plugin.getPM().registerEvents(new PlotListener(plugin), plugin);
                    // load the command
                    TARDISPlotCommand plotCommand = new TARDISPlotCommand(plugin);
                    plugin.getCommand("tardisplot").setExecutor(plotCommand);
                    plugin.getCommand("tardisplot").setTabCompleter(plotCommand);
                    // add worldguard protection
                    if (plugin.isWorldGuardOnServer()) {
                        plugin.getWorldGuardUtils().addPlotWorldProtection(w);
                    }
                    // should we run a TARDIS area?
                    if (plugin.getGeneratorConfig().getBoolean("plot.create_area")) {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new PlotArea(plugin, w), 100L);
                    }
                }
                String gm = plugin.getPlanetsConfig().getString("planets." + world + ".gamemode");
                if (gm != null && gm.toUpperCase(Locale.ROOT).equals("CREATIVE")) {
                    plugin.getTardisHelper().setWorldGameMode(world, GameMode.CREATIVE);
                }
                if (plugin.getPlanetsConfig().contains("planets." + world + ".gamerules") && plugin.getPlanetsConfig().getConfigurationSection("planets." + world + ".gamerules") != null) {
                    for (String rule : plugin.getPlanetsConfig().getConfigurationSection("planets." + world + ".gamerules").getKeys(false)) {
                        GameRule gameRule = GameRule.getByName(rule);
                        if (gameRule != null) {
                            w.setGameRule(gameRule, plugin.getPlanetsConfig().getBoolean("planets." + world + ".gamerules." + rule));
                        } else {
                            plugin.getServer().getLogger().log(Level.WARNING, "Invalid game rule detected in planets.yml!");
                            plugin.getServer().getLogger().log(Level.WARNING, "The rule was '" + rule + "' in world '" + world + "'.");
                        }
                    }
                }
                // spawn chunk radius (replaces deprecated `keep_spawn_in_memory` setting)
                // 0 is the equivalent of keep_spawn_in_memory: false
                // 10 is the equivalent of keep_spawn_in_memory: true (pre-1.20.5)
                // the new default in 1.20.5+ is 2
                w.setGameRule(GameRule.SPAWN_CHUNK_RADIUS, Math.clamp(plugin.getPlanetsConfig().getInt("planets." + world + ".spawn_chunk_radius", 0), 0, 32));
                String d = plugin.getPlanetsConfig().getString("planets." + world + ".difficulty");
                if (d != null) {
                    try {
                        Difficulty difficulty = Difficulty.valueOf(d.toUpperCase(Locale.ROOT));
                        w.setDifficulty(difficulty);
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().sendWithColour(plugin.getConsole(), TardisModule.DEBUG, "Could not load world '" + world + "'! " + e.getMessage(), "#FF5555");
        }
    }
}
