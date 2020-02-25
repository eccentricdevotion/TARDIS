/*
 * Copyright (C) 2020 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetStandby;
import me.eccentric_nz.TARDIS.database.ResultSetStandby.StandbyData;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISStandbyMode implements Runnable {

    private final TARDIS plugin;
    private final int amount;

    public TARDISStandbyMode(TARDIS plugin) {
        this.plugin = plugin;
        amount = this.plugin.getArtronConfig().getInt("standby");
    }

    @Override
    public void run() {
        // get TARDISes that are powered on
        HashMap<Integer, StandbyData> ids = new ResultSetStandby(plugin).onStandby();
        ids.forEach((key, value) -> {
            int id = key;
            int level = value.getLevel();
            // not while travelling or recharging and only until they hit zero
            if (!isTravelling(id) && !isNearCharger(id) && level > amount) {
                int remove = amount;
                // if TARDIS force field is on drain more power
                if (plugin.getTrackerKeeper().getActiveForceFields().containsKey(value.getUuid())) {
                    remove *= 3;
                }
                // remove some energy
                HashMap<String, Object> where = new HashMap<>();
                where.put("tardis_id", id);
                plugin.getQueryFactory().alterEnergyLevel("tardis", -remove, where, null);
            } else if (level <= amount) {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    // power down!
                    HashMap<String, Object> wherep = new HashMap<>();
                    wherep.put("tardis_id", id);
                    HashMap<String, Object> setp = new HashMap<>();
                    setp.put("powered_on", 0);
                    OfflinePlayer player = plugin.getServer().getOfflinePlayer(value.getUuid());
                    if (player.isOnline()) {
                        TARDISSounds.playTARDISSound(player.getPlayer().getLocation(), "power_down");
                        TARDISMessage.send(player.getPlayer(), "POWER_OFF_AUTO");
                    }
                    long delay = 0;
                    // if hidden, rebuild
                    if (value.isHidden()) {
                        plugin.getServer().dispatchCommand(plugin.getConsole(), "tardisremote " + player.getName() + " rebuild");
                        if (player.isOnline()) {
                            TARDISMessage.send(player.getPlayer(), "POWER_FAIL");
                        }
                        delay = 20L;
                    }
                    // police box lamp, delay it incase the TARDIS needs rebuilding
                    if (value.getPreset().equals(PRESET.NEW) || value.getPreset().equals(PRESET.OLD)) {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TARDISPoliceBoxLampToggler(plugin).toggleLamp(id, false), delay);
                    }
                    // if lights are on, turn them off
                    if (value.isLights()) {
                        new TARDISLampToggler(plugin).flickSwitch(id, value.getUuid(), true, value.isLanterns());
                    }
                    // if beacon is on turn it off
                    new TARDISBeaconToggler(plugin).flickSwitch(value.getUuid(), id, false);
                    // update database
                    plugin.getQueryFactory().doUpdate("tardis", setp, wherep);
                    // if force field is on, disable it
                    if (plugin.getTrackerKeeper().getActiveForceFields().containsKey(value.getUuid())) {
                        plugin.getTrackerKeeper().getActiveForceFields().remove(value.getUuid());
                    }
                }, 1L);
            }
        });
    }

    private boolean isTravelling(int id) {
        return (plugin.getTrackerKeeper().getDematerialising().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
    }

    /**
     * Checks whether the TARDIS is near a recharge location.
     */
    private boolean isNearCharger(int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetCurrentLocation rs = new ResultSetCurrentLocation(plugin, where);
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id) || !rs.resultSet()) {
            return false;
        }
        if (rs.getWorld() == null) {
            return false;
        }
        // get Police Box location
        Location pb_loc = new Location(rs.getWorld(), rs.getX(), rs.getY(), rs.getZ());
        // check location is within configured blocks of a recharger
        for (Location l : plugin.getGeneralKeeper().getRechargers()) {
            if (plugin.getUtils().compareLocations(pb_loc, l)) {
                return true;
            }
        }
        return false;
    }
}
