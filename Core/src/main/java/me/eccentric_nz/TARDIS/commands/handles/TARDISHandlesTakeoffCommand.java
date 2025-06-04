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
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.data.Throticle;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.flight.TARDISTakeoff;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISHandlesTakeoffCommand {

    private final TARDIS plugin;

    public TARDISHandlesTakeoffCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean enterVortex(Player player, String[] args) {
        // get TARDIS
        int id = TARDISNumberParsers.parseInt(args[2]);
        HashMap<String, Object> wherei = new HashMap<>();
        wherei.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            if (tardis.getPreset().equals(ChameleonPreset.JUNK)) {
                return true;
            }
            if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPoweredOn()) {
                plugin.getMessenger().handlesSend(player, "POWER_DOWN");
                return true;
            }
            if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getDidDematToVortex().contains(id) || plugin.getTrackerKeeper().getDestinationVortex().containsKey(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                plugin.getMessenger().handlesSend(player, "HANDBRAKE_IN_VORTEX");
                return true;
            }
            HashMap<String, Object> whereh = new HashMap<>();
            whereh.put("type", 0);
            whereh.put("tardis_id", id);
            ResultSetControls rsc = new ResultSetControls(plugin, whereh, false);
            if (rsc.resultSet()) {
                if (tardis.isHandbrakeOn()) {
                    // check there is enough power for at last random travel
                    if (!plugin.getTrackerKeeper().getHasDestination().containsKey(id) && tardis.getArtronLevel() < plugin.getArtronConfig().getInt("random")) {
                        plugin.getMessenger().handlesSend(player, "ENERGY_NOT_ENOUGH");
                        return true;
                    }
                    // check if door is open
                    if (isDoorOpen(id)) {
                        plugin.getMessenger().handlesSend(player, "DOOR_CLOSE");
                        // track handbrake clicked for takeoff when door closed
                        plugin.getTrackerKeeper().getHasClickedHandbrake().add(id);
                        // give them 30 seconds to close the door
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getTrackerKeeper().getHasClickedHandbrake().removeAll(Collections.singleton(id)), 600L);
                        return true;
                    }
                    Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation());
                    Block handbrake = location.getBlock();
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, args[1]);
                    boolean beac_on = true;
                    boolean bar = false;
                    if (rsp.resultSet()) {
                        beac_on = rsp.isBeaconOn();
                        bar = rsp.isTravelbarOn();
                    }
                    Throticle throticle = new ResultSetThrottle(plugin).getSpeedAndParticles(args[1]);
                    new TARDISTakeoff(plugin).run(id, handbrake, location, player, beac_on, tardis.getBeacon(), bar, throticle);
                } else {
                    plugin.getMessenger().handlesSend(player, "HANDBRAKE_OFF_ERR");
                }
            }
        }
        return true;
    }

    private boolean isDoorOpen(int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("door_type", 1);
        ResultSetDoors rs = new ResultSetDoors(plugin, where, false);
        if (rs.resultSet()) {
            Openable door = (Openable) TARDISStaticLocationGetters.getLocationFromDB(rs.getDoor_location()).getBlock().getBlockData();
            return door.isOpen();
        }
        return false;
    }
}
