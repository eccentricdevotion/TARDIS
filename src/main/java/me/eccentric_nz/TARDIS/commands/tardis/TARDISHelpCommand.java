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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import org.bukkit.command.CommandSender;

import java.util.Locale;

/**
 * @author eccentric_nz
 */
class TARDISHelpCommand {

    private final TARDIS plugin;

    TARDISHelpCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean showHelp(CommandSender sender, String[] args) {
        TARDISCommandHelper tch = new TARDISCommandHelper(plugin);
        if (args.length == 1) {
            tch.getCommand("", sender);
            return true;
        }
        if (args.length == 2) {
            tch.getCommand(args[1].toLowerCase(Locale.ENGLISH), sender);
            return true;
        }
        if (args.length > 2) {
            String cmds = args[1].toLowerCase(Locale.ENGLISH) + " " + args[2].toLowerCase(Locale.ENGLISH);
            tch.getCommand(cmds, sender);
            return true;
        }
        return true;
    }
}
