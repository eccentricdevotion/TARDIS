package me.eccentric_nz.TARDIS.planets;

import java.util.Objects;

public class ChunkInfo {

    String world;
    int x;
    int z;

    public ChunkInfo(String world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChunkInfo chunkInfo = (ChunkInfo) o;
        return x == chunkInfo.x && z == chunkInfo.z && Objects.equals(world, chunkInfo.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, z);
    }
}
