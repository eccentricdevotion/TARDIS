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
package me.eccentric_nz.TARDIS.artron;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISCache;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetArtronStorage;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisArtron;
import me.eccentric_nz.TARDIS.sensor.ChargingSensor;
import me.eccentric_nz.TARDIS.sensor.SensorTracker;
import org.bukkit.Location;

import java.util.HashMap;

/**
 * Within the TARDIS' generator room is an Artron Energy Capacitor. The Eighth Doctor had a habit of using Artron Energy
 * to make toast.
 *
 * @author eccentric_nz
 */
class TARDISArtronRunnable implements Runnable {

    private final TARDIS plugin;
    private final int id;
    private int task;

    private boolean sensor = true;

    TARDISArtronRunnable(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
    }

    /**
     * A runnable task that recharges the TARDIS.
     */
    @Override
    public void run() {
        // is there a capacitor?
        // TARDISes come with 1 capacitor when grown, but players could remove them from the eye storage
        int capacitors = 0;
        ResultSetArtronStorage rsa = new ResultSetArtronStorage(plugin);
        if (rsa.fromID(id)) {
            capacitors = rsa.getCapacitorCount();
        }
        int level = isFull(id);
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        boolean charged =  level > (plugin.getArtronConfig().getInt("full_charge") - 1);
        if (!isNearCharger(id) || charged || capacitors == 0) {
            plugin.getServer().getScheduler().cancelTask(task);
            task = 0;
            HashMap<String, Object> set = new HashMap<>();
            set.put("recharging", 0);
            plugin.getQueryFactory().doUpdate("tardis", set, where);
            TARDISCache.invalidate(id);
            if (charged) {
                // toggle charging sensor
                new ChargingSensor(plugin, id).toggle();
                SensorTracker.isCharging.remove(id);
            }
        } else {
            if (sensor) {
                new ChargingSensor(plugin, id).toggle();
                sensor = false;
                SensorTracker.isCharging.add(id);
            }
            // calculate percentage
            int onepercent = Math.round(plugin.getArtronConfig().getInt("full_charge") / 100.0F);
            // update TARDIS artron_level
            plugin.getQueryFactory().alterEnergyLevel("tardis", onepercent, where, null);
            TARDISCache.invalidate(id);
        }
    }

    /**
     * Checks whether the TARDIS is near a recharge location.
     */
    private boolean isNearCharger(int id) {
        Current current = TARDISCache.CURRENT.get(id);
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id) || current == null) {
            return false;
        }
        if (current.location().getWorld() == null) {
            return false;
        }
        // get Police Box location
        Location pb_loc = current.location().clone();
        // check location is within configured blocks of a recharger
        for (Location l : plugin.getGeneralKeeper().getRechargers()) {
            if (plugin.getUtils().compareLocations(pb_loc, l)) {
                // strike lightning to the Police Box torch location
                if (plugin.getConfig().getBoolean("preferences.strike_lightning")) {
                    pb_loc.setY(pb_loc.getY() + 3);
                    current.location().getWorld().strikeLightningEffect(pb_loc);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the current Artron Energy Level for the specified TARDIS.
     */
    private int isFull(int id) {
        ResultSetTardisArtron rs = new ResultSetTardisArtron(plugin);
        return (rs.fromID(id)) ? rs.getArtronLevel() : plugin.getArtronConfig().getInt("full_charge");
    }

    public void setTask(int task) {
        this.task = task;
    }
}
