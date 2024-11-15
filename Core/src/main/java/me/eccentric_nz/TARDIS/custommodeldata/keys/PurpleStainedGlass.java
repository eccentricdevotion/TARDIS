package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum PurpleStainedGlass {


    TINT_PURPLE(new NamespacedKey(TARDIS.plugin, "block/lights/tint_purple"));

    private final NamespacedKey key;

    PurpleStainedGlass(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
