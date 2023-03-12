/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.tardisweepingangels.utils;

import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class MonsterHeadEquipListener implements Listener {

    private final TARDISWeepingAngels plugin;

    public MonsterHeadEquipListener(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onHelmetSlotEquip(InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();
        if (inv != null) {
            InventoryType inventoryType = inv.getType();
            if (inventoryType == InventoryType.PLAYER && event.getRawSlot() == 5) {
                ItemStack cursor = event.getCursor();
                if (cursor != null && cursor.hasItemMeta() && cursor.getItemMeta().getPersistentDataContainer().has(TARDISWeepingAngels.MONSTER_HEAD, PersistentDataType.INTEGER) && isNullOrAir(event.getCurrentItem())) {
                    event.setCurrentItem(cursor);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> event.setCursor(new ItemStack(Material.AIR)), 1L);
                    event.setCancelled(true);
                }
            }
        }
    }

    private boolean isNullOrAir(ItemStack item) {
        return item == null || item.getType().isAir();
    }
}
