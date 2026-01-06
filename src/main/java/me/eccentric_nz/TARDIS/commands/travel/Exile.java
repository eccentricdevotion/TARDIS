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
package me.eccentric_nz.TARDIS.commands.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 *
 * @author eccentric_nz
 */
public class Exile {

    private final TARDIS plugin;

    public Exile(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean action(Player player, int id) {
        String permArea = plugin.getTardisArea().getExileArea(player);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "EXILE", permArea);
        Location l;
        HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("area_name", permArea);
        ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false, false);
        rsa.resultSet();
        if (rsa.getArea().grid()) {
            l = plugin.getTardisArea().getNextSpot(permArea);
        } else {
            l = plugin.getTardisArea().getSemiRandomLocation(rsa.getArea().areaId());
        }
        if (l == null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_MORE_SPOTS");
            return true;
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("world", l.getWorld().getName());
        set.put("x", l.getBlockX());
        set.put("y", l.getBlockY());
        set.put("z", l.getBlockZ());
        set.put("submarine", 0);
        HashMap<String, Object> tid = new HashMap<>();
        tid.put("tardis_id", id);
        plugin.getQueryFactory().doSyncUpdate("next", set, tid);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_APPROVED", permArea);
        plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), TravelType.EXILE));
        plugin.getTrackerKeeper().getRescue().remove(id);
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            new TARDISLand(plugin, id, player).exitVortex();
            plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.EXILE, id));
        }
        return true;
    }
}
