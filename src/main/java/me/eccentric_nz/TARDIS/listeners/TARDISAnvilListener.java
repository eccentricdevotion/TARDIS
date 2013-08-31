/*
 * Copyright (C) 2013 eccentric_nz
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

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISAnvilListener implements Listener {

    private final TARDIS plugin;
    HashMap<String, Integer> disallow = new HashMap<String, Integer>();

    public TARDISAnvilListener(TARDIS plugin) {
        this.plugin = plugin;
        disallow.put("TARDIS Locator", 345);
        disallow.put("TARDIS Stattenheim Circuit", 358);
        disallow.put("TARDIS Materialisation Circuit", 358);
        disallow.put("TARDIS Locator Circuit", 358);
        disallow.put("Stattenheim Remote", 318);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(InventoryClickEvent event) {
        Inventory inv = event.getView().getTopInventory();
        if (inv instanceof AnvilInventory) {
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            plugin.debug("Slot: " + slot);
            // slot 2 = result item slot
            if (slot == 2) {
                plugin.debug("Trying to get item...");
                ItemStack is = event.getCurrentItem();
                if (is != null && is.hasItemMeta()) {
                    plugin.debug("Trying to get meta...");
                    ItemMeta im = is.getItemMeta();
                    if (im.hasDisplayName() && disallow.containsKey(im.getDisplayName()) && is.getTypeId() == disallow.get(im.getDisplayName())) {
                        player.sendMessage(plugin.pluginName + "You cannot name the item that!");
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
