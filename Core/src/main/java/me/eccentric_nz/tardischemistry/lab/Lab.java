/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.tardischemistry.lab;

import me.eccentric_nz.TARDIS.custommodels.keys.ChemistryEquipment;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public enum Lab {

    Bleach("Water,Water,Water,Sodium Hypochlorite,Sodium Hypochlorite,Sodium Hypochlorite", Material.WHITE_DYE, ChemistryEquipment.BLEACH.getKey()),
    Heat_Block("Iron,Water,CHARCOAL,Salt", Material.RED_CONCRETE, ChemistryEquipment.HEAT_BLOCK.getKey()),
    Ice_Bomb("Sodium Acetate,Sodium Acetate,Sodium Acetate,Sodium Acetate", Material.SNOWBALL, ChemistryEquipment.ICE_BOMB.getKey()),
    Super_Fertiliser("Ammonia,Phosphorus", Material.BONE_MEAL, ChemistryEquipment.SUPER_FERTILISER.getKey());

    private final String recipe;
    private final Material itemMaterial;
    private final NamespacedKey model;

    Lab(String recipe, Material itemMaterial, NamespacedKey model) {
        this.recipe = recipe;
        this.itemMaterial = itemMaterial;
        this.model = model;
    }

    public String getRecipe() {
        return recipe;
    }

    public Material getItemMaterial() {
        return itemMaterial;
    }

    public NamespacedKey getModel() {
        return model;
    }

    public String getName() {
        return this.toString().replace("_", " ");
    }
}
