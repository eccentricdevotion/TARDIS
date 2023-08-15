package me.eccentric_nz.tardisweepingangels.nms;

import me.eccentric_nz.TARDIS.database.data.Follower;
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
        world.addFreshEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (LivingEntity) entity.getBukkitEntity();
    }

    public TWAFollower createFollower(Location location, Follower follower) {
        ServerLevel world = ((CraftWorld) location.getWorld()).getHandle();
        TWAFollower entity;
        switch (follower.getSpecies()) {
            case OOD -> entity = new TWAOod(world, follower.getOwner());
            case JUDOON -> entity = new TWAJudoon(world, follower.getOwner());
            default -> entity = new TWAK9(world, follower.getOwner());
        }
        entity.setPosRaw(location.getX(), location.getY() + 1.25d, location.getZ());
        world.addFreshEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return entity;
    }
}
