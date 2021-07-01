/*
 * Copyright (C) 2021 eccentric_nz
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
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

/**
 * @author eccentric_nz
 */
class TARDISMakePresetCommand {

    private final TARDIS plugin;

    TARDISMakePresetCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean scanBlocks(CommandSender sender, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (player == null) {
            TARDISMessage.send(sender, "CMD_PLAYER");
            return true;
        }
        // check they are facing east
        String yaw = TARDISStaticUtils.getPlayersDirection(player, false);
        if (!yaw.equals("EAST")) {
            TARDISMessage.send(player, "PRESET_DIRECTION");
            return true;
        }
        String bool;
        if (args.length == 3) {
            // check they typed true of false
            String tf = args[2].toLowerCase(Locale.ENGLISH);
            if (!tf.equals("true") && !tf.equals("false")) {
                TARDISMessage.send(player, "TRUE_FALSE");
                return false;
            }
            bool = tf;
        } else {
            // presume it is assymetric if not set
            bool = "true";
        }
        TARDISMessage.send(player, "PRESET_INFO");
        plugin.getTrackerKeeper().getPreset().put(player.getUniqueId(), args[1] + ":" + bool);
        return true;
    }
}
