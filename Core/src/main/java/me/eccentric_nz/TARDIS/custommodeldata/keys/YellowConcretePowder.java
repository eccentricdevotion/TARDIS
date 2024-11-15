package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum YellowConcretePowder {

    FACTORY(new NamespacedKey(TARDIS.plugin, "block/seed/factory"));

    private final NamespacedKey key;

    YellowConcretePowder(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
