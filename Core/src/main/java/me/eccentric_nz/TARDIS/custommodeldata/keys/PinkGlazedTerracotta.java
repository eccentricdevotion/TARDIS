package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum PinkGlazedTerracotta {

    DIVISION(new NamespacedKey(TARDIS.plugin, "block/seed/division"));

    private final NamespacedKey key;

    PinkGlazedTerracotta(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

