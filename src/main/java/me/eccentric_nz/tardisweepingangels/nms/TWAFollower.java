package me.eccentric_nz.tardisweepingangels.nms;

import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
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
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.Optional;
import java.util.UUID;

public class TWAFollower extends Husk implements OwnableEntity {

    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(TWAFollower.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(TWAFollower.class, EntityDataSerializers.OPTIONAL_UUID);
    protected final int[] frames = new int[]{0, 1, 2, 1, 0, 3, 4, 3};
    protected UUID uuid;
    protected boolean isAnimating = false;
    protected boolean following = false;
    protected int task = -1;
    protected int i = 0;

    public TWAFollower(Level world, UUID owner) {
        super(EntityType.HUSK, world);
        this.uuid = owner;
        setOwnerUUID(this.uuid);
    }

    public static void unfreezeEntityRegistry() throws NoSuchFieldException, IllegalAccessException {
        Field unregisteredIntrusiveHolders = MappedRegistry.class.getDeclaredField("m");
        unregisteredIntrusiveHolders.setAccessible(true);
        unregisteredIntrusiveHolders.set(BuiltInRegistries.ENTITY_TYPE, new IdentityHashMap<EntityType<?>, Holder.Reference<EntityType<?>>>());
        Field frozenField = MappedRegistry.class.getDeclaredField("l");
        frozenField.setAccessible(true);
        frozenField.set(BuiltInRegistries.ENTITY_TYPE, false);
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

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public UUID getOwnerUUID() {
        return (UUID) ((Optional) this.entityData.get(DATA_OWNERUUID_ID)).orElse(null);
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.setTame(true);
        this.entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(uuid));
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
                this.setTame(true);
            } catch (Throwable throwable) {
                this.setTame(false);
            }
        }
    }

    public boolean isTame() {
        return (this.entityData.get(TWAFollower.DATA_FLAGS_ID) & 4) != 0;
    }

    public void setTame(boolean tame) {
        byte b0 = this.entityData.get(TWAFollower.DATA_FLAGS_ID);
        if (tame) {
            this.entityData.set(TWAFollower.DATA_FLAGS_ID, (byte) (b0 | 4));
        } else {
            this.entityData.set(TWAFollower.DATA_FLAGS_ID, (byte) (b0 & -5));
        }
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }
}
