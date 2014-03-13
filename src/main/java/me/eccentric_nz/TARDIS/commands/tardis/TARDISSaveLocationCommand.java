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
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSaveLocationCommand {

    private final TARDIS plugin;

    public TARDISSaveLocationCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean doSave(Player player, String[] args) {
        if (player.hasPermission("tardis.save")) {
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("owner", player.getName());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NO_TARDIS.getText());
                return false;
            }
            if (args.length < 2) {
                TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.TOO_FEW_ARGS.getText());
                return false;
            }
            if (!args[1].matches("[A-Za-z0-9_]{2,16}")) {
                TARDISMessage.send(player, plugin.getPluginName() + "That doesn't appear to be a valid save name (it may be too long or contain spaces).");
                return false;
            } else {
                int id = rs.getTardis_id();
                TARDISCircuitChecker tcc = null;
                if (plugin.getConfig().getString("preferences.difficulty").equals("hard")) {
                    tcc = new TARDISCircuitChecker(plugin, id);
                    tcc.getCircuits();
                }
                if (tcc != null && !tcc.hasMemory()) {
                    TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NO_MEM_CIRCUIT.getText());
                    return true;
                }
                // check has unique name
                HashMap<String, Object> wherename = new HashMap<String, Object>();
                wherename.put("tardis_id", id);
                wherename.put("dest_name", args[1]);
                wherename.put("type", 0);
                ResultSetDestinations rsd = new ResultSetDestinations(plugin, wherename, false);
                if (rsd.resultSet()) {
                    TARDISMessage.send(player, plugin.getPluginName() + "You already have a save with that name!");
                    return true;
                }
                // get current destination
                HashMap<String, Object> wherecl = new HashMap<String, Object>();
                wherecl.put("tardis_id", rs.getTardis_id());
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                if (!rsc.resultSet()) {
                    TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NO_CURRENT.getText());
                    return true;
                }
                QueryFactory qf = new QueryFactory(plugin);
                HashMap<String, Object> set = new HashMap<String, Object>();
                set.put("tardis_id", id);
                set.put("dest_name", args[1]);
                set.put("world", rsc.getWorld().getName());
                set.put("x", rsc.getX());
                set.put("y", rsc.getY());
                set.put("z", rsc.getZ());
                set.put("direction", rsc.getDirection().toString());
                set.put("submarine", (rsc.isSubmarine()) ? 1 : 0);
                if (qf.doSyncInsert("destinations", set) < 0) {
                    return false;
                } else {
                    TARDISMessage.send(player, plugin.getPluginName() + "The location '" + args[1] + "' was saved successfully.");
                    return true;
                }
            }
        } else {
            TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NO_PERMS.getText());
            return false;
        }
    }
}
