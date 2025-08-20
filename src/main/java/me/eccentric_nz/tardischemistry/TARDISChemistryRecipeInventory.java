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
package me.eccentric_nz.tardischemistry;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.tardischemistry.block.ChemistryBlock;
import me.eccentric_nz.tardischemistry.block.RecipeData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISChemistryRecipeInventory implements InventoryHolder {

    private final String which;
    private final Material material;
    private final Inventory inventory;

    public TARDISChemistryRecipeInventory(TARDIS plugin, String which, Material material) {
        this.which = which;
        this.material = material;
        this.inventory = plugin.getServer().createInventory(this, 27, Component.text("Chemistry " + which + " recipe", NamedTextColor.DARK_RED));
        this.inventory.setContents(getStacks());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getStacks() {
        ItemStack[] stacks = new ItemStack[27];
        ItemStack ingredient = ItemStack.of(material, 1);
        stacks[0] = ingredient;
        stacks[1] = ingredient;
        stacks[2] = ingredient;
        stacks[9] = ingredient;
        stacks[10] = ItemStack.of(Material.CRAFTING_TABLE, 1);
        stacks[11] = ingredient;
        stacks[18] = ingredient;
        stacks[19] = ingredient;
        stacks[20] = ingredient;
        RecipeData data = ChemistryBlock.RECIPES.get(which);
        ItemStack result = ItemStack.of(data.displayItem().getMaterial(), 1);
        ItemMeta im = result.getItemMeta();
        im.displayName(ComponentUtils.toWhite(data.displayName()));
        im.lore(data.lore());
        im.setItemModel(data.displayItem().getCustomModel());
        result.setItemMeta(im);
        stacks[17] = result;
        return stacks;
    }
}
