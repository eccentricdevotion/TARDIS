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
import org.bukkit.block.data.MultipleFacing;

import java.util.HashSet;
import java.util.Set;

/**
 * @author eccentric_nz
 */
public class TARDISMultipleFacingRecalculator {

    /**
     * Recalculate the data for multiple facing block (FENCE, VINE, MUSHROOM_BLOCK) when the TARDIS preset changes direction.
     *
     * @param b the block data stored in the preset data
     * @param d the new direction of the TARDIS
     * @return the recalculated block data
     */
    public BlockData recalculate(BlockData b, COMPASS d) {
        MultipleFacing facing = (MultipleFacing) b;
        // get all set faces
        Set<BlockFace> has = facing.getFaces();
        Set<BlockFace> set = null;
        switch (has.size()) {
            case 1, 2, 3 -> {
                set = new HashSet<>();
                for (BlockFace face : has) {
                    switch (d) {
                        case NORTH -> {
                            switch (face) {
                                case NORTH -> set.add(BlockFace.WEST);
                                case EAST -> set.add(BlockFace.NORTH);
                                case SOUTH -> set.add(BlockFace.EAST);
                                // WEST
                                default -> set.add(BlockFace.SOUTH);
                            }
                        }
                        case WEST -> set.add(face.getOppositeFace());
                        case SOUTH -> {
                            switch (face) {
                                case NORTH -> set.add(BlockFace.EAST);
                                case EAST -> set.add(BlockFace.SOUTH);
                                case SOUTH -> set.add(BlockFace.WEST);
                                // WEST
                                default -> set.add(BlockFace.NORTH);
                            }
                        }
                        default -> {
                            // do nothing as this is the saved block data
                        }
                    }
                }
            }
            default -> {
                // 0, 4 faces
                // do nothing as rotation doesn't change shape
            }
        }
        if (set != null && set.size() != 0) {
            has.forEach((f) -> facing.setFace(f, false));
            set.forEach((f) -> facing.setFace(f, true));
        }
        return facing;
    }
}
