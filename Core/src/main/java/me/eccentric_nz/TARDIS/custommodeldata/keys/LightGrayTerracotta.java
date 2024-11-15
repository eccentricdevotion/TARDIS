package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LightGrayTerracotta {

    GROW(new NamespacedKey(TARDIS.plugin, "block/seed/grow"));

    private final NamespacedKey key;

    LightGrayTerracotta(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

