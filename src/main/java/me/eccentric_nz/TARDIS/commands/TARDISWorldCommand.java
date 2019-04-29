/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Teleport to the spawn point of worlds on the server
 *
 * @author eccentric_nz
 */
public class TARDISWorldCommand extends TARDISCompleter implements CommandExecutor, TabCompleter {

    private final TARDIS plugin;
    private final List<String> ROOT_SUBS = Arrays.asList("load", "unload");
    private final List<String> WORLD_SUBS = new ArrayList<>();
    private final List<String> TYPE_SUBS = new ArrayList<>();
    private final List<String> ENV_SUBS = new ArrayList<>();

    public TARDISWorldCommand(TARDIS plugin) {
        this.plugin = plugin;
        WORLD_SUBS.addAll(plugin.getTardisAPI().getWorlds());
        for (WorldType wt : WorldType.values()) {
            TYPE_SUBS.add(wt.getName());
        }
        for (World.Environment e : World.Environment.values()) {
            ENV_SUBS.add(e.toString());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisworld")) {
            if (args.length < 2) {
                TARDISMessage.send(sender, "TOO_FEW_ARGS");
                return false;
            }
            if (!ROOT_SUBS.contains(args[0])) {
                TARDISMessage.send(sender, "ARG_LOAD_UNLOAD");
                return false;
            }
            World world = plugin.getServer().getWorld(args[1]);
            if (world != null) {
                if (args[0].toLowerCase().equals("load")) {
                    TARDISMessage.send(sender, "WORLD_LOADED", args[1]);
                    return true;
                } else {
                    // try to unload the world
                    plugin.getServer().unloadWorld(world, true);
                    plugin.getConfig().set("worlds." + args[1], false);
                    TARDISMessage.send(sender, "WORLD_UNLOAD_SUCCESS", args[1]);
                    return true;
                }
            } else {
                if (args[0].toLowerCase().equals("load")) {
                    // try to load the world
                    WorldType worldType = WorldType.NORMAL;
                    World.Environment environment = World.Environment.NORMAL;
                    if (args.length > 2) {
                        try {
                            worldType = WorldType.valueOf(args[2].toUpperCase(Locale.ENGLISH));
                        } catch (IllegalArgumentException e) {
                            TARDISMessage.send(sender, "WORLD_TYPE", args[2]);
                            return true;
                        }
                    }
                    if (args.length > 3) {
                        try {
                            environment = World.Environment.valueOf(args[3].toUpperCase(Locale.ENGLISH));
                        } catch (IllegalArgumentException e) {
                            TARDISMessage.send(sender, "WORLD_ENV", args[3]);
                            return true;
                        }
                    }
                    if (WorldCreator.name(args[1]).type(worldType).environment(environment).createWorld() == null) {
                        TARDISMessage.send(sender, "WORLD_NOT_FOUND");
                        return true;
                    }
                    plugin.getConfig().set("worlds." + args[1], true);
                    return true;
                } else {
                    TARDISMessage.send(sender, "WORLD_UNLOADED", args[1]);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String lastArg = args[args.length - 1];
        if (args.length <= 1) {
            List<String> part = partial(args[0], ROOT_SUBS);
            return (part.size() > 0) ? part : null;
        } else if (args.length == 2) {
            return partial(lastArg, WORLD_SUBS);
        } else if (args.length == 3) {
            return partial(lastArg, TYPE_SUBS);
        } else if (args.length == 4) {
            return partial(lastArg, ENV_SUBS);
        }
        return ImmutableList.of();
    }
}
