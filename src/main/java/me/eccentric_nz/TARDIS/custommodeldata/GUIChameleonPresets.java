package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum GUIChameleonPresets {

    // Chameleon Presets
    GO_TO_PAGE_2(4, 51, Material.ARROW),
    BACK(8, 52, Material.BOWL),
    CLOSE(1, 53, Material.BOWL);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GUIChameleonPresets(int customModelData, int slot, Material material) {
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
