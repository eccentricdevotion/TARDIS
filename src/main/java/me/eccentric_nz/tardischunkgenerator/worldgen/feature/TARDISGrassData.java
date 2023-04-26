package me.eccentric_nz.tardischunkgenerator.worldgen.feature;

import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;

public class TARDISGrassData {

    private final RuleBasedBlockStateProvider state;
    private final BlockPredicate target;
    private final IntProvider radius;
    private final int halfHeight;

    public TARDISGrassData(RuleBasedBlockStateProvider state, BlockPredicate target, IntProvider radius, int halfHeight) {
        this.state = state;
        this.target = target;
        this.radius = radius;
        this.halfHeight = halfHeight;
    }

    public RuleBasedBlockStateProvider getState() {
        return state;
    }

    public BlockPredicate getTarget() {
        return target;
    }

    public IntProvider getRadius() {
        return radius;
    }

    public int getHalfHeight() {
        return halfHeight;
    }
}
