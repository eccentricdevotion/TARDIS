/*
 * Copyright (C) 2021 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisPreset;
import me.eccentric_nz.TARDIS.destroyers.DestroyData;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.planets.TARDISBiome;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISRemoteHideCommand {

    private final TARDIS plugin;

    public TARDISRemoteHideCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean doRemoteHide(CommandSender sender, int id) {
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            TARDISMessage.send(sender, "NOT_IN_VORTEX");
            return true;
        }
        HashMap<String, Object> wherecl = new HashMap<>();
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
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
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
        UUID uuid = olp.getUniqueId();
        DestroyData dd = new DestroyData();
        dd.setDirection(rsc.getDirection());
        dd.setLocation(l);
        dd.setPlayer(olp);
        dd.setHide(false);
        dd.setOutside(false);
        dd.setSubmarine(rsc.isSubmarine());
        dd.setTardisID(id);
        dd.setTardisBiome(TARDISBiome.get(rsc.getBiomeKey()));
        dd.setThrottle(SpaceTimeThrottle.REBUILD);
        plugin.getPresetDestroyer().destroyPreset(dd);
        TARDISMessage.send(sender, "TARDIS_HIDDEN", "/tardisremote [player] rebuild");
        // set hidden to true
        HashMap<String, Object> whereh = new HashMap<>();
        whereh.put("tardis_id", id);
        HashMap<String, Object> seth = new HashMap<>();
        seth.put("hidden", 1);
        plugin.getQueryFactory().doUpdate("tardis", seth, whereh);
        // turn force field off
        if (plugin.getTrackerKeeper().getActiveForceFields().containsKey(uuid)) {
            plugin.getTrackerKeeper().getActiveForceFields().remove(uuid);
            TARDISMessage.send(sender, "FORCE_FIELD", "OFF");
        }
        return true;
    }
}
