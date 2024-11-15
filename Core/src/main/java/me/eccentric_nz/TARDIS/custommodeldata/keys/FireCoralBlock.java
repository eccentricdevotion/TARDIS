package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum FireCoralBlock {

    CORAL(new NamespacedKey(TARDIS.plugin, "block/seed/coral"));

    private final NamespacedKey key;

    FireCoralBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

