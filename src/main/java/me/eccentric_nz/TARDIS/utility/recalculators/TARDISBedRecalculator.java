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
package me.eccentric_nz.TARDIS.utility.recalculators;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;

public class TARDISBedRecalculator {

    public BlockData recalculate(BlockData data, COMPASS d) {
        Directional bed = (Directional) data;
        switch (bed.getFacing()) {
            case WEST:
                bed.setFacing(BlockFace.valueOf(TARDIS.plugin.getPresetBuilder().getOppositeFace(d).toString()));
                break;
            case EAST:
                bed.setFacing(BlockFace.valueOf(d.toString()));
                break;
            case NORTH:
                // anticlockwise 90°
                bed.setFacing(rotate90Anticlockwise(d));
                break;
            default:
                // clockwise 90°
                bed.setFacing(rotate90Clockwise(d));
                break;
        }
        return bed;
    }

    private BlockFace rotate90Clockwise(COMPASS d) {
        switch (d) {
            case SOUTH:
                return BlockFace.WEST;
            case WEST:
                return BlockFace.NORTH;
            case NORTH:
                return BlockFace.EAST;
            default:
                return BlockFace.SOUTH;
        }
    }

    private BlockFace rotate90Anticlockwise(COMPASS d) {
        switch (d) {
            case SOUTH:
                return BlockFace.EAST;
            case WEST:
                return BlockFace.SOUTH;
            case NORTH:
                return BlockFace.WEST;
            default:
                return BlockFace.NORTH;
        }
    }
}
