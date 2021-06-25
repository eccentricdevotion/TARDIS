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
package me.eccentric_nz.tardis.utility;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetDoors;
import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TardisVoidFall {

    private final TardisPlugin plugin;

    public TardisVoidFall(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    public void teleport(Player p) {
        // get tardis player was in
        int id = plugin.getTardisApi().getIdOfTARDISPlayerIsIn(p);
        // get inner door location
        HashMap<String, Object> wherei = new HashMap<>();
        wherei.put("door_type", 1);
        wherei.put("tardis_id", id);
        ResultSetDoors rsi = new ResultSetDoors(plugin, wherei, false);
        if (rsi.resultSet()) {
            CardinalDirection innerD = rsi.getDoorDirection();
            String doorLocStr = rsi.getDoorLocation();
            World cw = TardisStaticLocationGetters.getWorld(doorLocStr);
            Location tardis_loc = TardisStaticLocationGetters.getLocationFromDB(doorLocStr);
            assert tardis_loc != null;
            int getx = tardis_loc.getBlockX();
            int getz = tardis_loc.getBlockZ();
            switch (innerD) {
                case NORTH -> {
                    // z -ve
                    tardis_loc.setX(getx + 0.5);
                    tardis_loc.setZ(getz - 0.5);
                }
                case EAST -> {
                    // x +ve
                    tardis_loc.setX(getx + 1.5);
                    tardis_loc.setZ(getz + 0.5);
                }
                case SOUTH -> {
                    // z +ve
                    tardis_loc.setX(getx + 0.5);
                    tardis_loc.setZ(getz + 1.5);
                }
                case WEST -> {
                    // x -ve
                    tardis_loc.setX(getx - 0.5);
                    tardis_loc.setZ(getz + 0.5);
                }
            }
            // enter tardis!
            cw.getChunkAt(tardis_loc).load();
            float yaw = p.getLocation().getYaw();
            float pitch = p.getLocation().getPitch();
            tardis_loc.setPitch(pitch);
            tardis_loc.setYaw(yaw);
            World playerWorld = p.getLocation().getWorld();
            p.setFallDistance(0.0f);
            plugin.getGeneralKeeper().getDoorListener().movePlayer(p, tardis_loc, false, playerWorld, false, 3, true);
        } else {
            p.setHealth(0);
        }
    }
}
