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
public class TARDISRebuildCommand {

    private final TARDIS plugin;

    public TARDISRebuildCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean rebuildPreset(final OfflinePlayer player) {
        if (player.getPlayer().hasPermission("tardis.rebuild")) {
            boolean cham = false;
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                TARDISMessage.send(player.getPlayer(), plugin.getPluginName() + MESSAGE.NO_TARDIS.getText());
                return false;
            }
            int id = rs.getTardis_id();
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
            if (rst.resultSet() && plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                TARDISMessage.send(player.getPlayer(), plugin.getPluginName() + "You cannot rebuild the TARDIS right now! Try travelling first.");
                return true;
            }
            int level = rs.getArtron_level();
            if (plugin.getTrackerKeeper().getInVortex().contains(id)) {
                TARDISMessage.send(player.getPlayer(), plugin.getPluginName() + MESSAGE.NOT_WHILE_MAT.getText());
                return true;
            }
            if (plugin.getConfig().getBoolean("travel.chameleon")) {
                cham = rs.isChamele_on();
            }
            HashMap<String, Object> wherecl = new HashMap<String, Object>();
            wherecl.put("tardis_id", rs.getTardis_id());
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
            if (!rsc.resultSet()) {
                TARDISMessage.send(player.getPlayer(), plugin.getPluginName() + MESSAGE.NO_CURRENT.getText());
                TARDISMessage.send(player.getPlayer(), "Try using the Stattenheim Remote, or the /tardis comehere command.");
                return true;
            }
            Location l = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
            HashMap<String, Object> wheret = new HashMap<String, Object>();
            wheret.put("tardis_id", id);
            QueryFactory qf = new QueryFactory(plugin);
            int rebuild = plugin.getArtronConfig().getInt("random");
            if (level < rebuild) {
                TARDISMessage.send(player.getPlayer(), plugin.getPluginName() + ChatColor.RED + "The TARDIS does not have enough Artron Energy to rebuild!");
                return false;
            }
            // remove the police box first - should fix conflict between wood and iron doors
            final TARDISMaterialisationData pbd = new TARDISMaterialisationData();
            pbd.setChameleon(cham);
            pbd.setDirection(rsc.getDirection());
            pbd.setLocation(l);
            pbd.setMalfunction(false);
            pbd.setOutside(false);
            pbd.setPlayer(player);
            pbd.setRebuild(true);
            pbd.setSubmarine(rsc.isSubmarine());
            pbd.setTardisID(id);
            pbd.setBiome(rsc.getBiome());
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    plugin.getPresetBuilder().buildPreset(pbd);
                }
            }, 10L);
            TARDISMessage.send(player.getPlayer(), plugin.getPluginName() + "The TARDIS Police Box was rebuilt!");
            qf.alterEnergyLevel("tardis", -rebuild, wheret, player.getPlayer());
            // set hidden to false
            if (rs.isHidden()) {
                HashMap<String, Object> whereh = new HashMap<String, Object>();
                whereh.put("tardis_id", id);
                HashMap<String, Object> seth = new HashMap<String, Object>();
                seth.put("hidden", 0);
                qf.doUpdate("tardis", seth, whereh);
            }
            return true;
        } else {
            TARDISMessage.send(player.getPlayer(), plugin.getPluginName() + MESSAGE.NO_PERMS.getText());
            return false;
        }
    }
}
