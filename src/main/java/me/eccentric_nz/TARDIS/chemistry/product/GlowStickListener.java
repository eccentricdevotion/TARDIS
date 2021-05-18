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
package me.eccentric_nz.TARDIS.chemistry.product;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class GlowStickListener implements Listener {

	private final NamespacedKey namespacedKey;

	public GlowStickListener(TARDIS plugin) {
		namespacedKey = new NamespacedKey(plugin, "glow_stick_time");
	}

	@EventHandler
	public void onGlowStickUse(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			Player player = event.getPlayer();
			ItemStack is = event.getItem();
			if (is != null && GlowStickMaterial.isCorrectMaterial(is.getType()) && is.hasItemMeta()) {
				ItemMeta im = is.getItemMeta();
				if (im.hasDisplayName() && im.getDisplayName().endsWith("Glow Stick") && im.hasCustomModelData() && !im.hasEnchant(Enchantment.LOYALTY)) {
					player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_HIT, 1.0f, 1.0f);
					// switch custom data models e.g. 10000008 -> 12000008
					int cmd = im.getCustomModelData() + 2000000;
					im.setCustomModelData(cmd);
					im.addEnchant(Enchantment.LOYALTY, 1, true);
					im.getPersistentDataContainer().set(namespacedKey, PersistentDataType.INTEGER, 100);
					is.setItemMeta(im);
				}
			}
		}
	}
}
