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
package me.eccentric_nz.TARDIS.commands.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISHandlesRemindCommand {

    private final TARDIS plugin;

    TARDISHandlesRemindCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean doReminder(Player player, String[] args) {
        String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length - 1));
        // the last argument should be a number
        long when = TARDISNumberParsers.parseLong(args[args.length - 1]);
        if (when == 0) {
            TARDISMessage.handlesMessage(player, "HANDLES_NUMBER");
            return true;
        }
        TARDISMessage.handlesSend(player, "HANDLES_OK", "" + when);
        TARDISSounds.playTARDISSound(player, "handles_confirmed", 5L);
        // convert minutes to milliseconds
        when *= 60000;
        // add the current time in milliseconds
        when = when + System.currentTimeMillis();
        // create a reminder record
        HashMap<String, Object> data = new HashMap<>();
        data.put("uuid", player.getUniqueId().toString());
        data.put("reminder", message);
        data.put("time", when);
        plugin.getQueryFactory().doInsert("reminders", data);
        return true;
    }
}
