package me.eccentric_nz.TARDIS.discovery;

import java.util.Set;
import java.util.UUID;

public record StructureData(UUID id, String type, String world, int originChunkX, int originChunkZ, int y, Set<Long> occupiedChunks) {
}
