package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum YellowStainedGlassPane {

    YELLOW_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/yellow_glow_stick")),
    YELLOW_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "products/glow_sticks/yellow_glow_stick_active"));

    private final NamespacedKey key;

    YellowStainedGlassPane(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
