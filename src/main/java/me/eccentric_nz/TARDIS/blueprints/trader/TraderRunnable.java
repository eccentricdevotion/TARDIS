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
import me.eccentric_nz.tardisweepingangels.utils.WorldProcessor;
import org.bukkit.World;

public class TraderRunnable implements Runnable {

    private final TARDIS plugin;

    public TraderRunnable(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (World w : plugin.getServer().getWorlds()) {
            // only non-blacklisted worlds
            String name = w.getKey().getKey();
            if (plugin.getTradesConfig().getStringList("no_spawn").contains(name)) {
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
        new TimeLordTraderSpawner(plugin).spawn(world);
    }
}
