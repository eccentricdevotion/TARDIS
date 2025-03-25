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

import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class TWAFollower extends Husk implements OwnableEntity {

    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(TWAFollower.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(TWAFollower.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
    protected UUID uuid;
    protected boolean following = false;

    public TWAFollower(EntityType<? extends Husk> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(6, new FollowPathFinder(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(8, new RandomPathGoal(this, 1.0D));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder datawatcher) {
        super.defineSynchedData(datawatcher);
        datawatcher.define(TWAFollower.DATA_FLAGS_ID, (byte) 0);
        datawatcher.define(TWAFollower.DATA_OWNERUUID_ID, Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        EntityReference<LivingEntity> entityreference = this.getOwnerReference();
        if (entityreference != null) {
            entityreference.store(tag, "Owner");
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        EntityReference<LivingEntity> entityreference = EntityReference.readWithOldOwnerConversion(tag, "Owner", this.level());
        if (entityreference != null) {
            try {
                this.entityData.set(TWAFollower.DATA_OWNERUUID_ID, Optional.of(entityreference));
            } catch (Throwable ignored) {
            }
        } else {
            this.entityData.set(TWAFollower.DATA_OWNERUUID_ID, Optional.empty());
        }
    }

    @Nullable
    @Override
    public EntityReference<LivingEntity> getOwnerReference() {
        return (EntityReference) ((Optional) this.entityData.get(TWAFollower.DATA_OWNERUUID_ID)).orElse(null);
    }

    public void setOwner(@Nullable LivingEntity entityliving) {
        this.entityData.set(TWAFollower.DATA_OWNERUUID_ID, Optional.ofNullable(entityliving).map(EntityReference::new));
    }

    public void setOwnerReference(@Nullable EntityReference<LivingEntity> entityreference) {
        this.entityData.set(TWAFollower.DATA_OWNERUUID_ID, Optional.ofNullable(entityreference));
    }

    public UUID getOwnerUUID() {
        EntityReference<LivingEntity> reference = this.getOwnerReference();
        return reference.getUUID();
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        if (uuid == null) {
            return null;
        }
        org.bukkit.entity.Player player = Bukkit.getPlayer(uuid);
        return (player != null) ? ((CraftPlayer) player).getHandle() : null;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }
}
