package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum GUIConfiguration {

    // Admin Menu
    ALLOW(1, 0, Material.REPEATER),
    ARCH(2, 22, Material.REPEATER),
    ARCHIVE(3, 25, Material.REPEATER),
    CREATION(4, 26, Material.REPEATER),
    GROWTH(5, 36, Material.REPEATER),
    HANDLES(6, 37, Material.REPEATER),
    POLICE_BOX(7, 38, Material.REPEATER),
    PREFERENCES(8, 41, Material.REPEATER),
    // TODO slot numbers
    ABANDON(9, 0, Material.REPEATER),
    CIRCUITS(10, 0, Material.REPEATER),
    DEBUG(11, 0, Material.REPEATER),
    DESKTOP(12, 0, Material.REPEATER),
    JUNK(13, 0, Material.REPEATER),
    SIEGE(14, 0, Material.REPEATER),
    TRAVEL(15, 0, Material.REPEATER),
    NEXT(86, 0, Material.BOWL),
    PREV(87, 0, Material.BOWL),
    PREFS(2, 0, Material.NETHER_STAR);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GUIConfiguration(int customModelData, int slot, Material material) {
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
