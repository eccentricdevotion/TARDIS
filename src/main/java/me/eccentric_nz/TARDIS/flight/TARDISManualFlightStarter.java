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
package me.eccentric_nz.TARDIS.flight;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

/**
 * Manual materialization can be enacted by first toggling Materialization Switch #1, which extends the Dimensional
 * Stabilizers into the Multiverse. Then Materialization Switch #2, which uses a materialization field to displace the
 * atmosphere from the area and causes the dematerialization circuit to extend the Exo-Plasmic Shell into real space.
 *
 * @author eccentric_nz
 */
class TARDISManualFlightStarter implements Runnable {

    private final TARDIS plugin;
    private final Player player;
    private final int id;
    private final boolean console;

    TARDISManualFlightStarter(TARDIS plugin, Player player, int id, boolean console) {
        this.plugin = plugin;
        this.player = player;
        this.id = id;
        this.console = console;
    }

    @Override
    public void run() {
        long delay = plugin.getConfig().getLong("travel.manual_flight_delay");
        // start a manual flight session
        plugin.getMessenger().send(player, TardisModule.TARDIS, "FLIGHT_ENGAGED");
        TARDISManualFlightRunnable mfr = new TARDISManualFlightRunnable(plugin, player, id, console);
        int taskid = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, mfr, 10L, delay);
        mfr.setTaskID(taskid);
        // play inflight sound
        if (!plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            plugin.getServer().getScheduler().runTask(plugin, new TARDISLoopingFlightSound(plugin, player.getLocation(), id));
        }
    }
}
