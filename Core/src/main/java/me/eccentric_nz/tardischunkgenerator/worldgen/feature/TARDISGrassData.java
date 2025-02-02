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
