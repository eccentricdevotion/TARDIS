package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum CryingObsidian {

    DELTA(new NamespacedKey(TARDIS.plugin, "block/seed/delta"));

    private final NamespacedKey key;

    CryingObsidian(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
