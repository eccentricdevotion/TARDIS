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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author eccentric_nz
 */
public class TARDISTemporalListener implements Listener {

    private final TARDIS plugin;

    public TARDISTemporalListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Material inhand = p.getItemInHand().getType();
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) && inhand.equals(Material.WATCH) && p.hasPermission("tardis.temporal")) {
            p.resetPlayerTime();
            if (plugin.getTrackerKeeper().getTrackSetTime().containsKey(p.getName())) {
                plugin.getTrackerKeeper().getTrackSetTime().remove(p.getName());
            }
            TARDISMessage.send(p, plugin.getPluginName() + "Temporal Location reset to server time.");
        }
    }
}
