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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
class TARDISFindCommand {

    private final TARDIS plugin;

    TARDISFindCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean findTARDIS(Player player) {
        if (TARDISPermission.hasPermission(player, "tardis.find")) {
            if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), SystemTree.TARDIS_LOCATOR)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "TARDIS Locator");
                return true;
            }
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            if (!rs.fromUUID(player.getUniqueId().toString())) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
                return true;
            }
            if (!plugin.getConfig().getBoolean("difficulty.tardis_locator") || plugin.getUtils().inGracePeriod(player, true)) {
                ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, rs.getTardisId());
                if (rsc.resultSet()) {
                    String world = TARDISAliasResolver.getWorldAlias(rsc.getWorld());
                    if (!plugin.getPlanetsConfig().getBoolean("planets." + rsc.getWorld().getName() + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
                        world = plugin.getMVHelper().getAlias(rsc.getWorld());
                    }
                    if (player.isOp()) {
                        plugin.getMessenger().sendFind(player, world, rsc.getX(), rsc.getY(), rsc.getZ());
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TARDIS_FIND", world + " at x: " + rsc.getX() + " y: " + rsc.getY() + " z: " + rsc.getZ());
                    }
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
                }
                return true;
            } else {
                plugin.getMessenger().sendColouredCommand(player, "DIFF_HARD_FIND", "/tardisrecipe locator", plugin);
                return true;
            }
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return false;
        }
    }
}
