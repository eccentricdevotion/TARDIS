package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

import java.util.Locale;

public enum GUIChameleonHelp {

    // CHAMELEON HELP
    BACK_TO_CONSTRUCTION(10000001, 0, Material.ARROW),
    INFO(10000001, 0, Material.BOWL), // x4
    ONE(10000001, 0, Material.BOWL), // x2
    TWO(10000001, 0, Material.BOWL), // x2
    THREE(10000001, 0, Material.BOWL), // x2
    FOUR(10000001, 0, Material.BOWL), // x2
    FIVE(10000001, 0, Material.BOWL),
    SIX(10000001, 0, Material.BOWL),
    SEVEN(10000001, 0, Material.BOWL),
    EIGHT(10000001, 0, Material.BOWL),
    NINE(10000001, 0, Material.BOWL),
    VIEW_EXAMPLE_TEMPLATE(10000001, 0, Material.BOWL),
    ;

    private final int customModelData;
    private final int slot;
    private final Material material;
    private final String name;

    GUIChameleonHelp(int customModelData, int slot, Material material) {
        this.customModelData = customModelData;
        this.slot = slot;
        this.material = material;
        name = setName(this);
    }

    public static GUIChameleonHelp getByName(String name) {
        String processed = name.replace(" ", "_").toUpperCase(Locale.ENGLISH);
        return GUIChameleonHelp.valueOf(processed);
    }

    private String setName(GUIChameleonHelp item) {
        String s = item.toString();
        if (s.startsWith("B") || s.startsWith("V")) {
            return TARDISStringUtils.sentenceCase(s);
        } else {
            return TARDISStringUtils.toNumber(s);
        }
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
