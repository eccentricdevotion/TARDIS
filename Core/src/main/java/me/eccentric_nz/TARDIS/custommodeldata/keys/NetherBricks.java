package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum NetherBricks {

    MASTER(new NamespacedKey(TARDIS.plugin, "block/seed/master"));

    private final NamespacedKey key;

    NetherBricks(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
