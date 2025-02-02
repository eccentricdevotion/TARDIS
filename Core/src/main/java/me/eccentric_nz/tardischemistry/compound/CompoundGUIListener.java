/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.tardischemistry.compound;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.tardischemistry.element.Element;
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

public class CompoundGUIListener extends TARDISMenuListener {

    public CompoundGUIListener(TARDIS plugin) {
        super(plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onCompoundMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "Chemical compounds")) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (slot >= 0 && slot < 27) {
            switch (slot) {
                case 0, 18, 19, 20, 21, 22, 23, 24, 25 -> {
                    // do nothing
                }
                case 17 -> { // check formula
                    event.setCancelled(true);
                    checkFormula(event.getClickedInventory(), player);
                }
                case 26 -> { // close
                    event.setCancelled(true);
                    close(player);
                }
                default -> event.setCancelled(true);
            }
        } else {
            ClickType click = event.getClick();
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                event.setCancelled(true);
            }
        }
    }

    private void checkFormula(Inventory inventory, Player player) {
        StringBuilder formula = new StringBuilder();
        for (int i = 18; i < 26; i++) {
            ItemStack is = inventory.getItem(i);
            if (is != null && is.getType().equals(Material.FEATHER) && is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                if (im.hasDisplayName()) {
                    try {
                        Element element = Element.valueOf(im.getDisplayName());
                        int amount = is.getAmount();
                        formula.append(element).append(":").append(amount).append("-");
                    } catch (IllegalArgumentException e) {
                        // ignore
                    }
                }
            }
        }
        String f = (formula.length() > 1) ? formula.substring(0, formula.length() - 1) : "";
        if (!f.isEmpty()) {
            for (Compound compound : Compound.values()) {
                if (compound.getFormula().equals(f)) {
                    ItemStack chemical = CompoundBuilder.getCompound(compound);
                    // set slot 0 to the compound
                    inventory.setItem(0, chemical);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    // remove the other item stacks
                    for (int i = 18; i < 26; i++) {
                        inventory.setItem(i, null);
                    }
                    return;
                }
            }
        }
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
    }
}
