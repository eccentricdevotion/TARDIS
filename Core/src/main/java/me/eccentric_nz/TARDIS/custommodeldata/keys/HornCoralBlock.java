package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum HornCoralBlock {

    THIRTEENTH(new NamespacedKey(TARDIS.plugin, "block/seed/thirteenth"));

    private final NamespacedKey key;

    HornCoralBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

