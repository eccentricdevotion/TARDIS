package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;

public enum GUIGeneticManipulator {

    // Genetic Manipulator
    BUTTON_MASTER(2, 45, Material.COMPARATOR),
    BUTTON_AGE(1, 47, Material.HOPPER),
    BUTTON_TYPE(1, 48, Material.CYAN_DYE),
    BUTTON_OPTS(1, 49, Material.LEAD),
    BUTTON_RESTORE(1, 51, Material.APPLE),
    BUTTON_DNA(1, 52, Material.WRITABLE_BOOK),
    BUTTON_CANCEL(16, 53, Material.BOWL);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GUIGeneticManipulator(int customModelData, int slot, Material material) {
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
