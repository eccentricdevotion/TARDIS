/*
 * Copyright (C) 2025 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardischunkgenerator.worldgen.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.WeepingVinesFeature;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public class TreePlacer {

    private final BlockPredicate predicate = BlockPredicate.matchesBlocks(Blocks.OAK_SAPLING, Blocks.SPRUCE_SAPLING, Blocks.BIRCH_SAPLING, Blocks.JUNGLE_SAPLING, Blocks.ACACIA_SAPLING, Blocks.CHERRY_SAPLING, Blocks.DARK_OAK_SAPLING, Blocks.MANGROVE_PROPAGULE, Blocks.DANDELION, Blocks.TORCHFLOWER, Blocks.POPPY, Blocks.BLUE_ORCHID, Blocks.ALLIUM, Blocks.AZURE_BLUET, Blocks.RED_TULIP, Blocks.ORANGE_TULIP, Blocks.WHITE_TULIP, Blocks.PINK_TULIP, Blocks.OXEYE_DAISY, Blocks.CORNFLOWER, Blocks.WITHER_ROSE, Blocks.LILY_OF_THE_VALLEY, Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, Blocks.WHEAT, Blocks.SUGAR_CANE, Blocks.ATTACHED_PUMPKIN_STEM, Blocks.ATTACHED_MELON_STEM, Blocks.PUMPKIN_STEM, Blocks.MELON_STEM, Blocks.LILY_PAD, Blocks.NETHER_WART, Blocks.COCOA, Blocks.CARROTS, Blocks.POTATOES, Blocks.CHORUS_PLANT, Blocks.CHORUS_FLOWER, Blocks.TORCHFLOWER_CROP, Blocks.PITCHER_CROP, Blocks.BEETROOTS, Blocks.SWEET_BERRY_BUSH, Blocks.WARPED_FUNGUS, Blocks.CRIMSON_FUNGUS, Blocks.WEEPING_VINES, Blocks.WEEPING_VINES_PLANT, Blocks.TWISTING_VINES, Blocks.TWISTING_VINES_PLANT, Blocks.CAVE_VINES, Blocks.CAVE_VINES_PLANT, Blocks.SPORE_BLOSSOM, Blocks.AZALEA, Blocks.FLOWERING_AZALEA, Blocks.MOSS_CARPET, Blocks.PINK_PETALS, Blocks.BIG_DRIPLEAF, Blocks.BIG_DRIPLEAF_STEM, Blocks.SMALL_DRIPLEAF);
    private final RandomSource random = RandomSource.create();

    public void place(TARDISTreeData data, WorldGenLevel level, BlockPos blockPos, ChunkGenerator generator) {
        BlockState base = ((CraftBlockData) Bukkit.createBlockData(data.base())).getState();
        BlockPos pos = null;
        BlockState under = level.getBlockState(blockPos.below());
        if (under == base) {
            pos = blockPos;
        }
        if (pos == null) {
        } else {
            int i = Mth.nextInt(random, 4, 13);
            if (random.nextInt(12) == 0) {
                i *= 2;
            }
            if (!data.planted()) {
                int j = generator.getGenDepth();
                if (pos.getY() + i + 1 >= j) {
                    return;
                }
            }
            boolean flag = !data.planted() && random.nextFloat() < 0.06F;
            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 4);
            this.placeStem(level, random, data, pos, i, flag);
            this.placeHat(level, random, data, pos, i, flag);
        }
    }

    private void placeStem(WorldGenLevel level, RandomSource random, TARDISTreeData data, BlockPos pos, int i, boolean b) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        BlockState stem = ((CraftBlockData) Bukkit.createBlockData(data.stem())).getState();
        int limit = b ? 1 : 0;
        for (int x = -limit; x <= limit; ++x) {
            for (int z = -limit; z <= limit; ++z) {
                boolean edge = b && Mth.abs(x) == limit && Mth.abs(z) == limit;
                for (int y = 0; y < i; ++y) {
                    mutableBlockPos.setWithOffset(pos, x, y, z);
                    if (isReplaceable(level, mutableBlockPos, true)) {
                        if (data.planted()) {
                            if (!level.getBlockState(mutableBlockPos.below()).isAir()) {
                                level.destroyBlock(mutableBlockPos, false);
                            }
                            level.setBlock(mutableBlockPos, stem, 3);
                        } else if (edge) {
                            if (random.nextFloat() < 0.1F) {
                                setBlock(level, mutableBlockPos, stem);
                            }
                        } else {
                            setBlock(level, mutableBlockPos, stem);
                        }
                    }
                }
            }
        }
    }

    private void placeHat(WorldGenLevel level, RandomSource random, TARDISTreeData data, BlockPos blockPos, int i, boolean b) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        BlockState hat = ((CraftBlockData) Bukkit.createBlockData(data.hat())).getState();
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
                    pos.setWithOffset(blockPos, x, h, z);
                    if (isReplaceable(level, pos, false)) {
                        if (data.planted() && !level.getBlockState(pos.below()).isAir()) {
                            level.destroyBlock(pos, false);
                        }
                        if (notTop) {
                            if (!notEdge) {
                                this.placeHatDropBlock(level, random, pos, hat, isNetherWart);
                            }
                        } else if (notEdge) {
                            this.placeHatBlock(level, random, data, pos, 0.1F, 0.2F, isNetherWart ? 0.1F : 0.0F);
                        } else if (bxz) {
                            this.placeHatBlock(level, random, data, pos, 0.01F, 0.7F, isNetherWart ? 0.083F : 0.0F);
                        } else {
                            this.placeHatBlock(level, random, data, pos, 5.0E-4F, 0.98F, isNetherWart ? 0.07F : 0.0F);
                        }
                    }
                }
            }
        }
    }

    private void placeHatBlock(WorldGenLevel level, RandomSource random, TARDISTreeData data, BlockPos pos, float f, float f1, float f2) {
        BlockState decor = ((CraftBlockData) Bukkit.createBlockData(data.decor())).getState();
        BlockState hat = ((CraftBlockData) Bukkit.createBlockData(data.hat())).getState();
        if (random.nextFloat() < f) {
            this.setBlock(level, pos, decor);
        } else if (random.nextFloat() < f1) {
            this.setBlock(level, pos, hat);
            if (random.nextFloat() < f2) {
                tryPlaceWeepingVines(pos, level, random);
            }
        }
    }

    private void placeHatDropBlock(WorldGenLevel level, RandomSource random, BlockPos pos, BlockState state, boolean b) {
        if (level.getBlockState(pos.below()).is(state.getBlock())) {
            this.setBlock(level, pos, state);
        } else if ((double) random.nextFloat() < 0.15D) {
            this.setBlock(level, pos, state);
            if (b && random.nextInt(11) == 0) {
                tryPlaceWeepingVines(pos, level, random);
            }
        }
    }

    private void tryPlaceWeepingVines(BlockPos pos, WorldGenLevel level, RandomSource random) {
        BlockPos.MutableBlockPos down = pos.mutable().move(Direction.DOWN);
        if (level.isEmptyBlock(down)) {
            int i = Mth.nextInt(random, 1, 5);
            if (random.nextInt(7) == 0) {
                i *= 2;
            }
            WeepingVinesFeature.placeWeepingVinesColumn(level, random, down, i, 23, 25);
        }
    }

    private boolean isReplaceable(WorldGenLevel level, BlockPos pos, boolean b) {
        if (level.isStateAtPosition(pos, BlockBehaviour.BlockStateBase::canBeReplaced)) {
            return true;
        } else {
            return b && predicate.test(level, pos);
        }
    }

    protected void setBlock(LevelWriter writer, BlockPos pos, BlockState state) {
        writer.setBlock(pos, state, 3);
    }
}
