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

import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
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
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                TARDISMessage.send(player, "NOT_A_TIMELORD");
                return true;
            }
            if (plugin.getConfig().getBoolean("allow.power_down") && !rs.isPowered_on()) {
                TARDISMessage.send(player, "POWER_DOWN");
                return true;
            }
            final String saved = args[1];
            Player destPlayer = plugin.getServer().getPlayer(saved);
            if (destPlayer == null) {
                TARDISMessage.send(player, "NOT_ONLINE");
                return true;
            }
            final UUID savedUUID = destPlayer.getUniqueId();
            TARDISMessage.send(destPlayer, "RESCUE_REQUEST", player.getName(), ChatColor.AQUA + "tardis rescue accept" + ChatColor.RESET);
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
        } else {
            TARDISMessage.send(player, "NO_PERM_PLAYER");
            return true;
        }
        return false;
    }
}
