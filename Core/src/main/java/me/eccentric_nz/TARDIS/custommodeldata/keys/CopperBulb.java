package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum CopperBulb {

    RUSTIC(new NamespacedKey(TARDIS.plugin, "block/seed/rustic"));

    private final NamespacedKey key;

    CopperBulb(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
