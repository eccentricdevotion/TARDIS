/*
 * Copyright (C) 2025 eccentric_nz
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
import me.eccentric_nz.TARDIS.TARDISCache;
import me.eccentric_nz.TARDIS.builders.exterior.BuildData;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetBackLocation;
import me.eccentric_nz.TARDIS.destroyers.DestroyData;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISRemoteBackCommand {

    private final TARDIS plugin;

    public TARDISRemoteBackCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean sendBack(CommandSender sender, int id, OfflinePlayer player) {

        // get fast return location
        HashMap<String, Object> wherebl = new HashMap<>();
        wherebl.put("tardis_id", id);
        ResultSetBackLocation rsb = new ResultSetBackLocation(plugin, wherebl);
        if (!rsb.resultSet()) {
            if (sender instanceof Player) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "PREV_NOT_FOUND");
            }
            return true;
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("world", rsb.getWorld().getName());
        set.put("x", rsb.getX());
        set.put("y", rsb.getY());
        set.put("z", rsb.getZ());
        set.put("direction", rsb.getDirection().toString());
        set.put("submarine", (rsb.isSubmarine()) ? 1 : 0);
        // get current police box location
        Current current = TARDISCache.CURRENT.get(id);
        if (current == null) {
            if (sender instanceof Player) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
            }
            return true;
        }
        // set hidden false
        HashMap<String, Object> sett = new HashMap<>();
        sett.put("hidden", 0);
        HashMap<String, Object> ttid = new HashMap<>();
        ttid.put("tardis_id", id);
        plugin.getQueryFactory().doUpdate("tardis", sett, ttid);
        HashMap<String, Object> tid = new HashMap<>();
        tid.put("tardis_id", id);
        plugin.getQueryFactory().doUpdate("current", set, tid);
        TARDISCache.CURRENT.invalidate(id);
        plugin.getTrackerKeeper().getInVortex().add(id);
        if (!plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            // destroy the police box
            DestroyData dd = new DestroyData();
            dd.setDirection(current.direction());
            dd.setLocation(current.location());
            dd.setPlayer(player);
            dd.setHide(false);
            dd.setOutside(true);
            dd.setSubmarine(current.submarine());
            dd.setTardisID(id);
            dd.setThrottle(SpaceTimeThrottle.NORMAL);
            plugin.getTrackerKeeper().getDematerialising().add(id);
            plugin.getPresetDestroyer().destroyPreset(dd);
        }
        // rebuild the police box
        BuildData bd = new BuildData(player.getUniqueId().toString());
        bd.setDirection(rsb.getDirection());
        bd.setLocation(new Location(rsb.getWorld(), rsb.getX(), rsb.getY(), rsb.getZ()));
        bd.setMalfunction(false);
        bd.setOutside(true);
        bd.setPlayer(player);
        bd.setRebuild(false);
        bd.setSubmarine(rsb.isSubmarine());
        bd.setTardisID(id);
        bd.setThrottle(SpaceTimeThrottle.NORMAL);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getPresetBuilder().buildPreset(bd), 20L);
        plugin.getTrackerKeeper().getHasDestination().remove(id);
        plugin.getTrackerKeeper().getRescue().remove(id);
        return true;
    }
}
