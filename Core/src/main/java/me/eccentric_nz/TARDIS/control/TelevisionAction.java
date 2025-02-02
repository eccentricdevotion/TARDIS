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
package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.skins.tv.TVInventory;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TelevisionAction {

    private final TARDIS plugin;

    public TelevisionAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player) {
        ItemStack[] items = new TVInventory().getMenu();
        Inventory tvinv = plugin.getServer().createInventory(player, 36, ChatColor.DARK_RED + "TARDIS Television");
        tvinv.setContents(items);
        player.openInventory(tvinv);
    }
}
