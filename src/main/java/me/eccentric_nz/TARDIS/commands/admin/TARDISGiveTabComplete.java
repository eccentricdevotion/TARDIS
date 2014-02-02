/*
 * Copyright (C) 2013 eccentric_nz
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

/**
 * TabCompleter for /tardisgive
 */
public class TARDISGiveTabComplete implements TabCompleter {

    private final TARDIS plugin;
    private final ImmutableList<String> GIVE_SUBS = ImmutableList.of("artron", "kit", "a-circuit", "ars-circuit", "bio-circuit", "biome-disk", "blank", "cell", "c-circuit", "d-circuit", "e-circuit", "filter", "i-circuit", "key", "l-circuit", "locator", "m-circuit", "memory-circuit", "oscillator", "p-circuit", "player-disk", "preset-disk", "r-circuit", "remote", "s-circuit", "scanner-circuit", "save-disk", "sonic", "t-circuit");
    private final Set<String> kits;
    private final ImmutableList<String> KIT_SUBS;

    public TARDISGiveTabComplete(TARDIS plugin) {
        this.plugin = plugin;
        this.kits = plugin.getKitsConfig().getConfigurationSection("kits").getKeys(false);
        this.KIT_SUBS = ImmutableList.copyOf(kits);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String lastArg = args[args.length - 1];
        if (args.length <= 1) {
            return null;
        } else if (args.length == 2) {
            return partial(lastArg, GIVE_SUBS);
        } else {
            String sub = args[1];
            if (sub.equals("kit")) {
                return partial(lastArg, KIT_SUBS);
            }
        }
        return ImmutableList.of();
    }

    private List<String> partial(String token, Collection<String> from) {
        return StringUtil.copyPartialMatches(token, from, new ArrayList<String>(from.size()));
    }
}
