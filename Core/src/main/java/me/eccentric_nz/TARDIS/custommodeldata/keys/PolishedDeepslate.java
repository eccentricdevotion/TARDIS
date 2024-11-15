package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum PolishedDeepslate {

    FUGITIVE(new NamespacedKey(TARDIS.plugin, "block/seed/fugitive"));

    private final NamespacedKey key;

    PolishedDeepslate(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

