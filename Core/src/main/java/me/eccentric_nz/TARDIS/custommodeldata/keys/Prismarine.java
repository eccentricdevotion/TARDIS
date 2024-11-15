package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Prismarine {

    TWELFTH(new NamespacedKey(TARDIS.plugin, "block/seed/twelfth"));

    private final NamespacedKey key;

    Prismarine(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
