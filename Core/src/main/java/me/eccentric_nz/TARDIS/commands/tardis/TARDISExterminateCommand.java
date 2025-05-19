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
import me.eccentric_nz.TARDIS.destroyers.TARDISExterminator;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.floodgate.FloodgateExterminateForm;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
class TARDISExterminateCommand {

    private final TARDIS plugin;

    TARDISExterminateCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean doExterminate(Player player, boolean messagePlayer) {
        if (TARDISPermission.hasPermission(player, "tardis.exterminate")) {
            if (messagePlayer) {
                // must be outside TARDIS to run this command
                if (plugin.getUtils().inTARDISWorld(player)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TARDIS_OUTSIDE");
                    return true;
                }
                plugin.getMessenger().send(player, TardisModule.TARDIS, "EXTERMINATE_CHECK");
                plugin.getMessenger().sendExterminate(player, plugin);
                // open floodgate gui
                if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new FloodgateExterminateForm(plugin, player.getUniqueId()).send(), 2L);
                }
                return true;
            } else {
                return new TARDISExterminator(plugin).playerExterminate(player);
            }
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_DELETE");
            return true;
        }
    }
}
