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
package me.eccentric_nz.tardis.sonic.actions;

import me.eccentric_nz.tardis.TARDIS;
import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.blueprints.TARDISPermission;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Snow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TARDISSonicDisruptor {

	public static void breakBlock(TARDIS plugin, Player player, Block block) {
		// not protected blocks - WorldGuard / GriefPrevention / Lockette / Towny
		if (TARDISSonicRespect.checkBlockRespect(plugin, player, block)) {
			TARDISSonicSound.playSonicSound(plugin, player, System.currentTimeMillis(), 600L, "sonic_short");
			// drop appropriate material
			Material mat = block.getType();
			if (TARDISPermission.hasPermission(player, "tardis.sonic.silktouch")) {
				Location l = block.getLocation();
				if (mat.equals(Material.SNOW)) {
					Snow snow = (Snow) block.getBlockData();
					block.getLocation().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SNOWBALL, 1 + snow.getLayers()));
				} else {
					l.getWorld().dropItemNaturally(l, new ItemStack(block.getType(), 1));
				}
				l.getWorld().playSound(l, Sound.ENTITY_SHEEP_SHEAR, 1.0F, 1.5F);
				// set the block to AIR
				block.setBlockData(TARDISConstants.AIR);
			} else if (mat.equals(Material.SNOW) || mat.equals(Material.SNOW_BLOCK)) {
				// how many?
				int balls;
				if (mat.equals(Material.SNOW_BLOCK)) {
					balls = 4;
				} else {
					Snow snow = (Snow) block.getBlockData();
					balls = 1 + snow.getLayers();
				}
				block.setBlockData(TARDISConstants.AIR);
				block.getLocation().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SNOWBALL, balls));
			} else {
				block.breakNaturally();
				block.getLocation().getWorld().playSound(block.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1.0F, 1.5F);
			}
		} else {
			TARDISMessage.send(player, "SONIC_PROTECT");
		}
	}
}
