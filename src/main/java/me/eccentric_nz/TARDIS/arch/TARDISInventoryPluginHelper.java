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
import me.eccentric_nz.TARDIS.utility.TARDISMultiverseInventoriesChecker;
import me.eccentric_nz.TARDIS.utility.TARDISPerWorldInventoryChecker;
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

        final Player player = event.getPlayer();
        boolean shouldSwitch;
        switch (plugin.getInvManager()) {
            case MULTI:
                shouldSwitch = !TARDISMultiInvChecker.checkWorldsCanShare(event.getFrom().getName(), player.getWorld().getName());
                break;
            case MULTIVERSE:
                shouldSwitch = !TARDISMultiverseInventoriesChecker.checkWorldsCanShare(event.getFrom().getName(), player.getWorld().getName());
                break;
            case PER_WORLD:
                shouldSwitch = !TARDISPerWorldInventoryChecker.checkWorldsCanShare(event.getFrom().getName(), player.getWorld().getName());
                break;
            default:
                // GAMEMODE
                shouldSwitch = (plugin.getGeneralKeeper().getDoorListener().checkSurvival(event.getFrom()) != plugin.getGeneralKeeper().getDoorListener().checkSurvival(player.getWorld()));
                break;
        }
        if (shouldSwitch) {
            // switch to non-fobbed inventory before inventory manager
            UUID uuid = player.getUniqueId();
            if (plugin.getTrackerKeeper().getJohnSmith().containsKey(uuid)) {
                new TARDISArchInventory().switchInventories(player, 1);
            }
        }
    }

    /*
     * Multiverse-Inventories and GameModeInventories listen on LOW priority, MultiInv on HIGHEST, PerWorldInventory on MONITOR so if we listen on MONITOR
     * we should go after.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerWorldChangeNORMAL(PlayerChangedWorldEvent event) {

        final Player player = event.getPlayer();
        boolean shouldSwitch;
        switch (plugin.getInvManager()) {
            case MULTI:
                shouldSwitch = !TARDISMultiInvChecker.checkWorldsCanShare(event.getFrom().getName(), player.getWorld().getName());
                break;
            case MULTIVERSE:
                shouldSwitch = !TARDISMultiverseInventoriesChecker.checkWorldsCanShare(event.getFrom().getName(), player.getWorld().getName());
                break;
            case PER_WORLD:
                shouldSwitch = !TARDISPerWorldInventoryChecker.checkWorldsCanShare(event.getFrom().getName(), player.getWorld().getName());
                break;
            default:
                // GAMEMODE
                shouldSwitch = (plugin.getGeneralKeeper().getDoorListener().checkSurvival(event.getFrom()) != plugin.getGeneralKeeper().getDoorListener().checkSurvival(player.getWorld()));
                break;
        }
        if (shouldSwitch) {
            // switch to back to fobbed inventory after MVI and MI
            UUID uuid = player.getUniqueId();
            if (plugin.getTrackerKeeper().getJohnSmith().containsKey(uuid)) {
                new TARDISArchInventory().switchInventories(player, 0);
            }
        }
    }
}
