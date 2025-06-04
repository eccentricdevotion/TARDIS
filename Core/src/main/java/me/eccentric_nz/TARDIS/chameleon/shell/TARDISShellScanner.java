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
package me.eccentric_nz.TARDIS.chameleon.shell;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISChameleonColumn;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;

public class TARDISShellScanner {

    final static int[] eastXsouthZ = new int[]{-1, 0, 1, 1, 1, 0, -1, -1, 0, -2};
    final static int[] northXeastZ = new int[]{-1, -1, -1, 0, 1, 1, 1, 0, 0, 0};
    final static int[] westXnorthZ = new int[]{1, 0, -1, -1, -1, 0, 1, 1, 0, 2};
    final static int[] southXwestZ = new int[]{1, 1, 1, 0, -1, -1, -1, 0, 0, 0};

    public static TARDISChameleonColumn scan(TARDIS plugin, int id, ChameleonPreset preset) {
        // get tardis current location
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        if (!rsc.resultSet()) {
            return plugin.getPresets().getColumn(preset, COMPASS.EAST);
        }
        Current current = rsc.getCurrent();
        World w = current.location().getWorld();
        int fx = current.location().getBlockX();
        int fy = current.location().getBlockY();
        int fz = current.location().getBlockZ();
        BlockData[][] shell = new BlockData[10][4];
        for (int c = 0; c < 10; c++) {
            for (int y = 0; y < 4; y++) {
                Block block;
                switch (current.direction()) {
                    case WEST -> block = w.getBlockAt(fx + westXnorthZ[c], fy + y, fz + southXwestZ[c]);
                    case NORTH -> block = w.getBlockAt(fx + northXeastZ[c], fy + y, fz + westXnorthZ[c]);
                    case SOUTH -> block = w.getBlockAt(fx + southXwestZ[c], fy + y, fz + eastXsouthZ[c]);
                    // EAST
                    default -> block = w.getBlockAt(fx + eastXsouthZ[c], fy + y, fz + northXeastZ[c]);
                }
                BlockData data = block.getBlockData();
                if (data instanceof Directional directional) {
                    switch (current.direction()) {
                        case WEST -> directional.setFacing(directional.getFacing().getOppositeFace()); // rotate 180
                        case NORTH -> {
                            // clockwise
                            BlockFace face;
                            switch (directional.getFacing()) {
                                case EAST -> face = BlockFace.NORTH;
                                case SOUTH -> face = BlockFace.EAST;
                                case WEST -> face = BlockFace.SOUTH;
                                // north
                                default -> face = BlockFace.WEST;
                            }
                            directional.setFacing(face);
                        }
                        case SOUTH -> {
                            // anti-clockwise
                            BlockFace face;
                            switch (directional.getFacing()) {
                                case EAST -> face = BlockFace.SOUTH;
                                case SOUTH -> face = BlockFace.WEST;
                                case WEST -> face = BlockFace.NORTH;
                                // north
                                default -> face = BlockFace.EAST;
                            }
                            directional.setFacing(face);
                        }
                        default -> {
                            // do nothing
                        }
                    }
                }
                shell[c][y] = data;
            }
        }
        return new TARDISChameleonColumn(shell);
    }
}
