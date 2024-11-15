package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Shroomlight {

    EYE(new NamespacedKey(TARDIS.plugin, "item/gui/room/eye"));

    private final NamespacedKey key;

    Shroomlight(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

