package me.eccentric_nz.TARDIS.commands.travel.structure;

import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class AsyncStructureFinder {
    
    /**
     * Finds the closest location that also fits the TARDIS police box.
     *
     * @param location  The location returned by World#locateNearestStructure
     * @param direction The compass direction of the police box
     * @param material  The block type to search for
     * @return A CompletableFuture containing the final safe Location, or empty if none found
     */
    @SuppressWarnings("unchecked")
    public static CompletableFuture<Optional<Location>> getSafeLocation(Location location, COMPASS direction, Material material, int minY, int maxY) {
        BlockData brick = material.createBlockData();
        World world = location.getWorld();
        int originChunkX = location.getBlockX() >> 4;
        int originChunkZ = location.getBlockZ() >> 4;
        // get a 3x3 chunk grid around the structure location
        CompletableFuture<Chunk>[] chunkFutures = new CompletableFuture[9];
        int index = 0;
        for (int cx = originChunkX - 1; cx <= originChunkX + 1; cx++) {
            for (int cz = originChunkZ - 1; cz <= originChunkZ + 1; cz++) {
                chunkFutures[index++] = world.getChunkAtAsync(cx, cz);
            }
        }
        return CompletableFuture.allOf(chunkFutures).thenApply(v -> {
            ChunkSnapshot[][] snapshotGrid = new ChunkSnapshot[3][3];
            for (CompletableFuture<Chunk> future : chunkFutures) {
                Chunk chunk = future.join();
                int gridX = chunk.getX() - (originChunkX - 1);
                int gridZ = chunk.getZ() - (originChunkZ - 1);
                snapshotGrid[gridX][gridZ] = chunk.getChunkSnapshot();
            }
            return snapshotGrid;
        }).thenApplyAsync(snapshotGrid -> {
            int minChunkX = originChunkX - 1;
            int minChunkZ = originChunkZ - 1;
            int activeChunkX = originChunkX;
            int activeChunkZ = originChunkZ;
            // does the center chunk actually have the target brick?
            ChunkSnapshot centerSnap = snapshotGrid[originChunkX - minChunkX][originChunkZ - minChunkZ];
            if (!centerSnap.contains(brick)) {
                boolean foundAlternative = false;
                // center chunk is empty! scan adjacent chunks in our preloaded 3x3 grid to shift focal point
                for (int gx = 0; gx < 3; gx++) {
                    for (int gz = 0; gz < 3; gz++) {
                        if (snapshotGrid[gx][gz].contains(brick)) {
                            activeChunkX = minChunkX + gx;
                            activeChunkZ = minChunkZ + gz;
                            foundAlternative = true;
                            break;
                        }
                    }
                    if (foundAlternative) {
                        break;
                    }
                }
                // none of the 9 loaded chunks contain the block, abort early to save CPU cycles
                if (!foundAlternative) {
                    return Optional.empty();
                }
            }
            // set up the spiral math to spin outwards from the valid chunk's center block coordinates
            int chunkCenterWorldX = (activeChunkX << 4) + 8;
            int chunkCenterWorldZ = (activeChunkZ << 4) + 8;
            int x = 0;
            int z = 0;
            int dx = 0;
            int dz = -1;
            for (int i = 0; i < 225; i++) {
                int targetX = chunkCenterWorldX + x;
                int targetZ = chunkCenterWorldZ + z;
                // safely grab the block data from whichever chunk snapshot the spiral lands on
                int currentChunkX = targetX >> 4;
                int currentChunkZ = targetZ >> 4;
                ChunkSnapshot currentSnapshot = snapshotGrid[currentChunkX - minChunkX][currentChunkZ - minChunkZ];
                for (int y = minY; y < maxY; y++) {
                    BlockData ground = currentSnapshot.getBlockData(targetX & 15, y, targetZ & 15);
                    // found a matching block
                    if (ground.equals(brick)) {
                        // check space starting exactly at foot level
                        int checkY = y + 1;
                        // run police box footprint rules cleanly via the snapshots
                        if (isAirAt(snapshotGrid, minChunkX, minChunkZ, targetX, checkY, targetZ)) {
                            // check there is a 3x3 room envelope for the physical box layout
                            if (isAirAt(snapshotGrid, minChunkX, minChunkZ, targetX - 1, checkY, targetZ - 1)
                                    && isAirAt(snapshotGrid, minChunkX, minChunkZ, targetX - 1, checkY, targetZ)
                                    && isAirAt(snapshotGrid, minChunkX, minChunkZ, targetX - 1, checkY, targetZ + 1)
                                    && isAirAt(snapshotGrid, minChunkX, minChunkZ, targetX, checkY, targetZ - 1)
                                    && isAirAt(snapshotGrid, minChunkX, minChunkZ, targetX, checkY, targetZ + 1)
                                    && isAirAt(snapshotGrid, minChunkX, minChunkZ, targetX + 1, checkY, targetZ - 1)
                                    && isAirAt(snapshotGrid, minChunkX, minChunkZ, targetX + 1, checkY, targetZ)
                                    && isAirAt(snapshotGrid, minChunkX, minChunkZ, targetX + 1, checkY, targetZ + 1)) {
                                // verify exit clearance
                                boolean safeExit = false;
                                switch (direction) {
                                    case NORTH -> {
                                        if (isAirAt(snapshotGrid, minChunkX, minChunkZ, targetX - 1, checkY, targetZ + 2)
                                                && isAirAt(snapshotGrid, minChunkX, minChunkZ, targetX, checkY, targetZ + 2)
                                                && isAirAt(snapshotGrid, minChunkX, minChunkZ, targetX + 1, checkY, targetZ + 2)) {
                                            safeExit = true;
                                        }
                                    }
                                    case WEST -> {
                                        if (isAirAt(snapshotGrid, minChunkX, minChunkZ, targetX + 2, checkY, targetZ - 1)
                                                && isAirAt(snapshotGrid, minChunkX, minChunkZ, targetX + 2, checkY, targetZ)
                                                && isAirAt(snapshotGrid, minChunkX, minChunkZ, targetX + 2, checkY, targetZ + 1)) {
                                            safeExit = true;
                                        }
                                    }
                                    case SOUTH -> {
                                        if (isAirAt(snapshotGrid, minChunkX, minChunkZ, targetX - 1, checkY, targetZ - 2)
                                                && isAirAt(snapshotGrid, minChunkX, minChunkZ, targetX, checkY, targetZ - 2)
                                                && isAirAt(snapshotGrid, minChunkX, minChunkZ, targetX + 1, checkY, targetZ - 2)) {
                                            safeExit = true;
                                        }
                                    }
                                    default -> { // EAST
                                        if (isAirAt(snapshotGrid, minChunkX, minChunkZ, targetX - 2, checkY, targetZ - 1)
                                                && isAirAt(snapshotGrid, minChunkX, minChunkZ, targetX - 2, checkY, targetZ)
                                                && isAirAt(snapshotGrid, minChunkX, minChunkZ, targetX - 2, checkY, targetZ + 1)) {
                                            safeExit = true;
                                        }
                                    }
                                }
                                // everything matches! return the location
                                if (safeExit) {
                                    Location safeLoc = new Location(world, targetX, checkY, targetZ);
                                    return Optional.of(safeLoc);
                                }
                            }
                        }
                    }
                }
                // otherwise move outward along the spiral pattern to check the next block coordinate
                if (x == z || (x < 0 && x == -z) || (x > 0 && x == 1 - z)) {
                    int temp = dx;
                    dx = -dz;
                    dz = temp;
                }
                x += dx;
                z += dz;
            }
            // no location found within the entire target region
            return Optional.empty();
        });
    }

    /**
     * Snapshot-safe air verification.
     */
    private static boolean isAirAt(ChunkSnapshot[][] grid, int minChunkX, int minChunkZ, int worldX, int y, int worldZ) {
        int chunkX = worldX >> 4;
        int chunkZ = worldZ >> 4;
        ChunkSnapshot snapshot = grid[chunkX - minChunkX][chunkZ - minChunkZ];
        return snapshot.getBlockType(worldX & 15, y, worldZ & 15).isAir();
    }
}

