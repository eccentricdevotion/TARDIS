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
package me.eccentric_nz.TARDIS.commands.dev;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * TabCompleter for /tardisadmin
 */
public class TARDISDevTabComplete extends TARDISCompleter implements TabCompleter {

    private final ImmutableList<String> ROOT_SUBS = ImmutableList.of("add_regions", "advancements", "list", "set_biome", "stats", "tree");
    private final ImmutableList<String> LIST_SUBS = ImmutableList.of("preset_perms", "perms", "recipes", "blueprints");
    private final ImmutableList<String> BIOME_SUBS = ImmutableList.of("by_walking", "tardis:gallifrey_badlands", "tardis:gallifrey_eroded", "tardis:gallifrey_plateau", "tardis:skaro_lakes", "tardis:skaro_desert", "tardis:skaro_hills");

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        String lastArg = args[args.length - 1];
        if (args.length <= 1) {
            return partial(args[0], ROOT_SUBS);
        } else if (args.length == 2) {
            String sub = args[0];
            if (sub.equals("list")) {
                return partial(lastArg, LIST_SUBS);
            }
            if (sub.equals("set_biome")) {
                return partial(lastArg, BIOME_SUBS);
            }
        }
        return ImmutableList.of();
    }
}
