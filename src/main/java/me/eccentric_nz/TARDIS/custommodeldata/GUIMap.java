package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;

public enum GUIMap {

    // TARDIS Map
    BUTTON_UP(1, 1, Material.CYAN_WOOL),
    BUTTON_DOWN(2, 18, Material.CYAN_WOOL),
    BUTTON_LEFT(3, 9, Material.CYAN_WOOL),
    BUTTON_RIGHT(4, 11, Material.CYAN_WOOL),
    BUTTON_MAP(2, 10, Material.MAP),
    BUTTON_LEVEL_B(3, 27, Material.WHITE_WOOL),
    BUTTON_LEVEL(1, 28, Material.YELLOW_WOOL),
    BUTTON_LEVEL_T(1, 29, Material.WHITE_WOOL),
    BUTTON_CLOSE(1, 45, Material.BOWL),
    BUTTON_WHERE(2, 47, Material.COMPASS),
    BUTTON_MAP_ON(1, -1, Material.BLACK_WOOL),
    EMPTY_SLOT(1, -1, Material.STONE),
    YOU_ARE_HERE(6, -1, Material.ARROW);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GUIMap(int customModelData, int slot, Material material) {
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
        return (this == EMPTY_SLOT) ? "Empty Slot" : TARDIS.plugin.getLanguage().getString(s);
    }
}
