package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LimeConcrete {

    PRODUCT(new NamespacedKey(TARDIS.plugin, "block/chemistry/product"));

    private final NamespacedKey key;

    LimeConcrete(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

