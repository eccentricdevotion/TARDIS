package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum QuartzBlock {

    ARS(new NamespacedKey(TARDIS.plugin, "block/seed/ars"));

    private final NamespacedKey key;

    QuartzBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
