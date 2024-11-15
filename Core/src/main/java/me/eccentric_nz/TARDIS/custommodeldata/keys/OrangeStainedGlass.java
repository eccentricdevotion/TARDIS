package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum OrangeStainedGlass {


    TINT_ORANGE(new NamespacedKey(TARDIS.plugin, "block/lights/tint_orange"));

    private final NamespacedKey key;

    OrangeStainedGlass(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
