package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum RedGlazedTerracotta {

    LEGACY_REDSTONE(new NamespacedKey(TARDIS.plugin, "block/seed/legacy_redstone"));

    private final NamespacedKey key;

    RedGlazedTerracotta(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
