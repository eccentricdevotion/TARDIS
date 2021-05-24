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
package me.eccentric_nz.tardis.advanced;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.enumeration.BiomeLookup;
import me.eccentric_nz.tardis.enumeration.PRESET;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISDiskCraftListener implements Listener {

	private final TARDISPlugin plugin;
	private final List<InventoryAction> actions = new ArrayList<>();

	public TARDISDiskCraftListener(TARDISPlugin plugin) {
		this.plugin = plugin;
		actions.add(InventoryAction.PLACE_ALL);
		actions.add(InventoryAction.PLACE_ONE);
		actions.add(InventoryAction.PLACE_SOME);
		actions.add(InventoryAction.SWAP_WITH_CURSOR);
	}

	@EventHandler(ignoreCancelled = true)
	public void onCraftBiomePresetDisk(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();
		int slot = event.getRawSlot();
		if (inv.getType().equals(InventoryType.WORKBENCH) && slot < 10 && actions.contains(event.getAction())) {
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				if (checkSlots(inv)) {
					// get the other ingredients
					List<ItemStack> items = getOtherItems(inv);
					ItemStack disk;
					if (inv.contains(Material.MUSIC_DISC_CAT)) {
						// check it is a Biome Storage Disk
						ItemStack is = inv.getItem(inv.first(Material.MUSIC_DISC_CAT));
						if (is != null && is.hasItemMeta()) {
							ItemMeta im = is.getItemMeta();
							if (im.hasDisplayName() && im.getDisplayName().equals("Biome Storage Disk") && im.hasLore()) {
								List<String> lore = im.getLore();
								int ladder = inv.first(Material.LADDER);
								if (lore.get(0).equals("Blank")) {
									List<String> disk_lore = new ArrayList<>();
									// biome disk
									if (items.size() > 1 && ladder > 0) {
										// mega biome
										items.remove(inv.getItem(ladder));
										String lookup = items.get(0).getType() + "_B";
										try {
											String biome = BiomeLookup.valueOf(lookup).getUpper();
											disk_lore.add(biome);
										} catch (IllegalArgumentException e) {
											plugin.debug("Could not get biome from craft item! " + e);
										}
									} else {
										// regular biome
										String lookup = items.get(0).getType() + "_B";
										try {
											String biome = BiomeLookup.valueOf(lookup).getRegular();
											disk_lore.add(biome);
										} catch (IllegalArgumentException e) {
											plugin.debug("Could not get biome from craft item! " + e);
										}
									}
									if (disk_lore.size() > 0) {
										disk = new ItemStack(Material.MUSIC_DISC_CAT, 1);
										ItemMeta dim = disk.getItemMeta();
										dim.setDisplayName("Biome Storage Disk");
										dim.setLore(disk_lore);
										dim.setCustomModelData(10000001);
										disk.setItemMeta(dim);
										inv.setItem(0, disk);
										player.updateInventory();
									}
								} else if (BiomeLookup.BY_REG.containsKey(lore.get(0)) && ladder > 0) {
									// upgrade to mega biome
									List<String> disk_lore = new ArrayList<>();
									try {
										String biome = BiomeLookup.getBiome(lore.get(0)).getUpper();
										disk_lore.add(biome);
										disk = new ItemStack(Material.MUSIC_DISC_CAT, 1);
										ItemMeta dim = disk.getItemMeta();
										dim.setDisplayName("Biome Storage Disk");
										dim.setLore(disk_lore);
										dim.setCustomModelData(10000001);
										disk.setItemMeta(dim);
										inv.setItem(0, disk);
										player.updateInventory();
									} catch (IllegalArgumentException e) {
										plugin.debug("Could not get biome from craft item! " + e);
									}
								} else {
									TARDISMessage.send(player, "DISK_BLANK_BIOME");
								}
							}
						}
					} else {
						// check it is a Preset Storage Disk
						ItemStack is = inv.getItem(inv.first(Material.MUSIC_DISC_MALL));
						if (is != null && is.hasItemMeta()) {
							ItemMeta im = is.getItemMeta();
							if (im.hasDisplayName() && im.getDisplayName().equals("Preset Storage Disk") && im.hasLore()) {
								List<String> lore = im.getLore();
								if (lore.get(0).equals("Blank")) {
									// preset disk
									if (items.size() > 0) {
										Material m = items.get(0).getType();
										String preset = "";
										if (PRESET.getPreset(m) != null) {
											preset = PRESET.getPreset(m).toString();
										}
										if (!preset.isEmpty()) {
											List<String> disk_lore = Collections.singletonList(preset);
											disk = new ItemStack(Material.MUSIC_DISC_MALL, 1);
											ItemMeta dim = disk.getItemMeta();
											dim.setDisplayName("Preset Storage Disk");
											dim.setLore(disk_lore);
											dim.setCustomModelData(10000001);
											disk.setItemMeta(dim);
											inv.setItem(0, disk);
											player.updateInventory();
										}
									}
								} else {
									TARDISMessage.send(player, "DISK_BLANK_PRESET");
								}
							}
						}
					}
				}
			}, 3L);
		}
	}

	private boolean checkSlots(Inventory inv) {
		boolean check = false;
		int count = 0;
		for (int i = 1; i < 10; i++) {
			if (inv.getItem(i) != null && !inv.getItem(i).getType().isAir()) {
				count++;
			}
		}
		if ((inv.contains(Material.MUSIC_DISC_CAT) || inv.contains(Material.MUSIC_DISC_MALL)) && count > 1) {
			check = true;
		}
		return check;
	}

	private List<ItemStack> getOtherItems(Inventory inv) {
		List<ItemStack> items = new ArrayList<>();
		for (ItemStack is : inv.getContents()) {
			if (is != null) {
				Material m = is.getType();
				if (!m.equals(Material.MUSIC_DISC_CAT) && !m.equals(Material.MUSIC_DISC_MALL) && !m.isAir()) {
					items.add(is);
				}
			}
		}
		return items;
	}
}
