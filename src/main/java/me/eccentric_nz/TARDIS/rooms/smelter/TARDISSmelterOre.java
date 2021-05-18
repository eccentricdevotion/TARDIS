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
package me.eccentric_nz.TARDIS.rooms.smelter;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class TARDISSmelterOre {

	public void processItems(Inventory inv, List<Chest> oreChests) {
		// process ore chest contents
		HashMap<Material, Integer> ores = new HashMap<>();
		HashMap<Material, Integer> remainders = new HashMap<>();
		for (ItemStack is : inv.getContents()) {
			if (is != null) {
				Material m = is.getType();
				if (Smelter.isSmeltable(m)) {
					int amount = (ores.containsKey(m)) ? ores.get(m) + is.getAmount() : is.getAmount();
					ores.put(m, amount);
					inv.remove(is);
				}
			}
		}
		// process ores
		int osize = oreChests.size();
		ores.forEach((key, value) -> {
			int remainder = value % osize;
			if (remainder > 0) {
				remainders.put(key, remainder);
			}
			int distrib = value / osize;
			oreChests.forEach((fc) -> {
				HashMap<Integer, ItemStack> leftoverore = fc.getInventory().addItem(new ItemStack(key, distrib));
				if (!leftoverore.isEmpty()) {
					for (ItemStack o : leftoverore.values()) {
						Material om = o.getType();
						int amount = (remainders.containsKey(om)) ? remainders.get(om) + o.getAmount() : o.getAmount();
						remainders.put(om, amount);
					}
				}
			});
		});
		// return remainder to ore chest
		remainders.forEach((key, value) -> {
			int max = key.getMaxStackSize();
			if (value > max) {
				int remainder = value % max;
				for (int i = 0; i < value / max; i++) {
					inv.addItem(new ItemStack(key, max));
				}
				if (remainder > 0) {
					inv.addItem(new ItemStack(key, remainder));
				}
			} else {
				inv.addItem(new ItemStack(key, value));
			}
		});
	}
}
