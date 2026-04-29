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
package me.eccentric_nz.tardischemistry.block;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ChemistryBlockRecipes {

    private final TARDIS plugin;

    public ChemistryBlockRecipes(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipes() {
        for (RecipeData data : ChemistryBlock.RECIPES.values()) {
            ItemStack is = ItemStack.of(data.displayItem().getMaterial(), 1);
            is.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite(data.displayName()));
            ItemLore.Builder lore = ItemLore.lore();
            for (Component l : data.lore()) {
                lore.addLine(l);
            }
            is.setData(DataComponentTypes.LORE, lore.build());
            is.setData(DataComponentTypes.ITEM_MODEL, data.displayItem().getCustomModel());
            is.editPersistentDataContainer(pdc -> pdc.set(plugin.getCustomBlockKey(), PersistentDataType.STRING, data.displayItem().getCustomModel().getKey()));
            NamespacedKey key = new NamespacedKey(plugin, data.nameSpacedKey());
            ShapedRecipe recipe = new ShapedRecipe(key, is);
            recipe.shape("AAA", "ACA", "AAA");
            recipe.setIngredient('A', data.craftMaterial());
            recipe.setIngredient('C', Material.CRAFTING_TABLE);
            plugin.getServer().addRecipe(recipe);
            plugin.getFigura().getShapedRecipes().put(data.displayName(), recipe);
        }
    }
}
