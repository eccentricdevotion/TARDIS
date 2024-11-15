package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SandstoneStairs {

    PYRAMID(new NamespacedKey(TARDIS.plugin, "block/seed/pyramid"));

    private final NamespacedKey key;

    SandstoneStairs(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
