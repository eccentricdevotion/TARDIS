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
package me.eccentric_nz.TARDIS.rooms.smelter;

import io.papermc.paper.registry.TypedKey;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.inventory.*;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * @author eccentric_nz
 */
public class Smelter {

    private static final List<Vector> FUEL_VECTORS = new ArrayList<>();
    private static final List<Vector> ORE_VECTORS = new ArrayList<>();
    private static final Set<Material> SMELTABLES = iterateFurnaceRecipes();

    static {
        FUEL_VECTORS.add(new Vector(-6.0, 3.0, 4.0));
        FUEL_VECTORS.add(new Vector(-6.0, 3.0, 3.0));
        FUEL_VECTORS.add(new Vector(-6.0, 3.0, -3.0));
        FUEL_VECTORS.add(new Vector(-6.0, 3.0, -4.0));
        FUEL_VECTORS.add(new Vector(-4.0, 3.0, 6.0));
        FUEL_VECTORS.add(new Vector(-4.0, 3.0, -6.0));
        FUEL_VECTORS.add(new Vector(-3.0, 3.0, 6.0));
        FUEL_VECTORS.add(new Vector(-3.0, 3.0, -6.0));
        FUEL_VECTORS.add(new Vector(3.0, 3.0, 6.0));
        FUEL_VECTORS.add(new Vector(3.0, 3.0, -6.0));
        FUEL_VECTORS.add(new Vector(4.0, 3.0, 6.0));
        FUEL_VECTORS.add(new Vector(4.0, 3.0, -6.0));
        FUEL_VECTORS.add(new Vector(6.0, 3.0, 4.0));
        FUEL_VECTORS.add(new Vector(6.0, 3.0, 3.0));
        FUEL_VECTORS.add(new Vector(6.0, 3.0, -3.0));
        FUEL_VECTORS.add(new Vector(6.0, 3.0, -4.0));
        ORE_VECTORS.add(new Vector(-5.0, 4.0, 4.0));
        ORE_VECTORS.add(new Vector(-5.0, 4.0, 3.0));
        ORE_VECTORS.add(new Vector(-5.0, 4.0, -3.0));
        ORE_VECTORS.add(new Vector(-5.0, 4.0, -4.0));
        ORE_VECTORS.add(new Vector(-4.0, 4.0, 5.0));
        ORE_VECTORS.add(new Vector(-4.0, 4.0, -5.0));
        ORE_VECTORS.add(new Vector(-3.0, 4.0, 5.0));
        ORE_VECTORS.add(new Vector(-3.0, 4.0, -5.0));
        ORE_VECTORS.add(new Vector(3.0, 4.0, 5.0));
        ORE_VECTORS.add(new Vector(3.0, 4.0, -5.0));
        ORE_VECTORS.add(new Vector(4.0, 4.0, 5.0));
        ORE_VECTORS.add(new Vector(4.0, 4.0, -5.0));
        ORE_VECTORS.add(new Vector(5.0, 4.0, 4.0));
        ORE_VECTORS.add(new Vector(5.0, 4.0, 3.0));
        ORE_VECTORS.add(new Vector(5.0, 4.0, -3.0));
        ORE_VECTORS.add(new Vector(5.0, 4.0, -4.0));
    }

    public static List<Vector> getFuelVectors() {
        return FUEL_VECTORS;
    }

    public static List<Vector> getOreVectors() {
        return ORE_VECTORS;
    }

    public static Set<Material> getSmeltables() {
        return SMELTABLES;
    }

    /**
     * Iterate through the Minecraft recipes and find all the input materials for furnace recipes
     *
     * @return a set of smeltable materials
     */
    private static Set<Material> iterateFurnaceRecipes() {
        Set<Material> smeltables = new HashSet<>();
        Iterator<Recipe> recipes = Bukkit.recipeIterator();
        while (recipes.hasNext()) {
            Recipe r = recipes.next();
            if (r instanceof FurnaceRecipe f) {
                RecipeChoice c = f.getInputChoice();
                switch (c) {
                    case RecipeChoice.ItemTypeChoice i -> {
                        for (TypedKey<ItemType> t : i.itemTypes().values()) {
                            smeltables.add(Registry.MATERIAL.get(t.key()));
                        }
                    }
                    case RecipeChoice.MaterialChoice m -> smeltables.addAll(m.getChoices());
                    case RecipeChoice.ExactChoice e -> {
                        for (ItemStack i : e.getChoices()) {
                            smeltables.add(i.getType());
                        }
                    }
                    default -> { }
                }
            }
        }
        return smeltables;
    }
}
