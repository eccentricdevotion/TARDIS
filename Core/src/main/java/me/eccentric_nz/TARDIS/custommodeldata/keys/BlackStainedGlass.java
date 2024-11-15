package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BlackStainedGlass {


    TINT_BLACK(new NamespacedKey(TARDIS.plugin, "block/lights/tint_black"));

    private final NamespacedKey key;

    BlackStainedGlass(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
