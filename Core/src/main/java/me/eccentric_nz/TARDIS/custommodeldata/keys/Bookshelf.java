package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Bookshelf {

    PLANK(new NamespacedKey(TARDIS.plugin, "block/seed/plank"));

    private final NamespacedKey key;

    Bookshelf(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
