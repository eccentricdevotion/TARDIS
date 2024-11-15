package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Obsidian {

    CUSTOM(new NamespacedKey(TARDIS.plugin, "block/seed/custom"));

    private final NamespacedKey key;

    Obsidian(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
