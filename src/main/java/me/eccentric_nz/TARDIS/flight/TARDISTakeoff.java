/*
 * Copyright (C) 2018 eccentric_nz
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
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.travel.TARDISMalfunction;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class TARDISTakeoff {

    private final TARDIS plugin;

    public TARDISTakeoff(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void run(int id, Block block, Location handbrake, Player player, boolean beac_on, String beacon, boolean bar) {
        Switch lever = (Switch) block.getBlockData();
        lever.setPowered(false);
        block.setBlockData(lever);
        if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getTrackerKeeper().getHasNotClickedHandbrake().contains(id)) {
            plugin.getTrackerKeeper().getHasNotClickedHandbrake().remove(Integer.valueOf(id));
        }
        TARDISSounds.playTARDISSound(handbrake, "tardis_handbrake_release");
        if (!beac_on && !beacon.isEmpty()) {
            toggleBeacon(beacon);
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("handbrake_on", 0);
        HashMap<String, Object> whereh = new HashMap<>();
        whereh.put("tardis_id", id);
        new QueryFactory(plugin).doUpdate("tardis", set, whereh);
        TARDISMessage.send(player, "HANDBRAKE_OFF");
        plugin.getTrackerKeeper().getInVortex().add(id);
        // check if we should malfunction
        if (!plugin.getTrackerKeeper().getMalfunction().containsKey(id)) {
            boolean malfunction = new TARDISMalfunction(plugin).isMalfunction();
            plugin.getTrackerKeeper().getMalfunction().put(id, malfunction);
        }
        // dematerialise
        new TARDISDematerialiseToVortex(plugin, id, player, handbrake).run();
        if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
            // materialise
            new TARDISMaterialseFromVortex(plugin, id, player, handbrake).run();
        } else {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new TARDISLoopingFlightSound(plugin, handbrake, id), 500L);
        }
        if (bar) {
            new TARDISTravelBar(plugin).showTravelRemaining(player, 500L, true);
        }
    }

    public void run(int id, Player player, String beacon) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("type", 0);
        ResultSetControls rs = new ResultSetControls(plugin, where, false);
        if (rs.resultSet()) {
            Location handbrake = plugin.getLocationUtils().getLocationFromBukkitString(rs.getLocation());
            // should the beacon turn on
            HashMap<String, Object> wherek = new HashMap<>();
            wherek.put("uuid", player.getUniqueId().toString());
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherek);
            boolean beac_on = true;
            boolean bar = false;
            if (rsp.resultSet()) {
                beac_on = rsp.isBeaconOn();
                bar = rsp.isTravelbarOn();
            }
            Switch lever = (Switch) handbrake.getBlock().getBlockData();
            lever.setPowered(false);
            handbrake.getBlock().setBlockData(lever);
            if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getTrackerKeeper().getHasNotClickedHandbrake().contains(id)) {
                plugin.getTrackerKeeper().getHasNotClickedHandbrake().remove(Integer.valueOf(id));
            }
            TARDISSounds.playTARDISSound(handbrake, "tardis_handbrake_release");
            if (!beac_on && !beacon.isEmpty()) {
                toggleBeacon(beacon);
            }
            HashMap<String, Object> set = new HashMap<>();
            set.put("handbrake_on", 0);
            HashMap<String, Object> whereh = new HashMap<>();
            whereh.put("tardis_id", id);
            new QueryFactory(plugin).doUpdate("tardis", set, whereh);
            TARDISMessage.send(player, "HANDBRAKE_OFF");
            plugin.getTrackerKeeper().getInVortex().add(id);
            // check if we should malfunction
            boolean malfunction = new TARDISMalfunction(plugin).isMalfunction();
            plugin.getTrackerKeeper().getMalfunction().put(id, malfunction);
            // dematerialise
            new TARDISDematerialiseToVortex(plugin, id, player, handbrake).run();
            if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                // materialise
                new TARDISMaterialseFromVortex(plugin, id, player, handbrake).run();
            } else {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new TARDISLoopingFlightSound(plugin, handbrake, id), 500L);
            }
            if (bar) {
                new TARDISTravelBar(plugin).showTravelRemaining(player, 500L, true);
            }
        }
    }

    private void toggleBeacon(String str) {
        String[] beaconData = str.split(":");
        String world = (beaconData[0].equals("TARDIS_TimeVortex") ? "tardis_time_vortex" : beaconData[0].toLowerCase(Locale.ENGLISH));
        World w = plugin.getServer().getWorld(world);
        int bx = TARDISNumberParsers.parseInt(beaconData[1]);
        int by = TARDISNumberParsers.parseInt(beaconData[2]);
        int bz = TARDISNumberParsers.parseInt(beaconData[3]);
        Location bl = new Location(w, bx, by, bz);
        Block b = bl.getBlock();
        b.setBlockData(TARDISConstants.GLASS);
    }
}
