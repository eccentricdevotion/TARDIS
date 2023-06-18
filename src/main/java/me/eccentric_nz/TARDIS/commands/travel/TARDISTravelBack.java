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
package me.eccentric_nz.TARDIS.commands.travel;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetBackLocation;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISTravelBack {

    private final TARDIS plugin;

    public TARDISTravelBack(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean action(Player player, int id) {
        HashMap<String, Object> wherebl = new HashMap<>();
        wherebl.put("tardis_id", id);
        ResultSetBackLocation rsb = new ResultSetBackLocation(plugin, wherebl);
        if (!rsb.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "PREV_NOT_FOUND");
            return true;
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("world", rsb.getWorld().getName());
        set.put("x", rsb.getX());
        set.put("y", rsb.getY());
        set.put("z", rsb.getZ());
        set.put("direction", rsb.getDirection().toString());
        set.put("submarine", (rsb.isSubmarine()) ? 1 : 0);
        String which = "Fast Return to (" + rsb.getWorld().getName() + ":" + rsb.getX() + ":" + rsb.getY() + ":" + rsb.getZ() + ")";
        HashMap<String, Object> tid = new HashMap<>();
        tid.put("tardis_id", id);
        plugin.getQueryFactory().doSyncUpdate("next", set, tid);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_LOADED", which, !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
        plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), TravelType.BACK));
        plugin.getTrackerKeeper().getRescue().remove(id);
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            new TARDISLand(plugin, id, player).exitVortex();
        }
        return true;
    }
}
