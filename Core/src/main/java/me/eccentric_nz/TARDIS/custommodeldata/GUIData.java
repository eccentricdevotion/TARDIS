package me.eccentric_nz.TARDIS.custommodeldata;

import org.bukkit.Material;

public record GUIData(int customModelData, int slot, Material material, String name) {

    public GUIData(int customModelData, int slot, Material material) {
        this(customModelData, slot, material, "");
    }
}
