package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum IronBlock {

    BUDGET(new NamespacedKey(TARDIS.plugin, "block/seed/budget"));

    private final NamespacedKey key;

    IronBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
