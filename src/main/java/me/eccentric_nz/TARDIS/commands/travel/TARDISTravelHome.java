/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.resultset.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISTravelHome {

    private final TARDIS plugin;

    public TARDISTravelHome(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean action(Player player, int id) {
        // get home location
        HashMap<String, Object> wherehl = new HashMap<>();
        wherehl.put("tardis_id", id);
        ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, wherehl);
        if (!rsh.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "HOME_NOT_FOUND");
            return true;
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("world", rsh.getWorld().getName());
        set.put("x", rsh.getX());
        set.put("y", rsh.getY());
        set.put("z", rsh.getZ());
        set.put("direction", rsh.getDirection().toString());
        set.put("submarine", (rsh.isSubmarine()) ? 1 : 0);
        if (!rsh.getPreset().isEmpty()) {
            // set the chameleon preset
            HashMap<String, Object> wherep = new HashMap<>();
            wherep.put("tardis_id", id);
            HashMap<String, Object> setp = new HashMap<>();
            setp.put("chameleon_preset", rsh.getPreset());
            // set chameleon adaption to OFF
            setp.put("adapti_on", 0);
            plugin.getQueryFactory().doSyncUpdate("tardis", setp, wherep);
        }
        HashMap<String, Object> tid = new HashMap<>();
        tid.put("tardis_id", id);
        plugin.getQueryFactory().doSyncUpdate("next", set, tid);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_LOADED", "Home", !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
        plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), TravelType.HOME));
        plugin.getTrackerKeeper().getRescue().remove(id);
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            new TARDISLand(plugin, id, player).exitVortex();
        }
        return true;
    }
}
