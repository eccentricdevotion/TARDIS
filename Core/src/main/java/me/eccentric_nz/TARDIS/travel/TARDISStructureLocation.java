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
package me.eccentric_nz.TARDIS.travel;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Location;
import org.bukkit.generator.structure.Structure;

public class TARDISStructureLocation {

    private Location location;
    private Structure which;

    public TARDISStructureLocation(Location location, Structure which) {
        this.location = location;
        this.which = which;
    }

    public Location getLocation() {
        return location;
    }

    public String getWhich() {
        String s = which.getKey().getKey();
        if (s.startsWith("ocean_ruin_") || s.startsWith("ruined_portal_") || s.startsWith("village_")) {
            return TARDISStringUtils.switchCapitalise(s);
        } else if (which.equals(Structure.FORTRESS)) {
            return "Nether Fortress";
        } else {
            return TARDISStringUtils.capitalise(s);
        }
    }
}
