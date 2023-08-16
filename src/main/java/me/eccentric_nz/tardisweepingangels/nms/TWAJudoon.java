package me.eccentric_nz.tardisweepingangels.nms;

import me.eccentric_nz.TARDIS.TARDIS;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R1.event.CraftEventFactory;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class TWAJudoon extends TWAFollower implements RangedAttackMob {

    protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(TWAJudoon.class, EntityDataSerializers.OPTIONAL_UUID);
    private final int[] frames = new int[]{0, 1, 2, 1, 0, 3, 4, 3};
    private UUID uuid;
    private boolean isAnimating = false;
    private int task = -1;
    private int i = 0;
    private int ammo;
    private boolean guard;

    public TWAJudoon(Level world, UUID owner) {
        super(world, owner);
        this.uuid = owner;
        this.guard = false;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new AttackGoal(this, 1.0D, 20, 15.0F));
    }

    @Override
    public void aiStep() {
        if (hasItemInSlot(EquipmentSlot.HEAD)) {
            ItemStack is = getItemBySlot(EquipmentSlot.HEAD);
            CompoundTag nbt = is.getTag();
            if (!isPathFinding()) {
                Bukkit.getScheduler().cancelTask(task);
                nbt.putInt("CustomModelData", 405);
                isAnimating = false;
            } else if (!isAnimating) {
                // play move animation
                task = Bukkit.getScheduler().scheduleSyncRepeatingTask(TARDIS.plugin, () -> {
                    nbt.putInt("CustomModelData", 400 + frames[i]);
                    i++;
                    if (i == frames.length) {
                        i = 0;
                    }
                }, 1L, 3L);
                isAnimating = true;
            }
        }
        super.aiStep();
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return uuid;
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.uuid = uuid;
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

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public boolean isGuard() {
        return guard;
    }

    public void setGuard(boolean guard) {
        this.guard = guard;
    }

    @Override
    public void performRangedAttack(LivingEntity entityliving, float v) {
        if (this.guard) {
            // fire snowballs from ammo
            while (this.ammo > 0) {
                Snowball snowball = new Snowball(entityliving.level(), entityliving);
                double d0 = entityliving.getX() - getX();
                double d1 = entityliving.getY(0.3333333333333333D) - snowball.getY();
                double d2 = entityliving.getZ() - getZ();
                double d3 = Math.sqrt(d0 * d0 + d2 * d2);
                snowball.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (14 - level().getDifficulty().getId() * 4));
                ProjectileLaunchEvent event = CraftEventFactory.callProjectileLaunchEvent(snowball);
                if (event.isCancelled()) {
                    event.getEntity().remove();
                    return;
                }
                if (event.getEntity() == snowball.getBukkitEntity()) {
                    level().addFreshEntity(snowball);
                }
                playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (getRandom().nextFloat() * 0.4F + 0.8F));
                this.ammo--;
            }
        }
    }
}
