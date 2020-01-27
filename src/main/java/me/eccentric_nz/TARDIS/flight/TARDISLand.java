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
package me.eccentric_nz.TARDIS.flight;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISLand {

    private final TARDIS plugin;
    private final int id;
    private final Player player;

    public TARDISLand(TARDIS plugin, int id, Player player) {
        this.plugin = plugin;
        this.id = id;
        this.player = player;
    }

    public void exitVortex() {
        // get handbrake location
        HashMap<String, Object> whereh = new HashMap<>();
        whereh.put("tardis_id", id);
        whereh.put("type", 0);
        ResultSetControls rsh = new ResultSetControls(plugin, whereh, false);
        if (rsh.resultSet()) {
            Location handbrake = TARDISStaticLocationGetters.getLocationFromBukkitString(rsh.getLocation());
            // materialise
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new TARDISMaterialseFromVortex(plugin, id, player, handbrake), 10L);
        }
    }
}
