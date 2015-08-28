/*
 * Copyright (C) 2014 eccentric_nz
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
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

/**
 * TabCompleter for /tardisrecipe command
 */
public class TARDISRecipeTabComplete extends TARDISCompleter implements TabCompleter {

    private final List<String> ROOT_SUBS = ImmutableList.of("a-circuit", "ars-circuit", "battery", "blaster", "bow-tie", "bio-circuit", "biome-disk", "blank", "c-circuit", "cell", "custard", "d-circuit", "e-circuit", "filter", "fish-finger", "furnace", "glasses", "i-circuit", "ignite-circuit", "invisible", "jammy-dodger", "jelly-baby", "key", "l-circuit", "locator", "m-circuit", "memory-circuit", "oscillator", "pad", "painter", "player-disk", "preset-disk", "p-circuit", "r-circuit", "r-key", "randomiser-circuit", "remote", "s-circuit", "save-disk", "scanner-circuit", "sonic", "t-circuit", "tardis", "vortex", "watch");
    private final List<String> TARDIS_TYPES = ImmutableList.of("ars", "bigger", "budget", "custom", "deluxe", "eleventh", "redstone", "steampunk", "tom", "twelfth", "war", "wood");

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
