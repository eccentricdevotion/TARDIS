/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.travel.TARDISTravelRequest;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 *
 * @author eccentric_nz
 */
public class TARDISTravelAsk {

    private final TARDIS plugin;

    public TARDISTravelAsk(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean action(Player player, String[] args) {
        if (!TARDISPermission.hasPermission(player, "tardis.timetravel.player")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_PLAYER");
            return true;
        }
        Player requested = plugin.getServer().getPlayer(args[0]);
        if (requested == null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ONLINE");
            return true;
        }
        // check the to player's DND status
        ResultSetPlayerPrefs rspp = new ResultSetPlayerPrefs(plugin, requested.getUniqueId().toString());
        if (rspp.resultSet() && rspp.isDND()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "DND", args[0]);
            return true;
        }
        // check the location
        TARDISTravelRequest ttr = new TARDISTravelRequest(plugin);
        if (!ttr.getRequest(player, requested, requested.getLocation())) {
            return true;
        }
        // ask if we can travel to this player
        UUID requestedUUID = requested.getUniqueId();
        plugin.getMessenger().send(requested, TardisModule.TARDIS, "REQUEST_TRAVEL", player.getName());
        plugin.getMessenger().sendRequestComehereAccept(requested, "REQUEST_COMEHERE_ACCEPT", "/tardis request accept");
        // message asking player too
        plugin.getMessenger().send(player, TardisModule.TARDIS, "REQUEST_SENT", requested.getName());
        plugin.getTrackerKeeper().getChatRescue().put(requestedUUID, player.getUniqueId());
        Player p = player;
        String to = args[0];
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (plugin.getTrackerKeeper().getChatRescue().containsKey(requestedUUID)) {
                plugin.getTrackerKeeper().getChatRescue().remove(requestedUUID);
                plugin.getMessenger().send(p, TardisModule.TARDIS, "REQUEST_NO_RESPONSE", to);
            }
        }, 1200L);
        return true;
    }
}
