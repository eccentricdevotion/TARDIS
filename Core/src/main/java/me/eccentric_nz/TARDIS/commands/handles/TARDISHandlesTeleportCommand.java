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
package me.eccentric_nz.TARDIS.commands.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISCache;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.builders.exterior.BuildData;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.destroyers.DestroyData;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISHandlesTeleportCommand {

    private final TARDIS plugin;

    public TARDISHandlesTeleportCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void beamMeUp(Player player) {
        // set destination to the player's location
        Location location = player.getLocation();
        // must be outside the TARDIS
        if (plugin.getUtils().inTARDISWorld(location)) {
            plugin.getMessenger().handlesSend(player, "TARDIS_OUTSIDE");
            return;
        }
        // get tardis data
        Tardis tardis = TARDISCache.BY_UUID.get(player.getUniqueId());
        if (tardis == null) {
            plugin.getMessenger().handlesSend(player, "NO_TARDIS");
            return;
        }
        int id = tardis.getTardisId();
        if (!tardis.isHandbrakeOn() && !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            plugin.getMessenger().handlesSend(player, "NOT_WHILE_TRAVELLING");
            return;
        }
        int level = tardis.getArtronLevel();
        int travel = plugin.getArtronConfig().getInt("travel");
        if (level < travel) {
            plugin.getMessenger().handlesSend(player, "NOT_ENOUGH_ENERGY");
            return;
        }
        // plugin respect
        if (plugin.getPluginRespect().getRespect(location, new Parameters(player, Flag.getAPIFlags()))) {
            // get direction
            Current current = TARDISCache.CURRENT.get(id);
            if (current == null) {
                plugin.getMessenger().handlesSend(player, "CURRENT_NOT_FOUND");
            }
            int[] start_loc = TARDISTimeTravel.getStartLocation(location, current.direction());
            // check destination has room for TARDIS
            int count = TARDISTimeTravel.safeLocation(start_loc[0], location.getBlockY(), start_loc[2], start_loc[1], start_loc[3], location.getWorld(), current.direction());
            if (count > 0) {
                plugin.getMessenger().handlesSend(player, "RESCUE_NOT_SAFE");
                return;
            }
            // set new current
            HashMap<String, Object> tid = new HashMap<>();
            tid.put("tardis_id", id);
            HashMap<String, Object> set = new HashMap<>();
            set.put("world", location.getWorld().getName());
            set.put("x", location.getBlockX());
            set.put("y", location.getBlockY());
            set.put("z", location.getBlockZ());
            set.put("submarine", (current.submarine()) ? 1 : 0);
            plugin.getQueryFactory().doUpdate("current", set, tid);
            plugin.getTrackerKeeper().getHadsDamage().remove(id);
            long delay = 1L;
            plugin.getTrackerKeeper().getInVortex().add(id);
            UUID uuid = player.getUniqueId();
            // rescue
            plugin.getTrackerKeeper().getRescue().put(id, uuid);
            // destroy TARDIS
            DestroyData dd = new DestroyData();
            dd.setDirection(current.direction());
            dd.setLocation(current.location());
            dd.setPlayer(player);
            dd.setHide(false);
            dd.setOutside(true);
            dd.setSubmarine(current.submarine());
            dd.setTardisID(id);
            dd.setThrottle(SpaceTimeThrottle.NORMAL);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                plugin.getTrackerKeeper().getDematerialising().add(id);
                plugin.getPresetDestroyer().destroyPreset(dd);
            }, delay);
            // move TARDIS
            BuildData bd = new BuildData(uuid.toString());
            bd.setDirection(current.direction());
            bd.setLocation(location);
            bd.setMalfunction(false);
            bd.setOutside(true);
            bd.setPlayer(player);
            bd.setRebuild(false);
            bd.setSubmarine(current.submarine());
            bd.setTardisID(id);
            bd.setThrottle(SpaceTimeThrottle.NORMAL);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getPresetBuilder().buildPreset(bd), delay * 2);
        }
    }
}
