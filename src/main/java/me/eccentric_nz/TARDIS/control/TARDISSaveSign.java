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
package me.eccentric_nz.tardis.control;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.advanced.TARDISCircuitChecker;
import me.eccentric_nz.tardis.enumeration.Difficulty;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.travel.TARDISSaveSignInventory;
import me.eccentric_nz.tardis.utility.TARDISNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

class TARDISSaveSign {

	private final TARDISPlugin plugin;

	TARDISSaveSign(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	void openGUI(Player player, int id) {
		TARDISCircuitChecker tcc = null;
		if (!plugin.getDifficulty().equals(Difficulty.EASY) && !plugin.getUtils().inGracePeriod(player, false)) {
			tcc = new TARDISCircuitChecker(plugin, id);
			tcc.getCircuits();
		}
		if (tcc != null && !tcc.hasMemory()) {
			TARDISMessage.send(player, "NO_MEM_CIRCUIT");
			return;
		}
		if (plugin.getTrackerKeeper().getJunkPlayers().containsKey(player.getUniqueId()) &&
			plugin.getDifficulty().equals(Difficulty.HARD)) {
			ItemStack disk = player.getInventory().getItemInMainHand();
			if (disk.hasItemMeta() && Objects.requireNonNull(disk.getItemMeta()).hasDisplayName() &&
				disk.getItemMeta().getDisplayName().equals("Save Storage Disk")) {
				List<String> lore = disk.getItemMeta().getLore();
				assert lore != null;
				if (!lore.get(0).equals("Blank")) {
					// read the lore from the disk
					String world = lore.get(1);
					int x = TARDISNumberParsers.parseInt(lore.get(2));
					int y = TARDISNumberParsers.parseInt(lore.get(3));
					int z = TARDISNumberParsers.parseInt(lore.get(4));
					HashMap<String, Object> set_next = new HashMap<>();
					set_next.put("world", world);
					set_next.put("x", x);
					set_next.put("y", y);
					set_next.put("z", z);
					set_next.put("direction", lore.get(6));
					boolean sub = Boolean.parseBoolean(lore.get(7));
					set_next.put("submarine", (sub) ? 1 : 0);
					TARDISMessage.send(player, "LOC_SET", true);
					// update next
					HashMap<String, Object> where_next = new HashMap<>();
					where_next.put("tardis_id", id);
					plugin.getQueryFactory().doSyncUpdate("next", set_next, where_next);
					plugin.getTrackerKeeper().getHasDestination().put(id, plugin.getArtronConfig().getInt("travel"));
				}
			} else {
				TARDISSaveSignInventory sst = new TARDISSaveSignInventory(plugin, id, player);
				ItemStack[] items = sst.getTerminal();
				Inventory inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS saves");
				inv.setContents(items);
				player.openInventory(inv);
			}
		} else {
			TARDISSaveSignInventory sst = new TARDISSaveSignInventory(plugin, id, player);
			ItemStack[] items = sst.getTerminal();
			Inventory inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS saves");
			inv.setContents(items);
			player.openInventory(inv);
		}
	}
}
