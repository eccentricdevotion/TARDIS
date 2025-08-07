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
package me.eccentric_nz.tardisweepingangels.monsters.sutekh;

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
import org.bukkit.entity.Stray;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;

/**
 * Sutekh is a powerful Osiran who desires to destroy all life in the universe so that no form of life could ever
 * challenge his hegemony. Beyond that, he believes his acts of destruction frees those he kills from the tyranny of
 * hope and choice, instead delivering them with the certainty of death. To the human people of Earth, he is mistaken
 * for a god, and is worshipped by the Egyptian culture.
 *
 * @author eccentric_nz
 */
public class SutekhRunnable implements Runnable {

    private final TARDIS plugin;
    private final int spawn_rate;

    public SutekhRunnable(TARDIS plugin) {
        this.plugin = plugin;
        spawn_rate = plugin.getMonstersConfig().getInt("spawn_rate.how_many");
    }

    @Override
    public void run() {
        plugin.getServer().getWorlds().forEach((w) -> {
            // only configured worlds
            String name = WorldProcessor.sanitiseName(w.getName());
            if (plugin.getMonstersConfig().getInt("sutekh.worlds." + name) > 0) {
                // get the current sutekh count
                int sutekh = 0;
                Collection<Stray> strays = w.getEntitiesByClass(Stray.class);
                for (Stray z : strays) {
                    PersistentDataContainer pdc = z.getPersistentDataContainer();
                    if (pdc.has(TARDISWeepingAngels.SUTEKH, PersistentDataType.INTEGER)) {
                        sutekh++;
                    }
                }
                if (sutekh < plugin.getMonstersConfig().getInt("sutekh.worlds." + name)) {
                    // if less than maximum, spawn some more
                    for (int i = 0; i < spawn_rate; i++) {
                        spawnSutekh(w);
                    }
                }
            }
        });
    }

    private void spawnSutekh(World world) {
        int players = world.getPlayers().size();
        // don't bother spawning if there are no players in the world
        if (players == 0) {
            return;
        }
        Chunk[] chunks = world.getLoadedChunks();
        if (chunks.length > 0) {
            Chunk chunk = chunks[TARDISConstants.RANDOM.nextInt(chunks.length)];
            int x = chunk.getX() * 16 + TARDISConstants.RANDOM.nextInt(16);
            int z = chunk.getZ() * 16 + TARDISConstants.RANDOM.nextInt(16);
            int y = world.getHighestBlockYAt(x, z);
            Location l = new Location(world, x, y + 1, z);
            if (WaterChecker.isNotWater(l)) {
                if (plugin.isWorldGuardOnServer() && !WorldGuardChecker.canSpawn(l)) {
                    return;
                }
                LivingEntity sutekh = new MonsterSpawner().create(l, Monster.SUTEKH);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    new Equipper(Monster.SUTEKH, sutekh, false).setHelmetAndInvisibility();
                    plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(sutekh, EntityType.ZOMBIE, Monster.SUTEKH, l));
                }, 5L);
            }
        }
    }
}
