/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.tardischemistry.product;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ProductGUIListener extends TARDISMenuListener {

    private final List<Integer> slots = Arrays.asList(0, 1, 2, 9, 10, 11, 18, 19, 20);
    private final List<Integer> pipe = Arrays.asList(2, 11, 20);

    public ProductGUIListener(TARDIS plugin) {
        super(plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onProductMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "Product crafting")) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 26) {
            ClickType click = event.getClick();
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                event.setCancelled(true);
            }
            return;
        }
        switch (slot) {
            case 0, 1, 2, 9, 10, 11, 18, 19, 20, 14 -> {
                // do nothing
            }
            case 17 -> { // craft
                event.setCancelled(true);
                craft(event.getClickedInventory(), player);
            }
            case 26 -> { // close
                event.setCancelled(true);
                close(player);
            }
            default -> event.setCancelled(true);
        }
    }

    private void craft(Inventory inventory, Player player) {
        StringBuilder builder = new StringBuilder();
        for (int slot : slots) {
            ItemStack is = inventory.getItem(slot);
            if (is != null) {
                Material material = is.getType();
                if ((material.equals(Material.GLASS_BOTTLE) || material.equals(Material.FEATHER)) && is.hasItemMeta()) {
                    ItemMeta im = is.getItemMeta();
                    if (im.hasDisplayName()) {
                        builder.append(im.getDisplayName()).append(pipe.contains(slot) ? "|" : ",");
                    }
                } else {
                    builder.append(is.getType()).append(pipe.contains(slot) ? "|" : ",");
                }
            } else {
                builder.append("-").append(pipe.contains(slot) ? "|" : ",");
            }
        }
        String recipe = builder.substring(0, builder.length() - 1);
        for (Product product : Product.values()) {
            if (product.getRecipe().equals(recipe)) {
                craft(product, inventory, player);
                return;
            }
        }
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
    }

    private void craft(Product product, Inventory inventory, Player player) {
        ItemStack crafted = ProductBuilder.getProduct(product);
        // set slot 14 to the crafted product
        inventory.setItem(14, crafted);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        // remove the crafting item stacks
        for (int i : slots) {
            inventory.setItem(i, null);
        }
    }
}
