package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LanternVariant {

    BLUE_LAMP_ON(new NamespacedKey(TARDIS.plugin, "blue_lamp_on")),
    GREEN_LAMP_ON(new NamespacedKey(TARDIS.plugin, "green_lamp_on")),
    PURPLE_LAMP_ON(new NamespacedKey(TARDIS.plugin, "purple_lamp_on")),
    RED_LAMP_ON(new NamespacedKey(TARDIS.plugin, "red_lamp_on")),
    CLASSIC_ON(new NamespacedKey(TARDIS.plugin, "light_classic_on")),
    TWELFTH_ON(new NamespacedKey(TARDIS.plugin, "light_twelfth_on")),
    THIRTEENTH_ON(new NamespacedKey(TARDIS.plugin, "light_thirteenth_on")),
    CLASSIC_OFFSET_ON(new NamespacedKey(TARDIS.plugin, "light_classic_offset_on")),
    CLASSIC_CLOISTER(new NamespacedKey(TARDIS.plugin, "light_classic_cloister")),
    TWELFTH_CLOISTER(new NamespacedKey(TARDIS.plugin, "light_twelfth_cloister")),
    THIRTEENTH_CLOISTER(new NamespacedKey(TARDIS.plugin, "light_thirteenth_cloister")),
    CLASSIC_OFFSET_CLOISTER(new NamespacedKey(TARDIS.plugin, "light_classic_offset_cloister"));

    private final NamespacedKey key;

    LanternVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
