package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum WaxedCopperBulb {

    BULB(new NamespacedKey(TARDIS.plugin, "block/lights/bulb"));

    private final NamespacedKey key;

    WaxedCopperBulb(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
