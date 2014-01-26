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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.destroyers.TARDISPruner;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISPruneCommand {

    private final TARDIS plugin;

    public TARDISPruneCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean startPruning(CommandSender sender, String[] args) {
        TARDISPruner pruner = new TARDISPruner(plugin);
        if (args[1].equalsIgnoreCase("list") && args.length == 3) {
            int i = 0;
            int days = plugin.utils.parseInt(args[2]);
            pruner.list(sender, days);
            return true;
        } else {
            try {
                int days = plugin.utils.parseInt(args[1]);
                pruner.prune(sender, days);
            } catch (NumberFormatException nfe) {
                return false;
            }
        }
        return false;
    }
}
