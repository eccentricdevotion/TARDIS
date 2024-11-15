package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BlueStainedGlass {

    TINT_BLUE(new NamespacedKey(TARDIS.plugin, "block/lights/tint_blue"));

    private final NamespacedKey key;

    BlueStainedGlass(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
