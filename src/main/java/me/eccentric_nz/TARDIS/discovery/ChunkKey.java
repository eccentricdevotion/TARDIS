package me.eccentric_nz.TARDIS.discovery;

import net.minecraft.world.level.ChunkPos;

public final class ChunkKey {

    public static long of(int chunkX, int chunkZ) {
        return ChunkPos.pack(chunkX, chunkZ);
    }

    public static int x(long key) {
        return ChunkPos.getX(key);
    }

    public static int z(long key) {
        return ChunkPos.getZ(key);
    }
}