package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum GUISaves {

    // TARDIS saves
    REARRANGE_SAVES(5, 45, Material.ARROW),
    LOAD_TARDIS_AREAS(1, 49, Material.MAP),
    DELETE_SAVE(1, 53, Material.BUCKET);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GUISaves(int customModelData, int slot, Material material) {
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
        return TARDISStringUtils.sentenceCase(s);
    }
}
