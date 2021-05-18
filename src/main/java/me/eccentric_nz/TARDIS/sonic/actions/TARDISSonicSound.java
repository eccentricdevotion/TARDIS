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
package me.eccentric_nz.TARDIS.sonic.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

public class TARDISSonicSound {

	private static final HashMap<UUID, Long> timeout = new HashMap<>();

	public static void playSonicSound(TARDIS plugin, Player player, long now, long cooldown, String sound) {
		if ((!timeout.containsKey(player.getUniqueId()) || timeout.get(player.getUniqueId()) < now)) {
			ItemMeta im = player.getInventory().getItemInMainHand().getItemMeta();
			im.addEnchant(Enchantment.DURABILITY, 1, true);
			im.addItemFlags(ItemFlag.values());
			player.getInventory().getItemInMainHand().setItemMeta(im);
			timeout.put(player.getUniqueId(), now + cooldown);
			TARDISSounds.playTARDISSound(player.getLocation(), sound);
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				ItemStack is = player.getInventory().getItemInMainHand();
				if (is.hasItemMeta()) {
					ItemMeta im1 = is.getItemMeta();
					if (im1.hasDisplayName() && ChatColor.stripColor(im1.getDisplayName()).equals("Sonic Screwdriver")) {
						player.getInventory().getItemInMainHand().getEnchantments().keySet().forEach((e) -> player.getInventory().getItemInMainHand().removeEnchantment(e));
					} else {
						// find the screwdriver in the player's inventory
						removeSonicEnchant(plugin, player.getInventory());
					}
				} else {
					// find the screwdriver in the player's inventory
					removeSonicEnchant(plugin, player.getInventory());
				}
			}, (cooldown / 50L));
		}
	}

	private static void removeSonicEnchant(TARDIS plugin, PlayerInventory inv) {
		String result = plugin.getRecipesConfig().getString("shaped.Sonic Screwdriver.result");
		Material sonic = Material.valueOf(result);
		int first = inv.first(sonic);
		if (first < 0) {
			return;
		}
		ItemStack stack = inv.getItem(first);
		if (stack.containsEnchantment(Enchantment.DURABILITY)) {
			stack.getEnchantments().keySet().forEach(stack::removeEnchantment);
		}
	}
}
