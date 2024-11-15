package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum DripstoneBlock {

    CAVE(new NamespacedKey(TARDIS.plugin, "block/seed/cave"));

    private final NamespacedKey key;

    DripstoneBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
