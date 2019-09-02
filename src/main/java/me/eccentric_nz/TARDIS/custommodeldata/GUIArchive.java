package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum GUIArchive {

    // TARDIS Archive
    BACK(8, 18, Material.BOWL),
    SET_SIZE(77, 19, Material.BOWL),
    SCAN_CONSOLE(75, 20, Material.BOWL),
    ARCHIVE_CURRENT_CONSOLE(5, 0, Material.BOWL),
    SMALL(79, 22, Material.BOWL),
    MEDIUM(62, 23, Material.BOWL),
    TALL(81, 24, Material.BOWL),
    CLOSE(1, 26, Material.BOWL);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GUIArchive(int customModelData, int slot, Material material) {
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
