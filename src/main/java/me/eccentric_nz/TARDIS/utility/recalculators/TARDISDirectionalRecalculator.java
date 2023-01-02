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
package me.eccentric_nz.TARDIS.utility.recalculators;

import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;

/**
 * @author eccentric_nz
 */
public class TARDISDirectionalRecalculator {

    /**
     * Recalculate the data for directional block (BUTTON) when the TARDIS preset changes direction.
     *
     * @param b the block data stored in the preset data
     * @param d the new direction of the TARDIS
     * @return the recalculated block data
     */
    public BlockData recalculate(BlockData b, COMPASS d) {
        Directional directional = (Directional) b;
        switch (d) {
            case SOUTH:
                switch (directional.getFacing()) {
                    case EAST -> directional.setFacing(BlockFace.SOUTH);
                    case WEST -> directional.setFacing(BlockFace.NORTH);
                    case SOUTH -> directional.setFacing(BlockFace.WEST);
                    case NORTH -> directional.setFacing(BlockFace.EAST);
                    default -> {
                    }
                }
                break;
            case WEST:
                switch (directional.getFacing()) {
                    case EAST -> directional.setFacing(BlockFace.WEST);
                    case WEST -> directional.setFacing(BlockFace.EAST);
                    case SOUTH -> directional.setFacing(BlockFace.NORTH);
                    case NORTH -> directional.setFacing(BlockFace.SOUTH);
                    default -> {
                    }
                }
                break;
            default:
                switch (directional.getFacing()) {
                    case EAST -> directional.setFacing(BlockFace.NORTH);
                    case WEST -> directional.setFacing(BlockFace.SOUTH);
                    case SOUTH -> directional.setFacing(BlockFace.EAST);
                    case NORTH -> directional.setFacing(BlockFace.WEST);
                    default -> {
                    }
                }
                break;
        }
        return directional;
    }
}
