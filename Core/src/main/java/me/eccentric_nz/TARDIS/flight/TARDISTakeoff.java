/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.flight;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.database.data.Throticle;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetThrottle;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.sensor.BeaconSensor;
import me.eccentric_nz.TARDIS.sensor.HandbrakeSensor;
import me.eccentric_nz.TARDIS.travel.TARDISMalfunction;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISTakeoff {

    private final TARDIS plugin;

    public TARDISTakeoff(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void run(int id, Block block, Location handbrake, Player player, boolean beac_on, String beacon, boolean bar, Throticle throticle) {
        if (block != null) {
            // set the handbrake
            TARDISHandbrake.setLevers(block, false, true, handbrake.toString(), id, plugin);
        }
        if (plugin.getConfig().getBoolean("circuits.damage")) {
            plugin.getTrackerKeeper().getHasNotClickedHandbrake().remove(id);
        }
        TARDISSounds.playTARDISSound(handbrake, "tardis_handbrake_release");
        if (!beac_on && !beacon.isEmpty()) {
            new BeaconSensor().toggle(beacon, true);
        }
        new HandbrakeSensor(plugin, id).toggle();
        HashMap<String, Object> set = new HashMap<>();
        set.put("handbrake_on", 0);
        HashMap<String, Object> whereh = new HashMap<>();
        whereh.put("tardis_id", id);
        plugin.getQueryFactory().doUpdate("tardis", set, whereh);
        plugin.getMessenger().sendStatus(player, "HANDBRAKE_OFF");
        plugin.getTrackerKeeper().getInVortex().add(id);
        // check if we should malfunction
        if (!plugin.getTrackerKeeper().getMalfunction().containsKey(id)) {
            boolean malfunction = new TARDISMalfunction(plugin).isMalfunction();
            plugin.getTrackerKeeper().getMalfunction().put(id, malfunction);
        }
        // dematerialise
        new TARDISDematerialiseToVortex(plugin, id, player, handbrake, throticle).run();
        if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
            plugin.getPM().callEvent(new TARDISTravelEvent(player, null, plugin.getTrackerKeeper().getHasDestination().get(id).getTravelType(), id));
            // materialise
            new TARDISMaterialseFromVortex(plugin, id, player, handbrake, throticle).run();
        } else {
            if (!plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new TARDISLoopingFlightSound(plugin, handbrake, id), throticle.getThrottle().getFlightTime());
            }
            plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.VORTEX, id));
        }
        if (bar) {
            new TARDISTravelBar(plugin, id).showTravelRemaining(player, throticle.getThrottle().getFlightTime(), true);
        }
    }

    public void run(int id, Player player, String beacon) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("type", 0);
        where.put("secondary", 0);
        ResultSetControls rs = new ResultSetControls(plugin, where, false);
        if (rs.resultSet()) {
            Location handbrake = TARDISStaticLocationGetters.getLocationFromBukkitString(rs.getLocation());
            // should the beacon turn on
            String uuid = player.getUniqueId().toString();
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
            boolean beac_on = true;
            boolean bar = false;
            if (rsp.resultSet()) {
                beac_on = rsp.isBeaconOn();
                bar = rsp.isTravelbarOn();
            }
            Throticle throticle = new ResultSetThrottle(plugin).getSpeedAndParticles(uuid);
            // set the handbrake
            TARDISHandbrake.setLevers(handbrake.getBlock(), false, true, rs.getLocation(), rs.getTardis_id(), plugin);
            if (plugin.getConfig().getBoolean("circuits.damage")) {
                plugin.getTrackerKeeper().getHasNotClickedHandbrake().remove(id);
            }
            TARDISSounds.playTARDISSound(handbrake, "tardis_handbrake_release");
            if (!beac_on && !beacon.isEmpty()) {
                new BeaconSensor().toggle(beacon, true);
            }
            new HandbrakeSensor(plugin, id).toggle();
            HashMap<String, Object> set = new HashMap<>();
            set.put("handbrake_on", 0);
            HashMap<String, Object> whereh = new HashMap<>();
            whereh.put("tardis_id", id);
            plugin.getQueryFactory().doUpdate("tardis", set, whereh);
            plugin.getMessenger().sendStatus(player, "HANDBRAKE_OFF");
            plugin.getTrackerKeeper().getInVortex().add(id);
            // check if we should malfunction
            boolean malfunction = new TARDISMalfunction(plugin).isMalfunction();
            plugin.getTrackerKeeper().getMalfunction().put(id, malfunction);
            // dematerialise
            new TARDISDematerialiseToVortex(plugin, id, player, handbrake, throticle).run();
            if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                // materialise
                new TARDISMaterialseFromVortex(plugin, id, player, handbrake, throticle).run();
            } else {
                if (!plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new TARDISLoopingFlightSound(plugin, handbrake, id), throticle.getThrottle().getFlightTime());
                }
            }
            if (bar) {
                new TARDISTravelBar(plugin, id).showTravelRemaining(player, throticle.getThrottle().getFlightTime(), true);
            }
        }
    }
}
