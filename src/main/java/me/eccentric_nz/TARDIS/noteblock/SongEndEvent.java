/*
 * Copyright (C) 2014  michidk && xxmicloxx
 * http://dev.bukkit.org/bukkit-plugins/noteblockapi/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.noteblock;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SongEndEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final SongPlayer song;

    public SongEndEvent(SongPlayer song) {
        this.song = song;
    }

    public SongPlayer getSongPlayer() {
        return song;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
