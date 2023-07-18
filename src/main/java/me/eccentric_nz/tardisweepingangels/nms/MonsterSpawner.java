package me.eccentric_nz.tardisweepingangels.nms;

import me.eccentric_nz.tardisweepingangels.utils.Monster;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MonsterSpawner {

    public LivingEntity create(Location location, Monster monster) {
        ServerLevel world = ((CraftWorld) location.getWorld()).getHandle();
        net.minecraft.world.entity.monster.Monster entity;
        switch(monster.getEntityType()) {
            case ZOMBIE -> entity = new TWAZombie(EntityType.ZOMBIE, world);
            case ZOMBIFIED_PIGLIN -> entity = new TWAZombifiedPiglin(EntityType.ZOMBIFIED_PIGLIN, world);
            case DROWNED -> entity = new TWADrowned(EntityType.DROWNED, world);
            case PIGLIN_BRUTE -> entity = new TWAPiglinBrute(EntityType.PIGLIN_BRUTE, world);
            default -> entity = new TWASkeleton(EntityType.SKELETON, world);
        }
        entity.setPosRaw(location.getX(), location.getY() + 1.25d, location.getZ());
//                org.bukkit.entity.Entity b = entity.getBukkitEntity();
//        MobEffect effect = MobEffect.byId(14);
//        MobEffectInstance instance = new MobEffectInstance(effect, MobEffectInstance.INFINITE_DURATION, 1, true, false);
//        entity.activeEffects.put(effect, instance);
//        entity.setItemSlot(EquipmentSlot.HEAD, Heads.STILL.get(monster), true);
//        CompoundTag tag = new CompoundTag();
//        entity.saveWithoutId(tag);
//        CompoundTag bukkit = new CompoundTag();
//        bukkit.putInt(TARDISWeepingAngels.PDC_KEYS.get(monster).asString(), monster.getPersist());
//        tag.put("BukkitValues", bukkit);
//        entity.load(tag);

        world.addFreshEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (LivingEntity) entity.getBukkitEntity();
    }
}
