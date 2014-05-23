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
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISRemoveSavedLocationCommand {

    private final TARDIS plugin;

    public TARDISRemoveSavedLocationCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean doRemoveSave(Player player, String[] args) {
        if (player.hasPermission("tardis.save")) {
            if (args.length < 2) {
                TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.TOO_FEW_ARGS.getText());
                return false;
            }
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NO_TARDIS.getText());
                return false;
            }
            int id = rs.getTardis_id();
            HashMap<String, Object> whered = new HashMap<String, Object>();
            whered.put("dest_name", args[1]);
            whered.put("tardis_id", id);
            ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
            if (!rsd.resultSet()) {
                TARDISMessage.send(player, plugin.getPluginName() + String.format(MESSAGE.SAVE_NOT_FOUND.getText(), ChatColor.GREEN + "/TARDIS list saves" + ChatColor.RESET));
                return false;
            }
            int destID = rsd.getDest_id();
            QueryFactory qf = new QueryFactory(plugin);
            HashMap<String, Object> did = new HashMap<String, Object>();
            did.put("dest_id", destID);
            qf.doDelete("destinations", did);
            TARDISMessage.send(player, plugin.getPluginName() + "The destination " + args[1] + " was deleted!");
            return true;
        } else {
            TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NO_PERMS.getText());
            return false;
        }
    }
}
