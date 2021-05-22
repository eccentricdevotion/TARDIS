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
package me.eccentric_nz.tardis.desktop;

import me.eccentric_nz.tardis.TARDIS;
import me.eccentric_nz.tardis.custommodeldata.GUIWallFloor;
import me.eccentric_nz.tardis.rooms.TARDISWalls;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * By the time of his eleventh incarnation, the Doctor's console room had gone through at least twelve redesigns, though
 * the tardis revealed that she had archived 30 versions. Once a control room was reconfigured, the tardis archived the
 * old design "for neatness". The tardis effectively "curated" a museum of control rooms — both those in the Doctor's
 * personal past and future
 *
 * @author eccentric_nz
 */
class TARDISWallsInventory {

	private final ItemStack[] menu;
	private final TARDIS plugin;

	TARDISWallsInventory(TARDIS plugin) {
		this.plugin = plugin;
		menu = getItemStack();
	}

	/**
	 * Constructs an inventory for the Player Preferences Menu GUI.
	 *
	 * @return an Array of itemStacks (an inventory)
	 */
	private ItemStack[] getItemStack() {
		ItemStack[] stack = new ItemStack[54];
		int i = 0;
		// get BLOCKS
		for (Material entry : TARDISWalls.BLOCKS) {
			if (i > 52) {
				break;
			}
			ItemStack is = new ItemStack(entry, 1);
			stack[i] = is;
			if (i % 9 == 7) {
				i += 2;
			} else {
				i++;
			}
		}

		// scroll up
		ItemStack scroll_up = new ItemStack(Material.ARROW, 1);
		ItemMeta uim = scroll_up.getItemMeta();
		uim.setDisplayName(plugin.getLanguage().getString("BUTTON_SCROLL_U"));
		uim.setCustomModelData(GUIWallFloor.BUTTON_SCROLL_U.getCustomModelData());
		scroll_up.setItemMeta(uim);
		stack[8] = scroll_up;
		// scroll down
		ItemStack scroll_down = new ItemStack(Material.ARROW, 1);
		ItemMeta dim = scroll_down.getItemMeta();
		dim.setDisplayName(plugin.getLanguage().getString("BUTTON_SCROLL_D"));
		dim.setCustomModelData(GUIWallFloor.BUTTON_SCROLL_D.getCustomModelData());
		scroll_down.setItemMeta(dim);
		stack[17] = scroll_down;
		// default wall
		ItemStack wall = new ItemStack(Material.BOWL, 1);
		ItemMeta wim = wall.getItemMeta();
		wim.setDisplayName("Default Wall Block");
		wim.setCustomModelData(GUIWallFloor.WALL.getCustomModelData());
		wall.setItemMeta(wim);
		stack[26] = wall;
		// default floor
		ItemStack floor = new ItemStack(Material.BOWL, 1);
		ItemMeta fim = floor.getItemMeta();
		fim.setDisplayName("Default Floor Block");
		fim.setCustomModelData(GUIWallFloor.FLOOR.getCustomModelData());
		floor.setItemMeta(fim);
		stack[35] = floor;
		// close
		ItemStack close = new ItemStack(Material.BOWL, 1);
		ItemMeta close_im = close.getItemMeta();
		close_im.setDisplayName("Abort upgrade");
		close_im.setCustomModelData(GUIWallFloor.BUTTON_ABORT.getCustomModelData());
		close.setItemMeta(close_im);
		stack[53] = close;

		return stack;
	}

	public ItemStack[] getMenu() {
		return menu;
	}
}
