package me.eccentric_nz.TARDIS.chemistry.lab;

import org.bukkit.Material;

public enum Lab {

    Bleach("Water,Water,Water,Sodium Hypochlorite,Sodium Hypochlorite,Sodium Hypochlorite", Material.WHITE_DYE),
    Heat_Block("Iron,Water,Charcoal,Salt", Material.LANTERN),
    Ice_Bomb("Sodium Acetate,Sodium Acetate,Sodium Acetate,Sodium Acetate", Material.SNOWBALL),
    Super_Fertiliser("Ammonia,Phosphorus", Material.BONE_MEAL);

    private final String recipe;
    private final Material itemMaterial;

    Lab(String recipe, Material itemMaterial) {
        this.recipe = recipe;
        this.itemMaterial = itemMaterial;
    }

    public String getRecipe() {
        return recipe;
    }

    public Material getItemMaterial() {
        return itemMaterial;
    }
}
