package me.eccentric_nz.tardisweepingangels.nms;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Follower;
import me.eccentric_nz.TARDIS.flight.TARDISChicken;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MonsterSpawner {

    public LivingEntity create(Location location, Monster monster) {
        ServerLevel world = ((CraftWorld) location.getWorld()).getHandle();
        net.minecraft.world.entity.Entity entity;
        switch (monster.getEntityType()) {
            case ZOMBIE -> entity = new TWAZombie(EntityType.ZOMBIE, world);
            case ZOMBIFIED_PIGLIN -> entity = new TWAZombifiedPiglin(EntityType.ZOMBIFIED_PIGLIN, world);
            case DROWNED -> entity = new TWADrowned(EntityType.DROWNED, world);
            case PIGLIN_BRUTE -> entity = new TWAPiglinBrute(EntityType.PIGLIN_BRUTE, world);
            case CHICKEN -> entity = new TARDISChicken(EntityType.CHICKEN, world);
            default -> entity = new TWASkeleton(EntityType.SKELETON, world);
        }
        entity.setPosRaw(location.getX(), location.getY() + 1.25d, location.getZ());
        world.addFreshEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (LivingEntity) entity.getBukkitEntity();
    }

    public TWAFollower createFollower(Location location, Follower follower) {
        ServerLevel world = ((CraftWorld) location.getWorld()).getHandle();
        TWAFollower entity;
        switch (follower.getSpecies()) {
            case OOD -> entity = new TWAOod(world);
            case JUDOON -> entity = new TWAJudoon(world);
            default -> entity = new TWAK9(world);
        }
        entity.setOwnerUUID(follower.getOwner());
        entity.setPosRaw(location.getX(), location.getY() + 0.5d, location.getZ());
        entity.setPersistenceRequired();
        world.addFreshEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        entity.getBukkitEntity().getPersistentDataContainer().set(TARDISWeepingAngels.PDC_KEYS.get(follower.getSpecies()), TARDISWeepingAngels.PersistentDataTypeUUID, entity.getUUID());
        new FollowerPersister(TARDIS.plugin).save(follower, entity.getUUID());
        return entity;
    }
}
