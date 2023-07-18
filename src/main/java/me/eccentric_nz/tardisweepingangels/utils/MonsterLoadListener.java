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

import me.eccentric_nz.tardisweepingangels.equip.MonsterEquipment;
import me.eccentric_nz.tardisweepingangels.nms.MonsterSpawner;
import me.eccentric_nz.tardisweepingangels.nms.TWASkeleton;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;

import java.lang.reflect.InvocationTargetException;

/**
 * @author eccentric_nz
 */
public class MonsterLoadListener implements Listener {

    @EventHandler
    public void onMonsterLoad(EntitiesLoadEvent event) {
        for (Entity e : event.getEntities()) {
            try {
                if (e.getClass().getDeclaredMethod("getHandle").invoke(e) instanceof TWASkeleton) {
                    return;
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignore) {
            }
            resetMonster(e);
        }
    }

    public void resetMonster(Entity entity) {
        if (!MonsterEquipment.isMonster(entity)) {
            return;
        }
        Location location = entity.getLocation();
        Monster monster = MonsterEquipment.getMonsterType(entity);
        if (monster != null && entity.getType() != EntityType.ARMOR_STAND) {
            entity.remove();
            new MonsterSpawner().create(location, monster);
        }
    }
}
