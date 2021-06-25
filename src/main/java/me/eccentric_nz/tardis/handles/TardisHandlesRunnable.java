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
package me.eccentric_nz.tardis.handles;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.data.Reminder;
import me.eccentric_nz.tardis.database.resultset.ResultSetReminders;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisSounds;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TardisHandlesRunnable implements Runnable {

    private final TardisPlugin plugin;

    public TardisHandlesRunnable(TardisPlugin plugin) {
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
                        TardisSounds.playTardisSound(player, "handles_reminder");
                        TardisMessage.handlesSend(player, "HANDLES_REMINDER", r.getReminder());
                        // remove the reminder...
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("reminder_id", r.getReminderId());
                        plugin.getQueryFactory().doDelete("reminders", where);
                    }
                }
            }
        }
    }
}
