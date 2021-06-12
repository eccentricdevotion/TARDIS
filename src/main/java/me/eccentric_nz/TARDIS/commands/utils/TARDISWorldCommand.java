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
package me.eccentric_nz.TARDIS.commands.utils;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.planets.*;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import me.eccentric_nz.tardischunkgenerator.helpers.TARDISPlanetData;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * Teleport to the spawn point of worlds on the server
 *
 * @author eccentric_nz
 */
public class TARDISWorldCommand extends TARDISCompleter implements CommandExecutor, TabCompleter {

    private final TARDIS plugin;
    private final List<String> ROOT_SUBS = Arrays.asList("load", "unload", "gm", "rename");
    private final List<String> WORLD_SUBS = new ArrayList<>();
    private final List<String> TYPE_SUBS = new ArrayList<>();
    private final List<String> ENV_SUBS = new ArrayList<>();
    private final List<String> GM_SUBS = new ArrayList<>();

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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisworld")) {
            if (sender == null) {
                plugin.debug("Sender was null!");
                return true;
            }
            if (args.length < 2) {
                TARDISMessage.send(sender, "TOO_FEW_ARGS");
                return false;
            }
            if (!ROOT_SUBS.contains(args[0])) {
                TARDISMessage.send(sender, "ARG_LOAD_UNLOAD");
                return false;
            }
            World world = TARDISAliasResolver.getWorldFromAlias(args[1]);
            if (world != null) {
                if (args[0].equalsIgnoreCase("rename")) {
                    if (args.length < 3) {
                        TARDISMessage.send(sender, "ARG_WORLD_RENAME");
                        return true;
                    }
                    // remove players from world
                    List<Player> players = world.getPlayers();
                    Location spawn = plugin.getServer().getWorlds().get(0).getSpawnLocation();
                    players.forEach((p) -> {
                        TARDISMessage.send(p, "WORLD_RENAME");
                        p.teleport(spawn);
                    });
                    // unload world
                    plugin.getServer().unloadWorld(world, true);
                    plugin.getTardisHelper().setLevelName(args[1], args[2]);
                    // rename the planet in planets.yml
                    ConfigurationSection section = plugin.getPlanetsConfig().getConfigurationSection("planets." + args[1]);
                    if (section != null) {
                        Map<String, Object> map = section.getValues(true);
                        plugin.getPlanetsConfig().set("planets." + args[2], map);
                        plugin.getPlanetsConfig().set("planets." + args[1], null);
                        plugin.savePlanetsConfig();
                    }
                    // load world
                    TARDISWorlds.loadWorld(args[2]);
                    TARDISMessage.send(sender, "WORLD_RENAME_SUCCESS", args[2]);
                    return true;
                }
                if (args[0].equalsIgnoreCase("gm")) {
                    if (args.length == 3) {
                        try {
                            GameMode gm = GameMode.valueOf(args[2].toUpperCase());
                            plugin.getTardisHelper().setWorldGameMode(args[1], gm);
                            plugin.getPlanetsConfig().set("planets." + args[1] + ".gamemode", gm.toString());
                            plugin.savePlanetsConfig();
                            TARDISMessage.send(sender, "WORLD_GM_SET", args[1], args[2]);
                            return true;
                        } catch (IllegalArgumentException e) {
                            TARDISMessage.send(sender, "ARG_GM", args[2]);
                            return true;
                        }
                    } else {
                        TARDISPlanetData data = plugin.getTardisHelper().getLevelData(args[1]);
                        TARDISMessage.send(sender, "WORLD_GM", data.getGameMode().toString());
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("load")) {
                    TARDISMessage.send(sender, "WORLD_LOADED", args[1]);
                } else {
                    // try to unload the world
                    plugin.getServer().unloadWorld(world, true);
                    plugin.getPlanetsConfig().set("planets." + args[1] + ".enabled", false);
                    plugin.getPlanetsConfig().set("planets." + args[1] + ".time_travel", false);
                    plugin.savePlanetsConfig();
                    TARDISMessage.send(sender, "WORLD_UNLOAD_SUCCESS", args[1]);
                }
            } else {
                if (args[0].equalsIgnoreCase("rename")) {
                    if (args.length < 3) {
                        TARDISMessage.send(sender, "ARG_WORLD_RENAME");
                        return true;
                    }
                    plugin.getTardisHelper().setLevelName(args[1], args[2]);
                    TARDISMessage.send(sender, "WORLD_RENAME_SUCCESS", args[2]);
                    return true;
                }
                if (args[0].equalsIgnoreCase("gm")) {
                    TARDISMessage.send(sender, "WORLD_NOT_FOUND");
                    return true;
                }
                if (args[0].equalsIgnoreCase("load")) {
                    WorldType worldType = WorldType.NORMAL;
                    World.Environment environment = World.Environment.NORMAL;
                    String name = args[1].toLowerCase(Locale.ROOT);
                    if (name.equals("gallifrey") || name.equals("siluria") || name.equals("skaro")) {
                        switch (name) {
                            case "gallifrey":
                                new TARDISGallifrey(plugin).loadTimeLordWorld();
                                break;
                            case "siluria":
                                new TARDISSiluria(plugin).loadSilurianUnderworld();
                                break;
                            default:
                                new TARDISSkaro(plugin).loadDalekWorld();
                                break;
                        }
                        Reader reader = new InputStreamReader(plugin.getResource("planets_template.yml"));
                        FileConfiguration pConfig = YamlConfiguration.loadConfiguration(reader);
                        ConfigurationSection section = pConfig.getConfigurationSection("planets." + TARDISStringUtils.uppercaseFirst(name));
                        ConfigurationSection rules = pConfig.getConfigurationSection("planets." + TARDISStringUtils.uppercaseFirst(name) + ".gamerules");
                        String s_world = plugin.getServer().getWorlds().get(0).getName();
                        name = s_world + "_tardis_" + name;
                        plugin.getPlanetsConfig().createSection("planets." + name, section.getValues(true));
                        plugin.getPlanetsConfig().set("planets." + name + "gamerules", null);
                        plugin.getPlanetsConfig().createSection("planets." + name + ".gamerules", rules.getValues(true));
                        plugin.getPlanetsConfig().set("planets." + name + ".time_travel", true);
                        if (name.equals(s_world + "_tardis_skaro")) {
                            plugin.getPlanetsConfig().set("planets." + name + ".acid_potions", Arrays.asList("WEAKNESS", "POISON"));
                        }
                        try {
                            reader.close();
                        } catch (IOException e) {
                            plugin.debug("Could not close input stream reader!");
                        }
                    } else {
                        // try to load the world
                        WorldCreator creator = new WorldCreator(name);
                        if (args.length > 2) {
                            try {
                                worldType = WorldType.valueOf(args[2].toUpperCase(Locale.ENGLISH));
                            } catch (IllegalArgumentException e) {
                                TARDISMessage.send(sender, "WORLD_TYPE", args[2]);
                                return true;
                            }
                        }
                        creator.type(worldType);
                        if (args.length > 3) {
                            try {
                                environment = World.Environment.valueOf(args[3].toUpperCase(Locale.ENGLISH));
                            } catch (IllegalArgumentException e) {
                                TARDISMessage.send(sender, "WORLD_ENV", args[3]);
                                return true;
                            }
                        }
                        creator.environment(environment);
                        if (args.length > 4) {
                            // Check generator exists
                            String[] split = args[4].split(":", 2);
                            Plugin gen = plugin.getPM().getPlugin(split[0]);
                            if (gen == null) {
                                TARDISMessage.send(sender, "WORLD_GEN", args[4]);
                                return true;
                            }
                            creator.generator(args[4]);
                        }
                        if (creator.createWorld() == null) {
                            TARDISMessage.send(sender, "WORLD_NOT_FOUND");
                            return true;
                        }
                        plugin.getPlanetsConfig().set("planets." + name + ".time_travel", false);
                        plugin.getPlanetsConfig().set("planets." + name + ".resource_pack", "default");
                    }
                    plugin.getPlanetsConfig().set("planets." + name + ".enabled", true);
                    plugin.getPlanetsConfig().set("planets." + name + ".gamemode", "SURVIVAL");
                    plugin.getPlanetsConfig().set("planets." + name + ".world_type", worldType.toString());
                    plugin.getPlanetsConfig().set("planets." + name + ".environment", environment.toString());
                    plugin.getPlanetsConfig().set("planets." + name + ".generator", args.length > 4 ? args[4] : "DEFAULT");
                    plugin.savePlanetsConfig();
                } else {
                    TARDISMessage.send(sender, "WORLD_UNLOADED", args[1]);
                }
            }
            return true;
        }
        return false;
    }

//    Function<String, Boolean> hasUpperCase = s -> s.chars().filter(c -> Character.isUpperCase(c)).count() > 0;

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        String lastArg = args[args.length - 1];
        if (args.length <= 1) {
            List<String> part = partial(args[0], ROOT_SUBS);
            return (part.size() > 0) ? part : null;
        } else if (args.length == 2) {
            return partial(lastArg, WORLD_SUBS);
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
