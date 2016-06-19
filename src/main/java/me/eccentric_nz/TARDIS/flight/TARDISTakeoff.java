/*
 * Copyright (C) 2016 eccentric_nz
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

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.travel.TARDISMalfunction;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.material.Lever;

/**
 *
 * @author eccentric_nz
 */
public class TARDISTakeoff {

    private final TARDIS plugin;

    public TARDISTakeoff(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void run(int id, Block block, Location handbrake, final Player player, boolean beac_on, String beacon, boolean bar, int flight_mode) {
        BlockState state = block.getState();
        Lever lever = (Lever) state.getData();
        lever.setPowered(false);
        state.setData(lever);
        state.update();
        if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getTrackerKeeper().getHasNotClickedHandbrake().contains(id)) {
            plugin.getTrackerKeeper().getHasNotClickedHandbrake().remove(Integer.valueOf(id));
        }
        TARDISSounds.playTARDISSound(handbrake, "tardis_handbrake_release");
        if (!beac_on && !beacon.isEmpty()) {
            toggleBeacon(beacon, true);
        }
        HashMap<String, Object> set = new HashMap<String, Object>();
        set.put("handbrake_on", 0);
        HashMap<String, Object> whereh = new HashMap<String, Object>();
        whereh.put("tardis_id", id);
        new QueryFactory(plugin).doUpdate("tardis", set, whereh);
        TARDISMessage.send(player, "HANDBRAKE_OFF");
        plugin.getTrackerKeeper().getInVortex().add(id);
        // check if we should malfunction
        boolean malfunction = new TARDISMalfunction(plugin).isMalfunction();
        plugin.getTrackerKeeper().getMalfunction().put(id, malfunction);
        // dematerialise
        new TARDISDematerialiseToVortex(plugin, id, player, handbrake).run();
        long delay = 1L;
        if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
            // materialise
            delay = (malfunction) ? 300L : 1L;
            new TARDISMaterialseFromVortex(plugin, id, player, handbrake).run();
        } else {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new TARDISLoopingFlightSound(plugin, handbrake, id), 500L);
        }
        if (bar) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    new TARDISTravelBar(plugin).showTravelRemaining(player, 445L, true);
                }
            }, delay);
        }
    }

    public void run(int id, final Player player, String beacon) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        where.put("type", 0);
        ResultSetControls rs = new ResultSetControls(plugin, where, false);
        if (rs.resultSet()) {
            Location handbrake = plugin.getLocationUtils().getLocationFromBukkitString(rs.getLocation());
            // should the beacon turn on
            HashMap<String, Object> wherek = new HashMap<String, Object>();
            wherek.put("uuid", player.getUniqueId().toString());
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherek);
            boolean beac_on = true;
            boolean bar = false;
            if (rsp.resultSet()) {
                beac_on = rsp.isBeaconOn();
                bar = rsp.isTravelbarOn();
            }
            BlockState state = handbrake.getBlock().getState();
            Lever lever = (Lever) state.getData();
            lever.setPowered(false);
            state.setData(lever);
            state.update();
            if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getTrackerKeeper().getHasNotClickedHandbrake().contains(id)) {
                plugin.getTrackerKeeper().getHasNotClickedHandbrake().remove(Integer.valueOf(id));
            }
            TARDISSounds.playTARDISSound(handbrake, "tardis_handbrake_release");
            if (!beac_on && !beacon.isEmpty()) {
                toggleBeacon(beacon, true);
            }
            HashMap<String, Object> set = new HashMap<String, Object>();
            set.put("handbrake_on", 0);
            HashMap<String, Object> whereh = new HashMap<String, Object>();
            whereh.put("tardis_id", id);
            new QueryFactory(plugin).doUpdate("tardis", set, whereh);
            TARDISMessage.send(player, "HANDBRAKE_OFF");
            plugin.getTrackerKeeper().getInVortex().add(id);
            // check if we should malfunction
            boolean malfunction = new TARDISMalfunction(plugin).isMalfunction();
            plugin.getTrackerKeeper().getMalfunction().put(id, malfunction);
            // dematerialise
            new TARDISDematerialiseToVortex(plugin, id, player, handbrake).run();
            long delay = 1L;
            if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                // materialise
                delay = (malfunction) ? 300L : 1L;
                new TARDISMaterialseFromVortex(plugin, id, player, handbrake).run();
            } else {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new TARDISLoopingFlightSound(plugin, handbrake, id), 500L);
            }
            if (bar) {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        new TARDISTravelBar(plugin).showTravelRemaining(player, 445L, true);
                    }
                }, delay);
            }
        }
    }

    private void toggleBeacon(String str, boolean on) {
        String[] beaconData = str.split(":");
        World w = plugin.getServer().getWorld(beaconData[0]);
        int bx = TARDISNumberParsers.parseInt(beaconData[1]);
        int by = TARDISNumberParsers.parseInt(beaconData[2]);
        int bz = TARDISNumberParsers.parseInt(beaconData[3]);
        Location bl = new Location(w, bx, by, bz);
        Block b = bl.getBlock();
        b.setType((on) ? Material.GLASS : Material.REDSTONE_BLOCK);
    }
}
