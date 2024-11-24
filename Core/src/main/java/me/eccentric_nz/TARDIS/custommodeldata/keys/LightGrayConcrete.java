package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LightGrayConcrete {

    CREATIVE(new NamespacedKey(TARDIS.plugin, "block/chemistry/creative"));

    private final NamespacedKey key;

    LightGrayConcrete(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

