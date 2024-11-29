package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum TintVariant {

    TINT_BLACK(new NamespacedKey(TARDIS.plugin, "lights/tint_black")),
    TINT_BLUE(new NamespacedKey(TARDIS.plugin, "lights/tint_blue")),
    TINT_BROWN(new NamespacedKey(TARDIS.plugin, "lights/tint_brown")),
    TINT_CYAN(new NamespacedKey(TARDIS.plugin, "lights/tint_cyan")),
    TINT_GRAY(new NamespacedKey(TARDIS.plugin, "lights/tint_gray")),
    TINT_GREEN(new NamespacedKey(TARDIS.plugin, "lights/tint_green")),
    TINT_LIGHT_BLUE(new NamespacedKey(TARDIS.plugin, "lights/tint_light_blue")),
    TINT_LIGHT_GRAY(new NamespacedKey(TARDIS.plugin, "lights/tint_light_gray")),
    TINT_LIME(new NamespacedKey(TARDIS.plugin, "lights/tint_lime")),
    TINT_MAGENTA(new NamespacedKey(TARDIS.plugin, "lights/tint_magenta")),
    TINT_ORANGE(new NamespacedKey(TARDIS.plugin, "lights/tint_orange")),
    TINT_PINK(new NamespacedKey(TARDIS.plugin, "lights/tint_pink")),
    TINT_PURPLE(new NamespacedKey(TARDIS.plugin, "lights/tint_purple")),
    TINT_RED(new NamespacedKey(TARDIS.plugin, "lights/tint_red")),
    TINT_WHITE(new NamespacedKey(TARDIS.plugin, "lights/tint_white")),
    TINT_YELLOW(new NamespacedKey(TARDIS.plugin, "lights/tint_yellow"));

    private final NamespacedKey key;

    TintVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
