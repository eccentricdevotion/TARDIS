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
import me.eccentric_nz.TARDIS.destroyers.TARDISExterminator;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISExterminateCommand {

    private final TARDIS plugin;

    public TARDISExterminateCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean doExterminate(CommandSender sender, Player player) {
        if (player == null) {
            sender.sendMessage(plugin.getPluginName() + MESSAGE.MUST_BE_PLAYER.getText());
            return false;
        }
        if (!plugin.getTrackerKeeper().getTrackExterminate().containsKey(player.getName())) {
            sender.sendMessage(plugin.getPluginName() + "You must break the TARDIS Police Box sign first!");
            return false;
        }
        TARDISExterminator del = new TARDISExterminator(plugin);
        return del.exterminate(player, plugin.getTrackerKeeper().getTrackExterminate().get(player.getName()));
    }
}
