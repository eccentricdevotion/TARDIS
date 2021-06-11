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
package me.eccentric_nz.tardis.commands.remote;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.builders.BuildData;
import me.eccentric_nz.tardis.database.data.TARDIS;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetNextLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.destroyers.DestroyData;
import me.eccentric_nz.tardis.enumeration.COMPASS;
import me.eccentric_nz.tardis.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISRemoteTravelCommand {

    private final TARDISPlugin plugin;

    TARDISRemoteTravelCommand(TARDISPlugin plugin) {
        this.plugin = plugin;
    }

    boolean doTravel(int id, OfflinePlayer player, CommandSender sender) {
        HashMap<String, Object> wherei = new HashMap<>();
        wherei.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false, 2);
        if (rs.resultSet()) {
            TARDIS tardis = rs.getTardis();
            boolean hidden = tardis.isHidden();
            HashMap<String, Object> wherecl = new HashMap<>();
            wherecl.put("tardis_id", id);
            ResultSetCurrentLocation rscl = new ResultSetCurrentLocation(plugin, wherecl);
            String resetw = "";
            Location l = null;
            if (!rscl.resultSet()) {
                hidden = true;
            } else {
                resetw = rscl.getWorld().getName();
                l = new Location(rscl.getWorld(), rscl.getX(), rscl.getY(), rscl.getZ());
            }
            COMPASS cd = rscl.getDirection();
            boolean sub = rscl.isSubmarine();
            HashMap<String, Object> wherenl = new HashMap<>();
            wherenl.put("tardis_id", id);
            ResultSetNextLocation rsn = new ResultSetNextLocation(plugin, wherenl);
            if (!rsn.resultSet() && !(sender instanceof BlockCommandSender)) {
                TARDISMessage.send(sender, "DEST_NO_LOAD");
                return true;
            }
            boolean is_next_sub = rsn.isSubmarine();
            Location exit = new Location(rsn.getWorld(), rsn.getX(), rsn.getY(), rsn.getZ());
            COMPASS sd = rsn.getDirection();
            // Removes Blue Box and loads chunk if it unloaded somehow
            if (!exit.getWorld().isChunkLoaded(exit.getChunk())) {
                exit.getWorld().loadChunk(exit.getChunk());
            }
            HashMap<String, Object> set = new HashMap<>();
            if (!plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                plugin.getTrackerKeeper().getInVortex().add(id);
                DestroyData dd = new DestroyData();
                dd.setDirection(cd);
                dd.setLocation(l);
                dd.setPlayer(player);
                dd.setHide(false);
                dd.setOutside(false);
                dd.setSubmarine(sub);
                dd.setTardisId(id);
                dd.setThrottle(SpaceTimeThrottle.NORMAL);
                if (!hidden && !plugin.getTrackerKeeper().getReset().contains(resetw)) {
                    plugin.getTrackerKeeper().getDematerialising().add(id);
                    plugin.getPresetDestroyer().destroyPreset(dd);
                } else {
                    // set hidden false!
                    set.put("hidden", 0);
                    plugin.getPresetDestroyer().removeBlockProtection(id);
                }
            }
            long delay = (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) ? 1L : 500L;
            BuildData bd = new BuildData(player.getUniqueId().toString());
            bd.setDirection(sd);
            bd.setLocation(exit);
            bd.setMalfunction(false);
            bd.setOutside(false);
            bd.setPlayer(player);
            bd.setRebuild(false);
            bd.setSubmarine(is_next_sub);
            bd.setTardisId(id);
            bd.setThrottle(SpaceTimeThrottle.NORMAL);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                plugin.getPresetBuilder().buildPreset(bd);
                TARDISSounds.playTARDISSound(bd.getLocation(), "tardis_land");
            }, delay);
            plugin.getTrackerKeeper().getDamage().remove(id);
            // current
            HashMap<String, Object> setcurrent = new HashMap<>();
            setcurrent.put("world", exit.getWorld().getName());
            setcurrent.put("x", exit.getBlockX());
            setcurrent.put("y", exit.getBlockY());
            setcurrent.put("z", exit.getBlockZ());
            setcurrent.put("direction", sd.toString());
            setcurrent.put("submarine", (is_next_sub) ? 1 : 0);
            HashMap<String, Object> wherecurrent = new HashMap<>();
            wherecurrent.put("tardis_id", id);
            // get current location for back
            HashMap<String, Object> wherecu = new HashMap<>();
            wherecu.put("tardis_id", id);
            ResultSetCurrentLocation rscu = new ResultSetCurrentLocation(plugin, wherecu);
            HashMap<String, Object> setback = new HashMap<>();
            if (!rscu.resultSet()) {
                // back
                setback.put("world", exit.getWorld().getName());
                setback.put("x", exit.getX());
                setback.put("y", exit.getY());
                setback.put("z", exit.getZ());
                setback.put("direction", exit.getDirection().toString());
                setback.put("submarine", (is_next_sub) ? 1 : 0);
            } else {
                // back
                setback.put("world", rscu.getWorld().getName());
                setback.put("x", rscu.getX());
                setback.put("y", rscu.getY());
                setback.put("z", rscu.getZ());
                setback.put("direction", rscu.getDirection().toString());
                setback.put("submarine", (rscu.isSubmarine()) ? 1 : 0);
            }
            HashMap<String, Object> whereback = new HashMap<>();
            whereback.put("tardis_id", id);
            // update Police Box door direction
            HashMap<String, Object> setdoor = new HashMap<>();
            setdoor.put("door_direction", sd.toString());
            HashMap<String, Object> wheredoor = new HashMap<>();
            wheredoor.put("tardis_id", id);
            wheredoor.put("door_type", 0);
            HashMap<String, Object> whereh = new HashMap<>();
            whereh.put("tardis_id", id);
            if (set.size() > 0) {
                plugin.getQueryFactory().doUpdate("tardis", set, whereh);
            }
            plugin.getQueryFactory().doUpdate("current", setcurrent, wherecurrent);
            plugin.getQueryFactory().doUpdate("back", setback, whereback);
            plugin.getQueryFactory().doUpdate("doors", setdoor, wheredoor);
            return true;
        }
        return false;
    }
}
