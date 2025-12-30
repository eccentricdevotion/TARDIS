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
package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.ARS.TARDISARSInventory;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.control.TARDISThemeButton;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import org.bukkit.entity.Player;

public class ARSAction {
    private final TARDIS plugin;

    public ARSAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player, Tardis tardis, TARDISCircuitChecker tcc, int id) {
        if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), SystemTree.ROOM_GROWING)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Room Growing");
            return;
        }
        if (!tardis.isHandbrakeOn()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARS_NO_TRAVEL");
            return;
        }
        // check they're in a compatible world
        if (!plugin.getUtils().canGrowRooms(tardis.getChunk())) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ROOM_OWN_WORLD");
            return;
        }
        if (player.isSneaking()) {
            // check they have permission to change the desktop
            if (!TARDISPermission.hasPermission(player, "tardis.upgrade")) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_UPGRADE");
                return;
            }
            if (tcc != null && !tcc.hasARS() && !plugin.getUtils().inGracePeriod(player, true)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ARS_MISSING");
                return;
            }
            // upgrade menu
            new TARDISThemeButton(plugin, player, tardis.getSchematic(), tardis.getArtronLevel(), id).clickButton();
        } else {
            // check they have permission to grow rooms
            if (!TARDISPermission.hasPermission(player, "tardis.architectural")) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_ROOMS");
                return;
            }
            if (tcc != null && !tcc.hasARS() && !plugin.getUtils().inGracePeriod(player, true)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ARS_MISSING");
                return;
            }
            player.openInventory(new TARDISARSInventory(plugin, player).getInventory());
        }
    }
}
