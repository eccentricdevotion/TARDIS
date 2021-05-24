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
package me.eccentric_nz.tardis.chemistry.lab;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.blueprints.TARDISPermission;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LabCommand {

	private final TARDISPlugin plugin;

	public LabCommand(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean combine(Player player) {
		if (!TARDISPermission.hasPermission(player, "tardis.lab.combine")) {
			TARDISMessage.send(player, "CHEMISTRY_SUB_PERM", "Lab");
			return true;
		}
		// do stuff
		ItemStack[] menu = new LabInventory(plugin).getMenu();
		Inventory products = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Lab table");
		products.setContents(menu);
		player.openInventory(products);
		return true;
	}
}
