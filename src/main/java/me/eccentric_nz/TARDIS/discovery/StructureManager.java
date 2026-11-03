package me.eccentric_nz.TARDIS.discovery;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class StructureManager {

    private final Plugin plugin;
    private final StructureRegistry registry;
    private final Set<Long> generating = ConcurrentHashMap.newKeySet();

    public StructureManager(Plugin plugin, StructureRegistry registry) {
        this.plugin = plugin;
        this.registry = registry;
    }

    public void tryGenerate(Chunk origin, String type, int widthBlocks, int lengthBlocks) {
        int chunkX = origin.getX();
        int chunkZ = origin.getZ();
        long originKey = ChunkKey.of(chunkX, chunkZ);
        if (!generating.add(originKey)) {
            return;
        }
        String world = origin.getWorld().getKey().asString();
        if (registry.isOccupied(world, chunkX, chunkZ)) {
            generating.remove(originKey);
            return;
        }
        Set<Long> occupied = computeFootprint(chunkX, chunkZ, widthBlocks, lengthBlocks);
        for (long chunk : occupied) {
            if (registry.isOccupied(world, ChunkKey.x(chunk), ChunkKey.z(chunk))) {
                generating.remove(originKey);
                return;
            }
        }
        loadRequiredChunks(origin.getWorld(), occupied).thenRun(() -> plugin.getServer().getScheduler().runTask(plugin, () -> {
            try {
                int y;
                switch (origin.getWorld().getEnvironment()) {
                    case NETHER -> y = origin.getWorld().getMinHeight() + 8 + TARDISConstants.RANDOM.nextInt(32);
                    case THE_END -> y = origin.getWorld().getMinHeight() + 32 + TARDISConstants.RANDOM.nextInt(128);
                    default -> y = origin.getWorld().getMinHeight() + 24 + TARDISConstants.RANDOM.nextInt(64);
                }
                new Temple(TARDIS.plugin).build(origin.getBlock(0, y, 0).getLocation());
                StructureData structure = new StructureData(UUID.randomUUID(), type, origin.getWorld().getKey().asString(), chunkX, chunkZ, y, occupied);
                registry.register(structure);
            } finally {
                generating.remove(originKey);
            }
        }));
    }

    private CompletableFuture<Void> loadRequiredChunks(World world, Set<Long> chunks) {
        CompletableFuture<?>[] futures = chunks.stream().map(chunk -> world.getChunkAtAsync(ChunkKey.x(chunk), ChunkKey.z(chunk))).toArray(CompletableFuture[]::new);
        return CompletableFuture.allOf(futures);
    }

    public static Set<Long> computeFootprint(int originChunkX, int originChunkZ, int widthBlocks, int lengthBlocks) {
        int widthChunks = (widthBlocks + 15) >> 4;
        int lengthChunks = (lengthBlocks + 15) >> 4;
        Set<Long> chunks = new HashSet<>();
        for (int dx = 0; dx < widthChunks; dx++) {
            for (int dz = 0; dz < lengthChunks; dz++) {
                chunks.add(ChunkKey.of(originChunkX + dx, originChunkZ + dz));
            }
        }
        return chunks;
    }
}