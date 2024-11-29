package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum GlowStickVariant {

    BLUE_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/blue_glow_stick")),
    BLUE_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/blue_glow_stick_active")),
    BROWN_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/brown_glow_stick")),
    BROWN_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/brown_glow_stick_active")),
    CYAN_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/cyan_glow_stick")),
    CYAN_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/cyan_glow_stick_active")),
    GREEN_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/green_glow_stick")),
    GREEN_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/green_glow_stick_active")),
    LIGHT_BLUE_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/light_blue_glow_stick")),
    LIGHT_BLUE_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/light_blue_glow_stick_active")),
    LIGHT_GRAY_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/light_gray_glow_stick")),
    LIGHT_GRAY_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/light_gray_glow_stick_active")),
    LIME_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/lime_glow_stick")),
    LIME_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/lime_glow_stick_active")),
    MAGENTA_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/magenta_glow_stick")),
    MAGENTA_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/magenta_glow_stick_active")),
    ORANGE_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/orange_glow_stick")),
    ORANGE_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/orange_glow_stick_active")),
    PINK_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/pink_glow_stick")),
    PINK_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/pink_glow_stick_active")),
    PURPLE_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/purple_glow_stick")),
    PURPLE_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/purple_glow_stick_active")),
    RED_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/red_glow_stick")),
    RED_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/red_glow_stick_active")),
    WHITE_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/white_glow_stick")),
    WHITE_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/white_glow_stick_active")),
    YELLOW_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/yellow_glow_stick")),
    YELLOW_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/yellow_glow_stick_active"));

    private final NamespacedKey key;

    GlowStickVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
