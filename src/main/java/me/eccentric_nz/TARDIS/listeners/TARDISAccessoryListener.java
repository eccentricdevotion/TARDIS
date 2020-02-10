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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISAccessoryListener implements Listener {

    private final TARDIS plugin;

    public TARDISAccessoryListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onHelmetSlotEquip(InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();
        if (inv != null) {
            InventoryType inventoryType = inv.getType();
            if (inventoryType == InventoryType.PLAYER && event.getRawSlot() == 5) {
                ItemStack cursor = event.getCursor();
                if (cursor != null && cursor.getType() == Material.LEATHER_HELMET && cursor.hasItemMeta() && isNullOrAir(event.getCurrentItem())) {
                    ItemMeta im = cursor.getItemMeta();
                    if (im.hasCustomModelData()) {
                        int cmd = im.getCustomModelData();
                        if (cmd > 10000022 && cmd < 10000041) {
                            ItemStack accessory = new ItemStack(Material.BIRCH_BUTTON, 1);
                            ItemMeta bim = accessory.getItemMeta();
                            bim.setDisplayName(im.getDisplayName());
                            bim.setCustomModelData(cmd);
                            accessory.setItemMeta(bim);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                event.getWhoClicked().setItemOnCursor(null);
                                event.setCurrentItem(accessory);
                            }, 1L);
                        }
                    }
                } else if (isNullOrAir(cursor) && event.getCurrentItem().getType() == Material.BIRCH_BUTTON && event.getCurrentItem().hasItemMeta()) {
                    ItemStack current = event.getCurrentItem();
                    ItemMeta im = current.getItemMeta();
                    if (im.hasCustomModelData()) {
                        int cmd = im.getCustomModelData();
                        if (cmd > 10000022 && cmd < 10000041) {
                            ItemStack accessory = new ItemStack(Material.LEATHER_HELMET, 1);
                            ItemMeta bim = accessory.getItemMeta();
                            bim.setDisplayName(im.getDisplayName());
                            bim.setCustomModelData(cmd);
                            bim.addItemFlags(ItemFlag.values());
                            Damageable damageable = (Damageable) im;
                            damageable.setDamage(50);
                            accessory.setItemMeta(bim);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                event.getWhoClicked().setItemOnCursor(accessory);
                            }, 1L);
                            event.setCurrentItem(null);
                        }
                    }
                }
            }
        }
    }

    private boolean isNullOrAir(ItemStack item) {
        return item == null || item.getType().isAir();
    }
}
