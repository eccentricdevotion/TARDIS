/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.flight;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.utility.TardisSounds;
import org.bukkit.Location;

/**
 * @author eccentric_nz
 */
class TardisLoopingFlightSound implements Runnable {

    private final TardisPlugin plugin;
    private final Location location;
    private final int id;

    TardisLoopingFlightSound(TardisPlugin plugin, Location location, int id) {
        this.plugin = plugin;
        this.location = location;
        this.id = id;
    }

    @Override
    public void run() {
        // start looping sfx
        int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> TardisSounds.playTARDISSound(location, "time_rotor", 0.5f), 1L, 280L);
        plugin.getTrackerKeeper().getDestinationVortex().put(id, taskID);
    }
}
