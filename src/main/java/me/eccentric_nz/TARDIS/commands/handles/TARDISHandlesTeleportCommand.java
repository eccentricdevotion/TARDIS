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
package me.eccentric_nz.tardis.commands.handles;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.api.Parameters;
import me.eccentric_nz.tardis.builders.BuildData;
import me.eccentric_nz.tardis.database.data.TARDIS;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.destroyers.DestroyData;
import me.eccentric_nz.tardis.enumeration.Flag;
import me.eccentric_nz.tardis.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.travel.TARDISTimeTravel;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISHandlesTeleportCommand {

    private final TARDISPlugin plugin;

    public TARDISHandlesTeleportCommand(TARDISPlugin plugin) {
        this.plugin = plugin;
    }

    public void beamMeUp(Player player) {
        // set destination to the player's location
        Location location = player.getLocation();
        // must be outside the tardis
        if (plugin.getUtils().inTARDISWorld(location)) {
            TARDISMessage.handlesSend(player, "TARDIS_OUTSIDE");
            return;
        }
        // get tardis data
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (!rs.resultSet()) {
            TARDISMessage.handlesSend(player, "NO_TARDIS");
            return;
        }
        TARDIS tardis = rs.getTardis();
        int id = tardis.getTardisId();
        if (!tardis.isHandbrakeOn() && !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            TARDISMessage.handlesSend(player, "NOT_WHILE_TRAVELLING");
            return;
        }
        int level = tardis.getArtronLevel();
        int travel = plugin.getArtronConfig().getInt("travel");
        if (level < travel) {
            TARDISMessage.handlesSend(player, "NOT_ENOUGH_ENERGY");
            return;
        }
        // plugin respect
        if (plugin.getPluginRespect().getRespect(location, new Parameters(player, Flag.getAPIFlags()))) {
            // get direction
            HashMap<String, Object> wherecl = new HashMap<>();
            wherecl.put("tardis_id", id);
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
            if (!rsc.resultSet()) {
                TARDISMessage.handlesSend(player, "CURRENT_NOT_FOUND");
            }
            int[] start_loc = TARDISTimeTravel.getStartLocation(location, rsc.getDirection());
            // check destination has room for tardis
            int count = TARDISTimeTravel.safeLocation(start_loc[0], location.getBlockY(), start_loc[2], start_loc[1], start_loc[3], location.getWorld(), rsc.getDirection());
            if (count > 0) {
                TARDISMessage.handlesSend(player, "RESCUE_NOT_SAFE");
                return;
            }
            // build current location
            Location current = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
            // set new current
            HashMap<String, Object> tid = new HashMap<>();
            tid.put("tardis_id", id);
            HashMap<String, Object> set = new HashMap<>();
            set.put("world", Objects.requireNonNull(location.getWorld()).getName());
            set.put("x", location.getBlockX());
            set.put("y", location.getBlockY());
            set.put("z", location.getBlockZ());
            set.put("submarine", (rsc.isSubmarine()) ? 1 : 0);
            plugin.getQueryFactory().doUpdate("current", set, tid);
            plugin.getTrackerKeeper().getDamage().remove(id);
            long delay = 1L;
            plugin.getTrackerKeeper().getInVortex().add(id);
            UUID uuid = player.getUniqueId();
            // rescue
            plugin.getTrackerKeeper().getRescue().put(id, uuid);
            // destroy tardis
            DestroyData dd = new DestroyData();
            dd.setDirection(rsc.getDirection());
            dd.setLocation(current);
            dd.setPlayer(player);
            dd.setHide(false);
            dd.setOutside(true);
            dd.setSubmarine(rsc.isSubmarine());
            dd.setTardisId(id);
            dd.setThrottle(SpaceTimeThrottle.NORMAL);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                plugin.getTrackerKeeper().getDematerialising().add(id);
                plugin.getPresetDestroyer().destroyPreset(dd);
            }, delay);
            // move tardis
            BuildData bd = new BuildData(uuid.toString());
            bd.setDirection(rsc.getDirection());
            bd.setLocation(location);
            bd.setMalfunction(false);
            bd.setOutside(true);
            bd.setPlayer(player);
            bd.setRebuild(false);
            bd.setSubmarine(rsc.isSubmarine());
            bd.setTardisId(id);
            bd.setThrottle(SpaceTimeThrottle.NORMAL);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getPresetBuilder().buildPreset(bd), delay * 2);
        }
    }
}
