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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISCache;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;

import java.util.HashMap;

/**
 * Artron energy is vital in the running of a TARDIS; it can run low and when down to 10% it means even backup power is
 * unavailable, as this requires artron energy as well.
 *
 * @author eccentric_nz
 */
public class TARDISLightningListener implements Listener {

    private final TARDIS plugin;

    public TARDISLightningListener(TARDIS plugin) {
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
            HashMap<String, Object> where = new HashMap<>();
            where.put("abandoned", 0);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", true);
            if (rs.resultSet()) {
                for (Tardis t : rs.getData()) {
                    boolean charging = !t.isRecharging();
                    int id = t.getTardisId();
                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                        return;
                    }
                    Current current = TARDISCache.CURRENT.get(id);
                    if (current != null) {
                        World w = current.location().getWorld();
                        // only if the tardis is in the same world as the lightning strike and is not at a beacon recharger!
                        if (strikeworld.equals(w) && !charging) {
                            // only recharge if the TARDIS is within range
                            if (plugin.getUtils().compareLocations(e.getLightning().getLocation(), current.location())) {
                                int amount = plugin.getArtronConfig().getInt("lightning_recharge") + t.getArtronLevel();
                                HashMap<String, Object> set = new HashMap<>();
                                set.put("artron_level", amount);
                                HashMap<String, Object> wheret = new HashMap<>();
                                wheret.put("tardis_id", id);
                                plugin.getQueryFactory().doUpdate("tardis", set, wheret);
                            }
                        }
                    }
                }
            }
        }
    }
}
