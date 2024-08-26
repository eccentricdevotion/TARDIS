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
package me.eccentric_nz.tardisweepingangels.monsters.daleks;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
import me.eccentric_nz.tardisweepingangels.nms.MonsterSpawner;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import me.eccentric_nz.tardisweepingangels.utils.WaterChecker;
import me.eccentric_nz.tardisweepingangels.utils.WorldGuardChecker;
import me.eccentric_nz.tardisweepingangels.utils.WorldProcessor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;

public class DalekRunnable implements Runnable {

    private final TARDIS plugin;
    private final int spawn_rate;

    public DalekRunnable(TARDIS plugin) {
        this.plugin = plugin;
        spawn_rate = plugin.getMonstersConfig().getInt("spawn_rate.how_many");
    }

    @Override
    public void run() {
        plugin.getServer().getWorlds().forEach((w) -> {
            // only configured worlds
            String name = WorldProcessor.sanitiseName(w.getName());
            if (plugin.getMonstersConfig().getInt("daleks.worlds." + name) > 0) {
                // get the current daleks
                int daleks = 0;
                Collection<Skeleton> skeletons = w.getEntitiesByClass(Skeleton.class);
                for (Skeleton d : skeletons) {
                    PersistentDataContainer pdc = d.getPersistentDataContainer();
                    if (pdc.has(TARDISWeepingAngels.DALEK, PersistentDataType.INTEGER)) {
                        daleks++;
                    }
                }
                // count the current daleks
                if (daleks < plugin.getMonstersConfig().getInt("daleks.worlds." + name)) {
                    // if less than maximum, spawn some more
                    for (int i = 0; i < spawn_rate; i++) {
                        spawnDalek(w);
                    }
                }
            }
        });
    }

    private void spawnDalek(World world) {
        int players = world.getPlayers().size();
        // don't bother spawning if there are no players in the world
        if (players == 0) {
            return;
        }
        Chunk[] chunks = world.getLoadedChunks();
        if (chunks.length > 0) {
            Chunk c = chunks[TARDISConstants.RANDOM.nextInt(chunks.length)];
            int x = c.getX() * 16 + TARDISConstants.RANDOM.nextInt(16);
            int z = c.getZ() * 16 + TARDISConstants.RANDOM.nextInt(16);
            int y = world.getHighestBlockYAt(x, z);
            Location l = new Location(world, x, y + 1, z);
            if (WaterChecker.isNotWater(l)) {
                if (plugin.isWorldGuardOnServer() && !WorldGuardChecker.canSpawn(l)) {
                    return;
                }
                EntityType dalek;
                Monster monster;
                int chance = TARDISConstants.RANDOM.nextInt(100);
                boolean sec = chance < plugin.getMonstersConfig().getInt("daleks.dalek_sec_chance");
                boolean dav = chance > (100 - plugin.getMonstersConfig().getInt("daleks.davros_chance"));
                if (sec) {
                    dalek = EntityType.ZOMBIFIED_PIGLIN;
                    monster = Monster.DALEK_SEC;
                } else if (dav) {
                    dalek = EntityType.ZOMBIFIED_PIGLIN;
                    monster = Monster.DAVROS;
                } else {
                    dalek = EntityType.SKELETON;
                    monster = Monster.DALEK;
                }
                LivingEntity e = (sec) ? new MonsterSpawner().create(l, monster) : (LivingEntity) world.spawnEntity(l, dalek);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    if (sec) {
                        new Equipper(monster, e, false).setHelmetAndInvisibility();
                    } else {
                        DalekEquipment.set(e, false);
                    }
                    plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(e, dalek, monster, l));
                }, 5L);
            }
        }
    }
}
