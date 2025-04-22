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
package me.eccentric_nz.TARDIS.flight.vehicle;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.exterior.BuildData;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentWithPreset;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.event.world.WorldLoadEvent;

public class VehicleLoadListener implements Listener {

    private final TARDIS plugin;

    public VehicleLoadListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onVehicleLoad(EntitiesLoadEvent event) {
        for (Entity e : event.getEntities()) {
            if (e.getType() == EntityType.ARMOR_STAND) {
                Location location = e.getLocation();
                ResultSetCurrentWithPreset rsc = new ResultSetCurrentWithPreset(plugin, location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
                if (rsc.resultSet()) {
                    BuildData data = new BuildData(null);
                    data.setTardisID(rsc.getId());
                    data.setAddSign(rsc.hasSign());
                    VehicleUtility.convertStand(location, rsc.getPreset(), data);
                }
            }
        }
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        for (Entity e : event.getWorld().getEntities()) {
            if (e.getType() == EntityType.ARMOR_STAND) {
                Location location = e.getLocation();
                ResultSetCurrentWithPreset rsc = new ResultSetCurrentWithPreset(plugin, location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
                if (rsc.resultSet()) {
                    BuildData data = new BuildData(null);
                    data.setTardisID(rsc.getId());
                    data.setAddSign(rsc.hasSign());
                    VehicleUtility.convertStand(location, rsc.getPreset(), data);
                }
            }
        }
    }
}
