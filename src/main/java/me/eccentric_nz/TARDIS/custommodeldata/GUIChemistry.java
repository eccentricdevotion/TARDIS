package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum GUIChemistry {

    // TARDIS Areas
    CLOSE(1, 1, Material.BOWL),
    INFO(57, 1, Material.BOWL),
    CHECK(89, 1, Material.BOWL),
    CRAFT(90, 1, Material.BOWL),
    ELECTRONS(91, 1, Material.BOWL),
    NEUTRONS(92, 1, Material.BOWL),
    PROTONS(93, 1, Material.BOWL),
    REDUCE(94, 1, Material.BOWL),
    SCROLL_DOWN(7, 1, Material.ARROW),
    SCROLL_UP(8, 1, Material.ARROW),
    PLUS(9, 1, Material.ARROW),
    MINUS(10, 1, Material.ARROW);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GUIChemistry(int customModelData, int slot, Material material) {
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
