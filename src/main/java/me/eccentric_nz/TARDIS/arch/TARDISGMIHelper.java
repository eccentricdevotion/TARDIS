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
package me.eccentric_nz.TARDIS.arch;

import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

/**
 *
 * @author eccentric_nz
 */
public class TARDISGMIHelper implements Listener {

    private final TARDIS plugin;

    public TARDISGMIHelper(TARDIS plugin) {
        this.plugin = plugin;
    }

    /*
     * GameModeInventories listens on LOW priority, so if we listen on LOWEST
     * we should go first.
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerWorldChangeLOWEST(PlayerGameModeChangeEvent event) {
        final Player player = event.getPlayer();
        // switch to non-fobbed inventory before MVI
        UUID uuid = player.getUniqueId();
        if (plugin.getTrackerKeeper().getJohnSmith().containsKey(uuid)) {
            new TARDISArchInventory().switchInventories(player, 1);
        }
    }

    /*
     * GameModeInventories listens on LOW priority, so if we listen on NORMAL
     * we should go after.
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerWorldChangeNORMAL(PlayerGameModeChangeEvent event) {
        final Player player = event.getPlayer();
        // switch to back to fobbed inventory after MVI
        UUID uuid = player.getUniqueId();
        if (plugin.getTrackerKeeper().getJohnSmith().containsKey(uuid)) {
            new TARDISArchInventory().switchInventories(player, 0);
        }
    }
}
