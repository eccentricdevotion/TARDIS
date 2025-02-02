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
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetBackLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class FastReturnAction {

    private final TARDIS plugin;

    public FastReturnAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void clickButton(Player player, int id, Tardis tardis) {
        if (plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id) || (!tardis.isHandbrakeOn() && !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) || plugin.getTrackerKeeper().getHasRandomised().contains(id)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_TRAVELLING");
            return;
        }
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            plugin.getTrackerKeeper().getHasRandomised().add(id);
        }
        int cost = plugin.getArtronConfig().getInt("travel");
        if (tardis.getArtronLevel() < cost) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ENOUGH_ENERGY");
            return;
        }
        HashMap<String, Object> wherebl = new HashMap<>();
        wherebl.put("tardis_id", id);
        ResultSetBackLocation rsb = new ResultSetBackLocation(plugin, wherebl);
        if (rsb.resultSet()) {
            ResultSetCurrentFromId rscu = new ResultSetCurrentFromId(plugin, id);
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
                        new TARDISLand(plugin, id, player).exitVortex();
                        plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.BACK, id));
                    }
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "PREV_SET", rsb.getWorld().getName() + ":" + rsb.getX() + ":" + rsb.getY() + ":" + rsb.getZ(), !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_NO_BACK");
                }
            }
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "PREV_NOT_FOUND");
        }
    }

    private boolean compareCurrentToBack(ResultSetCurrentFromId c, ResultSetBackLocation b) {
        return (c.getWorld().equals(b.getWorld()) && c.getX() == b.getX() && c.getY() == b.getY() && c.getZ() == b.getZ());
    }
}
