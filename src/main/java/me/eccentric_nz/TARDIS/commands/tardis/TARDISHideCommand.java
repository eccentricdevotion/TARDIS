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
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISHideCommand {

    private final TARDIS plugin;

    public TARDISHideCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean hide(Player player, String[] args) {
        if (player.hasPermission("tardis.rebuild")) {
            World w;
            int id;
            boolean cham = false;
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("owner", player.getName());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                player.sendMessage(plugin.getPluginName() + MESSAGE.NO_TARDIS.getText());
                return false;
            }
            id = rs.getTardis_id();
            TARDISCircuitChecker tcc = null;
            if (plugin.getConfig().getString("preferences.difficulty").equals("hard")) {
                tcc = new TARDISCircuitChecker(plugin, id);
                tcc.getCircuits();
            }
            if (tcc != null && !tcc.hasMaterialisation()) {
                player.sendMessage(plugin.getPluginName() + MESSAGE.NO_MAT_CIRCUIT.getText());
                return true;
            }
            HashMap<String, Object> wherein = new HashMap<String, Object>();
            wherein.put("player", player.getName());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wherein, false);
            if (rst.resultSet() && args[0].equalsIgnoreCase("rebuild") && plugin.getTrackerKeeper().getTrackHasDestination().containsKey(id)) {
                player.sendMessage(plugin.getPluginName() + "You cannot rebuild the TARDIS right now! Try travelling first.");
                return true;
            }
            int level = rs.getArtron_level();
            if (plugin.getTrackerKeeper().getTrackInVortex().contains(Integer.valueOf(id))) {
                player.sendMessage(plugin.getPluginName() + MESSAGE.NOT_WHILE_MAT.getText());
                return true;
            }
            if (plugin.getConfig().getBoolean("travel.chameleon")) {
                cham = rs.isChamele_on();
            }
            HashMap<String, Object> wherecl = new HashMap<String, Object>();
            wherecl.put("tardis_id", rs.getTardis_id());
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
            if (!rsc.resultSet()) {
                player.sendMessage(plugin.getPluginName() + MESSAGE.NO_CURRENT.getText());
                return true;
            }
            Location l = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
            HashMap<String, Object> wheret = new HashMap<String, Object>();
            wheret.put("tardis_id", id);
            QueryFactory qf = new QueryFactory(plugin);
            int hide = plugin.getArtronConfig().getInt("hide");
            if (level < hide) {
                player.sendMessage(plugin.getPluginName() + ChatColor.RED + "The TARDIS does not have enough Artron Energy to hide!");
                return false;
            }
            plugin.getPresetDestroyer().destroyPreset(l, rsc.getDirection(), id, true, false, false, null, rsc.isSubmarine());
            player.sendMessage(plugin.getPluginName() + "The TARDIS Police Box was hidden! Use " + ChatColor.GREEN + "/tardis rebuild" + ChatColor.RESET + " to show it again.");
            qf.alterEnergyLevel("tardis", -hide, wheret, player);
            // set hidden to true
            HashMap<String, Object> whereh = new HashMap<String, Object>();
            whereh.put("tardis_id", id);
            HashMap<String, Object> seth = new HashMap<String, Object>();
            seth.put("hidden", 1);
            qf.doUpdate("tardis", seth, whereh);
            return true;
        } else {
            player.sendMessage(plugin.getPluginName() + MESSAGE.NO_PERMS.getText());
            return false;
        }
    }
}
