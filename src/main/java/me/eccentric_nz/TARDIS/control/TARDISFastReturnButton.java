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
package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetBackLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISFastReturnButton {

    private final TARDIS plugin;
    private final Player player;
    private final int id;
    private final int level;

    public TARDISFastReturnButton(TARDIS plugin, Player player, int id, int level) {
        this.plugin = plugin;
        this.player = player;
        this.id = id;
        this.level = level;
    }

    public void clickButton() {
        int cost = plugin.getArtronConfig().getInt("travel");
        if (level < cost) {
            TARDISMessage.send(player, "NOT_ENOUGH_ENERGY");
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
                    plugin.getTrackerKeeper().getHasDestination().put(id, cost);
                    plugin.getTrackerKeeper().getRescue().remove(id);
                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                        new TARDISLand(plugin, id, player).exitVortex();
                    }
                    TARDISMessage.send(player, "PREV_SET", rsb.getWorld().getName() + ":" + rsb.getX() + ":" + rsb.getY() + ":" + rsb.getZ(), !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                } else {
                    TARDISMessage.send(player, "TRAVEL_NO_BACK");
                }
            }
        } else {
            TARDISMessage.send(player, "PREV_NOT_FOUND");
        }
    }

    private boolean compareCurrentToBack(ResultSetCurrentLocation c, ResultSetBackLocation b) {
        return (c.getWorld().equals(b.getWorld()) && c.getX() == b.getX() && c.getY() == b.getY() && c.getZ() == b.getZ());
    }
}
