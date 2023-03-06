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
package me.eccentric_nz.TARDIS.monitor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class MonitorMapView {

    public static void createSnapshot(Location location, Player player, int distance) {
        ItemStack itemStack = new ItemStack(Material.FILLED_MAP);
        MapMeta mapMeta = (MapMeta) itemStack.getItemMeta();
        MapView mapView = Bukkit.createMap(location.getWorld());
        mapView.setTrackingPosition(false);
        for (MapRenderer renderer : mapView.getRenderers()) {
            mapView.removeRenderer(renderer);
        }
        SnapshotRenderer renderer = new SnapshotRenderer(location, distance);
        mapView.addRenderer(renderer);
        mapMeta.setMapView(mapView);
        itemStack.setItemMeta(mapMeta);
        // set map in Item frame
        player.getInventory().addItem(itemStack);
    }

    public static void updateSnapshot(Location location, Player player, int distance, ItemStack map) {
        MapMeta mapMeta = (MapMeta) map.getItemMeta();
        MapView mapView = Bukkit.createMap(location.getWorld());
        mapView.setTrackingPosition(false);
        for (MapRenderer renderer : mapView.getRenderers()) {
            mapView.removeRenderer(renderer);
        }
        SnapshotRenderer renderer = new SnapshotRenderer(location, distance);
        mapView.addRenderer(renderer);
        mapMeta.setMapView(mapView);
        map.setItemMeta(mapMeta);
    }
}
