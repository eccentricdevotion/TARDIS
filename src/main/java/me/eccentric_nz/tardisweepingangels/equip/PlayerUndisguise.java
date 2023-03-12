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
package me.eccentric_nz.tardisweepingangels.equip;

import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class PlayerUndisguise implements Listener {

    private final TARDISWeepingAngels plugin;

    public PlayerUndisguise(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onManualUndisguise(InventoryClickEvent event) {
        if (event.getSlotType().equals(SlotType.ARMOR)) {
            int slot = event.getRawSlot();
            if (slot > 4 && slot < 9) {
                ItemStack is = event.getCurrentItem();
                if (is != null) {
                    if (is.hasItemMeta()) {
                        ItemMeta im = is.getItemMeta();
                        if (!im.getPersistentDataContainer().has(TARDISWeepingAngels.MONSTER_HEAD, PersistentDataType.INTEGER)) {
                            if (im.hasDisplayName() && (im.getDisplayName().startsWith("Weeping Angel") || im.getDisplayName().startsWith("Ice Warrior") || im.getDisplayName().startsWith("Cyberman") || im.getDisplayName().startsWith("Empty Child") || im.getDisplayName().startsWith("Hath") || im.getDisplayName().startsWith("Silurian") || im.getDisplayName().startsWith("Sontaran") || im.getDisplayName().startsWith("Strax") || im.getDisplayName().startsWith("Zygon") || im.getDisplayName().startsWith("Vashta"))) {
                                event.setCancelled(true);
                                (event.getWhoClicked()).sendMessage(plugin.pluginName + "You must use the '/twad [monster] off' command to remove this armour!");
                            }
                        }
                    }
                }
            }
        }
    }
}
