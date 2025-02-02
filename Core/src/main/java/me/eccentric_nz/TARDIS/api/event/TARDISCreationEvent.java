/*
 * Copyright (C) 2025 eccentric_nz
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

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
public final class TARDISCreationEvent extends TARDISLocationEvent {

    /**
     * A TARDIS creation event.
     *
     * @param player    the player who created the TARDIS
     * @param tardis_id the id of the TARDIS
     * @param location  the location the TARDIS was created
     */
    public TARDISCreationEvent(Player player, int tardis_id, Location location) {
        super(player, tardis_id, location);
    }
}
