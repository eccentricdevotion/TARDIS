package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum RedStainedGlass {

    TINT_RED(new NamespacedKey(TARDIS.plugin, "block/lights/tint_red"));

    private final NamespacedKey key;

    RedStainedGlass(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
