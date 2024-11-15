package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum YellowGlazedTerracotta {

    BIRDCAGE(new NamespacedKey(TARDIS.plugin, "item/gui/room/birdcage"));

    private final NamespacedKey key;

    YellowGlazedTerracotta(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
