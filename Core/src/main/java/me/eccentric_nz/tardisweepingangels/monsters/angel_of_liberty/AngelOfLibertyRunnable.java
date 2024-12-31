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
package me.eccentric_nz.tardisweepingangels.monsters.angel_of_liberty;

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
import org.bukkit.entity.Zombie;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;

/**
 * In a timeline in which the Weeping Angels were using New York City as a "battery farm" for feeding on humans' time
 * energy in 1938, the Statue of Liberty was one of the many statues in the city that was taken over by the Angels.
 * The Statue of Liberty would travel from Liberty Island to the Winter Quay in order to send the Angels' imprisoned
 * victims back in time.
 *
 * @author eccentric_nz
 */
public class AngelOfLibertyRunnable implements Runnable {

    private final TARDIS plugin;

    public AngelOfLibertyRunnable(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.getServer().getWorlds().forEach((w) -> {
            // only configured worlds
            String name = WorldProcessor.sanitiseName(w.getName());
            if (plugin.getMonstersConfig().getInt("angel_of_liberty.worlds." + name) > 0) {
                // get the current angel of liberty count
                int angel_of_liberty = 0;
                Collection<Zombie> zombies = w.getEntitiesByClass(Zombie.class);
                for (Zombie z : zombies) {
                    PersistentDataContainer pdc = z.getPersistentDataContainer();
                    if (pdc.has(TARDISWeepingAngels.ANGEL_OF_LIBERTY, PersistentDataType.INTEGER)) {
                        angel_of_liberty++;
                    }
                }
                if (angel_of_liberty < plugin.getMonstersConfig().getInt("angel_of_liberty.worlds." + name)) {
                    // if less than maximum, spawn one
                    spawnAngelOfLiberty(w);
                }
            }
        });
    }

    private void spawnAngelOfLiberty(World world) {
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
                LivingEntity angel_of_liberty = new MonsterSpawner().create(l, Monster.ANGEL_OF_LIBERTY);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    new Equipper(Monster.ANGEL_OF_LIBERTY, angel_of_liberty, false).setHelmetAndInvisibility();
                    plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(angel_of_liberty, EntityType.ZOMBIE, Monster.ANGEL_OF_LIBERTY, l));
                }, 5L);
            }
        }
    }
}
