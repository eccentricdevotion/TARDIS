package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum GlowStickVariant {

    BLUE_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "blue_glow_stick")),
    BLUE_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "blue_glow_stick_active")),
    BROWN_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "brown_glow_stick")),
    BROWN_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "brown_glow_stick_active")),
    CYAN_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "cyan_glow_stick")),
    CYAN_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "cyan_glow_stick_active")),
    GREEN_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "green_glow_stick")),
    GREEN_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "green_glow_stick_active")),
    LIGHT_BLUE_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "light_blue_glow_stick")),
    LIGHT_BLUE_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "light_blue_glow_stick_active")),
    LIGHT_GRAY_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "light_gray_glow_stick")),
    LIGHT_GRAY_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "light_gray_glow_stick_active")),
    LIME_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "lime_glow_stick")),
    LIME_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "lime_glow_stick_active")),
    MAGENTA_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "magenta_glow_stick")),
    MAGENTA_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "magenta_glow_stick_active")),
    ORANGE_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "orange_glow_stick")),
    ORANGE_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "orange_glow_stick_active")),
    PINK_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "pink_glow_stick")),
    PINK_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "pink_glow_stick_active")),
    PURPLE_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "purple_glow_stick")),
    PURPLE_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "purple_glow_stick_active")),
    RED_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "red_glow_stick")),
    RED_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "red_glow_stick_active")),
    WHITE_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "white_glow_stick")),
    WHITE_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "white_glow_stick_active")),
    YELLOW_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "yellow_glow_stick")),
    YELLOW_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "yellow_glow_stick_active"));

    private final NamespacedKey key;

    GlowStickVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
