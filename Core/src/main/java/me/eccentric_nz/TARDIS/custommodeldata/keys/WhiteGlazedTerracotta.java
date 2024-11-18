package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum WhiteGlazedTerracotta {


    IISTUBIL(new NamespacedKey(TARDIS.plugin, "gui/room/iistubil"));

    private final NamespacedKey key;

    WhiteGlazedTerracotta(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
