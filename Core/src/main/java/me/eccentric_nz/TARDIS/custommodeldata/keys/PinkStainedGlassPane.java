package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum PinkStainedGlassPane {

    PINK_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/pink_glow_stick")),
    PINK_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/pink_glow_stick_active"));

    private final NamespacedKey key;

    PinkStainedGlassPane(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
