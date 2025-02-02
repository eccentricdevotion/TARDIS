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
package me.eccentric_nz.tardisweepingangels.nms;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class RandomPathGoal extends RandomStrollGoal {

    protected final float probability = 0.001F;

    public RandomPathGoal(PathfinderMob var0, double var1) {
        super(var0, var1);
    }

    @Nullable
    protected Vec3 getPosition() {
        // check if entity is following
        boolean stay = false;
        if (this.mob instanceof TWAFollower follower) {
            stay = !follower.isFollowing();
        }
        if (stay) {
            return this.mob.position();
        }
        if (this.mob.isInWaterOrBubble()) {
            Vec3 var0 = LandRandomPos.getPos(this.mob, 15, 7);
            return (var0 == null) ? super.getPosition() : var0;
        }
        if (this.mob.getRandom().nextFloat() >= this.probability) {
            return LandRandomPos.getPos(this.mob, 10, 7);
        }
        return super.getPosition();
    }
}
