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
package me.eccentric_nz.TARDIS.recipes;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class TARDISRecipeInventoryListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISRecipeInventoryListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onRecipeInventoryClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "TARDIS Recipes")) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        if (slot < 0 || slot >= 27) {
            return;
        }
        ItemStack is = view.getItem(slot);
        if (is == null) {
            return;
        }
        switch (slot) {
            case 0 ->
                // back
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    ItemStack[] emenu = new TARDISRecipeCategoryInventory().getMenu();
                    Inventory categories = plugin.getServer().createInventory(player, 36, ChatColor.DARK_RED + "Recipe Categories");
                    categories.setContents(emenu);
                    player.openInventory(categories);
                }, 2L);
            case 4 -> { } // info
            case 8 -> close(player); // close
            default -> {
                String command = ChatColor.stripColor(is.getItemMeta().getLore().getFirst()).substring(1);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    player.performCommand(command);
                    plugin.getTrackerKeeper().getRecipeViewers().add(player.getUniqueId());
                }, 2L);
            }
        }
    }
}
