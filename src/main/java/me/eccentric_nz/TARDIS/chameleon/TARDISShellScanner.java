package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;

import java.util.HashMap;

public class TARDISShellScanner {

    final static int[] eastXsouthZ = new int[]{-1, 0, 1, 1, 1, 0, -1, -1, 0, -2};
    final static int[] northXeastZ = new int[]{-1, -1, -1, 0, 1, 1, 1, 0, 0, 0};
    final static int[] westXnorthZ = new int[]{1, 0, -1, -1, -1, 0, 1, 1, 0, 2};
    final static int[] southXwestZ = new int[]{1, 1, 1, 0, -1, -1, -1, 0, 0, 0};

    public static TARDISChameleonColumn scan(TARDIS plugin, int id, PRESET preset) {
        // get tardis current location
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, where);
        if (rsc.resultSet()) {
            World w = rsc.getWorld();
            int fx = rsc.getX();
            int fy = rsc.getY();
            int fz = rsc.getZ();
            BlockData[][] shell = new BlockData[10][4];
            for (int c = 0; c < 10; c++) {
                for (int y = 0; y < 4; y++) {
                    Block block;
                    switch (rsc.getDirection()) {
                        case WEST -> block = w.getBlockAt(fx + westXnorthZ[c], fy + y, fz + southXwestZ[c]);
                        case NORTH -> block = w.getBlockAt(fx + northXeastZ[c], fy + y, fz + westXnorthZ[c]);
                        case SOUTH -> block = w.getBlockAt(fx + southXwestZ[c], fy + y, fz + eastXsouthZ[c]);
                        // EAST
                        default -> block = w.getBlockAt(fx + eastXsouthZ[c], fy + y, fz + northXeastZ[c]);
                    }
                    BlockData data = block.getBlockData();
                    if (data instanceof Directional directional) {
                        switch (rsc.getDirection()) {
                            case WEST -> {
                                // rotate 180
                                directional.setFacing(directional.getFacing().getOppositeFace());
                            }
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
                                // don nothing
                            }
                        }
                    }
                    shell[c][y] = data;
                }
            }
            return new TARDISChameleonColumn(shell);
        }
        return plugin.getPresets().getColumn(preset, COMPASS.EAST);
    }
}
