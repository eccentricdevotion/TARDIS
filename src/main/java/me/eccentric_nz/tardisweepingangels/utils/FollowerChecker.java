/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.tardisweepingangels.utils;

import me.eccentric_nz.TARDIS.database.data.Follower;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.monsters.ood.OodColour;
import me.eccentric_nz.tardisweepingangels.nms.TWAFollower;
import me.eccentric_nz.tardisweepingangels.nms.TWAJudoon;
import me.eccentric_nz.tardisweepingangels.nms.TWAOod;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.UUID;

public class FollowerChecker {

    private Follower follower;
    private boolean valid = false;

    public void checkEntity(Entity entity, UUID playerUUID) {
        if (!entity.getType().equals(EntityType.HUSK)) {
            return;
        }
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        if (pdc.has(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID)) {
            UUID uuid = pdc.get(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID);
            UUID eid = entity.getUniqueId();
            net.minecraft.world.entity.Entity entityHusk = ((CraftEntity) entity).getHandle();
            if (entityHusk instanceof TWAFollower twaf && twaf.isFollowing()) {
                Monster monster = Monster.OOD;
                boolean option = false;
                OodColour colour = OodColour.BLACK;
                int ammo = 0;
                if (playerUUID.equals(uuid)) {
                    if (pdc.has(TARDISWeepingAngels.JUDOON, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                        eid = pdc.get(TARDISWeepingAngels.JUDOON, TARDISWeepingAngels.PersistentDataTypeUUID);
                        monster = Monster.JUDOON;
                        TWAJudoon judoon = (TWAJudoon) ((CraftEntity) entity).getHandle();
                        option = judoon.isGuard();
                        ammo = judoon.getAmmo();
                    } else if (pdc.has(TARDISWeepingAngels.K9, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                        monster = Monster.K9;
                        eid = pdc.get(TARDISWeepingAngels.K9, TARDISWeepingAngels.PersistentDataTypeUUID);
                    } else if (pdc.has(TARDISWeepingAngels.OOD, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                        eid = pdc.get(TARDISWeepingAngels.OOD, TARDISWeepingAngels.PersistentDataTypeUUID);
                        TWAOod ood = (TWAOod) ((CraftEntity) entity).getHandle();
                        option = ood.isRedeye();
                        colour = ood.getColour();
                    }
                    follower = new Follower(eid, uuid, monster, twaf.isFollowing(), option, colour, ammo);
                    valid = true;
                }
            }
        }
    }

    public boolean isValid() {
        return valid;
    }

    public Follower getFollower() {
        return follower;
    }
}
