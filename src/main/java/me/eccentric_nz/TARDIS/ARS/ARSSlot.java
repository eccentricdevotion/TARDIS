
package me.eccentric_nz.TARDIS.ARS;

import org.bukkit.Chunk;

public class ARSSlot {

    public int getChunkX(int xx, Chunk c) {
        int cx = c.getX();
        int cz = c.getZ();
        return switch (xx) {
            case 0 -> c.getWorld().getChunkAt(cx - 4, cz).getBlock(0, 64, 0).getX();
            case 1 -> c.getWorld().getChunkAt(cx - 3, cz).getBlock(0, 64, 0).getX();
            case 2 -> c.getWorld().getChunkAt(cx - 2, cz).getBlock(0, 64, 0).getX();
            case 3 -> c.getWorld().getChunkAt(cx - 1, cz).getBlock(0, 64, 0).getX();
            case 5 -> c.getWorld().getChunkAt(cx + 1, cz).getBlock(0, 64, 0).getX();
            case 6 -> c.getWorld().getChunkAt(cx + 2, cz).getBlock(0, 64, 0).getX();
            case 7 -> c.getWorld().getChunkAt(cx + 3, cz).getBlock(0, 64, 0).getX();
            case 8 -> c.getWorld().getChunkAt(cx + 4, cz).getBlock(0, 64, 0).getX();
            default -> c.getBlock(0, 64, 0).getX();
        };
    }

    public int getChunkZ(int zz, Chunk c) {
        int cx = c.getX();
        int cz = c.getZ();
        return switch (zz) {
            case 0 -> c.getWorld().getChunkAt(cx, cz - 4).getBlock(0, 64, 0).getZ();
            case 1 -> c.getWorld().getChunkAt(cx, cz - 3).getBlock(0, 64, 0).getZ();
            case 2 -> c.getWorld().getChunkAt(cx, cz - 2).getBlock(0, 64, 0).getZ();
            case 3 -> c.getWorld().getChunkAt(cx, cz - 1).getBlock(0, 64, 0).getZ();
            case 5 -> c.getWorld().getChunkAt(cx, cz + 1).getBlock(0, 64, 0).getZ();
            case 6 -> c.getWorld().getChunkAt(cx, cz + 2).getBlock(0, 64, 0).getZ();
            case 7 -> c.getWorld().getChunkAt(cx, cz + 3).getBlock(0, 64, 0).getZ();
            case 8 -> c.getWorld().getChunkAt(cx, cz + 4).getBlock(0, 64, 0).getZ();
            default -> c.getBlock(0, 64, 0).getZ();
        };
    }

    public int getChunkY(int yy) {
        return switch (yy) {
            case -1 -> 32;
            case 0 -> 48;
            case 2 -> 80;
            case 3 -> 96;
            default -> 64;
        };
    }
}
