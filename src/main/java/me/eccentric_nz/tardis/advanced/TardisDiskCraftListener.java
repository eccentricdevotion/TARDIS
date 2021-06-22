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
package me.eccentric_nz.tardis.advanced;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.enumeration.BiomeLookup;
import me.eccentric_nz.tardis.enumeration.Preset;
import me.eccentric_nz.tardis.messaging.TardisMessage;
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
import java.util.Objects;

/**
 * @author eccentric_nz
 */
public class TardisDiskCraftListener implements Listener {

    private final TardisPlugin plugin;
    private final List<InventoryAction> actions = new ArrayList<>();

    public TardisDiskCraftListener(TardisPlugin plugin) {
        this.plugin = plugin;
        actions.add(InventoryAction.PLACE_ALL);
        actions.add(InventoryAction.PLACE_ONE);
        actions.add(InventoryAction.PLACE_SOME);
        actions.add(InventoryAction.SWAP_WITH_CURSOR);
    }

    @EventHandler(ignoreCancelled = true)
    public void onCraftBiomePresetDisk(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        int slot = event.getRawSlot();
        if (inventory.getType().equals(InventoryType.WORKBENCH) && slot < 10 && actions.contains(event.getAction())) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if (checkSlots(inventory)) {
                    // get the other ingredients
                    List<ItemStack> otherItems = getOtherItems(inventory);
                    ItemStack disk;
                    if (inventory.contains(Material.MUSIC_DISC_CAT)) {
                        // check it is a Biome Storage Disk
                        ItemStack itemStack = inventory.getItem(inventory.first(Material.MUSIC_DISC_CAT));
                        if (itemStack != null && itemStack.hasItemMeta()) {
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            assert itemMeta != null;
                            if (itemMeta.hasDisplayName() && itemMeta.getDisplayName().equals("Biome Storage Disk") && itemMeta.hasLore()) {
                                List<String> lore = itemMeta.getLore();
                                int ladder = inventory.first(Material.LADDER);
                                assert lore != null;
                                if (lore.get(0).equals("Blank")) {
                                    List<String> diskLore = new ArrayList<>();
                                    // biome disk
                                    if (otherItems.size() > 1 && ladder > 0) {
                                        // mega biome
                                        otherItems.remove(inventory.getItem(ladder));
                                        String lookup = otherItems.get(0).getType() + "_B";
                                        try {
                                            String biome = BiomeLookup.valueOf(lookup).getUpper();
                                            diskLore.add(biome);
                                        } catch (IllegalArgumentException illegalArgumentException) {
                                            plugin.debug("Could not get biome from craft item! " + illegalArgumentException);
                                        }
                                    } else {
                                        // regular biome
                                        String lookup = otherItems.get(0).getType() + "_B";
                                        try {
                                            String biome = BiomeLookup.valueOf(lookup).getRegular();
                                            diskLore.add(biome);
                                        } catch (IllegalArgumentException illegalArgumentException) {
                                            plugin.debug("Could not get biome from craft item! " + illegalArgumentException);
                                        }
                                    }
                                    if (diskLore.size() > 0) {
                                        disk = new ItemStack(Material.MUSIC_DISC_CAT, 1);
                                        ItemMeta diskMeta = disk.getItemMeta();
                                        assert diskMeta != null;
                                        diskMeta.setDisplayName("Biome Storage Disk");
                                        diskMeta.setLore(diskLore);
                                        diskMeta.setCustomModelData(10000001);
                                        disk.setItemMeta(diskMeta);
                                        inventory.setItem(0, disk);
                                        player.updateInventory();
                                    }
                                } else if (BiomeLookup.BY_REG.containsKey(lore.get(0)) && ladder > 0) {
                                    // upgrade to mega biome
                                    List<String> diskLore = new ArrayList<>();
                                    try {
                                        String biome = BiomeLookup.getBiome(lore.get(0)).getUpper();
                                        diskLore.add(biome);
                                        disk = new ItemStack(Material.MUSIC_DISC_CAT, 1);
                                        ItemMeta diskMeta = disk.getItemMeta();
                                        assert diskMeta != null;
                                        diskMeta.setDisplayName("Biome Storage Disk");
                                        diskMeta.setLore(diskLore);
                                        diskMeta.setCustomModelData(10000001);
                                        disk.setItemMeta(diskMeta);
                                        inventory.setItem(0, disk);
                                        player.updateInventory();
                                    } catch (IllegalArgumentException illegalArgumentException) {
                                        plugin.debug("Could not get biome from craft item! " + illegalArgumentException);
                                    }
                                } else {
                                    TardisMessage.send(player, "DISK_BLANK_BIOME");
                                }
                            }
                        }
                    } else {
                        // check it is a Preset Storage Disk
                        ItemStack itemStack = inventory.getItem(inventory.first(Material.MUSIC_DISC_MALL));
                        if (itemStack != null && itemStack.hasItemMeta()) {
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            assert itemMeta != null;
                            if (itemMeta.hasDisplayName() && itemMeta.getDisplayName().equals("Preset Storage Disk") && itemMeta.hasLore()) {
                                List<String> lore = itemMeta.getLore();
                                assert lore != null;
                                if (lore.get(0).equals("Blank")) {
                                    // preset disk
                                    if (otherItems.size() > 0) {
                                        Material material = otherItems.get(0).getType();
                                        String preset = "";
                                        if (Preset.getPreset(material) != null) {
                                            preset = Preset.getPreset(material).toString();
                                        }
                                        if (!preset.isEmpty()) {
                                            List<String> diskLore = Collections.singletonList(preset);
                                            disk = new ItemStack(Material.MUSIC_DISC_MALL, 1);
                                            ItemMeta diskMeta = disk.getItemMeta();
                                            assert diskMeta != null;
                                            diskMeta.setDisplayName("Preset Storage Disk");
                                            diskMeta.setLore(diskLore);
                                            diskMeta.setCustomModelData(10000001);
                                            disk.setItemMeta(diskMeta);
                                            inventory.setItem(0, disk);
                                            player.updateInventory();
                                        }
                                    }
                                } else {
                                    TardisMessage.send(player, "DISK_BLANK_PRESET");
                                }
                            }
                        }
                    }
                }
            }, 3L);
        }
    }

    private boolean checkSlots(Inventory inventory) {
        boolean check = false;
        int count = 0;
        for (int i = 1; i < 10; i++) {
            if (inventory.getItem(i) != null && !Objects.requireNonNull(inventory.getItem(i)).getType().isAir()) {
                count++;
            }
        }
        if ((inventory.contains(Material.MUSIC_DISC_CAT) || inventory.contains(Material.MUSIC_DISC_MALL)) && count > 1) {
            check = true;
        }
        return check;
    }

    private List<ItemStack> getOtherItems(Inventory inventory) {
        List<ItemStack> itemStacks = new ArrayList<>();
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null) {
                Material material = itemStack.getType();
                if (!material.equals(Material.MUSIC_DISC_CAT) && !material.equals(Material.MUSIC_DISC_MALL) && !material.isAir()) {
                    itemStacks.add(itemStack);
                }
            }
        }
        return itemStacks;
    }
}
