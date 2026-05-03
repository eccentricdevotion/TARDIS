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
package me.eccentric_nz.TARDIS.recipes.shaped;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.DyedItemColor;
import io.papermc.paper.datacomponent.item.Equippable;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.set.RegistrySet;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.Whoniverse;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

public class SpaceSuitLeggingsRecipe {

    private final TARDIS plugin;

    public SpaceSuitLeggingsRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ItemStack.of(Material.CHAINMAIL_LEGGINGS, 1);
        is.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("TARDIS Space Suit Leggings"));
        is.setData(DataComponentTypes.MAX_STACK_SIZE, 1);
        is.setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.LEGS)
                .assetId(Whoniverse.SPACE_SUIT.getKey())
                .allowedEntities(RegistrySet.keySet(RegistryKey.ENTITY_TYPE, TypedKey.create(RegistryKey.ENTITY_TYPE, EntityType.PLAYER.getKey())))
                .dispensable(true)
                .build());
        NamespacedKey key = new NamespacedKey(plugin, "space_suit_leggings");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape(" H ", "YYY", "BGB");
        r.setIngredient('Y', Material.ORANGE_DYE);
        r.setIngredient('G', Material.BLACK_DYE);
        r.setIngredient('B', Material.ORANGE_WOOL);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            ItemStack exact = ItemStack.of(Material.LEATHER_LEGGINGS, 1);
            Color black = Color.fromARGB(-14869215); // [argb0xFF1D1D21] not BLACK!
            exact.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor()
                    .color(black)
                    .build());
            r.setIngredient('H', new RecipeChoice.ExactChoice(exact));
        } else {
            r.setIngredient('H', Material.LEATHER_LEGGINGS);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Space Suit Leggings", r);
    }
}
