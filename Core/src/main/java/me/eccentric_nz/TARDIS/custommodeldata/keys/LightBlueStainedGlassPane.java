package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LightBlueStainedGlassPane {

    LIGHT_BLUE_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "item/products/glow_sticks/light_blue_glow_stick")),
    LIGHT_BLUE_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "item/products/glow_sticks/light_blue_glow_stick_active"));

    private final NamespacedKey key;

    LightBlueStainedGlassPane(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
