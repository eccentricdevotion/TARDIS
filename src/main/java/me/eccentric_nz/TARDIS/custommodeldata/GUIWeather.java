package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum GUIWeather {

    // TARDIS Weather Menu
    CLEAR(1, 0, Material.SUNFLOWER),
    RAIN(2, 1, Material.WATER_BUCKET),
    THUNDER(4, 2, Material.GUNPOWDER),
    EXCITE(1, 5, Material.FIREWORK_ROCKET),
    CLOSE(1, 8, Material.BOWL);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GUIWeather(int customModelData, int slot, Material material) {
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
        return TARDISStringUtils.uppercaseFirst(s);
    }
}
