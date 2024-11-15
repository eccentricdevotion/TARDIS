package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum WhiteTerracotta {

    WAR(new NamespacedKey(TARDIS.plugin, "block/seed/war"));

    private final NamespacedKey key;

    WhiteTerracotta(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
