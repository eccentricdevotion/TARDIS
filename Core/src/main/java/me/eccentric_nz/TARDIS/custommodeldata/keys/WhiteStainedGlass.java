package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum WhiteStainedGlass {


    TINT_WHITE(new NamespacedKey(TARDIS.plugin, "block/lights/tint_white"));

    private final NamespacedKey key;

    WhiteStainedGlass(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
