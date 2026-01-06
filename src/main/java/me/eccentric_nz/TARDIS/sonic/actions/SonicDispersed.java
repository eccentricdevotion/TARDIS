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
package me.eccentric_nz.TARDIS.sonic.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SonicDispersed {

    public static void assemble(TARDIS plugin, Player player) {
        // check player's location
        Location tmp = player.getLocation();
        Location pl = new Location(tmp.getWorld(), tmp.getBlockX(), tmp.getBlockY(), tmp.getBlockZ());
        Location pb = plugin.getTrackerKeeper().getDispersed().get(player.getUniqueId());
        if (pl.equals(pb)) {
            UUID uuid = player.getUniqueId();
            // get TARDIS id
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            if (rs.fromUUID(uuid.toString())) {
                // rebuild
                plugin.getTrackerKeeper().getDispersed().remove(uuid);
                plugin.getTrackerKeeper().getDispersedTARDII().remove(rs.getTardisId());
                player.performCommand("tardis rebuild");
            }
        }
    }
}
