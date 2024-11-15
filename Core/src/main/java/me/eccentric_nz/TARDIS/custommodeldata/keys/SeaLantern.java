package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SeaLantern {

    BLUE_LAMP_ON(new NamespacedKey(TARDIS.plugin, "block/chemistry/blue_lamp_on")),
    GREEN_LAMP_ON(new NamespacedKey(TARDIS.plugin, "block/chemistry/green_lamp_on")),
    PURPLE_LAMP_ON(new NamespacedKey(TARDIS.plugin, "block/chemistry/purple_lamp_on")),
    RED_LAMP_ON(new NamespacedKey(TARDIS.plugin, "block/chemistry/red_lamp_on")),
    CLASSIC_ON(new NamespacedKey(TARDIS.plugin, "block/lights/classic_on")),
    TWELFTH_ON(new NamespacedKey(TARDIS.plugin, "block/lights/twelfth_on")),
    THIRTEENTH_ON(new NamespacedKey(TARDIS.plugin, "block/lights/thirteenth_on")),
    CLASSIC_OFFSET_ON(new NamespacedKey(TARDIS.plugin, "block/lights/classic_offset_on")),
    CLASSIC_CLOISTER(new NamespacedKey(TARDIS.plugin, "block/lights/classic_cloister")),
    TWELFTH_CLOISTER(new NamespacedKey(TARDIS.plugin, "block/lights/twelfth_cloister")),
    THIRTEENTH_CLOISTER(new NamespacedKey(TARDIS.plugin, "block/lights/thirteenth_cloister")),
    CLASSIC_OFFSET_CLOISTER(new NamespacedKey(TARDIS.plugin, "block/lights/classic_offset_cloister"));

    private final NamespacedKey key;

    SeaLantern(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
