package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum PurpleStainedGlassPane {

    PURPLE_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/purple_glow_stick")),
    PURPLE_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/purple_glow_stick_active"));

    private final NamespacedKey key;

    PurpleStainedGlassPane(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
