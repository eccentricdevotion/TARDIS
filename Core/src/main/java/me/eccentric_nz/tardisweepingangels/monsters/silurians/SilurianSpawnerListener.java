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
package me.eccentric_nz.tardisweepingangels.monsters.silurians;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
import me.eccentric_nz.tardisweepingangels.nms.MonsterSpawner;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import me.eccentric_nz.tardisweepingangels.utils.WorldGuardChecker;
import me.eccentric_nz.tardisweepingangels.utils.WorldProcessor;
import org.bukkit.Location;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SilurianSpawnerListener implements Listener {

    private final TARDIS plugin;
    private final int spawn_rate;

    public SilurianSpawnerListener(TARDIS plugin) {
        this.plugin = plugin;
        spawn_rate = plugin.getMonstersConfig().getInt("spawn_rate.how_many");
    }

    @EventHandler
    public void onSpawnEvent(SpawnerSpawnEvent event) {
        CreatureSpawner spawner = event.getSpawner();
        if (spawner.getSpawnedType().equals(EntityType.CAVE_SPIDER)) {
            Location cave = event.getLocation();
            String name = WorldProcessor.sanitiseName(cave.getWorld().getName());
            if (plugin.getMonstersConfig().getInt("silurians.worlds." + name) <= 0) {
                return;
            }
            if (plugin.isWorldGuardOnServer() && !WorldGuardChecker.canSpawn(cave)) {
                return;
            }
            // get the current silurian count
            List<Skeleton> silurians = new ArrayList<>();
            Collection<Skeleton> skeletons = cave.getWorld().getEntitiesByClass(Skeleton.class);
            skeletons.forEach((s) -> {
                PersistentDataContainer pdc = s.getPersistentDataContainer();
                if (pdc.has(TARDISWeepingAngels.SILURIAN, PersistentDataType.INTEGER)) {
                    silurians.add(s);
                }
            });
            if (silurians.size() < plugin.getMonstersConfig().getInt("silurians.worlds." + name)) {
                // if less than maximum, spawn another
                for (int i = 0; i < spawn_rate; i++) {
                    LivingEntity e = new MonsterSpawner().create(cave, Monster.SILURIAN);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        new Equipper(Monster.SILURIAN, e, false).setHelmetAndInvisibility();
                        plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(e, EntityType.SKELETON, Monster.SILURIAN, cave));
                    }, 5L);
                }
            }
        }
    }
}
