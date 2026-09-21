package me.eccentric_nz.TARDIS.commands.travel.cave;

import com.destroystokyo.paper.MaterialTags;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import org.bukkit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class AsyncCaveFinder {

    private static final int WIDTH = 3;
    private static final int HEIGHT = 4;
    private static final int MAX_ATTEMPTS = 15;
    public static final List<Material> LANDABLE = new ArrayList<>();
    public static final List<Material> REPLACEABLE = new ArrayList<>();

    static {
        LANDABLE.add(Material.DRIPSTONE_BLOCK);
        LANDABLE.add(Material.SCULK);
        LANDABLE.addAll(Tag.BASE_STONE_NETHER.getValues());
        LANDABLE.addAll(Tag.BASE_STONE_OVERWORLD.getValues());
        LANDABLE.addAll(Tag.SUBSTRATE_OVERWORLD.getValues());
        LANDABLE.addAll(Tag.TERRACOTTA.getValues());
        LANDABLE.addAll(Tag.STONE_BRICKS.getValues());
    }

    static {
        REPLACEABLE.add(Material.SNOW);
        REPLACEABLE.add(Material.MOSS_CARPET);
        REPLACEABLE.add(Material.PALE_MOSS_CARPET);
        REPLACEABLE.addAll(Tag.WOOL_CARPETS.getValues());
        REPLACEABLE.addAll(TARDISMaterials.plants);
        REPLACEABLE.addAll(Tag.FLOWERS.getValues());
        REPLACEABLE.addAll(MaterialTags.MUSHROOMS.getValues());
        REPLACEABLE.addAll(Tag.SAPLINGS.getValues());

    }

    public static CompletableFuture<Location> getSafeCave(Location location, int maxChunkRadius, COMPASS direction) {
        return findCaveIterative(location, maxChunkRadius, direction, 0);
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

    public static Location scanSnapshotForCave(World world, ChunkSnapshot snapshot, COMPASS direction) {
        int chunkWorldX = snapshot.getX() << 4;
        int chunkWorldZ = snapshot.getZ() << 4;
        int minY = world.getMinHeight() + 5;
        int maxY = 50;
        // step through the chunk by footprint size
        for (int y = maxY; y >= minY; y -= HEIGHT) {
            for (int x = 0; x <= 16 - WIDTH; x += WIDTH) {
                for (int z = 0; z <= 16 - WIDTH; z += WIDTH) {
                    if (isSafeSpace(snapshot, x, y, z, direction)) {
                        // return center base location of the 3x3 footprint
                        return new Location(world, chunkWorldX + x + 1, y, chunkWorldZ + z + 1);
                    }
                }
            }
        }
        return null;
    }

    private static boolean isSafeSpace(ChunkSnapshot snapshot, int relativeX, int startY, int relativeZ, COMPASS direction) {
        // ensure blocks under footprint are non-lava solid ground or air
        for (int x = 0; x < WIDTH; x++) {
            for (int z = 0; z < WIDTH; z++) {
                Material floorType = snapshot.getBlockType(relativeX + x, startY - 1, relativeZ + z);
                if (!LANDABLE.contains(floorType)) {
                    return false;
                }
            }
        }
        // air clearance check: 3x4x3 box must be air
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                for (int z = 0; z < WIDTH; z++) {
                    Material type = snapshot.getBlockType(relativeX + x, startY + y, relativeZ + z);
                    if (!type.isAir() && !REPLACEABLE.contains(type)) {
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

