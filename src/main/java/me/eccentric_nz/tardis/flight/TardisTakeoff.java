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
package me.eccentric_nz.tardis.flight;

import me.eccentric_nz.tardis.TardisConstants;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.api.event.TardisTravelEvent;
import me.eccentric_nz.tardis.database.resultset.ResultSetControls;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.tardis.enumeration.TravelType;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.travel.TardisMalfunction;
import me.eccentric_nz.tardis.utility.TardisSounds;
import me.eccentric_nz.tardis.utility.TardisStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TardisTakeoff {

    private final TardisPlugin plugin;

    public TardisTakeoff(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    public void run(int id, Block block, Location handbrake, Player player, boolean beac_on, String beacon, boolean bar, SpaceTimeThrottle spaceTimeThrottle) {
        // set the handbrake
        TardisHandbrake.setLevers(block, false, true, handbrake.toString(), id, plugin);
        if (plugin.getConfig().getBoolean("circuits.damage")) {
            plugin.getTrackerKeeper().getHasNotClickedHandbrake().remove(id);
        }
        TardisSounds.playTardisSound(handbrake, "tardis_handbrake_release");
        if (!beac_on && !beacon.isEmpty()) {
            toggleBeacon(beacon);
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("handbrake_on", 0);
        HashMap<String, Object> whereh = new HashMap<>();
        whereh.put("tardis_id", id);
        plugin.getQueryFactory().doUpdate("tardis", set, whereh);
        TardisMessage.send(player, "HANDBRAKE_OFF");
        plugin.getTrackerKeeper().getInVortex().add(id);
        // check if we should malfunction
        if (!plugin.getTrackerKeeper().getMalfunction().containsKey(id)) {
            boolean malfunction = new TardisMalfunction(plugin).isMalfunction();
            plugin.getTrackerKeeper().getMalfunction().put(id, malfunction);
        }
        // dematerialise
        new TardisDematerialiseToVortex(plugin, id, player, handbrake).run();
        if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
            plugin.getPluginManager().callEvent(new TardisTravelEvent(player, null, plugin.getTrackerKeeper().getHasDestination().get(id).getTravelType(), id));
            // materialise
            new TardisMaterialiseFromVortex(plugin, id, player, handbrake, spaceTimeThrottle).run();
        } else {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new TardisLoopingFlightSound(plugin, handbrake, id), spaceTimeThrottle.getFlightTime());
            plugin.getPluginManager().callEvent(new TardisTravelEvent(player, null, TravelType.VORTEX, id));
        }
        if (bar) {
            new TardisTravelBar(plugin).showTravelRemaining(player, spaceTimeThrottle.getFlightTime(), true);
        }
    }

    public void run(int id, Player player, String beacon) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("type", 0);
        where.put("secondary", 0);
        ResultSetControls rs = new ResultSetControls(plugin, where, false);
        if (rs.resultSet()) {
            Location handbrake = TardisStaticLocationGetters.getLocationFromBukkitString(rs.getLocation());
            // should the beacon turn on
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
            boolean beac_on = true;
            boolean bar = false;
            SpaceTimeThrottle spaceTimeThrottle = SpaceTimeThrottle.NORMAL;
            if (rsp.resultSet()) {
                beac_on = rsp.isBeaconOn();
                bar = rsp.isTravelBarOn();
                spaceTimeThrottle = SpaceTimeThrottle.getByDelay().get(rsp.getThrottle());
            }
            // set the handbrake
            assert handbrake != null;
            TardisHandbrake.setLevers(handbrake.getBlock(), false, true, rs.getLocation(), rs.getTardisId(), plugin);
            if (plugin.getConfig().getBoolean("circuits.damage")) {
                plugin.getTrackerKeeper().getHasNotClickedHandbrake().remove(id);
            }
            TardisSounds.playTardisSound(handbrake, "tardis_handbrake_release");
            if (!beac_on && !beacon.isEmpty()) {
                toggleBeacon(beacon);
            }
            HashMap<String, Object> set = new HashMap<>();
            set.put("handbrake_on", 0);
            HashMap<String, Object> whereh = new HashMap<>();
            whereh.put("tardis_id", id);
            plugin.getQueryFactory().doUpdate("tardis", set, whereh);
            TardisMessage.send(player, "HANDBRAKE_OFF");
            plugin.getTrackerKeeper().getInVortex().add(id);
            // check if we should malfunction
            boolean malfunction = new TardisMalfunction(plugin).isMalfunction();
            plugin.getTrackerKeeper().getMalfunction().put(id, malfunction);
            // dematerialise
            new TardisDematerialiseToVortex(plugin, id, player, handbrake).run();
            if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                // materialise
                new TardisMaterialiseFromVortex(plugin, id, player, handbrake, spaceTimeThrottle).run();
            } else {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new TardisLoopingFlightSound(plugin, handbrake, id), spaceTimeThrottle.getFlightTime());
            }
            if (bar) {
                new TardisTravelBar(plugin).showTravelRemaining(player, spaceTimeThrottle.getFlightTime(), true);
            }
        }
    }

    private void toggleBeacon(String str) {
        Location bl = TardisStaticLocationGetters.getLocationFromDB(str);
        assert bl != null;
        Block b = bl.getBlock();
        b.setBlockData(TardisConstants.GLASS);
    }
}