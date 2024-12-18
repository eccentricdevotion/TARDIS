package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum DalekVariant {

    BUTTON_DALEK(new NamespacedKey(TARDIS.plugin, "button_dalek")),
    DALEK_BOW(new NamespacedKey(TARDIS.plugin, "dalek_bow")),
    DALEK_HEAD(new NamespacedKey(TARDIS.plugin, "dalek_head")),
    DALEK_BRASS(new NamespacedKey(TARDIS.plugin, "dalek_brass")),
    DALEK_WHITE(new NamespacedKey(TARDIS.plugin, "dalek_white")),
    DALEK_ORANGE(new NamespacedKey(TARDIS.plugin, "dalek_orange")),
    DALEK_MAGENTA(new NamespacedKey(TARDIS.plugin, "dalek_magenta")),
    DALEK_LIGHT_BLUE(new NamespacedKey(TARDIS.plugin, "dalek_light_blue")),
    DALEK_YELLOW(new NamespacedKey(TARDIS.plugin, "dalek_yellow")),
    DALEK_LIME(new NamespacedKey(TARDIS.plugin, "dalek_lime")),
    DALEK_PINK(new NamespacedKey(TARDIS.plugin, "dalek_pink")),
    DALEK_GRAY(new NamespacedKey(TARDIS.plugin, "dalek_gray")),
    DALEK_LIGHT_GRAY(new NamespacedKey(TARDIS.plugin, "dalek_light_gray")),
    DALEK_CYAN(new NamespacedKey(TARDIS.plugin, "dalek_cyan")),
    DALEK_PURPLE(new NamespacedKey(TARDIS.plugin, "dalek_purple")),
    DALEK_BLUE(new NamespacedKey(TARDIS.plugin, "dalek_blue")),
    DALEK_BROWN(new NamespacedKey(TARDIS.plugin, "dalek_brown")),
    DALEK_GREEN(new NamespacedKey(TARDIS.plugin, "dalek_green")),
    DALEK_RED(new NamespacedKey(TARDIS.plugin, "dalek_red")),
    DALEK_BLACK(new NamespacedKey(TARDIS.plugin, "dalek_black"));

    private final NamespacedKey key;

    DalekVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
