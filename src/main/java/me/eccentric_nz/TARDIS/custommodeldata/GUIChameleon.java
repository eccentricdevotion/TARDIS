package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

import java.util.Locale;

public enum GUIChameleon {

    // CHAMELEON
    APPLY_NOW(10000001, 0, Material.COMPARATOR),
    CHAMELEON_CIRCUIT(10000010, 10, Material.BOWL),
    ADAPTIVE(10000021, 11, Material.BOWL),
    INVISIBLE(10000022, 12, Material.BOWL),
    SHORTED_OUT(10000023, 13, Material.BOWL),
    CONSTRUCT(10000024, 14, Material.BOWL);

    private final int customModelData;
    private final int slot;
    private final Material material;
    private final String name;

    GUIChameleon(int customModelData, int slot, Material material) {
        this.customModelData = customModelData;
        this.slot = slot;
        this.material = material;
        name = setName(this);
    }

    public static GUIChameleon getByName(String name) {
        String processed = name.replace(" ", "_").toUpperCase(Locale.ENGLISH);
        return GUIChameleon.valueOf(processed);
    }

    private String setName(GUIChameleon item) {
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
