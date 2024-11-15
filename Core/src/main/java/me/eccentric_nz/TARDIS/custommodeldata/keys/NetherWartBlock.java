package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum NetherWartBlock {

    CORAL(new NamespacedKey(TARDIS.plugin, "block/seed/coral"));

    private final NamespacedKey key;

    NetherWartBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
