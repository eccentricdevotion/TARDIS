package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum GUIChameleonPoliceBoxes {

    // Chameleon Police Boxes
    GO_TO_PAGE_1(11, 24, Material.ARROW),
    BACK(8, 25, Material.BOWL),
    CLOSE(1, 26, Material.BOWL);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GUIChameleonPoliceBoxes(int customModelData, int slot, Material material) {
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
