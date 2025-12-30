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
package me.eccentric_nz.tardischemistry.creative;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardischemistry.element.ElementInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class CreativeGUIListener implements Listener {

    private final TARDIS plugin;

    public CreativeGUIListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreativeMenuClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder(false);
        if (!(holder instanceof ProductsCreativeInventory) && !(holder instanceof CompoundsCreativeInventory)) {
            return;
        }
        Player p = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 53) {
            ClickType click = event.getClick();
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                plugin.debug("CreativeGUIListener");
                event.setCancelled(true);
            }
            return;
        }
        InventoryView view = event.getView();
        switch (slot) {
            case 35 -> {
                event.setCancelled(true);
                // switch to elements
                close(p);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    p.openInventory(new ElementInventory(plugin).getInventory());
                }, 2L);
            }
            case 44 -> {
                event.setCancelled(true);
                boolean molecular = holder instanceof CompoundsCreativeInventory;
                close(p);
                // switch to compounds or products
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    InventoryHolder stacks = (molecular) ? new ProductsCreativeInventory(plugin) : new CompoundsCreativeInventory(plugin);
                    p.openInventory(stacks.getInventory());
                }, 2L);
            }
            case 53 -> {
                // close
                event.setCancelled(true);
                close(p);
            }
            default -> {
                event.setCancelled(true);
                // get clicked ItemStack
                if (view.getItem(slot) != null) {
                    ItemStack choice = view.getItem(slot).clone();
                    choice.setAmount(event.getClick().equals(ClickType.SHIFT_LEFT) ? 64 : 1);
                    // add ItemStack to inventory if there is room
                    p.getInventory().addItem(choice);
                }
            }
        }
    }

    /**
     * Closes the inventory.
     *
     * @param p the player using the GUI
     */
    private void close(Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, p::closeInventory, 1L);
    }
}
