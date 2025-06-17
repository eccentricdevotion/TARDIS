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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftPlayer;

import java.util.UUID;

public class TWAFollower extends Husk implements OwnableEntity {

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
    public void addAdditionalSaveData(ValueOutput tag) {
        super.addAdditionalSaveData(tag);
        if (this.getOwnerUUID() != null) {
            EntityReference<LivingEntity> entityreference = new EntityReference<>(this.getOwnerUUID());
            entityreference.store(tag, "Owner");
        }
    }

    @Override
    public void readAdditionalSaveData(ValueInput tag) {
        super.readAdditionalSaveData(tag);
        EntityReference<LivingEntity> entityreference = EntityReference.readWithOldOwnerConversion(tag, "Owner", this.level());
        if (entityreference != null) {
            this.setOwnerUUID(entityreference.getUUID());
        } else {
            tag.getString("Owner").ifPresent((s -> {
                uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
                if (uuid != null) {
                    this.setOwnerUUID(uuid);
                }
            }));
        }
    }

    public UUID getOwnerUUID() {
        return this.getBukkitEntity().getPersistentDataContainer().get(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID);
    }

    public void setOwnerUUID(UUID uuid) {
        this.uuid = uuid;
        this.getBukkitEntity().getPersistentDataContainer().set(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID, uuid);
    }

    @Override
    public EntityReference<LivingEntity> getOwnerReference() {
        return null;
    }

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
