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
package me.eccentric_nz.TARDIS.destroyers;

import me.eccentric_nz.TARDIS.builders.exterior.MaterialisationData;
import org.bukkit.Location;

/**
 * Data class for building the TARDIS exterior.
 *
 * @author eccentric_nz
 */
public final class DestroyData extends MaterialisationData {

    private boolean hide;
    private Location fromToLocation;

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    Location getFromToLocation() {
        return fromToLocation;
    }

    public void setFromToLocation(Location fromToLocation) {
        this.fromToLocation = fromToLocation;
    }
}
