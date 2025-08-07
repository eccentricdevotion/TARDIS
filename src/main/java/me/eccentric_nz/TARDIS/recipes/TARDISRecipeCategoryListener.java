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
package me.eccentric_nz.TARDIS.recipes;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RecipeCategory;
import me.eccentric_nz.TARDIS.howto.TARDISSeedsInventory;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
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
        if (!(event.getInventory().getHolder(false) instanceof TARDISRecipeCategoryInventory)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        if (slot < 2 || slot > 35) {
            return;
        }
        ItemStack is = event.getView().getItem(slot);
        if (is != null) {
            ItemMeta im = is.getItemMeta();
            String cat = ComponentUtils.toEnumUppercase(im.displayName());
            RecipeCategory category = RecipeCategory.valueOf(cat);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                InventoryHolder recipes;
                if (category == RecipeCategory.SEED_BLOCKS) {
                    recipes = new TARDISSeedsInventory(plugin, player);
                } else {
                    recipes = new TARDISRecipeInventory(plugin, category);
                }
                player.openInventory(recipes.getInventory());
            }, 2L);
        }
    }
}
