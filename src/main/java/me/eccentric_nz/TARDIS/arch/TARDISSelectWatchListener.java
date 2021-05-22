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
package me.eccentric_nz.tardis.arch;

import me.eccentric_nz.tardis.TARDIS;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author eccentric_nz
 */
public class TARDISSelectWatchListener implements Listener {

	private final TARDIS plugin;

	public TARDISSelectWatchListener(TARDIS plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onTryToSelectWatch(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		if (!plugin.getTrackerKeeper().getJohnSmith().containsKey(player.getUniqueId())) {
			return;
		}
		if (plugin.getTrackerKeeper().getJohnSmith().get(player.getUniqueId()).getTime() <= System.currentTimeMillis()) {
			return;
		}
		int slot = event.getNewSlot();
		PlayerInventory inv = player.getInventory();
		ItemStack is = inv.getItem(slot);
		if (is == null || !is.getType().equals(Material.CLOCK) || !is.hasItemMeta()) {
			return;
		}
		ItemMeta im = is.getItemMeta();
		if (!im.hasDisplayName() || !im.getDisplayName().equals("Fob Watch")) {
			return;
		}
		// move the fob watch
		int empty = inv.firstEmpty();
		inv.setItem(slot, null);
		inv.setItem(empty, is);
		player.updateInventory();
	}
}
