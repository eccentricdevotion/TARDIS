package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LampVariant {

    BLUE_LAMP(new NamespacedKey(TARDIS.plugin, "chemistry/blue_lamp")),
    GREEN_LAMP(new NamespacedKey(TARDIS.plugin, "chemistry/green_lamp")),
    PURPLE_LAMP(new NamespacedKey(TARDIS.plugin, "chemistry/purple_lamp")),
    RED_LAMP(new NamespacedKey(TARDIS.plugin, "chemistry/red_lamp")),
    LAMP_ON(new NamespacedKey(TARDIS.plugin, "lights/lamp_on")),
    TENTH_ON(new NamespacedKey(TARDIS.plugin, "lights/tenth_on")),
    ELEVENTH_ON(new NamespacedKey(TARDIS.plugin, "lights/eleventh_on")),
    BULB_ON(new NamespacedKey(TARDIS.plugin, "lights/bulb_on")),
    TENTH_CLOISTER(new NamespacedKey(TARDIS.plugin, "lights/tenth_cloister")),
    ELEVENTH_CLOISTER(new NamespacedKey(TARDIS.plugin, "lights/eleventh_cloister")),
    BULB_CLOISTER(new NamespacedKey(TARDIS.plugin, "lights/bulb_cloister"));

    private final NamespacedKey key;

    LampVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
