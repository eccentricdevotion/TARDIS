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
package me.eccentric_nz.tardis.chemistry.lab;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.custommodeldata.GUIChemistry;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class LabInventory {

	private final TARDISPlugin plugin;
	private final ItemStack[] menu;

	public LabInventory(TARDISPlugin plugin) {
		this.plugin = plugin;
		menu = getItemStack();
	}

	private ItemStack[] getItemStack() {
		ItemStack[] stack = new ItemStack[27];
		// info
		ItemStack info = new ItemStack(Material.BOWL, 1);
		ItemMeta info_im = info.getItemMeta();
		assert info_im != null;
		info_im.setDisplayName("Info");
		info_im.setLore(Arrays.asList("Combine elements and compounds", "to create bleach, ice bombs", "heat blocks and fertiliser.", "To see a lab table formula", "use the " + ChatColor.GREEN + ChatColor.ITALIC + "/tardischemistry formula" + ChatColor.DARK_PURPLE + ChatColor.ITALIC + " command.", "Place items in the bottom", "row from left to right."));
		info_im.setCustomModelData(GUIChemistry.INFO.getCustomModelData());
		info.setItemMeta(info_im);
		stack[8] = info;
		// check recipe
		ItemStack check = new ItemStack(Material.BOWL, 1);
		ItemMeta check_im = check.getItemMeta();
		assert check_im != null;
		check_im.setDisplayName("Check product");
		check_im.setCustomModelData(GUIChemistry.CHECK.getCustomModelData());
		check.setItemMeta(check_im);
		stack[17] = check;
		// close
		ItemStack close = new ItemStack(Material.BOWL, 1);
		ItemMeta close_im = close.getItemMeta();
		assert close_im != null;
		close_im.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
		close_im.setCustomModelData(GUIChemistry.CLOSE.getCustomModelData());
		close.setItemMeta(close_im);
		stack[26] = close;
		return stack;
	}

	public ItemStack[] getMenu() {
		return menu;
	}
}
