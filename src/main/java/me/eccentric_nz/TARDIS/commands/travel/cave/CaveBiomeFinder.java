package me.eccentric_nz.TARDIS.commands.travel.cave;

import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class CaveBiomeFinder {

    /**
     * Asynchronously searches for a safe underground cave location in a target biome.
     *
     * @return A CompletableFuture containing an Optional Location.
     *         The future completes on an async worker pool thread.
     */
    public static CompletableFuture<Optional<Location>> getSafeLocation(Location biomeLoc, COMPASS direction) {
        World world = biomeLoc.getWorld();
        if (world == null) {
            return CompletableFuture.completedFuture(Optional.empty());
        }
        int chunkX = biomeLoc.getBlockX() >> 4;
        int chunkZ = biomeLoc.getBlockZ() >> 4;
        // asynchronously load/generate the target chunk
        return world.getChunkAtAsync(chunkX, chunkZ, true, true).thenCompose(chunk -> {
            // take a thread-safe snapshot to isolate block reading from the main thread
            ChunkSnapshot snapshot = chunk.getChunkSnapshot(true, false, false);
            // scan the Y-axis on an asynchronous worker pool thread
            return CompletableFuture.supplyAsync(() -> {
                Location loc = AsyncCaveFinder.scanSnapshotForCave(world, snapshot, direction);
                if (loc == null) {
                    return Optional.<Location>empty();
                }
                return Optional.of(loc);
            });
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return Optional.empty();
        });
    }
}

