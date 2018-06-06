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

import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;

/**
 * @author eccentric_nz
 */
public class TARDISTorchRecalculator {

    /**
     * Recalculate the data for directional block (TRAPDOOR) when the TARDIS preset changes direction.
     *
     * @param b the block data stored in the preset data
     * @param d the new direction of the TARDIS
     * @return the recalculated block data
     */
    public BlockData recalculate(BlockData b, COMPASS d) {
        Directional torch = (Directional) b;
        switch (d) {
            case SOUTH:
                switch (torch.getFacing()) {
                    case EAST: // 1
                        torch.setFacing(BlockFace.SOUTH); // 3
                        break;
                    case WEST:
                        torch.setFacing(BlockFace.NORTH); // 4
                        break;
                    case SOUTH:
                        torch.setFacing(BlockFace.WEST); // 2
                        break;
                    case NORTH:
                        torch.setFacing(BlockFace.EAST); // 1
                        break;
                    default:
                        break;
                }
                break;
            case WEST:
                switch (torch.getFacing()) {
                    case EAST: // 1
                        torch.setFacing(BlockFace.WEST); // 2
                        break;
                    case WEST:
                        torch.setFacing(BlockFace.EAST); // 1
                        break;
                    case SOUTH:
                        torch.setFacing(BlockFace.NORTH); // 4
                        break;
                    case NORTH:
                        torch.setFacing(BlockFace.SOUTH); // 3
                        break;
                    default:
                        break;
                }
                break;
            default:
                switch (torch.getFacing()) {
                    case EAST: // 1
                        torch.setFacing(BlockFace.NORTH); // 4
                        break;
                    case WEST:
                        torch.setFacing(BlockFace.SOUTH); // 3
                        break;
                    case SOUTH:
                        torch.setFacing(BlockFace.EAST); // 1
                        break;
                    case NORTH:
                        torch.setFacing(BlockFace.WEST); // 2
                        break;
                    default:
                        break;
                }
        }
        return torch;
    }

}
