package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LightVariant {

    OFF(new NamespacedKey(TARDIS.plugin, "light_off")),
    ON(new NamespacedKey(TARDIS.plugin, "light_on")),
    CLOISTER(new NamespacedKey(TARDIS.plugin, "light_cloister")),
    VARIABLE(new NamespacedKey(TARDIS.plugin, "light_variable")),
    BLUE(new NamespacedKey(TARDIS.plugin, "light_blue")),
    GREEN(new NamespacedKey(TARDIS.plugin, "light_green")),
    ORANGE(new NamespacedKey(TARDIS.plugin, "light_orange")),
    PINK(new NamespacedKey(TARDIS.plugin, "light_pink")),
    PURPLE(new NamespacedKey(TARDIS.plugin, "light_purple")),
    YELLOW(new NamespacedKey(TARDIS.plugin, "light_yellow")),
    BLUE_OFF(new NamespacedKey(TARDIS.plugin, "light_blue_off")),
    GREEN_OFF(new NamespacedKey(TARDIS.plugin, "light_green_off")),
    ORANGE_OFF(new NamespacedKey(TARDIS.plugin, "light_orange_off")),
    PINK_OFF(new NamespacedKey(TARDIS.plugin, "light_pink_off")),
    PURPLE_OFF(new NamespacedKey(TARDIS.plugin, "light_purple_off")),
    YELLOW_OFF(new NamespacedKey(TARDIS.plugin, "light_yellow_off")),
    INTERIOR(new NamespacedKey(TARDIS.plugin, "lights_interior")),
    EXTERIOR(new NamespacedKey(TARDIS.plugin, "lights_exterior")),
    CONSOLE(new NamespacedKey(TARDIS.plugin, "lights_console")),
    BULB(new NamespacedKey(TARDIS.plugin, "light_bulb"));

    private final NamespacedKey key;

    LightVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
