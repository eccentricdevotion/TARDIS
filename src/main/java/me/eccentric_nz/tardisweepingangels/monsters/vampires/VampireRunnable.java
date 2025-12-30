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
package me.eccentric_nz.tardisweepingangels.monsters.vampires;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
import me.eccentric_nz.tardisweepingangels.nms.MonsterSpawner;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import me.eccentric_nz.tardisweepingangels.utils.WorldGuardChecker;
import me.eccentric_nz.tardisweepingangels.utils.WorldProcessor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;

/**
 * Rosanna Calvierri was the alias used by the matriarch of a group of Saturnyns fleeing from a crack in time.
 * <p>
 * Rosanna and her children fled to Venice where she and her eldest son, Francesco, used perception filters to
 * appear human. Here, Rosanna set up a school for girls in order to transform them into Saturnyns, as none of
 * the other females had survived the trip.
 *
 * @author eccentric_nz
 */
public class VampireRunnable implements Runnable {

    private final TARDIS plugin;
    private final int spawn_rate;

    public VampireRunnable(TARDIS plugin) {
        this.plugin = plugin;
        spawn_rate = plugin.getMonstersConfig().getInt("spawn_rate.how_many");
    }

    @Override
    public void run() {
        plugin.getServer().getWorlds().forEach((w) -> {
            // only configured worlds
            String name = WorldProcessor.sanitiseName(w.getName());
            if (plugin.getMonstersConfig().getInt("vampires.worlds." + name) > 0) {
                // get the current vampire count
                int vampires = 0;
                Collection<Drowned> drowned = w.getEntitiesByClass(Drowned.class);
                for (Drowned d : drowned) {
                    PersistentDataContainer pdc = d.getPersistentDataContainer();
                    if (pdc.has(TARDISWeepingAngels.VAMPIRE, PersistentDataType.INTEGER)) {
                        vampires++;
                    }
                }
                if (vampires < plugin.getMonstersConfig().getInt("vampires.worlds." + name)) {
                    // if less than maximum, spawn some more
                    for (int i = 0; i < spawn_rate; i++) {
                        spawnVampire(w);
                    }
                }
            }
        });
    }

    private void spawnVampire(World world) {
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
            if (plugin.isWorldGuardOnServer() && !WorldGuardChecker.canSpawn(l)) {
                return;
            }
            Monster which = TARDISConstants.RANDOM.nextInt(100) < 15 ? Monster.SATURNYNIAN : Monster.VAMPIRE_OF_VENICE;
            LivingEntity vampire = new MonsterSpawner().create(l, which);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                new Equipper(which, vampire, false).setHelmetAndInvisibility();
                plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(vampire, EntityType.DROWNED, Monster.VAMPIRE_OF_VENICE, l));
            }, 5L);
        }
    }
}
