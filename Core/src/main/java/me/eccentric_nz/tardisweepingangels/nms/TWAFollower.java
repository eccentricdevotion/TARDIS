package me.eccentric_nz.tardisweepingangels.nms;

import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
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
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class TWAFollower extends Husk implements OwnableEntity {

    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(TWAFollower.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(TWAFollower.class, EntityDataSerializers.OPTIONAL_UUID);
    protected UUID uuid;
    protected boolean following = false;
    protected int i = 0;
    protected double oldX;
    protected double oldZ;

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
        if (this.getOwnerUUID() != null) {
            tag.putUUID("Owner", this.getOwnerUUID());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        UUID uuid;
        if (tag.hasUUID("Owner")) {
            uuid = tag.getUUID("Owner");
        } else {
            String s = tag.getString("Owner");
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }
        if (uuid != null) {
            try {
                this.setOwnerUUID(uuid);
            } catch (Throwable throwable) {
            }
        }
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public UUID getOwnerUUID() {
        return this.getBukkitEntity().getPersistentDataContainer().get(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID);
    }

    public void setOwnerUUID(UUID uuid) {
        this.uuid = uuid;
        this.getBukkitEntity().getPersistentDataContainer().set(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID, uuid);
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
