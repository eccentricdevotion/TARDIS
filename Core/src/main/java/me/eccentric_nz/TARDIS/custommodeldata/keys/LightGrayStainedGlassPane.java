package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LightGrayStainedGlassPane {

    LIGHT_GRAY_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/light_gray_glow_stick")),
    LIGHT_GRAY_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/light_gray_glow_stick_active"));

    private final NamespacedKey key;

    LightGrayStainedGlassPane(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
