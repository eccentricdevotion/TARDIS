/*
 * Copyright (C) 2015 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISQuestionMarkCommand implements CommandExecutor {

    private final TARDIS plugin;

    public TARDISQuestionMarkCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardis?")) {
            TARDISCommandHelper tch = new TARDISCommandHelper(plugin);
            switch (args.length) {
                case 2:
                    String cmds = args[0].toLowerCase() + " " + args[1].toLowerCase();
                    tch.getCommand(cmds, sender);
                    return true;
                case 1:
                    tch.getCommand(args[0].toLowerCase(), sender);
                    return true;
                default:
                    tch.getCommand("", sender);
                    return true;
            }
        }
        return false;
    }
}
