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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISRescueCommand {

    private final TARDIS plugin;

    public TARDISRescueCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean startRescue(final Player player, String[] args) {
        if (args.length < 2) {
            TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.TOO_FEW_ARGS.getText());
            return true;
        }
        if (player.hasPermission("tardis.timetravel.player")) {
            final String saved = args[1];
            Player destPlayer = plugin.getServer().getPlayer(saved);
            if (destPlayer == null) {
                TARDISMessage.send(player, plugin.getPluginName() + "That player is not online!");
                return true;
            }
            String playerNameStr = player.getName();
            TARDISMessage.send(destPlayer, plugin.getPluginName() + playerNameStr + " wants to rescue you! Type: " + ChatColor.AQUA + " tardis rescue accept " + ChatColor.RESET + " in chat within 60 seconds to accept the rescue.");
            plugin.getTrackerKeeper().getTrackChat().put(saved, playerNameStr);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    if (plugin.getTrackerKeeper().getTrackChat().containsKey(saved)) {
                        plugin.getTrackerKeeper().getTrackChat().remove(saved);
                        TARDISMessage.send(player, plugin.getPluginName() + saved + " didn't respond with 60 seconds, aborting rescue!");
                    }
                }
            }, 1200L);
        } else {
            TARDISMessage.send(player, plugin.getPluginName() + "You do not have permission to time travel to a player!");
            return true;
        }
        return false;
    }
}
