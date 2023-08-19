package me.eccentric_nz.tardisweepingangels.nms;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class TWAFollower extends Husk implements OwnableEntity {

    protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID_ID = SynchedEntityData.defineId(TWAFollower.class, EntityDataSerializers.OPTIONAL_UUID);
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

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(6, new FollowPathFinder(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(8, new RandomPathGoal(this, 1.0D));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_OWNER_UUID_ID, Optional.empty());
    }

//    @Override
//    public void addAdditionalSaveData(CompoundTag nbttagcompound) {
//        super.addAdditionalSaveData(nbttagcompound);
//        nbttagcompound.putByteArray("TWAOwner", uuidToByteArray(this.uuid));
//        nbttagcompound.putBoolean("TWAFollowing", this.following);
//    }
//
//    @Override
//    public void readAdditionalSaveData(CompoundTag nbttagcompound) {
//        super.readAdditionalSaveData(nbttagcompound);
//        if (nbttagcompound.contains("TWAOwner")) {
//            setOwnerUUID(byteArrayToUUID(nbttagcompound.getByteArray("TWAOwner")));
//        }
//        if (nbttagcompound.contains("TWAFollowing")) {
//            this.following = nbttagcompound.getBoolean("TWAFollowing");
//        }
//    }
//
//    @Override
//    public CompoundTag saveWithoutId(CompoundTag nbttagcompound) {
//        return super.saveWithoutId(nbttagcompound);
//    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        UUID id = (UUID) ((Optional) this.entityData.get(DATA_OWNER_UUID_ID)).orElse(null);
        return id;
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.uuid = uuid;
        this.entityData.set(DATA_OWNER_UUID_ID, Optional.ofNullable(uuid));
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

//    protected byte[] uuidToByteArray(UUID uuid) {
//        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
//        bb.putLong(uuid.getMostSignificantBits());
//        bb.putLong(uuid.getLeastSignificantBits());
//        return bb.array();
//    }
//
//    protected UUID byteArrayToUUID(byte[] bytes) {
//        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
//        long high = byteBuffer.getLong();
//        long low = byteBuffer.getLong();
//        return new UUID(high, low);
//    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }
}
