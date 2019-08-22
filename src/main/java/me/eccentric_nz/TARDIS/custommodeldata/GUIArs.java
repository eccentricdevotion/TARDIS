package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

import java.util.Locale;

public enum GUIArs {

    // ARS
    UP(10000001, 1, Material.CYAN_WOOL),
    DOWN(10000002, 1, Material.CYAN_WOOL),
    LEFT(10000003, 1, Material.CYAN_WOOL),
    RIGHT(10000004, 1, Material.CYAN_WOOL),
    LOAD_MAP(10000001, 1, Material.MAP),
    RECONFIGURE(10000001, 1, Material.PINK_WOOL),
    BOTTOM_LEVEL(10000001, 1, Material.WHITE_WOOL),
    MAIN_LEVEL(10000001, 1, Material.YELLOW_WOOL),
    TOP_LEVEL(10000002, 1, Material.WHITE_WOOL),
    RESET_SELECTED(10000001, 1, Material.COBBLESTONE),
    SCROLL_LEFT(10000001, 1, Material.RED_WOOL),
    SCROLL_RIGHT(10000001, 1, Material.LIME_WOOL),
    JETTISON(10000001, 1, Material.TNT),
    LOAD_THE_MAP(10000001, 1, Material.BLACK_WOOL),
    EMPTY_SLOT(10000001, 1, Material.STONE);

    // TODO add room blocks?

    private final int customModelData;
    private final int slot;
    private final Material material;
    private final String name;

    GUIArs(int customModelData, int slot, Material material) {
        this.customModelData = customModelData;
        this.slot = slot;
        this.material = material;
        name = setName(this);
    }

    public static GUIArs getByName(String name) {
        String processed = name.replace(" ", "_").toUpperCase(Locale.ENGLISH);
        return GUIArs.valueOf(processed);
    }

    private String setName(GUIArs item) {
        String s = item.toString();
        return TARDISStringUtils.sentenceCase(s);
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
        return name;
    }
}
