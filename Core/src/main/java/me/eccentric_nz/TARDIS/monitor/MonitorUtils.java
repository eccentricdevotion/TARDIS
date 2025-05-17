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
package me.eccentric_nz.TARDIS.monitor;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.util.BoundingBox;

import java.util.HashMap;
import java.util.UUID;

public class MonitorUtils {

    public static ItemStack createMap(Location location, int distance) {
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
        return itemStack;
    }

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
        // give the player the map
        player.getInventory().addItem(itemStack);
    }

    public static void updateSnapshot(Location location, int distance, ItemStack map) {
        MapMeta mapMeta = (MapMeta) map.getItemMeta();
        MapView mapView = null;
        if (mapMeta.hasMapView()) {
            mapView = mapMeta.getMapView();
        }
        if (mapView == null) {
            mapView = Bukkit.createMap(location.getWorld());
        }
        mapView.setTrackingPosition(false);
        // !IMPORTANT unlock map else it won't get a new render
        mapView.setLocked(false);
        for (MapRenderer renderer : mapView.getRenderers()) {
            mapView.removeRenderer(renderer);
        }
        SnapshotRenderer renderer = new SnapshotRenderer(location, distance);
        mapView.addRenderer(renderer);
        mapMeta.setMapView(mapView);
        map.setItemMeta(mapMeta);
    }

    public static Snapshot getLocationAndDirection(int id, boolean in) {
        int which = (in) ? 1 : 0;
        HashMap<String, Object> whered = new HashMap<>();
        whered.put("tardis_id", id);
        whered.put("door_type", which);
        ResultSetDoors rsd = new ResultSetDoors(TARDIS.plugin, whered, false);
        if (rsd.resultSet()) {
            COMPASS d = rsd.getDoor_direction();
            Location door = TARDISStaticLocationGetters.getLocationFromDB(rsd.getDoor_location());
            // set y position to eye height
            door.add(0, 1.6f, 0);
            // adjust x,z to be centred in front of the door
            int getx = door.getBlockX();
            int getz = door.getBlockZ();
            float yaw;
            switch (d) {
                case SOUTH -> {
                    // z -ve
                    door.setX(getx + 0.5);
                    door.setZ(getz - 0.5);
                    yaw = (in) ? 0.05f : 180.05f;
                }
                case WEST -> {
                    // x +ve
                    door.setX(getx + 1.5);
                    door.setZ(getz + 0.5);
                    yaw = (in) ? 90.05f : -90.05f;
                }
                case NORTH -> {
                    // z +ve
                    door.setX(getx + 0.5);
                    door.setZ(getz + 1.5);
                    yaw = (in) ? 180.05f : 0.05f;
                }
                // EAST
                default -> {
                    // x -ve
                    door.setX(getx - 0.5);
                    door.setZ(getz + 0.5);
                    yaw = (in) ? -90.05f : 90.05f;
                }
            }
            // set pitch to straight ahead
            door.setPitch(0);
            door.setYaw(yaw);
            return new Snapshot(door, d);
        }
        return null;
    }

    public static ItemFrame getItemFrameFromLocation(Location location, UUID uuid) {
        BoundingBox box = new BoundingBox(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getBlockX() + 1, location.getBlockY() + 1, location.getBlockZ() + 1);
        for (Entity e : location.getWorld().getNearbyEntities(box, (e) -> e.getType() == EntityType.ITEM_FRAME)) {
            if (e instanceof ItemFrame frame && frame.getUniqueId() != uuid) {
                return frame;
            }
        }
        return null;
    }

    public static ItemFrame getItemFrameFromLocation(int id, boolean monitor) {
        int control = (monitor) ? 45 : 46;
        Material material = (monitor) ? Material.FILLED_MAP : Material.GLASS;
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("type", control);
        ResultSetControls rsc = new ResultSetControls(TARDIS.plugin, where, false);
        if (rsc.resultSet()) {
            Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation());
            if (location != null) {
                BoundingBox box = new BoundingBox(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getBlockX() + 1, location.getBlockY() + 1, location.getBlockZ() + 1);
                for (Entity e : location.getWorld().getNearbyEntities(box, (e) -> e.getType() == EntityType.ITEM_FRAME)) {
                    if (e instanceof ItemFrame frame && frame.getItem().getType() == material) {
                        return frame;
                    }
                }
            }
        }
        return null;
    }
}
