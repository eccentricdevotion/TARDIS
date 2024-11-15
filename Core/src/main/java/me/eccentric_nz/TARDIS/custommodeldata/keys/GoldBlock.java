package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum GoldBlock {

    BIGGER(new NamespacedKey(TARDIS.plugin, "block/seed/bigger"));

    private final NamespacedKey key;

    GoldBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
