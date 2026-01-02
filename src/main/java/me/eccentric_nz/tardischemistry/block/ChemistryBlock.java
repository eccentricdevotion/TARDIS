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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardischemistry.block;

import me.eccentric_nz.TARDIS.customblocks.TARDISChemistryDisplayItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;

public class ChemistryBlock {

    public static final HashMap<String, RecipeData> RECIPES = new HashMap<>() {
        {
            put("creative", new RecipeData("Atomic Elements", "creative_block", List.of(Component.text("A creative inventory"), Component.text("of atomic elements.")), Material.DIAMOND, TARDISChemistryDisplayItem.CREATIVE));
            put("compound", new RecipeData("Chemical Compounds", "compound_block", List.of(Component.text("Create over thirty compounds"), Component.text("by combining elements.")), Material.REDSTONE, TARDISChemistryDisplayItem.COMPOUND));
            put("reduce", new RecipeData("Material Reducer", "reducer_block", List.of(Component.text("Learn about the natural world"), Component.text("by reducing Minecraft blocks"), Component.text("to their component elements.")), Material.GOLD_NUGGET, TARDISChemistryDisplayItem.REDUCER));
            put("construct", new RecipeData("Element Constructor", "constructor_block", List.of(Component.text("Build elements by choosing the number"), Component.text("of protons, electrons, and neutrons.")), Material.LAPIS_LAZULI, TARDISChemistryDisplayItem.CONSTRUCTOR));
            put("lab", new RecipeData("Lab Table", "lab_block", List.of(Component.text("Combine substances to"), Component.text("get new results.")), Material.COAL, TARDISChemistryDisplayItem.LAB));
            put("product", new RecipeData("Product Crafting", "crafting_block", List.of(Component.text("Combine substances to"), Component.text("make fun products.")), Material.IRON_NUGGET, TARDISChemistryDisplayItem.PRODUCT));
        }
    };
}
