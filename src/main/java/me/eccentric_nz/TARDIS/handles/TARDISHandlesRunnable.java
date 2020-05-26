/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetReminders;
import me.eccentric_nz.TARDIS.database.data.Reminder;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TARDISHandlesRunnable implements Runnable {

    private final TARDIS plugin;

    public TARDISHandlesRunnable(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();
        // check reminders table
        ResultSetReminders rsr = new ResultSetReminders(plugin);
        if (rsr.resultSet()) {
            for (Reminder r : rsr.getReminders()) {
                if (currentTime > r.getTime()) {
                    Player player = plugin.getServer().getPlayer(r.getUuid());
                    if (player != null && player.isOnline()) {
                        TARDISSounds.playTARDISSound(player, "handles_reminder");
                        TARDISMessage.handlesSend(player, "HANDLES_REMINDER", r.getReminder());
                        // remove the reminder...
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("reminder_id", r.getReminder_id());
                        plugin.getQueryFactory().doDelete("reminders", where);
                    }
                }
            }
        }
    }
}
