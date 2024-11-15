package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum RedstoneLamp {

    BLUE_LAMP(new NamespacedKey(TARDIS.plugin, "block/chemistry/blue_lamp")),
    GREEN_LAMP(new NamespacedKey(TARDIS.plugin, "block/chemistry/green_lamp")),
    PURPLE_LAMP(new NamespacedKey(TARDIS.plugin, "block/chemistry/purple_lamp")),
    RED_LAMP(new NamespacedKey(TARDIS.plugin, "block/chemistry/red_lamp")),
    LAMP_ON(new NamespacedKey(TARDIS.plugin, "block/lights/lamp_on")),
    TENTH_ON(new NamespacedKey(TARDIS.plugin, "block/lights/tenth_on")),
    ELEVENTH_ON(new NamespacedKey(TARDIS.plugin, "block/lights/eleventh_on")),
    BULB_ON(new NamespacedKey(TARDIS.plugin, "block/lights/bulb_on")),
    TENTH_CLOISTER(new NamespacedKey(TARDIS.plugin, "block/lights/tenth_cloister")),
    ELEVENTH_CLOISTER(new NamespacedKey(TARDIS.plugin, "block/lights/eleventh_cloister")),
    BULB_CLOISTER(new NamespacedKey(TARDIS.plugin, "block/lights/bulb_cloister"));

    private final NamespacedKey key;

    RedstoneLamp(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
