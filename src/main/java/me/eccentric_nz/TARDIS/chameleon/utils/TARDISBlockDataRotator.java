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
package me.eccentric_nz.TARDIS.chameleon.utils;

import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.structure.StructureRotation;

/**
 *
 * @author eccentric_nz
 */
public class TARDISBlockDataRotator {

    /**
     * Rotate Chameleon preset BlockData for the different directions TARDIS
     * exteriors can face. For block presets the valid values will only ever be
     * NORTH, WEST and SOUTH.
     *
     * @param data the block data to rotate
     * @param direction the direction to rotate to
     * @return the rotated block data
     */
    public static BlockData rotate(BlockData data, COMPASS direction) {
        switch (direction) {
            case NORTH -> data.rotate(StructureRotation.COUNTERCLOCKWISE_90);
            case WEST -> data.rotate(StructureRotation.CLOCKWISE_180);
            default -> data.rotate(StructureRotation.CLOCKWISE_90); // SOUTH
        }
        return data;
    }
}
