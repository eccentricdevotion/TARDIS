/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.tardishelper.TARDISWorldBorder;
import org.bukkit.Location;

/**
 * The TARDIS grants its passengers the ability to understand and speak other
 * languages. This is due to the TARDIS's telepathic field.
 *
 * @author eccentric_nz
 */
public class TARDISVanillaBorderChecker {

    /**
     * Checks to see whether the specified location is within a WorldBorder
     * border.
     *
     * @param border the vanilla world border data to check against
     * @param l the location to check
     * @return true or false depending on whether the location is within the
     * border
     */
    public static boolean isInBorder(TARDISWorldBorder border, Location l) {
        long size = border.getSize() / 2;
        // default world border is 60,000,000 blocks wide (in other words not set at all)
        if (size > 29999999) {
            return true;
        }
        double minX = border.getCentreX() - size;
        double minZ = border.getCentreZ() - size;
        double maxX = border.getCentreX() + size;
        double maxZ = border.getCentreZ() + size;
        double x = l.getX();
        double z = l.getZ();
        return (x > minX && x < maxX && z > minZ && z < maxZ);
    }
}
