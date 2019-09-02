package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum GUITemporalLocator {

    // Temporal Locator
    BUTTON_MORN(1, 0, Material.CLOCK, "0 ticks~6 AM"),
    BUTTON_NOON(2, 1, Material.CLOCK, "6000 ticks~12 Noon"),
    BUTTON_NIGHT(3, 2, Material.CLOCK, "12000 ticks~6 PM"),
    BUTTON_MID(4, 3, Material.CLOCK, "18000 ticks~12 PM"),
    AM_7(5, 4, Material.CLOCK, "1000 ticks"),
    AM_8(6, 5, Material.CLOCK, "2000 ticks"),
    AM_9(7, 6, Material.CLOCK, "3000 ticks"),
    AM_10(8, 7, Material.CLOCK, "4000 ticks"),
    AM_11(9, 8, Material.CLOCK, "5000 ticks"),
    PM_12(10, 9, Material.CLOCK, "6000 ticks"),
    PM_1(11, 10, Material.CLOCK, "7000 ticks"),
    PM_2(12, 11, Material.CLOCK, "8000 ticks"),
    PM_3(13, 12, Material.CLOCK, "9000 ticks"),
    PM_4(14, 13, Material.CLOCK, "10000 ticks"),
    PM_5(15, 14, Material.CLOCK, "11000 ticks"),
    PM_6(16, 15, Material.CLOCK, "12000 ticks"),
    PM_7(17, 16, Material.CLOCK, "13000 ticks"),
    PM_8(18, 17, Material.CLOCK, "14000 ticks"),
    PM_9(19, 18, Material.CLOCK, "15000 ticks"),
    PM_10(20, 19, Material.CLOCK, "16000 ticks"),
    PM_11(21, 20, Material.CLOCK, "17000 ticks"),
    AM_12(22, 21, Material.CLOCK, "18000 ticks"),
    AM_1(23, 22, Material.CLOCK, "19000 ticks"),
    AM_2(24, 23, Material.CLOCK, "20000 ticks"),
    AM_3(25, 24, Material.CLOCK, "21000 ticks"),
    AM_4(26, 25, Material.CLOCK, "22000 ticks"),
    AM_5(27, 26, Material.CLOCK, "23000 ticks");

    private final int customModelData;
    private final int slot;
    private final Material material;
    private final String lore;

    GUITemporalLocator(int customModelData, int slot, Material material, String lore) {
        this.customModelData = customModelData;
        this.slot = slot;
        this.material = material;
        this.lore = lore;
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
        if (s.startsWith("A") || s.startsWith("P")) {
            String[] split = s.split("_");
            return split[1] + " " + split[0];
        } else {
            return TARDISStringUtils.uppercaseFirst(s);
        }
    }

    public String getLore() {
        return lore;
    }
}
