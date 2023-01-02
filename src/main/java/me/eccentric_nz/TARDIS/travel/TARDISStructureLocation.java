/*
 * Copyright (C) 2023 eccentric_nz
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
        if (which.equals(Structure.END_CITY)) {
            return "End City";
        }
        if (which.equals(Structure.ANCIENT_CITY)) {
            return "Ancient City";
        }
        if (which.equals(Structure.BASTION_REMNANT)) {
            return "Bastion Remnant";
        }
        if (which.equals(Structure.FORTRESS)) {
            return "Nether Fortress";
        }
        if (which.equals(Structure.VILLAGE_PLAINS)) {
            return "Plains Village";
        }
        if (which.equals(Structure.VILLAGE_DESERT)) {
            return "Desert Village";
        }
        if (which.equals(Structure.VILLAGE_SAVANNA)) {
            return "Savanna Village";
        }
        if (which.equals(Structure.VILLAGE_SNOWY)) {
            return "Snowy Village";
        }
        if (which.equals(Structure.VILLAGE_TAIGA)) {
            return "Taiga Village";
        }
        if (which.equals(Structure.MANSION)) {
            return "Mansion";
        }
        if (which.equals(Structure.JUNGLE_PYRAMID)) {
            return "Jungle Pyramid";
        }
        if (which.equals(Structure.DESERT_PYRAMID)) {
            return "Desert Pyramid";
        }
        if (which.equals(Structure.IGLOO)) {
            return "Igloo";
        }
        if (which.equals(Structure.SWAMP_HUT)) {
            return "Swamp Hut";
        }
        return "No Structure";
    }
}
