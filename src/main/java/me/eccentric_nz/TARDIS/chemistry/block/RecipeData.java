package me.eccentric_nz.TARDIS.chemistry.block;

import org.bukkit.Material;

import java.util.List;

public class RecipeData {

    private final String displayName;
    private final String nameSpacedKey;
    private final List<String> lore;
    private final Material craftMaterial;

    public RecipeData(String displayName, String namespacedKey, List<String> lore, Material craftMaterial) {
        this.displayName = displayName;
        this.nameSpacedKey = namespacedKey;
        this.lore = lore;
        this.craftMaterial = craftMaterial;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getNameSpacedKey() {
        return nameSpacedKey;
    }

    public List<String> getLore() {
        return lore;
    }

    public Material getCraftMaterial() {
        return craftMaterial;
    }
}
