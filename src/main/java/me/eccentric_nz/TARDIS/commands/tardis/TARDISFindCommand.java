/*
 * Copyright (C) 2020 eccentric_nz
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
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISFindCommand {

    private final TARDIS plugin;

    TARDISFindCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean findTARDIS(Player player) {
        if (TARDISPermission.hasPermission(player, "tardis.find")) {
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            if (!rs.fromUUID(player.getUniqueId().toString())) {
                TARDISMessage.send(player, "NO_TARDIS");
                return true;
            }
            if (plugin.getDifficulty().equals(Difficulty.EASY) || plugin.getUtils().inGracePeriod(player, true)) {
                HashMap<String, Object> wherecl = new HashMap<>();
                wherecl.put("tardis_id", rs.getTardis_id());
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                if (rsc.resultSet()) {
                    String world = rsc.getWorld().getName();
                    if (plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
                        world = plugin.getMVHelper().getAlias(rsc.getWorld());
                    }
                    TARDISMessage.send(player, "TARDIS_FIND", world + " at x: " + rsc.getX() + " y: " + rsc.getY() + " z: " + rsc.getZ());
                    return true;
                } else {
                    TARDISMessage.send(player, "CURRENT_NOT_FOUND");
                    return true;
                }
            } else {
                TARDISMessage.send(player, "DIFF_HARD_FIND", ChatColor.AQUA + "/tardisrecipe locator" + ChatColor.RESET);
                return true;
            }
        } else {
            TARDISMessage.send(player, "NO_PERMS");
            return false;
        }
    }
}
