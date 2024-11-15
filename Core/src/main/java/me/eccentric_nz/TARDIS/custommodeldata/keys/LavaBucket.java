package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LavaBucket {

    RUST_BUCKET(new NamespacedKey(TARDIS.plugin, "item/planets/rust_bucket"));

    private final NamespacedKey key;

    LavaBucket(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
