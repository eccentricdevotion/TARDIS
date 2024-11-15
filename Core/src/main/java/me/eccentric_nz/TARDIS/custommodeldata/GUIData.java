package me.eccentric_nz.TARDIS.custommodeldata;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public record GUIData(NamespacedKey key, int slot, Material material, String name) {

    public GUIData(NamespacedKey key, int slot, Material material) {
        this(key, slot, material, "");
    }
}
