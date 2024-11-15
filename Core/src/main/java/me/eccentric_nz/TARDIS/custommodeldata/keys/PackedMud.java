package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum PackedMud {

    ORIGINAL(new NamespacedKey(TARDIS.plugin, "block/seed/original"));

    private final NamespacedKey key;

    PackedMud(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

