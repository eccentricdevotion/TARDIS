package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum OchreFroglight {

    FIFTEENTH(new NamespacedKey(TARDIS.plugin, "block/seed/fifteenth"));

    private final NamespacedKey key;

    OchreFroglight(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
