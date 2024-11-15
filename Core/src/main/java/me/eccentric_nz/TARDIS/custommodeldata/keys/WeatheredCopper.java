package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum WeatheredCopper {

    WEATHERED(new NamespacedKey(TARDIS.plugin, "block/seed/weathered"));

    private final NamespacedKey key;

    WeatheredCopper(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
