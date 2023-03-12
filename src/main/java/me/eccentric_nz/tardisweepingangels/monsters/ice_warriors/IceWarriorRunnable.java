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
package me.eccentric_nz.tardisweepingangels.monsters.ice_warriors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import me.eccentric_nz.tardisweepingangels.utils.WaterChecker;
import me.eccentric_nz.tardisweepingangels.utils.WorldGuardChecker;
import me.eccentric_nz.tardisweepingangels.utils.WorldProcessor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class IceWarriorRunnable implements Runnable {

    private final TARDISWeepingAngels plugin;
    private final int spawn_rate;
    private final List<Biome> biomes = new ArrayList<>();

    public IceWarriorRunnable(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
        spawn_rate = plugin.getConfig().getInt("spawn_rate.how_many");
        biomes.add(Biome.DEEP_FROZEN_OCEAN);
        biomes.add(Biome.FROZEN_OCEAN);
        biomes.add(Biome.FROZEN_PEAKS);
        biomes.add(Biome.FROZEN_RIVER);
        biomes.add(Biome.ICE_SPIKES);
        biomes.add(Biome.SNOWY_BEACH);
        biomes.add(Biome.SNOWY_PLAINS);
        biomes.add(Biome.SNOWY_SLOPES);
        biomes.add(Biome.SNOWY_TAIGA);
    }

    @Override
    public void run() {
        plugin.getServer().getWorlds().forEach((w) -> {
            // only configured worlds
            String name = WorldProcessor.sanitiseName(w.getName());
            if (plugin.getConfig().getInt("ice_warriors.worlds." + name) > 0) {
                long time = w.getTime();
                // only spawn in day - times according to http://minecraft.gamepedia.com/Day-night_cycle
                if ((time > 0 && time < 13187) || time > 22812) {
                    // get the current warriors
                    int warriors = 0;
                    Collection<PigZombie> piggies = w.getEntitiesByClass(PigZombie.class);
                    for (PigZombie pz : piggies) {
                        PersistentDataContainer pdc = pz.getPersistentDataContainer();
                        if (pdc.has(TARDISWeepingAngels.WARRIOR, PersistentDataType.INTEGER)) {
                            warriors++;
                        }
                    }
                    // count the current warriors
                    if (warriors < plugin.getConfig().getInt("ice_warriors.worlds." + name)) {
                        // if less than maximum, spawn some more
                        for (int i = 0; i < spawn_rate; i++) {
                            spawnIceWarrior(w);
                        }
                    }
                }
            }
        });
    }

    private void spawnIceWarrior(World world) {
        Chunk[] chunks = world.getLoadedChunks();
        if (chunks.length > 0) {
            Chunk chunk = chunks[TARDISWeepingAngels.random.nextInt(chunks.length)];
            int x = chunk.getX() * 16 + TARDISWeepingAngels.random.nextInt(16);
            int z = chunk.getZ() * 16 + TARDISWeepingAngels.random.nextInt(16);
            int y = world.getHighestBlockYAt(x, z);
            Location l = new Location(world, x, y + 1, z);
            if (biomes.contains(l.getBlock().getBiome()) && WaterChecker.isNotWater(l)) {
                if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null && !WorldGuardChecker.canSpawn(l)) {
                    return;
                }
                LivingEntity i = (LivingEntity) world.spawnEntity(l, EntityType.ZOMBIFIED_PIGLIN);
                i.setSilent(true);
                PigZombie warrior = (PigZombie) i;
                warrior.setAngry(true);
                warrior.setAnger(Integer.MAX_VALUE);
                PotionEffect p = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 360000, 3, true, false);
                warrior.addPotionEffect(p);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    new Equipper(Monster.ICE_WARRIOR, i, false, false).setHelmetAndInvisibilty();
                    plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(i, EntityType.ZOMBIFIED_PIGLIN, Monster.ICE_WARRIOR, l));
                }, 5L);
            }
        }
    }
}
