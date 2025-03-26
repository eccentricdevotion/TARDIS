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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Follower;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R4.CraftWorld;
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
        TWAFollower entity = switch (follower.getSpecies()) {
            case OOD -> new TWAOod(world);
            case JUDOON -> new TWAJudoon(world);
            default -> new TWAK9(world);
        };
//        EntityReference<net.minecraft.world.entity.LivingEntity> reference = (follower.getOwner() != null) ? new EntityReference<>(follower.getOwner()) : null;
//        entity.setOwnerReference(reference);
        entity.setPosRaw(location.getX(), location.getY(), location.getZ());
        entity.setPersistenceRequired();
        world.addFreshEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        entity.getBukkitEntity().getPersistentDataContainer().set(TARDISWeepingAngels.PDC_KEYS.get(follower.getSpecies()), TARDISWeepingAngels.PersistentDataTypeUUID, entity.getUUID());
        entity.getBukkitEntity().getPersistentDataContainer().set(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID, TARDISWeepingAngels.UNCLAIMED);
        new FollowerPersister(TARDIS.plugin).save(follower, entity.getUUID());
        return entity;
    }
}
