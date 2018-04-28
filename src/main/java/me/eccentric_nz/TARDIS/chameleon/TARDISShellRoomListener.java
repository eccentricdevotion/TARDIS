/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.TARDIS;

/**
 *
 * The shell room is the room in a TARDIS where the plasmic shells are stored.
 * The chameleon circuit then picks the most suitable shell from the room and
 * applies it as the ship's exterior. The shell room, ten metres wide, is more
 * like a seemingly unending corridor displaying a large variety of plasmic
 * shells for different times and places.
 *
 * In the case of the Doctor's TARDIS, whose chameleon circuit was broken from
 * the start, the room is useless and the Doctor never uses it. The Fourth
 * Doctor and Romana II were bewildered to find a message on the TARDIS scanner
 * saying, "Warning: Shell room full." This led to the two discovering the
 * TARDIS' final exterior before becoming a police box: a post box with a
 * misplaced letter inside.
 *
 * @author eccentric_nz
 */
public class TARDISShellRoomListener {

    private final TARDIS plugin;

    public TARDISShellRoomListener(TARDIS plugin) {
        this.plugin = plugin;
    }

}
