package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Bricks {

    TRENZALORE(new NamespacedKey(TARDIS.plugin, "item/gui/room/trenzalore"));

    private final NamespacedKey key;

    Bricks(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
