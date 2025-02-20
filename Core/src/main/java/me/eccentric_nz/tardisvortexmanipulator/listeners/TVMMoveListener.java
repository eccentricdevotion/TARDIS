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
package me.eccentric_nz.tardisvortexmanipulator.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetBeacon;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.UUID;

public class TVMMoveListener implements Listener {

    private final TARDIS plugin;

    public TVMMoveListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!plugin.getTvmSettings().getBeaconSetters().contains(uuid)) {
            return;
        }
        // if only the pitch or yaw has changed
        if (compareXYZ(event.getFrom(), event.getTo())) {
            return;
        }
        if (!event.getTo().getBlock().getType().equals(Material.BEACON)) {
            plugin.getTvmSettings().getBeaconSetters().remove(uuid);
            // remove beacon
            TVMResultSetBeacon rs = new TVMResultSetBeacon(plugin, uuid.toString());
            if (rs.resultSet()) {
                rs.getBlocks().forEach((tvmb) -> {
                    tvmb.getBlock().setBlockData(tvmb.getBlockData());
                    // remove protection
                    plugin.getTvmSettings().getBlocks().remove(tvmb.getBlock().getLocation());
                });
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid.toString());
                plugin.getQueryFactory().doDelete("beacons", where);
            }
        }
    }

    private boolean compareXYZ(Location from, Location to) {
        return from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ();
    }
}
