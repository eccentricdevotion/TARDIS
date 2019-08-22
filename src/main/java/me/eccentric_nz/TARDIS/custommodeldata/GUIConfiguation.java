package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

import java.util.Locale;

public enum GUIConfiguation {

    // CONFIGURATION
    ALLOW_FALSE(10000001, 0, Material.REPEATER),
    ALLOW_TRUE(20000001, 1, Material.REPEATER),
    ARCH_FALSE(10000002, 22, Material.REPEATER),
    ARCH_TRUE(20000002, 23, Material.REPEATER),
    ARCHIVE_FALSE(10000003, 25, Material.REPEATER),
    ARCHIVE_TRUE(20000003, 25, Material.REPEATER),
    CREATION_FALSE(10000004, 26, Material.REPEATER),
    CREATION_TRUE(20000004, 27, Material.REPEATER),
    GROWTH_FALSE(10000005, 36, Material.REPEATER),
    GROWTH_TRUE(20000005, 35, Material.REPEATER),
    HANDLES_FALSE(10000006, 37, Material.REPEATER),
    HANDLES_TRUE(20000006, 37, Material.REPEATER),
    POLICE_BOX_FALSE(10000007, 38, Material.REPEATER),
    POLICE_BOX_TRUE(20000007, 39, Material.REPEATER),
    PREFERENCES_FALSE(10000008, 41, Material.REPEATER),
    PREFERENCES_TRUE(20000008, 42, Material.REPEATER),
    ABANDON_FALSE(10000009, 0, Material.REPEATER),
    ABANDON_TRUE(20000009, 1, Material.REPEATER),
    CIRCUIT_FALSE(10000010, 0, Material.REPEATER),
    CIRCUIT_TRUE(20000010, 1, Material.REPEATER),
    DEBUG_FALSE(10000011, 0, Material.REPEATER),
    DEBUG_TRUE(20000011, 1, Material.REPEATER),
    THEME_FALSE(10000012, 0, Material.REPEATER),
    THEME_TRUE(20000012, 1, Material.REPEATER),
    JUNK_FALSE(10000013, 0, Material.REPEATER),
    JUNK_TRUE(20000013, 1, Material.REPEATER),
    SIEGE_FALSE(10000014, 0, Material.REPEATER),
    SIEGE_TRUE(200000014, 1, Material.REPEATER),
    TRAVEL_FALSE(10000015, 0, Material.REPEATER),
    TRAVEL_TRUE(20000015, 1, Material.REPEATER);

    private final int customModelData;
    private final int slot;
    private final Material material;
    private final String name;

    GUIConfiguation(int customModelData, int slot, Material material) {
        this.customModelData = customModelData;
        this.slot = slot;
        this.material = material;
        name = setName(this);
    }

    public static GUIConfiguation getByName(String name, boolean tf) {
        String processed = name.toUpperCase(Locale.ENGLISH) + (tf ? "_TRUE" : "_FALSE");
        return GUIConfiguation.valueOf(processed);
    }

    private String setName(GUIConfiguation item) {
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
