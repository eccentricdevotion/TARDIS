package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum IronSword {

    RUST_PLAGUE_SWORD(new NamespacedKey(TARDIS.plugin, "planets/rust_plague_sword"));

    private final NamespacedKey key;

    IronSword(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
