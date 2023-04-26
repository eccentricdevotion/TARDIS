/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.chemistry.lab;

import org.bukkit.Material;

public enum Lab {

    Bleach("Water,Water,Water,Sodium Hypochlorite,Sodium Hypochlorite,Sodium Hypochlorite", Material.WHITE_DYE, 1),
    Heat_Block("Iron,Water,CHARCOAL,Salt", Material.RED_CONCRETE, 10001),
    Ice_Bomb("Sodium Acetate,Sodium Acetate,Sodium Acetate,Sodium Acetate", Material.SNOWBALL, 3),
    Super_Fertiliser("Ammonia,Phosphorus", Material.BONE_MEAL, 4);

    private final String recipe;
    private final Material itemMaterial;
    private final int customModelData;

    Lab(String recipe, Material itemMaterial, int customModelData) {
        this.recipe = recipe;
        this.itemMaterial = itemMaterial;
        this.customModelData = customModelData;
    }

    public String getRecipe() {
        return recipe;
    }

    public Material getItemMaterial() {
        return itemMaterial;
    }

    public int getCustomModelData() {
        return customModelData;
    }
}
