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
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
import me.eccentric_nz.tardisweepingangels.monsters.daleks.DalekEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.empty_child.EmptyChildEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.headless_monks.HeadlessFlameRunnable;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ChunkListener implements Listener {

    private final TARDISWeepingAngels plugin;

    public ChunkListener(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent event) {
        for (Entity d : event.getChunk().getEntities()) {
            PersistentDataContainer pdc = d.getPersistentDataContainer();
            if (d instanceof Skeleton skeleton) {
                if (pdc.has(TARDISWeepingAngels.DALEK, PersistentDataType.INTEGER)) {
                    DalekEquipment.set(skeleton, false);
                } else if (pdc.has(TARDISWeepingAngels.ANGEL, PersistentDataType.INTEGER)) {
                    new Equipper(Monster.WEEPING_ANGEL, skeleton, false, false).setHelmetAndInvisibilty();
                } else if (pdc.has(TARDISWeepingAngels.SILURIAN, PersistentDataType.INTEGER)) {
                    new Equipper(Monster.SILURIAN, skeleton, false, false).setHelmetAndInvisibilty();
                } else if (pdc.has(TARDISWeepingAngels.MONK, PersistentDataType.INTEGER)) {
                    new Equipper(Monster.HEADLESS_MONK, skeleton, false, false).setHelmetAndInvisibilty();
                    // restart flame runnable?
                    int flameID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new HeadlessFlameRunnable(skeleton), 1, 20);
                    pdc.set(TARDISWeepingAngels.FLAME_TASK, PersistentDataType.INTEGER, flameID);
                }
            } else if (d instanceof PigZombie pigZombie) {
                if (pdc.has(TARDISWeepingAngels.WARRIOR, PersistentDataType.INTEGER)) {
                    new Equipper(Monster.ICE_WARRIOR, pigZombie, false, false).setHelmetAndInvisibilty();
                } else if (pdc.has(TARDISWeepingAngels.STRAX, PersistentDataType.INTEGER)) {
                    new Equipper(Monster.STRAX, pigZombie, false, false).setHelmetAndInvisibilty();
                } else if (pdc.has(TARDISWeepingAngels.HATH, PersistentDataType.INTEGER)) {
                    new Equipper(Monster.HATH, pigZombie, false, false).setHelmetAndInvisibilty();
                } else if (pdc.has(TARDISWeepingAngels.DALEK_SEC, PersistentDataType.INTEGER)) {
                    new Equipper(Monster.DALEK_SEC, pigZombie, false, false).setHelmetAndInvisibilty();
                } else if (pdc.has(TARDISWeepingAngels.DAVROS, PersistentDataType.INTEGER)) {
                    new Equipper(Monster.DAVROS, pigZombie, false, false).setHelmetAndInvisibilty();
                }
            } else if (d instanceof Drowned drowned) {
                if (drowned.getEquipment().getHelmet() != null) {
                    ItemMeta im = drowned.getEquipment().getHelmet().getItemMeta();
                    if (im != null && im.hasDisplayName() && im.getDisplayName().endsWith(" Head")) {
                        if (pdc.has(TARDISWeepingAngels.DEVIL, PersistentDataType.INTEGER)) {
                            new Equipper(Monster.SEA_DEVIL, drowned, false, false, true).setHelmetAndInvisibilty();
                        } else {
                            Bukkit.getScheduler().scheduleSyncDelayedTask(TARDISWeepingAngels.plugin, () -> drowned.remove(), 2L);
                        }
                    }
                }
            } else if (d instanceof Zombie zombie) {
                if (pdc.has(TARDISWeepingAngels.CYBERMAN, PersistentDataType.INTEGER)) {
                    new Equipper(Monster.CYBERMAN, zombie, false, false).setHelmetAndInvisibilty();
                } else if (pdc.has(TARDISWeepingAngels.EMPTY, PersistentDataType.INTEGER)) {
                    new Equipper(Monster.EMPTY_CHILD, zombie, false, false).setHelmetAndInvisibilty();
                    EmptyChildEquipment.setSpeed(zombie);
                } else if (zombie.getPersistentDataContainer().has(TARDISWeepingAngels.SLITHEEN, PersistentDataType.INTEGER)) {
                    new Equipper(Monster.SLITHEEN, zombie, false).setHelmetAndInvisibilty();
                } else if (pdc.has(TARDISWeepingAngels.SONTARAN, PersistentDataType.INTEGER)) {
                    new Equipper(Monster.SONTARAN, zombie, false, false).setHelmetAndInvisibilty();
                } else if (pdc.has(TARDISWeepingAngels.VASHTA, PersistentDataType.INTEGER)) {
                    new Equipper(Monster.VASHTA_NERADA, zombie, false, false).setHelmetAndInvisibilty();
                } else if (pdc.has(TARDISWeepingAngels.ZYGON, PersistentDataType.INTEGER)) {
                    new Equipper(Monster.ZYGON, zombie, false, false).setHelmetAndInvisibilty();
                }
            } else if (d instanceof ArmorStand stand && stand.getPersistentDataContainer().has(TARDISWeepingAngels.FLAME_TASK, PersistentDataType.INTEGER)) {
                // restart flame runnable
                int flameID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new HeadlessFlameRunnable(stand), 1, 20);
                pdc.set(TARDISWeepingAngels.FLAME_TASK, PersistentDataType.INTEGER, flameID);
            }
        }
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        for (Entity monk : event.getChunk().getEntities()) {
            if (monk instanceof Skeleton || monk instanceof ArmorStand) {
                PersistentDataContainer pdc = monk.getPersistentDataContainer();
                if (pdc.has(TARDISWeepingAngels.FLAME_TASK, PersistentDataType.INTEGER)) {
                    // stop flame runnable?
                    int f = pdc.get(TARDISWeepingAngels.FLAME_TASK, PersistentDataType.INTEGER);
                    if (f != -1) {
                        plugin.getServer().getScheduler().cancelTask(f);
                    }
                }
            }
        }
    }
}
