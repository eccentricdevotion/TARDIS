package me.eccentric_nz.TARDIS.custommodels.keys;

import org.bukkit.NamespacedKey;

public enum DaylightDetector {

    ;

    private final NamespacedKey key;

    DaylightDetector(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
