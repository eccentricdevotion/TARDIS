package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BlueStainedGlassPane {

    BLUE_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/blue_glow_stick")),
    BLUE_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/blue_glow_stick_active"));

    private final NamespacedKey key;

    BlueStainedGlassPane(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
