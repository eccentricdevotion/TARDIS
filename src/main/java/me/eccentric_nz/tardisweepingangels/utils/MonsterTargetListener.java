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

import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class MonsterTargetListener implements Listener {

    private static boolean isWearingMonsterHead(Player player, Material material) {
        ItemStack is = player.getInventory().getHelmet();
        if (is != null && is.getType().equals(material) && is.hasItemMeta()) {
            return is.getItemMeta().getPersistentDataContainer().has(TARDISWeepingAngels.MONSTER_HEAD, PersistentDataType.INTEGER);
        }
        return false;
    }

    public static boolean monsterShouldIgnorePlayer(Entity entity, Player player) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        boolean ignore = false;
        switch (entity.getType()) {
            case ZOMBIE -> {
                // cyberman
                if (pdc.has(TARDISWeepingAngels.CYBERMAN, PersistentDataType.INTEGER)) {
                    ignore = isWearingMonsterHead(player, Monster.CYBERMAN.getMaterial());
                }
                // empty child
                if (pdc.has(TARDISWeepingAngels.EMPTY, PersistentDataType.INTEGER)) {
                    ignore = isWearingMonsterHead(player, Monster.EMPTY_CHILD.getMaterial());
                }
                // slitheen
                if (pdc.has(TARDISWeepingAngels.SLITHEEN, PersistentDataType.INTEGER)) {
                    ignore = isWearingMonsterHead(player, Monster.SLITHEEN.getMaterial());
                }
                // sontaran
                if (pdc.has(TARDISWeepingAngels.SONTARAN, PersistentDataType.INTEGER)) {
                    ignore = isWearingMonsterHead(player, Monster.SONTARAN.getMaterial());
                }
                // vashta nerada
                if (pdc.has(TARDISWeepingAngels.VASHTA, PersistentDataType.INTEGER)) {
                    ignore = isWearingMonsterHead(player, Monster.VASHTA_NERADA.getMaterial());
                }
                // zygon
                if (pdc.has(TARDISWeepingAngels.ZYGON, PersistentDataType.INTEGER)) {
                    ignore = isWearingMonsterHead(player, Monster.ZYGON.getMaterial());
                }
            }
            case SKELETON -> {
                // dalek
                if (pdc.has(TARDISWeepingAngels.DALEK, PersistentDataType.INTEGER)) {
                    ignore = isWearingMonsterHead(player, Monster.DALEK.getMaterial());
                }
                // headless monk
                if (pdc.has(TARDISWeepingAngels.MONK, PersistentDataType.INTEGER)) {
                    ignore = isWearingMonsterHead(player, Monster.HEADLESS_MONK.getMaterial());
                }
                // mire
                if (pdc.has(TARDISWeepingAngels.MIRE, PersistentDataType.INTEGER)) {
                    ignore = isWearingMonsterHead(player, Monster.MIRE.getMaterial());
                }
                // silent
                if (pdc.has(TARDISWeepingAngels.SILENT, PersistentDataType.INTEGER)) {
                    ignore = isWearingMonsterHead(player, Monster.SILENT.getMaterial());
                }
                // silurian
                if (pdc.has(TARDISWeepingAngels.SILURIAN, PersistentDataType.INTEGER)) {
                    ignore = isWearingMonsterHead(player, Monster.SILURIAN.getMaterial());
                }
                // weeping angel
                if (pdc.has(TARDISWeepingAngels.ANGEL, PersistentDataType.INTEGER)) {
                    ignore = isWearingMonsterHead(player, Monster.WEEPING_ANGEL.getMaterial());
                }
            }
            case GUARDIAN -> {
                // silent
                if (pdc.has(TARDISWeepingAngels.SILENT, PersistentDataType.INTEGER)) {
                    ignore = isWearingMonsterHead(player, Monster.SILENT.getMaterial());
                }
            }
            case ZOMBIFIED_PIGLIN -> {
                // dalek sec
                if (pdc.has(TARDISWeepingAngels.DALEK_SEC, PersistentDataType.INTEGER)) {
                    ignore = isWearingMonsterHead(player, Monster.DALEK_SEC.getMaterial());
                }
                // davros
                if (pdc.has(TARDISWeepingAngels.DAVROS, PersistentDataType.INTEGER)) {
                    ignore = isWearingMonsterHead(player, Monster.DAVROS.getMaterial());
                }
                // hath
                if (pdc.has(TARDISWeepingAngels.HATH, PersistentDataType.INTEGER)) {
                    ignore = isWearingMonsterHead(player, Monster.HATH.getMaterial());
                }
                // ice warrior
                if (pdc.has(TARDISWeepingAngels.WARRIOR, PersistentDataType.INTEGER)) {
                    ignore = isWearingMonsterHead(player, Monster.ICE_WARRIOR.getMaterial());
                }
                // strax
                if (pdc.has(TARDISWeepingAngels.STRAX, PersistentDataType.INTEGER)) {
                    ignore = isWearingMonsterHead(player, Monster.STRAX.getMaterial());
                }
            }
            case DROWNED -> {
                // sea devil
                if (pdc.has(TARDISWeepingAngels.DEVIL, PersistentDataType.INTEGER)) {
                    ignore = isWearingMonsterHead(player, Monster.SEA_DEVIL.getMaterial());
                }
            }
            default -> {
            }
        }
        return ignore;
    }

    @EventHandler
    public void onMonsterTargetEvent(EntityTargetLivingEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof org.bukkit.entity.Monster monster) {
            double range = (entity instanceof Zombie) ? 40.0d : 16.0d;
            Player closest = null;
            double distance = 1000.0f;
            Location locEnt = entity.getLocation();
            for (Entity e : entity.getNearbyEntities(range, range, range)) {
                if (e instanceof Player player) {
                    if (closest == null) {
                        closest = player;
                        distance = e.getLocation().distanceSquared(locEnt);
                    } else if (e.getLocation().distanceSquared(locEnt) < distance) {
                        closest = player;
                        distance = e.getLocation().distanceSquared(locEnt);
                    }
                }
            }
            if (closest != null && monsterShouldIgnorePlayer(entity, closest)) {
                event.setCancelled(true);
                monster.setTarget(null);
            }
        }
    }
}
