package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;

public enum GUIWallFloor {

    // TARDIS Wall & Floor Menu
    BUTTON_SCROLL_U(3, 8, Material.ARROW),
    BUTTON_SCROLL_D(2, 35, Material.ARROW),
    BUTTON_ABORT(2, 53, Material.BOWL);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GUIWallFloor(int customModelData, int slot, Material material) {
        this.customModelData = customModelData;
        this.slot = slot;
        this.material = material;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public int getSlot() {
        return slot;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        String s = toString();
        return TARDIS.plugin.getLanguage().getString(s);
    }
}
