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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.chemistry.lab;

import me.eccentric_nz.tardis.TARDIS;
import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.api.Parameters;
import me.eccentric_nz.tardis.enumeration.Flag;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Objects;

public class IceBombListener implements Listener {

	private final TARDIS plugin;

	public IceBombListener(TARDIS plugin) {
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true)
	public void onIceBombThrow(ProjectileLaunchEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Snowball snowball) {
			ProjectileSource shooter = snowball.getShooter();
			if (shooter instanceof Player player) {
				ItemStack is = player.getInventory().getItemInMainHand();
				if (is.getType() == Material.SNOWBALL && is.hasItemMeta() && Objects.requireNonNull(is.getItemMeta()).hasDisplayName() && is.getItemMeta().getDisplayName().equals("Ice Bomb")) {
					snowball.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, "Ice_Bomb");
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onIceBombHit(ProjectileHitEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Snowball && entity.getPersistentDataContainer().has(plugin.getCustomBlockKey(), PersistentDataType.STRING)) {
			Block block = event.getHitBlock();
			if (block != null) {
				Block up = block.getRelative(BlockFace.UP);
				if (up.getType().equals(Material.WATER)) {
					// should really do some fancy vector math to get the first water block that the snowball entered
					while (up.getType().equals(Material.WATER)) {
						up = up.getRelative(BlockFace.UP);
					}
					up = up.getRelative(BlockFace.DOWN);
					// check plugin respect
					if (plugin.getPluginRespect().getRespect(block.getLocation(), new Parameters((Player) event.getEntity().getShooter(), Flag.getNoMessageFlags()))) {
						// freeze water
						up.setBlockData(TARDISConstants.ICE);
						for (BlockFace face : plugin.getGeneralKeeper().getSurrounding()) {
							Block water = up.getRelative(face);
							if (water.getType().equals(Material.WATER)) {
								water.setBlockData(TARDISConstants.ICE);
							}
						}
					}
				}
			}
		}
	}
}
