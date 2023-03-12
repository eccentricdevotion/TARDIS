/*
 * Copyright (C) 2023 eccentric_nz
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

import java.util.UUID;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class FollowerChecker {

    private Monster monster;
    private int persist = -1;
    private boolean following = false;

    public FollowerChecker(Entity entity, UUID playerUUID) {
        checkEntity(entity, playerUUID);
    }

    void checkEntity(Entity entity, UUID playerUUID) {
        if (!entity.getType().equals(EntityType.ARMOR_STAND)) {
            monster = Monster.WEEPING_ANGEL;
            return;
        }
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        if (pdc.has(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID)) {
            UUID uuid = pdc.get(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID);
            if (playerUUID.equals(uuid)) {
                if (TARDISWeepingAngels.plugin.getFollowTasks().containsKey(playerUUID)) {
                    following = true;
                    // remove following task
                    TARDISWeepingAngels.plugin.getFollowTasks().remove(playerUUID);
                }
                if (pdc.has(TARDISWeepingAngels.JUDOON, PersistentDataType.INTEGER)) {
                    monster = Monster.JUDOON;
                    persist = pdc.get(TARDISWeepingAngels.JUDOON, PersistentDataType.INTEGER);
                    return;
                } else if (pdc.has(TARDISWeepingAngels.K9, PersistentDataType.INTEGER)) {
                    monster = Monster.K9;
                    return;
                } else if (pdc.has(TARDISWeepingAngels.OOD, PersistentDataType.INTEGER)) {
                    monster = Monster.OOD;
                    return;
                }
            }
        }
        monster = Monster.WEEPING_ANGEL;
    }

    public Monster getMonster() {
        return monster;
    }

    public int getPersist() {
        return persist;
    }

    public boolean isFollowing() {
        return following;
    }
}
