/*
 * Copyright (C) 2026 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Comparator;

import java.util.HashMap;

public class Handbrake {

    private final TARDIS plugin;

    public Handbrake(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean isRelativityDifferentiated(int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("type", 47);
        ResultSetControls rsc = new ResultSetControls(plugin, where, false);
        if (rsc.resultSet()) {
            Block rd = TARDISStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation()).getBlock();
            if (rd.getType() == Material.COMPARATOR) {
                Comparator comparator = (Comparator) rd.getBlockData();
                return comparator.getMode().equals(Comparator.Mode.SUBTRACT);
            } else {
                return rsc.getSecondary() == 1;
            }
        }
        return false;
    }

    public boolean isFlightModeExterior(String uuid) {
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
        return (rsp.resultSet()) && rsp.getFlightMode() == 4;
    }

    public boolean isDoorOpen(int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("door_type", 1);
        ResultSetDoors rs = new ResultSetDoors(plugin, where, false);
        if (rs.resultSet()) {
            Block door = TARDISStaticLocationGetters.getLocationFromDB(rs.getDoor_location()).getBlock();
            return TARDISStaticUtils.isDoorOpen(door);
        }
        return false;
    }
}
