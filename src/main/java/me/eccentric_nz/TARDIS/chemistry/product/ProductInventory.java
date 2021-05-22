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
package me.eccentric_nz.tardis.chemistry.product;

import me.eccentric_nz.tardis.TARDIS;
import me.eccentric_nz.tardis.custommodeldata.GUIChemistry;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ProductInventory {

	private final ItemStack[] menu;
	private final TARDIS plugin;

	public ProductInventory(TARDIS plugin) {
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
		info_im.setLore(Arrays.asList("Combine elements and compounds", "to create sparklers, balloons,", "lamps, and glow sticks.", "To see a product formula", "use the " + ChatColor.GREEN + ChatColor.ITALIC + "/tardischemistry formula" + ChatColor.DARK_PURPLE + ChatColor.ITALIC + " command.", "Place items like you would", "in a crafting table", "in the 9 left slots."));
		info_im.setCustomModelData(GUIChemistry.INFO.getCustomModelData());
		info.setItemMeta(info_im);
		stack[8] = info;
		// craft recipe
		ItemStack craft = new ItemStack(Material.BOWL, 1);
		ItemMeta craft_im = craft.getItemMeta();
		assert craft_im != null;
		craft_im.setDisplayName("Craft");
		craft_im.setCustomModelData(GUIChemistry.CRAFT.getCustomModelData());
		craft.setItemMeta(craft_im);
		stack[17] = craft;
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
