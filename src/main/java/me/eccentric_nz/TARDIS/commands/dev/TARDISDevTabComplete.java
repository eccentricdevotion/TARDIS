/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.dev;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

/**
 * TabCompleter for /tardisdev
 */
public class TARDISDevTabComplete extends TARDISCompleter implements TabCompleter {

    private final ImmutableList<String> ROOT_SUBS = ImmutableList.of("add_regions", "advancements", "chunky", "list", "plurals", "stats", "tree", "snapshot", "displayitem");
    private final ImmutableList<String> LIST_SUBS = ImmutableList.of("preset_perms", "perms", "recipes", "blueprints", "commands", "block_colours", "change");
    private final ImmutableList<String> SNAPSHOT_SUBS = ImmutableList.of("in", "out", "c");
    private final ImmutableList<String> DISPLAY_SUBS = ImmutableList.of("add", "remove", "place", "break");
    private final ImmutableList<String> MUSHROOM_SUBS = ImmutableList.of("advanced_console", "ancient", "ars", "bigger", "blue_box", "blue_lamp_on", "blue_lamp", "budget", "cave", "cog", "compound", "constructor", "copper", "coral", "creative", "custom", "delta", "deluxe", "disk_storage", "division", "eleventh", "ender", "factory", "fugitive", "green_lamp_on", "green_lamp", "grow", "heat_block", "hexagon", "lab", "lamp_off", "lantern_off", "legacy_bigger", "legacy_budget", "legacy_deluxe", "legacy_eleventh", "legacy_redstone", "master", "mechanical", "medium", "original", "pandorica", "plank", "product", "purple_lamp_on", "purple_lamp", "pyramid", "red_lamp_on", "red_lamp", "redstone", "reducer", "rotor", "roundel_offset", "roundel", "siege_cube", "small", "steampunk", "tall", "the_moment", "thirteenth", "tom", "twelfth", "war", "weathered");
    private final List<String> MAT_SUBS = new ArrayList<>();

    public TARDISDevTabComplete(TARDIS plugin) {
        plugin.getTardisHelper().getTreeMatrials().forEach((m) -> MAT_SUBS.add(m.toString()));
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        String lastArg = args[args.length - 1];
        switch (args.length) {
            case 1 -> {
                return partial(args[0], ROOT_SUBS);
            }
            case 2 -> {
                String sub = args[0];
                if (sub.equals("list")) {
                    return partial(lastArg, LIST_SUBS);
                }   if (sub.equals("tree")) {
                    return partial(lastArg, MAT_SUBS);
                }   if (sub.equals("snapshot")) {
                    return partial(lastArg, SNAPSHOT_SUBS);
                }   if (sub.equals("displayitem")) {
                    return partial(lastArg, DISPLAY_SUBS);
                }
            }
            default -> {
                if (args[1].equals("place")) {
                    return partial(lastArg, MUSHROOM_SUBS);
                } else {
                    return partial(lastArg, MAT_SUBS);
                }
            }
        }
        return ImmutableList.of();
    }
}
