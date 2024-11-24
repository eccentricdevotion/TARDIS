package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LightBlueConcrete {

    CONSTRUCTOR(new NamespacedKey(TARDIS.plugin, "block/chemistry/constructor"));

    private final NamespacedKey key;

    LightBlueConcrete(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

