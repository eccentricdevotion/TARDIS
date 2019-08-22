package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

import java.util.Locale;

public enum GUIChameleonConstructor {

    // CHAMELEON CONSTRUCTOR
    BACK_TO_CHAMELEON_CIRCUIT(10000001, 0, Material.ARROW),
    HELP(10000025, 2, Material.BOWL),
    INFO(10000026, 3, Material.BOWL),
    ABORT(10000026, 5, Material.BOWL),
    USE_LAST_SAVED_CONSTRUCT(10000027, 7, Material.BOWL),
    SAVE_CONSTRUCT(10000028, 8, Material.BOWL);

    // CHAMELEON EXAMPLE

    // DESKTOP THEME

    // DESTINATION TERMINAL

    // HANDLES

    // HUM

    // KEY

    // LAZARUS

    // MAP

    // PLAYER PREFERENCES

    // SAVES

    // SONIC

    // SONIC GENERATOR?

    // STORAGE

    // ???
    private final int customModelData;
    private final int slot;
    private final Material material;
    private final String name;

    GUIChameleonConstructor(int customModelData, int slot, Material material) {
        this.customModelData = customModelData;
        this.slot = slot;
        this.material = material;
        name = setName(this);
    }

    public static GUIChameleonConstructor getByName(String name) {
        String processed = name.replace(" ", "_").toUpperCase(Locale.ENGLISH);
        return GUIChameleonConstructor.valueOf(processed);
    }

    private String setName(GUIChameleonConstructor item) {
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
