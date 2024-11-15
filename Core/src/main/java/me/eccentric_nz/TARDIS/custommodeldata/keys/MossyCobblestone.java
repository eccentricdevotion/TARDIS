package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum MossyCobblestone {

    GRAVITY(new NamespacedKey(TARDIS.plugin, "item/gui/room/gravity"));

    private final NamespacedKey key;

    MossyCobblestone(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
