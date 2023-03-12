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
package me.eccentric_nz.tardisweepingangels.monsters.sea_devils;

import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import me.eccentric_nz.tardisweepingangels.utils.WorldGuardChecker;
import me.eccentric_nz.tardisweepingangels.utils.WorldProcessor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

public class SeaDevilRunnable implements Runnable {

    private final TARDISWeepingAngels plugin;
    private final int spawn_rate;

    public SeaDevilRunnable(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
        spawn_rate = plugin.getConfig().getInt("spawn_rate.how_many");
    }

    @Override
    public void run() {
        plugin.getServer().getWorlds().forEach((w) -> {
            // only configured worlds
            String name = WorldProcessor.sanitiseName(w.getName());
            if (plugin.getConfig().getInt("sea_devils.worlds." + name) > 0) {
                // get the current warriors
                int devil = 0;
                Collection<Drowned> drowned = w.getEntitiesByClass(Drowned.class);
                for (Drowned d : drowned) {
                    PersistentDataContainer pdc = d.getPersistentDataContainer();
                    if (pdc.has(TARDISWeepingAngels.DEVIL, PersistentDataType.INTEGER)) {
                        devil++;
                    }
                }
                if (devil < plugin.getConfig().getInt("sea_devils.worlds." + name)) {
                    // if less than maximum, spawn some more
                    for (int i = 0; i < spawn_rate; i++) {
                        spawnSeaDevil(w);
                    }
                }
            }
        });
    }

    private void spawnSeaDevil(World world) {
        Chunk[] chunks = world.getLoadedChunks();
        if (chunks.length > 0) {
            Chunk chunk = chunks[TARDISWeepingAngels.random.nextInt(chunks.length)];
            int x = chunk.getX() * 16 + TARDISWeepingAngels.random.nextInt(16);
            int z = chunk.getZ() * 16 + TARDISWeepingAngels.random.nextInt(16);
            int y = world.getHighestBlockYAt(x, z);
            Location l = new Location(world, x, y + 1, z);
            if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null && !WorldGuardChecker.canSpawn(l)) {
                return;
            }
            LivingEntity devil = (LivingEntity) world.spawnEntity(l, EntityType.DROWNED);
            devil.setSilent(true);
            ((Drowned) devil).setAdult();
            PotionEffect p = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 360000, 3, true, false);
            devil.addPotionEffect(p);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                new Equipper(Monster.SEA_DEVIL, devil, false, false).setHelmetAndInvisibilty();
                plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(devil, EntityType.DROWNED, Monster.SEA_DEVIL, l));
            }, 5L);
        }
    }
}
