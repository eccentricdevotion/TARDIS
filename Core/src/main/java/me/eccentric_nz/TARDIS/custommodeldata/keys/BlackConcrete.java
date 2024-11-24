package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BlackConcrete {

    PANDORICA(new NamespacedKey(TARDIS.plugin, "block/pandorica"));

    private final NamespacedKey key;

    BlackConcrete(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

