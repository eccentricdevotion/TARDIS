package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum GreenStainedGlassPane {

    GREEN_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/green_glow_stick")),
    GREEN_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/green_glow_stick_active"));

    private final NamespacedKey key;

    GreenStainedGlassPane(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
