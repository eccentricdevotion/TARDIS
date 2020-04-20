/*
 * Copyright (C) 2020 eccentric_nz
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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

/**
 * TabCompleter for /tardisrecipe command
 */
public class TARDISRecipeTabComplete extends TARDISCompleter implements TabCompleter {

    public static final List<String> ROOT_SUBS = ImmutableList.of("a-circuit", "acid-battery", "arrow-circuit", "ars-circuit", "battery", "blaster", "bow-tie", "bio-circuit", "biome-disk", "blank", "c-circuit", "cell", "communicator", "custard", "d-circuit", "e-circuit", "filter", "fish-finger", "furnace", "generator", "glasses", "handles", "i-circuit", "ignite-circuit", "invisible", "jammy-dodger", "jelly-baby", "key", "keyboard", "l-circuit", "locator", "m-circuit", "memory-circuit", "oscillator", "pad", "painter", "paper-bag", "player-disk", "preset-disk", "p-circuit", "r-circuit", "r-key", "randomiser-circuit", "reader", "remote", "rift-circuit", "rift-manipulator", "rust", "s-circuit", "save-disk", "scanner-circuit", "sonic", "t-circuit", "tardis", "telepathic", "vortex", "wand", "watch");
    public static final List<String> TARDIS_TYPES = ImmutableList.of("ars", "bigger", "budget", "coral", "custom", "deluxe", "eleventh", "ender", "master", "pyramid", "redstone", "steampunk", "thirteenth", "tom", "twelfth", "war", "wood", "legacy_budget", "legacy_bigger", "legacy_deluxe", "legacy_eleventh", "legacy_redstone");

    public TARDISRecipeTabComplete() {
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        String lastArg = args[args.length - 1];
        if (args.length <= 1) {
            return partial(args[0], ROOT_SUBS);
        } else if (args.length == 2) {
            String sub = args[0];
            if (sub.equals("tardis")) {
                return partial(lastArg, TARDIS_TYPES);
            }
        }
        return ImmutableList.of();
    }
}
