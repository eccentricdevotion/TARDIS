package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum GUIChameleonConstructor {

    // Chameleon Constructor
    BACK_TO_CHAMELEON_CIRCUIT(1, 0, Material.ARROW),
    HELP(55, 2, Material.BOWL),
    INFO(57, 3, Material.BOWL),
    ABORT(2, 5, Material.BOWL),
    USE_LAST_SAVED_CONSTRUCT(82, 7, Material.BOWL),
    SAVE_CONSTRUCT(74, 8, Material.BOWL);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GUIChameleonConstructor(int customModelData, int slot, Material material) {
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
