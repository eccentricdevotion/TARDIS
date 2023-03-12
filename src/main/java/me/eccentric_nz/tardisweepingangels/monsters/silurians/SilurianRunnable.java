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

import java.util.Collection;
import java.util.logging.Level;
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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SilurianRunnable implements Runnable {

    private final TARDISWeepingAngels plugin;
    private final int spawn_rate;

    public SilurianRunnable(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
        spawn_rate = plugin.getConfig().getInt("spawn_rate.how_many");
    }

    @Override
    public void run() {
        plugin.getServer().getWorlds().forEach((w) -> {
            // only configured worlds
            String name = WorldProcessor.sanitiseName(w.getName());
            if (plugin.getConfig().getInt("silurians.worlds." + name) > 0) {
                // check the world generator
                if (w.getGenerator() != null) {
                    plugin.getConfig().set("silurians.worlds." + name, 0);
                    plugin.saveConfig();
                    plugin.getServer().getLogger().log(Level.WARNING, "TARDISWeepingAngels cannot safely spawn Silurians in custom worlds!");
                } else {
                    // get the current silurian count
                    int silurians = 0;
                    Collection<Skeleton> skeletons = w.getEntitiesByClass(Skeleton.class);
                    for (Skeleton s : skeletons) {
                        PersistentDataContainer pdc = s.getPersistentDataContainer();
                        if (pdc.has(TARDISWeepingAngels.SILURIAN, PersistentDataType.INTEGER)) {
                            silurians++;
                        }
                    }
                    if (silurians < plugin.getConfig().getInt("silurians.worlds." + name)) {
                        // if less than maximum, spawn some more
                        for (int i = 0; i < spawn_rate; i++) {
                            spawnSilurian(w);
                        }
                    }
                }
            }
        });
    }

    private void spawnSilurian(World world) {
        Chunk[] chunks = world.getLoadedChunks();
        if (chunks.length > 0) {
            Chunk chunk = chunks[TARDISWeepingAngels.random.nextInt(chunks.length)];
            int x = chunk.getX() * 16 + TARDISWeepingAngels.random.nextInt(16);
            int z = chunk.getZ() * 16 + TARDISWeepingAngels.random.nextInt(16);
            int y = world.getHighestBlockYAt(x, z);
            Location l = new Location(world, x, y + 1, z);
            Location search = CaveFinder.searchCave(l);
            Location cave = ((search == null)) ? l : search;
            if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null && !WorldGuardChecker.canSpawn(cave)) {
                return;
            }
            LivingEntity s = (LivingEntity) world.spawnEntity(cave, EntityType.SKELETON);
            s.setSilent(true);
            PotionEffect p = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 360000, 3, true, false);
            s.addPotionEffect(p);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                new Equipper(Monster.SILURIAN, s, false, true).setHelmetAndInvisibilty();
                plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(s, EntityType.SKELETON, Monster.SILURIAN, cave));
            }, 5L);
        }
    }
}
