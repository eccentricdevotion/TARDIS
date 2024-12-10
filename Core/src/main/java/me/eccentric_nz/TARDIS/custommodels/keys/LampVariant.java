package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LampVariant {

    BLUE_LAMP(new NamespacedKey(TARDIS.plugin, "blue_lamp")),
    GREEN_LAMP(new NamespacedKey(TARDIS.plugin, "green_lamp")),
    PURPLE_LAMP(new NamespacedKey(TARDIS.plugin, "purple_lamp")),
    RED_LAMP(new NamespacedKey(TARDIS.plugin, "red_lamp")),
    LAMP_ON(new NamespacedKey(TARDIS.plugin, "light_lamp_on")),
    TENTH_ON(new NamespacedKey(TARDIS.plugin, "light_tenth_on")),
    ELEVENTH_ON(new NamespacedKey(TARDIS.plugin, "light_eleventh_on")),
    BULB_ON(new NamespacedKey(TARDIS.plugin, "light_bulb_on")),
    TENTH_CLOISTER(new NamespacedKey(TARDIS.plugin, "light_tenth_cloister")),
    ELEVENTH_CLOISTER(new NamespacedKey(TARDIS.plugin, "light_eleventh_cloister")),
    BULB_CLOISTER(new NamespacedKey(TARDIS.plugin, "light_bulb_cloister"));

    private final NamespacedKey key;

    LampVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
