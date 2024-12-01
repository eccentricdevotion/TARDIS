package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum JellyBabyVariant {

    JELLY_BABY_WHITE(new NamespacedKey(TARDIS.plugin, "food/jelly_baby_white")),
    JELLY_BABY_ORANGE(new NamespacedKey(TARDIS.plugin, "food/jelly_baby_orange")),
    JELLY_BABY_MAGENTA(new NamespacedKey(TARDIS.plugin, "food/jelly_baby_magenta")),
    JELLY_BABY_LIGHT_BLUE(new NamespacedKey(TARDIS.plugin, "food/jelly_baby_light_blue")),
    JELLY_BABY_YELLOW(new NamespacedKey(TARDIS.plugin, "food/jelly_baby_yellow")),
    JELLY_BABY_LIME(new NamespacedKey(TARDIS.plugin, "food/jelly_baby_lime")),
    JELLY_BABY_PINK(new NamespacedKey(TARDIS.plugin, "food/jelly_baby_pink")),
    JELLY_BABY_GRAY(new NamespacedKey(TARDIS.plugin, "food/jelly_baby_gray")),
    JELLY_BABY_LIGHT_GRAY(new NamespacedKey(TARDIS.plugin, "food/jelly_baby_light_gray")),
    JELLY_BABY_CYAN(new NamespacedKey(TARDIS.plugin, "food/jelly_baby_cyan")),
    JELLY_BABY_PURPLE(new NamespacedKey(TARDIS.plugin, "food/jelly_baby_purple")),
    JELLY_BABY_BLUE(new NamespacedKey(TARDIS.plugin, "food/jelly_baby_blue")),
    JELLY_BABY_BROWN(new NamespacedKey(TARDIS.plugin, "food/jelly_baby_brown")),
    JELLY_BABY_GREEN(new NamespacedKey(TARDIS.plugin, "food/jelly_baby_green")),
    JELLY_BABY_RED(new NamespacedKey(TARDIS.plugin, "food/jelly_baby_red")),
    JELLY_BABY_BLACK(new NamespacedKey(TARDIS.plugin, "food/jelly_baby_black"));

    private final NamespacedKey key;

    JellyBabyVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
