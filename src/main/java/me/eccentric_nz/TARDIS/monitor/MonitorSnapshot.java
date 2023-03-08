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

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetChunks;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 *
 * @author macgeek
 */
public class MonitorSnapshot {

    private final TARDIS plugin;

    public MonitorSnapshot(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void get(boolean in, Player player) {
        int which = (in) ? 1 : 0;
        // set raytrace distance -> maximum console size is 48 blocks, exterior view should be further
        int distance = (in) ? 48 : 128;
        // get interior door location
        ResultSetTardisID rst = new ResultSetTardisID(plugin);
        if (rst.fromUUID(player.getUniqueId().toString())) {
            int id = rst.getTardis_id();
            HashMap<String, Object> whered = new HashMap<>();
            whered.put("tardis_id", id);
            whered.put("door_type", which);
            ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
            if (rsd.resultSet()) {
                COMPASS d = rsd.getDoor_direction();
                Location doorBottom = TARDISStaticLocationGetters.getLocationFromDB(rsd.getDoor_location());
                // set y position to eye height
                doorBottom.add(0, 1.6f, 0);
                // adjust x,z to be centred in front of the door
                int getx = doorBottom.getBlockX();
                int getz = doorBottom.getBlockZ();
                float yaw = 0.05f;
                switch (d) {
                    case NORTH -> {
                        // z -ve
                        doorBottom.setX(getx + 0.5);
                        doorBottom.setZ(getz - 0.5);
                        yaw = (in) ? 180.05f : 0.05f;
                    }
                    case EAST -> {
                        // x +ve
                        doorBottom.setX(getx + 1.5);
                        doorBottom.setZ(getz + 0.5);
                        yaw = (in) ? -90.05f : 90.05f;
                    }
                    case SOUTH -> {
                        // z +ve
                        doorBottom.setX(getx + 0.5);
                        doorBottom.setZ(getz + 1.5);
                        yaw = (in) ? 0.05f : 180.05f;
                    }
                    case WEST -> {
                        // x -ve
                        doorBottom.setX(getx - 0.5);
                        doorBottom.setZ(getz + 0.5);
                        yaw = (in) ? 90.05f : -90.05f;
                    }
                }
                // set pitch to up and down so we can fill two maps
                doorBottom.setPitch(25);
                doorBottom.setYaw(yaw);
                Location doorTop = doorBottom.clone();
                doorTop.setPitch(-25.5f);
                loadChunks(plugin, doorBottom, in, d, id, distance);
                if (player.getInventory().getItemInMainHand().getType().equals(Material.FILLED_MAP)
                        && player.getInventory().getItemInOffHand().getType().equals(Material.FILLED_MAP)) {
                    MonitorUtils.updateSnapshot(doorTop, distance, player.getInventory().getItemInMainHand());
                    MonitorUtils.updateSnapshot(doorBottom, distance, player.getInventory().getItemInOffHand());
                } else {
                    MonitorUtils.createSnapshot(doorBottom, player, distance);
                    MonitorUtils.createSnapshot(doorTop, player, distance);
                }
            }
        }
    }

    public static void loadChunks(TARDIS plugin, Location doorBottom, boolean in, COMPASS d, int id, int distance) {
        if (in) {
            // load all console chunks!
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            ResultSetChunks rsc = new ResultSetChunks(plugin, where, true);
            if (rsc.resultSet()) {
                for (HashMap<String, String> map : rsc.getData()) {
                    int x = TARDISNumberParsers.parseInt(map.get("x"));
                    int z = TARDISNumberParsers.parseInt(map.get("z"));
                    Chunk chunk = doorBottom.getWorld().getChunkAt(x, z);
                    while (!chunk.isLoaded()) {
                        chunk.load();
                    }
                }
            }
        } else {
            // load distant chunks
            int dx = 0;
            int dz = 0;
            boolean bx = false;
            boolean bz = false;
            switch (d) {
                case NORTH -> {
                    dz = 1;
                    bx = true;
                }
                case EAST -> {
                    dx = -1;
                    bz = true;
                }
                case SOUTH -> {
                    dz = -1;
                    bx = true;
                }
                case WEST -> {
                    dx = 1;
                    bz = true;
                }
            }
            Chunk chunk = doorBottom.getChunk();
            while (!chunk.isLoaded()) {
                chunk.load();
            }
            int cx = chunk.getX();
            int cz = chunk.getZ();
            // 16 x 8 = 128 blocks - the ray trace distance for exterior
            // the further out we go, the wider we need to load chunks...
            int side = 0;
            for (int i = 1; i < ((distance / 16) + 1); i++) {
                cx += dx;
                cz += dz;
                Chunk next = doorBottom.getWorld().getChunkAt(cx, cz);
                while (!next.isLoaded()) {
                    next.load();
                }
                for (int j = -side; j <= side; j++) {
                    if (bx) {
                        next = doorBottom.getWorld().getChunkAt(cx + j, cz);
                    }
                    if (bz) {
                        next = doorBottom.getWorld().getChunkAt(cx, cz + j);
                    }
                    while (!next.isLoaded()) {
                        next.load();
                    }
                }
                if (i % 2 == 0) {
                    side += 2;
                }
            }
        }
    }
}
