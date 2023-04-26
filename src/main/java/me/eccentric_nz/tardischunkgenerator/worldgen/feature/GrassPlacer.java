package me.eccentric_nz.tardischunkgenerator.worldgen.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Iterator;

public class GrassPlacer {

    private final RandomSource random = RandomSource.create();

    // public boolean place(FeaturePlaceContext<DiskConfiguration> data, WorldGenLevel level, BlockPos blockPos) {
    public boolean place(TARDISGrassData data, WorldGenLevel level, BlockPos blockPos) {
        boolean success = false;
        int y = blockPos.getY();
        int upperY = y + data.getHalfHeight();
        int lowerY = y - data.getHalfHeight() - 1;
        int var9 = data.getRadius().sample(random);
        BlockPos.MutableBlockPos var10 = new BlockPos.MutableBlockPos();
        Iterator blockPosIterator = BlockPos.betweenClosed(blockPos.offset(-var9, 0, -var9), blockPos.offset(var9, 0, var9)).iterator();
        while (blockPosIterator.hasNext()) {
            BlockPos pos = (BlockPos) blockPosIterator.next();
            int var13 = pos.getX() - blockPos.getX();
            int var14 = pos.getZ() - blockPos.getZ();
            if (var13 * var13 + var14 * var14 <= var9 * var9) {
                success |= placeColumn(data, level, upperY, lowerY, var10.set(pos));
            }
        }
        return success;
    }

    protected boolean placeColumn(TARDISGrassData data, WorldGenLevel level, int start, int limit, BlockPos.MutableBlockPos pos) {
        boolean success = false;
        for (int y = start; y > limit; --y) {
            pos.setY(y);
            if (data.getTarget().test(level, pos)) {
                BlockState state = data.getState().getState(level, random, pos);
                level.setBlock(pos, state, 2);
                markAboveForPostProcessing(level, pos);
                success = true;
            }
        }
        return success;
    }

    protected void markAboveForPostProcessing(WorldGenLevel level, BlockPos pos) {
        BlockPos.MutableBlockPos blockPos = pos.mutable();
        for (int y = 0; y < 2; ++y) {
            blockPos.move(Direction.UP);
            if (level.getBlockState(blockPos).isAir()) {
                return;
            }
            level.getChunk(blockPos).markPosForPostprocessing(blockPos);
        }
    }
}
