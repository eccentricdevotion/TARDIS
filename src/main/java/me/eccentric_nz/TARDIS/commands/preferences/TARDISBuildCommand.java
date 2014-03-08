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

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;

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
            TARDISMessage.send(player, plugin.getPluginName() + "That command is not available on this server!");
            return true;
        }
        String playerNameStr = player.getName();
        // get the player's TARDIS world
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("owner", playerNameStr);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (!rs.resultSet()) {
            TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NO_TARDIS.getText());
            return true;
        }
        String data[] = rs.getChunk().split(":");
        if (plugin.getServer().getWorld(data[0]) == null) {
            TARDISMessage.send(player, plugin.getPluginName() + "Could not get TARDIS world!");
            return true;
        }
        if (args[1].equalsIgnoreCase("on")) {
            plugin.getServer().dispatchCommand(plugin.getConsole(), "rg flag tardis_" + playerNameStr + " build -w " + data[0] + " -g members allow");
            return true;
        } else {
            plugin.getServer().dispatchCommand(plugin.getConsole(), "rg flag tardis_" + playerNameStr + " build -w " + data[0] + " -g members deny");
            return true;
        }
    }
}
