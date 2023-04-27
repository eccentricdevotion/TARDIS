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
package me.eccentric_nz.tardisvortexmanipulator.command;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

/**
 * TabCompleter for /vm
 */
public class TVMTabComplete extends TARDISCompleter implements TabCompleter {

    private final ImmutableList<String> ROOT_SUBS = ImmutableList.of("go", "gui", "help", "message", "save", "remove", "lifesigns", "beacon", "activate", "give");
    private final ImmutableList<String> MSG_SUBS = ImmutableList.of("msg", "list", "read", "delete", "clear");
    private final ImmutableList<String> INOUT_SUBS = ImmutableList.of("in", "out");
    private final ImmutableList<String> HELP_SUBS = ImmutableList.of("command", "gui", "message", "tachyon");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length <= 1) {
            return partial(args[0], ROOT_SUBS);
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("message")) {
                return partial(args[0], MSG_SUBS);
            }
            if (args[0].equalsIgnoreCase("help")) {
                return partial(args[0], HELP_SUBS);
            }
            if (args[0].equalsIgnoreCase("give")) {
                // player names
                return null;
            }
        }
        if (args.length == 3) {
            if (args[1].equalsIgnoreCase("msg")) {
                return null; // online player names
            }
            if (args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("clear")) {
                return partial(args[1], INOUT_SUBS);
            }
        }
        return ImmutableList.of();
    }
}
