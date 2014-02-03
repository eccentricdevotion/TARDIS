/*
 * Copyright (C) 2013 eccentric_nz
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
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISFindCommand {

    private final TARDIS plugin;

    public TARDISFindCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean findTARDIS(Player player, String[] args) {
        if (plugin.getConfig().getString("preferences.difficulty").equalsIgnoreCase("easy")) {
            if (player.hasPermission("tardis.find")) {
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("owner", player.getName());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (!rs.resultSet()) {
                    player.sendMessage(plugin.getPluginName() + MESSAGE.NO_TARDIS.getText());
                    return false;
                }
                HashMap<String, Object> wherecl = new HashMap<String, Object>();
                wherecl.put("tardis_id", rs.getTardis_id());
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                if (rsc.resultSet()) {
                    player.sendMessage(plugin.getPluginName() + "TARDIS was left at " + rsc.getWorld().getName() + " at x: " + rsc.getX() + " y: " + rsc.getY() + " z: " + rsc.getZ());
                    return true;
                } else {
                    player.sendMessage(plugin.getPluginName() + MESSAGE.NO_CURRENT.getText());
                    return true;
                }
            } else {
                player.sendMessage(plugin.getPluginName() + MESSAGE.NO_PERMS.getText());
                return false;
            }
        } else {
            player.sendMessage(plugin.getPluginName() + "You need to craft a TARDIS Locator! Type " + ChatColor.AQUA + "/tardisrecipe locator" + ChatColor.RESET + " to see how to make it.");
            return true;
        }
    }
}
