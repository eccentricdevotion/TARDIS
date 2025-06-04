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
package me.eccentric_nz.TARDIS.commands.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.builders.exterior.BuildData;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.flight.FlightReturnData;
import me.eccentric_nz.TARDIS.flight.TARDISExteriorFlight;
import me.eccentric_nz.TARDIS.rotors.TARDISTimeRotor;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

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
        UUID uuid = player.getUniqueId();
        if (plugin.getTrackerKeeper().getFlyingReturnLocation().containsKey(uuid)) {
            plugin.getTrackerKeeper().getStillFlyingNotReturning().remove(uuid);
            // land TARDIS
            FlightReturnData frd = plugin.getTrackerKeeper().getFlyingReturnLocation().get(uuid);
            Entity stand = plugin.getServer().getEntity(frd.stand());
            if (stand != null) {
                stand.setVelocity(new Vector(0, 0, 0));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    // teleport player back to the TARDIS interior
                    new TARDISExteriorFlight(plugin).stopFlying(player, (ArmorStand) stand);
                });
            } else {
                // scan for nearby armour stands in case player teleport fails due to lag
                for (Entity e : player.getLocation().getWorld().getNearbyEntities(player.getLocation(), 4, 4, 4, (s) -> s.getType() == EntityType.ARMOR_STAND)) {
                    if (e instanceof ArmorStand armorStand) {
                        e.setVelocity(new Vector(0, 0, 0));
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            // teleport player back to the TARDIS interior
                            new TARDISExteriorFlight(plugin).stopFlying(player, armorStand);
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
            BuildData bd = new BuildData(uuid.toString());
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
            // also set the console handbrake state if there is one
            HashMap<String, Object> setc = new HashMap<>();
            setc.put("state", 1);
            HashMap<String, Object> wherec = new HashMap<>();
            wherec.put("tardis_id", id);
            wherec.put("control", "HANDBRAKE");
            plugin.getQueryFactory().doSyncUpdate("interactions", setc, wherec);
            plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.STOP, id));
        }
        // stop time rotor?
        HashMap<String, Object> wherei = new HashMap<>();
        wherei.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            if (tardis.getRotor() != null) {
                ItemFrame itemFrame = TARDISTimeRotor.getItemFrame(tardis.getRotor());
                if (itemFrame != null) {
                    // cancel the animation
                    int task = TARDISTimeRotor.ANIMATED_ROTORS.getOrDefault(itemFrame.getUniqueId(), -1);
                    plugin.getServer().getScheduler().cancelTask(task);
                    TARDISTimeRotor.setRotor(TARDISTimeRotor.getRotorOffModel(itemFrame), itemFrame);
                }
            }
        }
        return true;
    }
}
