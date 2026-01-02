/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.sensor;

import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class SensorToggle {

    public Block getBlock(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        Location location = TARDISStaticLocationGetters.getLocationFromDB(str);
        if (location != null) {
            return location.getBlock();
        }
        return null;
    }

    public void setState(Block block) {
        if (block.getType() == Material.REDSTONE_BLOCK) {
            block.setType(Material.STONE);
        } else {
            block.setType(Material.REDSTONE_BLOCK);
        }
    }
}
