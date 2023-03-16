package me.eccentric_nz.tardischunkgenerator.worldgen.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.WeepingVinesFeature;
import net.minecraft.world.level.material.Material;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R3.block.data.CraftBlockData;

public class TreePlacer {

    private final RandomSource random = RandomSource.create();

    public void place(TARDISTreeData data, WorldGenLevel level, BlockPos blockPos, ChunkGenerator generator) {
        BlockState base = ((CraftBlockData) Bukkit.createBlockData(data.getBase())).getState();
        BlockPos pos = null;
        BlockState under = level.getBlockState(blockPos.below());
        if (under == base) {
            pos = blockPos;
        }
        if (pos != null) {
            int i = Mth.nextInt(random, 4, 13);
            if (random.nextInt(12) == 0) {
                i *= 2;
            }
            if (!data.isPlanted()) {
                int depth = generator.getGenDepth();
                if (pos.getY() + i + 1 >= depth) {
                    return;
                }
            }
            boolean b = !data.isPlanted() && random.nextFloat() < 0.06F;
            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 4);
            placeStem(level, data, pos, i, b);
            placeHat(level, data, pos, i, b);
        }
    }

    private boolean isReplaceable(LevelAccessor level, BlockPos pos, boolean b) {
        return level.isStateAtPosition(pos, (blockState) -> {
            Material material = blockState.getMaterial();
            return blockState.canBeReplaced() || b && material == Material.PLANT;
        });
    }

    private void placeStem(LevelAccessor level, TARDISTreeData data, BlockPos pos, int i, boolean b) {
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        BlockState stem = ((CraftBlockData) Bukkit.createBlockData(data.getStem())).getState();
        int limit = b ? 1 : 0;
        for (int x = -limit; x <= limit; ++x) {
            for (int z = -limit; z <= limit; ++z) {
                boolean edge = b && Mth.abs(x) == limit && Mth.abs(z) == limit;
                for (int y = 0; y < i; ++y) {
                    blockPos.setWithOffset(pos, x, y, z);
                    if (isReplaceable(level, blockPos, true)) {
                        if (data.isPlanted()) {
                            if (!level.getBlockState(blockPos.below()).isAir()) {
                                level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                            }
                            level.setBlock(blockPos, stem, 3);
                        } else if (edge) {
                            if (random.nextFloat() < 0.1F) {
                                setBlock(level, blockPos, stem);
                            }
                        } else {
                            setBlock(level, blockPos, stem);
                        }
                    }
                }
            }
        }
    }

    private void placeHat(LevelAccessor level, TARDISTreeData data, BlockPos pos, int i, boolean b) {
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        BlockState hat = ((CraftBlockData) Bukkit.createBlockData(data.getHat())).getState();
        boolean isNetherWart = hat.is(Blocks.NETHER_WART_BLOCK);
        int max = Math.min(random.nextInt(1 + i / 3) + 5, i);
        int min = i - max;
        for (int h = min; h <= i; ++h) {
            int offset = h < i - random.nextInt(3) ? 2 : 1;
            if (max > 8 && h < min + 4) {
                offset = 3;
            }
            if (b) {
                ++offset;
            }
            for (int x = -offset; x <= offset; ++x) {
                for (int z = -offset; z <= offset; ++z) {
                    boolean bx = x == -offset || x == offset;
                    boolean bz = z == -offset || z == offset;
                    boolean notEdge = !bx && !bz && h != i;
                    boolean bxz = bx && bz;
                    boolean notTop = h < min + 3;
                    blockPos.setWithOffset(pos, x, h, z);
                    if (isReplaceable(level, blockPos, false)) {
                        if (data.isPlanted() && !level.getBlockState(blockPos.below()).isAir()) {
                            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                        }
                        if (notTop) {
                            if (!notEdge) {
                                placeHatDropBlock(level, blockPos, hat, isNetherWart);
                            }
                        } else if (notEdge) {
                            placeHatBlock(level, data, blockPos, 0.1F, 0.2F, isNetherWart ? 0.1F : 0.0F);
                        } else if (bxz) {
                            placeHatBlock(level, data, blockPos, 0.01F, 0.7F, isNetherWart ? 0.083F : 0.0F);
                        } else {
                            placeHatBlock(level, data, blockPos, 5.0E-4F, 0.98F, isNetherWart ? 0.07F : 0.0F);
                        }
                    }
                }
            }
        }
    }

    private void placeHatBlock(LevelAccessor level, TARDISTreeData data, BlockPos.MutableBlockPos blockPos, float var4, float var5, float var6) {
        BlockState decor = ((CraftBlockData) Bukkit.createBlockData(data.getDecor())).getState();
        BlockState hat = ((CraftBlockData) Bukkit.createBlockData(data.getHat())).getState();
        if (random.nextFloat() < var4) {
            setBlock(level, blockPos, decor);
        } else if (random.nextFloat() < var5) {
            setBlock(level, blockPos, hat);
            if (random.nextFloat() < var6) {
                tryPlaceWeepingVines(blockPos, level);
            }
        }
    }

    private void placeHatDropBlock(LevelAccessor level, BlockPos pos, BlockState state, boolean b) {
        if (level.getBlockState(pos.below()).is(state.getBlock())) {
            setBlock(level, pos, state);
        } else if ((double) random.nextFloat() < 0.15) {
            setBlock(level, pos, state);
            if (b && random.nextInt(11) == 0) {
                tryPlaceWeepingVines(pos, level);
            }
        }
    }

    private void tryPlaceWeepingVines(BlockPos pos, LevelAccessor level) {
        BlockPos.MutableBlockPos blockPos = pos.mutable().move(Direction.DOWN);
        if (level.isEmptyBlock(blockPos)) {
            int i = Mth.nextInt(random, 1, 5);
            if (random.nextInt(7) == 0) {
                i *= 2;
            }
            WeepingVinesFeature.placeWeepingVinesColumn(level, random, blockPos, i, 23, 25);
        }
    }

    protected void setBlock(LevelWriter writer, BlockPos pos, BlockState state) {
        writer.setBlock(pos, state, 3);
    }
}
