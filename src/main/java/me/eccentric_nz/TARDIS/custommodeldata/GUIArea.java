package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

import java.util.Locale;

public enum GUIArea {

    // AREAS

    LOAD_TARDIS_SAVES(10000001, 1, Material.MAP);

    private final int customModelData;
    private final int slot;
    private final Material material;
    private final String name;

    GUIArea(int customModelData, int slot, Material material) {
        this.customModelData = customModelData;
        this.slot = slot;
        this.material = material;
        name = setName(this);
    }

    public static GUIArea getByName(String name) {
        String processed = name.replace(" ", "_").toUpperCase(Locale.ENGLISH);
        return GUIArea.valueOf(processed);
    }

    private String setName(GUIArea item) {
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
