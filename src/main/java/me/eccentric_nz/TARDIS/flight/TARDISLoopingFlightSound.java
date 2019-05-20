/*
 * Copyright (C) 2019 eccentric_nz
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
package me.eccentric_nz.TARDIS.flight;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;

/**
 * @author eccentric_nz
 */
class TARDISLoopingFlightSound implements Runnable {

    private final TARDIS plugin;
    private final Location location;
    private final int id;

    TARDISLoopingFlightSound(TARDIS plugin, Location location, int id) {
        this.plugin = plugin;
        this.location = location;
        this.id = id;
    }

    @Override
    public void run() {
        // start looping sfx
        int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> TARDISSounds.playTARDISSound(location, "time_rotor", 0.5f), 1L, 280L);
        plugin.getTrackerKeeper().getDestinationVortex().put(id, taskID);
    }
}
