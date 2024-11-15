package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum AncientDebris {

    UNTEMPERED_SCHISM_BLOCK(new NamespacedKey(TARDIS.plugin, "regeneration/untempered_schism_block")),
    UNTEMPERED_SCHISM(new NamespacedKey(TARDIS.plugin, "regeneration/untempered_schism"));

    private final NamespacedKey key;

    AncientDebris(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
