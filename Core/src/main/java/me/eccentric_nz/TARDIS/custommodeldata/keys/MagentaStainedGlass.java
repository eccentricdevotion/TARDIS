package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum MagentaStainedGlass {


    TINT_MAGENTA(new NamespacedKey(TARDIS.plugin, "block/lights/tint_magenta"));

    private final NamespacedKey key;

    MagentaStainedGlass(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
