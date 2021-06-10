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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.chemistry.block;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.HashMap;

public class ChemistryBlock {

    public static final HashMap<String, RecipeData> RECIPES = new HashMap<>() {
        {
            put("creative", new RecipeData("Atomic elements", "creative_block", Arrays.asList("A creative inventory", "of atomic elements."), Material.DIAMOND, 10000040));
            put("compound", new RecipeData("Chemical compounds", "compound_block", Arrays.asList("Create over thirty compounds", "by combining elements."), Material.REDSTONE, 10000041));
            put("reduce", new RecipeData("Material reducer", "reducer_block", Arrays.asList("Learn about the natural world", "by reducing Minecraft blocks", "to their component elements."), Material.GOLD_NUGGET, 10000042));
            put("construct", new RecipeData("Element constructor", "constructor_block", Arrays.asList("Build elements by choosing the number", "of protons, electrons, and neutrons."), Material.LAPIS_LAZULI, 10000043));
            put("lab", new RecipeData("Lab table", "lab_block", Arrays.asList("Combine substances to", "get new results."), Material.COAL, 10000044));
            put("product", new RecipeData("Product crafting", "crafting_block", Arrays.asList("Combine substances to", "make fun products."), Material.IRON_NUGGET, 10000045));
        }
    };
}
