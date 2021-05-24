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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.chemistry.inventory;

import me.eccentric_nz.tardis.TARDISPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class InventoryHelper implements Listener {

	private final TARDISPlugin plugin;
	private final List<String> INV_TITLES = Arrays.asList("Chemical compounds", "Lab table", "Product crafting", "Material reducer");

	public InventoryHelper(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onChemistryInventoryClose(InventoryCloseEvent event) {
		InventoryView view = event.getView();
		InventoryType type = view.getTopInventory().getType();
		if (type == InventoryType.CHEST) {
			String name = ChatColor.stripColor(view.getTitle());
			if (INV_TITLES.contains(name)) {
				Player player = (Player) event.getPlayer();
				List<ItemStack> leftovers = new ArrayList<>();
				for (ItemStack is : view.getTopInventory().getContents()) {
					if (is != null && !is.getType().equals(Material.BOWL)) {
						leftovers.add(is);
					}
				}
				if (!leftovers.isEmpty()) {
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
						HashMap<Integer, ItemStack> notAdded = player.getInventory().addItem(leftovers.toArray(new ItemStack[0]));
						if (!notAdded.isEmpty()) {
							Location location = player.getLocation();
							for (ItemStack is : notAdded.values()) {
								Objects.requireNonNull(location.getWorld()).dropItemNaturally(location, is);
							}
						}
					}, 1L);
				}
			}
		}
	}
}
