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
package me.eccentric_nz.tardisweepingangels.monsters.judoon;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.data.Follower;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.nms.MonsterSpawner;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import me.eccentric_nz.tardisweepingangels.utils.WaterChecker;
import me.eccentric_nz.tardisweepingangels.utils.WorldGuardChecker;
import me.eccentric_nz.tardisweepingangels.utils.WorldProcessor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Husk;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Collection;
import java.util.UUID;

public class JudoonRunnable implements Runnable {

    private final TARDIS plugin;

    public JudoonRunnable(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.getServer().getWorlds().forEach((w) -> {
            // only configured worlds
            String name = WorldProcessor.sanitiseName(w.getName());
            if (plugin.getMonstersConfig().getInt("judoon.worlds." + name) > 0) {
                // get the current judoon count
                int galactic = 0;
                Collection<Husk> police = w.getEntitiesByClass(Husk.class);
                for (Husk h : police) {
                    PersistentDataContainer pdc = h.getPersistentDataContainer();
                    if (pdc.has(TARDISWeepingAngels.JUDOON, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                        galactic++;
                    }
                }
                if (galactic < plugin.getMonstersConfig().getInt("judoon.worlds." + name)) {
                    // if less than maximum, spawn some more
                    spawnJudoon(w);
                }
            }
        });
    }

    private void spawnJudoon(World world) {
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
                LivingEntity husk = (LivingEntity) new MonsterSpawner().createFollower(l, new Follower(UUID.randomUUID(), TARDISWeepingAngels.UNCLAIMED, Monster.JUDOON)).getBukkitEntity();
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    JudoonEquipment.set(null, husk, false);
                    plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(husk, EntityType.HUSK, Monster.JUDOON, l));
                }, 2L);
            }
        }
    }
}
