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
package me.eccentric_nz.TARDIS.chameleon;

import org.bukkit.block.data.BlockData;

/**
 * Data storage class for Chameleon Circuit presets. Each sub-array corresponds to a column of blocks of a 3x3x4 area +
 * the sign column.
 *
 * @author eccentric_nz
 */
public class TARDISChameleonColumn {

    private final BlockData[][] blockData;

    TARDISChameleonColumn(BlockData[][] blockData) {
        this.blockData = blockData;
    }

    public BlockData[][] getBlockData() {
        return blockData;
    }
}
