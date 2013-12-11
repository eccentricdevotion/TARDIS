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
import java.util.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

/**
 * TabCompleter for /tardisgive
 */
public class TARDISGiveTabComplete implements TabCompleter {

    private final ImmutableList<String> GIVE_SUBS = ImmutableList.of("artron", "remote", "locator", "l-circuit", "m-circuit", "s-circuit", "c-circuit", "sonic", "blank", "save-disk", "preset-disk", "biome-disk", "player-disk");

    public TARDISGiveTabComplete() {
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String lastArg = args[args.length - 1];
        if (args.length <= 1) {
            return null;
        } else if (args.length == 2) {
            return partial(lastArg, GIVE_SUBS);
        }
        return ImmutableList.of();
    }

    private List<String> partial(String token, Collection<String> from) {
        return StringUtil.copyPartialMatches(token, from, new ArrayList<String>(from.size()));
    }
}
