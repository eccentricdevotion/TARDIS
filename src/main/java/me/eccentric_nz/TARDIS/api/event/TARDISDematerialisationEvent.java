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

import me.eccentric_nz.tardis.database.data.TARDIS;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
public final class TARDISDematerialisationEvent extends TARDISEvent {

    private final Location location;

    public TARDISDematerialisationEvent(Player player, TARDIS tardis, Location location) {
        super(player, tardis);
        this.location = location;
    }

    /**
     * Returns the Bukkit Location of the tardis exterior preset where it dematerialise from.
     *
     * @return the location of the dematerialisation event
     */
    public Location getLocation() {
        return location;
    }
}
