package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Coal {

    BLOCK(new NamespacedKey(TARDIS.plugin, "item/gui/particle/block"));

    private final NamespacedKey key;

    Coal(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
