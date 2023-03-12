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
package me.eccentric_nz.tardisweepingangels.monsters.silurians;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SilurianSpawnerListener implements Listener {

    private final TARDISWeepingAngels plugin;
    private final int spawn_rate;

    public SilurianSpawnerListener(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
        spawn_rate = plugin.getConfig().getInt("spawn_rate.how_many");
    }

    @EventHandler
    public void onSpawnEvent(SpawnerSpawnEvent event) {
        CreatureSpawner spawner = event.getSpawner();
        if (spawner.getSpawnedType().equals(EntityType.CAVE_SPIDER)) {
            Location cave = event.getLocation();
            String name = WorldProcessor.sanitiseName(cave.getWorld().getName());
            if (plugin.getConfig().getInt("silurians.worlds." + name) <= 0) {
                return;
            }
            if (plugin.getServer().getPluginManager().getPlugin("WorldGuard") != null && !WorldGuardChecker.canSpawn(cave)) {
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
            if (silurians.size() < plugin.getConfig().getInt("silurians.worlds." + name)) {
                // if less than maximum, spawn another
                for (int i = 0; i < spawn_rate; i++) {
                    LivingEntity e = (LivingEntity) cave.getWorld().spawnEntity(cave, EntityType.SKELETON);
                    e.setSilent(true);
                    PotionEffect p = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 360000, 3, true, false);
                    e.addPotionEffect(p);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        new Equipper(Monster.SILURIAN, e, false, true).setHelmetAndInvisibilty();
                        plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(e, EntityType.SKELETON, Monster.SILURIAN, cave));
                    }, 5L);
                }
            }
        }
    }
}
