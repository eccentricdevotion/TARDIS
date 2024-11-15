package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum CopperBlock {

    COPPER(new NamespacedKey(TARDIS.plugin, "block/seed/copper"));

    private final NamespacedKey key;

    CopperBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

