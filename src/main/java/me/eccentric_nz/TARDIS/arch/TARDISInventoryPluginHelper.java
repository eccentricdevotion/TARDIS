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
import me.eccentric_nz.TARDIS.utility.TARDISMultiInvChecker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

/**
 *
 * @author eccentric_nz
 */
public class TARDISInventoryPluginHelper implements Listener {

    private final TARDIS plugin;

    public TARDISInventoryPluginHelper(TARDIS plugin) {
        this.plugin = plugin;
    }

    /*
     * Multiverse-Inventories listens on LOW priority, so if we listen on LOWEST
     * we should go first.
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerWorldChangeLOWEST(PlayerChangedWorldEvent event) {
        if (!plugin.isMVIOnServer() && !plugin.isMIOnServer()) {
            return;
        }
        final Player player = event.getPlayer();
        boolean shouldSwitch = (plugin.isMVIOnServer())
                ? !plugin.getTMIChecker().checkWorldsCanShare(event.getFrom().getName(), player.getWorld().getName())
                : !TARDISMultiInvChecker.checkWorldsCanShare(event.getFrom().getName(), player.getWorld().getName());
        if (shouldSwitch) {
            // switch to non-fobbed inventory before MVI and MI
            UUID uuid = player.getUniqueId();
            if (plugin.getTrackerKeeper().getJohnSmith().containsKey(uuid)) {
                new TARDISArchInventory().switchInventories(player, 1);
            }
        }
    }

    /*
     * Multiverse-Inventories listens on LOW priority, MultiInv on HIGHEST so if we listen on HIGHEST
     * we should (hopefully for MultiInv) go after.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerWorldChangeNORMAL(PlayerChangedWorldEvent event) {
        if (!plugin.isMVIOnServer() && !plugin.isMIOnServer()) {
            return;
        }
        final Player player = event.getPlayer();
        boolean shouldSwitch = (plugin.isMVIOnServer())
                ? !plugin.getTMIChecker().checkWorldsCanShare(event.getFrom().getName(), player.getWorld().getName())
                : !TARDISMultiInvChecker.checkWorldsCanShare(event.getFrom().getName(), player.getWorld().getName());
        if (shouldSwitch) {
            // switch to back to fobbed inventory after MVI and MI
            UUID uuid = player.getUniqueId();
            if (plugin.getTrackerKeeper().getJohnSmith().containsKey(uuid)) {
                new TARDISArchInventory().switchInventories(player, 0);
            }
        }
    }
}
