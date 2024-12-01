package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ConsolePart {

    CONSOLE_LIGHT_GRAY(new NamespacedKey(TARDIS.plugin, "tardis/console_light_gray")),
    CONSOLE_GRAY(new NamespacedKey(TARDIS.plugin, "tardis/console_gray")),
    CONSOLE_BLACK(new NamespacedKey(TARDIS.plugin, "tardis/console_black")),
    CONSOLE_WHITE(new NamespacedKey(TARDIS.plugin, "tardis/console_white")),
    CONSOLE_RED(new NamespacedKey(TARDIS.plugin, "tardis/console_red")),
    CONSOLE_ORANGE(new NamespacedKey(TARDIS.plugin, "tardis/console_orange")),
    CONSOLE_YELLOW(new NamespacedKey(TARDIS.plugin, "tardis/console_yellow")),
    CONSOLE_LIME(new NamespacedKey(TARDIS.plugin, "tardis/console_lime")),
    CONSOLE_GREEN(new NamespacedKey(TARDIS.plugin, "tardis/console_green")),
    CONSOLE_CYAN(new NamespacedKey(TARDIS.plugin, "tardis/console_cyan")),
    CONSOLE_LIGHT_BLUE(new NamespacedKey(TARDIS.plugin, "tardis/console_light_blue")),
    CONSOLE_BLUE(new NamespacedKey(TARDIS.plugin, "tardis/console_blue")),
    CONSOLE_PURPLE(new NamespacedKey(TARDIS.plugin, "tardis/console_purple")),
    CONSOLE_MAGENTA(new NamespacedKey(TARDIS.plugin, "tardis/console_magenta")),
    CONSOLE_PINK(new NamespacedKey(TARDIS.plugin, "tardis/console_pink")),
    CONSOLE_BROWN(new NamespacedKey(TARDIS.plugin, "tardis/console_brown")),
    CONSOLE_RUSTIC(new NamespacedKey(TARDIS.plugin, "tardis/console_rustic")),
    CONSOLE_DIVISION_LIGHT_GRAY(new NamespacedKey(TARDIS.plugin, "tardis/console_division_light_gray")),
    CONSOLE_DIVISION_GRAY(new NamespacedKey(TARDIS.plugin, "tardis/console_division_gray")),
    CONSOLE_DIVISION_BLACK(new NamespacedKey(TARDIS.plugin, "tardis/console_division_black")),
    CONSOLE_DIVISION_WHITE(new NamespacedKey(TARDIS.plugin, "tardis/console_division_white")),
    CONSOLE_DIVISION_RED(new NamespacedKey(TARDIS.plugin, "tardis/console_division_red")),
    CONSOLE_DIVISION_ORANGE(new NamespacedKey(TARDIS.plugin, "tardis/console_division_orange")),
    CONSOLE_DIVISION_YELLOW(new NamespacedKey(TARDIS.plugin, "tardis/console_division_yellow")),
    CONSOLE_DIVISION_LIME(new NamespacedKey(TARDIS.plugin, "tardis/console_division_lime")),
    CONSOLE_DIVISION_GREEN(new NamespacedKey(TARDIS.plugin, "tardis/console_division_green")),
    CONSOLE_DIVISION_CYAN(new NamespacedKey(TARDIS.plugin, "tardis/console_division_cyan")),
    CONSOLE_DIVISION_LIGHT_BLUE(new NamespacedKey(TARDIS.plugin, "tardis/console_division_light_blue")),
    CONSOLE_DIVISION_BLUE(new NamespacedKey(TARDIS.plugin, "tardis/console_division_blue")),
    CONSOLE_DIVISION_PURPLE(new NamespacedKey(TARDIS.plugin, "tardis/console_division_purple")),
    CONSOLE_DIVISION_MAGENTA(new NamespacedKey(TARDIS.plugin, "tardis/console_division_magenta")),
    CONSOLE_DIVISION_PINK(new NamespacedKey(TARDIS.plugin, "tardis/console_division_pink")),
    CONSOLE_DIVISION_BROWN(new NamespacedKey(TARDIS.plugin, "tardis/console_division_brown")),
    CONSOLE_DIVISION_RUSTIC(new NamespacedKey(TARDIS.plugin, "tardis/console_division_rustic")),
    CONSOLE_CENTRE_LIGHT_GRAY(new NamespacedKey(TARDIS.plugin, "tardis/console_centre_light_gray")),
    CONSOLE_CENTRE_GRAY(new NamespacedKey(TARDIS.plugin, "tardis/console_centre_gray")),
    CONSOLE_CENTRE_BLACK(new NamespacedKey(TARDIS.plugin, "tardis/console_centre_black")),
    CONSOLE_CENTRE_WHITE(new NamespacedKey(TARDIS.plugin, "tardis/console_centre_white")),
    CONSOLE_CENTRE_RED(new NamespacedKey(TARDIS.plugin, "tardis/console_centre_red")),
    CONSOLE_CENTRE_ORANGE(new NamespacedKey(TARDIS.plugin, "tardis/console_centre_orange")),
    CONSOLE_CENTRE_YELLOW(new NamespacedKey(TARDIS.plugin, "tardis/console_centre_yellow")),
    CONSOLE_CENTRE_LIME(new NamespacedKey(TARDIS.plugin, "tardis/console_centre_lime")),
    CONSOLE_CENTRE_GREEN(new NamespacedKey(TARDIS.plugin, "tardis/console_centre_green")),
    CONSOLE_CENTRE_CYAN(new NamespacedKey(TARDIS.plugin, "tardis/console_centre_cyan")),
    CONSOLE_CENTRE_LIGHT_BLUE(new NamespacedKey(TARDIS.plugin, "tardis/console_centre_light_blue")),
    CONSOLE_CENTRE_BLUE(new NamespacedKey(TARDIS.plugin, "tardis/console_centre_blue")),
    CONSOLE_CENTRE_PURPLE(new NamespacedKey(TARDIS.plugin, "tardis/console_centre_purple")),
    CONSOLE_CENTRE_MAGENTA(new NamespacedKey(TARDIS.plugin, "tardis/console_centre_magenta")),
    CONSOLE_CENTRE_PINK(new NamespacedKey(TARDIS.plugin, "tardis/console_centre_pink")),
    CONSOLE_CENTRE_BROWN(new NamespacedKey(TARDIS.plugin, "tardis/console_centre_brown")),
    CONSOLE_CENTRE_RUSTIC(new NamespacedKey(TARDIS.plugin, "tardis/console_centre_rustic"));

    private final NamespacedKey key;

    ConsolePart(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
