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
package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISVoidFall {

    private final TARDIS plugin;

    public TARDISVoidFall(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void teleport(Player p) {
        // get TARDIS player was in
        int id = plugin.getTardisAPI().getIdOfTARDISPlayerIsIn(p);
        // get inner door location
        HashMap<String, Object> wherei = new HashMap<>();
        wherei.put("door_type", 1);
        wherei.put("tardis_id", id);
        ResultSetDoors rsi = new ResultSetDoors(plugin, wherei, false);
        if (rsi.resultSet()) {
            COMPASS innerD = rsi.getDoor_direction();
            String doorLocStr = rsi.getDoor_location();
            World cw = TARDISStaticLocationGetters.getWorld(doorLocStr);
            Location tardis_loc = TARDISStaticLocationGetters.getLocationFromDB(doorLocStr);
            int getx = tardis_loc.getBlockX();
            int getz = tardis_loc.getBlockZ();
            switch (innerD) {
                case NORTH:
                    // z -ve
                    tardis_loc.setX(getx + 0.5);
                    tardis_loc.setZ(getz - 0.5);
                    break;
                case EAST:
                    // x +ve
                    tardis_loc.setX(getx + 1.5);
                    tardis_loc.setZ(getz + 0.5);
                    break;
                case SOUTH:
                    // z +ve
                    tardis_loc.setX(getx + 0.5);
                    tardis_loc.setZ(getz + 1.5);
                    break;
                case WEST:
                    // x -ve
                    tardis_loc.setX(getx - 0.5);
                    tardis_loc.setZ(getz + 0.5);
                    break;
            }
            // enter TARDIS!
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
