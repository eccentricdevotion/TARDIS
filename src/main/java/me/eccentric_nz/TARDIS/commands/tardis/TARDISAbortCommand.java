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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISAbortCommand {

    private final TARDIS plugin;

    public TARDISAbortCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean doAbort(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(plugin.pluginName + "You must specify the task ID number!");
            return false;
        }
        try {
            int task = Integer.parseInt(args[1]);
            plugin.getServer().getScheduler().cancelTask(task);
            player.sendMessage(plugin.pluginName + "Task aborted!");
            return true;
        } catch (NumberFormatException nfe) {
            player.sendMessage(plugin.pluginName + "Task ID is not a number!");
            return false;
        }
    }
}
