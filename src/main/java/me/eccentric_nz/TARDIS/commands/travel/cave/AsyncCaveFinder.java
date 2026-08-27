package me.eccentric_nz.TARDIS.commands.travel.cave;

import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class AsyncCaveFinder {

    private static final int WIDTH = 3;
    private static final int HEIGHT = 4;
    private static final int MAX_ATTEMPTS = 15;

    public static CompletableFuture<Location> getSafeCave(Location center, int maxRadiusChunks, COMPASS direction) {
        return findCaveIterative(center, maxRadiusChunks, direction, 0);
    }

    private static CompletableFuture<Location> findCaveIterative(Location location, int chunkRadius, COMPASS direction, int attempt) {
        World world = location.getWorld();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int randomX = location.getChunk().getX() + random.nextInt(-chunkRadius, chunkRadius + 1);
        int randomZ = location.getChunk().getZ() + random.nextInt(-chunkRadius, chunkRadius + 1);
        // fetch chunk asynchronously
        return world.getChunkAtAsync(randomX, randomZ).thenApply(chunk -> {
            // capture lightweight chunk snapshot on the main thread (takes microsecond-level time)
            return chunk.getChunkSnapshot(true, false, false);
        }).thenApplyAsync(snapshot -> {
            // scan the snapshot async
            return scanSnapshotForCave(world, snapshot, direction);
        }).thenCompose(optionalLoc -> {
            if (optionalLoc != null) {
                return CompletableFuture.completedFuture(optionalLoc);
            }
            // limit attempts to avoid infinite loops if no cave exists in the radius
            if (attempt >= MAX_ATTEMPTS) {
                return CompletableFuture.completedFuture(null);
            }
            // retry with a new random chunk iteration
            return findCaveIterative(location, chunkRadius, direction, attempt + 1);
        });
    }

    private static Location scanSnapshotForCave(World world, ChunkSnapshot snapshot, COMPASS direction) {
        int chunkWorldX = snapshot.getX() << 4;
        int chunkWorldZ = snapshot.getZ() << 4;
        int minY = world.getMinHeight() + 5;
        int maxY = 50;
        // step through the chunk by footprint size
        for (int y = maxY; y >= minY; y -= HEIGHT) {
            for (int x = 0; x <= 16 - WIDTH; x += WIDTH) {
                for (int z = 0; z <= 16 - WIDTH; z += WIDTH) {
                    if (isSafe3x4x3Space(snapshot, x, y, z, direction)) {
                        // return center base location of the 3x3 footprint
                        return new Location(world, chunkWorldX + x + 1, y, chunkWorldZ + z + 1);
                    }
                }
            }
        }
        return null;
    }

    private static boolean isSafe3x4x3Space(ChunkSnapshot snapshot, int relativeX, int startY, int relativeZ, COMPASS direction) {
        // ensure blocks under footprint are non-lava solid ground or air
        for (int x = 0; x < WIDTH; x++) {
            for (int z = 0; z < WIDTH; z++) {
                Material floorType = snapshot.getBlockType(relativeX + x, startY - 1, relativeZ + z);
                if (!Tag.BASE_STONE_OVERWORLD.isTagged(floorType) || floorType.isAir()) {
                    return false;
                }
            }
        }
        // air clearance check: 3x4x3 box must be air
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                for (int z = 0; z < WIDTH; z++) {
                    Material type = snapshot.getBlockType(relativeX + x, startY + y, relativeZ + z);
                    if (!type.isAir()) {
                        return false;
                    }
                }
            }
        }
        // exit location
        switch (direction) {
            case NORTH -> {
                if (snapshot.getBlockType(relativeX, startY, relativeZ + 3).isAir()
                        && snapshot.getBlockType(relativeX + 1, startY, relativeZ + 3).isAir()
                        && snapshot.getBlockType(relativeX + 2, startY, relativeZ + 3).isAir()) {
                    return true;
                }
            }
            case WEST -> {
                if (snapshot.getBlockType(relativeX + 3, startY, relativeZ).isAir()
                        && snapshot.getBlockType(relativeX + 3, startY, relativeZ + 1).isAir()
                        && snapshot.getBlockType(relativeX + 3, startY, relativeZ + 2).isAir()) {
                    return true;
                }
            }
            case SOUTH -> {
                if (snapshot.getBlockType(relativeX, startY, relativeZ - 1).isAir()
                        && snapshot.getBlockType(relativeX + 1, startY, relativeZ - 1).isAir()
                        && snapshot.getBlockType(relativeX + 2, startY, relativeZ - 1).isAir()) {
                    return true;
                }
            }
            default -> {
                if (snapshot.getBlockType(relativeX - 1, startY, relativeZ).isAir()
                        && snapshot.getBlockType(relativeX - 1, startY, relativeZ + 1).isAir()
                        && snapshot.getBlockType(relativeX - 1, startY, relativeZ + 2).isAir()) {
                    return true;
                }
            }
        }
        return true;
    }
}

