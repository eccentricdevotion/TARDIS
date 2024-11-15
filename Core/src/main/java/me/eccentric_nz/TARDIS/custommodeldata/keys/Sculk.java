package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Sculk {

    ANCIENT(new NamespacedKey(TARDIS.plugin, "block/seed/ancient"));

    private final NamespacedKey key;

    Sculk(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

