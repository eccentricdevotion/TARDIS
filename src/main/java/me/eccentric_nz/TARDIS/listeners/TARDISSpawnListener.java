/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.listeners;

import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSpawnListener implements Listener {

    private final TARDIS plugin;
    List<SpawnReason> good_spawns = new ArrayList<SpawnReason>();

    public TARDISSpawnListener(TARDIS plugin) {
        this.plugin = plugin;
        good_spawns.add(SpawnReason.BREEDING);
        good_spawns.add(SpawnReason.BUILD_IRONGOLEM);
        good_spawns.add(SpawnReason.BUILD_SNOWMAN);
        good_spawns.add(SpawnReason.EGG);
        good_spawns.add(SpawnReason.SPAWNER_EGG);
    }

    /**
     * Listens for entity spawn events. If WorldGuard is enabled it blocks
     * mob-spawning inside the TARDIS, so this checks to see if we are doing the
     * spawning and un-cancels WorldGuard's setCancelled(true).
     *
     * It also prevents natural mob spawning in the TARDIS DEEP_OCEAN biome.
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntitySpawn(CreatureSpawnEvent event) {
        Location l = event.getLocation();
        // override allowable TARDIS spawns
        if (event.isCancelled()) {
            boolean isTardisWorldSpawn = (l.getWorld().getName().contains("TARDIS") && good_spawns.contains(event.getSpawnReason()));
            if (isTardisWorldSpawn || plugin.isMySpawn()) {
                event.setCancelled(false);
                if (plugin.isMySpawn()) {
                    plugin.setMySpawn(false);
                }
            }
            return;
        }
        // only if configured
        if (!plugin.getConfig().getBoolean("police_box.set_biome")) {
            return;
        }
        // only natural spawning
        if (!event.getSpawnReason().equals(SpawnReason.NATURAL)) {
            return;
        }
        // only in DEEP_OCEAN
        if (!l.getBlock().getBiome().equals(Biome.DEEP_OCEAN)) {
            return;
        }
        // only monsters
        if (!TARDISConstants.MONSTER_TYPES.contains(event.getEntity().getType())) {
            return;
        }
        // only TARDIS locations
        if (isTARDISBiome(l)) {
            event.setCancelled(true);
        }
    }

    private boolean isTARDISBiome(Location l) {
        /*
         * Looping through all the TARDISes on the server and checking
         * a 3x3 area around their location is too expensive, instead
         * we'll check the specific area around the location and if a 3x3
         * DEEP_OCEAN biome is found then we'll deny the spawn.
         */
        int found = 0, three_by_three = 0;
        int x = l.getBlockX();
        int z = l.getBlockZ();
        World w = l.getWorld();
        /*
         * A 7x7 block area is sure to encapsulate a 3x3 one if the mob
         * spawns anywhere in the 3x3 square. Caveat: This relies on the
         * premise that there is at least 1 block between TARDISes.
         */
        for (int col = -3; col < 4; col++) {
            for (int row = -3; row < 4; row++) {
                Biome b = w.getBlockAt(x + col, 64, z + row).getBiome();
                if (b.equals(Biome.DEEP_OCEAN)) {
                    found++;
                }
                if (found < 3 && !b.equals(Biome.DEEP_OCEAN)) {
                    // reset count - not three in a row
                    found = 0;
                }
                if (found == 3 && !b.equals(Biome.DEEP_OCEAN)) {
                    // found 3 consecutive blocks in a row, increment 3x3 row count
                    three_by_three++;
                    // reset count
                    found = 0;
                    // skip the rest of the row
                    break;
                }
            }

        }
        // check if location is in region
        return three_by_three == 3;
    }
}
