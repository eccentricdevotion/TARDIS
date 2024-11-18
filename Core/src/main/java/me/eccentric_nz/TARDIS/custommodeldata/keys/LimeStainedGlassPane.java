package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LimeStainedGlassPane {

    LIME_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/lime_glow_stick")),
    LIME_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/lime_glow_stick_active"));

    private final NamespacedKey key;

    LimeStainedGlassPane(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
