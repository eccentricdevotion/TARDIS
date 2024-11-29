package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LightVariant {

    OFF(new NamespacedKey(TARDIS.plugin, "lights/off")),
    ON(new NamespacedKey(TARDIS.plugin, "lights/on")),
    CLOISTER(new NamespacedKey(TARDIS.plugin, "lights/cloister")),
    VARIABLE(new NamespacedKey(TARDIS.plugin, "lights/variable")),
    BLUE(new NamespacedKey(TARDIS.plugin, "lights/blue")),
    GREEN(new NamespacedKey(TARDIS.plugin, "lights/green")),
    ORANGE(new NamespacedKey(TARDIS.plugin, "lights/orange")),
    PINK(new NamespacedKey(TARDIS.plugin, "lights/pink")),
    PURPLE(new NamespacedKey(TARDIS.plugin, "lights/purple")),
    YELLOW(new NamespacedKey(TARDIS.plugin, "lights/yellow")),
    BLUE_OFF(new NamespacedKey(TARDIS.plugin, "lights/blue_off")),
    GREEN_OFF(new NamespacedKey(TARDIS.plugin, "lights/green_off")),
    ORANGE_OFF(new NamespacedKey(TARDIS.plugin, "lights/orange_off")),
    PINK_OFF(new NamespacedKey(TARDIS.plugin, "lights/pink_off")),
    PURPLE_OFF(new NamespacedKey(TARDIS.plugin, "lights/purple_off")),
    YELLOW_OFF(new NamespacedKey(TARDIS.plugin, "lights/yellow_off")),
    INTERIOR(new NamespacedKey(TARDIS.plugin, "gui/lights/interior")),
    EXTERIOR(new NamespacedKey(TARDIS.plugin, "gui/lights/exterior")),
    CONSOLE(new NamespacedKey(TARDIS.plugin, "gui/lights/console")),
    BULB(new NamespacedKey(TARDIS.plugin, "lights/bulb"));

    private final NamespacedKey key;

    LightVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
