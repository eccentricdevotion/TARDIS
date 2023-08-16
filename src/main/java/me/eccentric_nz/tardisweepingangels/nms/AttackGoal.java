package me.eccentric_nz.tardisweepingangels.nms;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;

import java.util.EnumSet;

public class AttackGoal extends Goal {

    private final Mob mob;
    private final RangedAttackMob rangedAttackMob;
    private final double speedModifier;
    private final int attackIntervalMin;
    private final int attackIntervalMax;
    private final float attackRadius;
    private final float attackRadiusSqr;
    private LivingEntity target;
    private int attackTime = -1;
    private int seeTime;

    public AttackGoal(RangedAttackMob var0, double var1, int var3, float var4) {
        this(var0, var1, var3, var3, var4);
    }

    public AttackGoal(RangedAttackMob var0, double var1, int var3, int var4, float var5) {
        if (!(var0 instanceof LivingEntity)) {
            throw new IllegalArgumentException("AttackGoal requires a Monster that implements RangedAttackMob");
        }
        this.rangedAttackMob = var0;
        this.mob = (Mob) var0;
        this.speedModifier = var1;
        this.attackIntervalMin = var3;
        this.attackIntervalMax = var4;
        this.attackRadius = var5;
        this.attackRadiusSqr = var5 * var5;
        setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean canUse() {
        LivingEntity var0 = this.mob.getTarget();
        if (var0 == null || !var0.isAlive())
            return false;
        this.target = var0;
        return true;
    }

    public boolean canContinueToUse() {
        return (canUse() || (this.target.isAlive() && !this.mob.getNavigation().isDone()));
    }

    public void stop() {
        this.target = null;
        this.seeTime = 0;
        this.attackTime = -1;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        double distance = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        boolean hasLineOfSight = this.mob.getSensing().hasLineOfSight(this.target);
        if (hasLineOfSight) {
            this.seeTime++;
        } else {
            this.seeTime = 0;
        }
        if (distance > this.attackRadiusSqr || this.seeTime < 5) {
            this.mob.getNavigation().moveTo(this.target, this.speedModifier);
        } else {
            this.mob.getNavigation().stop();
        }
        this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        if (--this.attackTime == 0) {
            if (!hasLineOfSight) {
                return;
            }
            float var3 = (float) Math.sqrt(distance) / this.attackRadius;
            float var4 = Mth.clamp(var3, 0.1F, 1.0F);
            this.rangedAttackMob.performRangedAttack(this.target, var4);
            this.attackTime = Mth.floor(var3 * (this.attackIntervalMax - this.attackIntervalMin) + this.attackIntervalMin);
        } else if (this.attackTime < 0) {
            this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(distance) / this.attackRadius, this.attackIntervalMin, this.attackIntervalMax));
        }
    }
}

