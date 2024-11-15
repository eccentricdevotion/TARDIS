package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum GrayShulkerBox {

    EYE_STORAGE(new NamespacedKey(TARDIS.plugin, "block/tardis/eye_storage"));

    private final NamespacedKey key;

    GrayShulkerBox(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

