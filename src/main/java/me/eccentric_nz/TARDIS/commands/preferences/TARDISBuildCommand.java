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
package me.eccentric_nz.TARDIS.commands.preferences;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISAntiBuild;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISBuildCommand {

    private final TARDIS plugin;

    TARDISBuildCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    void toggleCompanionBuilding(Player player, String[] args) {
        if (!plugin.isWorldGuardOnServer() || !plugin.getConfig().getBoolean("allow.wg_flag_set")) {
            TARDISMessage.send(player, "CMD_DISABLED");
            return;
        }
        String playerNameStr = player.getName();
        // get the player's TARDIS world
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (!rs.resultSet()) {
            TARDISMessage.send(player, "NO_TARDIS");
            return;
        }
        Tardis tardis = rs.getTardis();
        Integer id = tardis.getTardis_id();
        HashMap<String, Object> setp = new HashMap<>();
        HashMap<String, Object> wherep = new HashMap<>();
        wherep.put("uuid", player.getUniqueId().toString());
        if (args[1].equalsIgnoreCase(plugin.getLanguage().getString("SET_ON")) || args[1].equalsIgnoreCase("on")) {
            setp.put("build_on", 1);
            plugin.getTrackerKeeper().getAntiBuild().remove(id);
            TARDISMessage.send(player, "ANTIBUILD_ON");
        }
        if (args[1].equalsIgnoreCase(plugin.getLanguage().getString("SET_OFF")) || args[1].equalsIgnoreCase("off")) {
            setp.put("build_on", 0);
            TARDISAntiBuild tab = new TARDISAntiBuild();
            String[] data = tardis.getChunk().split(":");
            // get region vectors
            ProtectedRegion pr = plugin.getWorldGuardUtils().getRegion(data[0], playerNameStr);
            if (pr == null) {
                TARDISMessage.send(player, "WG_NOT_FOUND");
                return;
            }
            Vector min = new Vector(pr.getMinimumPoint().getBlockX(), pr.getMinimumPoint().getBlockY(), pr.getMinimumPoint().getBlockZ());
            Vector max = new Vector(pr.getMaximumPoint().getBlockX(), pr.getMaximumPoint().getBlockY(), pr.getMaximumPoint().getBlockZ());
            tab.setMin(min);
            tab.setMax(max);
            tab.setTimelord(playerNameStr);
            plugin.getTrackerKeeper().getAntiBuild().put(id, tab);
            TARDISMessage.send(player, "ANTIBUILD_OFF");
        }
        plugin.getQueryFactory().doUpdate("player_prefs", setp, wherep);
    }
}
