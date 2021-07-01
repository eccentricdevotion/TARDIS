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
package me.eccentric_nz.tardis.api.event;

import me.eccentric_nz.tardis.ars.TardisArsSlot;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.rooms.TardisRoomData;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
public final class TardisRoomGrowEvent extends TardisEvent {

    private final TardisArsSlot slot;
    private final TardisRoomData roomData;

    /**
     * A room growing event.
     *
     * @param player   the player growing the room
     * @param tardis   the Tardis data object, may be null - if room was manually grown, use {@link
     *                 #getRoomData()}.getTardisId()
     * @param slot     the {@link TardisArsSlot} data object, may be null - if the room was manually grown, use {@link
     *                 #getRoomData()}.getLocation()
     * @param roomData the {@link TardisRoomData} data object
     */
    public TardisRoomGrowEvent(Player player, Tardis tardis, TardisArsSlot slot, TardisRoomData roomData) {
        super(player, tardis);
        this.slot = slot;
        this.roomData = roomData;
    }

    /**
     * Returns the Architectural Reconfiguration System position data object. Use the {@link
     * TardisArsSlot}'s getter methods to retrieve the coordinates.
     *
     * @return the {@link TardisArsSlot} object or null if the room was manually grown
     */
    public TardisArsSlot getSlot() {
        return slot;
    }

    /**
     * Returns the room data object. Use the {@link TardisRoomData TARDISRoomData}'s getter
     * methods to retrieve the room type, wall and floor materials.
     *
     * @return the TARDISRoomData object
     */
    private TardisRoomData getRoomData() {
        return roomData;
    }
}
