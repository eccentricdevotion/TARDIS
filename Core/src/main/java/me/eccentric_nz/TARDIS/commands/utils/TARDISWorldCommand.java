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
package me.eccentric_nz.TARDIS.commands.utils;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISSaveIconCommand;
import me.eccentric_nz.TARDIS.database.converters.TARDISWorldNameConverter;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.planets.*;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import me.eccentric_nz.tardischunkgenerator.helpers.TARDISPlanetData;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Load and unload worlds on the server
 *
 * @author eccentric_nz
 */
public class TARDISWorldCommand extends TARDISCompleter implements CommandExecutor, TabCompleter {

    private final TARDIS plugin;
    private final List<String> ROOT_SUBS = List.of("load", "unload", "enable", "disable", "gm", "rename", "update_name", "info", "dimensionicon");
    private final List<String> WORLD_SUBS = new ArrayList<>();
    private final List<String> TYPE_SUBS = new ArrayList<>();
    private final List<String> ENV_SUBS = new ArrayList<>();
    private final List<String> GM_SUBS = new ArrayList<>();
    private final List<String> PLANET_SUBS = List.of("gallifrey", "siluria", "skaro");

    public TARDISWorldCommand(TARDIS plugin) {
        this.plugin = plugin;
        WORLD_SUBS.addAll(plugin.getTardisAPI().getWorlds());
        for (WorldType wt : WorldType.values()) {
            TYPE_SUBS.add(wt.toString());
        }
        for (World.Environment e : World.Environment.values()) {
            ENV_SUBS.add(e.toString());
        }
        for (GameMode g : GameMode.values()) {
            GM_SUBS.add(g.toString());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisworld")) {
            if (sender == null) {
                plugin.debug("Sender was null!");
                return true;
            }
            if (args.length < 1 || !ROOT_SUBS.contains(args[0])) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_LOAD_UNLOAD");
                return false;
            }
            // icon
            if (args[0].equalsIgnoreCase("dimensionicon")) {
                return new TARDISSaveIconCommand(plugin).changeIcon(sender, args);
            }
            // info
            if (args[0].equalsIgnoreCase("info") && sender instanceof Player player) {
                World world = player.getWorld();
                plugin.getMessenger().send(player, TardisModule.TARDIS, "WORLD_INFO", world.getName());
                plugin.getMessenger().message(player, "Gamemode -> " + plugin.getPlanetsConfig().getString("planets." + world.getName() + ".gamemode"));
                plugin.getMessenger().message(player, "Environment -> " + world.getEnvironment());
                plugin.getMessenger().message(player, "Generator -> " + plugin.getPlanetsConfig().getString("planets." + world.getName() + ".generator"));
                plugin.getMessenger().message(player, "Difficulty -> " + world.getDifficulty());
                plugin.getMessenger().message(player, "Gamerules -> ");
                for (String s : world.getGameRules()) {
                    GameRule rule = GameRule.getByName(s);
                    if (rule != null) {
                        plugin.getMessenger().message(player, "     " + s + " -> " + world.getGameRuleValue(rule));
                    }
                }
                plugin.getMessenger().message(player, "Spawn chunk radius -> " + plugin.getPlanetsConfig().getInt("planets." + world.getName() + ".spawn_chunk_radius"));
                plugin.getMessenger().message(player, "Time travel -> " + plugin.getPlanetsConfig().getString("planets." + world.getName() + ".time_travel"));
                return true;
            }
            if (args.length < 2) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "TOO_FEW_ARGS");
                return false;
            }
            ArgumentParser parser = new ArgumentParser();
            String command = parser.join(args);
            Arguments arguments = parser.parse(command);
            if (args[0].equalsIgnoreCase("update_name")) {
                String world = arguments.getArguments().get(1).toLowerCase(Locale.ROOT);
                if (!PLANET_SUBS.contains(world)) {
                    plugin.getMessenger().sendColouredCommand(sender, "WORLD_NOT_FOUND", "/tardisworld load", plugin);
                    return true;
                }
                new TARDISWorldNameConverter(plugin, world).update();
                return true;
            }
            World world = TARDISAliasResolver.getWorldFromAlias(arguments.getArguments().get(1));
            if (world != null) {
                if (args[0].equalsIgnoreCase("disable")) {
                    // is the world in the config?
                    if (!plugin.getPlanetsConfig().contains("planets." + arguments.getArguments().get(1))) {
                        plugin.getMessenger().sendColouredCommand(sender, "WORLD_NOT_FOUND", "/tardisworld load", plugin);
                        return true;
                    }
                    plugin.getPlanetsConfig().set("planets." + arguments.getArguments().get(1) + ".enabled", false);
                    plugin.savePlanetsConfig();
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "WORLD_DISABLED", arguments.getArguments().get(1));
                    if (TARDISConstants.isTARDISPlanetExact(arguments.getArguments().get(1))) {
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
                    return true;
                }
                if (args[0].equalsIgnoreCase("rename")) {
                    if (args.length < 3) {
                        plugin.getMessenger().sendColouredCommand(sender, "ARG_WORLD_RENAME", "/tardisworld rename [old name] [new name]", plugin);
                        return true;
                    }
                    // remove players from world
                    List<Player> players = world.getPlayers();
                    Location spawn = plugin.getServer().getWorlds().getFirst().getSpawnLocation();
                    players.forEach((p) -> {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "WORLD_RENAME");
                        p.teleport(spawn);
                    });
                    // unload world
                    plugin.getServer().unloadWorld(world, true);
                    plugin.getTardisHelper().setLevelName(arguments.getArguments().get(1), arguments.getArguments().get(2));
                    // rename the planet in planets.yml
                    ConfigurationSection section = plugin.getPlanetsConfig().getConfigurationSection("planets." + arguments.getArguments().get(1));
                    if (section != null) {
                        Map<String, Object> map = section.getValues(true);
                        plugin.getPlanetsConfig().set("planets." + arguments.getArguments().get(2), map);
                        plugin.getPlanetsConfig().set("planets." + arguments.getArguments().get(1), null);
                        plugin.savePlanetsConfig();
                    }
                    // load world
                    TARDISWorlds.loadWorld(arguments.getArguments().get(2));
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "WORLD_RENAME_SUCCESS", arguments.getArguments().get(2));
                    return true;
                }
                if (args[0].equalsIgnoreCase("gm")) {
                    if (args.length == 3) {
                        try {
                            GameMode gm = GameMode.valueOf(arguments.getArguments().get(2).toUpperCase(Locale.ROOT));
                            plugin.getTardisHelper().setWorldGameMode(arguments.getArguments().get(1), gm);
                            plugin.getPlanetsConfig().set("planets." + arguments.getArguments().get(1) + ".gamemode", gm.toString());
                            plugin.savePlanetsConfig();
                            plugin.getMessenger().send(sender, TardisModule.TARDIS, "WORLD_GM_SET", arguments.getArguments().get(1), arguments.getArguments().get(2));
                            return true;
                        } catch (IllegalArgumentException e) {
                            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_GM", arguments.getArguments().get(2));
                            return true;
                        }
                    } else {
                        TARDISPlanetData data = plugin.getTardisHelper().getLevelData(arguments.getArguments().get(1));
                        plugin.getMessenger().send(sender, TardisModule.TARDIS, "WORLD_GM", data.getGameMode().toString());
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("load")) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "WORLD_LOADED", arguments.getArguments().get(1));
                } else {
                    // try to unload the world
                    plugin.getServer().unloadWorld(world, true);
                    plugin.getPlanetsConfig().set("planets." + arguments.getArguments().get(1) + ".enabled", false);
                    plugin.getPlanetsConfig().set("planets." + arguments.getArguments().get(1) + ".time_travel", false);
                    plugin.savePlanetsConfig();
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "WORLD_UNLOAD_SUCCESS", arguments.getArguments().get(1));
                }
            } else {
                if (args[0].equalsIgnoreCase("enable")) {
                    // is the world in the config?
                    if (!plugin.getPlanetsConfig().contains("planets." + arguments.getArguments().get(1))) {
                        plugin.getMessenger().sendColouredCommand(sender, "WORLD_NOT_FOUND", "/tardisworld load", plugin);
                        return true;
                    }
                    plugin.getPlanetsConfig().set("planets." + arguments.getArguments().get(1) + ".enabled", true);
                    plugin.savePlanetsConfig();
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "WORLD_ENABLED", arguments.getArguments().get(1));
                    if (TARDISConstants.isTARDISPlanetExact(arguments.getArguments().get(1))) {
                        switch (arguments.getArguments().get(1)) {
                            case "rooms" -> {
                                plugin.debug("Rooms world enabled, checking transmat location");
                                new RoomsWorld().check(plugin);
                            }
                            case "gallifrey" -> {
                                plugin.debug("Gallifrey enabled, registering planet event listeners");
                                plugin.getPM().registerEvents(new TARDISGallifreySpawnListener(plugin), plugin);
                                plugin.getTardisHelper().addCustomBiome("gallifrey");
                            }
                            case "siluria" -> {
                                plugin.debug("Siluria enabled, registering planet event listeners");
                                if (plugin.getConfig().getBoolean("modules.weeping_angels")) {
                                    plugin.getPM().registerEvents(new TARDISSiluriaSpawnListener(plugin), plugin);
                                }
                            }
                            default -> {
                                // skaro
                                plugin.debug("Skaro enabled, registering planet event listeners");
                                if (plugin.getPlanetsConfig().getBoolean("planets.skaro.acid")) {
                                    plugin.getPM().registerEvents(new TARDISAcidWater(plugin), plugin);
                                }
                                if (plugin.getConfig().getBoolean("modules.weeping_angels")) {
                                    plugin.getPM().registerEvents(new TARDISSkaroSpawnListener(plugin), plugin);
                                }
                                plugin.getTardisHelper().addCustomBiome("skaro");
                            }
                        }
                    }
                    TARDISWorlds.loadWorld(arguments.getArguments().get(1));
                    return true;
                }
                if (args[0].equalsIgnoreCase("rename")) {
                    if (args.length < 3) {
                        plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_WORLD_RENAME");
                        return true;
                    }
                    plugin.getTardisHelper().setLevelName(arguments.getArguments().get(1), arguments.getArguments().get(2));
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "WORLD_RENAME_SUCCESS", arguments.getArguments().get(2));
                    return true;
                }
                if (args[0].equalsIgnoreCase("gm")) {
                    plugin.getMessenger().sendColouredCommand(sender, "WORLD_NOT_FOUND", "/tardisworld load", plugin);
                    return true;
                }
                if (args[0].equalsIgnoreCase("load")) {
                    WorldType worldType = WorldType.NORMAL;
                    World.Environment environment = World.Environment.NORMAL;
                    String name = arguments.getArguments().get(1).toLowerCase(Locale.ROOT);
                    // try to load the world
                    WorldCreator creator = new WorldCreator(name);
                    if (args.length > 2) {
                        try {
                            worldType = WorldType.valueOf(arguments.getArguments().get(2).toUpperCase(Locale.ROOT));
                        } catch (IllegalArgumentException e) {
                            plugin.getMessenger().send(sender, TardisModule.TARDIS, "WORLD_TYPE", arguments.getArguments().get(2));
                            return true;
                        }
                    }
                    creator.type(worldType);
                    if (args.length > 3) {
                        try {
                            environment = World.Environment.valueOf(arguments.getArguments().get(3).toUpperCase(Locale.ROOT));
                        } catch (IllegalArgumentException e) {
                            plugin.getMessenger().send(sender, TardisModule.TARDIS, "WORLD_ENV", arguments.getArguments().get(3));
                            return true;
                        }
                    }
                    creator.environment(environment);
                    if (args.length > 4) {
                        // Check generator exists
                        String[] split = arguments.getArguments().get(4).split(":", 2);
                        Plugin gen = plugin.getPM().getPlugin(split[0]);
                        if (gen == null) {
                            plugin.getMessenger().send(sender, TardisModule.TARDIS, "WORLD_GEN", arguments.getArguments().get(4));
                            return true;
                        }
                        creator.generator(arguments.getArguments().get(4));
                    }
                    if (creator.createWorld() == null) {
                        plugin.getMessenger().sendColouredCommand(sender, "WORLD_NOT_FOUND", "/tardisworld load", plugin);
                        return true;
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
                        plugin.getPlanetsConfig().set("planets." + name + ".generator", args.length > 4 ? arguments.getArguments().get(4) : "DEFAULT");
                    }
                    plugin.savePlanetsConfig();
                } else {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "WORLD_UNLOADED", arguments.getArguments().get(1));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String lastArg = args[args.length - 1];
        if (args.length == 1) {
            List<String> part = partial(args[0], ROOT_SUBS);
            return (!part.isEmpty()) ? part : null;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("disable") || args[0].equalsIgnoreCase("update_name")) {
                return partial(lastArg, PLANET_SUBS);
            } else {
                return partial(lastArg, WORLD_SUBS);
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("gm")) {
                return partial(lastArg, GM_SUBS);
            } else {
                return partial(lastArg, TYPE_SUBS);
            }
        } else if (args.length == 4) {
            return partial(lastArg, ENV_SUBS);
        }
        return ImmutableList.of();
    }
}
