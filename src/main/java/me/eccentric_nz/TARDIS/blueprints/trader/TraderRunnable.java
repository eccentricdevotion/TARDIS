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
package me.eccentric_nz.TARDIS.blueprints.trader;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.World;
import org.bukkit.entity.Mannequin;
import org.bukkit.persistence.PersistentDataType;

public class TraderRunnable implements Runnable {

    private final TARDIS plugin;
    private final long after;

    public TraderRunnable(TARDIS plugin) {
        this.plugin = plugin;
        // config setting is in minutes so convert to milliseconds
        after = this.plugin.getTradesConfig().getLong("traders.despawn_after") * 60000;
    }

    @Override
    public void run() {
        for (World w : plugin.getServer().getWorlds()) {
            // only non-blacklisted worlds
            String name = w.getKey().getKey();
            if (plugin.getTradesConfig().getStringList("traders.no_spawn").contains(name)) {
                continue;
            }
            spawnTrader(w);
        }
    }

    private void spawnTrader(World world) {
        int players = world.getPlayers().size();
        // don't bother spawning if there are no players in the world
        if (players == 0) {
            return;
        }
        // check for time lord traders in the world
        for (Mannequin entity : world.getEntitiesByClass(Mannequin.class)) {
            if (!entity.getPersistentDataContainer().has(plugin.getTimeLordUuidKey(), PersistentDataType.STRING)) {
                continue;
            }
            if (entity.getPersistentDataContainer().has(plugin.getDestroyKey(), PersistentDataType.LONG)) {
                long last = entity.getPersistentDataContainer().getOrDefault(plugin.getDestroyKey(), PersistentDataType.LONG, 5L);
                if (last + after < System.currentTimeMillis()) {
                    // despawn due to no interactions
                    despawnTrader(entity);
                }
            } else {
                // despawn because has no destroy key
                despawnTrader(entity);
            }
        }

        new TimeLordTraderSpawner(plugin).spawn(world);
    }

    private void despawnTrader(Mannequin mannequin) {
        Dematerialise runnable = new Dematerialise(plugin, mannequin);
        int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 2L, 20L);
        runnable.setTask(task);
        TARDISSounds.playTARDISSound(mannequin.getLocation(), "tardis_takeoff_fast");
    }
}
