package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum PinkStainedGlass {

    TINT_PINK(new NamespacedKey(TARDIS.plugin, "block/lights/tint_pink"));

    private final NamespacedKey key;

    PinkStainedGlass(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
