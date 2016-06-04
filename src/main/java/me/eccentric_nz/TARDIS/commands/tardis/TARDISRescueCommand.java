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
package me.eccentric_nz.TARDIS.commands.tardis;

import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardisPowered;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISRescueCommand {

    private final TARDIS plugin;

    public TARDISRescueCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean startRescue(final Player player, String[] args) {
        if (args.length < 2) {
            TARDISMessage.send(player, "TOO_FEW_ARGS");
            return true;
        }
        if (player.hasPermission("tardis.timetravel.player")) {
            ResultSetTardisPowered rs = new ResultSetTardisPowered(plugin);
            if (!rs.fromUUID(player.getUniqueId().toString())) {
                TARDISMessage.send(player, "NOT_A_TIMELORD");
                return true;
            }
            if (plugin.getConfig().getBoolean("allow.power_down") && !rs.isPowered()) {
                TARDISMessage.send(player, "POWER_DOWN");
                return true;
            }
            final String saved = args[1];
            if (!saved.equalsIgnoreCase("accept")) {
                Player destPlayer = plugin.getServer().getPlayer(saved);
                if (destPlayer == null) {
                    TARDISMessage.send(player, "NOT_ONLINE");
                    return true;
                }
                final UUID savedUUID = destPlayer.getUniqueId();
                String who = (plugin.getTrackerKeeper().getTelepathicRescue().containsKey(savedUUID)) ? plugin.getServer().getPlayer(plugin.getTrackerKeeper().getTelepathicRescue().get(savedUUID)).getName() : player.getName();
                TARDISMessage.send(destPlayer, "RESCUE_REQUEST", who, ChatColor.AQUA + "tardis rescue accept" + ChatColor.RESET);
                plugin.getTrackerKeeper().getChat().put(savedUUID, player.getUniqueId());
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        if (plugin.getTrackerKeeper().getChat().containsKey(savedUUID)) {
                            plugin.getTrackerKeeper().getChat().remove(savedUUID);
                            TARDISMessage.send(player, "RESCUE_NO_RESPONSE", saved);
                        }
                    }
                }, 1200L);
            }
        } else {
            TARDISMessage.send(player, "NO_PERM_PLAYER");
            return true;
        }
        return false;
    }
}
