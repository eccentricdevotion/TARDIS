/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.desktop.TARDISThemeInventory;
import me.eccentric_nz.TARDIS.desktop.TARDISUpgradeData;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author eccentric_nz
 */
public class TARDISThemeButton {

    private final TARDIS plugin;
    private final Player player;
    private final SCHEMATIC current_console;
    private final int level;

    public TARDISThemeButton(TARDIS plugin, Player player, SCHEMATIC current_console, int level) {
        this.plugin = plugin;
        this.player = player;
        this.current_console = current_console;
        this.level = level;
    }

    public void clickButton() {
        // check they have permission to change the desktop
        if (!player.hasPermission("tardis.upgrade")) {
            TARDISMessage.send(player, "NO_PERM_UPGRADE");
            return;
        }
        // get player's current console
        TARDISUpgradeData tud = new TARDISUpgradeData();
        tud.setPrevious(current_console);
        tud.setLevel(level);
        plugin.getTrackerKeeper().getUpgrades().put(player.getUniqueId(), tud);
        // open the upgrade menu
        ItemStack[] consoles = new TARDISThemeInventory(plugin, player, current_console.toString(), level).getMenu();
        Inventory upg = plugin.getServer().createInventory(player, 27, "§4TARDIS Upgrade Menu");
        upg.setContents(consoles);
        player.openInventory(upg);
    }
}
