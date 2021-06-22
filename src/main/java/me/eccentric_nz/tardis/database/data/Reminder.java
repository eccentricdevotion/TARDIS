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
package me.eccentric_nz.tardis.database.data;

import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class Reminder {

    private final int reminderId;
    private final UUID uuid;
    private final String reminder;
    private final long time;

    public Reminder(int reminderId, UUID uuid, String reminder, long time) {
        this.reminderId = reminderId;
        this.uuid = uuid;
        this.reminder = reminder;
        this.time = time;
    }

    public int getReminderId() {
        return reminderId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getReminder() {
        return reminder;
    }

    public long getTime() {
        return time;
    }
}
