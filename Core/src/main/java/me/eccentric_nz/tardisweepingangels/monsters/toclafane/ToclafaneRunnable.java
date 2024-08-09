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
package me.eccentric_nz.tardisweepingangels.monsters.toclafane;

import java.util.Collection;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import me.eccentric_nz.tardisweepingangels.utils.WaterChecker;
import me.eccentric_nz.tardisweepingangels.utils.WorldGuardChecker;
import me.eccentric_nz.tardisweepingangels.utils.WorldProcessor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class ToclafaneRunnable implements Runnable {

    private final TARDIS plugin;
    private final int spawn_rate;

    public ToclafaneRunnable(TARDIS plugin) {
        this.plugin = plugin;
        spawn_rate = this.plugin.getMonstersConfig().getInt("spawn_rate.how_many");
    }

    @Override
    public void run() {
        plugin.getServer().getWorlds().forEach((w) -> {
            // only configured worlds
            String name = WorldProcessor.sanitiseName(w.getName());
            if (plugin.getMonstersConfig().getInt("toclafane.worlds." + name) > 0) {
                // get the current toclafane
                int n = 0;
                Collection<Bee> hive = w.getEntitiesByClass(Bee.class);
                for (Bee b : hive) {
                    if (b.getPassengers() != null && !b.getPassengers().isEmpty() && b.getPassengers().getFirst() instanceof ArmorStand) {
                        n++;
                    }
                }
                if (n < plugin.getMonstersConfig().getInt("toclafane.worlds." + name)) {
                    // if less than maximum, spawn some more
                    for (int i = 0; i < spawn_rate; i++) {
                        spawnToclafane(w);
                    }
                }
            }
        });
    }

    private void spawnToclafane(World world) {
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
                Entity e = world.spawnEntity(l, EntityType.ARMOR_STAND);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    ToclafaneEquipment.set(e, false);
                    plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(e, EntityType.ARMOR_STAND, Monster.TOCLAFANE, l));
                }, 5L);
            }
        }
    }
}
