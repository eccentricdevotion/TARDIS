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
package me.eccentric_nz.tardis.flight;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetControls;
import me.eccentric_nz.tardis.database.resultset.ResultSetThrottle;
import me.eccentric_nz.tardis.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.tardis.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISLand {

    private final TARDISPlugin plugin;
    private final int id;
    private final Player player;
    private final SpaceTimeThrottle spaceTimeThrottle;

    public TARDISLand(TARDISPlugin plugin, int id, Player player) {
        this.plugin = plugin;
        this.id = id;
        this.player = player;
        spaceTimeThrottle = getThrottle(this.player);
    }

    public void exitVortex() {
        // get handbrake location
        HashMap<String, Object> whereh = new HashMap<>();
        whereh.put("tardis_id", id);
        whereh.put("type", 0);
        whereh.put("secondary", 0);
        ResultSetControls rsh = new ResultSetControls(plugin, whereh, false);
        if (rsh.resultSet()) {
            Location handbrake = TARDISStaticLocationGetters.getLocationFromBukkitString(rsh.getLocation());
            // materialise
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new TARDISMaterialseFromVortex(plugin, id, player, handbrake, spaceTimeThrottle), 10L);
        }
    }

    private SpaceTimeThrottle getThrottle(Player player) {
        return new ResultSetThrottle(plugin).getSpeed(player.getUniqueId().toString());
    }
}
