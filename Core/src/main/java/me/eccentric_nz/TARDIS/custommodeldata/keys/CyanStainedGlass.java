package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum CyanStainedGlass {

    TINT_CYAN(new NamespacedKey(TARDIS.plugin, "block/lights/tint_cyan"));

    private final NamespacedKey key;

    CyanStainedGlass(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
