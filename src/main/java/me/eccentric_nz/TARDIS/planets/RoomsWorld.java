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
package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Transmat;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTransmat;

import java.util.HashMap;

public class RoomsWorld {

    public void check(TARDIS plugin) {
        ResultSetTransmat rst = new ResultSetTransmat(plugin, -1, "rooms");
        if (!rst.resultSet()) {
            // insert record
            HashMap<String, Object> set = new HashMap<>();
            set.put("tardis_id", -1);
            set.put("name", "rooms");
            set.put("world", "rooms");
            set.put("x", plugin.getPlanetsConfig().getDouble("planets.rooms.transmat.x", 8.5d));
            set.put("y", plugin.getPlanetsConfig().getDouble("planets.rooms.transmat.y", 68.0d));
            set.put("z", plugin.getPlanetsConfig().getDouble("planets.rooms.transmat.z", 2.5d));
            set.put("yaw", 0.0d);
            plugin.getQueryFactory().doInsert("transmats", set);
        }
    }

    public Transmat getTransmat(TARDIS plugin) {
        ResultSetTransmat rst = new ResultSetTransmat(plugin, -1, "rooms");
        return rst.resultSet() ? new Transmat("Rooms World", rst.getWorld(), rst.getX(), rst.getY(), rst.getZ(), rst.getYaw()) : null;
    }
}
