package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Glowstone {

    BEDROOM(new NamespacedKey(TARDIS.plugin, "item/gui/room/bedroom"));

    private final NamespacedKey key;

    Glowstone(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
