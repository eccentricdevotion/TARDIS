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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
import me.eccentric_nz.tardisweepingangels.monsters.empty_child.EmptyChildEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.headless_monks.HeadlessFlameRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

    private final TARDIS plugin;

    public ChunkListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent event) {
        for (Entity d : event.getChunk().getEntities()) {
            PersistentDataContainer pdc = d.getPersistentDataContainer();
            switch (d) {
                case Skeleton skeleton -> {
                    if (pdc.has(TARDISWeepingAngels.MONK, PersistentDataType.INTEGER)) {
                        new Equipper(Monster.HEADLESS_MONK, skeleton, false).setHelmetAndInvisibility();
                        // restart flame runnable?
                        int flameID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new HeadlessFlameRunnable(skeleton), 1, 20);
                        pdc.set(TARDISWeepingAngels.FLAME_TASK, PersistentDataType.INTEGER, flameID);
                    }
                }
                case Drowned drowned -> {
                    if (drowned.getEquipment().getHelmet() != null) {
                        ItemMeta im = drowned.getEquipment().getHelmet().getItemMeta();
                        if (im != null && im.hasDisplayName() && ComponentUtils.endsWith(im.displayName(), " Head")) {
                            if (pdc.has(TARDISWeepingAngels.DEVIL, PersistentDataType.INTEGER)) {
                                new Equipper(Monster.SEA_DEVIL, drowned, false).setHelmetAndInvisibility();
                            } else {
                                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, drowned::remove, 2L);
                            }
                        }
                    }
                }
                case Zombie zombie -> {
                    if (pdc.has(TARDISWeepingAngels.EMPTY, PersistentDataType.INTEGER)) {
                        new Equipper(Monster.EMPTY_CHILD, zombie, false).setHelmetAndInvisibility();
                        EmptyChildEquipment.setSpeed(zombie);
                    }
                }
                case ArmorStand stand when (stand.getPersistentDataContainer().has(TARDISWeepingAngels.FLAME_TASK, PersistentDataType.INTEGER)) -> {
                    if (stand.getEquipment().getHelmet() != null && stand.getEquipment().getHelmet().getType() == Material.RED_CANDLE) {
                        // restart flame runnable
                        int flameID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new HeadlessFlameRunnable(stand), 1, 20);
                        pdc.set(TARDISWeepingAngels.FLAME_TASK, PersistentDataType.INTEGER, flameID);
                    } else {
                        pdc.set(TARDISWeepingAngels.FLAME_TASK, PersistentDataType.INTEGER, -1);
                    }
                }
                default -> {
                }
            }
        }
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        for (Entity monk : event.getChunk().getEntities()) {
            PersistentDataContainer pdc = monk.getPersistentDataContainer();
            if (monk instanceof Skeleton || monk instanceof ArmorStand) {
                if (pdc.has(TARDISWeepingAngels.FLAME_TASK, PersistentDataType.INTEGER)) {
                    // stop flame runnable?
                    int f = pdc.getOrDefault(TARDISWeepingAngels.FLAME_TASK, PersistentDataType.INTEGER, -1);
                    if (f != -1) {
                        plugin.getServer().getScheduler().cancelTask(f);
                    }
                }
            }
        }
    }
}
