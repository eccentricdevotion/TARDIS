package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum GUIUpgrade {

    // TARDIS Upgrade Menu
    ARCHIVE_CONSOLES(4, 46, Material.BOWL),
    REPAIR_CONSOLE(72, 47, Material.BOWL),
    CLEAN(34, 48, Material.BOWL),
    CLOSE(1, 53, Material.BOWL);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GUIUpgrade(int customModelData, int slot, Material material) {
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
