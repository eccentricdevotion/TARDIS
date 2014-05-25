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
package me.eccentric_nz.TARDIS.commands.preferences;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.utility.TARDISAntiBuild;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 *
 * @author eccentric_nz
 */
public class TARDISBuildCommand {

    private final TARDIS plugin;

    public TARDISBuildCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean toggleCompanionBuilding(Player player, String[] args) {
        if (!plugin.isWorldGuardOnServer() || !plugin.getConfig().getBoolean("allow.wg_flag_set")) {
            TARDISMessage.send(player, "CMD_DISABLED");
            return true;
        }
        String playerNameStr = player.getName();
        // get the player's TARDIS world
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (!rs.resultSet()) {
            TARDISMessage.send(player, "NO_TARDIS");
            return true;
        }
        Integer id = rs.getTardis_id();
        HashMap<String, Object> setp = new HashMap<String, Object>();
        HashMap<String, Object> wherep = new HashMap<String, Object>();
        wherep.put("uuid", player.getUniqueId().toString());
        if (args[1].equalsIgnoreCase("on")) {
            setp.put("build_on", 1);
            plugin.getTrackerKeeper().getAntiBuild().remove(id);
            TARDISMessage.send(player, "ANTIBUILD_ON");
        }
        if (args[1].equalsIgnoreCase("off")) {
            setp.put("build_on", 0);
            TARDISAntiBuild tab = new TARDISAntiBuild();
            // get region vectors
            ProtectedRegion pr = plugin.getWorldGuardUtils().getRegion(rs.getChunk().split(":")[0], playerNameStr);
            if (pr == null) {
                TARDISMessage.send(player, "WG_NOT_FOUND");
                return true;
            }
            Vector min = new Vector(pr.getMinimumPoint().getBlockX(), pr.getMinimumPoint().getBlockY(), pr.getMinimumPoint().getBlockZ());
            Vector max = new Vector(pr.getMaximumPoint().getBlockX(), pr.getMaximumPoint().getBlockY(), pr.getMaximumPoint().getBlockZ());
            tab.setMin(min);
            tab.setMax(max);
            tab.setTimelord(playerNameStr);
            plugin.getTrackerKeeper().getAntiBuild().put(id, tab);
            TARDISMessage.send(player, "ANTIBUILD_OFF");
        }
        new QueryFactory(plugin).doUpdate("player_prefs", setp, wherep);
        return true;
    }
}
