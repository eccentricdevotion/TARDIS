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
package me.eccentric_nz.tardis.commands.tardis;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisPowered;
import me.eccentric_nz.tardis.flight.TardisLand;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.travel.TardisRescue;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TardisRescueCommand {

    private final TardisPlugin plugin;

    TardisRescueCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    boolean startRescue(Player player, String[] args) {
        if (args.length < 2) {
            TardisMessage.send(player, "TOO_FEW_ARGS");
            return true;
        }
        if (TardisPermission.hasPermission(player, "tardis.timetravel.rescue")) {
            ResultSetTardisPowered rs = new ResultSetTardisPowered(plugin);
            if (!rs.fromUUID(player.getUniqueId().toString())) {
                TardisMessage.send(player, "NOT_A_TIMELORD");
                return true;
            }
            if (plugin.getConfig().getBoolean("allow.power_down") && !rs.isPowered()) {
                TardisMessage.send(player, "POWER_DOWN");
                return true;
            }
            String saved = args[1];
            if (!saved.equalsIgnoreCase("accept")) {
                Player destPlayer = plugin.getServer().getPlayer(saved);
                if (destPlayer == null) {
                    TardisMessage.send(player, "NOT_ONLINE");
                    return true;
                }
                UUID savedUUID = destPlayer.getUniqueId();
                String who = (plugin.getTrackerKeeper().getTelepathicRescue().containsKey(savedUUID)) ? Objects.requireNonNull(plugin.getServer().getPlayer(plugin.getTrackerKeeper().getTelepathicRescue().get(savedUUID))).getName() : player.getName();
                // get auto_rescue_on preference
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, destPlayer.getUniqueId().toString());
                if (rsp.resultSet() && rsp.isAutoRescueOn()) {
                    // go straight to rescue
                    TardisRescue res = new TardisRescue(plugin);
                    plugin.getTrackerKeeper().getChat().remove(savedUUID);
                    // delay it so the chat appears before the message
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        TardisRescue.RescueData rd = res.tryRescue(player, destPlayer.getUniqueId(), false);
                        if (rd.success()) {
                            if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(rd.getTardisId())) {
                                new TardisLand(plugin, rd.getTardisId(), player).exitVortex();
                            } else {
                                TardisMessage.send(player, "REQUEST_RELEASE", destPlayer.getName());
                            }
                        }
                    }, 2L);
                } else {
                    TardisMessage.send(destPlayer, "RESCUE_REQUEST", who, ChatColor.AQUA + "tardis rescue accept" + ChatColor.RESET);
                    plugin.getTrackerKeeper().getChat().put(savedUUID, player.getUniqueId());
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        if (plugin.getTrackerKeeper().getChat().containsKey(savedUUID)) {
                            plugin.getTrackerKeeper().getChat().remove(savedUUID);
                            TardisMessage.send(player, "RESCUE_NO_RESPONSE", saved);
                        }
                    }, 1200L);
                }
            }
        } else {
            TardisMessage.send(player, "NO_PERM_PLAYER");
            return true;
        }
        return false;
    }
}
