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
package me.eccentric_nz.TARDIS.commands.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.flight.TARDISTakeoff;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author eccentric_nz
 */
class TARDISHandlesTakeoffCommand {

    private final TARDIS plugin;

    public TARDISHandlesTakeoffCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void enterVortex(Player player, String[] args) {
        // must be inside tardis
        HashMap<String, Object> whereu = new HashMap<>();
        whereu.put("uuid", args[1]);
        ResultSetTravellers rsv = new ResultSetTravellers(plugin, whereu, false);
        if (rsv.resultSet()) {
            // get TARDIS
            int id = TARDISNumberParsers.parseInt(args[2]);
            HashMap<String, Object> wherei = new HashMap<>();
            wherei.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false, 2);
            if (rs.resultSet()) {
                Tardis tardis = rs.getTardis();
                if (tardis.getPreset().equals(PRESET.JUNK)) {
                    return;
                }
                if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered_on()) {
                    TARDISMessage.handlesSend(player, "POWER_DOWN");
                    return;
                }
                if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getDidDematToVortex().contains(id) || plugin.getTrackerKeeper().getDestinationVortex().containsKey(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                    TARDISMessage.handlesSend(player, "HANDBRAKE_IN_VORTEX");
                    return;
                }
                HashMap<String, Object> whereh = new HashMap<>();
                whereh.put("type", 0);
                whereh.put("tardis_id", id);
                ResultSetControls rsc = new ResultSetControls(plugin, whereh, false);
                if (rsc.resultSet()) {
                    if (tardis.isHandbrake_on()) {
                        // check there is enough power for at last random travel
                        if (!plugin.getTrackerKeeper().getHasDestination().containsKey(id) && tardis.getArtron_level() < plugin.getArtronConfig().getInt("random")) {
                            TARDISMessage.handlesSend(player, "ENERGY_NOT_ENOUGH");
                            return;
                        }
                        // check if door is open
                        if (isDoorOpen(id)) {
                            TARDISMessage.handlesSend(player, "DOOR_CLOSE");
                            // track handbrake clicked for takeoff when door closed
                            plugin.getTrackerKeeper().getHasClickedHandbrake().add(id);
                            // give them 30 seconds to close the door
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getTrackerKeeper().getHasClickedHandbrake().removeAll(Collections.singleton(id)), 600L);
                            return;
                        }
                        Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation());
                        assert location != null;
                        Block handbrake = location.getBlock();
                        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, args[1]);
                        boolean beac_on = true;
                        boolean bar = false;
                        SpaceTimeThrottle spaceTimeThrottle = SpaceTimeThrottle.NORMAL;
                        if (rsp.resultSet()) {
                            beac_on = rsp.isBeaconOn();
                            bar = rsp.isTravelbarOn();
                            spaceTimeThrottle = SpaceTimeThrottle.getByDelay().get(rsp.getThrottle());
                        }
                        new TARDISTakeoff(plugin).run(id, handbrake, location, player, beac_on, tardis.getBeacon(), bar, spaceTimeThrottle);
                    } else {
                        TARDISMessage.handlesSend(player, "HANDBRAKE_OFF_ERR");
                    }
                }
            }
        }
    }

    private boolean isDoorOpen(int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("door_type", 1);
        ResultSetDoors rs = new ResultSetDoors(plugin, where, false);
        if (rs.resultSet()) {
            Openable door = (Openable) Objects.requireNonNull(TARDISStaticLocationGetters.getLocationFromDB(rs.getDoor_location())).getBlock().getBlockData();
            return door.isOpen();
        }
        return false;
    }
}
