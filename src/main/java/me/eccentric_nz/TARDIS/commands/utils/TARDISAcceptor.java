/*
 * Copyright (C) 2022 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.utils;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.travel.TARDISRescue;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TARDISAcceptor {

    private final TARDIS plugin;

    public TARDISAcceptor(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void doRequest(Player player, boolean request) {
        UUID uuid = player.getUniqueId();
        Player rescuer = plugin.getServer().getPlayer(plugin.getTrackerKeeper().getChatRescue().get(uuid));
        TARDISRescue res = new TARDISRescue(plugin);
        plugin.getTrackerKeeper().getChatRescue().remove(uuid);
        // delay it so the chat appears before the message
        String name = player.getName();
        String message = (request) ? "REQUEST_RELEASE" : "RESCUE_RELEASE";
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            TARDISRescue.RescueData rd = res.tryRescue(rescuer, uuid, request);
            if (rd.success()) {
                if (plugin.getTrackerKeeper().getTelepathicRescue().containsKey(uuid)) {
                    Player who = plugin.getServer().getPlayer(plugin.getTrackerKeeper().getTelepathicRescue().get(uuid));
                    if (!plugin.getTrackerKeeper().getDestinationVortex().containsKey(rd.getTardis_id())) {
                        TARDISMessage.send(who, message, name);
                    }
                    plugin.getTrackerKeeper().getTelepathicRescue().remove(uuid);
                } else if (!plugin.getTrackerKeeper().getDestinationVortex().containsKey(rd.getTardis_id())) {
                    TARDISMessage.send(rescuer, message, name);
                    TARDISMessage.send(player, "REQUEST_ACCEPTED", rescuer.getName(), request ? "travel" : "rescue");
                }
                if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(rd.getTardis_id())) {
                    new TARDISLand(plugin, rd.getTardis_id(), rescuer).exitVortex();
                    plugin.getPM().callEvent(new TARDISTravelEvent(rescuer, null, TravelType.RANDOM, rd.getTardis_id()));
                }
            }
        }, 2L);
    }
}
