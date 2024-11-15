package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum CyanGlazedTerracotta {

    LEGACY_ELEVENTH(new NamespacedKey(TARDIS.plugin, "block/seed/legacy_eleventh"));

    private final NamespacedKey key;

    CyanGlazedTerracotta(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
