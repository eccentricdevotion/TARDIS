package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum OrangeGlazedTerracotta {

    LEGACY_BIGGER(new NamespacedKey(TARDIS.plugin, "block/seed/legacy_bigger"));

    private final NamespacedKey key;

    OrangeGlazedTerracotta(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
