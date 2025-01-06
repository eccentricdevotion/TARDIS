package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Schism {

    UNTEMPERED_SCHISM_BLOCK(new NamespacedKey(TARDIS.plugin, "untempered_schism_block")),
    UNTEMPERED_SCHISM(new NamespacedKey(TARDIS.plugin, "untempered_schism"));

    private final NamespacedKey key;

    Schism(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
