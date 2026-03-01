package me.eccentric_nz.TARDIS.commands.utils;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.planets.*;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WorldUtility {

    public static void disable(TARDIS plugin, CommandSender sender, World world) {
        String w = world.getName();
        // is the world in the config?
        if (!plugin.getPlanetsConfig().contains("planets." + w)) {
            plugin.getMessenger().sendColouredCommand(sender, "WORLD_NOT_FOUND", "/tardisworld load", plugin);
            return;
        }
        plugin.getPlanetsConfig().set("planets." + w + ".enabled", false);
        plugin.savePlanetsConfig();
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "WORLD_DISABLED", w);
        if (TARDISConstants.isTARDISPlanetExact(w)) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "WORLD_DISABLED_RESTART");
        }
        // remove players from world
        Location spawn = plugin.getServer().getWorlds().getFirst().getSpawnLocation();
        for (Player player : world.getPlayers()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "WORLD_DISABLED_TELEPORT");
            player.teleport(spawn);
        }
        // unload the world
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().unloadWorld(world, true), 5);
    }

    public static void enable(TARDIS plugin, CommandSender sender, String world) {
        // is the world in the config?
        if (!plugin.getPlanetsConfig().contains("planets." + world)) {
            plugin.getMessenger().sendColouredCommand(sender, "WORLD_NOT_FOUND", "/tardisworld load", plugin);
            return;
        }
        plugin.getPlanetsConfig().set("planets." + world + ".enabled", true);
        plugin.savePlanetsConfig();
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "WORLD_ENABLED", world);
        if (TARDISConstants.isTARDISPlanetExact(world)) {
            switch (world) {
                case "rooms" -> {
                    plugin.debug("Rooms world enabled, checking transmat location");
                    new RoomsWorld().check(plugin);
                }
                case "gallifrey" -> {
                    plugin.debug("Gallifrey enabled, registering planet event listeners");
                    plugin.getPM().registerEvents(new TARDISGallifreySpawnListener(plugin), plugin);
                }
                case "siluria" -> {
                    plugin.debug("Siluria enabled, registering planet event listeners");
                    if (plugin.getConfig().getBoolean("modules.weeping_angels")) {
                        plugin.getPM().registerEvents(new TARDISSiluriaSpawnListener(plugin), plugin);
                    }
                }
                case "skaro" -> {
                    plugin.debug("Skaro enabled, registering planet event listeners");
                    if (plugin.getPlanetsConfig().getBoolean("planets.skaro.acid")) {
                        plugin.getPM().registerEvents(new TARDISAcidWater(plugin), plugin);
                    }
                    if (plugin.getConfig().getBoolean("modules.weeping_angels")) {
                        plugin.getPM().registerEvents(new TARDISSkaroSpawnListener(plugin), plugin);
                    }
                }
                default -> {
                    // telos
                    plugin.debug("Telos enabled, registering planet event listeners");
                    if (plugin.getPlanetsConfig().getBoolean("planets.telos.vastial.enabled")) {
                        plugin.getPM().registerEvents(new TARDISVastialListener(plugin), plugin);
                    }
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
                    if (plugin.getConfig().getBoolean("modules.weeping_angels")) {
                        plugin.getPM().registerEvents(new TARDISTelosSpawnListener(plugin), plugin);
                    }
                }
            }
        }
        new WorldLoader(plugin).loadWorld(world);
    }

    public static void load(TARDIS plugin, CommandSender sender, String name, String type, String env, String gen) {
        WorldType worldType = WorldType.NORMAL;
        World.Environment environment = World.Environment.NORMAL;
        // try to load the world
        WorldCreator creator = new WorldCreator(name);
        if (!type.isEmpty()) {
            try {
                worldType = WorldType.valueOf(type.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "WORLD_TYPE", type);
                return;
            }
        }
        creator.type(worldType);
        if (!env.isEmpty()) {
            try {
                environment = World.Environment.valueOf(env.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "WORLD_ENV", env);
                return;
            }
        }
        creator.environment(environment);
        if (!gen.isEmpty()) {
            // Check generator exists
            String[] split = gen.split(":", 2);
            Plugin p = plugin.getPM().getPlugin(split[0]);
            if (p == null) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "WORLD_GEN", gen);
                return;
            }
            creator.generator(gen);
        }
        if (creator.createWorld() == null) {
            plugin.getMessenger().sendColouredCommand(sender, "WORLD_NOT_FOUND", "/tardisworld load", plugin);
            return;
        }
        plugin.getPlanetsConfig().set("planets." + name + ".enabled", true);
        plugin.getPlanetsConfig().set("planets." + name + ".resource_pack", "default");
        plugin.getPlanetsConfig().set("planets." + name + ".gamemode", "SURVIVAL");
        plugin.getPlanetsConfig().set("planets." + name + ".time_travel", false);
        plugin.getPlanetsConfig().set("planets." + name + ".world_type", worldType.toString());
        plugin.getPlanetsConfig().set("planets." + name + ".environment", environment.toString());
        plugin.getPlanetsConfig().set("planets." + name + ".spawn_chunk_radius", 0);
        plugin.getPlanetsConfig().set("planets." + name + ".spawn_other_mobs", true);
        plugin.getPlanetsConfig().set("planets." + name + ".gamerules", List.of());
        plugin.getPlanetsConfig().set("planets." + name + ".allow_portals", true);
        plugin.getPlanetsConfig().set("planets." + name + ".alias", TARDISStringUtils.uppercaseFirst(name));
        String icon;
        switch (environment) {
            case NETHER -> icon = "NETHERRACK";
            case THE_END -> icon = "END_STONE";
            default -> icon = "STONE";
        }
        plugin.getPlanetsConfig().set("planets." + name + ".icon", icon);
        plugin.getPlanetsConfig().set("planets." + name + ".helmic_regulator_order", -1);
        // don't set siluria/gallifrey/skaro generator
        if (!TARDISConstants.isTARDISPlanetExact(name)) {
            plugin.getPlanetsConfig().set("planets." + name + ".generator", !gen.isEmpty() ? gen : "DEFAULT");
        }
        plugin.savePlanetsConfig();
    }

    public static void unload(TARDIS plugin, CommandSender sender, World world) {
        String w = world.getName();
        plugin.getServer().unloadWorld(world, true);
        plugin.getPlanetsConfig().set("planets." + w + ".enabled", false);
        plugin.getPlanetsConfig().set("planets." + w + ".time_travel", false);
        plugin.savePlanetsConfig();
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "WORLD_UNLOAD_SUCCESS", w);
    }

    public static void sendInfo(TARDIS plugin, Player player) {
        World world = player.getWorld();
        plugin.getMessenger().send(player, TardisModule.TARDIS, "WORLD_INFO", world.getName());
        plugin.getMessenger().message(player, "Gamemode -> " + plugin.getPlanetsConfig().getString("planets." + world.getName() + ".gamemode"));
        plugin.getMessenger().message(player, "Environment -> " + world.getEnvironment());
        plugin.getMessenger().message(player, "Generator -> " + plugin.getPlanetsConfig().getString("planets." + world.getName() + ".generator"));
        plugin.getMessenger().message(player, "Difficulty -> " + world.getDifficulty());
        plugin.getMessenger().message(player, "Gamerules -> ");
        for (String s : world.getGameRules()) {
            GameRule rule = Registry.GAME_RULE.getOrThrow(NamespacedKey.minecraft(s.toLowerCase(Locale.ROOT)));
            if (rule != null) {
                plugin.getMessenger().message(player, "     " + s + " -> " + world.getGameRuleValue(rule));
            }
        }
        plugin.getMessenger().message(player, "Spawn chunk radius -> " + plugin.getPlanetsConfig().getInt("planets." + world.getName() + ".spawn_chunk_radius"));
        plugin.getMessenger().message(player, "Time travel -> " + plugin.getPlanetsConfig().getString("planets." + world.getName() + ".time_travel"));
    }
    
    public static void rename(TARDIS plugin, CommandSender sender, World world, String newName) {
        String oldName = world.getName();
        // remove players from world
        List<Player> players = world.getPlayers();
        Location spawn = plugin.getServer().getWorlds().getFirst().getSpawnLocation();
        players.forEach((p) -> {
            plugin.getMessenger().send(p, TardisModule.TARDIS, "WORLD_RENAME");
            p.teleport(spawn);
        });
        // unload world
        plugin.getServer().unloadWorld(world, true);
        plugin.getTardisHelper().setLevelName(oldName, newName);
        // rename the planet in planets.yml
        ConfigurationSection section = plugin.getPlanetsConfig().getConfigurationSection("planets." + oldName);
        if (section != null) {
            Map<String, Object> map = section.getValues(true);
            plugin.getPlanetsConfig().set("planets." + newName, map);
            plugin.getPlanetsConfig().set("planets." + oldName, null);
            plugin.savePlanetsConfig();
        }
        // load world
        new WorldLoader(plugin).loadWorld(newName);
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "WORLD_RENAME_SUCCESS", newName);
    }

    public static void setGameMode(TARDIS plugin, CommandSender sender, String world, GameMode gm) {
        plugin.getTardisHelper().setWorldGameMode(world, gm);
        plugin.getPlanetsConfig().set("planets." + world + ".gamemode", gm.toString());
        plugin.savePlanetsConfig();
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "WORLD_GM_SET", world, gm.toString());
    }
}
