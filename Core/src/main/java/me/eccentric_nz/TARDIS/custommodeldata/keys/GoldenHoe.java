package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum GoldenHoe {

    BLASTER(new NamespacedKey(TARDIS.plugin, "item/sonic/blaster"));

    private final NamespacedKey key;

    GoldenHoe(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

