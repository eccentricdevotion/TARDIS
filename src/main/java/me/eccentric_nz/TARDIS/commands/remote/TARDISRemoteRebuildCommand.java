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
package me.eccentric_nz.TARDIS.commands.remote;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISRemoteRebuildCommand {

    private final TARDIS plugin;

    public TARDISRemoteRebuildCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean doRemoteRebuild(CommandSender sender, int id, OfflinePlayer player, boolean cham, boolean hidden) {
        HashMap<String, Object> wherecl = new HashMap<String, Object>();
        wherecl.put("tardis_id", id);
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
        if (!rsc.resultSet()) {
            TARDISMessage.send(sender, "CURRENT_NOT_FOUND");
            return true;
        }
        Location l = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
        HashMap<String, Object> wheret = new HashMap<String, Object>();
        wheret.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false);
        if (rs.resultSet() && rs.getPreset().equals(PRESET.INVISIBLE)) {
            TARDISMessage.send(player.getPlayer(), "INVISIBILITY_ENGAGED");
            return true;
        }
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
        TARDISMessage.send(sender, "TARDIS_REBUILT");
        // set hidden to false
        if (hidden) {
            HashMap<String, Object> whereh = new HashMap<String, Object>();
            whereh.put("tardis_id", id);
            HashMap<String, Object> seth = new HashMap<String, Object>();
            seth.put("hidden", 0);
            new QueryFactory(plugin).doUpdate("tardis", seth, whereh);
        }
        return true;
    }
}
