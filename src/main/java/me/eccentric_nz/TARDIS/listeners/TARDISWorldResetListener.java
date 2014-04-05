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

import com.onarandombox.MultiverseAdventure.event.MVAResetEvent;
import com.onarandombox.MultiverseAdventure.event.MVAResetFinishedEvent;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Track Multiverse Adventure world resetting. If the world is being reset,
 * prevent time travel in TARDIS that were in the world being reset.
 *
 * @author eccentric_nz
 */
public class TARDISWorldResetListener implements Listener {

    private final TARDIS plugin;

    public TARDISWorldResetListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMVAReset(MVAResetEvent event) {
        String world = event.getWorld();
        plugin.getTrackerKeeper().getTrackReset().add(world);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMVAResetFinished(MVAResetFinishedEvent event) {
        String world = event.getWorld();
        plugin.getTrackerKeeper().getTrackReset().remove(world);
    }
}
