package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum HoneycombBlock {

    ROTOR(new NamespacedKey(TARDIS.plugin, "block/seed/rotor"));

    private final NamespacedKey key;

    HoneycombBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
