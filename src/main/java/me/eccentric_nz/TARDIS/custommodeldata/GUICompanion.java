package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

import java.util.Locale;

public enum GUICompanion {

    // COMPANION
    INFO(10000001, 45, Material.BOOK),
    LIST_COMPANIONS(10000001, 47, Material.WRITABLE_BOOK),
    CLOSE(10000001, 53, Material.BOWL);

    private final int customModelData;
    private final int slot;
    private final Material material;
    private final String name;

    GUICompanion(int customModelData, int slot, Material material) {
        this.customModelData = customModelData;
        this.slot = slot;
        this.material = material;
        name = setName(this);
    }

    public static GUICompanion getByName(String name) {
        String processed = name.replace(" ", "_").toUpperCase(Locale.ENGLISH);
        return GUICompanion.valueOf(processed);
    }

    private String setName(GUICompanion item) {
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
