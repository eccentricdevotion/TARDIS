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
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISRemoteHideCommand {

    private final TARDIS plugin;

    public TARDISRemoteHideCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean doRemoteHide(CommandSender sender, int id) {
        HashMap<String, Object> wherecl = new HashMap<String, Object>();
        wherecl.put("tardis_id", id);
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
        if (!rsc.resultSet()) {
            sender.sendMessage(plugin.getPluginName() + MESSAGE.NO_CURRENT.getText());
            return true;
        }
        Location l = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
        HashMap<String, Object> wheret = new HashMap<String, Object>();
        wheret.put("tardis_id", id);
        final TARDISMaterialisationData pdd = new TARDISMaterialisationData();
        pdd.setChameleon(false);
        pdd.setDirection(rsc.getDirection());
        pdd.setLocation(l);
        pdd.setDematerialise(false);
        pdd.setPlayer(null);
        pdd.setHide(false);
        pdd.setOutside(false);
        pdd.setSubmarine(rsc.isSubmarine());
        pdd.setTardisID(id);
        plugin.getPresetDestroyer().destroyPreset(pdd);
        sender.sendMessage(plugin.getPluginName() + "The TARDIS Police Box was hidden!");
        // set hidden to true
        HashMap<String, Object> whereh = new HashMap<String, Object>();
        whereh.put("tardis_id", id);
        HashMap<String, Object> seth = new HashMap<String, Object>();
        seth.put("hidden", 1);
        new QueryFactory(plugin).doUpdate("tardis", seth, whereh);
        return true;
    }
}
