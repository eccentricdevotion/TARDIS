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
package me.eccentric_nz.tardis.listeners;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.data.TARDIS;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;

import java.util.HashMap;

/**
 * Artron energy is vital in the running of a tardis; it can run low and when down to 10% it means even backup power is
 * unavailable, as this requires artron energy as well.
 *
 * @author eccentric_nz
 */
public class TARDISLightningListener implements Listener {

    private final TARDISPlugin plugin;

    public TARDISLightningListener(TARDISPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for lightning strikes around the TARDIS Police Box. If the strike is within (recharge_distance in
     * artron.yml) blocks, then the TARDIS Artron Levels will be increased by the configured amount (lightning_recharge
     * in artron.yml).
     *
     * @param e a lightning strike
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onLightningStrike(LightningStrikeEvent e) {
        LightningStrike strike = e.getLightning();
        // only if a natural lightning strike
        if (!strike.isEffect()) {
            Location l = strike.getLocation();
            World strikeworld = l.getWorld();
            ResultSetTardis rs = new ResultSetTardis(plugin, new HashMap<>(), "", true, 0);
            if (rs.resultSet()) {
                for (TARDIS t : rs.getData()) {
                    boolean charging = !t.isRecharging();
                    int id = t.getTardisId();
                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                        return;
                    }
                    HashMap<String, Object> wherecl = new HashMap<>();
                    wherecl.put("tardis_id", id);
                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                    if (rsc.resultSet()) {
                        World w = rsc.getWorld();
                        // only if the tardis is in the same world as the lightning strike and is not at a beacon recharger!
                        assert strikeworld != null;
                        if (strikeworld.equals(w) && !charging) {
                            Location loc = new Location(w, rsc.getX(), rsc.getY(), rsc.getZ());
                            // only recharge if the TARDIS is within range
                            if (plugin.getUtils().compareLocations(loc, loc)) {
                                int amount = plugin.getArtronConfig().getInt("lightning_recharge") + t.getArtronLevel();
                                HashMap<String, Object> set = new HashMap<>();
                                set.put("artron_level", amount);
                                HashMap<String, Object> where = new HashMap<>();
                                where.put("tardis_id", id);
                                plugin.getQueryFactory().doUpdate("tardis", set, where);
                            }
                        }
                    }
                }
            }
        }
    }
}
