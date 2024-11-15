package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum PolishedAndesite {

    MECHANICAL(new NamespacedKey(TARDIS.plugin, "block/seed/mechanical"));

    private final NamespacedKey key;

    PolishedAndesite(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

