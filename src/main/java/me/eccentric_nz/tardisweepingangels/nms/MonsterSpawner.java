package me.eccentric_nz.tardisweepingangels.nms;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Follower;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MonsterSpawner {

    public LivingEntity create(Location location, Monster monster) {
        Entity entity = location.getWorld().spawnEntity(location, monster.getEntityType());
        return (LivingEntity) entity;
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
        entity.setPosRaw(location.getX(), location.getY(), location.getZ());
        entity.setPersistenceRequired();
        world.addFreshEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        entity.getBukkitEntity().getPersistentDataContainer().set(TARDISWeepingAngels.PDC_KEYS.get(follower.getSpecies()), TARDISWeepingAngels.PersistentDataTypeUUID, entity.getUUID());
        entity.getBukkitEntity().getPersistentDataContainer().set(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID, TARDISWeepingAngels.UNCLAIMED);
        new FollowerPersister(TARDIS.plugin).save(follower, entity.getUUID());
        return entity;
    }
}
