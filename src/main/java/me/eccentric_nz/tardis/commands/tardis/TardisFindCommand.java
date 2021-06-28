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
package me.eccentric_nz.tardis.commands.tardis;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisId;
import me.eccentric_nz.tardis.enumeration.Difficulty;
import me.eccentric_nz.tardis.enumeration.WorldManager;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.planets.TardisAliasResolver;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TardisFindCommand {

    private final TardisPlugin plugin;

    TardisFindCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    boolean findTardis(Player player) {
        if (TardisPermission.hasPermission(player, "tardis.find")) {
            ResultSetTardisId rs = new ResultSetTardisId(plugin);
            if (!rs.fromUuid(player.getUniqueId().toString())) {
                TardisMessage.send(player, "NO_TARDIS");
                return true;
            }
            if (plugin.getDifficulty().equals(Difficulty.EASY) || plugin.getUtils().inGracePeriod(player, true)) {
                HashMap<String, Object> wherecl = new HashMap<>();
                wherecl.put("tardis_id", rs.getTardisId());
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                if (rsc.resultSet()) {
                    String world = TardisAliasResolver.getWorldAlias(rsc.getWorld());
                    if (plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
                        world = plugin.getMultiverseHelper().getAlias(rsc.getWorld());
                    }
                    TardisMessage.send(player, "TARDIS_FIND", world + " at x: " + rsc.getX() + " y: " + rsc.getY() + " z: " + rsc.getZ());
                } else {
                    TardisMessage.send(player, "CURRENT_NOT_FOUND");
                }
            } else {
                TardisMessage.send(player, "DIFF_HARD_FIND", ChatColor.AQUA + "/tardisrecipe locator" + ChatColor.RESET);
            }
            return true;
        } else {
            TardisMessage.send(player, "NO_PERMS");
            return false;
        }
    }
}
