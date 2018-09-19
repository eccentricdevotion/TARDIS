/*
 * Copyright (C) 2018 eccentric_nz
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

import com.google.common.collect.Sets;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;

import java.util.Set;

/**
 * @author eccentric_nz
 */
public class TARDISMushroomRecalculator {

    private final Set<BlockFace> one = Sets.newHashSet(BlockFace.UP, BlockFace.WEST, BlockFace.NORTH);
    private final Set<BlockFace> two = Sets.newHashSet(BlockFace.UP, BlockFace.NORTH);
    private final Set<BlockFace> three = Sets.newHashSet(BlockFace.UP, BlockFace.NORTH, BlockFace.EAST);
    private final Set<BlockFace> four = Sets.newHashSet(BlockFace.UP, BlockFace.WEST);
    private final Set<BlockFace> six = Sets.newHashSet(BlockFace.UP, BlockFace.EAST);
    private final Set<BlockFace> seven = Sets.newHashSet(BlockFace.UP, BlockFace.SOUTH, BlockFace.WEST);
    private final Set<BlockFace> eight = Sets.newHashSet(BlockFace.UP, BlockFace.SOUTH);
    private final Set<BlockFace> nine = Sets.newHashSet(BlockFace.UP, BlockFace.EAST, BlockFace.SOUTH);

    /**
     * Recalculate the data for multiple facing block (MUSHROOM) when the TARDIS preset changes direction.
     *
     * @param b   the block data stored in the preset data
     * @param d   the new direction of the TARDIS
     * @param col the preset column that is being calculated
     * @return the recalculated block data
     */
    public BlockData recalculate(BlockData b, COMPASS d, int col) {
        MultipleFacing mushroom = (MultipleFacing) b;
        // get all set faces
        Set<BlockFace> has = mushroom.getFaces();
        Set<BlockFace> set = null;
        switch (d) {
            case SOUTH:
                if (has.equals(one)) {
                    set = three;
                } else if (has.equals(two)) {
                    set = six;
                } else if (has.equals(three)) {
                    set = nine;
                } else if (has.equals(four)) {
                    set = two;
                } else if (has.equals(six)) {
                    set = eight;
                } else if (has.equals(seven)) {
                    set = one;
                } else if (has.equals(eight)) {
                    set = four;
                } else if (has.equals(nine)) {
                    set = seven;
                }
                break;
            case WEST:
                if (col == 3 || col == 7) {
                    if (has.equals(four)) {
                        set = six;
                    } else if (has.equals(six)) {
                        set = four;
                    }
                }
                break;
            default: // NORTH
                if (has.equals(one)) {
                    set = seven;
                } else if (has.equals(two)) {
                    set = four;
                } else if (has.equals(three)) {
                    set = one;
                } else if (has.equals(four)) {
                    set = eight;
                } else if (has.equals(six)) {
                    set = two;
                } else if (has.equals(seven)) {
                    set = nine;
                } else if (has.equals(eight)) {
                    set = six;
                } else if (has.equals(nine)) {
                    set = three;
                }
                break;
        }
        if (set != null) {
            has.forEach((f) -> mushroom.setFace(f, false));
            set.forEach((f) -> mushroom.setFace(f, true));
        }
        return mushroom;
    }
}
