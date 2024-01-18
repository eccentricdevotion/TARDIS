/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.api.event;

import me.eccentric_nz.TARDIS.ARS.TARDISARSSlot;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.rooms.TARDISRoomData;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
public final class TARDISRoomGrowEvent extends TARDISEvent {

    private final TARDISARSSlot slot;
    private final TARDISRoomData roomData;

    /**
     * A room growing event.
     *
     * @param player   the player growing the room
     * @param tardis   the Tardis data object, may be null - if room was manually grown, use {@link
     *                 #getRoomData()}.getTardis_id()
     * @param slot     the TARDISARSSlot data object, may be null - if the room was manually grown, use {@link
     *                 #getRoomData()}.getLocation()
     * @param roomData the TARDISRoomData data object
     */
    public TARDISRoomGrowEvent(Player player, Tardis tardis, TARDISARSSlot slot, TARDISRoomData roomData) {
        super(player, tardis);
        this.slot = slot;
        this.roomData = roomData;
    }

    /**
     * Returns the Architectural Reconfiguration System position data object. Use the {@link
     * me.eccentric_nz.TARDIS.ARS.TARDISARSSlot TARDISARSSlot}'s getter methods to retrieve the coordinates.
     *
     * @return the TARDISARSSlot object or null if the room was manually grown
     */
    public TARDISARSSlot getSlot() {
        return slot;
    }

    /**
     * Returns the room data object. Use the {@link me.eccentric_nz.TARDIS.rooms.TARDISRoomData TARDISRoomData}'s getter
     * methods to retrieve the room type, wall and floor materials.
     *
     * @return the TARDISRoomData object
     */
    private TARDISRoomData getRoomData() {
        return roomData;
    }
}
