package me.eccentric_nz.tardisweepingangels.nms;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R1.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTeleportEvent;

import java.util.EnumSet;

public class FollowPathFinder extends Goal {

    private final TWAFollower follower;
    private final LevelReader level;
    private final double speedModifier;
    private final PathNavigation navigation;
    private final float stopDistance;
    private final float startDistance;
    private final boolean canFly;
    private LivingEntity owner;
    private int timeToRecalcPath;
    private float oldWaterCost;

    public FollowPathFinder(TWAFollower follower, double speed, float start, float stop, boolean fly) {
        this.follower = follower;
        this.level = follower.level();
        this.speedModifier = speed;
        this.navigation = follower.getNavigation();
        this.startDistance = start;
        this.stopDistance = stop;
        this.canFly = fly;
        setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean canUse() {
        LivingEntity entity = this.follower.getOwner();
        if (entity == null) {
            return false;
        }
        if (entity.isSpectator()) {
            return false;
        }
        if (unableToMove()) {
            return false;
        }
        if (this.follower.distanceToSqr(entity) < (this.startDistance * this.startDistance)) {
            return false;
        }
        this.owner = entity;
        return true;
    }

    public boolean canContinueToUse() {
        return !this.navigation.isDone() && (!unableToMove() && ((this.follower.distanceToSqr(this.owner) > (this.stopDistance * this.stopDistance))));
    }

    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.follower.getPathfindingMalus(PathType.WATER);
        this.follower.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    public void stop() {
        this.owner = null;
        this.navigation.stop();
        this.follower.setPathfindingMalus(PathType.WATER, this.oldWaterCost);
    }

    public void tick() {
        this.follower.getLookControl().setLookAt(this.owner, 10.0F, this.follower.getMaxHeadXRot());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = adjustedTickDelay(10);
            if (this.follower.distanceToSqr(this.owner) >= 144.0D) {
                teleportToOwner();
            } else {
                this.navigation.moveTo(this.owner, this.speedModifier);
            }
        }
    }

    private boolean unableToMove() {
        return (!this.follower.isFollowing() || this.follower.isPassenger() || this.follower.isLeashed());
    }

    private void teleportToOwner() {
        BlockPos pos = this.owner.blockPosition();
        for (int i = 0; i < 10; i++) {
            int x = randomIntInclusive(-3, 3);
            int y = randomIntInclusive(-1, 1);
            int z = randomIntInclusive(-3, 3);
            boolean flag = maybeTeleportTo(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
            if (flag) {
                return;
            }
        }
    }

    private boolean maybeTeleportTo(int x, int y, int z) {
        if (Math.abs(x - this.owner.getX()) < 2.0D && Math.abs(z - this.owner.getZ()) < 2.0D) {
            return false;
        }
        if (!canTeleportTo(new BlockPos(x, y, z))) {
            return false;
        }
        EntityTeleportEvent event = CraftEventFactory.callEntityTeleportEvent(this.follower, x + 0.5D, y, z + 0.5D);
        if (event.isCancelled()) {
            return false;
        }
        Location to = event.getTo();
        this.follower.moveTo(to.getX(), to.getY(), to.getZ(), to.getYaw(), to.getPitch());
        this.navigation.stop();
        return true;
    }

    private boolean canTeleportTo(BlockPos pos) {
        PathType pathType = WalkNodeEvaluator.getPathTypeStatic(this.follower, pos.mutable());
        if (pathType != PathType.WALKABLE) {
            return false;
        }
        BlockState state = this.level.getBlockState(pos.below());
        if (!this.canFly && state.getBlock() instanceof net.minecraft.world.level.block.LeavesBlock) {
            return false;
        }
        BlockPos pos1 = pos.subtract(this.follower.blockPosition());
        return this.level.noCollision(this.follower, this.follower.getBoundingBox().move(pos1));
    }

    private int randomIntInclusive(int min, int max) {
        return this.follower.getRandom().nextInt(max - min + 1) + min;
    }
}
