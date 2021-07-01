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
package me.eccentric_nz.TARDIS.utility;

import com.wimbli.WorldBorder.BorderData;
import com.wimbli.WorldBorder.Config;
import com.wimbli.WorldBorder.WorldBorder;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;

/**
 * The TARDIS grants its passengers the ability to understand and speak other languages. This is due to the TARDIS's
 * telepathic field.
 *
 * @author eccentric_nz
 */
public class TARDISWorldBorderChecker {

    private final WorldBorder border;

    public TARDISWorldBorderChecker(TARDIS plugin) {
        border = (WorldBorder) plugin.getPM().getPlugin("WorldBorder");
    }

    /**
     * Checks to see whether the specified location is within a WorldBorder border.
     *
     * @param l the location to check.
     * @return true or false depending on whether the location is within the border
     */
    public boolean isInBorder(Location l) {
        boolean bool = true;
        if (border != null) {
            BorderData bd = border.getWorldBorder(l.getWorld().getName());
            if (bd != null && !bd.insideBorder(l)) {
                bool = false;
            }
        }
        return bool;
    }

    /**
     * Gets the border radius for a specified world.
     *
     * @param world the world to get the radius for
     * @return the world radius
     */
    public int[] getBorderDistance(String world) {
        int[] distance = new int[2];
        if (border != null) {
            BorderData bd = Config.Border(world);
            if (bd != null) {
                distance[0] = bd.getRadiusX();
                distance[1] = bd.getRadiusZ();
            } else {
                distance[0] = 30000;
                distance[1] = 30000;
            }
        }
        return distance;
    }
}
