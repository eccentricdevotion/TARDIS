package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;

public enum GUIGeneticManipulator {

    // Genetic Manipulator
    BUTTON_MASTER(2, 45, Material.COMPARATOR),
    BUTTON_AGE(2, 47, Material.HOPPER),
    BUTTON_TYPE(1, 48, Material.CYAN_DYE),
    BUTTON_OPTS(1, 49, Material.LEAD),
    BUTTON_RESTORE(1, 51, Material.APPLE),
    BUTTON_DNA(1, 52, Material.WRITABLE_BOOK),
    BUTTON_CANCEL(16, 53, Material.BOWL),
    ANGEL(1, 1, Material.BRICK),
    CYBERMAN(1, 1, Material.IRON_INGOT),
    DALEK(1, 1, Material.SLIME_BALL),
    ICE_WARRIOR(1, 1, Material.SNOWBALL),
    EMPTY_CHILD(1, 1, Material.SUGAR),
    SILURIAN(1, 1, Material.FEATHER),
    SONTARAN(1, 1, Material.POTATO),
    STRAX(1, 1, Material.BAKED_POTATO),
    VASHTA_NERADA(2, 1, Material.BOOK),
    ZYGON(1, 1, Material.PAINTING);

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
