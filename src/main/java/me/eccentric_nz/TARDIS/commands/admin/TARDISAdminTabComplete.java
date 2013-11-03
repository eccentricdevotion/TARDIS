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
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

/**
 * TabCompleter for /tardistravel
 */
public class TARDISAdminTabComplete implements TabCompleter {

    private final TARDIS plugin;
    private final ImmutableList<String> DIFFICULTY_SUBS = ImmutableList.of("easy", "normal", "hard");
    private final ImmutableList<String> BOOL_SUBS = ImmutableList.of("true", "false");

    public TARDISAdminTabComplete(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String lastArg = args[args.length - 1];
        if (args.length <= 1) {
            return partial(args[0], combineLists());
        } else if (args.length == 2) {
            String sub = args[0];
            if (sub.equals("difficulty")) {
                return partial(lastArg, DIFFICULTY_SUBS);
            } else {
                return partial(lastArg, BOOL_SUBS);
            }
        }
        return ImmutableList.of();
    }

    private List<String> partial(String token, Collection<String> from) {
        return StringUtil.copyPartialMatches(token, from, new ArrayList<String>(from.size()));
    }

    private List<String> combineLists() {
        List<String> newList = new ArrayList<String>(plugin.tardisAdminCommand.firstsStr.size() + plugin.tardisAdminCommand.firstsBool.size() + plugin.tardisAdminCommand.firstsInt.size() + plugin.tardisAdminCommand.firstsStrArtron.size() + plugin.tardisAdminCommand.firstsIntArtron.size());
        newList.addAll(plugin.tardisAdminCommand.firstsStr);
        newList.addAll(plugin.tardisAdminCommand.firstsBool);
        newList.addAll(plugin.tardisAdminCommand.firstsInt);
        newList.addAll(plugin.tardisAdminCommand.firstsStrArtron);
        newList.addAll(plugin.tardisAdminCommand.firstsIntArtron);
        return newList;
    }
}
