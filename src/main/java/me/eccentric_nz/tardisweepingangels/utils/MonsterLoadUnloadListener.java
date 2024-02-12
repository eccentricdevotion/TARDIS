/*
 * Copyright (C) 2024 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.equip.MonsterEquipment;
import me.eccentric_nz.tardisweepingangels.nms.FollowerPersister;
import me.eccentric_nz.tardisweepingangels.nms.TWAFollower;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Arrays;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class MonsterLoadUnloadListener implements Listener {

    private final TARDIS plugin;
    private final List<EntityType> justThese = Arrays.asList(
            EntityType.DROWNED,
            EntityType.PIGLIN_BRUTE,
            EntityType.SKELETON,
            EntityType.ZOMBIE,
            EntityType.HUSK,
            EntityType.ZOMBIFIED_PIGLIN,
            EntityType.ARMOR_STAND
    );

    public MonsterLoadUnloadListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMonsterLoad(EntitiesLoadEvent event) {
        for (Entity e : event.getEntities()) {
            if (!justThese.contains(e.getType()) || !MonsterEquipment.isMonster(e)) {
                return;
            }
            new ResetMonster(plugin, e).reset();
        }
    }

    @EventHandler
    public void onEntityUnload(EntitiesUnloadEvent event) {
        for (Entity e : event.getEntities()) {
            if (e.getType() != EntityType.HUSK) {
                return;
            }
            PersistentDataContainer pdc = e.getPersistentDataContainer();
            if (pdc.has(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                TWAFollower follower = (TWAFollower) ((CraftZombie) e).getHandle();
                // save entity in followers table
                new FollowerPersister(plugin).save(follower);
            }
        }
    }
}
