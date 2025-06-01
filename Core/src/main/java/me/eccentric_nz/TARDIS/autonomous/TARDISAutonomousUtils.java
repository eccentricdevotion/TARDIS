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
package me.eccentric_nz.TARDIS.autonomous;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetHomeLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class TARDISAutonomousUtils {

    public static Location getRecharger(String world, Player player) {
        Location l = null;
        HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("world", world);
        ResultSetAreas rsa = new ResultSetAreas(TARDIS.plugin, wherea, false, false);
        if (rsa.resultSet()) {
            String area = rsa.getArea().areaName();
            if (!TARDISPermission.hasPermission(player, "tardis.area." + area) || !player.isPermissionSet("tardis.area." + area)) {
                return null;
            }
            if (rsa.getArea().grid()) {
                l = TARDIS.plugin.getTardisArea().getNextSpot(area);
            } else {
                l = TARDIS.plugin.getTardisArea().getSemiRandomLocation(rsa.getArea().areaId());
            }
        }
        return l;
    }

    public static Location getConfiguredRecharger(Player player) {
        // get configured area names
        List<String> areas = TARDIS.plugin.getConfig().getStringList("autonomous_areas");
        // will always return the first area in the list if there is room to park
        for (String area : areas) {
            HashMap<String, Object> wherea = new HashMap<>();
            wherea.put("area_name", area);
            ResultSetAreas rsa = new ResultSetAreas(TARDIS.plugin, wherea, false, false);
            if (rsa.resultSet()) {
                if (!TARDISPermission.hasPermission(player, "tardis.area." + area) || !player.isPermissionSet("tardis.area." + area)) {
                    return null;
                }
                Location l;
                if (rsa.getArea().grid()) {
                    l = TARDIS.plugin.getTardisArea().getNextSpot(area);
                } else {
                    l = TARDIS.plugin.getTardisArea().getSemiRandomLocation(rsa.getArea().areaId());
                }
                if (l != null) {
                    return l;
                }
            }
        }
        return null;
    }

    public static boolean compareCurrentToHome(Location c, ResultSetHomeLocation h) {
        return (c.getWorld() == (h.getWorld()) && c.getBlockX() == h.getX() && c.getBlockY() == h.getY() && c.getBlockZ() == h.getZ());
    }
}
