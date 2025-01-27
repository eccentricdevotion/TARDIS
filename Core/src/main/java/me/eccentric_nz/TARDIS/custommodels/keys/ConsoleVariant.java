package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ConsoleVariant {

    CONSOLE_LIGHT_GRAY(new NamespacedKey(TARDIS.plugin, "console_light_gray")),
    CONSOLE_GRAY(new NamespacedKey(TARDIS.plugin, "console_gray")),
    CONSOLE_BLACK(new NamespacedKey(TARDIS.plugin, "console_black")),
    CONSOLE_WHITE(new NamespacedKey(TARDIS.plugin, "console_white")),
    CONSOLE_RED(new NamespacedKey(TARDIS.plugin, "console_red")),
    CONSOLE_ORANGE(new NamespacedKey(TARDIS.plugin, "console_orange")),
    CONSOLE_YELLOW(new NamespacedKey(TARDIS.plugin, "console_yellow")),
    CONSOLE_LIME(new NamespacedKey(TARDIS.plugin, "console_lime")),
    CONSOLE_GREEN(new NamespacedKey(TARDIS.plugin, "console_green")),
    CONSOLE_CYAN(new NamespacedKey(TARDIS.plugin, "console_cyan")),
    CONSOLE_LIGHT_BLUE(new NamespacedKey(TARDIS.plugin, "console_light_blue")),
    CONSOLE_BLUE(new NamespacedKey(TARDIS.plugin, "console_blue")),
    CONSOLE_PURPLE(new NamespacedKey(TARDIS.plugin, "console_purple")),
    CONSOLE_PINK(new NamespacedKey(TARDIS.plugin, "console_pink")),
    CONSOLE_MAGENTA(new NamespacedKey(TARDIS.plugin, "console_magenta")),
    CONSOLE_BROWN(new NamespacedKey(TARDIS.plugin, "console_brown")),
    CONSOLE_RUSTIC(new NamespacedKey(TARDIS.plugin, "console_rustic"));

    private final NamespacedKey key;

    ConsoleVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

