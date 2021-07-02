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
package me.eccentric_nz.TARDIS.chemistry.lab;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class LabGUIListener extends TARDISMenuListener implements Listener {

    //    private final TARDIS plugin;
    private final List<Integer> slots = Arrays.asList(18, 19, 20, 21, 22, 23);

    public LabGUIListener(TARDIS plugin) {
        super(plugin);
//        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onLabMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "Lab table")) {
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 27) {
                switch (slot) {
                    case 0:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                        // do nothing
                        break;
                    case 17:
                        // check product
                        event.setCancelled(true);
                        check(event.getClickedInventory(), player);
                        break;
                    case 26:
                        // close
                        event.setCancelled(true);
                        close(player);
                        break;
                    default:
                        event.setCancelled(true);
                        break;
                }
            } else {
                ClickType click = event.getClick();
                if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    private void check(Inventory inventory, Player player) {
        StringBuilder builder = new StringBuilder();
        for (int slot : slots) {
            ItemStack is = inventory.getItem(slot);
            if (is != null) {
                Material material = is.getType();
                if ((material.equals(Material.GLASS_BOTTLE) || material.equals(Material.FEATHER)) && is.hasItemMeta()) {
                    ItemMeta im = is.getItemMeta();
                    assert im != null;
                    if (im.hasDisplayName()) {
                        builder.append(im.getDisplayName()).append(",");
                    }
                } else {
                    builder.append(is.getType()).append(",");
                }
            }
        }
        String recipe = builder.substring(0, builder.length() - 1);
        for (Lab lab : Lab.values()) {
            if (lab.getRecipe().equals(recipe)) {
                craft(lab, inventory, player);
                return;
            }
        }
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
    }

    private void craft(Lab lab, Inventory inventory, Player player) {
        ItemStack crafted = LabBuilder.getLabProduct(lab);
        // set slot 14 to the crafted product
        inventory.setItem(0, crafted);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        // remove the crafting item stacks
        for (int i : slots) {
            inventory.setItem(i, null);
        }
    }
}
