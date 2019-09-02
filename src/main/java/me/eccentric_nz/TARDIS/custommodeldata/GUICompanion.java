package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum GUICompanion {

    // Add Companion
    INFO(1, 45, Material.BOOK),
    LIST_COMPANIONS(1, 47, Material.WRITABLE_BOOK),
    BUTTON_CLOSE(1, 53, Material.BOWL);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GUICompanion(int customModelData, int slot, Material material) {
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
        if (s.startsWith("BUTTON")) {
            TARDIS.plugin.getLanguage().getString("s");
        }
        return TARDISStringUtils.sentenceCase(s);
    }
}
