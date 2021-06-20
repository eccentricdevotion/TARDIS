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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisPowered;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.travel.TARDISRescue;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISRescueCommand {

    private final TARDIS plugin;

    TARDISRescueCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean startRescue(Player player, String[] args) {
        if (args.length < 2) {
            TARDISMessage.send(player, "TOO_FEW_ARGS");
            return true;
        }
        if (TARDISPermission.hasPermission(player, "tardis.timetravel.rescue")) {
            ResultSetTardisPowered rs = new ResultSetTardisPowered(plugin);
            if (!rs.fromUUID(player.getUniqueId().toString())) {
                TARDISMessage.send(player, "NOT_A_TIMELORD");
                return true;
            }
            if (plugin.getConfig().getBoolean("allow.power_down") && !rs.isPowered()) {
                TARDISMessage.send(player, "POWER_DOWN");
                return true;
            }
            String saved = args[1];
            if (!saved.equalsIgnoreCase("accept")) {
                Player destPlayer = plugin.getServer().getPlayer(saved);
                if (destPlayer == null) {
                    TARDISMessage.send(player, "NOT_ONLINE");
                    return true;
                }
                UUID savedUUID = destPlayer.getUniqueId();
                String who = (plugin.getTrackerKeeper().getTelepathicRescue().containsKey(savedUUID)) ? plugin.getServer().getPlayer(plugin.getTrackerKeeper().getTelepathicRescue().get(savedUUID)).getName() : player.getName();
                // get auto_rescue_on preference
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, destPlayer.getUniqueId().toString());
                if (rsp.resultSet() && rsp.isAutoRescueOn()) {
                    // go straight to rescue
                    TARDISRescue res = new TARDISRescue(plugin);
                    plugin.getTrackerKeeper().getChatRescue().remove(savedUUID);
                    // delay it so the chat appears before the message
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        TARDISRescue.RescueData rd = res.tryRescue(player, destPlayer.getUniqueId(), false);
                        if (rd.success()) {
                            if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(rd.getTardis_id())) {
                                new TARDISLand(plugin, rd.getTardis_id(), player).exitVortex();
                                plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.RESCUE, rd.getTardis_id()));
                            } else {
                                TARDISMessage.send(player, "REQUEST_RELEASE", destPlayer.getName());
                            }
                        }
                    }, 2L);
                } else {
                    TARDISMessage.send(destPlayer, "RESCUE_REQUEST", who, ChatColor.AQUA + "tardis rescue accept" + ChatColor.RESET);
                    plugin.getTrackerKeeper().getChatRescue().put(savedUUID, player.getUniqueId());
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        if (plugin.getTrackerKeeper().getChatRescue().containsKey(savedUUID)) {
                            plugin.getTrackerKeeper().getChatRescue().remove(savedUUID);
                            TARDISMessage.send(player, "RESCUE_NO_RESPONSE", saved);
                        }
                    }, 1200L);
                }
            }
        } else {
            TARDISMessage.send(player, "NO_PERM_PLAYER");
            return true;
        }
        return false;
    }
}
