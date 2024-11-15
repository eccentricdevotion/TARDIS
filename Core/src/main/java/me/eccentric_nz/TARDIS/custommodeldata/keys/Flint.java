package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Flint {

    STATTENHEIM_REMOTE(new NamespacedKey(TARDIS.plugin, "item/tardis/stattenheim_remote"));

    private final NamespacedKey key;

    Flint(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
