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
import me.eccentric_nz.TARDIS.enumeration.RecipeCategory;
import me.eccentric_nz.TARDIS.howto.TARDISSeedsInventory;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISRecipeCategoryListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISRecipeCategoryListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onRecipeCategoryClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "Recipe Categories")) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        if (slot < 4 || slot > 26) {
            return;
        }
        ItemStack is = view.getItem(slot);
        if (is != null) {
            ItemMeta im = is.getItemMeta();
            String cat = TARDISStringUtils.toEnumUppercase(im.getDisplayName());
            RecipeCategory category = RecipeCategory.valueOf(cat);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                Inventory recipes;
                ItemStack[] items;
                if (category == RecipeCategory.SEED_BLOCKS) {
                    items = new TARDISSeedsInventory(plugin, player).getMenu();
                    recipes = plugin.getServer().createInventory(player, 36, ChatColor.DARK_RED + "TARDIS Seeds Menu");
                    recipes.setContents(items);
                } else {
                    items = new TARDISRecipeInventory(plugin, category).getMenu();
                    recipes = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "TARDIS Recipes");
                    recipes.setContents(items);
                }
                player.openInventory(recipes);
            }, 2L);
        }
    }
}
