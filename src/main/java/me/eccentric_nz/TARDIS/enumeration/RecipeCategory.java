package me.eccentric_nz.TARDIS.enumeration;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum RecipeCategory {

    ACCESSORIES(Material.LEATHER_HELMET, 10000037, 9),
    CONSOLE_CIRCUITS(Material.GLOWSTONE_DUST, 10001977, 11),
    FOOD(Material.MELON_SLICE, 10000002, 13),
    ITEM_CIRCUITS(Material.GLOWSTONE_DUST, 10001967, 15),
    ITEMS(Material.GOLD_NUGGET, 12, 17),
    ROTORS(Material.LIGHT_GRAY_DYE, 10000002, 18),
    SONIC_CIRCUITS(Material.GLOWSTONE_DUST, 10001971, 20),
    SONIC_UPGRADES(Material.BLAZE_ROD, 10000009, 22),
    STORAGE_DISKS(Material.MUSIC_DISC_STRAD, 10000001, 24),
    MISC(Material.WATER_BUCKET, 1, 26),
    UNUSED(Material.STONE, 1, -1);

    private final Material material;
    private final int customModelData;
    private final int slot;

    RecipeCategory(Material material, int customModelData, int slot) {
        this.material = material;
        this.customModelData = customModelData;
        this.slot = slot;
    }

    public String getName() {
        String s = toString();
        return TARDISStringUtils.sentenceCase(s);
    }

    public Material getMaterial() {
        return material;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public int getSlot() {
        return slot;
    }
}
