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
package me.eccentric_nz.tardis.commands.preferences;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.custommodeldata.GUIInteriorSounds;
import me.eccentric_nz.tardis.enumeration.Hum;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Administrator of Solos is the Earth Empire's civilian overseer for that planet.
 *
 * @author eccentric_nz
 */
class TARDISHumInventory {

	private final TARDISPlugin plugin;
	private final ItemStack[] sounds;

	TARDISHumInventory(TARDISPlugin plugin) {
		this.plugin = plugin;
		sounds = getItemStack();
	}

	/**
	 * Constructs an inventory for the Player Preferences Menu GUI.
	 *
	 * @return an Array of itemStacks (an inventory)
	 */

	private ItemStack[] getItemStack() {
		List<ItemStack> options = new ArrayList<>();
		// get HUM sounds
		for (Hum hum : Hum.values()) {
			ItemStack is = new ItemStack(Material.BOWL, 1);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(hum.toString());
			im.setCustomModelData(GUIInteriorSounds.valueOf(hum.toString()).getCustomModelData());
			is.setItemMeta(im);
			options.add(is);
		}
		// add to stack
		ItemStack[] stack = new ItemStack[18];
		for (int s = 0; s < 17; s++) {
			if (s < options.size()) {
				stack[s] = options.get(s);
			} else {
				stack[s] = null;
			}
		}
		// play / save
		ItemStack play = new ItemStack(Material.BOWL, 1);
		ItemMeta save = play.getItemMeta();
		save.setDisplayName("Action");
		save.setLore(Collections.singletonList("PLAY"));
		save.setCustomModelData(GUIInteriorSounds.ACTION.getCustomModelData());
		play.setItemMeta(save);
		stack[15] = play;
		// close
		ItemStack close = new ItemStack(Material.BOWL, 1);
		ItemMeta c_im = close.getItemMeta();
		c_im.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
		c_im.setCustomModelData(GUIInteriorSounds.CLOSE.getCustomModelData());
		close.setItemMeta(c_im);
		stack[17] = close;

		return stack;
	}

	public ItemStack[] getSounds() {
		return sounds;
	}
}
