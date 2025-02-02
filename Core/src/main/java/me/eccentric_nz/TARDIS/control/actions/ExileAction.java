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
package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ExileAction {

    private final TARDIS plugin;

    public ExileAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void getExile(Player player, int id, COMPASS direction) {
        // get the exile area
        String permArea = plugin.getTardisArea().getExileArea(player);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "EXILE", permArea);
        Location l;
        HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("area_name", permArea);
        ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false, false);
        rsa.resultSet();
        if (rsa.getArea().isGrid()) {
            l = plugin.getTardisArea().getNextSpot(permArea);
        } else {
            l = plugin.getTardisArea().getSemiRandomLocation(rsa.getArea().getAreaId());
        }
        if (l == null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_MORE_SPOTS");
        } else {
            HashMap<String, Object> set = new HashMap<>();
            set.put("world", l.getWorld().getName());
            set.put("x", l.getBlockX());
            set.put("y", l.getBlockY());
            set.put("z", l.getBlockZ());
            set.put("direction", direction.toString());
            set.put("submarine", 0);
            HashMap<String, Object> wherel = new HashMap<>();
            wherel.put("tardis_id", id);
            plugin.getQueryFactory().doSyncUpdate("next", set, wherel);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_APPROVED", permArea);
        }
    }
}
