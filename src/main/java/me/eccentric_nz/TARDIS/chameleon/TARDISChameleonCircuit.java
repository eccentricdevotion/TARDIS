/*
 * Copyright (C) 2013 eccentric_nz
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

import java.util.Arrays;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 * The Chameleon Circuit controls the exterior shell's Cloaking Device. The
 * Cloaking Device allows the shape, color, mass and texture of the shell to be
 * altered to blend in with its surroundings. This feature is designed to help
 * prevent changes to history.
 *
 * @author eccentric_nz
 */
public class TARDISChameleonCircuit {

    private final TARDIS plugin;

    public TARDISChameleonCircuit(TARDIS plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    public int[] getChameleonBlock(Block b, Player p, boolean short_out) {
        int[] data = new int[2];
        int chameleonType = b.getTypeId();
        int wall_block = 35;
        byte chameleonData = 11;
        // determine wall_block
        if (plugin.getBlocksConfig().getIntegerList("chameleon_blocks").contains((Integer) chameleonType)) {
            wall_block = chameleonType;
            chameleonData = b.getData();
        }
        if (TARDISConstants.CHAMELEON_BLOCKS_BAD.contains((Integer) chameleonType)) {
            String message = (short_out) ? "You cannot short out the Chameleon Circuit with that kind of block!" : "Bummer, the TARDIS could not engage the Chameleon Circuit!";
            p.sendMessage(plugin.pluginName + message);
        }
        if (TARDISConstants.CHAMELEON_BLOCKS_CHANGE.contains((Integer) chameleonType)) {
            wall_block = swapId(chameleonType);
            switch (chameleonType) {
                case 22:
                    chameleonData = 11;
                    break;
                case 41:
                    chameleonData = 4;
                    break;
                case 42:
                    chameleonData = 8;
                    break;
                case 57:
                    chameleonData = 3;
                    break;
                case 133:
                    chameleonData = 5;
                    break;
                case 134:
                    chameleonData = 1;
                    break;
                case 135:
                    chameleonData = 2;
                    break;
                case 136:
                    chameleonData = 3;
                    break;
                default:
                    chameleonData = b.getData();
                    break;
            }
        }
        if (TARDISConstants.CHAMELEON_BLOCKS_NEXT.contains((Integer) chameleonType)) {
            List<BlockFace> surrounding = Arrays.asList(new BlockFace[]{BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST});
            // try the surrounding blocks
            for (BlockFace bf : surrounding) {
                Block surroundblock = b.getRelative(bf);
                int eid = surroundblock.getTypeId();
                if (TARDISConstants.CHAMELEON_BLOCKS_VALID.contains((Integer) eid)) {
                    wall_block = eid;
                    chameleonData = surroundblock.getData();
                    break;
                }
                if (TARDISConstants.CHAMELEON_BLOCKS_CHANGE.contains((Integer) eid)) {
                    wall_block = swapId(eid);
                    switch (eid) {
                        case 134:
                            chameleonData = 1;
                            break;
                        case 135:
                            chameleonData = 2;
                            break;
                        case 136:
                            chameleonData = 3;
                            break;
                        default:
                            chameleonData = b.getData();
                            break;
                    }
                    break;
                }
            }
        }
        // if it's a precious block or TNT and all_blocks is false, then switch it to wool of similar colour
        if (TARDISConstants.CHAMELEON_BLOCKS_PRECIOUS.contains((Integer) chameleonType) && !plugin.getConfig().getBoolean("allow.all_blocks")) {
            wall_block = 35;
            switch (chameleonType) {
                case 41:
                    chameleonData = (byte) 8;
                    break;
                case 42:
                    chameleonData = (byte) 4;
                    break;
                case 46:
                    chameleonData = (byte) 14;
                    break;
                case 57:
                    chameleonData = (byte) 3;
                    break;
                case 133:
                    chameleonData = (byte) 5;
                    break;
                case 152:
                    chameleonData = (byte) 14;
                    break;
                case 173:
                    chameleonData = (byte) 15;
                    break;
                default:
                    chameleonData = (byte) 0;
                    break;
            }
        }
        data[0] = wall_block;
        data[1] = chameleonData;
        return data;
    }

    public int swapId(int id) {
        int swappedId = TARDISConstants.CHAMELEON_BLOCKS_CHANGE_HASH.get(id);
        return swappedId;
    }
}
