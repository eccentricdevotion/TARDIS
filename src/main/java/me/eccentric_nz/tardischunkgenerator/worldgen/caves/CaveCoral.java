package me.eccentric_nz.tardischunkgenerator.worldgen.caves;

import com.mojang.datafixers.util.Pair;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.SeaPickle;
import org.bukkit.generator.LimitedRegion;

import java.util.Map;
import java.util.Random;

public class CaveCoral {

    private static final Material[] coral_blocks = Tag.CORAL_BLOCKS.getValues().toArray(new Material[0]);
    private static final Material[] corals = Tag.CORALS.getValues().toArray(new Material[0]);
    private static final Material[] wall_corals = Tag.WALL_CORALS.getValues().toArray(new Material[0]);
    private static final Map<BlockFace, Pair<Integer, Integer>> directions = Map.of(
            BlockFace.SOUTH, new Pair<>(0, 1),
            BlockFace.NORTH, new Pair<>(0, -1),
            BlockFace.EAST, new Pair<>(1, 0),
            BlockFace.WEST, new Pair<>(-1, 0)
    );

    public static void placeBlocks(LimitedRegion region, Random random, int x, int y, int z) {
        BlockData data = coral_blocks[random.nextInt(coral_blocks.length)].createBlockData();
        BlockData targetBlockData = region.getBlockData(x, y, z);
        if (targetBlockData.getMaterial() == Material.WATER || Tag.CORALS.isTagged(targetBlockData.getMaterial()) && region.getBlockData(x, y + 1, z).getMaterial() == Material.WATER) {
            region.setBlockData(x, y, z, data);
            if (random.nextFloat() < 0.25F) {
                Material above = corals[random.nextInt(corals.length)];
                region.setType(x, y + 1, z, above);
            } else if (random.nextFloat() < 0.05F) {
                region.setBlockData(x, y + 1, z, Material.SEA_PICKLE.createBlockData(bd -> ((SeaPickle) bd).setPickles(random.nextInt(4) + 1)));
            }
            for (Map.Entry<BlockFace, Pair<Integer, Integer>> d : directions.entrySet()) {
                if (random.nextFloat() < 0.2F) {
                    if (region.getBlockData(x + d.getValue().getFirst(), y, z + d.getValue().getSecond()).getMaterial() == Material.WATER) {
                        Material fan = wall_corals[random.nextInt(wall_corals.length)];
                        region.setBlockData(x + d.getValue().getFirst(), y, z + d.getValue().getSecond(), fan.createBlockData(fd -> ((Directional) fd).setFacing(d.getKey())));
                    }
                }
            }
        }
    }
}
