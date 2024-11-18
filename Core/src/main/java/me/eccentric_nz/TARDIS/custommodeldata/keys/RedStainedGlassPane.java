package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum RedStainedGlassPane {

    RED_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/red_glow_stick")),
    RED_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/red_glow_stick_active"));

    private final NamespacedKey key;

    RedStainedGlassPane(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
