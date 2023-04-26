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

import me.eccentric_nz.tardisweepingangels.equip.Equipper;
import me.eccentric_nz.tardisweepingangels.equip.MonsterEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.daleks.DalekEquipment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;

public class HelmetChecker implements Listener {

    @EventHandler
    public void onLoseHead(EntityCombustEvent event) {
        Entity e = event.getEntity();
        // is it a TARDISWeepingAngels monster?
        Monster monster = MonsterEquipment.getMonsterType(e);
        if (monster != null) {
            event.setCancelled(true);
            // restore head
            switch (monster) {
                case DALEK -> DalekEquipment.set((LivingEntity) e, false);
                case SEA_DEVIL -> new Equipper(monster, (LivingEntity) e, false, false, true);
                default -> new Equipper(monster, (LivingEntity) e, false, (monster.equals(Monster.SILURIAN)));
            }
        }
    }
}
