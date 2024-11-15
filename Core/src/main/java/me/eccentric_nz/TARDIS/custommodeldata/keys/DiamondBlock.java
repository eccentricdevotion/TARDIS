package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum DiamondBlock {

    DELUXE(new NamespacedKey(TARDIS.plugin, "block/seed/deluxe"));

    private final NamespacedKey key;

    DiamondBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
