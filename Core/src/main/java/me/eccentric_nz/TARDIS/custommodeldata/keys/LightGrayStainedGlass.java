package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LightGrayStainedGlass {

    TINT_LIGHT_GRAY(new NamespacedKey(TARDIS.plugin, "block/lights/tint_light_gray"));

    private final NamespacedKey key;

    LightGrayStainedGlass(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
