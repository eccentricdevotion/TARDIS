/*
 * Copyright (C) 2022 eccentric_nz
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
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TabCompleter for /tardisrecipe command
 */
public class TARDISRecipeTabComplete extends TARDISCompleter implements TabCompleter {

    public static final Set<String> ROOT_SUBS = new HashSet<>();
    public static final List<String> TARDIS_TYPES = ImmutableList.of("ars", "bigger", "budget", "coral", "custom", "deluxe", "division", "eleventh", "ender", "master", "pyramid", "redstone", "rotor", "steampunk", "thirteenth", "tom", "twelfth", "war", "wood", "legacy_budget", "legacy_bigger", "legacy_deluxe", "legacy_eleventh", "legacy_redstone");

    public TARDISRecipeTabComplete() {
        ROOT_SUBS.add("seed");
        ROOT_SUBS.add("tardis");
        for (RecipeItem recipeItem : RecipeItem.values()) {
            ROOT_SUBS.add(recipeItem.toTabCompletionString());
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        String lastArg = args[args.length - 1];
        if (args.length <= 1) {
            return partial(args[0], ROOT_SUBS);
        } else if (args.length == 2) {
            String sub = args[0];
            if (sub.equals("tardis") || sub.equals("seed")) {
                return partial(lastArg, TARDIS_TYPES);
            }
        }
        return ImmutableList.of();
    }
}
