/*
 * Copyright (C) 2012 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.utility;

import com.wimbli.WorldBorder.BorderData;
import com.wimbli.WorldBorder.WorldBorder;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;

/**
 * The TARDIS grants its passengers the ability to understand and speak other
 * languages. This is due to the TARDIS's telepathic field.
 *
 * @author eccentric_nz
 */
public class TARDISWorldBorderChecker {

    private WorldBorder border;

    public TARDISWorldBorderChecker(TARDIS plugin) {
        if (plugin.borderOnServer) {
            border = (WorldBorder) plugin.getServer().getPluginManager().getPlugin("WorldBorder");
        }
    }

    /**
     * Checks to see whether the specified location is within a WorldBorder
     * border.
     *
     * @param l the location to check.
     */
    public boolean isInBorder(Location l) {
        boolean bool = true;
        if (border != null) {
            BorderData bd = border.GetWorldBorder(l.getWorld().getName());
            if (bd != null && !bd.insideBorder(l)) {
                bool = false;
            }
        }
        return bool;
    }
}
