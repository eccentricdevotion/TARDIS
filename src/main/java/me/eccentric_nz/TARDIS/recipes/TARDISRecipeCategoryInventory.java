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
package me.eccentric_nz.TARDIS.recipes;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.custommodels.keys.KeyVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.SonicVariant;
import me.eccentric_nz.TARDIS.enumeration.RecipeCategory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TARDISRecipeCategoryInventory implements InventoryHolder {

    private final Inventory inventory;

    public TARDISRecipeCategoryInventory(TARDIS plugin) {
        this.inventory = plugin.getServer().createInventory(this, 36, Component.text("Recipe Categories", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[36];
        // info
        ItemStack info = ItemStack.of(Material.BOWL, 1);
        info.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Info"));
        info.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("Click a button below"),
                Component.text("to see the items"),
                Component.text("in that recipe category")
        )));
        stack[0] = info;
        for (RecipeCategory category : RecipeCategory.values()) {
            if (!category.equals(RecipeCategory.UNUSED) && category != RecipeCategory.UNCRAFTABLE && category != RecipeCategory.CHEMISTRY) {
                ItemStack cat = ItemStack.of(category.getMaterial(), 1);
                cat.setData(DataComponentTypes.CUSTOM_NAME, Component.text(category.getName()));
                if (category == RecipeCategory.ITEMS) {
                    cat.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                            .addFloats(KeyVariant.BRASS_STRING.getFloats())
                            .build());
                }
                if (category == RecipeCategory.SONIC_UPGRADES) {
                    cat.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                            .addFloats(SonicVariant.NINTH.getFloats())
                            .build());
                }
                if (category == RecipeCategory.SONIC_CIRCUITS) {
                    cat.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                            .addFloats(List.of(127.0f))
                            .build());
                }
                if (category == RecipeCategory.ITEM_CIRCUITS) {
                    cat.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                            .addFloats(List.of(128.0f))
                            .build());
                }
                if (category == RecipeCategory.CONSOLE_CIRCUITS) {
                    cat.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                            .addFloats(List.of(129.0f))
                            .build());
                }
                if (category == RecipeCategory.ROTORS) {
                    cat.setData(DataComponentTypes.ITEM_MODEL, category.getModel());
                }
                cat.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                        .addHiddenComponents(TARDISConstants.HIDE)
                        .build());
                stack[category.getSlot()] = cat;
            }
        }
        return stack;
    }
}
