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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.listeners;

import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.builders.TARDISBuildData;
import me.eccentric_nz.tardis.builders.TARDISSeedBlockProcessor;
import me.eccentric_nz.tardis.custommodeldata.TARDISMushroomBlockData;
import me.eccentric_nz.tardis.custommodeldata.TARDISSeedModel;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.enumeration.Consoles;
import me.eccentric_nz.tardis.enumeration.Schematic;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.utility.TARDISStringUtils;
import org.bukkit.*;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * @author eccentric_nz
 */
public class TARDISSeedBlockListener implements Listener {

	private final TARDISPlugin plugin;

	public TARDISSeedBlockListener(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Store the tardis Seed block's values for use when clicked with the tardis key to activate growing, or to return
	 * the block if broken.
	 *
	 * @param event The tardis Seed block placement event
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onSeedBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		ItemStack is = player.getInventory().getItemInMainHand();
		if (!is.hasItemMeta()) {
			return;
		}
		ItemMeta im = is.getItemMeta();
		assert im != null;
		if (!im.hasDisplayName() || !im.hasLore()) {
			return;
		}
		if (im.getDisplayName().equals(ChatColor.GOLD + "tardis Seed Block")) {
			if (im.getPersistentDataContainer().has(plugin.getCustomBlockKey(), PersistentDataType.INTEGER)) {
				int which = im.getPersistentDataContainer().get(plugin.getCustomBlockKey(), PersistentDataType.INTEGER);
				MultipleFacing multipleFacing;
				if (which >= 42 && which <= 45) {
					multipleFacing = (MultipleFacing) plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(which));
				} else {
					multipleFacing = (MultipleFacing) plugin.getServer().createBlockData(TARDISMushroomBlockData.RED_MUSHROOM_DATA.get(which));
				}
				event.getBlockPlaced().setBlockData(multipleFacing);
			}
			List<String> lore = im.getLore();
			assert lore != null;
			Schematic schm = Consoles.getBY_NAMES().get(lore.get(0));
			Material wall = Material.valueOf(TARDISStringUtils.getValuesFromWallString(lore.get(1)));
			Material floor = Material.valueOf(TARDISStringUtils.getValuesFromWallString(lore.get(2)));
			TARDISBuildData seed = new TARDISBuildData();
			seed.setSchematic(schm);
			seed.setWallType(wall);
			seed.setFloorType(floor);
			Location l = event.getBlockPlaced().getLocation();
			plugin.getBuildKeeper().getTrackTARDISSeed().put(l, seed);
			TARDISMessage.send(player, "SEED_PLACE");
			// now the player has to click the block with the tardis key
		}
	}

	/**
	 * Return the tardis seed block to the player after it is broken.
	 *
	 * @param event a block break event
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)

	public void onSeedBlockBreak(BlockBreakEvent event) {
		Location l = event.getBlock().getLocation();
		Player p = event.getPlayer();
		if (plugin.getBuildKeeper().getTrackTARDISSeed().containsKey(l)) {
			if (!p.getGameMode().equals(GameMode.CREATIVE)) {
				// get the Seed block data
				TARDISBuildData data = plugin.getBuildKeeper().getTrackTARDISSeed().get(l);
				// drop a tardis Seed Block
				World w = l.getWorld();
				ItemStack is = new ItemStack(event.getBlock().getType(), 1);
				ItemMeta im = is.getItemMeta();
				if (im == null) {
					return;
				}
				String console = data.getSchematic().getPermission().toUpperCase(Locale.ENGLISH);
				int model;
				if (TARDISSeedModel.consoleMap.containsKey(console)) {
					model = TARDISSeedModel.modelByString(console);
				} else {
					model = 45;
				}
				im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, model);
				im.setDisplayName(ChatColor.GOLD + "tardis Seed Block");
				im.setCustomModelData(10000000 + model);
				List<String> lore = new ArrayList<>();
				lore.add(console);
				lore.add("Walls: " + data.getWallType().toString());
				lore.add("Floors: " + data.getFloorType().toString());
				im.setLore(lore);
				is.setItemMeta(im);
				// set the block to AIR
				event.getBlock().setBlockData(TARDISConstants.AIR);
				assert w != null;
				w.dropItemNaturally(l, is);
			}
			plugin.getBuildKeeper().getTrackTARDISSeed().remove(l);
		}
	}

	/**
	 * Process the tardis seed block and turn it into a tardis!
	 *
	 * @param event a block interact event
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onSeedInteract(PlayerInteractEvent event) {
		if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
			return;
		}
		if (event.getClickedBlock() != null) {
			Location l = event.getClickedBlock().getLocation();
			if (plugin.getBuildKeeper().getTrackTARDISSeed().containsKey(l)) {
				Player player = event.getPlayer();
				String key;
				ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
				if (rsp.resultSet()) {
					key = (!rsp.getKey().isEmpty()) ? rsp.getKey() : plugin.getConfig().getString("preferences.key");
				} else {
					key = plugin.getConfig().getString("preferences.key");
				}
				if (player.getInventory().getItemInMainHand().getType().equals(Material.valueOf(key))) {
					if (!plugin.getPlanetsConfig().getBoolean(
							"planets." + Objects.requireNonNull(l.getWorld()).getName() + ".time_travel")) {
						TARDISMessage.send(player, "WORLD_NO_TARDIS");
						return;
					}
					if (!Objects.equals(plugin.getConfig().getString("creation.area"), "none")) {
						String area = plugin.getConfig().getString("creation.area");
						if (plugin.getTardisArea().areaCheckInExile(area, l)) {
							TARDISMessage.send(player, "TARDIS_ONLY_AREA", area);
							return;
						}
					}
					// grow a tardis
					TARDISBuildData seed = plugin.getBuildKeeper().getTrackTARDISSeed().get(l);
					// process seed data
					if (new TARDISSeedBlockProcessor(plugin).processBlock(seed, l, player)) {
						// remove seed data
						plugin.getBuildKeeper().getTrackTARDISSeed().remove(l);
						// replace seed block with animated grow block
						MultipleFacing multipleFacing = (MultipleFacing) plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(55));
						event.getClickedBlock().setBlockData(multipleFacing);
					}
				}
			}
		}
	}
}
