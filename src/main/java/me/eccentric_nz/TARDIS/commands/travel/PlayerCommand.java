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
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.travel.TARDISRescue;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class PlayerCommand {

    private final TARDIS plugin;

    public PlayerCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean action(Player player, String p, int id) {
        if (TARDISPermission.hasPermission(player, "tardis.timetravel.player")) {
            if (plugin.getConfig().getBoolean("difficulty.disks") && !plugin.getUtils().inGracePeriod(player, false)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ADV_PLAYER");
                return true;
            }
            if (player.getName().equalsIgnoreCase(p)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_NO_SELF");
                return true;
            }
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
            if (!rsc.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
                return true;
            }
            // check the player
            Player saved = plugin.getServer().getPlayer(p);
            if (saved == null) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ONLINE");
                return true;
            }
            // check the to player's DND status
            ResultSetPlayerPrefs rspp = new ResultSetPlayerPrefs(plugin, saved.getUniqueId().toString());
            if (rspp.resultSet() && rspp.isDND()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "DND", p);
                return true;
            }
            new TARDISRescue(plugin).rescue(player, saved.getUniqueId(), id, rsc.getCurrent().direction(), false, false);
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_PLAYER");
        }
        return true;
    }
}
