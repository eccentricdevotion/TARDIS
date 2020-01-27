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
package me.eccentric_nz.TARDIS.commands.admin;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.Set;

/**
 * TabCompleter for /tardisgive
 */
public class TARDISGiveTabComplete extends TARDISCompleter implements TabCompleter {

    private final ImmutableList<String> GIVE_SUBS = ImmutableList.of("artron", "kit", "a-circuit", "acid-battery", "arrow-circuit", "ars-circuit", "battery", "blaster", "bow-tie", "bio-circuit", "biome-disk", "blank", "c-circuit", "cell", "communicator", "custard", "d-circuit", "e-circuit", "filter", "fish-finger", "furnace", "generator", "glasses", "handles", "i-circuit", "ignite-circuit", "invisible", "jammy-dodger", "jelly-baby", "key", "keyboard", "l-circuit", "locator", "m-circuit", "memory-circuit", "oscillator", "pad", "painter", "paper-bag", "player-disk", "preset-disk", "p-circuit", "r-circuit", "r-key", "randomiser-circuit", "reader", "remote", "rift-circuit", "rift-manipulator", "rust", "s-circuit", "save-disk", "scanner-circuit", "seed", "sonic", "t-circuit", "telepathic", "tachyon", "vortex", "wand", "watch");
    private final ImmutableList<String> GIVE_KNOWLEDGE = ImmutableList.of("knowledge", "1", "2", "64");
    private final ImmutableList<String> KIT_SUBS;
    private final ImmutableList<String> SEED_SUBS;

    public TARDISGiveTabComplete(TARDIS plugin) {
        Set<String> kits = plugin.getKitsConfig().getConfigurationSection("kits").getKeys(false);
        KIT_SUBS = ImmutableList.copyOf(kits);
        SEED_SUBS = ImmutableList.copyOf(CONSOLES.getBY_NAMES().keySet());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String lastArg = args[args.length - 1];
        if (args.length <= 1) {
            return null;
        } else if (args.length == 2) {
            return partial(lastArg, GIVE_SUBS);
        } else if (args.length == 3) {
            String sub = args[1];
            if (sub.equals("kit")) {
                return partial(lastArg, KIT_SUBS);
            }
            if (sub.equals("seed")) {
                return partial(lastArg, SEED_SUBS);
            }
            return partial(lastArg, GIVE_KNOWLEDGE);
        }
        return ImmutableList.of();
    }
}
