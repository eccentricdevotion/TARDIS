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
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

/**
 *
 * @author eccentric_nz
 */
public class TARDISHideCommand {

    private final TARDIS plugin;

    public TARDISHideCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean hide(OfflinePlayer player) {
        if (player.getPlayer().hasPermission("tardis.rebuild")) {
            int id;
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                TARDISMessage.send(player.getPlayer(), plugin.getPluginName() + MESSAGE.NO_TARDIS.getText());
                return false;
            }
            id = rs.getTardis_id();
            TARDISCircuitChecker tcc = null;
            if (plugin.getConfig().getString("preferences.difficulty").equals("hard")) {
                tcc = new TARDISCircuitChecker(plugin, id);
                tcc.getCircuits();
            }
            if (tcc != null && !tcc.hasMaterialisation()) {
                TARDISMessage.send(player.getPlayer(), plugin.getPluginName() + MESSAGE.NO_MAT_CIRCUIT.getText());
                return true;
            }
            HashMap<String, Object> wherein = new HashMap<String, Object>();
            wherein.put("uuid", player.getUniqueId().toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wherein, false);
            if (rst.resultSet() && plugin.getTrackerKeeper().getTrackHasDestination().containsKey(id)) {
                TARDISMessage.send(player.getPlayer(), plugin.getPluginName() + "You cannot rebuild the TARDIS right now! Try travelling first.");
                return true;
            }
            int level = rs.getArtron_level();
            if (plugin.getTrackerKeeper().getTrackInVortex().contains(id)) {
                TARDISMessage.send(player.getPlayer(), plugin.getPluginName() + MESSAGE.NOT_WHILE_MAT.getText());
                return true;
            }
            HashMap<String, Object> wherecl = new HashMap<String, Object>();
            wherecl.put("tardis_id", rs.getTardis_id());
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
            if (!rsc.resultSet()) {
                TARDISMessage.send(player.getPlayer(), plugin.getPluginName() + MESSAGE.NO_CURRENT.getText());
                return true;
            }
            Location l = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
            HashMap<String, Object> wheret = new HashMap<String, Object>();
            wheret.put("tardis_id", id);
            QueryFactory qf = new QueryFactory(plugin);
            int hide = plugin.getArtronConfig().getInt("hide");
            if (level < hide) {
                TARDISMessage.send(player.getPlayer(), plugin.getPluginName() + ChatColor.RED + "The TARDIS does not have enough Artron Energy to hide!");
                return false;
            }
            final TARDISMaterialisationData pdd = new TARDISMaterialisationData();
            pdd.setChameleon(false);
            pdd.setDirection(rsc.getDirection());
            pdd.setLocation(l);
            pdd.setDematerialise(false);
            pdd.setPlayer(player.getPlayer());
            pdd.setHide(false);
            pdd.setOutside(false);
            pdd.setSubmarine(rsc.isSubmarine());
            pdd.setTardisID(id);
            pdd.setBiome(rsc.getBiome());
            plugin.getPresetDestroyer().destroyPreset(pdd);
            TARDISMessage.send(player.getPlayer(), plugin.getPluginName() + "The TARDIS Police Box was hidden! Use " + ChatColor.GREEN + " /tardis rebuild " + ChatColor.RESET + " to show it again.");
            qf.alterEnergyLevel("tardis", -hide, wheret, player.getPlayer());
            // set hidden to true
            HashMap<String, Object> whereh = new HashMap<String, Object>();
            whereh.put("tardis_id", id);
            HashMap<String, Object> seth = new HashMap<String, Object>();
            seth.put("hidden", 1);
            qf.doUpdate("tardis", seth, whereh);
            return true;
        } else {
            TARDISMessage.send(player.getPlayer(), plugin.getPluginName() + MESSAGE.NO_PERMS.getText());
            return false;
        }
    }
}
