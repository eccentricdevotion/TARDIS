/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.builders.TARDISTimeRotor;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.flight.TARDISExteriorFlight;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISTravelStop {

    private final TARDIS plugin;

    public TARDISTravelStop(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean action(Player player, int id) {
        if (!plugin.getTrackerKeeper().getMaterialising().contains(id) && !plugin.getTrackerKeeper().getInVortex().contains(id) && !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_TRAVELLING");
            return true;
        }
        // remove trackers
        plugin.getTrackerKeeper().getMaterialising().removeAll(Collections.singleton(id));
        plugin.getTrackerKeeper().getInVortex().removeAll(Collections.singleton(id));
        plugin.getTrackerKeeper().getHadsDamage().remove(id);
        plugin.getTrackerKeeper().getMalfunction().remove(id);
        if (plugin.getTrackerKeeper().getDidDematToVortex().contains(id)) {
            plugin.getTrackerKeeper().getDidDematToVortex().removeAll(Collections.singleton(id));
        }
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            int taskID = plugin.getTrackerKeeper().getDestinationVortex().get(id);
            plugin.getServer().getScheduler().cancelTask(taskID);
            plugin.getTrackerKeeper().getDestinationVortex().remove(id);
        }
        // is player flying?
        if (plugin.getTrackerKeeper().getFlyingReturnLocation().containsKey(player.getUniqueId())) {
            // land TARDIS
            Entity stand = player.getVehicle();
            if (stand != null && stand.getType() == EntityType.ARMOR_STAND) {
                Entity chicken = stand.getVehicle();
                if (chicken != null) {
                    chicken.setVelocity(new Vector(0, 0, 0));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> {
                        // kill chicken
                        chicken.removePassenger(stand);
                        chicken.remove();
                        // teleport player back to the TARDIS interior
                        new TARDISExteriorFlight(TARDIS.plugin).stopFlying(player, (ArmorStand) stand);
                    });
                }
            } else {
                // scan for nearby chickens in case player teleport fails due to lag
                for (Entity e : player.getLocation().getWorld().getNearbyEntities(player.getLocation(), 4, 4, 4, (s) -> s.getType() == EntityType.CHICKEN)) {
                    if (!e.getPassengers().isEmpty() && e.getPassengers().get(0) instanceof ArmorStand armorStand) {
                        e.setVelocity(new Vector(0, 0, 0));
                        Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> {
                            // kill chicken
                            e.removePassenger(armorStand);
                            e.remove();
                            // teleport player back to the TARDIS interior
                            new TARDISExteriorFlight(TARDIS.plugin).stopFlying(player, armorStand);
                        });
                    }
                }
            }
        } else {
            // get home location
            HashMap<String, Object> wherehl = new HashMap<>();
            wherehl.put("tardis_id", id);
            ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, wherehl);
            if (!rsh.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "HOME_NOT_FOUND");
                return true;
            }
            // update current, next and back tables
            HashMap<String, Object> setlocs = new HashMap<>();
            setlocs.put("world", rsh.getWorld().getName());
            setlocs.put("x", rsh.getX());
            setlocs.put("y", rsh.getY());
            setlocs.put("z", rsh.getZ());
            setlocs.put("direction", rsh.getDirection().toString());
            setlocs.put("submarine", (rsh.isSubmarine()) ? 1 : 0);
            Location l = new Location(rsh.getWorld(), rsh.getX(), rsh.getY(), rsh.getZ());
            plugin.getQueryFactory().updateLocations(setlocs, id);
            // rebuild the exterior
            BuildData bd = new BuildData(player.getUniqueId().toString());
            bd.setDirection(rsh.getDirection());
            bd.setLocation(l);
            bd.setMalfunction(false);
            bd.setOutside(true);
            bd.setPlayer(player);
            bd.setRebuild(true);
            bd.setSubmarine(rsh.isSubmarine());
            bd.setTardisID(id);
            bd.setThrottle(SpaceTimeThrottle.REBUILD);
            if (!rsh.getPreset().isEmpty()) {
                // set the chameleon preset
                HashMap<String, Object> wherep = new HashMap<>();
                wherep.put("tardis_id", id);
                HashMap<String, Object> setp = new HashMap<>();
                setp.put("chameleon_preset", rsh.getPreset());
                // set chameleon adaption to OFF
                setp.put("adapti_on", 0);
                plugin.getQueryFactory().doSyncUpdate("tardis", setp, wherep);
            }
            plugin.getPresetBuilder().buildPreset(bd);
            // engage the handbrake!
            HashMap<String, Object> seth = new HashMap<>();
            seth.put("handbrake_on", 1);
            HashMap<String, Object> whereh = new HashMap<>();
            whereh.put("tardis_id", id);
            plugin.getQueryFactory().doSyncUpdate("tardis", seth, whereh);
            plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.STOP, id));
        }
        // stop time rotor?
        HashMap<String, Object> wherei = new HashMap<>();
        wherei.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false, 2);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            if (tardis.getRotor() != null) {
                ItemFrame itemFrame = TARDISTimeRotor.getItemFrame(tardis.getRotor());
                if (itemFrame != null) {
                    // cancel the animation
                    int task = TARDISTimeRotor.ANIMATED_ROTORS.getOrDefault(itemFrame.getUniqueId(), -1);
                    plugin.getServer().getScheduler().cancelTask(task);
                    TARDISTimeRotor.setRotor(TARDISTimeRotor.getRotorOffModelData(itemFrame), itemFrame);
                }
            }
        }
        return true;
    }
}
