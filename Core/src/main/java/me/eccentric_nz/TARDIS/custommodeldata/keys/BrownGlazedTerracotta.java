package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BrownGlazedTerracotta {

    STALL(new NamespacedKey(TARDIS.plugin, "item/gui/room/stall"));

    private final NamespacedKey key;

    BrownGlazedTerracotta(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
