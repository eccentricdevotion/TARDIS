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
import me.eccentric_nz.TARDIS.database.ResultSetTardisPreset;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
            TARDISMessage.send(sender, "CURRENT_NOT_FOUND");
            return true;
        }
        OfflinePlayer olp = null;
        if (sender instanceof Player) {
            olp = (OfflinePlayer) sender;
        } else {
            // get tardis owner
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (rs.resultSet()) {
                olp = plugin.getServer().getOfflinePlayer(rs.getTardis().getUuid());
            }
        }
        Location l = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
        ResultSetTardisPreset rs = new ResultSetTardisPreset(plugin);
        if (rs.fromID(id) && rs.getPreset().equals(PRESET.INVISIBLE) && olp != null) {
            TARDISMessage.send(olp.getPlayer(), "INVISIBILITY_ENGAGED");
            return true;
        }
        final TARDISMaterialisationData pdd = new TARDISMaterialisationData(plugin, olp.getUniqueId().toString());
        pdd.setChameleon(false);
        pdd.setDirection(rsc.getDirection());
        pdd.setLocation(l);
        pdd.setDematerialise(false);
        pdd.setPlayer(olp);
        pdd.setHide(false);
        pdd.setOutside(false);
        pdd.setSubmarine(rsc.isSubmarine());
        pdd.setTardisID(id);
        pdd.setBiome(rsc.getBiome());
        plugin.getPresetDestroyer().destroyPreset(pdd);
        TARDISMessage.send(sender, "TARDIS_HIDDEN", "/tardisremote [player] rebuild");
        // set hidden to true
        HashMap<String, Object> whereh = new HashMap<String, Object>();
        whereh.put("tardis_id", id);
        HashMap<String, Object> seth = new HashMap<String, Object>();
        seth.put("hidden", 1);
        new QueryFactory(plugin).doUpdate("tardis", seth, whereh);
        return true;
    }
}
