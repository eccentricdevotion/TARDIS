package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum PurpurBlock {

    ENDER(new NamespacedKey(TARDIS.plugin, "block/seed/ender"));

    private final NamespacedKey key;

    PurpurBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
