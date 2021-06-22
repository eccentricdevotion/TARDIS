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
package me.eccentric_nz.tardis.recipes;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.listeners.TardisMenuListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class TardisRecipeInventoryListener extends TardisMenuListener implements Listener {

    private final TardisPlugin plugin;

    public TardisRecipeInventoryListener(TardisPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onRecipeInventoryClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "tardis Recipes")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            Player player = (Player) event.getWhoClicked();
            if (slot >= 0 && slot < 27) {
                ItemStack is = view.getItem(slot);
                if (is != null) {
                    switch (slot) {
                        case 0:
                            // back
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                ItemStack[] emenu = new TardisRecipeCategoryInventory().getMenu();
                                Inventory categories = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Recipe Categories");
                                categories.setContents(emenu);
                                player.openInventory(categories);
                            }, 2L);
                            break;
                        case 4:
                            // info
                            break;
                        case 8:
                            // close
                            close(player);
                            break;
                        default:
                            String command = ChatColor.stripColor(Objects.requireNonNull(Objects.requireNonNull(is.getItemMeta()).getLore()).get(0)).substring(1);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                player.performCommand(command);
                                plugin.getTrackerKeeper().getRecipeView().add(player.getUniqueId());
                            }, 2L);
                            break;
                    }
                }
            }
        }
    }
}
