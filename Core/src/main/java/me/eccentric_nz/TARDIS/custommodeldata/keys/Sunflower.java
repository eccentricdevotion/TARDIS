package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Sunflower {

    CLEAR(new NamespacedKey(TARDIS.plugin, "item/gui/clear"));

    private final NamespacedKey key;

    Sunflower(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
