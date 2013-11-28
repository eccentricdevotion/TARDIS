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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISMakePresetCommand {

    private final TARDIS plugin;

    public TARDISMakePresetCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean scanBlocks(CommandSender sender, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (player == null) {
            sender.sendMessage(plugin.pluginName + "Only a player can run this command!");
            return true;
        }
        // check they are facing east
        String yaw = plugin.utils.getPlayersDirection(player, false);
        if (!yaw.equals("EAST")) {
            player.sendMessage(plugin.pluginName + "You must be facing EAST, with the preset front facing WEST!");
            return true;
        }
        player.sendMessage(plugin.pluginName + "Please right-click the lower left block of the preset with your TARDIS key. If there is no block there, place some sponge instead.");
        plugin.trackPreset.put(player.getName(), args[1]);
        return true;
    }
}
