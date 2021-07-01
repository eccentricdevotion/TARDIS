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
package me.eccentric_nz.tardis.control;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.api.event.TardisTravelEvent;
import me.eccentric_nz.tardis.database.resultset.ResultSetBackLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.enumeration.TravelType;
import me.eccentric_nz.tardis.flight.TardisLand;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.travel.TravelCostAndType;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TardisFastReturnButton {

    private final TardisPlugin plugin;
    private final Player player;
    private final int id;
    private final int level;

    TardisFastReturnButton(TardisPlugin plugin, Player player, int id, int level) {
        this.plugin = plugin;
        this.player = player;
        this.id = id;
        this.level = level;
    }

    public void clickButton() {
        int cost = plugin.getArtronConfig().getInt("travel");
        if (level < cost) {
            TardisMessage.send(player, "NOT_ENOUGH_ENERGY");
            return;
        }
        HashMap<String, Object> wherebl = new HashMap<>();
        wherebl.put("tardis_id", id);
        ResultSetBackLocation rsb = new ResultSetBackLocation(plugin, wherebl);
        if (rsb.resultSet()) {
            HashMap<String, Object> wherecu = new HashMap<>();
            wherecu.put("tardis_id", id);
            ResultSetCurrentLocation rscu = new ResultSetCurrentLocation(plugin, wherecu);
            if (rscu.resultSet()) {
                if (!compareCurrentToBack(rscu, rsb)) {
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("world", rsb.getWorld().getName());
                    set.put("x", rsb.getX());
                    set.put("y", rsb.getY());
                    set.put("z", rsb.getZ());
                    set.put("direction", rsb.getDirection().toString());
                    set.put("submarine", (rsb.isSubmarine()) ? 1 : 0);
                    HashMap<String, Object> wherel = new HashMap<>();
                    wherel.put("tardis_id", id);
                    plugin.getQueryFactory().doSyncUpdate("next", set, wherel);
                    plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(cost, TravelType.BACK));
                    plugin.getTrackerKeeper().getRescue().remove(id);
                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                        new TardisLand(plugin, id, player).exitVortex();
                        plugin.getPluginManager().callEvent(new TardisTravelEvent(player, null, TravelType.BACK, id));
                    }
                    TardisMessage.send(player, "PREV_SET", rsb.getWorld().getName() + ":" + rsb.getX() + ":" + rsb.getY() + ":" + rsb.getZ(), !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                } else {
                    TardisMessage.send(player, "TRAVEL_NO_BACK");
                }
            }
        } else {
            TardisMessage.send(player, "PREV_NOT_FOUND");
        }
    }

    private boolean compareCurrentToBack(ResultSetCurrentLocation c, ResultSetBackLocation b) {
        return (c.getWorld().equals(b.getWorld()) && c.getX() == b.getX() && c.getY() == b.getY() && c.getZ() == b.getZ());
    }
}