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
package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class DoorCustomRecipe {

    private final TARDIS plugin;

    public DoorCustomRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipes() {
        for (String r : plugin.getCustomDoorsConfig().getKeys(false)) {
            try {
                Material material = Material.valueOf(plugin.getCustomDoorsConfig().getString(r + ".material"));
                ItemStack is = ItemStack.of(material);
                ItemMeta im = is.getItemMeta();
                String dn = TARDISStringUtils.capitalise(r);
                im.displayName(ComponentUtils.toWhite("Door " + dn));
                im.setItemModel(new NamespacedKey(plugin, r + "_closed"));
                is.setItemMeta(im);
                NamespacedKey key = new NamespacedKey(plugin, "door_" + r);
                ShapedRecipe recipe = new ShapedRecipe(key, is);
                recipe.shape("#A#", "#D#", "###");
                recipe.setIngredient('#', plugin.getCraftingDifficulty() == CraftingDifficulty.HARD ? Material.GLASS : Material.GLASS_PANE);
                recipe.setIngredient('A', material);
                recipe.setIngredient('D', Material.IRON_DOOR);
                plugin.getServer().addRecipe(recipe);
                plugin.getFigura().getShapedRecipes().put("Door " + dn, recipe);
            } catch (IllegalArgumentException e) {
                plugin.debug("Invalid custom door item material for " + r + "!");
            }
        }
    }
}
