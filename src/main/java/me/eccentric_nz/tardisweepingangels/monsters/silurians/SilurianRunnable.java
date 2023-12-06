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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
import me.eccentric_nz.tardisweepingangels.nms.MonsterSpawner;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import me.eccentric_nz.tardisweepingangels.utils.WorldGuardChecker;
import me.eccentric_nz.tardisweepingangels.utils.WorldProcessor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;

public class SilurianRunnable implements Runnable {

    private final TARDIS plugin;
    private final int spawn_rate;

    public SilurianRunnable(TARDIS plugin) {
        this.plugin = plugin;
        spawn_rate = plugin.getMonstersConfig().getInt("spawn_rate.how_many");
    }

    @Override
    public void run() {
        plugin.getServer().getWorlds().forEach((w) -> {
            // only configured worlds
            String name = WorldProcessor.sanitiseName(w.getName());
            if (plugin.getMonstersConfig().getInt("silurians.worlds." + name) > 0) {
                // get the current silurian count
                int silurians = 0;
                Collection<Skeleton> skeletons = w.getEntitiesByClass(Skeleton.class);
                for (Skeleton s : skeletons) {
                    PersistentDataContainer pdc = s.getPersistentDataContainer();
                    if (pdc.has(TARDISWeepingAngels.SILURIAN, PersistentDataType.INTEGER)) {
                        silurians++;
                    }
                }
                if (silurians < plugin.getMonstersConfig().getInt("silurians.worlds." + name)) {
                    // if less than maximum, spawn some more
                    for (int i = 0; i < spawn_rate; i++) {
                        spawnSilurian(w);
                    }
                }
            }
        });
    }

    private void spawnSilurian(World world) {
        Collection<Player> players = world.getPlayers();
        // don't bother spawning if there are no players in the world
        if (players.isEmpty()) {
            return;
        }
        for (Player p : players) {
            Location playerLocation = p.getLocation();
            int y = playerLocation.getBlockY();
            // caves mostly occur between y = -56 and y = 44
            if (y > -55 && y < 48) {
                Location cave = CaveFinder.searchSpawnPoint(playerLocation);
                if (cave == null) {
                    continue;
                }
                if (plugin.isWorldGuardOnServer() && !WorldGuardChecker.canSpawn(cave)) {
                    continue;
                }
                LivingEntity silurian = new MonsterSpawner().create(cave, Monster.SILURIAN);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    new Equipper(Monster.SILURIAN, silurian, false, true).setHelmetAndInvisibilty();
                    plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(silurian, EntityType.SKELETON, Monster.SILURIAN, cave));
                }, 5L);
            }
        }
    }
}
