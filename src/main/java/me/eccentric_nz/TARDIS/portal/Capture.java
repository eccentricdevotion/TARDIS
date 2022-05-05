package me.eccentric_nz.TARDIS.portal;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;

public class Capture {

    /**
     * This function captures the interior of a TARDIS, and returns a 3D array of BlockData objects
     *
     * @param location The location of the door
     * @param distance The distance from the door
     * @return A 3D array of BlockData objects
     */
    public BlockData[][][] captureInterior(Location location, int distance) {
        int iy, ix, iz;
        if (distance > 6) {
            iy = 3;
            ix = 3;
            iz = 3;
        } else if (distance > 1) {
            iy = 3;
            ix = 5;
            iz = 5;
        } else {
            iy = 6;
            ix = 9;
            iz = 9;
        }
        // make block array
        BlockData[][][] capture = new BlockData[iy][ix][iz];
        // adjust start position from door position
        int bx = -ix / 2;
        int bz = 1;
        World world = location.getWorld();
        // get start positions
        int startX = location.getBlockX() + bx;
        int startY = location.getBlockY() - 1;
        int startZ = location.getBlockZ() + bz;
        // capture blocks
        for (int y = 0; y < iy; y++) {
            for (int x = 0; x < ix; x++) {
                for (int z = 0; z < iz; z++) {
                    int xx = startX + x;
                    int yy = startY + y;
                    int zz = startZ + z;
                    // get the blockdata
                    capture[y][x][z] = world.getBlockAt(xx, yy, zz).getBlockData();
                }
            }
        }
        // TODO add a box / view limiter
        return capture;
    }

    // TODO capture item frame time rotor
}
