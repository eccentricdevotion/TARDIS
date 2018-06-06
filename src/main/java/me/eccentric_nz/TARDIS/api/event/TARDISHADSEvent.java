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
package me.eccentric_nz.TARDIS.api.event;

import me.eccentric_nz.TARDIS.enumeration.HADS;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
public final class TARDISHADSEvent extends TARDISLocationEvent {

    private final HADS hadsType;

    public TARDISHADSEvent(Player player, int tardis_id, Location location, HADS hadsType) {
        super(player, tardis_id, location);
        this.hadsType = hadsType;
    }

    /**
     * Returns the type of HADS that was performed.
     *
     * @return the HADS type
     */
    public HADS getHADSType() {
        return hadsType;
    }
}
