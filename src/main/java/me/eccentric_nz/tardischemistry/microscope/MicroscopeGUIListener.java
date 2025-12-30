/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.tardischemistry.microscope;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class MicroscopeGUIListener implements Listener {

    private final TARDIS plugin;

    MicroscopeGUIListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder(false);
        if (!(holder instanceof SlideInventory) && !(holder instanceof ComputerInventory) && !(holder instanceof FileCabinetInventory)) {
            return;
        }
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        // for now at least not slot 0 as it is an empty slide/screen...
        if (slot > 0 && slot < 54) {
            if (slot == 53) {
                // close
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, player::closeInventory, 1L);
            } else {
                ItemStack is = event.getInventory().getItem(slot);
                if (is != null) {
                    // add slide/screen to player's inventory
                    ItemStack ss = is.clone();
                    ItemMeta ssMeta = ss.getItemMeta();
                    ssMeta.getPersistentDataContainer().set(plugin.getMicroscopeKey(), PersistentDataType.INTEGER, slot + 10000);
                    ss.setItemMeta(ssMeta);
                    HashMap<Integer, ItemStack> items = player.getInventory().addItem(ss);
                    if (!items.isEmpty()) {
                        player.getWorld().dropItem(player.getLocation(), ss);
                    }
                }
            }
        }
    }
}
