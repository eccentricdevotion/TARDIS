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
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.travel.TARDISRescue;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISTravelPlayer {

    private final TARDIS plugin;

    public TARDISTravelPlayer(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean action(Player player, String p, int id) {
        if (TARDISPermission.hasPermission(player, "tardis.timetravel.player")) {
            if (!plugin.getDifficulty().equals(Difficulty.EASY) && !plugin.getUtils().inGracePeriod(player, false)) {
                TARDISMessage.send(player, "ADV_PLAYER");
                return true;
            }
            if (player.getName().equalsIgnoreCase(p)) {
                TARDISMessage.send(player, "TRAVEL_NO_SELF");
                return true;
            }
            HashMap<String, Object> wherecl = new HashMap<>();
            wherecl.put("tardis_id", id);
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
            if (!rsc.resultSet()) {
                TARDISMessage.send(player, "CURRENT_NOT_FOUND");
                return true;
            }
            // check the player
            Player saved = plugin.getServer().getPlayer(p);
            if (saved == null) {
                TARDISMessage.send(player, "NOT_ONLINE");
                return true;
            }
            // check the to player's DND status
            ResultSetPlayerPrefs rspp = new ResultSetPlayerPrefs(plugin, saved.getUniqueId().toString());
            if (rspp.resultSet() && rspp.isDND()) {
                TARDISMessage.send(player, "DND", p);
                return true;
            }
            new TARDISRescue(plugin).rescue(player, saved.getUniqueId(), id, rsc.getDirection(), false, false);
        } else {
            TARDISMessage.send(player, "NO_PERM_PLAYER");
        }
        return true;
    }
}
