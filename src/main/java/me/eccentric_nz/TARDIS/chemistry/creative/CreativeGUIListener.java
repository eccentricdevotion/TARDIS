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
package me.eccentric_nz.TARDIS.chemistry.creative;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chemistry.element.ElementInventory;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class CreativeGUIListener implements Listener {

	private final TARDIS plugin;

	public CreativeGUIListener(TARDIS plugin) {
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true)
	public void onCreativeMenuClick(InventoryClickEvent event) {
		InventoryView view = event.getView();
		String name = view.getTitle();
		if (name.equals(ChatColor.DARK_RED + "Molecular compounds") || name.equals(ChatColor.DARK_RED + "Products")) {
			Player p = (Player) event.getWhoClicked();
			int slot = event.getRawSlot();
			if (slot >= 0 && slot < 54) {
				switch (slot) {
					case 35:
						event.setCancelled(true);
						// switch to elements
						close(p);
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							ItemStack[] emenu = new ElementInventory(plugin).getMenu();
							Inventory elements = plugin.getServer().createInventory(p, 54, ChatColor.DARK_RED + "Atomic elements");
							elements.setContents(emenu);
							p.openInventory(elements);
						}, 2L);
						break;
					case 44:
						event.setCancelled(true);
						boolean molecular = (name.equals(ChatColor.DARK_RED + "Molecular compounds"));
						close(p);
						// switch to compounds or products
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							ItemStack[] stacks = (molecular) ? new ProductsCreativeInventory(plugin).getMenu() : new CompoundsCreativeInventory(plugin).getMenu();
							Inventory inventory = plugin.getServer().createInventory(p, 54, ChatColor.DARK_RED + (molecular ? "Products" : "Molecular compounds"));
							inventory.setContents(stacks);
							p.openInventory(inventory);
						}, 2L);
						break;
					case 53:
						// close
						event.setCancelled(true);
						close(p);
						break;
					default:
						event.setCancelled(true);
						// get clicked ItemStack
						if (view.getItem(slot) != null) {
							ItemStack choice = view.getItem(slot).clone();
							choice.setAmount(event.getClick().equals(ClickType.SHIFT_LEFT) ? 64 : 1);
							// add ItemStack to inventory if there is room
							p.getInventory().addItem(choice);
						}
						break;
				}
			} else {
				ClickType click = event.getClick();
				if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
					event.setCancelled(true);
				}
			}
		}
	}

	/**
	 * Closes the inventory.
	 *
	 * @param p the player using the GUI
	 */
	private void close(Player p) {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, p::closeInventory, 1L);
	}
}
